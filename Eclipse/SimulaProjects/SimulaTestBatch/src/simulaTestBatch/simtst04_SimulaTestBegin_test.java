// JavaLine 1 <== SourceLine 41
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:55:09 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst04_SimulaTestBegin_test extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=41, lastLine=55, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_msg;
    public RTS_TXT p_result;
    public RTS_TXT p_expected;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst04_SimulaTestBegin_test setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 3: p_msg=(RTS_TXT)objectValue(param); break;
                case 2: p_result=(RTS_TXT)objectValue(param); break;
                case 1: p_expected=(RTS_TXT)objectValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst04_SimulaTestBegin_test(RTS_RTObject _SL) {
        super(_SL,3); // Expecting 3 parameters
    }
    // Normal Constructor
    public simtst04_SimulaTestBegin_test(RTS_RTObject _SL,RTS_TXT sp_msg,RTS_TXT sp_result,RTS_TXT sp_expected) {
        super(_SL);
        // Parameter assignment to locals
        this.p_msg = sp_msg;
        this.p_result = sp_result;
        this.p_expected = sp_expected;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst04_SimulaTestBegin_test _STM() {
        // JavaLine 45 <== SourceLine 46
        if(_VALUE(RTS_UTIL._TXTREL_NE(p_result,p_expected))) {
            // JavaLine 47 <== SourceLine 47
            {
                new SimulaTest_err((_CUR._SL),CONC(new RTS_TXT("*** error :  In "),p_msg));
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT(" - the following result is wrong: "));
                ;
                RTS_BASICIO.sysout().outchar('"');
                ;
                RTS_BASICIO.sysout().outtext(((simtst04_SimulaTestBegin)(_CUR._SL)).txt_1);
                ;
                RTS_BASICIO.sysout().outchar('"');
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT(" -               expected result: "));
                ;
                RTS_BASICIO.sysout().outchar('"');
                ;
                RTS_BASICIO.sysout().outtext(p_expected);
                ;
                RTS_BASICIO.sysout().outchar('"');
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst04.sim","Procedure test",1,41,45,46,47,47,76,55);
} // End of Procedure
