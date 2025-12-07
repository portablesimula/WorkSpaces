// JavaLine 1 <== SourceLine 2
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Oct 09 12:26:39 CEST 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class adHoc02_Sporveksel extends RTS_CLASS {
    // ClassDeclaration: Kind=9, BlockLevel=2, PrefixLevel=0, firstLine=2, lastLine=7, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 4
    public boolean har_pinnen=false;
    // JavaLine 12 <== SourceLine 5
    public RTS_Head koe=null;
    // Normal Constructor
    public adHoc02_Sporveksel(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        BBLK(); // Iff no prefix
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc02_Sporveksel _STM() {
        // JavaLine 24 <== SourceLine 6
        koe=new RTS_Head((_CUR._SL))._STM();
        ;
        // JavaLine 27 <== SourceLine 7
        // BEGIN Sporveksel INNER PART
        // ENDOF Sporveksel INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc02.sim","9 Sporveksel",1,2,10,4,12,5,24,6,27,7,32,7);
} // End of Class
