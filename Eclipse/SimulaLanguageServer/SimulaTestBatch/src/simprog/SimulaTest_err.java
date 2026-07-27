// JavaLine 1 <== SourceLine 9
package simprog;
// Simula-2.0 Compiled at Mon Jul 27 15:20:44 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class SimulaTest_err extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=9, lastLine=9, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_t;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public SimulaTest_err setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_t=(RTS_TXT)objectValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public SimulaTest_err(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public SimulaTest_err(RTS_RTObject _SL,RTS_TXT sp_t) {
        super(_SL);
        // Parameter assignment to locals
        this.p_t = sp_t;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public SimulaTest_err _STM() {
        // JavaLine 39 <== SourceLine 10
        RTS_BASICIO.sysout().setpos(1);
        // JavaLine 41 <== SourceLine 11
        RTS_BASICIO.sysout().outtext(new RTS_TXT("*** error: in test "));
        RTS_BASICIO.sysout().outtext(p_t);
        RTS_BASICIO.sysout().outimage();
        // JavaLine 45 <== SourceLine 12
        ((SimulaTest)(_CUR._SL)).found_error=true;
        ((SimulaTest)(_CUR._SL)).nFailed=RTS_UTIL._IADD(((SimulaTest)(_CUR._SL)).nFailed,1);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/SimulaTest.sim","Procedure err",1,9,39,10,41,11,45,12,50,9);
} // End of Procedure
