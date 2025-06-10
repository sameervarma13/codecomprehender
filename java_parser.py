# java_parser.py
import javalang


def extract_methods_with_lines(code: str):
    """
    Extracts methods and their starting lines.
    Returns a list of (start_line, method_name, raw_code_block).
    """
    try:
        tree = javalang.parse.parse(code)
    except Exception as e:
        print(f"Error parsing Java code: {e}")
        return []

    lines = code.splitlines()
    methods = []

    for _, node in tree.filter(javalang.tree.MethodDeclaration):
        start = node.position.line - 1 if node.position else None
        if start is None:
            continue

        # Find method end by counting braces
        brace_count = 0
        method_lines = []
        for i in range(start, len(lines)):
            line = lines[i]
            method_lines.append(line)
            brace_count += line.count("{") - line.count("}")
            if brace_count <= 0 and "}" in line:
                break

        raw_code = "\n".join(method_lines)
        methods.append((start, node.name, raw_code))

    return methods


def extract_class_structure(code: str):
    """
    Extracts class name and imported packages from Java code.
    Returns a dictionary with keys: 'class_name', 'imports', 'extends', 'implements'.
    """
    try:
        tree = javalang.parse.parse(code)
    except Exception as e:
        print(f"Error parsing Java code: {e}")
        return {
            "class_name": None,
            "imports": [],
            "extends": None,
            "implements": []
        }

    structure = {
        "class_name": None,
        "imports": [],
        "extends": None,
        "implements": []
    }

    # Extract class information
    for path, node in tree.filter(javalang.tree.ClassDeclaration):
        structure["class_name"] = node.name
        structure["extends"] = node.extends.name if node.extends else None
        structure["implements"] = [impl.name for impl in node.implements] if node.implements else []
        break  # Assume one primary class per file

    # Extract imports
    for path, node in tree.filter(javalang.tree.Import):
        structure["imports"].append(node.path)

    return structure