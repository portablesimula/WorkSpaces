// JavaLine 1 <== SourceLine 5
package simprog;
// Simula-2.0 Compiled at Tue Aug 04 08:32:08 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc05_Block0_CatchingErrors_Begin_onError extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=5, lastLine=5, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_message;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public adHoc05_Block0_CatchingErrors_Begin_onError setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_message=(RTS_TXT)objectValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public adHoc05_Block0_CatchingErrors_Begin_onError(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public adHoc05_Block0_CatchingErrors_Begin_onError(RTS_RTObject _SL,RTS_TXT sp_message) {
        super(_SL);
        // Parameter assignment to locals
        this.p_message = sp_message;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc05_Block0_CatchingErrors_Begin_onError _STM() {
        // JavaLine 39 <== SourceLine 6
        RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("Inside onError: "),p_message));
        // JavaLine 41 <== SourceLine 7
        _GOTO(((adHoc05)_USR)._LABEL_adHoc05_Block0_LAB2_0); // GOTO EVALUATED LABEL
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc05.sim","Procedure onError",1,5,39,6,41,7,45,5);
} // End of Procedure
