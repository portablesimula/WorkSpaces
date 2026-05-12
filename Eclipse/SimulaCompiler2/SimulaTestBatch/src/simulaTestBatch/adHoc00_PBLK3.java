package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 14:32:02 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_PBLK3 extends RTS_Simulation {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=1, lastLine=5, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_PBLK3(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_PBLK3 _STM() {
        // BEGIN Simset INNER PART
        // BEGIN Simulation INNER PART
        // BEGIN PBLK3 INNER PART
        // ENDOF PBLK3 INNER PART
        // JavaLine 24 <== SourceLine 3
        ((adHoc00_PBLK3)(_CUR)).ActivateAt(true,(RTS_Process)main_1,(time()+(20.0d)),false);
        ;
        // ENDOF Simulation INNER PART
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock PBLK3",24,3,31,5);
} // End of Class
