package simula.client;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import simula.Comn;

import java.io.ByteArrayOutputStream;

public class LoggingOutputStream extends FilterOutputStream {
    private final ByteArrayOutputStream copy = new ByteArrayOutputStream();

    public LoggingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
    	Comn.popUp("Output(1): "+(char)b);
        copy.write(b);   // Lagre kopi for overvåking
        super.write(b);  // Send videre til den ekte strømmen
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
    	Comn.popUp("Output(2): " + new String(b, off, len));
        copy.write(b, off, len);   // Lagre kopi
        super.write(b, off, len);  // Send videre
    }

    // Hent ut det som har blitt skrevet så langt
    public String getCapturedData() {
        return copy.toString();
    }
}
