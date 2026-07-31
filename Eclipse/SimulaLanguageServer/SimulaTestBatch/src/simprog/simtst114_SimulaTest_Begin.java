// JavaLine 1 <== SourceLine 10
package simprog;
// Simula-2.0 Compiled at Fri Jul 31 10:32:52 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst114_SimulaTest_Begin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=10, lastLine=10, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare local labels
    // JavaLine 10 <== SourceLine 68
    final RTS_LABEL _LABEL_simtst114_SimulaTest_Begin_NOMORE_1=new RTS_LABEL(this,1,1,"NOMORE"); // Local Label #1=NOMORE At PrefixLevel 1
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine 10
    public int traceCase_1=0;
    // JavaLine 15 <== SourceLine 11
    public RTS_TEXT_ARRAY facit=null;
    // Normal Constructor
    public simtst114_SimulaTest_Begin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
        // JavaLine 22 <== SourceLine 11
        facit=new RTS_TEXT_ARRAY(new RTS_BOUNDS(0,5));
    }
    // Class Statements
    @Override
    public simtst114_SimulaTest_Begin _STM() {
        simtst114_SimulaTest_Begin _THIS=(simtst114_SimulaTest_Begin)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                // JavaLine 32 <== SourceLine 0
                if(_VALUE(false)) {
                    {
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outimage();
                        RTS_BASICIO.sysout().outtext(p_title);
                        RTS_BASICIO.sysout().outimage();
                        RTS_BASICIO.sysout().outimage();
                    }
                }
                // BEGIN SIMULATEST INNER PART
                // BEGIN SimulaTest_Begin INNER PART
                // ENDOF SimulaTest_Begin INNER PART
                // JavaLine 46 <== SourceLine 56
                facit.putELEMENT(facit.index(0),new RTS_TXT("Test 1 selected case 2"));
                // JavaLine 48 <== SourceLine 57
                facit.putELEMENT(facit.index(1),new RTS_TXT("Test 2 selected case 1"));
                // JavaLine 50 <== SourceLine 58
                facit.putELEMENT(facit.index(2),new RTS_TXT("Test 3 selected case 3"));
                // JavaLine 52 <== SourceLine 59
                facit.putELEMENT(facit.index(3),new RTS_TXT("Test 4 selected case NONE"));
                // JavaLine 54 <== SourceLine 60
                facit.putELEMENT(facit.index(4),new RTS_TXT("In MAIN after NOMORE"));
                // JavaLine 56 <== SourceLine 62
                new simtst114_SimulaTest_Begin_test((_CUR),2,new RTS_TXT("Test 1"));
                // JavaLine 58 <== SourceLine 63
                new simtst114_SimulaTest_Begin_test((_CUR),1,new RTS_TXT("Test 2"));
                // JavaLine 60 <== SourceLine 64
                new simtst114_SimulaTest_Begin_test((_CUR),3,new RTS_TXT("Test 3"));
                // JavaLine 62 <== SourceLine 65
                new simtst114_SimulaTest_Begin_test((_CUR),5,new RTS_TXT("Test 4"));
                // JavaLine 64 <== SourceLine 66
                new simtst114_SimulaTest_Begin_trace((_CUR),new RTS_TXT("Should never come here"));
                // JavaLine 66 <== SourceLine 68
                {
                    _SIM_LABEL(1); // DeclaredIn: Line 0: IDENTIFIER[col:0, lng:0] Text: "SimulaTest_Begin", Value: "SimulaTest_Begin"
                    new simtst114_SimulaTest_Begin_trace((_CUR),new RTS_TXT("In MAIN after NOMORE"));
                }
                // ENDOF SIMULATEST INNER PART
                // JavaLine 72 <== SourceLine 0
                if(_VALUE(noMessage)) {
                    ;
                } else {
                    {
                        if(_VALUE(found_error)) {
                            {
                                RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                                RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                            }
                        } else {
                            {
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                                RTS_BASICIO.sysout().outint(p_n,4);
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                                RTS_BASICIO.sysout().outtext(p_title);
                            }
                        }
                        if(_VALUE(false)) {
                            {
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/simtst114.sim","PrefixedBlock SimulaTest_Begin",1,10,10,68,13,10,15,11,22,11,32,0,46,56,48,57,50,58,52,59,54,60,56,62,58,63,60,64,62,65,64,66,66,68,72,0,111,10);
} // End of Class
