// JavaLine 1 <== SourceLine 1
package simprog;
// Simula-2.0 Compiled at Mon Nov 09 15:43:28 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class AdHoc00_Block0_P extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=1, lastLine=1, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_x;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public AdHoc00_Block0_P setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_x=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public AdHoc00_Block0_P(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public AdHoc00_Block0_P(RTS_RTObject _SL,int sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public AdHoc00_Block0_P _STM() {
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/expression/AdHoc00.sim","Procedure P",1,1,41,1);
} // End of Procedure
