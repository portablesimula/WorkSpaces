// JavaLine 1 <== SourceLine 9
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Oct 09 12:26:39 CEST 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class adHoc02_Trikk extends RTS_Process {
    // ClassDeclaration: Kind=9, BlockLevel=2, PrefixLevel=3, firstLine=9, lastLine=28, hasLocalClasses=false, System=false, detachUsed=false
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    public adHoc02_Sporveksel p3_pens1;
    public adHoc02_Sporveksel p3_pens2;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine 12
    public float ankomsttid_3=0.0f;
    // Normal Constructor
    public adHoc02_Trikk(RTS_RTObject staticLink,adHoc02_Sporveksel sp3_pens1,adHoc02_Sporveksel sp3_pens2) {
        super(staticLink);
        // Parameter assignment to locals
        this.p3_pens1 = sp3_pens1;
        this.p3_pens2 = sp3_pens2;
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc02_Trikk _STM() {
        // BEGIN Linkage INNER PART
        // BEGIN Link INNER PART
        // JavaLine 28 <== SourceLine 1
        detach(); // Process'detach
        // BEGIN Process INNER PART
        // JavaLine 31 <== SourceLine 15
        ankomsttid_3=((float)(((adHoc02)(_CUR._SL)).time()));
        ;
        // JavaLine 34 <== SourceLine 16
        if(_VALUE((!(p3_pens1.har_pinnen)))) {
            ((adHoc02)(_CUR._SL)).wait(p3_pens1.koe);
        }
        ;
        // JavaLine 39 <== SourceLine 19
        p3_pens1.har_pinnen=false;
        ;
        // JavaLine 42 <== SourceLine 20
        new adHoc02_noter_ventetid((_CUR._SL),ankomsttid_3,((float)(((adHoc02)(_CUR._SL)).time())),p3_pens1.koe.cardinal());
        ;
        // JavaLine 45 <== SourceLine 21
        if(_VALUE((suc()!=(null)))) {
            ((adHoc02)(_CUR._SL)).ActivateAfter(false,(RTS_Process)((RTS_Process)(suc())),(RTS_Process)((adHoc02)(_CUR._SL)).current());
        }
        ;
        // JavaLine 50 <== SourceLine 22
        out();
        ;
        // JavaLine 53 <== SourceLine 23
        ((adHoc02)(_CUR._SL)).hold(120.0d);
        ;
        // JavaLine 56 <== SourceLine 26
        p3_pens2.har_pinnen=true;
        ;
        // JavaLine 59 <== SourceLine 27
        ((adHoc02)(_CUR._SL)).ActivateDirect(false,(RTS_Process)((RTS_Process)(p3_pens2.koe.first())));
        ;
        // JavaLine 62 <== SourceLine 28
        // BEGIN Trikk INNER PART
        // ENDOF Trikk INNER PART
        // ENDOF Process INNER PART
        // JavaLine 66 <== SourceLine 1
        terminate(); // Process'terminate
        // ENDOF Link INNER PART
        // ENDOF Linkage INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc02.sim","9 Trikk",1,9,13,12,28,1,31,15,34,16,39,19,42,20,45,21,50,22,53,23,56,26,59,27,62,28,66,1,72,28);
} // End of Class
