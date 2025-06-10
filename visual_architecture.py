# visual_architecture.py
import os
from architecture_generator import analyze_repo_architecture


def generate_mermaid_diagram(architecture: list) -> str:
    """
    Generate a Mermaid class diagram from architecture data.
    
    Args:
        architecture: List of class information dictionaries
        
    Returns:
        Mermaid diagram as string
    """
    if not architecture:
        return "```mermaid\nclassDiagram\n    note \"No Java classes found\"\n```"
    
    lines = ["```mermaid", "classDiagram"]
    
    # Define classes
    for cls in architecture:
        class_name = cls['class_name']
        lines.append(f"    class {class_name} {{") 
        
        # Add some basic info as class attributes
        file_name = os.path.basename(cls['file_path'])
        lines.append(f"        +{file_name}")
        lines.append("    }")
    
    lines.append("")
    
    # Add inheritance relationships
    for cls in architecture:
        class_name = cls['class_name']
        
        if cls.get('extends'):
            parent = cls['extends']
            lines.append(f"    {parent} <|-- {class_name}")
        
        if cls.get('implements'):
            for interface in cls['implements']:
                lines.append(f"    {interface} <|.. {class_name}")
    
    lines.append("")
    
    # Add dependency relationships (based on imports)
    for cls in architecture:
        class_name = cls['class_name']
        if cls.get('imports'):
            for imp in cls['imports']:
                # Check if this import refers to another class in our architecture
                imported_class = imp.split('.')[-1]  # Get last part of import
                if any(c['class_name'] == imported_class for c in architecture):
                    lines.append(f"    {class_name} --> {imported_class}")
    
    lines.append("```")
    return "\n".join(lines)


def generate_graphviz_dot(architecture: list) -> str:
    """
    Generate Graphviz DOT format diagram.
    
    Args:
        architecture: List of class information dictionaries
        
    Returns:
        DOT format string
    """
    lines = [
        "digraph Architecture {",
        "    rankdir=TB;",
        "    node [shape=box, style=filled, fillcolor=lightblue];",
        ""
    ]
    
    # Define nodes
    for cls in architecture:
        class_name = cls['class_name']
        file_name = os.path.basename(cls['file_path'])
        lines.append(f'    "{class_name}" [label="{class_name}\\n({file_name})"];')
    
    lines.append("")
    
    # Add relationships
    for cls in architecture:
        class_name = cls['class_name']
        
        # Inheritance
        if cls.get('extends'):
            parent = cls['extends']
            lines.append(f'    "{parent}" -> "{class_name}" [arrowhead=empty, color=blue];')
        
        # Interfaces
        if cls.get('implements'):
            for interface in cls['implements']:
                lines.append(f'    "{interface}" -> "{class_name}" [arrowhead=empty, style=dashed, color=green];')
        
        # Dependencies
        if cls.get('imports'):
            for imp in cls['imports']:
                imported_class = imp.split('.')[-1]
                if any(c['class_name'] == imported_class for c in architecture):
                    lines.append(f'    "{class_name}" -> "{imported_class}" [style=dotted, color=red];')
    
    lines.append("}")
    return "\n".join(lines)


def save_visual_architecture(repo_path: str, output_base: str, repo_name: str):
    """
    Generate and save multiple visual architecture representations.
    
    Args:
        repo_path: Path to the repository
        output_base: Output directory
        repo_name: Name of the repository
    """
    try:
        # Analyze architecture
        arch_data = analyze_repo_architecture(repo_path)
        
        if not arch_data:
            print("No architecture data found")
            return
        
        # Ensure output directory exists
        os.makedirs(output_base, exist_ok=True)
        
        # Generate Mermaid diagram
        mermaid_diagram = generate_mermaid_diagram(arch_data)
        mermaid_path = os.path.join(output_base, f"{repo_name}_architecture_mermaid.md")
        with open(mermaid_path, "w", encoding='utf-8') as f:
            f.write("# Architecture Diagram\n\n")
            f.write(mermaid_diagram)
        print(f"🎨 Mermaid diagram saved to: {mermaid_path}")
        
        # Generate Graphviz DOT
        dot_diagram = generate_graphviz_dot(arch_data)
        dot_path = os.path.join(output_base, f"{repo_name}_architecture.dot")
        with open(dot_path, "w", encoding='utf-8') as f:
            f.write(dot_diagram)
        print(f"🔗 Graphviz DOT file saved to: {dot_path}")
        
        return {
            'mermaid': mermaid_path,
            'dot': dot_path
        }
        
    except Exception as e:
        print(f"Error generating visual architecture: {e}")
        return None


def print_visual_architecture(repo_path: str):
    """Print Mermaid architecture diagram to console."""
    try:
        arch_data = analyze_repo_architecture(repo_path)
        mermaid_diagram = generate_mermaid_diagram(arch_data)
        print("\n" + mermaid_diagram)
    except Exception as e:
        print(f"Error generating visual architecture: {e}")