// JavaLine 1 <== SourceLine 17
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin_outimage extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=17, lastLine=23, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_outimage(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_outimage _STM() {
        // JavaLine 21 <== SourceLine 20
        ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2=RTS_UTIL._IADD(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2,1);
        ;
        ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ut.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ut.index(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).utno_2),RTS_ENVIRONMENT.copy(copy(RTS_TXT.strip(RTS_BASICIO.sysout().image))));
        ;
        // JavaLine 26 <== SourceLine 21
        RTS_UTIL._ASGTXT(RTS_BASICIO.sysout().image,null);
        ;
        // JavaLine 29 <== SourceLine 17
        RTS_BASICIO.sysout().setpos(1);
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Procedure outimage",1,17,21,20,26,21,29,17,34,23);
} // End of Procedure
