package simula.compiler;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;

public class SimulaError {
	Range range;
	String message;
	DiagnosticSeverity severity; // Error, Warning,	Information, Hint

	enum Reporter { SimulaLexer, SimulaParser, SimulaChecker, SimulaCoderb }
	Reporter reporter;
	
	public Diagnostic getDiagnostic() {
		return new Diagnostic(range, message, severity, reporter.toString());
	}


//	public int getCharPosition() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	public int getLine() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	public String getMessage() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public int getLength() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

}
