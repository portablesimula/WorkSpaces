package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:55:16 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst21_SimulaTestBegin_Block160 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=2, firstLine=160, lastLine=197, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 161
    public RTS_TEXT_ARRAY T=null;
    // JavaLine 10 <== SourceLine 163
    public RTS_TXT t1=null;
    // Normal Constructor
    public simtst21_SimulaTestBegin_Block160(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
        // JavaLine 17 <== SourceLine 161
        T=new RTS_TEXT_ARRAY(new RTS_BOUNDS(1,4),new RTS_BOUNDS(1,2));
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 23 <== SourceLine 165
        t1=RTS_ENVIRONMENT.blanks(16);
        for(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=1;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1<=4;((simtst21_SimulaTestBegin)(_CUR._SL)).i_1++) {
            for(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=1;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1<=2;((simtst21_SimulaTestBegin)(_CUR._SL)).j_1++) {
                // JavaLine 27 <== SourceLine 171
                {
                    // JavaLine 29 <== SourceLine 172
                    T.putELEMENT(T.index(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1),RTS_TXT.sub(t1,RTS_UTIL._ISUB(RTS_UTIL._IADD(RTS_UTIL._IMUL(4,RTS_UTIL._ISUB(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,1)),RTS_UTIL._IMUL(2,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1)),1),2));
                    // JavaLine 31 <== SourceLine 173
                    RTS_TXT.putint(RTS_TXT.sub(T.getELEMENT(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1),1,1),((simtst21_SimulaTestBegin)(_CUR._SL)).i_1);
                    // JavaLine 33 <== SourceLine 174
                    RTS_TXT.putint(RTS_TXT.sub(T.getELEMENT(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1),2,1),((simtst21_SimulaTestBegin)(_CUR._SL)).j_1);
                }
            }
        }
        // JavaLine 38 <== SourceLine 177
        if(_VALUE(RTS_UTIL._TXTREL_EQ(t1,new RTS_TXT("1112212231324142")))) {
            ;
        } else {
            // JavaLine 42 <== SourceLine 179
            {
                // JavaLine 44 <== SourceLine 180
                new SimulaTest_err((_CUR._SL),new RTS_TXT(" : Text array (6)."));
                // JavaLine 46 <== SourceLine 181
                RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus value : "));
                // JavaLine 48 <== SourceLine 182
                RTS_BASICIO.sysout().outtext(t1);
                RTS_BASICIO.sysout().outimage();
            }
        }
        // JavaLine 53 <== SourceLine 185
        for(boolean CB_185:new FOR_List(
        new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).i_1); }	},new RTS_NAME<Number>() { public Number get(){return(10); }})
       ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).i_1); }	},new RTS_NAME<Number>() { public Number get(){return(20); }})
       ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).i_1); }	},new RTS_NAME<Number>() { public Number get(){return(30); }})
       ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).i_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).i_1); }	},new RTS_NAME<Number>() { public Number get(){return(40); }})
           )) { if(!CB_185) continue;
            // JavaLine 60 <== SourceLine 186
            for(boolean CB_186:new FOR_List(
            new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).j_1); }	},new RTS_NAME<Number>() { public Number get(){return(1); }})
           ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){((simtst21_SimulaTestBegin)(_CUR._SL)).j_1=x_.intValue(); return(x_);};  public Number get(){return((Number)((simtst21_SimulaTestBegin)(_CUR._SL)).j_1); }	},new RTS_NAME<Number>() { public Number get(){return(2); }})
               )) { if(!CB_186) continue;
                // JavaLine 65 <== SourceLine 189
                if(_VALUE((RTS_TXT.getint(T.getELEMENT((int)Math.round((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(10.0f))),((simtst21_SimulaTestBegin)(_CUR._SL)).j_1))==(RTS_UTIL._IADD(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1))))) {
                    ;
                } else {
                    // JavaLine 69 <== SourceLine 191
                    {
                        // JavaLine 71 <== SourceLine 192
                        new SimulaTest_err((_CUR._SL),new RTS_TXT(" : Text array (7)."));
                        // JavaLine 73 <== SourceLine 193
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("            Erroneus values : "));
                        // JavaLine 75 <== SourceLine 194
                        RTS_BASICIO.sysout().outint(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1,4);
                        RTS_BASICIO.sysout().outint(((simtst21_SimulaTestBegin)(_CUR._SL)).j_1,3);
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        // JavaLine 79 <== SourceLine 195
                        RTS_BASICIO.sysout().outtext(T.getELEMENT((int)Math.round((((4.0f*(((((float)(((simtst21_SimulaTestBegin)(_CUR._SL)).i_1))/(10.0f))-(1.0f))))+(((float)(RTS_UTIL._IMUL(2,((simtst21_SimulaTestBegin)(_CUR._SL)).j_1)))))-(1.0f))),2));
                        RTS_BASICIO.sysout().outimage();
                    }
                }
            }
        }
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst21.sim","SubBlock Block160",8,161,10,163,17,161,23,165,27,171,29,172,31,173,33,174,38,177,42,179,44,180,46,181,48,182,53,185,60,186,65,189,69,191,71,192,73,193,75,194,79,195,88,197);
} // End of SubBlock
