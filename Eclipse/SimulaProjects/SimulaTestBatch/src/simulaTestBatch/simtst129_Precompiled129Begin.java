package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 13 08:12:35 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst129_Precompiled129Begin extends Precompiled129 {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=10, lastLine=41, hasLocalClasses=false, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
    // Declare parameters as attributes
    // Declare local labels
    // JavaLine 10 <== SourceLine 22
    final RTS_LABEL _LABEL_Precompiled129_X1_1=new RTS_LABEL(this,1,1,"X1"); // Local Label #1=X1 At PrefixLevel 1
    final RTS_LABEL _LABEL_Precompiled129_X2_1=new RTS_LABEL(this,1,2,"X2"); // Local Label #2=X2 At PrefixLevel 1
    final RTS_LABEL _LABEL_Precompiled129_X3_1=new RTS_LABEL(this,1,3,"X3"); // Local Label #3=X3 At PrefixLevel 1
    // Declare locals as attributes
    // JavaLine 15 <== SourceLine 11
    public int traceCase_2=0;
    // JavaLine 17 <== SourceLine 12
    public RTS_TEXT_ARRAY facit=null;
    // Normal Constructor
    public simtst129_Precompiled129Begin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title,int sp1_i) {
        super(staticLink,sp_n,sp_title,sp1_i);
        // Parameter assignment to locals
        // Declaration Code
        // JavaLine 24 <== SourceLine 12
        facit=new RTS_TEXT_ARRAY(new RTS_BOUNDS(0,5));
    }
    // Class Statements
    @Override
    public simtst129_Precompiled129Begin _STM() {
        simtst129_Precompiled129Begin _THIS=(simtst129_Precompiled129Begin)_CUR;
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
                // BEGIN Precompiled129Begin INNER PART
                // ENDOF Precompiled129Begin INNER PART
                // JavaLine 60 <== SourceLine 33
                facit.putELEMENT(facit.index(0),new RTS_TXT("Abra Message 2"));
                // JavaLine 62 <== SourceLine 34
                facit.putELEMENT(facit.index(1),new RTS_TXT("Abra Message 1"));
                // JavaLine 64 <== SourceLine 35
                facit.putELEMENT(facit.index(2),new RTS_TXT("Abra Message 3"));
                // JavaLine 66 <== SourceLine 36
                facit.putELEMENT(facit.index(3),new RTS_TXT("Abra Message 99"));
                // JavaLine 68 <== SourceLine 38
                new simtst129_Precompiled129Begin_test((_CUR));
                // ENDOF Precompiled129 INNER PART
                // ENDOF SimulaTest INNER PART
                // JavaLine 72 <== SourceLine 24
                if(_VALUE(noMessage)) {
                    ;
                } else {
                    {
                        // JavaLine 77 <== SourceLine 25
                        if(_VALUE(found_error)) {
                            {
                                // JavaLine 80 <== SourceLine 26
                                RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                                // JavaLine 85 <== SourceLine 27
                                RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                            }
                        } else {
                            // JavaLine 89 <== SourceLine 28
                            {
                                // JavaLine 91 <== SourceLine 29
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                            }
                        }
                        // JavaLine 98 <== SourceLine 32
                        if(_VALUE(false)) {
                            {
                                // JavaLine 101 <== SourceLine 33
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst129.sim","PrefixedBlock Precompiled129Begin",10,22,15,11,17,12,24,12,34,16,37,17,41,18,43,19,49,22,56,3,60,33,62,34,64,35,66,36,68,38,72,24,77,25,80,26,85,27,89,28,91,29,98,32,101,33,118,41);
} // End of Class
