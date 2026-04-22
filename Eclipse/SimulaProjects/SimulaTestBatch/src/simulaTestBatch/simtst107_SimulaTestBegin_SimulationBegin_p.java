// JavaLine 1 <== SourceLine 23
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class simtst107_SimulaTestBegin_SimulationBegin_p extends RTS_Process {
    // ClassDeclaration: Kind=9, BlockLevel=3, PrefixLevel=3, firstLine=23, lastLine=33, hasLocalClasses=false, System=false, detachUsed=false
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    public int p3_i;
    // Declare locals as attributes
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_p(RTS_RTObject staticLink,int sp3_i) {
        super(staticLink);
        // Parameter assignment to locals
        this.p3_i = sp3_i;
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_p _STM() {
        // BEGIN Linkage INNER PART
        // BEGIN Link INNER PART
        // JavaLine 24 <== SourceLine -25
        detach(); // Process'detach
        // BEGIN Process INNER PART
        // JavaLine 27 <== SourceLine 26
        if(_VALUE(false)) {
            {
                RTS_BASICIO.sysout().outtext(new RTS_TXT("p("));
                ;
                RTS_BASICIO.sysout().outint(p3_i,2);
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT(") activated at time = "));
                ;
                RTS_BASICIO.sysout().outfix(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time(),3,8);
                ;
                new simtst107_SimulaTestBegin_SimulationBegin_outimage((_CUR._SL));
                ;
                // JavaLine 40 <== SourceLine 29
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(p3_i),false);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(p3_i),false);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(p3_i),true);
                ;
                // JavaLine 47 <== SourceLine 26
                new simtst107_SimulaTestBegin_SimulationBegin_outstate((_CUR._SL),p3_i);
                ;
                // JavaLine 50 <== SourceLine 31
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(p3_i),false);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(p3_i),true);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(p3_i),false);
                ;
            }
        }
        ;
        // JavaLine 60 <== SourceLine 33
        // BEGIN p INNER PART
        // ENDOF p INNER PART
        // ENDOF Process INNER PART
        // JavaLine 64 <== SourceLine -25
        terminate(); // Process'terminate
        // ENDOF Link INNER PART
        // ENDOF Linkage INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Class p",1,23,24,-25,27,26,40,29,47,26,50,31,60,33,64,-25,70,33);
} // End of Class
