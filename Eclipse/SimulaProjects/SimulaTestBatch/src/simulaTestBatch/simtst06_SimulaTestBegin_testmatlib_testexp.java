// JavaLine 1 <== SourceLine 182
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testexp extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=182, lastLine=198, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    public double p_y;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 184
    public double a=0.0d;
    public double b=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testexp setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testexp(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testexp(RTS_RTObject _SL,double sp_x,double sp_y) {
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
    public simtst06_SimulaTestBegin_testmatlib_testexp _STM() {
        // JavaLine 45 <== SourceLine 186
        a=RTS_ENVIRONMENT.exp((p_x+(p_y)));
        b=(RTS_ENVIRONMENT.exp(p_x)*(RTS_ENVIRONMENT.exp(p_y)));
        // JavaLine 48 <== SourceLine 188
        if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,b)._RESULT)) {
            // JavaLine 50 <== SourceLine 189
            {
                // JavaLine 52 <== SourceLine 190
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("exp")));
                // JavaLine 54 <== SourceLine 191
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x="));
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outtext(new RTS_TXT("           "));
                // JavaLine 58 <== SourceLine 192
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  y=  "));
                RTS_BASICIO.sysout().outreal(p_y,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 62 <== SourceLine 193
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  exp (x+y)=    "));
                RTS_BASICIO.sysout().outreal(a,16,23);
                // JavaLine 65 <== SourceLine 194
                RTS_BASICIO.sysout().outimage();
                // JavaLine 67 <== SourceLine 195
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  exp(x)*exp(y)="));
                RTS_BASICIO.sysout().outreal(b,16,23);
                // JavaLine 70 <== SourceLine 196
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testexp",1,182,12,184,45,186,48,188,50,189,52,190,54,191,58,192,62,193,65,194,67,195,70,196,77,198);
} // End of Procedure
