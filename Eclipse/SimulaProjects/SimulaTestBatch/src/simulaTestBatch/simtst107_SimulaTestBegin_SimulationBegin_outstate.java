package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin_outstate extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=36, lastLine=71, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public int p_c;
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 39
    public int i=0;
    public float r=0.0f;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_outstate setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_c=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst107_SimulaTestBegin_SimulationBegin_outstate(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_outstate(RTS_RTObject _SL,int sp_c) {
        super(_SL);
        // Parameter assignment to locals
        this.p_c = sp_c;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_outstate _STM() {
        // JavaLine 41 <== SourceLine 40
        if(_VALUE(false)) {
            {
                RTS_BASICIO.sysout().outtext(new RTS_TXT("State("));
                ;
                RTS_BASICIO.sysout().outint(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).testno_2,1);
                ;
                RTS_BASICIO.sysout().outchar(',');
                ;
                RTS_BASICIO.sysout().outint(p_c,2);
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT("): "));
                ;
                for(i=1;i<=10;i++) {
                    // JavaLine 55 <== SourceLine 45
                    {
                        if(_VALUE(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.getELEMENT(i))) {
                            RTS_BASICIO.sysout().outtext(new RTS_TXT(" a    "));
                        } else {
                            // JavaLine 60 <== SourceLine 46
                            if(_VALUE(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.getELEMENT(i))) {
                                RTS_BASICIO.sysout().outtext(new RTS_TXT(" t    "));
                            } else {
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("p/s   "));
                            }
                        }
                        ;
                    }
                }
                ;
                // JavaLine 71 <== SourceLine 40
                new simtst107_SimulaTestBegin_SimulationBegin_outimage((_CUR._SL));
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT("SQS:      "));
                ;
                for(i=1;i<=10;i++) {
                    // JavaLine 77 <== SourceLine 52
                    {
                        if(_VALUE(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).idle())) {
                            // JavaLine 80 <== SourceLine 53
                            {
                                if(_VALUE(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).terminated())) {
                                    RTS_BASICIO.sysout().outtext(new RTS_TXT("    t "));
                                } else {
                                    RTS_BASICIO.sysout().outtext(new RTS_TXT("    p "));
                                }
                                ;
                            }
                        } else {
                            // JavaLine 90 <== SourceLine 56
                            if(_VALUE((((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).current()==(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i))))) {
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("    a "));
                            } else {
                                RTS_BASICIO.sysout().outtext(new RTS_TXT("    s "));
                            }
                        }
                        ;
                    }
                }
                ;
                // JavaLine 101 <== SourceLine 40
                new simtst107_SimulaTestBegin_SimulationBegin_outimage((_CUR._SL));
                ;
                RTS_BASICIO.sysout().outfix(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time(),2,6);
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT(" :   "));
                ;
                for(i=1;i<=10;i++) {
                    // JavaLine 109 <== SourceLine 62
                    {
                        if(_VALUE(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).idle())) {
                            RTS_BASICIO.sysout().outtext(new RTS_TXT("  idle"));
                        } else {
                            // JavaLine 114 <== SourceLine 63
                            if(_VALUE((((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i)!=(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).current())))) {
                                RTS_BASICIO.sysout().outfix(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).evtime(),2,6);
                            } else {
                                // JavaLine 118 <== SourceLine 65
                                {
                                    r=((float)(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time()));
                                    ;
                                    // JavaLine 122 <== SourceLine 66
                                    if(_VALUE((RTS_ENVIRONMENT.abs((((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).evtime()-(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time())))>(0.0010000000474974513d)))) {
                                        // JavaLine 124 <== SourceLine 67
                                        {
                                            RTS_BASICIO.sysout().outfix(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).evtime(),2,5);
                                            ;
                                            RTS_BASICIO.sysout().outchar('*');
                                        }
                                    } else {
                                        // JavaLine 131 <== SourceLine 66
                                        RTS_BASICIO.sysout().outfix(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.getELEMENT(i).evtime(),2,6);
                                    }
                                    ;
                                }
                            }
                        }
                    }
                }
                ;
                // JavaLine 141 <== SourceLine 40
                new simtst107_SimulaTestBegin_SimulationBegin_outimage((_CUR._SL));
                ;
            }
        }
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Procedure outstate",10,39,41,40,55,45,60,46,71,40,77,52,80,53,90,56,101,40,109,62,114,63,118,65,122,66,124,67,131,66,141,40,149,71);
} // End of Procedure
