// JavaLine 1 <== SourceLine 17
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class adHoc00_SimulaTestBegin_SimulationBegin_p extends RTS_Process {
    // ClassDeclaration: Kind=9, BlockLevel=3, PrefixLevel=3, firstLine=17, lastLine=25, hasLocalClasses=false, System=false, detachUsed=false
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    public int p3_i;
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin_p(RTS_RTObject staticLink,int sp3_i) {
        super(staticLink);
        // Parameter assignment to locals
        this.p3_i = sp3_i;
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_p _STM() {
        // BEGIN Linkage INNER PART
        // BEGIN Link INNER PART
        // JavaLine 24 <== SourceLine -23
        detach(); // Process'detach
        // BEGIN Process INNER PART
        // JavaLine 27 <== SourceLine 18
        if(_VALUE(false)) {
            {
                // JavaLine 30 <== SourceLine 19
                RTS_BASICIO.sysout().outtext(new RTS_TXT("p("));
                RTS_BASICIO.sysout().outint(p3_i,2);
                // JavaLine 33 <== SourceLine 20
                RTS_BASICIO.sysout().outtext(new RTS_TXT(") activated at time = "));
                RTS_BASICIO.sysout().outfix(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time(),3,8);
                new adHoc00_SimulaTestBegin_SimulationBegin_outimage((_CUR._SL));
                // JavaLine 37 <== SourceLine 21
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(p3_i),false);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(p3_i),false);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(p3_i),true);
                // JavaLine 41 <== SourceLine 22
                new adHoc00_SimulaTestBegin_SimulationBegin_outstate((_CUR._SL),p3_i);
                // JavaLine 43 <== SourceLine 23
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(p3_i),false);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(p3_i),true);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(p3_i),false);
            }
        }
        // JavaLine 49 <== SourceLine 17
        // BEGIN p INNER PART
        // ENDOF p INNER PART
        // ENDOF Process INNER PART
        // JavaLine 53 <== SourceLine -23
        terminate(); // Process'terminate
        // ENDOF Link INNER PART
        // ENDOF Linkage INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Class p",1,17,24,-23,27,18,30,19,33,20,37,21,41,22,43,23,49,17,53,-23,59,25);
} // End of Class
