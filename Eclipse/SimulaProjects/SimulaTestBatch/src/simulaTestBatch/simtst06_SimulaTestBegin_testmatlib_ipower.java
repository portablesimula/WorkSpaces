// JavaLine 1 <== SourceLine 61
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_ipower extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=61, lastLine=79, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public double p_b;
    public int p_j;
    // Declare locals as attributes
    // JavaLine 14 <== SourceLine 63
    public int exp=0;
    public double prod=0.0d;
    public double base=0.0d;
    // JavaLine 18 <== SourceLine -23
    public double _RESULT=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_ipower setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_b=doubleValue(param); break;
                case 1: p_j=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_ipower(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_ipower(RTS_RTObject _SL,double sp_b,int sp_j) {
        super(_SL);
        // Parameter assignment to locals
        this.p_b = sp_b;
        this.p_j = sp_j;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_ipower _STM() {
        // JavaLine 50 <== SourceLine 68
        base=p_b;
        exp=(int)Math.round(RTS_ENVIRONMENT.abs(((double)(p_j))));
        // JavaLine 53 <== SourceLine 69
        prod=((new simtst06_SimulaTestBegin_testmatlib_ipower_bit0((_CUR),exp)._RESULT)?(base):(1.0d));
        // JavaLine 55 <== SourceLine 71
        while((exp>(1))) {
            // JavaLine 57 <== SourceLine 72
            {
                // JavaLine 59 <== SourceLine 73
                base=(base*(base));
                exp=(exp/(2));
                // JavaLine 62 <== SourceLine 74
                if(_VALUE(new simtst06_SimulaTestBegin_testmatlib_ipower_bit0((_CUR),exp)._RESULT)) {
                    prod=(prod*(base));
                }
            }
        }
        // JavaLine 68 <== SourceLine 77
        _RESULT=(((p_j>(0)))?(prod):((1.0d/(prod))));
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure ipower",1,61,14,63,18,-23,50,68,53,69,55,71,57,72,59,73,62,74,68,77,72,79);
} // End of Procedure
