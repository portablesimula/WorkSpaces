package simula.core.builder.export;

public final class SimulaTokenTypes {
	  public static final String Keyword = SemanticTokenTypes.Keyword;
	  public static final String Symbol = SemanticTokenTypes.Keyword;
	  public static final String WhiteSpace = SemanticTokenTypes.Keyword;

	  public static final String Class = SemanticTokenTypes.Class;
	  public static final String Attribute =  SemanticTokenTypes.Property;
	  
	  public static final String Procedure = SemanticTokenTypes.Method;
//	  public static final String Procedure = SemanticTokenTypes.Function;
//	  public static final String Procedure = SemanticTokenTypes.Macro;
	  public static final String Parameter = SemanticTokenTypes.Parameter;

	  public static final String Identifier = SemanticTokenTypes.Variable;
	  public static final String Variable = SemanticTokenTypes.Variable;

	  public static final String Comment = SemanticTokenTypes.Comment;

	  public static final String String = SemanticTokenTypes.String;
	  public static final String Character = SemanticTokenTypes.String;

	  public static final String Number = SemanticTokenTypes.Number;

	  public static final String Operator = SemanticTokenTypes.Operator;

	  public static final String Label = SemanticTokenTypes.Label;
	  

//	  public static final String Namespace = "namespace";
//
//	  /**
//	   * Represents a generic type. Acts as a fallback for types which
//	   * can't be mapped to a specific type like class or enum.
//	   */
//	  public static final String Type = "type";
//
//	  public static final String EnumMember = "enumMember";
//
//	  public static final String Event = "event";
//
//	  public static final String Modifier = "modifier";
//
//	  public static final String Regexp = "regexp";
//
//	  public static final String Decorator = "decorator";

	  private SimulaTokenTypes() {
	  }
	}
