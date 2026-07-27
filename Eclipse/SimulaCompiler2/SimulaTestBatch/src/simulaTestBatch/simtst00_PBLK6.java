package simulaTestBatch;
// Simula-2.0 Compiled at Mon Jul 27 15:17:24 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst00_PBLK6 extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=4, lastLine=9, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public simtst00_PBLK6(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst00_PBLK6 _STM() {
        // JavaLine 18 <== SourceLine 16
        if(_VALUE(false)) {
            {
                // JavaLine 21 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                ;
                RTS_BASICIO.sysout().outint(p_n,4);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                // JavaLine 28 <== SourceLine 18
                RTS_BASICIO.sysout().outtext(p_title);
                ;
                // JavaLine 31 <== SourceLine 19
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 39 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // BEGIN PBLK6 INNER PART
        // ENDOF PBLK6 INNER PART
        // JavaLine 43 <== SourceLine 6
        RTS_BASICIO.sysout().outtext(new RTS_TXT("IN EMPTY TEST"));
        ;
        RTS_BASICIO.sysout().outimage();
        ;
        // ENDOF SimulaTest INNER PART
        ;
        // JavaLine 50 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 55 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 58 <== SourceLine 26
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        ;
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                        // JavaLine 67 <== SourceLine 27
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                        ;
                    }
                } else {
                    // JavaLine 72 <== SourceLine 28
                    {
                        // JavaLine 74 <== SourceLine 29
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
                // JavaLine 86 <== SourceLine 32
                if(_VALUE(false)) {
                    {
                        // JavaLine 89 <== SourceLine 33
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst00.sim","PrefixedBlock PBLK6",18,16,21,17,28,18,31,19,39,22,43,6,50,24,55,25,58,26,67,27,72,28,74,29,86,32,89,33,104,9);
} // End of Class
