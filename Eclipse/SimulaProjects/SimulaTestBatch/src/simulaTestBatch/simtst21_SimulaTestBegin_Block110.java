package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:55:16 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst21_SimulaTestBegin_Block110 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=2, firstLine=110, lastLine=158, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 111
    public RTS_CHARACTER_ARRAY C=null;
    public RTS_CHARACTER_ARRAY D=null;
    // JavaLine 11 <== SourceLine 113
    public int n=0;
    // Normal Constructor
    public simtst21_SimulaTestBegin_Block110(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
        // JavaLine 18 <== SourceLine 111
        C=new RTS_CHARACTER_ARRAY(new RTS_BOUNDS(8,9),new RTS_BOUNDS(1,2));
        D=new RTS_CHARACTER_ARRAY(new RTS_BOUNDS(8,9),new RTS_BOUNDS(1,2));
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=8;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1<=9;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1++) {
            for(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=1;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1<=2;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1++) {
                // JavaLine 27 <== SourceLine 134
                {
                    // JavaLine 29 <== SourceLine 135
                    D.putELEMENT(D.index(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1),RTS_ENVIRONMENT.Char(RTS_UTIL._IADD(n,65)));
                    // JavaLine 31 <== SourceLine 136
                    n=RTS_UTIL._IADD(n,1);
                }
            }
        }
        // JavaLine 36 <== SourceLine 139
        if(_VALUE(((((D.getELEMENT(8,1)==('A'))&((D.getELEMENT(8,2)==('B'))))&((D.getELEMENT(9,1)==('C'))))&((D.getELEMENT(9,2)==('D')))))) {
            ;
        } else {
            // JavaLine 40 <== SourceLine 142
            {
                // JavaLine 42 <== SourceLine 143
                new SimulaTest_err((_CUR._SL),new RTS_TXT(" : Value of character array (5)."));
                // JavaLine 44 <== SourceLine 144
                RTS_BASICIO.sysout().outimage();
                // JavaLine 46 <== SourceLine 145
                RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus values : "));
                // JavaLine 48 <== SourceLine 146
                RTS_BASICIO.sysout().outimage();
                for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=8;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1<=9;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1++) {
                    for(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=1;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1<=2;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1++) {
                        // JavaLine 52 <== SourceLine 151
                        {
                            // JavaLine 54 <== SourceLine 152
                            RTS_BASICIO.sysout().outint(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,13);
                            RTS_BASICIO.sysout().outint(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1,3);
                            // JavaLine 57 <== SourceLine 153
                            RTS_BASICIO.sysout().outchar(D.getELEMENT(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1));
                            // JavaLine 59 <== SourceLine 154
                            RTS_BASICIO.sysout().outimage();
                        }
                    }
                }
            }
        }
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst21.sim","SubBlock Block110",8,111,11,113,18,111,27,134,29,135,31,136,36,139,40,142,42,143,44,144,46,145,48,146,52,151,54,152,57,153,59,154,68,158);
} // End of SubBlock
