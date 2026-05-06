// JavaLine 1 <== SourceLine 158
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testln extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=158, lastLine=179, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    public double p_y;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 160
    public double a=0.0d;
    public double b=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testln setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testln(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testln(RTS_RTObject _SL,double sp_x,double sp_y) {
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
    public simtst06_SimulaTestBegin_testmatlib_testln _STM() {
        // JavaLine 45 <== SourceLine 162
        a=RTS_ENVIRONMENT.ln((p_x*(p_y)));
        // JavaLine 47 <== SourceLine 163
        b=(RTS_ENVIRONMENT.ln(p_x)+(RTS_ENVIRONMENT.ln(p_y)));
        // JavaLine 49 <== SourceLine 165
        if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,b)._RESULT)) {
            // JavaLine 51 <== SourceLine 166
            {
                // JavaLine 53 <== SourceLine 167
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("ln")));
                // JavaLine 55 <== SourceLine 168
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x="));
                // JavaLine 57 <== SourceLine 169
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                // JavaLine 59 <== SourceLine 170
                RTS_BASICIO.sysout().outtext(new RTS_TXT("             y=  "));
                // JavaLine 61 <== SourceLine 171
                RTS_BASICIO.sysout().outreal(p_y,16,23);
                // JavaLine 63 <== SourceLine 172
                RTS_BASICIO.sysout().outimage();
                // JavaLine 65 <== SourceLine 173
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  ln(x*y)=      "));
                // JavaLine 67 <== SourceLine 174
                RTS_BASICIO.sysout().outreal(a,16,23);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 70 <== SourceLine 175
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  ln(x)+ln(y) = "));
                // JavaLine 72 <== SourceLine 176
                RTS_BASICIO.sysout().outreal(b,16,23);
                // JavaLine 74 <== SourceLine 177
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testln",1,158,12,160,45,162,47,163,49,165,51,166,53,167,55,168,57,169,59,170,61,171,63,172,65,173,67,174,70,175,72,176,74,177,81,179);
} // End of Procedure
