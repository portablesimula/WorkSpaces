package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin_SimulationBegin extends RTS_Simulation {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=2, firstLine=5, lastLine=54, hasLocalClasses=true, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 6
    public boolean failed_2=false;
    // JavaLine 13 <== SourceLine 8
    public RTS_TEXT_ARRAY ut=null;
    public RTS_TEXT_ARRAY answer=null;
    // JavaLine 16 <== SourceLine 9
    public int utno_2=0;
    // JavaLine 18 <== SourceLine 47
    public RTS_REF_ARRAY<adHoc00_SimulaTestBegin_SimulationBegin_p> pa=null;
    // JavaLine 20 <== SourceLine 48
    public RTS_BOOLEAN_ARRAY active=null;
    public RTS_BOOLEAN_ARRAY passive=null;
    public RTS_BOOLEAN_ARRAY terminatd=null;
    // JavaLine 24 <== SourceLine 49
    public int i_2=0;
    public int testno_2=0;
    // JavaLine 27 <== SourceLine 50
    public RTS_TXT activationtimes_2=null;
    public RTS_TXT delaytimes_2=null;
    public RTS_TXT actime_2=null;
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
        // JavaLine 36 <== SourceLine 8
        ut=new RTS_TEXT_ARRAY(new RTS_BOUNDS(1,250));
        answer=new RTS_TEXT_ARRAY(new RTS_BOUNDS(1,250));
        // JavaLine 39 <== SourceLine 47
        pa=new RTS_REF_ARRAY<adHoc00_SimulaTestBegin_SimulationBegin_p>(new RTS_BOUNDS(1,10));
        // JavaLine 41 <== SourceLine 48
        active=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
        passive=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
        terminatd=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
    }
    // Class Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin _STM() {
        // BEGIN Simset INNER PART
        // BEGIN Simulation INNER PART
        // BEGIN SimulationBegin INNER PART
        // ENDOF SimulationBegin INNER PART
        // ENDOF Simulation INNER PART
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock SimulationBegin",11,6,13,8,16,9,18,47,20,48,24,49,27,50,36,8,39,47,41,48,57,54);
} // End of Class
