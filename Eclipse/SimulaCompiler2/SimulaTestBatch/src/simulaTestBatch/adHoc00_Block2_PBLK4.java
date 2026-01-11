package simulaTestBatch;
// Simula-2.0 Compiled at Mon Dec 29 12:40:17 CET 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block2_PBLK4 extends RTS_Simset {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=2, lastLine=9, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 4
    public RTS_Head liste_1=null;
    // JavaLine 12 <== SourceLine 5
    public RTS_Link elt_1=null;
    // Normal Constructor
    public adHoc00_Block2_PBLK4(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_Block2_PBLK4 _STM() {
        // BEGIN Simset INNER PART
        // BEGIN PBLK4 INNER PART
        // ENDOF PBLK4 INNER PART
        // JavaLine 26 <== SourceLine 7
        elt_1=liste_1.first();
        ;
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock PBLK4",10,4,12,5,26,7,32,9);
} // End of Class
