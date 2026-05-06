// JavaLine 1 <== SourceLine 220
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testsincos extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=220, lastLine=236, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 222
    public double a=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testsincos setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testsincos(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testsincos(RTS_RTObject _SL,double sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testsincos _STM() {
        // JavaLine 41 <== SourceLine 224
        a=(Math.pow(RTS_ENVIRONMENT.sin(p_x),2.0d)+(Math.pow(RTS_ENVIRONMENT.cos(p_x),2.0d)));
        // JavaLine 43 <== SourceLine 226
        if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,1.0d)._RESULT)) {
            // JavaLine 45 <== SourceLine 227
            {
                // JavaLine 47 <== SourceLine 228
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("sin or cos")));
                // JavaLine 49 <== SourceLine 229
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x="));
                // JavaLine 51 <== SourceLine 230
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 54 <== SourceLine 231
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  sin(x)**2+cos(x)**2="));
                // JavaLine 56 <== SourceLine 232
                RTS_BASICIO.sysout().outreal(a,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 59 <== SourceLine 233
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  Theoretical value=   1.0"));
                // JavaLine 61 <== SourceLine 234
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testsincos",1,220,11,222,41,224,43,226,45,227,47,228,49,229,51,230,54,231,56,232,59,233,61,234,68,236);
} // End of Procedure
