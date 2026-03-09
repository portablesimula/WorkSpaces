/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.expression;

import java.lang.classfile.CodeBuilder;

import simula.compiler.parsing.Parse;
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
import simula.psi.PsiTree;
import simula.token.CharacterConst;
import simula.token.Identifier;
import simula.token.IntegerConst;
import simula.token.RealConst;
import simula.token.SimpleString;

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
	public static Expression acceptExpression() {
		if(Parse.accept(KeyWord.IF)) {
			Expression condition=acceptExpression();
			Parse.expect(KeyWord.THEN); Expression thenExpression=acceptSimpleExpression();
			Parse.expect(KeyWord.ELSE); Expression elseExpression=acceptExpression();
			Expression expr=new ConditionalExpression(Type.Boolean,condition,thenExpression,elseExpression);
			if(Option.internal.TRACE_PARSE) Util.TRACE("Expression: ParseExpression, result="+expr);
			return(expr);
		} else return(acceptSimpleExpression());
	} 
	
	/// Expect expression.
	/// <pre>
	/// Expression	=  SimpleExpression
	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
	/// </pre>
	/// If no expression is found an error message is printed.
	/// @return Expression or null if no expression is found.
	public static Expression expectExpression() {
		Expression expr=acceptExpression();
		if(expr==null) Util.error("Expecting Expression");
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
	private static Expression acceptSimpleExpression()  {   
		Expression expr = acceptANDTHEN();
		while(Parse.accept(KeyWord.OR_ELSE))  {
			expr=new BooleanExpression(expr,KeyWord.OR_ELSE,acceptANDTHEN());
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND THEN.
	/// <pre>
	/// BooleanTertiary =  Equivalence  { AND THEN  Equivalence }
	/// </pre>
	/// @return an expression
	private static Expression acceptANDTHEN() {
		Expression expr = acceptEQV();
		while(Parse.accept(KeyWord.AND_THEN))
			expr=new BooleanExpression(expr,KeyWord.AND_THEN,acceptEQV());
		return(expr);
	}

	/// Parse Utility: Accept Boolean EQV.
	/// <pre>
	/// Equivalence  =  Implication  { EQV  Implication }
	/// </pre>
	/// @return an expression
	private static Expression acceptEQV() { 
		Expression expr=acceptIMP();
		while(Parse.accept(KeyWord.EQV))
			expr=new BooleanExpression(expr,KeyWord.EQV,acceptIMP());
		return(expr);
	}

	/// Parse Utility: Accept Boolean IMP.
	/// <pre>
	/// Implication =  BooleanTerm  { IMP  BooleanTerm }
	/// </pre>
	/// @return an expression
	private static Expression acceptIMP() {
		Expression expr=acceptOR();
		while(Parse.accept(KeyWord.IMP))
			expr=new BooleanExpression(expr,KeyWord.IMP,acceptOR());
		return(expr);
	}

	/// Parse Utility: Accept Boolean OR.
	/// <pre>
	/// BooleanTerm  =  BooleanFactor  { OR  BooleanFactor }
	/// </pre>
	/// @return an expression
	private static Expression acceptOR() {
		Expression expr=acceptAND();
		while(Parse.accept(KeyWord.OR))
			expr=new BooleanExpression(expr,KeyWord.OR,acceptAND());
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND.
	/// <pre>
	/// BooleanFactor =  BooleanSecondary  { AND  BooleanSecondary }
	/// </pre>
	/// @return an expression
	private static Expression acceptAND() {
		Expression expr=acceptNOT();
		while(Parse.accept(KeyWord.AND))
			expr=new BooleanExpression(expr,KeyWord.AND,acceptNOT());
		return(expr);
	}

	/// Parse Utility: Accept Boolean NOT.
	/// <pre>
	/// BooleanSecondary  =  [ NOT ]  BooleanPrimary
	/// </pre>
	/// @return an expression
	private static Expression  acceptNOT() {
		Expression expr;
		if(Parse.accept(KeyWord.NOT)) {
			expr=UnaryOperation.create(KeyWord.NOT,acceptTEXTCONC());
		} else expr = acceptTEXTCONC();
		return(expr);
	}

	/// Parse Utility: Accept text concatenation.
	/// <pre>
	/// BooleanPrimary  =  TextPrimary  { & TextPrimary }
	/// </pre>
	/// @return an expression
	private static Expression acceptTEXTCONC() {
		Expression expr=acceptRelation();
		while(Parse.accept(KeyWord.AMPERSAND))
			expr=new TextExpression(expr,acceptRelation());
		return(expr);
	}

	/// Parse Utility: Accept relation.
	/// <pre>
	/// TextPrimary =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
	///    RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	/// </pre>
	/// @return an expression
	private static Expression acceptRelation() {   // Metode-form      
		Expression expr = acceptAdditiveOperation();
		if(Parse.acceptRelationalOperator())   { 
			int opr=Parse.prevToken.getKeyWord();
			expr=new RelationalOperation(expr,opr,acceptAdditiveOperation());
		}
		return(expr);
	}

	/// Parse Utility: Accept additive operation.
	/// <pre>
	/// SimpleArithmeticExpression  =  UnaryTerm  {  ( + | - )  Term }
	/// </pre>
	/// @return an expression
	private static Expression acceptAdditiveOperation() {
		Expression expr=acceptUNIMULDIV();
		while(Parse.accept(KeyWord.PLUS,KeyWord.MINUS)) { 
			int opr=Parse.prevToken.getKeyWord();
			expr=ArithmeticExpression.create(expr,opr,acceptMULDIV());
		}
		return(expr);
	}

	/// Parse Utility: Accept unary plus and minus.
	/// <pre>
	/// UnaryTerm  =  [ + | - ]  Term
	/// </pre>
	/// @return an expression
	private static Expression acceptUNIMULDIV() {
		Expression expr;
		if(Parse.accept(KeyWord.PLUS,KeyWord.MINUS)) {
			int opr=Parse.prevToken.getKeyWord();
			if(opr==KeyWord.PLUS) expr=acceptMULDIV();
			else expr=UnaryOperation.create(opr,acceptMULDIV());
		} else expr = acceptMULDIV();
		return(expr);
	}

	/// Parse Utility: Accept multiplicative operation.
	/// <pre>
	/// Term  =  Factor  {  ( * | / | // )  Factor }
	/// </pre>
	/// @return an expression
	private static Expression acceptMULDIV() {
		Expression expr=acceptEXPON();
		while(Parse.accept(KeyWord.MUL,KeyWord.DIV,KeyWord.INTDIV)) {
			int opr=Parse.prevToken.getKeyWord();
			expr=ArithmeticExpression.create(expr,opr,acceptEXPON());
		}
		return(expr);
	}

	/// Parse Utility: Accept exponentiation.
	/// <pre>
	/// Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>
	/// @return an expression
	private static Expression acceptEXPON() {
		Expression expr=acceptBASICEXPR();
		while(Parse.accept(KeyWord.EXP))
			expr=ArithmeticExpression.create(expr,KeyWord.EXP,acceptBASICEXPR());
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
	private static Expression acceptBASICEXPR() {
		// Dette er vel kanskje det samme som “primary”?
		// Merk: Alt som kan stå foran et postfix (DOT, IS, IN og QUA) må være et BASICEXPR
		if(Option.internal.TRACE_PARSE) Parse.TRACE("Expression: acceptExpression");
		Expression expr=null;
		if(Parse.accept(KeyWord.BEGPAR)) { expr = acceptExpression(); Parse.expect(KeyWord.ENDPAR); }
		else if(Parse.accept(KeyWord.INTEGERKONST)) expr = new Constant(Type.Integer,Parse.prevToken.getValue());
		else if(Parse.accept(KeyWord.REALKONST)) expr = Constant.createRealType(Parse.prevToken.getValue());
		else if(Parse.accept(KeyWord.BOOLEANKONST)) expr = new Constant(Type.Boolean,Parse.prevToken.getValue());
		else if(Parse.accept(KeyWord.CHARACTERKONST)) expr = new Constant(Type.Character,Parse.prevToken.getValue());
		else if(Parse.accept(KeyWord.TEXTKONST)) expr = new Constant(Type.Text,Parse.prevToken.getValue());
		else if(Parse.accept(KeyWord.NONE)) expr = new Constant(Type.Ref,null);
		else if(Parse.accept(KeyWord.NOTEXT)) expr = new Constant(Type.Text,null);
		else if(Parse.accept(KeyWord.NEW)) expr =ObjectGenerator.expectNew();
		else if(Parse.accept(KeyWord.THIS)) expr =LocalObject.expectThisIdentifier(); 
		else { String ident=Parse.acceptIdentifier();
			if(ident!=null) expr=VariableExpression.expectVariable(ident);
			else {
				if(Option.internal.TRACE_PARSE) Parse.TRACE("Expression: acceptBASICEXPR returns: NULL, prevKeyword="+Parse.prevToken.getKeyWord());
				if(Parse.prevToken.getKeyWord()==KeyWord.SEMICOLON) Parse.skipMisplacedCurrentSymbol(); // Ad'Hoc
				return(null);
			}
		}
		// Then there can be a sequence of postfixes, which builds a tree “upwards to the right”
		while (Parse.acceptPostfixOprator()) {
			int opr=Parse.prevToken.getKeyWord(); // opr == DOT || opr== IS || opr == IN || opr == QUA
			if (opr == KeyWord.DOT ) 
				expr=new RemoteVariable(expr,expectVariable());
			else {  // opr == IS or opr == IN or opr == QUA.  Then a class identifier must follow.
				String classIdentifier=Parse.expectIdentifier();
				if(opr==KeyWord.QUA)
					expr=new QualifiedObject(expr,classIdentifier);
				else expr=new ObjectRelation(expr,opr,classIdentifier);
			}
		}
		if(Option.internal.TRACE_PARSE) Parse.TRACE("Expression: acceptBasicExpression returns: "+expr);
		return(expr);
	}
  

//	/// Accept expression.
//	/// <pre>
//	/// Expression  =  SimpleExpression
//	/// 	        |  IF  BooleanExpression  THEN  SimpleExpression  ELSE  Expression
//	/// </pre>
//	/// @return Expression or null if no expression is found.
//	public static Expression acceptExpression() {
//		if(Parse.accept(simBuilder, KeyWord.IF)) {
//			Expression condition=acceptExpression();
//			Parse.expect(simBuilder, KeyWord.THEN); Expression thenExpression=acceptSimpleExpression();
//			Parse.expect(simBuilder, KeyWord.ELSE); Expression elseExpression=acceptExpression();
//			Expression expr=new ConditionalExpression(Type.Boolean,condition,thenExpression,elseExpression);
//			if(Option.internal.TRACE_PARSE) Util.TRACE("Expression: ParseExpression, result="+expr);
//			return(expr);
//		} else return(acceptSimpleExpression());
//	} 
	public static Expression acceptExpression(PsiBuilder simBuilder) {
		if(PsiParse.accept(simBuilder, KeyWord.IF)) {
			Expression condition=acceptExpression(simBuilder);
			PsiParse.expect(simBuilder, KeyWord.THEN);
				int thenTree = simBuilder.startSubtree(Expression.class, "Expression");
				Expression thenExpression=acceptSimpleExpression(simBuilder);
			PsiParse.expect(simBuilder, KeyWord.ELSE);
				Expression elseExpression=acceptExpression(simBuilder);
			Expression expr=new ConditionalExpression(Type.Boolean,condition,thenExpression,elseExpression);
			if(Option.internal.TRACE_PARSE) Util.TRACE("Expression: ParseExpression, result="+expr);
//			if(true) throw new RuntimeException("Expression.acceptExpression: NOT IMPL: "+expr);
			simBuilder.doneSubtree(expr);
			return expr;
		} else {
			if(Option.TRACE_ACCEPT_EXPRESSION > 0) IO.println("Expression.acceptExpression: ZZZZZZZZZZZZZZZZZZZZZZZZZZ   BEGIN: ");
//			simBuilder.printPSI("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
			Expression expr= acceptSimpleExpression(simBuilder);
			if(Option.TRACE_ACCEPT_EXPRESSION > 0) IO.println("Expression.acceptExpression: ZZZZZZZZZZZZZZZZZZZZZZZZZZ   RESULT: "+expr);
//			simBuilder.printPSI("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
			if(expr != null)
//				 simBuilder.doneSubtree(exprTree, expr);
				 simBuilder.doneSubtree(expr);
			else simBuilder.dropSubtree();
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
	public static Expression expectExpression(PsiBuilder simBuilder) {
		IO.println("Expression.expectExpression: BEGIN");
		Expression expr=acceptExpression(simBuilder);
		if(expr==null) Util.error("Expecting Expression");
		IO.println("Expression.expectExpression: END: expr="+expr);
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
	private static Expression acceptSimpleExpression(PsiBuilder simBuilder)  {   
		Expression expr = acceptANDTHEN(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptSimpleExpression");
		while(PsiParse.accept_OR_ELSE(simBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.OR_ELSE,acceptANDTHEN(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptSimpleExpression");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND THEN.
	/// <pre>
	/// BooleanTertiary =  Equivalence  { AND THEN  Equivalence }
	/// </pre>
	/// @return an expression
	private static Expression acceptANDTHEN(PsiBuilder simBuilder) {
		Expression expr = acceptEQV(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptANDTHEN");
		while(PsiParse.accept_AND_THEN(simBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.AND_THEN,acceptEQV(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptANDTHEN");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean EQV.
	/// <pre>
	/// Equivalence  =  Implication  { EQV  Implication }
	/// </pre>
	/// @return an expression
	private static Expression acceptEQV(PsiBuilder simBuilder) { 
		Expression expr=acceptIMP(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptEQV");
		while(PsiParse.accept(simBuilder, KeyWord.EQV)) {
			expr=new BooleanExpression(expr,KeyWord.EQV,acceptIMP(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptEQV");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean IMP.
	/// <pre>
	/// Implication =  BooleanTerm  { IMP  BooleanTerm }
	/// </pre>
	/// @return an expression
	private static Expression acceptIMP(PsiBuilder simBuilder) {
		Expression expr=acceptOR(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptIMP");
		while(PsiParse.accept(simBuilder, KeyWord.IMP)) {
			expr=new BooleanExpression(expr,KeyWord.IMP,acceptOR(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptIMP");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean OR.
	/// <pre>
	/// BooleanTerm  =  BooleanFactor  { OR  BooleanFactor }
	/// </pre>
	/// @return an expression
	private static Expression acceptOR(PsiBuilder simBuilder) {
		Expression expr=acceptAND(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptOR");
		while(PsiParse.accept_OR_ONLY(simBuilder)) {
			expr=new BooleanExpression(expr,KeyWord.OR,acceptAND(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptOR");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean AND.
	/// <pre>
	/// BooleanFactor =  BooleanSecondary  { AND  BooleanSecondary }
	/// </pre>
	/// @return an expression
	private static Expression acceptAND(PsiBuilder simBuilder) {
		Expression expr=acceptNOT(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptAND");
		while(PsiParse.accept_AND_ONLY(simBuilder)) {
			IO.println("Expression.acceptAND: ");
			expr=new BooleanExpression(expr,KeyWord.AND,acceptNOT(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptAND");
		}
		return(expr);
	}

	/// Parse Utility: Accept Boolean NOT.
	/// <pre>
	/// BooleanSecondary  =  [ NOT ]  BooleanPrimary
	/// </pre>
	/// @return an expression
	private static Expression acceptNOT(PsiBuilder simBuilder) {
		Expression expr;
		if(PsiParse.accept(simBuilder, KeyWord.NOT)) {
			expr=UnaryOperation.create(KeyWord.NOT,acceptTEXTCONC(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptNOT");
		} else {
			expr = acceptTEXTCONC(simBuilder);
			if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptNOT");
		}
		return(expr);
	}

	/// Parse Utility: Accept text concatenation.
	/// <pre>
	/// BooleanPrimary  =  TextPrimary  { & TextPrimary }
	/// </pre>
	/// @return an expression
	private static Expression acceptTEXTCONC(PsiBuilder simBuilder) {
		Expression expr=acceptRelation(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptTEXTCONC");
		while(PsiParse.accept(simBuilder, KeyWord.AMPERSAND)) {
			expr=new TextExpression(expr,acceptRelation(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptTEXTCONC");
		}
		return(expr);
	}

	/// Parse Utility: Accept relation.
	/// <pre>
	/// TextPrimary =  SimpleArithmeticExpression  [ RelationOperator  SimpleArithmeticExpression ]
	///    RelationOperator  =  <  |  <=  |  =  |  >=  |  >  |  <> |  ==  |  =/=
	/// </pre>
	/// @return an expression
	private static Expression acceptRelation(PsiBuilder simBuilder) {
		Expression expr = acceptAdditiveOperation(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptRelation");
		LexToken prevToken = null;
		if((prevToken = PsiParse.acceptRelationalOperator(simBuilder)) != null)   { 
			int opr = prevToken.keyWord;
			expr = new RelationalOperation(expr,opr,acceptAdditiveOperation(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptRelation");
		}
		return(expr);
	}

	/// Parse Utility: Accept additive operation.
	/// <pre>
	/// SimpleArithmeticExpression  =  UnaryTerm  {  ( + | - )  Term }
	/// </pre>
	/// @return an expression
	private static Expression acceptAdditiveOperation(PsiBuilder simBuilder) {
		Expression expr=acceptUNIMULDIV(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptAdditiveOperation");
		LexToken accepted = null;
		while( (accepted = PsiParse.acceptParserToken(simBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) { 
			int opr=accepted.keyWord;
			expr=ArithmeticExpression.create(expr,opr,acceptMULDIV(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptAdditiveOperation");
		}
		return(expr);
	}

	/// Parse Utility: Accept unary plus and minus.
	/// <pre>
	/// UnaryTerm  =  [ + | - ]  Term
	/// </pre>
	/// @return an expression
	private static Expression acceptUNIMULDIV(PsiBuilder simBuilder) {
		Expression expr;
		LexToken prevToken = null;
		if((prevToken = PsiParse.acceptParserToken(simBuilder, KeyWord.PLUS,KeyWord.MINUS)) != null) {
			int opr=prevToken.keyWord;
			if(opr==KeyWord.PLUS) expr=acceptMULDIV(simBuilder);
			else {
				expr=UnaryOperation.create(opr,acceptMULDIV(simBuilder));
				if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptUNIMULDIV");
			}
		} else {
			expr = acceptMULDIV(simBuilder);
			if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptUNIMULDIV");
		}
		return(expr);
	}

	/// Parse Utility: Accept multiplicative operation.
	/// <pre>
	/// Term  =  Factor  {  ( * | / | // )  Factor }
	/// </pre>
	/// @return an expression
	private static Expression acceptMULDIV(PsiBuilder simBuilder) {
//		simBuilder.startSubtree(Expression.class, "acceptMULDIV-1");
		Expression expr=acceptEXPON(simBuilder);
//		if(expr != null) {
//			if(TEST4) simBuilder.doneSubtree(expr);
//			if(TEST4) simBuilder.startSubtree(Expression.class, "acceptMULDIV-1");
//		}
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptMULDIV");
		LexToken accepted = null;
		while((accepted = PsiParse.acceptParserToken(simBuilder, KeyWord.MUL,KeyWord.DIV,KeyWord.INTDIV)) != null) {
			int opr = accepted.keyWord;
			expr = ArithmeticExpression.create(expr,opr,acceptEXPON(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptMULDIV");
		}
		return(expr);
	}

	/// Parse Utility: Accept exponentiation.
	/// <pre>
	/// Factor  =  BasicExpression  { **  BasicExpression }
	/// </pre>
	/// @return an expression
	private static Expression acceptEXPON(PsiBuilder simBuilder) {
		Expression expr=acceptBASICEXPR(simBuilder);
		if(TEST5) simBuilder.doneAndStartSubtree1(expr, Expression.class, "acceptEXPON");
		while(PsiParse.accept(simBuilder, KeyWord.EXP)) {
			expr = ArithmeticExpression.create(expr,KeyWord.EXP,acceptBASICEXPR(simBuilder));
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptEXPON");
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
	private static Expression acceptBASICEXPR(PsiBuilder simBuilder) {
		// Dette er vel kanskje det samme som “primary”?
		// Merk: Alt som kan stå foran et postfix (DOT, IS, IN og QUA) må være et BASICEXPR
		if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptExpression");
//		if(TEST1) simBuilder.startSubtree(Expression.class, "acceptBASICEXPR");
//		if(TEST2) simBuilder.startSubtree(Expression.class, "acceptBASICEXPR");
		Expression expr=null;
		LexToken prevToken = PsiParse.getParserToken(simBuilder);
		if(PsiParse.accept(simBuilder, KeyWord.BEGPAR)) { expr = acceptExpression(simBuilder); PsiParse.expect(simBuilder, KeyWord.ENDPAR); }
		else if(PsiParse.accept(simBuilder, KeyWord.INTEGERKONST)) expr = new Constant(Type.Integer,((IntegerConst)prevToken).value);
		else if(PsiParse.accept(simBuilder, KeyWord.REALKONST)) expr = Constant.createRealType(((RealConst)prevToken).value);

//		else if(PsiParse.accept(simBuilder, KeyWord.BOOLEANKONST)) expr = new Constant(Type.Boolean,((IntegerConst)prevToken).value);
		else if(PsiParse.accept(simBuilder, KeyWord.TRUE)) expr = new Constant(Type.Boolean,true);
		else if(PsiParse.accept(simBuilder, KeyWord.FALSE)) expr = new Constant(Type.Boolean,false);

		else if(PsiParse.accept(simBuilder, KeyWord.CHARACTERKONST)) expr = new Constant(Type.Character,((CharacterConst)prevToken).value);
		else if(PsiParse.accept(simBuilder, KeyWord.TEXTKONST)) expr = new Constant(Type.Text,((SimpleString)prevToken).value);
		else if(PsiParse.accept(simBuilder, KeyWord.NONE)) expr = new Constant(Type.Ref,null);
		else if(PsiParse.accept(simBuilder, KeyWord.NOTEXT)) expr = new Constant(Type.Text,null);
		else if(PsiParse.accept(simBuilder, KeyWord.NEW)) expr = ObjectGenerator.expectNew(simBuilder);
		else if(PsiParse.accept(simBuilder, KeyWord.THIS)) expr = LocalObject.expectThisIdentifier(simBuilder); 
		else {
//			String ident=PsiParse.acceptIdentifier(simBuilder);
			
//			public static String acceptIdentifier(final PsiBuilder simBuilder) {
			LexToken prevToken2 = PsiParse.getParserToken(simBuilder) ;
			LexToken token = null;
			if ((token = PsiParse.acceptParserToken(simBuilder, KeyWord.IDENTIFIER)) != null) {
				String ident = ((Identifier)token).value;
				expr=VariableExpression.expectVariable(simBuilder, ident);
			}
//				return (null);
//			}

//			if(ident!=null) expr=VariableExpression.expectVariable(ident);
			else {
//				if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Expression: acceptBASICEXPR returns: NULL, prevKeyword="+PsiParse.prevToken.getKeyWord());
				if(prevToken2.keyWord == KeyWord.SEMICOLON) PsiParse.skipMisplacedCurrentSymbol(simBuilder); // Ad'Hoc
				return(null);
			}
		}
		if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptBASICEXPR");
		// Then there can be a sequence of postfixes, which builds a tree “upwards to the right”
		while ((prevToken = PsiParse.acceptPostfixOprator(simBuilder)) != null) {
			int opr = prevToken.keyWord; // opr == DOT || opr== IS || opr == IN || opr == QUA
			if (opr == KeyWord.DOT ) 
				expr=new RemoteVariable(expr,expectVariable(simBuilder));
			else {  // opr == IS or opr == IN or opr == QUA.  Then a class identifier must follow.
				String classIdentifier=PsiParse.expectIdentifier(simBuilder);
				if(opr==KeyWord.QUA)
					expr=new QualifiedObject(expr,classIdentifier);
				else expr=new ObjectRelation(expr,opr,classIdentifier);
			}
			if(TEST5) simBuilder.doneAndStartSubtree2(expr, Expression.class, "acceptPOSTFIX");
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
	private static VariableExpression expectVariable() { 
		// An identifier, possibly followed by arguments in parentheses.
		String ident=Parse.expectIdentifier();
		return(VariableExpression.expectVariable(ident));
	}

	private static VariableExpression expectVariable(PsiBuilder simBuilder) { 
		// An identifier, possibly followed by arguments in parentheses.
		String ident=PsiParse.expectIdentifier(simBuilder);
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
