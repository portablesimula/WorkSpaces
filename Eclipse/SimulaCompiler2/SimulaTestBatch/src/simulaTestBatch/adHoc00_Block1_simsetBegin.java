// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Apr 02 07:48:39 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_simsetBegin extends RTS_Simset {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=1, lastLine=9, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 4
    public RTS_Head liste_1=null;
    public RTS_Link elt_1=null;
    // Normal Constructor
    public adHoc00_Block1_simsetBegin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_Block1_simsetBegin _STM() {
        // BEGIN Simset INNER PART
        // BEGIN simsetBegin INNER PART
        // ENDOF simsetBegin INNER PART
        // JavaLine 26 <== SourceLine 7
        elt_1=liste_1.first();
        ;
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock simsetBegin",1,1,11,4,26,7,32,9);
} // End of Class
