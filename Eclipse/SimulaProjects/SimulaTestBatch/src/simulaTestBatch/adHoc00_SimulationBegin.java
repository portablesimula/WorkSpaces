package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 15:39:37 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulationBegin extends RTS_Simulation {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=1, lastLine=5, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulationBegin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_SimulationBegin _STM() {
        // BEGIN Simset INNER PART
        // BEGIN Simulation INNER PART
        // BEGIN SimulationBegin INNER PART
        // ENDOF SimulationBegin INNER PART
        // JavaLine 24 <== SourceLine 3
        ((adHoc00_SimulationBegin)(_CUR)).ActivateAt(true,(RTS_Process)main,(time()+(20.0d)),false);
        // ENDOF Simulation INNER PART
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock SimulationBegin",24,3,30,5);
} // End of Class
