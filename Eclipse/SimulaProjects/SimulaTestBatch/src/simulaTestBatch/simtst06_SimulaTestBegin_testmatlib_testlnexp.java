// JavaLine 1 <== SourceLine 201
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testlnexp extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=201, lastLine=217, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 203
    public double a=0.0d;
    public double b=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testlnexp setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_x=doubleValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_testlnexp(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testlnexp(RTS_RTObject _SL,double sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testlnexp _STM() {
        // JavaLine 42 <== SourceLine 205
        a=RTS_ENVIRONMENT.exp(RTS_ENVIRONMENT.ln(p_x));
        // JavaLine 44 <== SourceLine 206
        b=RTS_ENVIRONMENT.ln(RTS_ENVIRONMENT.exp(p_x));
        // JavaLine 46 <== SourceLine 208
        if(_VALUE((new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,b)._RESULT|(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),b,p_x)._RESULT)))) {
            // JavaLine 48 <== SourceLine 209
            {
                // JavaLine 50 <== SourceLine 210
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("ln or exp")));
                // JavaLine 52 <== SourceLine 211
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x="));
                // JavaLine 54 <== SourceLine 212
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 57 <== SourceLine 213
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  exp (ln (x) )="));
                RTS_BASICIO.sysout().outreal(a,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 61 <== SourceLine 214
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  ln (exp (x) )="));
                RTS_BASICIO.sysout().outreal(b,16,23);
                // JavaLine 64 <== SourceLine 215
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testlnexp",1,201,11,203,42,205,44,206,46,208,48,209,50,210,52,211,54,212,57,213,61,214,64,215,71,217);
} // End of Procedure
