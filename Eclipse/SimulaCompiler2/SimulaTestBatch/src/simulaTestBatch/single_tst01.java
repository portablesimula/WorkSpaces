// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Oct 15 10:47:00 CEST 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class single_tst01 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=1, lastLine=0, hasLocalClasses=false, System=false
    // Declare local labels
    final RTS_LABEL _LABEL_single_tst01_L_0=new RTS_LABEL(this,0,1,"L"); // Local Label #1=L At PrefixLevel 0
    // Declare locals as attributes
    // Normal Constructor
    public single_tst01(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        single_tst01 _THIS=(single_tst01)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                {
                    _SIM_LABEL(1); // DeclaredIn: single_tst01
                    RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN Single test 1"));
                }
                break _LOOP;
            }
            catch(RTS_LABEL q) {
                RTS_RTObject._TREAT_GOTO_CATCH_BLOCK(_THIS, q);
                _JTX=q.index; continue _LOOP; // EG. GOTO Lx
            }
        }
        EBLK();
        return(this);
    } // End of 11 Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("single_tst01", args);
        RTS_UTIL.RUN_STM(new single_tst01(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("single_tst01.sim","11 single_tst01",1,1,43,0);
} // End of SubBlock
