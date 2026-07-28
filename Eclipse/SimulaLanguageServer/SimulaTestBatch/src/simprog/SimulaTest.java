// JavaLine 1 <== SourceLine 1
package simprog;
// Simula-2.0 Compiled at Tue Jul 28 08:38:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class SimulaTest extends RTS_CLASS {
    // ClassDeclaration: Kind=9, BlockLevel=1, PrefixLevel=0, firstLine=1, lastLine=1, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    public int p_n;
    public RTS_TXT p_title;
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 4
    public boolean found_error=false;
    // JavaLine 14 <== SourceLine 5
    public int nFailed=0;
    // JavaLine 16 <== SourceLine 6
    public final boolean verbose=(boolean)(false);
    // JavaLine 18 <== SourceLine 7
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
        // JavaLine 32 <== SourceLine 15
        if(_VALUE(false)) {
            {
                // JavaLine 35 <== SourceLine 16
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                RTS_BASICIO.sysout().outint(p_n,4);
                RTS_BASICIO.sysout().outimage();
                // JavaLine 39 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(p_title);
                // JavaLine 41 <== SourceLine 18
                RTS_BASICIO.sysout().outimage();
                RTS_BASICIO.sysout().outimage();
            }
        }
        // BEGIN SIMULATEST INNER PART
        // ENDOF SIMULATEST INNER PART
        // JavaLine 48 <== SourceLine 23
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 53 <== SourceLine 24
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 56 <== SourceLine 25
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                        // JavaLine 61 <== SourceLine 26
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                    }
                } else {
                    // JavaLine 65 <== SourceLine 27
                    {
                        // JavaLine 67 <== SourceLine 28
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                        RTS_BASICIO.sysout().outint(p_n,4);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        RTS_BASICIO.sysout().outtext(p_title);
                    }
                }
                // JavaLine 74 <== SourceLine 31
                if(_VALUE(false)) {
                    {
                        // JavaLine 77 <== SourceLine 32
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/SimulaTest.sim","Class SIMULATEST",1,1,12,4,14,5,16,6,18,7,32,15,35,16,39,17,41,18,48,23,53,24,56,25,61,26,65,27,67,28,74,31,77,32,87,1);
} // End of Class
