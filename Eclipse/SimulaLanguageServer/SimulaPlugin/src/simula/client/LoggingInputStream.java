package simula.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import simula.Comn;

public class LoggingInputStream extends FilterInputStream {
    
    public LoggingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b != -1) {
//            System.out.print((char) b); // Skriver ut som tekst
        	Comn.popUp("Input(1): "+(char)b);
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead != -1) {
//            System.out.print(new String(b, off, bytesRead)); // Skriver ut bufferen
        	Comn.popUp("Input(2): " + new String(b, off, bytesRead)); // Skriver ut bufferen
        }
        return bytesRead;
    }
}
