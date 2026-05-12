// JavaLine 1 <== SourceLine 11
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin_SimulationBegin_outimage extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=11, lastLine=15, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin_outimage(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_outimage _STM() {
        // JavaLine 21 <== SourceLine 12
        ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2=RTS_UTIL._IADD(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2,1);
        ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ut.putELEMENT(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ut.index(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2),RTS_ENVIRONMENT.copy(copy(RTS_TXT.strip(RTS_BASICIO.sysout().image))));
        // JavaLine 24 <== SourceLine 13
        RTS_UTIL._ASGTXT(RTS_BASICIO.sysout().image,null);
        // JavaLine 26 <== SourceLine 14
        RTS_BASICIO.sysout().setpos(1);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure outimage",1,11,21,12,24,13,26,14,30,15);
} // End of Procedure
