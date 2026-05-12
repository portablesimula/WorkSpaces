// JavaLine 1 <== SourceLine 30
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin_SimulationBegin_outstate extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=30, lastLine=31, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_c;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_outstate setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_c=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public adHoc00_SimulaTestBegin_SimulationBegin_outstate(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin_outstate(RTS_RTObject _SL,int sp_c) {
        super(_SL);
        // Parameter assignment to locals
        this.p_c = sp_c;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_outstate _STM() {
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure outstate",1,30,41,31);
} // End of Procedure
