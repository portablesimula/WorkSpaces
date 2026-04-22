// JavaLine 1 <== SourceLine 11
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=11, lastLine=39, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 13
    public float r_1=0.0f;
    // Normal Constructor
    public simtst104_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst104_SimulaTestBegin _STM() {
        // JavaLine 21 <== SourceLine 16
        if(_VALUE(false)) {
            {
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                ;
                RTS_BASICIO.sysout().outint(p_n,4);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outtext(p_title);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 39 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 43 <== SourceLine 36
    r_1=new simtst104_SimulaTestBegin_Q((_CUR),new RTS_NAME<RTS_PRCQNT>(){ public RTS_PRCQNT get() { return(new RTS_PRCQNT(((simtst104_SimulaTestBegin)(_CUR)),simtst104_SimulaTestBegin_P.class)); } })._RESULT;
        ;
        // JavaLine 46 <== SourceLine 11
        new simtst104_SimulaTestBegin_trace((_CUR),new RTS_TXT("r := Q(P)"),r_1,34.0f);
        ;
        // ENDOF SimulaTest INNER PART
        ;
        // JavaLine 51 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 56 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        ;
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                        ;
                    }
                } else {
                    // JavaLine 71 <== SourceLine 28
                    {
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
                // JavaLine 84 <== SourceLine 32
                if(_VALUE(false)) {
                    {
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
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","PrefixedBlock SimulaTestBegin",1,11,10,13,21,16,39,22,43,36,46,11,51,24,56,25,71,28,84,32,101,39);
} // End of Class
