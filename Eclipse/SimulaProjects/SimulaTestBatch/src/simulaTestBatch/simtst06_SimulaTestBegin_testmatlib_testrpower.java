// JavaLine 1 <== SourceLine 82
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testrpower extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=82, lastLine=106, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public double p_b;
    public double p_e;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 84
    public double x=0.0d;
    public double y=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testrpower setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_b=doubleValue(param); break;
                case 1: p_e=doubleValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_testrpower(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testrpower(RTS_RTObject _SL,double sp_b,double sp_e) {
        super(_SL);
        // Parameter assignment to locals
        this.p_b = sp_b;
        this.p_e = sp_e;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testrpower _STM() {
        // JavaLine 45 <== SourceLine 86
        if(_VALUE((p_b>(0.0d)))) {
            // JavaLine 47 <== SourceLine 87
            {
                // JavaLine 49 <== SourceLine 88
                x=new simtst06_SimulaTestBegin_testmatlib_rpower((_CUR._SL),p_b,p_e)._RESULT;
                y=Math.pow(p_b,p_e);
                // JavaLine 52 <== SourceLine 90
                if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),x,y)._RESULT)) {
                    // JavaLine 54 <== SourceLine 91
                    {
                        // JavaLine 56 <== SourceLine 92
                        new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("rpower")));
                        // JavaLine 58 <== SourceLine 93
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  base="));
                        // JavaLine 60 <== SourceLine 94
                        RTS_BASICIO.sysout().outreal(p_b,16,23);
                        // JavaLine 62 <== SourceLine 95
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("          exp="));
                        // JavaLine 64 <== SourceLine 96
                        RTS_BASICIO.sysout().outreal(p_e,16,23);
                        // JavaLine 66 <== SourceLine 97
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 68 <== SourceLine 98
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  exp(y*ln(x))= "));
                        // JavaLine 70 <== SourceLine 99
                        RTS_BASICIO.sysout().outreal(x,16,23);
                        // JavaLine 72 <== SourceLine 100
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 74 <== SourceLine 101
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  base**exp=    "));
                        // JavaLine 76 <== SourceLine 102
                        RTS_BASICIO.sysout().outreal(y,16,23);
                        // JavaLine 78 <== SourceLine 103
                        RTS_BASICIO.sysout().outimage();
                        RTS_BASICIO.sysout().outimage();
                    }
                }
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testrpower",1,82,12,84,45,86,47,87,49,88,52,90,54,91,56,92,58,93,60,94,62,95,64,96,66,97,68,98,70,99,72,100,74,101,76,102,78,103,87,106);
} // End of Procedure
