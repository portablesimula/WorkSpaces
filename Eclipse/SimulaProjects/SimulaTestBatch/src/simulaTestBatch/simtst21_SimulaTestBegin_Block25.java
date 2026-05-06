package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:55:16 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst21_SimulaTestBegin_Block25 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=2, firstLine=25, lastLine=77, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 28
    public RTS_INTEGER_ARRAY A=null;
    public RTS_INTEGER_ARRAY B=null;
    // Normal Constructor
    public simtst21_SimulaTestBegin_Block25(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
        A=new RTS_INTEGER_ARRAY(new RTS_BOUNDS(RTS_UTIL._ISUB(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,3),RTS_UTIL._IADD(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1,3)));
        B=new RTS_INTEGER_ARRAY(new RTS_BOUNDS(RTS_UTIL._ISUB(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,3),RTS_UTIL._IADD(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1,3)));
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=3;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1>=-3;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1--) {
            // JavaLine 23 <== SourceLine 37
            A.putELEMENT(A.index((-(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))),B.putELEMENT(B.index(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1),((simtst21_SimulaTestBegin)(_CUR._SL)).i_1));
        }
        // JavaLine 26 <== SourceLine 39
        if(_VALUE((RTS_UTIL._IADD(A.getELEMENT(-2),B.getELEMENT(-2))==(0)))) {
            ;
        } else {
            // JavaLine 30 <== SourceLine 41
            {
                // JavaLine 32 <== SourceLine 42
                new SimulaTest_err((_CUR._SL),new RTS_TXT(" : Integer Array (1)."));
                // JavaLine 34 <== SourceLine 43
                RTS_BASICIO.sysout().outimage();
                // JavaLine 36 <== SourceLine 44
                RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus values : "));
                // JavaLine 38 <== SourceLine 45
                RTS_BASICIO.sysout().outimage();
                for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=-3;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1<=3;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1++) {
                    // JavaLine 41 <== SourceLine 47
                    {
                        // JavaLine 43 <== SourceLine 48
                        RTS_BASICIO.sysout().outint(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,13);
                        // JavaLine 45 <== SourceLine 49
                        RTS_BASICIO.sysout().outint(A.getELEMENT(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1),4);
                        // JavaLine 47 <== SourceLine 50
                        RTS_BASICIO.sysout().outint(B.getELEMENT(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1),4);
                        // JavaLine 49 <== SourceLine 51
                        RTS_BASICIO.sysout().outimage();
                    }
                }
                ;
            }
        }
        for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=0;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1<=18;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=((simtst21_SimulaTestBegin)(_CUR._SL)).i_1+3) {
            // JavaLine 57 <== SourceLine 59
            {
                // JavaLine 59 <== SourceLine 60
                ((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=RTS_UTIL._IADD(A.getELEMENT((int)Math.round(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(3.0f))-(3.0f)))),B.getELEMENT((int)Math.round(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(3.0f))-(3.0f)))));
                // JavaLine 61 <== SourceLine 62
                if(_VALUE((((simtst21_SimulaTestBegin)(_CUR._SL)).j_1==(0)))) {
                    ;
                } else {
                    // JavaLine 65 <== SourceLine 64
                    {
                        // JavaLine 67 <== SourceLine 65
                        new SimulaTest_err((_CUR._SL),new RTS_TXT(" : Integer Array (2)."));
                        // JavaLine 69 <== SourceLine 66
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 71 <== SourceLine 67
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus values : "));
                        // JavaLine 73 <== SourceLine 68
                        RTS_BASICIO.sysout().outimage();
                        // JavaLine 75 <== SourceLine 69
                        RTS_BASICIO.sysout().outint((int)Math.round(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(3.0f))-(3.0f))),13);
                        // JavaLine 77 <== SourceLine 70
                        RTS_BASICIO.sysout().outint(A.getELEMENT((int)Math.round(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(3.0f))-(3.0f)))),4);
                        // JavaLine 79 <== SourceLine 71
                        RTS_BASICIO.sysout().outint(B.getELEMENT((int)Math.round(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(3.0f))-(3.0f)))),4);
                        // JavaLine 81 <== SourceLine 72
                        RTS_BASICIO.sysout().outimage();
                    }
                }
            }
        }
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst21.sim","SubBlock Block25",8,28,23,37,26,39,30,41,32,42,34,43,36,44,38,45,41,47,43,48,45,49,47,50,49,51,57,59,59,60,61,62,65,64,67,65,69,66,71,67,73,68,75,69,77,70,79,71,81,72,89,77);
} // End of SubBlock
