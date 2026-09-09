package simula.core.builder.export;

import org.eclipse.lsp4j.Range;

import simula.core.utilities.Util;

public class SimulaDiagnostic {
	public enum Severity { Error, Warning, Information, Hint }
	public Severity severity;
	public Range range;
	public String mss;

	
	public SimulaDiagnostic(Severity severity, Range range, String mss) {
		this.severity = severity;
		this.range = range;
		this.mss = mss;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(severity)
		.append(" ").append(Util.edRange(range))
		.append(" ").append(mss);
		return sb.toString();
	}
}
