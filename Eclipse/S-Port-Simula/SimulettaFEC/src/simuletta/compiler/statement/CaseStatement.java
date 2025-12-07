/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import java.util.Vector;

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.expression.Expression;
//import simuletta.compiler.JavaModule;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * Case Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *  	CaseStatement ::= CASE Lower'IntegerNumber  :  Upper'IntegerNumber  (  Integer'Expression  )
 *  							<  WHEN  WhenKeyList :  < Statement >*  >+  
 *  							<  OTHERWISE  < Statement >*  >?
 *  					  ENDCASE
 *  
 *  		WhenKeyList ::= Which'IntegerNumber  <  ,  Which'IntegerNumber  >*
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class CaseStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	private int lowKey,hiKey;
	private Expression switchKey;
	private Vector<WhenPart> switchCases=new Vector<WhenPart>();
	private Otherwise otherwise=null;
	

	public static CaseStatement parse() {
		CaseStatement stm=new CaseStatement();
		if (Option.TRACE_PARSE)	Parser.TRACE("Parse CaseStatement");
		stm.lowKey = Parser.expectIntegerConst().intValue();
		Parser.expect(KeyWord.COLON);
		stm.hiKey = Parser.expectIntegerConst().intValue();
		Parser.expect(KeyWord.BEGPAR);
		stm.switchKey = scan_expr(getprim());//Expression.parseExpression();
		Parser.expect(KeyWord.ENDPAR);
		while (Parser.accept(KeyWord.WHEN)) {
			stm.switchCases.add(stm.new WhenPart());
		}
		if(Parser.accept(KeyWord.OTHERWISE)) stm.otherwise=stm.new Otherwise();
		Parser.expect(KeyWord.ENDCASE);
		if (Option.TRACE_PARSE)	Util.TRACE("END NEW ConnectionStatement: " + stm.toString());
		return(stm);
	}

    public static int caseindex() {
    	Long idx=Parser.acceptIntegerConst();
    	if(idx != null) {
    		return(idx.intValue());
    	} else {
    		String idc=Parser.acceptString();
    		return(idc.charAt(0));
    	}
    }

    class WhenPart {
    	//  WhenPart ::= WHEN  WhenKeyList :  < Statement >*  >+  
    	//     WhenKeyList ::= Which'IntegerNumber  <  ,  Which'IntegerNumber  >*
    	final Vector<Integer> caseKeyList=new Vector<Integer>();
    	final Vector<Statement> statements=new Vector<Statement>();
    	public WhenPart()	{
			do { caseKeyList.add(Parser.expectIntegerConst().intValue());
			} while(Parser.accept(KeyWord.COMMA));
			Parser.expect(KeyWord.COLON);
			while(!Parser.accept(KeyWord.WHEN,KeyWord.OTHERWISE,KeyWord.ENDCASE)) {
				Statement.parseStatements(statements);
			} Parser.saveCurrentToken();
    		if(Option.TRACE_PARSE) Util.TRACE("NEW WhenPart: " + toString());
    	}
    		
    	public void print(final String lead,final int indent) {
        	String spc=Util.edIndent(indent);
    		IO.println(spc+edWhen()+statements);
    	}
    	
    	private String edWhen() {
    		StringBuilder s=new StringBuilder();
    		s.append("WHEN ").append(caseKeyList).append(" : ").append(statements);
    		return(s.toString());
    	}
	
    	public String toString() { return(edWhen()); }
    }
	
	class Otherwise {
		// OTHERWISE  < Statement >*
    	final Vector<Statement> statements=new Vector<Statement>();
		public Otherwise() {
			while(!Parser.accept(KeyWord.WHEN,KeyWord.OTHERWISE,KeyWord.ENDCASE)) {
				Statement.parseStatements(statements);
			} Parser.saveCurrentToken();
    		if(Option.TRACE_PARSE) Util.TRACE("NEW Otherwise: " + toString());			
		}
			    		
    	public void print(final String lead,final int indent) {
        	String spc=Util.edIndent(indent);
    		IO.println(spc+edOtherwise());
    	}
    	
    	private String edOtherwise() {
    		StringBuilder s=new StringBuilder();
    		s.append("OTHERWISE ").append(statements);
    		return(s.toString());
    	}
	
    	public String toString() { return(edOtherwise()); }
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			SET_SEMANTICS_CHECKED();
		exitLine();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {
		enterLine();
		
    	int nIndex=hiKey-lowKey+1;
    	boolean[] defined=new boolean[nIndex]; //(lowKey:hiKey);
    	ProgramPoint[] xdest=new ProgramPoint[nIndex]; //(1:hiKey-lowKey+1);
    	Type typ=switchKey.doSCodingDirect();
    	if(typ != Type.Integer) {
			Type res=Type.checkCompatible(typ,Type.Integer); // tstconv(typ,T_INT);
			if(res==null) ERROR("Missing type conversion: "+typ+" --> Integer");
    	}
    	if(lowKey != 0) {
    		sCode.outinst(S_PUSHC); sCode.outinst(S_C_INT);
    		sCode.outstring(""+lowKey);
    		sCode.outcode();
    		sCode.outinst(S_SUB);
    		sCode.outcode();
    	}
    	Tag stag=Tag.newTag(false,"SWITCH","");
    	sCode.outinst(S_SWITCH); sCode.outtag(stag);
    	sCode.outnumber(hiKey-lowKey+1);
    	sCode.outcode(+1);
    	
    	int nwhen=0;
     	for(WhenPart when:switchCases) {
    		for(Integer caseKey:when.caseKeyList) {
    			Util.BREAK("CaseStatement.WHEN: caseKey="+caseKey);
    			if(defined[caseKey-lowKey]) ERROR("When index already defined");
    			else { defined[caseKey-lowKey]=true;
    			sCode.outinst(S_SDEST); sCode.outtag(stag);
    			sCode.outnumber(caseKey-lowKey);
    			}
    		}
			sCode.outcode(+1);
			for(Statement stm:when.statements) stm.doSCodeStatement();
			sCode.outcode(-1);

    		if(otherwise!=null || when!=switchCases.lastElement()) {
    			nwhen=nwhen+1;
    			Util.BREAK("CaseStatement.ENDWHEN: nwhen="+nwhen+", hiKey-lowKey="+(hiKey-lowKey));
    			//inspect new program_point do {
    			ProgramPoint pp=new ProgramPoint("Case:whenLab#"+nwhen);
    			xdest[nwhen-1]=pp; //:-this program_point;
    			pp.go_to();
    		} //!else jump immediately before end-when;
    	}
    	for(int x=0;x<nIndex;x++)
    		if(!defined[x]) {
    			sCode.outinst(S_SDEST); sCode.outtag(stag);
    			sCode.outnumber(x);
    			sCode.outcode();
    		}
    	if(otherwise!=null) {
    		for(Statement stm:otherwise.statements) stm.doSCodeStatement();
    	}
    	while(nwhen !=0) { xdest[nwhen-1].define(); nwhen=nwhen-1; }
		sCode.outcode(-1);
		
		exitLine();
	}
	  
    // ***********************************************************************************************
    // *** Printing Utility: print
    // ***********************************************************************************************
	public void print(final String lead,final int indent) {
    	String spc=Util.edIndent(indent);
    	Util.println(spc+"CASE "+lowKey+':'+hiKey+" ("+switchKey+')');
    	for(WhenPart when:switchCases) when.print("",indent+1);
    	if(otherwise!=null) otherwise.print("",indent+1);
        Util.println(spc+"ENDCASE"); 
    }

    public String toString() {
    	return("CASE "+lowKey+':'+hiKey+" ("+switchKey+") ...");
    }

}
