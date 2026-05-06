// JavaLine 1 <== SourceLine 239
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testarctan extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=239, lastLine=255, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 241
    public double a=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testarctan setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testarctan(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testarctan(RTS_RTObject _SL,double sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testarctan _STM() {
        // JavaLine 41 <== SourceLine 243
        a=RTS_ENVIRONMENT.arctan((RTS_ENVIRONMENT.sin(p_x)/(RTS_ENVIRONMENT.cos(p_x))));
        // JavaLine 43 <== SourceLine 245
        if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,p_x)._RESULT)) {
            // JavaLine 45 <== SourceLine 246
            {
                // JavaLine 47 <== SourceLine 247
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("arctan")));
                // JavaLine 49 <== SourceLine 248
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x=                     "));
                // JavaLine 51 <== SourceLine 249
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 54 <== SourceLine 250
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  arctan(sin(x)/cos(x))= "));
                // JavaLine 56 <== SourceLine 251
                RTS_BASICIO.sysout().outreal(a,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 59 <== SourceLine 252
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  Theoretical value = x"));
                // JavaLine 61 <== SourceLine 253
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testarctan",1,239,11,241,41,243,43,245,45,246,47,247,49,248,51,249,54,250,56,251,59,252,61,253,68,255);
} // End of Procedure
