package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:54:30 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=1, lastLine=30, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 3
    public RTS_TXT txt=null;
    public int realprecision=0;
    // JavaLine 11 <== SourceLine 4
    public int longprecision=0;
    // Normal Constructor
    public adHoc00_Block1(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 22 <== SourceLine 21
        txt=RTS_ENVIRONMENT.blanks(30);
        ;
        // JavaLine 25 <== SourceLine 23
        realprecision=7;
        ;
        // JavaLine 28 <== SourceLine 24
        longprecision=16;
        ;
        // JavaLine 31 <== SourceLine 1
        RTS_TXT.putreal(txt,1.0E20f,realprecision);
        ;
        new adHoc00_Block1_test((_CUR),new RTS_TXT("real operation (11)."),txt,new RTS_TXT("                  1.000000&+20"));
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block1",8,3,11,4,22,21,25,23,28,24,31,1,38,30);
} // End of SubBlock
