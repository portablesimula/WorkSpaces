package simula.compiler.utilities;

import simula.lsp.util.SimRange;

public class SimulaDiagnostic {
	public enum Severity { Error, Warning, Information, Hint }
	Severity severity;
	SimRange range;
	String mss;

	
	public SimulaDiagnostic(Severity severity, SimRange range, String mss) {
		this.severity = severity;
		this.range = range;
		this.mss = mss;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(severity)
		.append(" ").append(range)
		.append(" ").append(mss);
		return sb.toString();
	}
}
