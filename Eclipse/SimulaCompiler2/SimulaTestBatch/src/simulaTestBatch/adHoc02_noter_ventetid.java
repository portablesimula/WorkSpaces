// JavaLine 1 <== SourceLine 33
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Oct 09 12:26:39 CEST 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc02_noter_ventetid extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=33, lastLine=42, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public float p_Start;
    public float p_slutt;
    public int p_i_koe;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine 37
    public float tid=0.0f;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public adHoc02_noter_ventetid setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 3: p_Start=floatValue(param); break;
                case 2: p_slutt=floatValue(param); break;
                case 1: p_i_koe=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public adHoc02_noter_ventetid(RTS_RTObject _SL) {
        super(_SL,3); // Expecting 3 parameters
    }
    // Normal Constructor
    public adHoc02_noter_ventetid(RTS_RTObject _SL,float sp_Start,float sp_slutt,int sp_i_koe) {
        super(_SL);
        // Parameter assignment to locals
        this.p_Start = sp_Start;
        this.p_slutt = sp_slutt;
        this.p_i_koe = sp_i_koe;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc02_noter_ventetid _STM() {
        tid=(p_slutt-(p_Start));
        ;
        // JavaLine 49 <== SourceLine 38
        ((adHoc02)(_CUR._SL)).sum_venting_2=(((adHoc02)(_CUR._SL)).sum_venting_2+(tid));
        ;
        // JavaLine 52 <== SourceLine 39
        ((adHoc02)(_CUR._SL)).antall_ventinger_2=RTS_UTIL._IADD(((adHoc02)(_CUR._SL)).antall_ventinger_2,1);
        ;
        // JavaLine 55 <== SourceLine 40
        if(_VALUE((tid>(((adHoc02)(_CUR._SL)).maks_venting_2)))) {
            ((adHoc02)(_CUR._SL)).maks_venting_2=tid;
        }
        ;
        // JavaLine 60 <== SourceLine 41
        if(_VALUE((p_i_koe>(((adHoc02)(_CUR._SL)).maks_i_koe_2)))) {
            ((adHoc02)(_CUR._SL)).maks_i_koe_2=p_i_koe;
        }
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc02.sim","6 noter_ventetid",1,33,13,37,49,38,52,39,55,40,60,41,67,42);
} // End of Procedure
