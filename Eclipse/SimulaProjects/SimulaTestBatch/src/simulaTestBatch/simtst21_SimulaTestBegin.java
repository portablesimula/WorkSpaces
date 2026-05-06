package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:55:16 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst21_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=21, lastLine=199, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 23
    public int i_1=0;
    public int j_1=0;
    // Normal Constructor
    public simtst21_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst21_SimulaTestBegin _STM() {
        // JavaLine 21 <== SourceLine 16
        if(_VALUE(false)) {
            {
                // JavaLine 24 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                RTS_BASICIO.sysout().outint(p_n,4);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 28 <== SourceLine 18
                RTS_BASICIO.sysout().outtext(p_title);
                // JavaLine 30 <== SourceLine 19
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 38 <== SourceLine 25
        new simtst21_SimulaTestBegin_Block25((_CUR))._STM();
        // JavaLine 40 <== SourceLine 79
        i_1=10;
        // JavaLine 42 <== SourceLine 110
        new simtst21_SimulaTestBegin_Block110((_CUR))._STM();
        // JavaLine 44 <== SourceLine 160
        new simtst21_SimulaTestBegin_Block160((_CUR))._STM();
        // ENDOF SimulaTest INNER PART
        // JavaLine 47 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 52 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 55 <== SourceLine 26
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                        // JavaLine 60 <== SourceLine 27
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                    }
                } else {
                    // JavaLine 64 <== SourceLine 28
                    {
                        // JavaLine 66 <== SourceLine 29
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                    }
                }
                // JavaLine 73 <== SourceLine 32
                if(_VALUE(false)) {
                    {
                        // JavaLine 76 <== SourceLine 33
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst21.sim","PrefixedBlock SimulaTestBegin",9,23,21,16,24,17,28,18,30,19,38,25,40,79,42,110,44,160,47,24,52,25,55,26,60,27,64,28,66,29,73,32,76,33,86,199);
} // End of Class
