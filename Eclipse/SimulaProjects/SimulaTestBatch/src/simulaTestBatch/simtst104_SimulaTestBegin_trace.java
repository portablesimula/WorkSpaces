package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104_SimulaTestBegin_trace extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=13, lastLine=23, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_msg;
    public float p_r;
    public float p_facit;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst104_SimulaTestBegin_trace setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 3: p_msg=(RTS_TXT)objectValue(param); break;
                case 2: p_r=floatValue(param); break;
                case 1: p_facit=floatValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst104_SimulaTestBegin_trace(RTS_RTObject _SL) {
        super(_SL,3); // Expecting 3 parameters
    }
    // Normal Constructor
    public simtst104_SimulaTestBegin_trace(RTS_RTObject _SL,RTS_TXT sp_msg,float sp_r,float sp_facit) {
        super(_SL);
        // Parameter assignment to locals
        this.p_msg = sp_msg;
        this.p_r = sp_r;
        this.p_facit = sp_facit;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst104_SimulaTestBegin_trace _STM() {
        // JavaLine 44 <== SourceLine 17
        if(_VALUE((false|((p_r!=(p_facit)))))) {
            new simtst104_SimulaTestBegin_trace_Block18((_CUR))._STM();
        }
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","Procedure trace",44,17,51,23);
} // End of Procedure
