# architecture_generator.py
import os
from java_parser import extract_class_structure


def analyze_repo_architecture(repo_path: str):
    """
    Analyze Java repository and extract class architecture information.
    
    Args:
        repo_path: Path to the Java repository
        
    Returns:
        List of dictionaries containing class information
    """
    architecture = []

    for root, _, files in os.walk(repo_path):
        for file in files:
            if file.endswith(".java"):
                full_path = os.path.join(root, file)
                try:
                    with open(full_path, 'r', encoding='utf-8') as f:
                        code = f.read()

                    class_info = extract_class_structure(code)
                    if class_info and class_info.get("class_name"):
                        class_info["file_path"] = os.path.relpath(full_path, repo_path)
                        architecture.append(class_info)
                except Exception as e:
                    print(f"⚠️ Error processing {full_path}: {e}")
                    continue

    return architecture


def architecture_to_markdown(architecture: list) -> str:
    """
    Convert architecture data to markdown format.
    
    Args:
        architecture: List of class information dictionaries
        
    Returns:
        Formatted markdown string
    """
    if not architecture:
        return "# Project Architecture\n\nNo Java classes found."
    
    lines = ["# Project Architecture\n"]
    
    # Sort by class name for consistent output
    sorted_arch = sorted(architecture, key=lambda x: x['class_name'])
    
    for cls in sorted_arch:
        lines.append(f"## `{cls['class_name']}`")
        lines.append(f"- **File**: `{cls['file_path']}`")
        
        if cls.get("extends"):
            lines.append(f"- **Extends**: `{cls['extends']}`")
            
        if cls.get("implements") and cls["implements"]:
            impl_list = ", ".join([f"`{impl}`" for impl in cls["implements"]])
            lines.append(f"-  **Implements**: {impl_list}")
            
        if cls.get("imports") and cls["imports"]:
            # Show only non-standard imports (filter out java.lang.* etc.)
            filtered_imports = [
                imp for imp in cls["imports"] 
                if not imp.startswith("java.lang") and not imp.startswith("java.util")
            ]
            if filtered_imports:
                import_list = ", ".join([f"`{imp}`" for imp in filtered_imports[:5]])  # Limit to 5
                if len(filtered_imports) > 5:
                    import_list += f" (and {len(filtered_imports) - 5} more)"
                lines.append(f"- **Key Imports**: {import_list}")
                
        lines.append("")  # Spacer
    
    # Add summary at the end
    lines.append("---")
    lines.append(f"**Total Classes**: {len(architecture)}")
    
    # Count inheritance relationships
    extends_count = sum(1 for cls in architecture if cls.get("extends"))
    implements_count = sum(1 for cls in architecture if cls.get("implements"))
    
    if extends_count > 0:
        lines.append(f"**Classes with Inheritance**: {extends_count}")
    if implements_count > 0:
        lines.append(f"**Classes with Interfaces**: {implements_count}")
    
    return "\n".join(lines)