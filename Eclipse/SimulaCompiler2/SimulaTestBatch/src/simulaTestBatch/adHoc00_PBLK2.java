package simulaTestBatch;
// Simula-2.0 Compiled at Mon Dec 29 10:40:45 CET 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_PBLK2 extends RTS_Simset {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=1, lastLine=7, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 2
    public RTS_Head liste_1=null;
    // JavaLine 12 <== SourceLine 3
    public RTS_Link elt_1=null;
    // Normal Constructor
    public adHoc00_PBLK2(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_PBLK2 _STM() {
        // BEGIN Simset INNER PART
        // BEGIN PBLK2 INNER PART
        // ENDOF PBLK2 INNER PART
        // JavaLine 26 <== SourceLine 5
        elt_1=liste_1.first();
        ;
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock PBLK2",10,2,12,3,26,5,32,7);
} // End of Class
