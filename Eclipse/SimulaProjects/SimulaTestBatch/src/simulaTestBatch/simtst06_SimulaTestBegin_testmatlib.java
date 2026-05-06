// JavaLine 1 <== SourceLine 21
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class simtst06_SimulaTestBegin_testmatlib extends RTS_CLASS {
    // ClassDeclaration: Kind=9, BlockLevel=2, PrefixLevel=0, firstLine=21, lastLine=272, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 23
    public double maxlongdig=0.0d;
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        BBLK(); // Iff no prefix
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib _STM() {
        // JavaLine 22 <== SourceLine 270
        maxlongdig=1.0E18d;
        // JavaLine 24 <== SourceLine 21
        // BEGIN testmatlib INNER PART
        // ENDOF testmatlib INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Class testmatlib",1,21,10,23,22,270,24,21,29,272);
} // End of Class
