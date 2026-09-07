///
module SimulaCore {
	requires java.desktop;
	requires java.compiler;
	requires java.logging;
	requires org.eclipse.lsp4j;
	
	exports simula;
	exports simula.runtime;
	exports simula.core.builder.export;
//	exports simula.compiler.utilities;
}