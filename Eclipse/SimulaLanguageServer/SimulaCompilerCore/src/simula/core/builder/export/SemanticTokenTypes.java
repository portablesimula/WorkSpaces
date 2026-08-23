package simula.core.builder.export;

public final class SemanticTokenTypes {
	  public static final String Namespace = "namespace";

	  /**
	   * Represents a generic type. Acts as a fallback for types which
	   * can't be mapped to a specific type like class or enum.
	   */
	  public static final String Type = "type";

	  public static final String Class = "class";

	  public static final String Enum = "enum";

	  public static final String Interface = "interface";

	  public static final String Struct = "struct";

	  public static final String TypeParameter = "typeParameter";

	  public static final String Parameter = "parameter";

	  public static final String Variable = "variable";

	  public static final String Property = "property";

	  public static final String EnumMember = "enumMember";

	  public static final String Event = "event";

	  public static final String Function = "function";

	  public static final String Method = "method";

	  public static final String Macro = "macro";

	  public static final String Keyword = "keyword";

	  public static final String Modifier = "modifier";

	  public static final String Comment = "comment";

	  public static final String String = "string";

	  public static final String Number = "number";

	  public static final String Regexp = "regexp";

	  public static final String Operator = "operator";

	  public static final String Decorator = "decorator";

	  public static final String Label = "label";

	  private SemanticTokenTypes() {
	  }
	}
