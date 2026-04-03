// JavaLine 1 <== SourceLine 37
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:54:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class SimulaTest extends RTS_CLASS {
    // ClassDeclaration: Kind=9, BlockLevel=1, PrefixLevel=0, firstLine=37, lastLine=37, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    public int p_n;
    public RTS_TXT p_title;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 5
    public boolean found_error=false;
    public int nFailed=0;
    // JavaLine 15 <== SourceLine 6
    public final boolean verbose=(boolean)(false);
    // JavaLine 17 <== SourceLine 7
    public boolean noMessage=false;
    // Normal Constructor
    public SimulaTest(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink);
        // Parameter assignment to locals
        this.p_n = sp_n;
        this.p_title = sp_title;
        BBLK(); // Iff no prefix
        // Declaration Code
    }
    // Class Statements
    @Override
    public SimulaTest _STM() {
        // JavaLine 31 <== SourceLine 16
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
        // JavaLine 49 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // ENDOF SimulaTest INNER PART
        ;
        // JavaLine 53 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 58 <== SourceLine 25
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
                    // JavaLine 73 <== SourceLine 28
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
                // JavaLine 86 <== SourceLine 32
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("SimulaTest.sim","Class SimulaTest",1,37,12,5,15,6,17,7,31,16,49,22,53,24,58,25,73,28,86,32,103,37);
} // End of Class
