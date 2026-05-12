// JavaLine 1 <== SourceLine 3
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 12:09:28 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class Precompiled129 extends SimulaTest {
    // ClassDeclaration: Kind=9, BlockLevel=1, PrefixLevel=1, firstLine=3, lastLine=26, hasLocalClasses=true, System=false, detachUsed=false
    // Declare parameters as attributes
    public int p1_i;
    // Declare local labels
    // JavaLine 11 <== SourceLine 22
    final RTS_LABEL _LABEL_Precompiled129_X1_1=new RTS_LABEL(this,1,1,"X1"); // Local Label #1=X1 At PrefixLevel 1
    final RTS_LABEL _LABEL_Precompiled129_X2_1=new RTS_LABEL(this,1,2,"X2"); // Local Label #2=X2 At PrefixLevel 1
    final RTS_LABEL _LABEL_Precompiled129_X3_1=new RTS_LABEL(this,1,3,"X3"); // Local Label #3=X3 At PrefixLevel 1
    // Declare locals as attributes
    // JavaLine 16 <== SourceLine 4
    public Precompiled129_A x_1=null;
    // JavaLine 18 <== SourceLine 5
    public int n_1=0;
    // Normal Constructor
    public Precompiled129(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title,int sp1_i) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        this.p1_i = sp1_i;
        // Declaration Code
    }
    // Class Statements
    @Override
    public Precompiled129 _STM() {
        Precompiled129 _THIS=(Precompiled129)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,3); // For ByteCode Engineering
                // JavaLine 34 <== SourceLine 16
                if(_VALUE(false)) {
                    {
                        // JavaLine 37 <== SourceLine 17
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 41 <== SourceLine 18
                        RTS_BASICIO.sysout().outtext(p_title);
                        // JavaLine 43 <== SourceLine 19
                        RTS_BASICIO.sysout().outimage();
                        RTS_BASICIO.sysout().outimage();
                    }
                }
                // BEGIN SimulaTest INNER PART
                // JavaLine 49 <== SourceLine 22
                {
                    _SIM_LABEL(1); // DeclaredIn: Precompiled129
                    _SIM_LABEL(2); // DeclaredIn: Precompiled129
                    _SIM_LABEL(3); // DeclaredIn: Precompiled129
                    ;
                }
                // JavaLine 56 <== SourceLine 3
                // BEGIN Precompiled129 INNER PART
                // ENDOF Precompiled129 INNER PART
                // ENDOF SimulaTest INNER PART
                // JavaLine 60 <== SourceLine 24
                if(_VALUE(noMessage)) {
                    ;
                } else {
                    {
                        // JavaLine 65 <== SourceLine 25
                        if(_VALUE(found_error)) {
                            {
                                // JavaLine 68 <== SourceLine 26
                                RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                                // JavaLine 73 <== SourceLine 27
                                RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                            }
                        } else {
                            // JavaLine 77 <== SourceLine 28
                            {
                                // JavaLine 79 <== SourceLine 29
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                            }
                        }
                        // JavaLine 86 <== SourceLine 32
                        if(_VALUE(false)) {
                            {
                                // JavaLine 89 <== SourceLine 33
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- END Simula a.s. TEST"));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outimage();
                            }
                        }
                    }
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
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("Precompiled129.sim","Class Precompiled129",1,3,11,22,16,4,18,5,34,16,37,17,41,18,43,19,49,22,56,3,60,24,65,25,68,26,73,27,77,28,79,29,86,32,89,33,106,26);
} // End of Class
