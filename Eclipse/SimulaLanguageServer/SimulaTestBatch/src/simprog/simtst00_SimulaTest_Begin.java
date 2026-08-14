// JavaLine 1 <== SourceLine 5
package simprog;
// Simula-2.0 Compiled at Thu Aug 13 08:24:00 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst00_SimulaTest_Begin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=5, lastLine=5, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public simtst00_SimulaTest_Begin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst00_SimulaTest_Begin _STM() {
        // JavaLine 19 <== SourceLine 0
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
        // JavaLine 33 <== SourceLine 5
        RTS_BASICIO.sysout().outtext(new RTS_TXT("IN EMPTY TEST"));
        RTS_BASICIO.sysout().outimage();
        // ENDOF SIMULATEST INNER PART
        // JavaLine 37 <== SourceLine 0
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
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/simtst00.sim","PrefixedBlock SimulaTest_Begin",1,5,19,0,33,5,37,0,69,5);
} // End of Class
