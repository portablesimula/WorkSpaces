// JavaLine 1 <== SourceLine 140
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testsqrt extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=140, lastLine=155, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 142
    public double a=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testsqrt setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testsqrt(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testsqrt(RTS_RTObject _SL,double sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testsqrt _STM() {
        // JavaLine 41 <== SourceLine 144
        a=Math.pow(RTS_ENVIRONMENT.sqrt(p_x),2.0d);
        // JavaLine 43 <== SourceLine 146
        if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,p_x)._RESULT)) {
            // JavaLine 45 <== SourceLine 147
            {
                // JavaLine 47 <== SourceLine 148
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("sqrt")));
                // JavaLine 49 <== SourceLine 149
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x=            "));
                // JavaLine 51 <== SourceLine 150
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 54 <== SourceLine 151
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  sqrt(x)**2=   "));
                // JavaLine 56 <== SourceLine 152
                RTS_BASICIO.sysout().outreal(a,16,23);
                // JavaLine 58 <== SourceLine 153
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testsqrt",1,140,11,142,41,144,43,146,45,147,47,148,49,149,51,150,54,151,56,152,58,153,65,155);
} // End of Procedure
