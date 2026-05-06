// JavaLine 1 <== SourceLine 257
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testepsilon extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=257, lastLine=268, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_x;
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 259
    public double a=0.0d;
    public double b=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testepsilon setPar(Object param) {
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
    public simtst06_SimulaTestBegin_testmatlib_testepsilon(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testepsilon(RTS_RTObject _SL,double sp_x) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testepsilon _STM() {
        // JavaLine 42 <== SourceLine 260
        a=RTS_ENVIRONMENT.subepsilon(RTS_ENVIRONMENT.addepsilon(p_x));
        // JavaLine 44 <== SourceLine 261
        b=RTS_ENVIRONMENT.addepsilon(RTS_ENVIRONMENT.subepsilon(p_x));
        // JavaLine 46 <== SourceLine 262
        if(_VALUE((new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),a,p_x)._RESULT|(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),b,p_x)._RESULT)))) {
            // JavaLine 48 <== SourceLine 263
            {
                // JavaLine 50 <== SourceLine 264
                new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("Add/sub-epsilon")));
                // JavaLine 52 <== SourceLine 265
                RTS_BASICIO.sysout().outtext(new RTS_TXT("  x="));
                // JavaLine 54 <== SourceLine 266
                RTS_BASICIO.sysout().outreal(p_x,16,23);
                RTS_BASICIO.sysout().outimage();
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testepsilon",1,257,11,259,42,260,44,261,46,262,48,263,50,264,52,265,54,266,61,268);
} // End of Procedure
