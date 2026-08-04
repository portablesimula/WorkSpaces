package simprog;
// Simula-2.0 Compiled at Tue Aug 04 08:32:08 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc05 extends RTS_BASICIO {
    // SubBlock: Kind=11, BlockLevel=0, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare local labels
    // JavaLine 8 <== SourceLine 15
    final RTS_LABEL _LABEL_adHoc05_Block0_LAB2_0=new RTS_LABEL(this,0,1,"LAB2"); // Local Label #1=LAB2 At PrefixLevel 0
    // Declare locals as attributes
    // Normal Constructor
    public adHoc05(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 11 Statements
    @Override
    public RTS_RTObject _STM() {
        adHoc05 _THIS=(adHoc05)_CUR;
        _LOOP:while(_JTX>=0) {
            try {
                _JUMPTABLE(_JTX,1); // For ByteCode Engineering
                // JavaLine 24 <== SourceLine 0
                {
                    // JavaLine 26 <== SourceLine 12
                    new adHoc05_Block0_CatchingErrors_Begin(_USR)._STM();
                    // JavaLine 28 <== SourceLine 15
                    {
                        _SIM_LABEL(1); // DeclaredIn: Line 0: IDENTIFIER[col:0, lng:0] Text: "adHoc05", Value: "adHoc05" -> adHoc05[adHoc05]
                        ;
                    }
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
        RTS_UTIL.BPRG("adHoc05", args);
        RTS_UTIL.RUN_STM(new adHoc05(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc05.sim","SimulaProgram adHoc05",8,15,24,0,26,12,28,15,49,0);
} // End of SubBlock
