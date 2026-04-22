// JavaLine 1 <== SourceLine 7
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 07:49:31 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_A_P extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=7, lastLine=10, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_line;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public adHoc00_Block1_A_P setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_line=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public adHoc00_Block1_A_P(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public adHoc00_Block1_A_P(RTS_RTObject _SL,int sp_line) {
        super(_SL);
        // Parameter assignment to locals
        this.p_line = sp_line;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_Block1_A_P _STM() {
        RTS_BASICIO.sysout().outtext(new RTS_TXT("LINE: "));
        ;
        RTS_BASICIO.sysout().outint(p_line,0);
        ;
        RTS_BASICIO.sysout().outimage();
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure P",1,7,47,10);
} // End of Procedure
