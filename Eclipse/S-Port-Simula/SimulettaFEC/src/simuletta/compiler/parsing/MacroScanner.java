/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import static simuletta.utilities.Util.*;

import java.util.Vector;

import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

/**
 * @author Øystein Myhre Andersen
 */
public class MacroScanner {
	public static MacroScanner currentMacroScanner;
	public final MacroDefinition macroDefinition;
	final MacroScanner prevScanner;
	final Token prevToken;
	private Vector<Token> expandedTokens=new Vector<Token>();
	private int nextIndex; // During Scanning
	private int expandDepth=0;
	
	public MacroScanner(MacroDefinition macroDefinition) { //,MacroScanner prevScanner) {
		this.macroDefinition=macroDefinition;
		this.prevScanner=currentMacroScanner;
		this.prevToken=Parser.prevToken;
		//Util.BREAK("MacroScanner.initScan: Current Token="+Parser.currentToken+",  PrevToken="+Parser.prevToken);
		Parser.nextSymb();
		Parser.expect(KeyWord.BEGPAR);
		//Util.BREAK("MacroScanner.initScan("+macroDefinition.identifier+"): Current Token="+Parser.currentToken);
		ActualParameter par[] = null;
		if(macroDefinition.nPar>0) {
			par=new ActualParameter[macroDefinition.nPar];
			for(int i=0;i<macroDefinition.nPar;i++) {
				if(i>0) Parser.expect(KeyWord.COMMA);
				par[i]=acceptParameter();
			}
		}
		nextIndex=0; Parser.prevToken=this.prevToken;
		//Util.BREAK("MacroScanner.initScan("+macroDefinition.identifier+"): DONE par="+edParams()+",  CurrentToken="+Parser.currentToken+",  PrevToken="+Parser.prevToken);
		expand(macroDefinition,expandedTokens,par);
	}
	
	public String identifier() { return(macroDefinition.identifier); }
	
	private boolean macroFinished() { return(nextIndex>=expandedTokens.size()); }

	public Token nextToken() {
		Token next=expandedTokens.elementAt(nextIndex++);
		if(currentMacroScanner.macroFinished()) {
			if(Option.TRACE_MACRO_SCAN) println("MacroScanner:"+currentMacroScanner.identifier()+": TERMINATES");
			currentMacroScanner=null;
		}
		if(Option.TRACE_MACRO_SCAN) println("MacroScanner:"+identifier()+".nextToken: "+next+"    nextIndex="+nextIndex
												+", LastToken="+macroFinished()+",  Current Token="+Parser.currentToken);
		return(next);
	}
	
	private void expand(MacroDefinition macroDefinition,Vector<Token> expandedTokens,ActualParameter par[]) {
		//Util.BREAK("MacroScanner.initScan("+macroDefinition.identifier+"): Current Token="+Parser.currentToken);
		if(Option.TRACE_MACRO_EXPAND) printBeginExpand(macroDefinition,par);
		int idx=0;
		while(idx < macroDefinition.elts.size()) {
			Token token=macroDefinition.elts.elementAt(idx++);
			if( token.getKeyWord()==KeyWord.IDENTIFIER) {
				Object val=SimulettaScanner.mnemonics.get(token.getIdentifier());
				if(val!=null) {
					if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.expand: "+val.getClass().getSimpleName()+"   "+idx+"   "+val);
					if(val instanceof MacroDefinition) {
						ActualParameter par2[] = null;
						if(expandDepth++ > 0) FATAL_ERROR("More the two Macro levels");
						
						expect(macroDefinition,idx++,KeyWord.BEGPAR);
						if(macroDefinition.nPar>0) {
							par2=new ActualParameter[macroDefinition.nPar];
							for(int j=0;j<macroDefinition.nPar;j++) {
								if(j>0) Parser.expect(KeyWord.COMMA);
								par2[j]=new ActualParameter();
								// % any token ... %   or   any token...  except  ,  )
								//Util.BREAK("MacroScanner.acceptParameter: Current Token="+Parser.currentToken);
								if(accept(macroDefinition,idx,KeyWord.PERCENT)) {
									idx++;
									Token tok;
									while((tok=macroDefinition.elts.elementAt(idx++)).getKeyWord()!=KeyWord.PERCENT){
										par2[j].tkn.add(tok);
									}
								} else {
									Token tok=currentToken;
									while(acceptAnyExcept(macroDefinition,idx++,KeyWord.COMMA,KeyWord.ENDPAR)) {
										par2[j].tkn.add(tok);
										tok=currentToken;
									}
								}
								if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.acceptParameter: Return="+par2[j]+",  Current Token="+Parser.currentToken);
								if(par2[j].tkn.size()==0) IERR();
							}
							//printParameters(par2);
						}
						expand((MacroDefinition)val,expandedTokens,par2); 
					} else {
						add(macroDefinition.identifier,expandedTokens,((LiteralMnemonic)val).token);
					}
				}
				else add(macroDefinition.identifier,expandedTokens,token);
			} else if(token.getKeyWord()==KeyWord.PERCENT) {
				// REPLACE WITH ACTUAL PARAMETER
				int n=((Long)(token.getValue())).intValue();
				//Util.BREAK("MacroScanner.expand: REPLACE WITH ACTUAL PARAMETER "+n+",  Current Token="+Parser.currentToken);
				ActualParameter apar=par[n-1];
				if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.expand: REPLACE PARAMETER "+n+",  "+apar);
				//printParameters(par);
				for(Token tok:apar.tkn) {
					add(macroDefinition.identifier,expandedTokens,tok);
					//Util.BREAK("MacroScanner.expand: REPLACE WITH ACTUAL PARAMETER "+scanParameter+",  Current Token="+Parser.currentToken);
				}
			}
			else add(macroDefinition.identifier,expandedTokens,token);
		}
//		println("EXPANDED " + macroDefinition.identifier + '(' + macroDefinition.nPar + ") XTOKENS=" + expandedTokens);
		if(Option.TRACE_MACRO_EXPAND) printEndExpand(macroDefinition,par);
	}
	
	private static void add(String ident,Vector<Token> expandedTokens,Token token) {
		if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.expand: "+ident+":ADD["+(expandedTokens.size())+']'+token);
		expandedTokens.add(token);
	}
	
	private static Token currentToken;
	private static boolean accept(MacroDefinition def,int idx,final int s) {
		currentToken=def.elts.elementAt(idx);
		if (currentToken.getKeyWord() == s) {
			if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.accept("+s+") == True  "+currentToken);
			return (true);
		}
		if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.accept("+s+") == False  "+currentToken);
		return (false);
	}

	private static boolean acceptAnyExcept(MacroDefinition def,int idx,final int ... s) {
		currentToken=def.elts.elementAt(idx);
		if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.acceptAnyExcept: idx="+idx+", currentToken="+currentToken+", s[1}="+s[1]);
		for(int i=0;i<s.length;i++) {
			if (currentToken.getKeyWord() == s[i]) {
				if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.acceptAnyExcept("+s[i]+") == false  "+currentToken);
				return (false);
			}
		}
		if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.acceptAnyExcept("+s+") == False  "+currentToken);
		return (true);
	}

	private static boolean expect(MacroDefinition def,int idx,final int s) {
		if (accept(def,idx,s)) return (true);
		Util.ERROR("MacroScanner Got symbol '" + currentToken + "' while expecting Symbol " + s);
		return (false);
	}

	private void printBeginExpand(MacroDefinition macroDefinition,ActualParameter par[]) {
		String s="BEGIN EXPAND " + macroDefinition.identifier + '(' + macroDefinition.nPar + ")=";
		for(Token tok:expandedTokens) s=s+" "+tok; println(s);
		printParameters(par);
		s="    TOKENS=";
		for(Token tok:macroDefinition.elts) s=s+" "+tok; println(s);
	}
	
	private void printEndExpand(MacroDefinition macroDefinition,ActualParameter par[]) {
		String s="END EXPAND " + macroDefinition.identifier + '(' + macroDefinition.nPar + ")=";
		for(Token tok:expandedTokens) s=s+" "+tok; println(s);
		printParameters(par);
		s="    XTOKENS=";
		for(Token tok:expandedTokens) s=s+" "+tok; println(s);
	}
	
	private void printParameters(ActualParameter par[]) {
		if(par==null) println("    NO PARAMETERS"); else {
			StringBuilder sb=new StringBuilder("    PARAMETERS=");
			String sep="";
			for(int i=0;i<par.length;i++) {
				sb.append(sep+par[i]); sep="|";
			}
			println(sb.toString());
		}
	}
	
	class ActualParameter {
		Vector<Token> tkn=new Vector<Token>();
		private int paramIndex;
		public void initScan() { paramIndex=0; }
		public Token nextToken() {
			Token next=(tkn.size()==0)?null:tkn.elementAt(paramIndex++);
			//Util.BREAK("MacroScanner.ActualParameter.nextToken: "+next+", scanParameter="+scanParameter);
			return(next);
		}
		public String toString() {
			return (tkn.toString());
		}
	}
	
	ActualParameter acceptParameter() {
		// % any token ... %   or   any token...  except  ,  )
		ActualParameter par=new ActualParameter();
		//Util.BREAK("MacroScanner.acceptParameter: Current Token="+Parser.currentToken);
		if(Parser.accept(KeyWord.PERCENT)) {
			Token token;
			while((token=Parser.getSymb()).getKeyWord()!=KeyWord.PERCENT){
				par.tkn.add(token);
			}
		} else {
			Token token=Parser.currentToken;
			while(Parser.acceptAnyExcept(KeyWord.COMMA,KeyWord.ENDPAR)) {
				par.tkn.add(token);
				token=Parser.currentToken;
			}
			if(Option.TRACE_MACRO_EXPAND) println("MacroScanner.acceptParameter: Return="+par+",  Current Token="+Parser.currentToken);
		}
		//Util.BREAK("MacroScanner.acceptParameter: Return="+par+",  Current Token="+Parser.currentToken);
		return(par);
	}
	
	public String toString() {
		return("MacroScanner "+macroDefinition.identifier);//+", finalToken="+finalToken);
	}

}
