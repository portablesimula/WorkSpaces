///
module Simula {
	requires java.desktop;
	requires java.compiler;
	
	requires org.eclipse.lsp4j;
	requires org.eclipse.lsp4j.jsonrpc;
	requires com.google.gson;
	requires java.logging;
	
    exports simula.inline.tester to org.eclipse.lsp4j.jsonrpc;
}