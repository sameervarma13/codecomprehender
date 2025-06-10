# main.py
import os
import shutil
import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
import time

from java_parser import extract_methods_with_lines
from comment_generator import batch_generate_comments_sync
from architecture_generator import analyze_repo_architecture, architecture_to_markdown
from visual_architecture import save_visual_architecture, print_visual_architecture


def validate_repo_path(repo_path: str) -> bool:
    """Validate that the repository path exists and contains Java files."""
    if not os.path.exists(repo_path):
        print(f"Repository path does not exist: {repo_path}")
        return False
    
    java_files = []
    for root, _, files in os.walk(repo_path):
        java_files.extend([f for f in files if f.endswith('.java')])
    
    if not java_files:
        print(f"No Java files found in: {repo_path}")
        return False
        
    print(f"Found {len(java_files)} Java files")
    return True


def copy_repo(src_path, dest_path):
    """Copy repository to destination, removing existing if present."""
    if os.path.exists(dest_path):
        shutil.rmtree(dest_path)
    shutil.copytree(src_path, dest_path)
    print(f"Repo copied to: {dest_path}")


def get_java_files(dest_repo_root):
    """Get all Java files to process."""
    java_files = []
    for root, _, files in os.walk(dest_repo_root):
        for file in files:
            if file.endswith(".java") and not file.endswith("_commented.java"):
                full_path = os.path.join(root, file)
                java_files.append(full_path)
    return java_files


def process_java_files_parallel(dest_repo_root, max_workers=4):
    """Process Java files in parallel using ThreadPoolExecutor."""
    java_files = get_java_files(dest_repo_root)
    
    if not java_files:
        print("No Java files to process")
        return
    
    processed_count = 0
    error_count = 0
    start_time = time.time()
    
    print(f"Processing {len(java_files)} files with {max_workers} workers...")
    
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        # Submit all tasks
        future_to_file = {}
        for filepath in java_files:
            base_name = os.path.splitext(os.path.basename(filepath))[0]
            output_path = os.path.join(os.path.dirname(filepath), f"{base_name}_commented.java")
            future = executor.submit(comment_methods_in_file_optimized, filepath, output_path)
            future_to_file[future] = filepath
        
        # Process completed tasks
        for future in as_completed(future_to_file):
            filepath = future_to_file[future]
            try:
                future.result()
                processed_count += 1
                if processed_count % 5 == 0:  # Progress update every 5 files
                    elapsed = time.time() - start_time
                    rate = processed_count / elapsed
                    print(f"Progress: {processed_count}/{len(java_files)} files ({rate:.1f} files/sec)")
            except Exception as e:
                print(f"Error processing {filepath}: {e}")
                error_count += 1
    
    elapsed = time.time() - start_time
    print(f"Processed {processed_count} Java files in {elapsed:.1f}s ({error_count} errors)")


def comment_methods_in_file_optimized(filepath, output_path):
    """Optimized version that batches API calls per file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        original_lines = f.readlines()

    code = "".join(original_lines)
    methods = extract_methods_with_lines(code)

    if not methods:
        # Still create the file, just copy it
        shutil.copy2(filepath, output_path)
        return

    # Extract all method codes for batch processing
    method_codes = [raw_code for _, _, raw_code in methods]
    
    try:
        # Batch generate all comments for this file
        class_comment, method_comments = batch_generate_comments_sync(method_codes, code)
        
        new_lines = original_lines[:]
        offset = 0

        # Add class-level JavaDoc
        if class_comment:
            for i, line in enumerate(new_lines):
                if "public class" in line or "class " in line:
                    comment_block = [f"{l}\n" for l in class_comment.splitlines()]
                    new_lines[i:i] = comment_block + ["\n"]
                    offset += len(comment_block) + 1
                    break

        # Add JavaDoc above each method
        for i, (start_line, method_name, _) in enumerate(methods):
            if i < len(method_comments) and method_comments[i] and not isinstance(method_comments[i], Exception):
                comment = method_comments[i]
                comment_block = [f"{line}\n" for line in comment.splitlines()]
                insert_index = start_line + offset
                new_lines[insert_index:insert_index] = comment_block + ["\n"]
                offset += len(comment_block) + 1

        with open(output_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)

    except Exception as e:
        print(f"Batch processing error for {filepath}: {e}")
        # Fallback: just copy the file
        shutil.copy2(filepath, output_path)


def comment_methods_in_file(filepath, output_path):
    """Original method kept for compatibility."""
    comment_methods_in_file_optimized(filepath, output_path)


def process_java_files_in_place(dest_repo_root):
    """Updated to use parallel processing."""
    process_java_files_parallel(dest_repo_root)


def generate_architecture_report(repo_path: str, output_base: str, repo_name: str):
    """Generate and save architecture report as markdown."""
    try:
        arch_data = analyze_repo_architecture(repo_path)
        markdown = architecture_to_markdown(arch_data)
        arch_path = os.path.join(output_base, f"{repo_name}_architecture.md")
        
        os.makedirs(output_base, exist_ok=True)
        
        with open(arch_path, "w", encoding='utf-8') as f:
            f.write(markdown)
        print(f"Architecture report saved to: {arch_path}")
    except Exception as e:
        print(f"Error generating architecture report: {e}")


def print_architecture_inline(repo_path: str):
    """Print architecture analysis to console."""
    try:
        arch_data = analyze_repo_architecture(repo_path)
        markdown = architecture_to_markdown(arch_data)
        print("\nProject Architecture\n")
        print(markdown)
    except Exception as e:
        print(f"Error analyzing architecture: {e}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="CodeComprehender - Java codebase analyzer and commenter")
    parser.add_argument("--repo", required=True, help="Path to Java repo to process")
    parser.add_argument("--out", default="output", help="Output base folder")
    parser.add_argument("--arch-report", action="store_true", help="Generate architecture report (Markdown)")
    parser.add_argument("--visual", action="store_true", help="Generate visual architecture diagrams")
    parser.add_argument("--architecture", action="store_true", help="Print class architecture to console")
    parser.add_argument("--workers", type=int, default=4, help="Number of parallel workers (default: 4)")

    args = parser.parse_args()

    # Validate input
    if not validate_repo_path(args.repo):
        exit(1)

    repo_name = os.path.basename(os.path.abspath(args.repo.rstrip("/")))
    output_repo_path = os.path.join(args.out, f"{repo_name}_commented")

    # Ensure output directory exists
    os.makedirs(args.out, exist_ok=True)

    # Copy repo and process files
    copy_repo(args.repo, output_repo_path)
    process_java_files_parallel(output_repo_path, max_workers=args.workers)

    # Generate reports using ORIGINAL repo (not commented version)
    if args.arch_report:
        generate_architecture_report(args.repo, args.out, repo_name)

    if args.visual:
        save_visual_architecture(args.repo, args.out, repo_name)

    if args.architecture:
        print_visual_architecture(args.repo)