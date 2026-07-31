// JavaLine 1 <== SourceLine 24
package simprog;
// Simula-2.0 Compiled at Fri Jul 31 10:32:52 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114_SimulaTest_Begin_test extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=24, lastLine=24, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_key;
    public RTS_TXT p_msg;
    // Declare local labels
    // JavaLine 12 <== SourceLine 53
    final RTS_LABEL _LABEL_simtst114_SimulaTest_Begin_test_BREAK_0=new RTS_LABEL(this,0,1,"BREAK"); // Local Label #1=BREAK At PrefixLevel 0
    // Declare locals as attributes
    // JavaLine 15 <== SourceLine 26
    public final int lowKey=(int)(0);
    // JavaLine 17 <== SourceLine 27
    public final int hiKey=(int)(5);
    // JavaLine 19 <== SourceLine 28
    public final int case1=(int)(1);
    // JavaLine 21 <== SourceLine 29
    public final int case2=(int)(2);
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst114_SimulaTest_Begin_test setPar(Object param) {
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
    public simtst114_SimulaTest_Begin_test(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst114_SimulaTest_Begin_test(RTS_RTObject _SL,int sp_key,RTS_TXT sp_msg) {
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
    public simtst114_SimulaTest_Begin_test _STM() {
        simtst114_SimulaTest_Begin_test _THIS=(simtst114_SimulaTest_Begin_test)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                // JavaLine 57 <== SourceLine 31
                0;
                ;
                5;
                ;
                // JavaLine 62 <== SourceLine 52
                new simtst114_SimulaTest_Begin_test_key_Begin((_CUR))._STM();
                // JavaLine 64 <== SourceLine 53
                {
                    _SIM_LABEL(1); // DeclaredIn: Line 24: IDENTIFIER[col:13, lng:4] Text: "test", Value: "test"
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/simtst114.sim","Procedure test",1,24,12,53,15,26,17,27,19,28,21,29,57,31,62,52,64,53,78,24);
} // End of Procedure
