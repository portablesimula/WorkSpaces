package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 10:50:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst40_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=28, lastLine=40, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 31
    public int i_1=0;
    // Normal Constructor
    public simtst40_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst40_SimulaTestBegin _STM() {
        // JavaLine 20 <== SourceLine 16
        if(_VALUE(false)) {
            {
                // JavaLine 23 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                RTS_BASICIO.sysout().outint(p_n,4);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 27 <== SourceLine 18
                RTS_BASICIO.sysout().outtext(p_title);
                // JavaLine 29 <== SourceLine 19
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 37 <== SourceLine 33
        i_1=new pa(_USR,0)._RESULT;
        // JavaLine 39 <== SourceLine 34
        if(_VALUE((i_1==(45)))) {
            ;
        } else {
            {
                // JavaLine 44 <== SourceLine 35
                new SimulaTest_err((_CUR),new RTS_TXT("PA returned with wrong value."));
                // JavaLine 46 <== SourceLine 36
                RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus value : "));
                // JavaLine 48 <== SourceLine 37
                RTS_BASICIO.sysout().outint(i_1,5);
                RTS_BASICIO.sysout().outimage();
            }
        }
        // ENDOF SimulaTest INNER PART
        // JavaLine 54 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 59 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 62 <== SourceLine 26
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                        // JavaLine 67 <== SourceLine 27
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                    }
                } else {
                    // JavaLine 71 <== SourceLine 28
                    {
                        // JavaLine 73 <== SourceLine 29
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                    }
                }
                // JavaLine 80 <== SourceLine 32
                if(_VALUE(false)) {
                    {
                        // JavaLine 83 <== SourceLine 33
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst40.sim","PrefixedBlock SimulaTestBegin",9,31,20,16,23,17,27,18,29,19,37,33,39,34,44,35,46,36,48,37,54,24,59,25,62,26,67,27,71,28,73,29,80,32,83,33,93,40);
} // End of Class
