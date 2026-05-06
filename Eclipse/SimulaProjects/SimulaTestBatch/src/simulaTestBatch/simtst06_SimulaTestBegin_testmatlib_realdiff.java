// JavaLine 1 <== SourceLine 29
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_realdiff extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=29, lastLine=54, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public double p_x;
    public double p_y;
    // Declare locals as attributes
    // JavaLine 14 <== SourceLine 31
    public double diff=0.0d;
    public int pow=0;
    // JavaLine 17 <== SourceLine -23
    public boolean _RESULT=false;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_realdiff setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_x=doubleValue(param); break;
                case 1: p_y=doubleValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_realdiff(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_realdiff(RTS_RTObject _SL,double sp_x,double sp_y) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        this.p_y = sp_y;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_realdiff _STM() {
        // JavaLine 49 <== SourceLine 41
        diff=RTS_ENVIRONMENT.abs((((p_x==(0.0d)))?((p_x-(p_y))):(((p_x-(p_y))/(p_x)))));
        // JavaLine 51 <== SourceLine 43
        if(_VALUE((diff>(1.000000013351432E-10d)))) {
            // JavaLine 53 <== SourceLine 44
            {
                // JavaLine 55 <== SourceLine 45
                RTS_BASICIO.sysout().outtext(new RTS_TXT("x="));
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                // JavaLine 58 <== SourceLine 46
                RTS_BASICIO.sysout().outtext(new RTS_TXT(", y="));
                RTS_BASICIO.sysout().outreal(p_y,16,23);
                // JavaLine 61 <== SourceLine 47
                RTS_BASICIO.sysout().outtext(new RTS_TXT(", x-y="));
                RTS_BASICIO.sysout().outreal((p_x-(p_y)),16,23);
                // JavaLine 64 <== SourceLine 48
                RTS_BASICIO.sysout().outtext(new RTS_TXT(", (x-y)/x="));
                RTS_BASICIO.sysout().outreal(((p_x-(p_y))/(p_x)),16,23);
                // JavaLine 67 <== SourceLine 49
                RTS_BASICIO.sysout().outtext(new RTS_TXT(" *** rel. error= "));
                // JavaLine 69 <== SourceLine 50
                RTS_BASICIO.sysout().outreal(diff,16,23);
                // JavaLine 71 <== SourceLine 51
                RTS_BASICIO.sysout().outimage();
                // JavaLine 73 <== SourceLine 52
                _RESULT=true;
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure realdiff",1,29,14,31,17,-23,49,41,51,43,53,44,55,45,58,46,61,47,64,48,67,49,69,50,71,51,73,52,79,54);
} // End of Procedure
