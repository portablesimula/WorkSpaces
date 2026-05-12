package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=3, lastLine=54, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_SimulaTestBegin _STM() {
        // JavaLine 18 <== SourceLine 16
        if(_VALUE(false)) {
            {
                // JavaLine 21 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                RTS_BASICIO.sysout().outint(p_n,4);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 25 <== SourceLine 18
                RTS_BASICIO.sysout().outtext(p_title);
                // JavaLine 27 <== SourceLine 19
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 35 <== SourceLine 5
        new adHoc00_SimulaTestBegin_SimulationBegin((_CUR))._START();
        // ENDOF SimulaTest INNER PART
        // JavaLine 38 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 43 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 46 <== SourceLine 26
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                        // JavaLine 51 <== SourceLine 27
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                    }
                } else {
                    // JavaLine 55 <== SourceLine 28
                    {
                        // JavaLine 57 <== SourceLine 29
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                    }
                }
                // JavaLine 64 <== SourceLine 32
                if(_VALUE(false)) {
                    {
                        // JavaLine 67 <== SourceLine 33
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","PrefixedBlock SimulaTestBegin",18,16,21,17,25,18,27,19,35,5,38,24,43,25,46,26,51,27,55,28,57,29,64,32,67,33,77,54);
} // End of Class
