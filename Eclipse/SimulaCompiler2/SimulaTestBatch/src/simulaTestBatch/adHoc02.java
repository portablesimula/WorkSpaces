// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Oct 09 12:26:39 CEST 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc02 extends RTS_Simulation {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=1, lastLine=73, hasLocalClasses=true, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 30
    public float sum_venting_2=0.0f;
    public float maks_venting_2=0.0f;
    // JavaLine 15 <== SourceLine 31
    public int antall_ventinger_2=0;
    public int maks_i_koe_2=0;
    // JavaLine 18 <== SourceLine 46
    public int avganger_i_timen_2=0;
    // JavaLine 20 <== SourceLine 47
    public int i_2=0;
    public int u_2=0;
    // JavaLine 23 <== SourceLine 49
    public adHoc02_Sporveksel pens_vestfra_2=null;
    public adHoc02_Sporveksel pens_oestfra_2=null;
    // Normal Constructor
    public adHoc02(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc02 _STM() {
        // BEGIN Simset INNER PART
        // BEGIN Simulation INNER PART
        // BEGIN adHoc02 INNER PART
        // ENDOF adHoc02 INNER PART
        // JavaLine 39 <== SourceLine 50
        pens_vestfra_2=new adHoc02_Sporveksel((_CUR))._STM();
        ;
        // JavaLine 42 <== SourceLine 51
        pens_oestfra_2=new adHoc02_Sporveksel((_CUR))._STM();
        ;
        // JavaLine 45 <== SourceLine 52
        pens_oestfra_2.har_pinnen=true;
        ;
        // JavaLine 48 <== SourceLine 54
        avganger_i_timen_2=12;
        ;
        // JavaLine 51 <== SourceLine 55
        u_2=2468753;
        ;
        for(i_2=1;i_2<=RTS_UTIL._IMUL(avganger_i_timen_2,24);i_2++) {
            // JavaLine 55 <== SourceLine 56
            {
                // JavaLine 57 <== SourceLine 57
            ((adHoc02)(_CUR)).ActivateAt(false,(RTS_Process)new adHoc02_Trikk((_CUR),pens_vestfra_2,pens_oestfra_2)._START(),RTS_ENVIRONMENT.uniform(1.0d,86400.0d,new RTS_NAME<Integer>(){ public Integer get() { return(u_2); } public Integer put(Integer x_) { return(u_2=(int)x_); } }),false);
                ;
                // JavaLine 60 <== SourceLine 59
            ((adHoc02)(_CUR)).ActivateAt(false,(RTS_Process)new adHoc02_Trikk((_CUR),pens_oestfra_2,pens_vestfra_2)._START(),RTS_ENVIRONMENT.uniform(1.0d,86400.0d,new RTS_NAME<Integer>(){ public Integer get() { return(u_2); } public Integer put(Integer x_) { return(u_2=(int)x_); } }),false);
                ;
            }
        }
        ;
        // JavaLine 66 <== SourceLine 64
        hold(86400.0d);
        ;
        // JavaLine 69 <== SourceLine 65
        RTS_BASICIO.sysout().outtext(new RTS_TXT("Snitt venting: "));
        ;
        // JavaLine 72 <== SourceLine 66
        RTS_BASICIO.sysout().outfix((sum_venting_2/(((float)(antall_ventinger_2)))),1,0);
        ;
        // JavaLine 75 <== SourceLine 67
        RTS_BASICIO.sysout().outtext(new RTS_TXT("   max: "));
        ;
        // JavaLine 78 <== SourceLine 68
        RTS_BASICIO.sysout().outint((int)Math.round(maks_venting_2),0);
        ;
        // JavaLine 81 <== SourceLine 69
        RTS_BASICIO.sysout().outtext(new RTS_TXT("   max i k\u00f8: "));
        ;
        // JavaLine 84 <== SourceLine 70
        RTS_BASICIO.sysout().outint(maks_i_koe_2,0);
        ;
        // JavaLine 87 <== SourceLine 71
        RTS_BASICIO.sysout().outimage();
        ;
        // ENDOF Simulation INNER PART
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    
    public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
        RTS_UTIL.BPRG("adHoc02", args);
        RTS_UTIL.RUN_STM(new adHoc02(_CTX));
    } // End of main
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc02.sim","10 adHoc02",1,1,12,30,15,31,18,46,20,47,23,49,39,50,42,51,45,52,48,54,51,55,55,56,57,57,60,59,66,64,69,65,72,66,75,67,78,68,81,69,84,70,87,71,100,73);
} // End of Class
