package simula.core.builder.export;

public class SimulaDiagnostic {
	public enum Severity { Error, Warning, Information, Hint }
	public Severity severity;
	public LexRange range;
	public String mss;

	
	public SimulaDiagnostic(Severity severity, LexRange range, String mss) {
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
