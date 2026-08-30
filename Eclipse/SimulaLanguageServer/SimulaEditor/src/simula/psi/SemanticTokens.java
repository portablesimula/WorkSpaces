package simula.psi;

import java.util.List;
import simula.compiler.SourceModule;
import simula.compiler.utilities.Util;

public class SemanticTokens {
//	public List<LexToken> tokenList;
	private SourceModule sourceModule;
	public List<Integer> tokens;
	
	public SemanticTokens(SourceModule sourceModule, List<Integer> tokens) {
		this.sourceModule = sourceModule;
		this.tokens = tokens;
	}
		
	public String getText() {
		// TODO Auto-generated method stub
		Util.IERR("");
		return sourceModule.getUpdatedText();
	}

}
