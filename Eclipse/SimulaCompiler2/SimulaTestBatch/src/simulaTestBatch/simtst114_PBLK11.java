package simulaTestBatch;
// Simula-2.0 Compiled at Fri Jul 31 11:16:48 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114_PBLK11 extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=10, lastLine=71, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare local labels
    // JavaLine 9 <== SourceLine 69
    final RTS_LABEL _LABEL_simtst114_PBLK11_NOMORE_1=new RTS_LABEL(this,1,1,"NOMORE"); // Local Label #1=NOMORE At PrefixLevel 1
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 11
    public int traceCase_1=0;
    // JavaLine 14 <== SourceLine 12
    public RTS_TEXT_ARRAY facit=null;
    // Normal Constructor
    public simtst114_PBLK11(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
        // JavaLine 21 <== SourceLine 12
        facit=new RTS_TEXT_ARRAY(new RTS_BOUNDS(0,5));
    }
    // Class Statements
    @Override
    public simtst114_PBLK11 _STM() {
        simtst114_PBLK11 _THIS=(simtst114_PBLK11)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                // JavaLine 31 <== SourceLine 16
                if(_VALUE(false)) {
                    {
                        // JavaLine 34 <== SourceLine 17
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outimage();
                        ;
                        // JavaLine 41 <== SourceLine 18
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                        // JavaLine 44 <== SourceLine 19
                        RTS_BASICIO.sysout().outimage();
                        ;
                        RTS_BASICIO.sysout().outimage();
                        ;
                    }
                }
                ;
                // JavaLine 52 <== SourceLine 22
                // BEGIN SimulaTest INNER PART
                // BEGIN PBLK11 INNER PART
                // ENDOF PBLK11 INNER PART
                // JavaLine 56 <== SourceLine 57
                facit.putELEMENT(facit.index(0),new RTS_TXT("Test 1 selected case 2"));
                ;
                // JavaLine 59 <== SourceLine 58
                facit.putELEMENT(facit.index(1),new RTS_TXT("Test 2 selected case 1"));
                ;
                // JavaLine 62 <== SourceLine 59
                facit.putELEMENT(facit.index(2),new RTS_TXT("Test 3 selected case 3"));
                ;
                // JavaLine 65 <== SourceLine 60
                facit.putELEMENT(facit.index(3),new RTS_TXT("Test 4 selected case NONE"));
                ;
                // JavaLine 68 <== SourceLine 61
                facit.putELEMENT(facit.index(4),new RTS_TXT("In MAIN after NOMORE"));
                ;
                // JavaLine 71 <== SourceLine 63
                new simtst114_PBLK11_test((_CUR),2,new RTS_TXT("Test 1"));
                ;
                // JavaLine 74 <== SourceLine 64
                new simtst114_PBLK11_test((_CUR),1,new RTS_TXT("Test 2"));
                ;
                // JavaLine 77 <== SourceLine 65
                new simtst114_PBLK11_test((_CUR),3,new RTS_TXT("Test 3"));
                ;
                // JavaLine 80 <== SourceLine 66
                new simtst114_PBLK11_test((_CUR),5,new RTS_TXT("Test 4"));
                ;
                // JavaLine 83 <== SourceLine 67
                new simtst114_PBLK11_trace((_CUR),new RTS_TXT("Should never come here"));
                ;
                // JavaLine 86 <== SourceLine 68
                {
                    _SIM_LABEL(1); // DeclaredIn: PBLK11
                    // JavaLine 89 <== SourceLine 69
                    new simtst114_PBLK11_trace((_CUR),new RTS_TXT("In MAIN after NOMORE"));
                }
                ;
                // ENDOF SimulaTest INNER PART
                ;
                // JavaLine 95 <== SourceLine 24
                if(_VALUE(noMessage)) {
                    ;
                } else {
                    {
                        // JavaLine 100 <== SourceLine 25
                        if(_VALUE(found_error)) {
                            {
                                // JavaLine 103 <== SourceLine 26
                                RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                                ;
                                RTS_BASICIO.sysout().outint(p_n,4);
                                ;
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                ;
                                RTS_BASICIO.sysout().outtext(p_title);
                                ;
                                // JavaLine 112 <== SourceLine 27
                                RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                                ;
                            }
                        } else {
                            // JavaLine 117 <== SourceLine 28
                            {
                                // JavaLine 119 <== SourceLine 29
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                                ;
                                RTS_BASICIO.sysout().outint(p_n,4);
                                ;
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                ;
                                RTS_BASICIO.sysout().outtext(p_title);
                                ;
                            }
                        }
                        ;
                        // JavaLine 131 <== SourceLine 32
                        if(_VALUE(false)) {
                            {
                                // JavaLine 134 <== SourceLine 33
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- END Simula a.s. TEST"));
                                ;
                                RTS_BASICIO.sysout().outint(p_n,4);
                                ;
                                RTS_BASICIO.sysout().outimage();
                                ;
                            }
                        }
                        ;
                    }
                }
                ;
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst114.sim","PrefixedBlock PBLK11",9,69,12,11,14,12,21,12,31,16,34,17,41,18,44,19,52,22,56,57,59,58,62,59,65,60,68,61,71,63,74,64,77,65,80,66,83,67,86,68,89,69,95,24,100,25,103,26,112,27,117,28,119,29,131,32,134,33,156,71);
} // End of Class
