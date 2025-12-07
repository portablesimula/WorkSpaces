// JavaLine 1 <== SourceLine 11
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Feb 27 09:04:41 CET 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst49 extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=11, lastLine=69, hasLocalClasses=true, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 11 <== SourceLine 13
    public RTS_TXT t_1=null;
    // JavaLine 13 <== SourceLine 22
    public simtst49_A ra_1=null;
    // JavaLine 15 <== SourceLine 23
    public int l_1=0;
    // JavaLine 17 <== SourceLine 27
    public simtst49_A _inspect_27_1=null;
    // JavaLine 19 <== SourceLine 39
    public simtst49_A _inspect_39_2=null;
    // JavaLine 21 <== SourceLine 54
    public simtst49_A _inspect_53_3=null;
    // Normal Constructor
    public simtst49(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst49 _STM() {
        // JavaLine 32 <== SourceLine 16
        if(_VALUE(false)) {
            {
                // JavaLine 35 <== SourceLine 17
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                ;
                RTS_BASICIO.sysout().outint(p_n,4);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                // JavaLine 42 <== SourceLine 18
                RTS_BASICIO.sysout().outtext(p_title);
                ;
                // JavaLine 45 <== SourceLine 19
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 53 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // BEGIN simtst49 INNER PART
        // ENDOF simtst49 INNER PART
        // JavaLine 57 <== SourceLine 25
        ra_1=new simtst49_A((_CUR))._STM();
        ;
        // JavaLine 60 <== SourceLine 27
        {
            // BEGIN INSPECTION 
            _inspect_27_1=ra_1;
            if(_inspect_27_1!=null) { // INSPECT _inspect_27_1  type=ref(A)
                // JavaLine 65 <== SourceLine 28
                {
                    for(boolean CB_28:new FOR_List(
                    new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_27_1.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_27_1.i); }	},new RTS_NAME<Number>() { public Number get(){return(l_1); }})
                   ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_27_1.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_27_1.i); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(l_1,1)); }})
                   ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_27_1.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_27_1.i); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(l_1,2)); }})
                       )) { if(!CB_28) continue;
                        RTS_BASICIO.sysout().outint(_inspect_27_1.i,3);
                    }
                }
            }
        } // END INSPECTION
        ;
        // JavaLine 78 <== SourceLine 30
        t_1=RTS_ENVIRONMENT.copy(copy(RTS_TXT.strip(RTS_BASICIO.sysout().image)));
        ;
        // JavaLine 81 <== SourceLine 31
        RTS_TXT.setpos(RTS_BASICIO.sysout().image,1);
        ;
        // JavaLine 84 <== SourceLine 32
        RTS_UTIL._ASGTXT(RTS_BASICIO.sysout().image,null);
        ;
        // JavaLine 87 <== SourceLine 34
        if(_VALUE(RTS_UTIL._TXTREL_EQ(t_1,new RTS_TXT("  0  1  2")))) {
            ;
        } else {
            {
                // JavaLine 92 <== SourceLine 35
                new SimulaTest_err((_CUR),new RTS_TXT("for-loop(1)."));
                ;
                // JavaLine 95 <== SourceLine 36
                RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("            Erroneus values : "),t_1));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 103 <== SourceLine 39
        {
            // BEGIN INSPECTION 
            _inspect_39_2=ra_1;
            if(_inspect_39_2!=null) { // INSPECT _inspect_39_2  type=ref(A)
                // JavaLine 108 <== SourceLine 40
                {
                    for(boolean CB_40:new FOR_List(
                    new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_39_2.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_39_2.i); }	},new RTS_NAME<Number>() { public Number get(){return(_inspect_39_2.i); }})
                   ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_39_2.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_39_2.i); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(_inspect_39_2.i,1)); }})
                   ,new FOR_StepUntil(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_39_2.i=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_39_2.i); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(_inspect_39_2.i,2)); }},new RTS_NAME<Number>() { public Number get(){return(-1); }},new RTS_NAME<Number>() { public Number get(){return(0); }})
                       )) { if(!CB_40) continue;
                        // JavaLine 115 <== SourceLine 41
                        for(boolean CB_41:new FOR_List(
                        new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_39_2.j=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_39_2.j); }	},new RTS_NAME<Number>() { public Number get(){return(_inspect_39_2.i); }})
                       ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_39_2.j=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_39_2.j); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(_inspect_39_2.i,1)); }})
                           )) { if(!CB_41) continue;
                            RTS_BASICIO.sysout().outint(RTS_UTIL._IADD(_inspect_39_2.i,_inspect_39_2.j),3);
                        }
                    }
                }
            }
        } // END INSPECTION
        ;
        // JavaLine 127 <== SourceLine 43
        t_1=RTS_ENVIRONMENT.copy(copy(RTS_TXT.strip(RTS_BASICIO.sysout().image)));
        ;
        // JavaLine 130 <== SourceLine 44
        RTS_TXT.setpos(RTS_BASICIO.sysout().image,1);
        ;
        // JavaLine 133 <== SourceLine 45
        RTS_UTIL._ASGTXT(RTS_BASICIO.sysout().image,null);
        ;
        // JavaLine 136 <== SourceLine 47
        if(_VALUE(RTS_UTIL._TXTREL_EQ(t_1,new RTS_TXT("  4  5  6  7 10 11  8  9  6  7  4  5  2  3  0  1")))) {
            ;
        } else {
            {
                // JavaLine 141 <== SourceLine 48
                new SimulaTest_err((_CUR),new RTS_TXT("for-loop(2)."));
                ;
                // JavaLine 144 <== SourceLine 49
                RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("            Erroneus values : "),t_1));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 152 <== SourceLine 53
        {
            // BEGIN INSPECTION 
            _inspect_53_3=ra_1;
            if(_inspect_53_3 instanceof simtst49_A) { // WHEN simtst49_A DO 
                simtst49_A _connID_4 = (simtst49_A)_inspect_53_3;
                // JavaLine 158 <== SourceLine 55
                {
                    for(boolean CB_55:new FOR_List(
                    new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_53_3.k=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_53_3.k); }	},new RTS_NAME<Number>() { public Number get(){return(_inspect_53_3.k); }})
                   ,new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){_inspect_53_3.k=x_.intValue(); return(x_);};  public Number get(){return((Number)_inspect_53_3.k); }	},new RTS_NAME<Number>() { public Number get(){return(RTS_UTIL._IADD(_inspect_53_3.k,1)); }})
                       )) { if(!CB_55) continue;
                        l_1=_inspect_53_3.k; {
                            // JavaLine 165 <== SourceLine 56
                            RTS_BASICIO.sysout().outint(RTS_UTIL._IADD(_inspect_53_3.k,l_1),3);
                        }
                    }
                }
            }
            else if(_inspect_53_3 instanceof simtst49_B) { // WHEN simtst49_B DO 
                simtst49_B _connID_5 = (simtst49_B)_inspect_53_3;
                // JavaLine 173 <== SourceLine 57
                {
                    new SimulaTest_err((_CUR),new RTS_TXT("Wrong statement selected(3)."));
                }
            }
            else { // OTHERWISE 
                // JavaLine 179 <== SourceLine 58
                new SimulaTest_err((_CUR),new RTS_TXT("Wrong statement selected(4)."));
            } // END OTHERWISE 
        } // END INSPECTION
        ;
        // JavaLine 184 <== SourceLine 60
        t_1=RTS_ENVIRONMENT.copy(copy(RTS_TXT.strip(RTS_BASICIO.sysout().image)));
        ;
        // JavaLine 187 <== SourceLine 61
        RTS_TXT.setpos(RTS_BASICIO.sysout().image,1);
        ;
        // JavaLine 190 <== SourceLine 62
        RTS_UTIL._ASGTXT(RTS_BASICIO.sysout().image,null);
        ;
        // JavaLine 193 <== SourceLine 64
        if(_VALUE(RTS_UTIL._TXTREL_EQ(t_1,new RTS_TXT("  0  2")))) {
            ;
        } else {
            {
                // JavaLine 198 <== SourceLine 65
                new SimulaTest_err((_CUR),new RTS_TXT("for-loop(5)."));
                ;
                // JavaLine 201 <== SourceLine 66
                RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("            Erroneus values : "),t_1));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // ENDOF SimulaTest INNER PART
        ;
        // JavaLine 211 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 216 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        // JavaLine 219 <== SourceLine 26
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        ;
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                        // JavaLine 228 <== SourceLine 27
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                        ;
                    }
                } else {
                    // JavaLine 233 <== SourceLine 28
                    {
                        // JavaLine 235 <== SourceLine 29
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- NO ERRORS FOUND IN TEST"));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        ;
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                    }
                }
                ;
                // JavaLine 247 <== SourceLine 32
                if(_VALUE(false)) {
                    {
                        // JavaLine 250 <== SourceLine 33
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("--- END Simula a.s. TEST"));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outimage();
                        ;
                    }
                }
                ;
            }
        }
        ;
        EBLK();
        return(this);
    } // End of Class Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("simtst49", args);
        RTS_UTIL.RUN_STM(new simtst49(_CTX,49,new RTS_TXT("--- Test for statements in connection blocks.")));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst49.sim","10 simtst49",1,11,11,13,13,22,15,23,17,27,19,39,21,54,32,16,35,17,42,18,45,19,53,22,57,25,60,27,65,28,78,30,81,31,84,32,87,34,92,35,95,36,103,39,108,40,115,41,127,43,130,44,133,45,136,47,141,48,144,49,152,53,158,55,165,56,173,57,179,58,184,60,187,61,190,62,193,64,198,65,201,66,211,24,216,25,219,26,228,27,233,28,235,29,247,32,250,33,271,69);
} // End of Class
