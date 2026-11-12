// JavaLine 1 <== SourceLine 1
package simprog;
// Simula-2.0 Compiled at Thu Nov 12 09:38:40 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ForStatement_Errors_Block0_P extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=1, lastLine=3, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_NAME<Integer> p_n;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public ForStatement_Errors_Block0_P setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_n=(RTS_NAME<Integer>)param; break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public ForStatement_Errors_Block0_P(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public ForStatement_Errors_Block0_P(RTS_RTObject _SL,RTS_NAME<Integer> sp_n) {
        super(_SL);
        // Parameter assignment to locals
        this.p_n = sp_n;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public ForStatement_Errors_Block0_P _STM() {
        // JavaLine 39 <== SourceLine 2
        for(boolean CB_2:new FOR_List(
        new FOR_SingleElt<Number>(new RTS_NAME<Number>(){ public Number put(Number x_){p_n.get()=x_.intValue(); return(x_);};  public Number get(){return((Number)p_n.get()); }	},new RTS_NAME<Number>() { public Number get(){return(4); }})
       ,new FOR_StepUntil(new RTS_NAME<Number>(){ public Number put(Number x_){p_n.get()=x_.intValue(); return(x_);};  public Number get(){return((Number)p_n.get()); }	},new RTS_NAME<Number>() { public Number get(){return(4); }},new RTS_NAME<Number>() { public Number get(){return(1); }},new RTS_NAME<Number>() { public Number get(){return(16); }})
           )) { if(!CB_2) continue;
            ;
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/statement/ForStatement_Errors.sim","Procedure P",1,1,39,2,48,3);
} // End of Procedure
