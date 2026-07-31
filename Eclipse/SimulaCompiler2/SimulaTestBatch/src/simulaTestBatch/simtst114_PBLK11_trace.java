// JavaLine 1 <== SourceLine 14
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Jul 31 11:16:48 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114_PBLK11_trace extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=14, lastLine=23, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_msg;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst114_PBLK11_trace setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_msg=(RTS_TXT)objectValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst114_PBLK11_trace(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst114_PBLK11_trace(RTS_RTObject _SL,RTS_TXT sp_msg) {
        super(_SL);
        // Parameter assignment to locals
        this.p_msg = sp_msg;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst114_PBLK11_trace _STM() {
        // JavaLine 39 <== SourceLine 16
        if(_VALUE(false)) {
            {
                RTS_BASICIO.sysout().outtext(p_msg);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 49 <== SourceLine 17
        if(_VALUE(RTS_UTIL._TXTREL_NE(p_msg,((simtst114_PBLK11)(_CUR._SL)).facit.getELEMENT(((simtst114_PBLK11)(_CUR._SL)).traceCase_1)))) {
            {
                // JavaLine 52 <== SourceLine 18
                new SimulaTest_err((_CUR._SL),CONC(new RTS_TXT("Case "),RTS_ENVIRONMENT.edit(((simtst114_PBLK11)(_CUR._SL)).traceCase_1)));
                ;
                // JavaLine 55 <== SourceLine 19
                RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("Trace: "),p_msg));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                // JavaLine 60 <== SourceLine 20
                RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("Facit: "),((simtst114_PBLK11)(_CUR._SL)).facit.getELEMENT(((simtst114_PBLK11)(_CUR._SL)).traceCase_1)));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 68 <== SourceLine 22
        ((simtst114_PBLK11)(_CUR._SL)).traceCase_1=RTS_UTIL._IADD(((simtst114_PBLK11)(_CUR._SL)).traceCase_1,1);
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst114.sim","Procedure trace",1,14,39,16,49,17,52,18,55,19,60,20,68,22,73,23);
} // End of Procedure
