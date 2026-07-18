/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.expression;

import java.lang.classfile.CodeBuilder;

import simula.builder.SimulaBuilder;
import simula.Option;
import simula.builder.Parse;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.syntaxClass.declaration.SimpleVariableDeclaration;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Util;
import simula.token.CharacterConst;
import simula.token.Identifier;
import simula.token.IntegerConst;
import simula.token.LexToken;
import simula.token.LongRealConst;
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
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/expression/Expression.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
/// @author Stein Krogdahl
public abstract class Expression extends SyntaxElement {

	/// The type
	public Type type = null;
	
	/// This Expression is part of  backLink Expression/Statement.
	public SyntaxElement backLink;

	/// Expression.
	public Expression(final SimulaBuilder simBuilder){
		super(simBuilder);
	}

	/// Accept expression.
	/// <pre>
	/// Expression  =  SimpleExpression
	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
	/// </pre>
	/// 
	/// NOTE: Possible start of an expression. MUST start a LexTokenRange !
	/// 
	/// @return Expression or null if no expression is found.
	public static Expression acceptExpression(SimulaBuilder simBuilder) {
		if(Parse.accept(simBuilder, KeyWord.IF)) {
			Expression condition=acceptExpression(simBuilder);
			Parse.expect(simBuilder, KeyWord.THEN);
				Expression thenExpression=acceptSimpleExpression(simBuilder);
			Parse.expect(simBuilder, KeyWord.ELSE);
				Expression elseExpression=acceptExpression(simBuilder);
			Expression expr=new ConditionalExpression(simBuilder, Type.Boolean, condition, thenExpression, elseExpression);
			if(Option.internal.TRACE_PARSE) Util.TRACE("Expression: ParseExpression, result="+expr);
//			if(true) throw new RuntimeException("Expression.acceptExpression: NOT IMPL: "+expr);
			return expr;
		} else {
			Expression expr= acceptSimpleExpression(simBuilder);
			return expr;
		}
	} 
	
	/// Expect expression.
	/// <pre>
	/// Expression	=  SimpleExpression
	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
	/// </pre>
	/// If no expression is found an error message is printed.
	/// @return Expression or 'MissingExpression' if no expression is found.
	public static Expression expectExpression(SimulaBuilder simBuilder, String kind) {
//		IO.println("Expression.expectExpression: BEGIN");
		Expression expr=acceptExpression(simBuilder);
		if(expr==null) {
//			Util.syntaxError(simBuilder, "Expecting Expression");
			expr = new MissingExpression(simBuilder);
			Util.semanticError(expr, "Expecting " + kind + " Expression");
		}
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
	///  	RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	///  	SimpleArithmeticExpression  =  [ + | - ]  Term  {  ( + | - )  Term }
	///  		Term    =  Factor  {  (* | / | // )  Factor }
	///  			Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>             
	///        
	/// @return Expression or null if no expression is found.
	private static Expression acceptSimpleExpression(SimulaBuilder simBuilder)  { 
		Expression expr = acceptANDTHEN(simBuilder);
		while(Parse.accept_OR_ELSE(simBuilder)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.OR_ELSE, acceptANDTHEN(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND THEN.
	/// <pre>
	/// BooleanTertiary =  Equivalence  { AND THEN  Equivalence }
	/// </pre>
	/// @return an expression
	private static Expression acceptANDTHEN(SimulaBuilder simBuilder) {
		Expression expr = acceptEQV(simBuilder);
		while(Parse.accept_AND_THEN(simBuilder)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.AND_THEN, acceptEQV(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean EQV.
	/// <pre>
	/// Equivalence  =  Implication  { EQV  Implication }
	/// </pre>
	/// @return an expression
	private static Expression acceptEQV(SimulaBuilder simBuilder) { 
		Expression expr=acceptIMP(simBuilder);
		while(Parse.accept(simBuilder, KeyWord.EQV)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.EQV, acceptIMP(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean IMP.
	/// <pre>
	/// Implication =  BooleanTerm  { IMP  BooleanTerm }
	/// </pre>
	/// @return an expression
	private static Expression acceptIMP(SimulaBuilder simBuilder) {
		Expression expr=acceptOR(simBuilder);
		while(Parse.accept(simBuilder, KeyWord.IMP)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.IMP, acceptOR(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean OR.
	/// <pre>
	/// BooleanTerm  =  BooleanFactor  { OR  BooleanFactor }
	/// </pre>
	/// @return an expression
	private static Expression acceptOR(SimulaBuilder simBuilder) {
		Expression expr=acceptAND(simBuilder);
		while(Parse.accept_OR_ONLY(simBuilder)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.OR, acceptAND(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND.
	/// <pre>
	/// BooleanFactor =  BooleanSecondary  { AND  BooleanSecondary }
	/// </pre>
	/// @return an expression
	private static Expression acceptAND(SimulaBuilder simBuilder) {
		Expression expr=acceptNOT(simBuilder);
		while(Parse.accept_AND_ONLY(simBuilder)) {
			expr=new BooleanExpression(simBuilder, expr, KeyWord.AND, acceptNOT(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean NOT.
	/// <pre>
	/// BooleanSecondary  =  [ NOT ]  BooleanPrimary
	/// </pre>
	/// @return an expression
	private static Expression acceptNOT(SimulaBuilder simBuilder) {
		Expression expr;
		if(Parse.accept(simBuilder, KeyWord.NOT)) {
			expr = UnaryOperation.create(simBuilder, KeyWord.NOT, acceptTEXTCONC(simBuilder));
		} else {
			expr = acceptTEXTCONC(simBuilder);
		}
		return(expr);
	}

	/// Parse Utility: Accept text concatenation.
	/// <pre>
	/// BooleanPrimary  =  TextPrimary  { & TextPrimary }
	/// </pre>
	/// @return an expression
	private static Expression acceptTEXTCONC(SimulaBuilder simBuilder) {
		Expression expr=acceptRelation(simBuilder);
		while(Parse.accept(simBuilder, KeyWord.AMPERSAND)) {
			expr=new TextExpression(simBuilder, expr, acceptRelation(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept relation.
	/// <pre>
	/// TextPrimary =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
	///    RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	/// </pre>
	/// @return an expression
	private static Expression acceptRelation(SimulaBuilder simBuilder) {
		Expression expr = acceptAdditiveOperation(simBuilder);
		LexToken prevToken = null;
		if((prevToken = Parse.acceptRelationalOperator(simBuilder)) != null)   { 
			int opr = prevToken.keyWord;
			expr = new RelationalOperation(simBuilder, expr, opr, acceptAdditiveOperation(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept additive operation.
	/// <pre>
	/// SimpleArithmeticExpression  =  UnaryTerm  {  ( + | - )  Term }
	/// </pre>
	/// @return an expression
	private static Expression acceptAdditiveOperation(SimulaBuilder simBuilder) {
		Expression expr=acceptUNIMULDIV(simBuilder);
		LexToken accepted = null;
		while( (accepted = Parse.acceptParserToken(simBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) { 
			int opr=accepted.keyWord;
			expr=ArithmeticExpression.create(simBuilder, expr, opr, acceptMULDIV(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept unary plus and minus.
	/// <pre>
	/// UnaryTerm  =  [ + | - ]  Term
	/// </pre>
	/// @return an expression
	private static Expression acceptUNIMULDIV(SimulaBuilder simBuilder) {
		Expression expr;
		LexToken prevToken = null;
		if((prevToken = Parse.acceptParserToken(simBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) {
			int opr=prevToken.keyWord;
			if(opr==KeyWord.PLUS) expr=acceptMULDIV(simBuilder);
			else {
				expr=UnaryOperation.create(simBuilder, opr,acceptMULDIV(simBuilder));
			}
		} else {
			expr = acceptMULDIV(simBuilder);
		}
		return(expr);
	}

	/// Parse Utility: Accept multiplicative operation.
	/// <pre>
	/// Term  =  Factor  {  ( * | / | // )  Factor }
	/// </pre>
	/// @return an expression
	private static Expression acceptMULDIV(SimulaBuilder simBuilder) {
		Expression expr=acceptEXPON(simBuilder);
		LexToken accepted = null;
		while((accepted = Parse.acceptParserToken(simBuilder, KeyWord.MUL,KeyWord.DIV,KeyWord.INTDIV)) != null) {
			int opr = accepted.keyWord;
			expr = ArithmeticExpression.create(simBuilder, expr, opr, acceptEXPON(simBuilder));
		}
		return(expr);
	}

	/// Parse Utility: Accept exponentiation.
	/// <pre>
	/// Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>
	/// @return an expression
	private static Expression acceptEXPON(SimulaBuilder simBuilder) {
		Expression expr=acceptBASICEXPR(simBuilder);
		while(Parse.accept(simBuilder, KeyWord.EXP)) {
			expr = ArithmeticExpression.create(simBuilder, expr, KeyWord.EXP, acceptBASICEXPR(simBuilder));
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
	private static Expression acceptBASICEXPR(SimulaBuilder simBuilder) {
		// Dette er vel kanskje det samme som “primary”?
		// Merk: Alt som kan stå foran et postfix (DOT, IS, IN og QUA) må være et BASICEXPR
		if(Option.internal.TRACE_PARSE) Parse.TRACE("Expression: acceptExpression");
		Expression expr=null;
		LexToken prevToken = Parse.getCurrentParserToken(simBuilder);
		if(Parse.accept(simBuilder, KeyWord.BEGPAR)) { expr = acceptExpression(simBuilder); Parse.expect(simBuilder, KeyWord.ENDPAR); }
		else if(Parse.accept(simBuilder, KeyWord.INTEGERKONST)) expr = new Constant(simBuilder, Type.Integer,((IntegerConst)prevToken).value);
		else if(Parse.accept(simBuilder, KeyWord.REALKONST)) expr = Constant.createRealType(simBuilder, ((RealConst)prevToken).value);
		else if(Parse.accept(simBuilder, KeyWord.LONGREALKONST)) expr = Constant.createLongRealType(simBuilder, ((LongRealConst)prevToken).value);

//		else if(PsiParse.accept(simBuilder, KeyWord.BOOLEANKONST)) expr = new Constant(Type.Boolean,((IntegerConst)prevToken).value);
		else if(Parse.accept(simBuilder, KeyWord.TRUE)) expr = new Constant(simBuilder, Type.Boolean,true);
		else if(Parse.accept(simBuilder, KeyWord.FALSE)) expr = new Constant(simBuilder, Type.Boolean,false);

		else if(Parse.accept(simBuilder, KeyWord.CHARACTERKONST)) expr = new Constant(simBuilder, Type.Character,((CharacterConst)prevToken).value);
		else if(Parse.accept(simBuilder, KeyWord.TEXTKONST)) expr = new Constant(simBuilder, Type.Text,simBuilder.getTextString(prevToken));
		else if(Parse.accept(simBuilder, KeyWord.NONE)) expr = new Constant(simBuilder, Type.Ref,null);
		else if(Parse.accept(simBuilder, KeyWord.NOTEXT)) expr = new Constant(simBuilder, Type.Text,null);
		else if(Parse.accept(simBuilder, KeyWord.NEW)) expr = ObjectGenerator.expectNew(simBuilder);
		else if(Parse.accept(simBuilder, KeyWord.THIS)) expr = LocalObject.expectThisIdentifier(simBuilder); 
		else {
			LexToken prevToken2 = Parse.getCurrentParserToken(simBuilder) ;
			Identifier ident = Parse.acceptIdentifier(simBuilder);
			if(ident != null) {
				expr=VariableExpression.expectVariable(simBuilder, ident);
			} else {
//				if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptBASICEXPR returns: NULL, prevKeyword="+PsiParse.prevToken.getKeyWord());
				if(prevToken2.keyWord == KeyWord.SEMICOLON) Parse.skipMisplacedCurrentSymbol(simBuilder); // Ad'Hoc
				return(null);
			}
		}
		// Then there can be a sequence of postfixes, which builds a tree “upwards to the right”
		while ((prevToken = Parse.acceptPostfixOprator(simBuilder)) != null) {
			int opr = prevToken.keyWord; // opr == DOT || opr== IS || opr == IN || opr == QUA
			if (opr == KeyWord.DOT ) 
				expr=new RemoteVariable(simBuilder, expr, expectVariable(simBuilder));
			else {  // opr == IS or opr == IN or opr == QUA.  Then a class identifier must follow.
//				String classIdentifier=PsiParse.acceptIdentifier(simBuilder).getText();
				Identifier classIdentifier=Parse.acceptIdentifier(simBuilder);
				if(opr==KeyWord.QUA)
					expr=new QualifiedObject(simBuilder, expr, classIdentifier);
				else expr=new ObjectRelation(simBuilder, expr, opr, classIdentifier);
			}
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
	private static VariableExpression expectVariable(SimulaBuilder simBuilder) { 
		// An identifier, possibly followed by arguments in parentheses.
//		String ident=PsiParse.acceptIdentifier(simBuilder).getText();
//		String ident=Parse.acceptIdentifier(simBuilder).edText();
		Identifier ident = Parse.acceptIdentifier(simBuilder);
		return(VariableExpression.expectVariable(simBuilder, ident));
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
		Identifier refIdent=expr.type.getRefIdent();
		Declaration objDecl = CoreGlobal.getCurrentScope().findMeaning(refIdent).declaredAs;
		if(objDecl instanceof ClassDeclaration cls)	return(cls);
		Util.semanticError(expr, "Illegal ref(" + refIdent + "): " + refIdent + " is not a class");
		return(null);
	}

	/// Get qualification.
	/// @param classIdentifier a class identifier
	/// @return the ClassDeclaration with same identifier
	public static ClassDeclaration getQualification(final Identifier classIdentifier) {
		Declaration classDecl=CoreGlobal.getCurrentScope().findMeaning(classIdentifier).declaredAs;
		if(classDecl instanceof ClassDeclaration cls) return(cls);
		return(null);
	}

	/// Check compatibility between simpleObjectExpression and a classDeclaration.
	/// @param simpleObjectExpression a simple object expression
	/// @param classIdentifier a class identifier
	/// @return true if compatible, otherwise false
	public static boolean checkCompatibility(final Expression simpleObjectExpression,final Identifier classIdentifier) {
		ClassDeclaration objDecl=getQualification(simpleObjectExpression);
		ClassDeclaration quaDecl=getQualification(classIdentifier);
		if(quaDecl == null) {
//			Util.semanticError(simpleObjectExpression, "Illegal: " + classIdentifier + " is not a class");
		} else {
			if(quaDecl==objDecl) ; // Nothing: Util.warning("Unneccessary QUA/IS/IN "+ classIdentifier);
			else if(!(objDecl.isCompatibleClasses(quaDecl))) return(false);
		}
		return(true);
	}

//	/// Try to Compile-time Evaluate this expression
//	/// @return the resulting evaluated expression
//	public Expression evaluate(final PsiBuilder simBuilder) { return(this); }

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
