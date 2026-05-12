// JavaLine 1 <== SourceLine 33
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin_SimulationBegin_createobjects extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=33, lastLine=40, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 34
    public int i=0;
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin_createobjects(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_createobjects _STM() {
        for(i=1;i<=10;i++) {
            // JavaLine 24 <== SourceLine 36
            {
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.index(i),((adHoc00_SimulaTestBegin_SimulationBegin_p)new adHoc00_SimulaTestBegin_SimulationBegin_p((_CUR._SL),i)._START()));
                // JavaLine 27 <== SourceLine 37
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(i),false);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(i),false);
                ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(i),true);
            }
        }
        // JavaLine 33 <== SourceLine 39
        ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).testno_2=RTS_UTIL._IADD(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).testno_2,1);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure createobjects",1,33,10,34,24,36,27,37,33,39,37,40);
} // End of Procedure
