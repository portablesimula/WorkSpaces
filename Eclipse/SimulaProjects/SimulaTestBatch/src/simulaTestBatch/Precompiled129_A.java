// JavaLine 1 <== SourceLine 8
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 12:09:28 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class Precompiled129_A extends RTS_CLASS {
    // ClassDeclaration: Kind=9, BlockLevel=2, PrefixLevel=0, firstLine=8, lastLine=21, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    public int p_j;
    // Declare local labels
    // JavaLine 11 <== SourceLine 15
    final RTS_LABEL _LABEL_Precompiled129_A_M1_0=new RTS_LABEL(this,0,1,"M1"); // Local Label #1=M1 At PrefixLevel 0
    // JavaLine 13 <== SourceLine 16
    final RTS_LABEL _LABEL_Precompiled129_A_M2_0=new RTS_LABEL(this,0,2,"M2"); // Local Label #2=M2 At PrefixLevel 0
    // JavaLine 15 <== SourceLine 17
    final RTS_LABEL _LABEL_Precompiled129_A_M3_0=new RTS_LABEL(this,0,3,"M3"); // Local Label #3=M3 At PrefixLevel 0
    // JavaLine 17 <== SourceLine 19
    final RTS_LABEL _LABEL_Precompiled129_A_Mxx_0=new RTS_LABEL(this,0,4,"Mxx"); // Local Label #4=Mxx At PrefixLevel 0
    // JavaLine 19 <== SourceLine 21
    final RTS_LABEL _LABEL_Precompiled129_A_EXIT_0=new RTS_LABEL(this,0,5,"EXIT"); // Local Label #5=EXIT At PrefixLevel 0
    // Declare locals as attributes
    // JavaLine 22 <== SourceLine 10
    public final RTS_TXT pre=(RTS_TXT)(new RTS_TXT("Abra "));
    // JavaLine 24 <== SourceLine 11
    public RTS_TXT mess=null;
    // Normal Constructor
    public Precompiled129_A(RTS_RTObject staticLink,int sp_j) {
        super(staticLink);
        // Parameter assignment to locals
        this.p_j = sp_j;
        BBLK(); // Iff no prefix
        // Declaration Code
    }
    // Class Statements
    @Override
    public Precompiled129_A _STM() {
        Precompiled129_A _THIS=(Precompiled129_A)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,5); // For ByteCode Engineering
                // JavaLine 41 <== SourceLine 12
                if(_VALUE(((p_j<(1))|((p_j>(3)))))) {
                    _GOTO(_LABEL_Precompiled129_A_Mxx_0); // GOTO EVALUATED LABEL
                }
                // JavaLine 45 <== SourceLine 13
                _GOTO(new MESSAGE((_CUR),p_j)._RESULT); // GOTO EVALUATED LABEL
                // JavaLine 47 <== SourceLine 15
                {
                    _SIM_LABEL(1); // DeclaredIn: A
                    mess=CONC(new RTS_TXT("Abra "),new RTS_TXT("Message 1"));
                }
                _GOTO(_LABEL_Precompiled129_A_EXIT_0); // GOTO EVALUATED LABEL
                // JavaLine 53 <== SourceLine 16
                {
                    _SIM_LABEL(2); // DeclaredIn: A
                    mess=CONC(new RTS_TXT("Abra "),new RTS_TXT("Message 2"));
                }
                _GOTO(_LABEL_Precompiled129_A_EXIT_0); // GOTO EVALUATED LABEL
                // JavaLine 59 <== SourceLine 17
                {
                    _SIM_LABEL(3); // DeclaredIn: A
                    mess=CONC(new RTS_TXT("Abra "),new RTS_TXT("Message 3"));
                }
                _GOTO(_LABEL_Precompiled129_A_EXIT_0); // GOTO EVALUATED LABEL
                // JavaLine 65 <== SourceLine 19
                {
                    _SIM_LABEL(4); // DeclaredIn: A
                    mess=CONC(CONC(new RTS_TXT("Abra "),new RTS_TXT("Message ")),RTS_ENVIRONMENT.edit(p_j));
                }
                _GOTO(_LABEL_Precompiled129_A_EXIT_0); // GOTO EVALUATED LABEL
                // JavaLine 71 <== SourceLine 21
                {
                    _SIM_LABEL(5); // DeclaredIn: A
                    ;
                }
                // JavaLine 76 <== SourceLine 8
                // BEGIN A INNER PART
                // ENDOF A INNER PART
                break _LOOP;
            }
            catch(RTS_LABEL q) {
                RTS_RTObject._TREAT_GOTO_CATCH_BLOCK(_THIS, q);
                _JTX=q.index; continue _LOOP; // EG. GOTO Lx
            }
        }
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("Precompiled129.sim","Class A",1,8,11,15,13,16,15,17,17,19,19,21,22,10,24,11,41,12,45,13,47,15,53,16,59,17,65,19,71,21,76,8,88,21);
} // End of Class
