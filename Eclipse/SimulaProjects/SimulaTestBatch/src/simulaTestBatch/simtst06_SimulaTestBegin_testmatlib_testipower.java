// JavaLine 1 <== SourceLine 109
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_testipower extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=109, lastLine=137, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_b;
    public int p_e;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 111
    public double x=0.0d;
    public double y=0.0d;
    public double z=0.0d;
    public double r=0.0d;
    // JavaLine 17 <== SourceLine 112
    public boolean n=false;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_testipower setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_b=intValue(param); break;
                case 1: p_e=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_testipower(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_testipower(RTS_RTObject _SL,int sp_b,int sp_e) {
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
    public simtst06_SimulaTestBegin_testmatlib_testipower _STM() {
        // JavaLine 49 <== SourceLine 114
        r=((double)(p_b));
        // JavaLine 51 <== SourceLine 115
        if(_VALUE(((p_b!=(0))&((p_e!=(0)))))) {
            // JavaLine 53 <== SourceLine 116
            {
                // JavaLine 55 <== SourceLine 117
                if(_VALUE((p_e<(0)))) {
                    {
                        n=true;
                        p_e=(-(p_e));
                    }
                }
                // JavaLine 62 <== SourceLine 119
                x=new simtst06_SimulaTestBegin_testmatlib_ipower((_CUR._SL),((double)(p_b)),p_e)._RESULT;
                y=((double)(RTS_UTIL._IPOW(p_b,p_e)));
                z=Math.pow(r,((double)(p_e)));
                // JavaLine 66 <== SourceLine 121
                if(_VALUE((new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),x,y)._RESULT|(new simtst06_SimulaTestBegin_testmatlib_realdiff((_CUR._SL),y,z)._RESULT)))) {
                    // JavaLine 68 <== SourceLine 122
                    {
                        // JavaLine 70 <== SourceLine 123
                        if(_VALUE(n)) {
                            p_e=(-(p_e));
                        }
                        // JavaLine 74 <== SourceLine 124
                        new simtst06_SimulaTestBegin_testmatlib_error((_CUR._SL),copy(new RTS_TXT("ipower")));
                        // JavaLine 76 <== SourceLine 125
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  base="));
                        // JavaLine 78 <== SourceLine 126
                        RTS_BASICIO.sysout().outint(p_b,12);
                        // JavaLine 80 <== SourceLine 127
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("                     exp="));
                        // JavaLine 82 <== SourceLine 128
                        RTS_BASICIO.sysout().outint(p_e,12);
                        // JavaLine 84 <== SourceLine 129
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 86 <== SourceLine 130
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  base**abs(exp) ="));
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 89 <== SourceLine 131
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  ipower: "));
                        RTS_BASICIO.sysout().outreal(x,16,23);
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 93 <== SourceLine 132
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  i**i:   "));
                        RTS_BASICIO.sysout().outreal(y,16,23);
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 97 <== SourceLine 133
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  r**i:   "));
                        RTS_BASICIO.sysout().outreal(z,16,23);
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 101 <== SourceLine 134
                        RTS_BASICIO.sysout().outimage();
                    }
                }
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure testipower",1,109,12,111,17,112,49,114,51,115,53,116,55,117,62,119,66,121,68,122,70,123,74,124,76,125,78,126,80,127,82,128,84,129,86,130,89,131,93,132,97,133,101,134,109,137);
} // End of Procedure
