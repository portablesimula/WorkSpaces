/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.expression;

import java.lang.classfile.CodeBuilder;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.syntaxClass.declaration.SimpleVariableDeclaration;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.token.CharacterConst;
import simula.token.Identifier;
import simula.token.IntegerConst;
import simula.token.RealConst;

/// Expression.
/// 
/// <pre>
/// Simula Standard: Chapter 3 Expressions
/// 
///   expression
///      = value-expression
///      | reference-expression
///      | designational-expression
///      
///   value-expression
///      = arithmetic-expression
///      | Boolean-expression
///      | character-expression
///      
///   reference-expression
///      = object-expression
///      | text-expression
/// </pre>
/// 
/// <h2>Syntax used during Parsing:</h2>
/// 
/// <pre>
/// Expression  =  SimpleExpression
/// 	           |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
/// 
///  SimpleExpression  =  BooleanTertiary  { OR ELSE  BooleanTertiary }
///  BooleanTertiary   =  Equivalence  { AND THEN  Equivalence }
///  Equivalence       =  Implication  { EQV  Implication }
///  Implication       =  BooleanTerm  { IMP  BooleanTerm }
///  BooleanTerm       =  BooleanFactor  { OR  BooleanFactor }
///  BooleanFactor     =  BooleanSecondary  { AND  BooleanSecondary }
///  BooleanSecondary  =  [ NOT ]  BooleanPrimary
///  BooleanPrimary    =  TextPrimary  { & TextPrimary }
///  TextPrimary       =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
///       RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
///  SimpleArithmeticExpression  =  [ + | - ]  Term  {  ( + | - )  Term }
///  Term    =  Factor  {  ( * | / | // )  Factor }
///  Factor  =  BasicExpression  { **  BasicExpression }
///              
///  
///  BasicExpression  =  PrimaryExpression  |  {  RemoteIdentifier  |  ObjectRelation  |  QualifiedObject   }
/// 		RemoteIdentifier =  PrimaryExpression  .  AttributeIdentifier
/// 		ObjectRelation   =  PrimaryExpression ( IS | IN )  ClassIdentifier
/// 		QualifiedObject  =  PrimaryExpression  QUA  ClassIdentifier
/// 
///  PrimaryExpression =  ( Expression ) | Constant | ObjectGenerator | LocalObject | Variable | SubscriptedVariable
/// 		Constant = IntegerConstant | RealConstant | CharacterConstant | TextConstant | BooleanConstant | SymbolicValue  
/// 				BooleanConstant = TRUE | FALSE
/// 				SymbolicValue   = NONE | NOTEXT
///                ... other constants as delivered by the scanner
/// 		ObjectGenerator  =  NEW  Identifier  "("  Expression  {  ,  Expression  }  ")"
/// 		LocalObject = THIS ClassIdentifier
/// 		Variable  =  Identifier
/// 		SubscriptedVariable  =  Identifier  "("  Expression  {  ,  Expression  }  ")"
///   
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/expression/Expression.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
/// @author Stein Krogdahl
public abstract class Expression extends SyntaxClass {

//	private static final boolean TEST1 = false;
//	private static final boolean TEST2 = false;
//	private static final boolean TEST3 = false;//true;
//	private static final boolean TEST4 = false;
	private static final boolean TEST5 = true;

	/// The type
	public Type type = null;
	
	/// This Expression is part of  backLink Expression/Statement.
	public SyntaxClass backLink;

	/// Expression.
	public Expression(){}

  

	/// Accept expression.
	/// <pre>
	/// Expression  =  SimpleExpression
	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
	/// </pre>
	/// @return Expression or null if no expression is found.
	public static Expression acceptExpression(PsiBuilder psiBuilder) {
		if(PsiParse.accept(psiBuilder, KeyWord.IF)) {
			Expression condition=acceptExpression(psiBuilder);
			PsiParse.expect(psiBuilder, KeyWord.THEN);
				psiBuilder.startSubtree("Expression");
				Expression thenExpression=acceptSimpleExpression(psiBuilder);
			PsiParse.expect(psiBuilder, KeyWord.ELSE);
				Expression elseExpression=acceptExpression(psiBuilder);
			Expression expr=new ConditionalExpression(Type.Boolean,condition,thenExpression,elseExpression);
			if(Option.internal.TRACE_PARSE) Util.TRACE("Expression: ParseExpression, result="+expr);
//			if(true) throw new RuntimeException("Expression.acceptExpression: NOT IMPL: "+expr);
			psiBuilder.doneSubtree(expr);
			return expr;
		} else {
			psiBuilder.startSubtree("Expression");
			if(Option.TRACE_ACCEPT_EXPRESSION > 0) IO.println("Expression.acceptExpression: ZZZZZZZZZZZZZZZZZZZZZZZZZZ   BEGIN: ");
			Expression expr= acceptSimpleExpression(psiBuilder);
			if(Option.TRACE_ACCEPT_EXPRESSION > 0) IO.println("Expression.acceptExpression: ZZZZZZZZZZZZZZZZZZZZZZZZZZ   RESULT: "+expr);
			psiBuilder.doneSubtree(expr);
			return expr;
		}
	} 
	
	/// Expect expression.
	/// <pre>
	/// Expression	=  SimpleExpression
	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
	/// </pre>
	/// If no expression is found an error message is printed.
	/// @return Expression or null if no expression is found.
	public static Expression expectExpression(PsiBuilder psiBuilder) {
//		IO.println("Expression.expectExpression: BEGIN");
		Expression expr=acceptExpression(psiBuilder);
		if(expr==null) Util.error("Expecting Expression");
//		IO.println("Expression.expectExpression: END: expr="+expr);
		return(expr);
	}

	/// Parse simple expression.
	/// <pre>
	///  SimpleExpression  =  BooleanTertiary  { OR ELSE  BooleanTertiary }
	///  BooleanTertiary   =  Equivalence  { AND THEN  Equivalence }
	///  Equivalence       =  Implication  { EQV  Implication }
	///  Implication       =  BooleanTerm  { IMP  BooleanTerm }
	///  BooleanTerm       =  BooleanFactor  { OR  BooleanFactor }
	///  BooleanFactor     =  BooleanSecondary  { AND  BooleanSecondary }
	///  BooleanSecondary  =  [ NOT ]  BooleanPrimary
	///  BooleanPrimary    =  TextPrimary  { & TextPrimary }
	///  TextPrimary       =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
	///       RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	///  SimpleArithmeticExpression  =  [ + | - ]  Term  {  ( + | - )  Term }
	///  Term    =  Factor  {  (/// | / | // )  Factor }
	///  Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>             
	///        
	/// @return Expression or null if no expression is found.
	private static Expression acceptSimpleExpression(PsiBuilder psiBuilder)  {   
		psiBuilder.startSubtree("SimpleExpression");
		Expression expr = acceptANDTHEN(psiBuilder);
		while(PsiParse.accept_OR_ELSE(psiBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.OR_ELSE,acceptANDTHEN(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptSimpleExpression");
		}
		psiBuilder.doneSubtree(expr);
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND THEN.
	/// <pre>
	/// BooleanTertiary =  Equivalence  { AND THEN  Equivalence }
	/// </pre>
	/// @return an expression
	private static Expression acceptANDTHEN(PsiBuilder psiBuilder) {
		Expression expr = acceptEQV(psiBuilder);
		while(PsiParse.accept_AND_THEN(psiBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.AND_THEN,acceptEQV(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptANDTHEN");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean EQV.
	/// <pre>
	/// Equivalence  =  Implication  { EQV  Implication }
	/// </pre>
	/// @return an expression
	private static Expression acceptEQV(PsiBuilder psiBuilder) { 
		Expression expr=acceptIMP(psiBuilder);
		while(PsiParse.accept(psiBuilder, KeyWord.EQV)) {
			expr=new BooleanExpression(expr,KeyWord.EQV,acceptIMP(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptEQV");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean IMP.
	/// <pre>
	/// Implication =  BooleanTerm  { IMP  BooleanTerm }
	/// </pre>
	/// @return an expression
	private static Expression acceptIMP(PsiBuilder psiBuilder) {
		Expression expr=acceptOR(psiBuilder);
		while(PsiParse.accept(psiBuilder, KeyWord.IMP)) {
			expr=new BooleanExpression(expr,KeyWord.IMP,acceptOR(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptIMP");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean OR.
	/// <pre>
	/// BooleanTerm  =  BooleanFactor  { OR  BooleanFactor }
	/// </pre>
	/// @return an expression
	private static Expression acceptOR(PsiBuilder psiBuilder) {
		Expression expr=acceptAND(psiBuilder);
		while(PsiParse.accept_OR_ONLY(psiBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.OR,acceptAND(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptOR");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND.
	/// <pre>
	/// BooleanFactor =  BooleanSecondary  { AND  BooleanSecondary }
	/// </pre>
	/// @return an expression
	private static Expression acceptAND(PsiBuilder psiBuilder) {
		Expression expr=acceptNOT(psiBuilder);
		while(PsiParse.accept_AND_ONLY(psiBuilder)) {
			IO.println("Expression.acceptAND: ");
			expr=new BooleanExpression(expr,KeyWord.AND,acceptNOT(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptAND");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean NOT.
	/// <pre>
	/// BooleanSecondary  =  [ NOT ]  BooleanPrimary
	/// </pre>
	/// @return an expression
	private static Expression acceptNOT(PsiBuilder psiBuilder) {
		Expression expr;
		if(PsiParse.accept(psiBuilder, KeyWord.NOT)) {
			expr=UnaryOperation.create(KeyWord.NOT,acceptTEXTCONC(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptNOT");
		} else {
			expr = acceptTEXTCONC(psiBuilder);
		}
		return(expr);
	}

	/// Parse Utility: Accept text concatenation.
	/// <pre>
	/// BooleanPrimary  =  TextPrimary  { & TextPrimary }
	/// </pre>
	/// @return an expression
	private static Expression acceptTEXTCONC(PsiBuilder psiBuilder) {
		Expression expr=acceptRelation(psiBuilder);
		while(PsiParse.accept(psiBuilder, KeyWord.AMPERSAND)) {
			expr=new TextExpression(expr,acceptRelation(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptTEXTCONC");
		}
		return(expr);
	}

	/// Parse Utility: Accept relation.
	/// <pre>
	/// TextPrimary =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
	///    RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	/// </pre>
	/// @return an expression
	private static Expression acceptRelation(PsiBuilder psiBuilder) {
		Expression expr = acceptAdditiveOperation(psiBuilder);
		LexToken prevToken = null;
		if((prevToken = PsiParse.acceptRelationalOperator(psiBuilder)) != null)   { 
			int opr = prevToken.keyWord;
			expr = new RelationalOperation(expr,opr,acceptAdditiveOperation(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptRelation");
		}
		return(expr);
	}

	/// Parse Utility: Accept additive operation.
	/// <pre>
	/// SimpleArithmeticExpression  =  UnaryTerm  {  ( + | - )  Term }
	/// </pre>
	/// @return an expression
	private static Expression acceptAdditiveOperation(PsiBuilder psiBuilder) {
		Expression expr=acceptUNIMULDIV(psiBuilder);
		LexToken accepted = null;
		while( (accepted = PsiParse.acceptParserToken(psiBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) { 
			int opr=accepted.keyWord;
			expr=ArithmeticExpression.create(expr,opr,acceptMULDIV(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptAdditiveOperation");
		}
		return(expr);
	}

	/// Parse Utility: Accept unary plus and minus.
	/// <pre>
	/// UnaryTerm  =  [ + | - ]  Term
	/// </pre>
	/// @return an expression
	private static Expression acceptUNIMULDIV(PsiBuilder psiBuilder) {
		Expression expr;
		LexToken prevToken = null;
		if((prevToken = PsiParse.acceptParserToken(psiBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) {
			int opr=prevToken.keyWord;
			if(opr==KeyWord.PLUS) expr=acceptMULDIV(psiBuilder);
			else {
				expr=UnaryOperation.create(opr,acceptMULDIV(psiBuilder));
				psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptUNIMULDIV");
			}
		} else {
			expr = acceptMULDIV(psiBuilder);
		}
		return(expr);
	}

	/// Parse Utility: Accept multiplicative operation.
	/// <pre>
	/// Term  =  Factor  {  ( * | / | // )  Factor }
	/// </pre>
	/// @return an expression
	private static Expression acceptMULDIV(PsiBuilder psiBuilder) {
		Expression expr=acceptEXPON(psiBuilder);
		LexToken accepted = null;
		while((accepted = PsiParse.acceptParserToken(psiBuilder, KeyWord.MUL,KeyWord.DIV,KeyWord.INTDIV)) != null) {
			int opr = accepted.keyWord;
			expr = ArithmeticExpression.create(expr,opr,acceptEXPON(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptMULDIV");
		}
		return(expr);
	}

	/// Parse Utility: Accept exponentiation.
	/// <pre>
	/// Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>
	/// @return an expression
	private static Expression acceptEXPON(PsiBuilder psiBuilder) {
		Expression expr=acceptBASICEXPR(psiBuilder);
		while(PsiParse.accept(psiBuilder, KeyWord.EXP)) {
			expr = ArithmeticExpression.create(expr,KeyWord.EXP,acceptBASICEXPR(psiBuilder));
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptEXPON");
		}
		return(expr);
	}


	/// Parse basic expression.
	/// <pre>
	///  BasicExpression  =  PrimaryExpression  |  {  RemoteIdentifier  |  ObjectRelation  |  QualifiedObject   }
	/// 		RemoteIdentifier =  PrimaryExpression  .  AttributeIdentifier
	/// 		ObjectRelation   =  PrimaryExpression ( IS | IN )  ClassIdentifier
	/// 		QualifiedObject  =  PrimaryExpression  QUA  ClassIdentifier
	/// 
	///  PrimaryExpression =  ( Expression ) | Constant | ObjectGenerator | LocalObject | Variable | SubscriptedVariable
	/// 		Constant = IntegerConstant | RealConstant | CharacterConstant | TextConstant | BooleanConstant | SymbolicValue  
	/// 				BooleanConstant = TRUE | FALSE
	/// 				SymbolicValue   = NONE | NOTEXT
	///                ... other constants as delivered by the scanner
	/// 		ObjectGenerator  =  NEW  Identifier  "("  Expression  {  ,  Expression  }  ")"
	/// 		LocalObject = THIS ClassIdentifier
	/// 		Variable  =  Identifier
	/// 		SubscriptedVariable  =  Identifier  "("  Expression  {  ,  Expression  }  ")"
	/// </pre>
	/// @return Expression or null if no expression is found.
	private static Expression acceptBASICEXPR(PsiBuilder psiBuilder) {
		// Dette er vel kanskje det samme som “primary”?
		// Merk: Alt som kan stå foran et postfix (DOT, IS, IN og QUA) må være et BASICEXPR
		if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptExpression");
//		if(TEST1) psiBuilder.startSubtree(Expression.class, "acceptBASICEXPR");
//		if(TEST2) psiBuilder.startSubtree(Expression.class, "acceptBASICEXPR");
		Expression expr=null;
		LexToken prevToken = PsiParse.getParserToken(psiBuilder);
		if(PsiParse.accept(psiBuilder, KeyWord.BEGPAR)) { expr = acceptExpression(psiBuilder); PsiParse.expect(psiBuilder, KeyWord.ENDPAR); }
		else if(PsiParse.accept(psiBuilder, KeyWord.INTEGERKONST)) expr = new Constant(Type.Integer,((IntegerConst)prevToken).value);
		else if(PsiParse.accept(psiBuilder, KeyWord.REALKONST)) expr = Constant.createRealType(((RealConst)prevToken).value);

//		else if(PsiParse.accept(psiBuilder, KeyWord.BOOLEANKONST)) expr = new Constant(Type.Boolean,((IntegerConst)prevToken).value);
		else if(PsiParse.accept(psiBuilder, KeyWord.TRUE)) expr = new Constant(Type.Boolean,true);
		else if(PsiParse.accept(psiBuilder, KeyWord.FALSE)) expr = new Constant(Type.Boolean,false);

		else if(PsiParse.accept(psiBuilder, KeyWord.CHARACTERKONST)) expr = new Constant(Type.Character,((CharacterConst)prevToken).value);
		else if(PsiParse.accept(psiBuilder, KeyWord.TEXTKONST)) expr = new Constant(Type.Text,psiBuilder.getTextString(prevToken));
		else if(PsiParse.accept(psiBuilder, KeyWord.NONE)) expr = new Constant(Type.Ref,null);
		else if(PsiParse.accept(psiBuilder, KeyWord.NOTEXT)) expr = new Constant(Type.Text,null);
		else if(PsiParse.accept(psiBuilder, KeyWord.NEW)) expr = ObjectGenerator.expectNew(psiBuilder);
		else if(PsiParse.accept(psiBuilder, KeyWord.THIS)) expr = LocalObject.expectThisIdentifier(psiBuilder); 
		else {
//			String ident=PsiParse.acceptIdentifier(psiBuilder);
			
//			public static String acceptIdentifier(final PsiBuilder psiBuilder) {
			LexToken prevToken2 = PsiParse.getParserToken(psiBuilder) ;
			LexToken token = null;
			if ((token = PsiParse.acceptParserToken(psiBuilder, KeyWord.IDENTIFIER)) != null) {
				String ident = ((Identifier)token).value;
				expr=VariableExpression.expectVariable(psiBuilder, ident);
			}
//				return (null);
//			}

//			if(ident!=null) expr=VariableExpression.expectVariable(ident);
			else {
//				if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptBASICEXPR returns: NULL, prevKeyword="+PsiParse.prevToken.getKeyWord());
				if(prevToken2.keyWord == KeyWord.SEMICOLON) PsiParse.skipMisplacedCurrentSymbol(psiBuilder); // Ad'Hoc
				return(null);
			}
		}
		psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptBASICEXPR");
		// Then there can be a sequence of postfixes, which builds a tree “upwards to the right”
		while ((prevToken = PsiParse.acceptPostfixOprator(psiBuilder)) != null) {
			int opr = prevToken.keyWord; // opr == DOT || opr== IS || opr == IN || opr == QUA
			if (opr == KeyWord.DOT ) 
				expr=new RemoteVariable(expr,expectVariable(psiBuilder));
			else {  // opr == IS or opr == IN or opr == QUA.  Then a class identifier must follow.
				String classIdentifier=PsiParse.expectIdentifier(psiBuilder);
				if(opr==KeyWord.QUA)
					expr=new QualifiedObject(expr,classIdentifier);
				else expr=new ObjectRelation(expr,opr,classIdentifier);
			}
			psiBuilder.doneSubtree(expr); psiBuilder.startSubtree("acceptPOSTFIX");
		}
//		if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptBasicExpression returns: "+expr);
		return(expr);
	}
  
	/// Parse Utility: Expect Variable
	/// <pre>
	/// Variable  =  Identifier  |  SubscriptedVariable
	/// 	SubscriptedVariable  =  Identifier  "("  Expression  {  ,  Expression  }  ")"
	/// </pre>
	/// NOTE: That a SubscriptedVariable may be an subscripted array or a function designator.
	/// @return the created Variable
	private static VariableExpression expectVariable(PsiBuilder psiBuilder) { 
		// An identifier, possibly followed by arguments in parentheses.
		String ident=PsiParse.expectIdentifier(psiBuilder);
		return(VariableExpression.expectVariable(psiBuilder, ident));
	}

	/// Get a writeable variable.
	/// 
	/// This method is redefined in Variable, RemoteVariable and TypeConversion.
	/// @return a writeable variable or null
	public VariableExpression getWriteableVariable() { return(null); } 

	/// Returns the qualification of the given simpleObjectExpression.
	/// @param expr simpleObjectExpression
	/// @return  the qualification of the given simpleObjectExpression
	private static ClassDeclaration getQualification(final Expression expr) {
		String refIdent=expr.type.getRefIdent();
		Declaration objDecl = Global.getCurrentScope().findMeaning(refIdent).declaredAs;
		if(objDecl instanceof ClassDeclaration cls)	return(cls);
		Util.error("Illegal ref(" + refIdent + "): " + refIdent + " is not a class");
		return(null);
	}

	/// Get qualification.
	/// @param classIdentifier a class identifier
	/// @return the ClassDeclaration with same identifier
	public static ClassDeclaration getQualification(final String classIdentifier) {
		Declaration classDecl=Global.getCurrentScope().findMeaning(classIdentifier).declaredAs;
		if(classDecl instanceof ClassDeclaration cls) return(cls);
		Util.error("Illegal: " + classIdentifier + " is not a class");
		return(null);
	}

	/// Check compatibility between simpleObjectExpression and a classDeclaration.
	/// @param simpleObjectExpression a simple object expression
	/// @param classIdentifier a class identifier
	/// @return true if compatible, otherwise false
	public static boolean checkCompatibility(final Expression simpleObjectExpression,final String classIdentifier) {
		ClassDeclaration objDecl=getQualification(simpleObjectExpression);
		ClassDeclaration quaDecl=getQualification(classIdentifier);
		if(quaDecl==objDecl) ; // Nothing: Util.warning("Unneccessary QUA/IS/IN "+ classIdentifier);
		else if(quaDecl==null) Util.error("Illegal QUA -- " + classIdentifier + " is not a class");
		else if(!(objDecl.isCompatibleClasses(quaDecl))) return(false);
		return(true);
	}

	/// Try to Compile-time Evaluate this expression
	/// @return the resulting evaluated expression
	public Expression evaluate() { return(this); }

	/// Returns true if this expression may be used as a statement.
	/// @return true if this expression may be used as a statement
	public abstract boolean maybeStatement();

	/// Generate code for getting the value of this Expression
	/// @return the resulting Java code
	protected String get() {
		return (this.toJavaCode());
	}

	/// Generate code for putting the value into this Expression
	/// @param rhs a evaluated expression.
	/// @return the resulting Java code
	String put(String rhs) {
		return (this.toJavaCode() + '=' + rhs);
	}
	
	/// Try to evaluate this expression to a number.
	/// @return the resulting number or null
    public Number getNumber() {
    	if(this instanceof UnaryOperation u) {
    		if(u.oprator==KeyWord.MINUS) {
    			Number val=u.operand.getNumber();
    			if(val!=null) return(-val.intValue());
    		}
    	} else if(this instanceof Constant cnst) {
		    if(cnst.value instanceof Number num) return(num);
	    } else if(this instanceof VariableExpression) {
		    return(null);
	    } else if(this instanceof TypeConversion conv) {
	    	return(conv.expression.getNumber()); // Hva hvis   (int)3.14  som real
	    }
	    return(null);
    }
	
	/// Try to evaluate this expression to an integer.
	/// @return the resulting int or 0
	public int getInt() {
		if(this instanceof Constant cnst) {
			if(cnst.value instanceof Number num)	return(num.intValue());
			if(cnst.value instanceof Character chr) return((int)chr.charValue());
		}
		if(this instanceof VariableExpression var) {
			Meaning meaning=var.meaning;
			Declaration declaredAs=meaning.declaredAs;
			if(declaredAs instanceof SimpleVariableDeclaration tp) {
				Expression constElt=tp.constantElement;
				if(constElt!=null) {
					if(constElt instanceof Constant constant) {
						Object value=constant.value;
						if(value instanceof Number num)	return(num.intValue());
						if(value instanceof Character chr) return((int)chr.charValue());
					}
				}
			}
		}
		Util.IERR("Expression: "+this+" is not a Constant");
		return(0);
	}
	
	/// ClassFile coding utility: Build Evaluation Code.
	/// @param rightPart expression
	/// @param codeBuilder the codeBuilder used.
	public abstract void buildEvaluation(Expression rightPart,CodeBuilder codeBuilder);


	@Override
	public void printTree(final int indent, final Object head) {
		IO.println(edTreeIndent(indent)+this);
	}
	
	@Override
	public String toString() { return("NO EXPRESSION"); }

	    
}
