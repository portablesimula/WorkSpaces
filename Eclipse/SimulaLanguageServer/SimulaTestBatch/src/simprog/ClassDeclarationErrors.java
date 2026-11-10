package simprog;
// Simula-2.0 Compiled at Fri Nov 06 12:53:08 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ClassDeclarationErrors extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public ClassDeclarationErrors(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        new ClassDeclarationErrors_Block0(_USR)._STM();
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("ClassDeclarationErrors", args);
        RTS_UTIL.RUN_STM(new ClassDeclarationErrors(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaSemErrors/ClassDeclarationErrors.sim","SimulaProgram ClassDeclarationErrors",26,0);
} // End of SubBlock
