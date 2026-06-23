package test.logger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinKlasse {

    // Opprett en logger låst til denne klassen
//    private static final Logger logger = Logger.getLogger(MinKlasse.class.getName());
    private static final Logger logger = Logger.getAnonymousLogger();
    
    public static void main(String[] args) {
        // Loggmeldinger med ulike alvorlighetsgrader
        logger.info("Dette er en vanlig informasjonsmelding.");
        logger.warning("Dette er en advarsel!");
        logger.severe("Dette er en kritisk feil!");
        
        // Debug-meldinger (vises ofte ikke på konsollen uten ekstra konfigurasjon)
        logger.fine("Dette er en debug-melding.");
    }
}
