// JavaLine 1 <== SourceLine 25
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Jul 31 11:16:48 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114_PBLK11_test extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=25, lastLine=54, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_key;
    public RTS_TXT p_msg;
    // Declare local labels
    // JavaLine 12 <== SourceLine 54
    final RTS_LABEL _LABEL_simtst114_PBLK11_test__BREAK_0=new RTS_LABEL(this,0,1,"_BREAK"); // Local Label #1=_BREAK At PrefixLevel 0
    // Declare locals as attributes
    // JavaLine 15 <== SourceLine 27
    public final int lowKey=(int)(0);
    // JavaLine 17 <== SourceLine 28
    public final int hiKey=(int)(5);
    // JavaLine 19 <== SourceLine 29
    public final int case1=(int)(1);
    // JavaLine 21 <== SourceLine 30
    public final int case2=(int)(2);
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst114_PBLK11_test setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_key=intValue(param); break;
                case 1: p_msg=(RTS_TXT)objectValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst114_PBLK11_test(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst114_PBLK11_test(RTS_RTObject _SL,int sp_key,RTS_TXT sp_msg) {
        super(_SL);
        // Parameter assignment to locals
        this.p_key = sp_key;
        this.p_msg = sp_msg;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst114_PBLK11_test _STM() {
        simtst114_PBLK11_test _THIS=(simtst114_PBLK11_test)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                // JavaLine 57 <== SourceLine 32
                if(p_key<0 || p_key>5) throw new RTS_SimulaRuntimeError("Switch key outside key interval");
                switch(p_key) { // BEGIN SWITCH STATEMENT
                    case 1: 
                    // JavaLine 61 <== SourceLine 34
                    {
                        // JavaLine 63 <== SourceLine 35
                        new simtst114_PBLK11_trace((_CUR._SL),CONC(p_msg,new RTS_TXT(" selected case 1")));
                        ;
                        // JavaLine 66 <== SourceLine 36
                        _GOTO(_LABEL_simtst114_PBLK11_test__BREAK_0); // GOTO EVALUATED LABEL
                        ;
                    }
                    break;
                    case 2: 
                    // JavaLine 72 <== SourceLine 39
                    {
                        // JavaLine 74 <== SourceLine 40
                        new simtst114_PBLK11_trace((_CUR._SL),CONC(p_msg,new RTS_TXT(" selected case 2")));
                        ;
                        // JavaLine 77 <== SourceLine 41
                        _GOTO(_LABEL_simtst114_PBLK11_test__BREAK_0); // GOTO EVALUATED LABEL
                        ;
                    }
                    break;
                    case 3: 
                    // JavaLine 83 <== SourceLine 44
                    {
                        // JavaLine 85 <== SourceLine 45
                        new simtst114_PBLK11_trace((_CUR._SL),CONC(p_msg,new RTS_TXT(" selected case 3")));
                        ;
                        // JavaLine 88 <== SourceLine 46
                        _GOTO(_LABEL_simtst114_PBLK11_test__BREAK_0); // GOTO EVALUATED LABEL
                        ;
                    }
                    break;
                    default:
                    // JavaLine 94 <== SourceLine 49
                    {
                        // JavaLine 96 <== SourceLine 50
                        new simtst114_PBLK11_trace((_CUR._SL),CONC(p_msg,new RTS_TXT(" selected case NONE")));
                        ;
                        // JavaLine 99 <== SourceLine 51
                        _GOTO(((simtst114_PBLK11)(_CUR._SL))._LABEL_simtst114_PBLK11_NOMORE_1); // GOTO EVALUATED LABEL
                        ;
                    }
                    break;
                } // END SWITCH STATEMENT
                ;
                // JavaLine 106 <== SourceLine 54
                {
                    _SIM_LABEL(1); // DeclaredIn: test
                    ;
                }
                break _LOOP;
            }
            catch(RTS_LABEL q) {
                RTS_RTObject._TREAT_GOTO_CATCH_BLOCK(_THIS, q);
                _JTX=q.index; continue _LOOP; // EG. GOTO Lx
            }
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst114.sim","Procedure test",1,25,12,54,15,27,17,28,19,29,21,30,57,32,61,34,63,35,66,36,72,39,74,40,77,41,83,44,85,45,88,46,94,49,96,50,99,51,106,54,120,54);
} // End of Procedure
