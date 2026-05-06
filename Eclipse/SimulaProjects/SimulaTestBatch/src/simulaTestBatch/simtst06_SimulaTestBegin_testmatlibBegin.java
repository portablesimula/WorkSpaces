package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlibBegin extends simtst06_SimulaTestBegin_testmatlib {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=2, firstLine=274, lastLine=313, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 275
    public int i_1=0;
    public int j_1=0;
    public int k_1=0;
    public int u1_1=0;
    public int u2_1=0;
    public double x_1=0.0d;
    public double y_1=0.0d;
    // JavaLine 17 <== SourceLine 276
    public final double limexp_1=(double)(709.782712893384d);
    // JavaLine 19 <== SourceLine 277
    public final int Ipowbaselim_1=(int)(215);
    public final int Ipowexplim_1=(int)(4);
    // JavaLine 22 <== SourceLine 279
    public final double Lpowbaselim_1=(double)(1.844674407370955E19d);
    public final double Lpowexplim_1=(double)(16.0d);
    // JavaLine 25 <== SourceLine 281
    public final double Twopi_1=(double)(6.2831853d);
    // JavaLine 27 <== SourceLine 282
    public final double Pihalf_1=(double)(1.5707963d);
    // JavaLine 29 <== SourceLine 283
    public double hlexp_1=0.0d;
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlibBegin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlibBegin _STM() {
        // JavaLine 40 <== SourceLine 270
        maxlongdig=1.0E18d;
        // JavaLine 42 <== SourceLine 21
        // BEGIN testmatlib INNER PART
        // BEGIN testmatlibBegin INNER PART
        // ENDOF testmatlibBegin INNER PART
        // JavaLine 46 <== SourceLine 284
        u1_1=455470315;
        // JavaLine 48 <== SourceLine 285
        u2_1=2011455689;
        for(k_1=1;k_1<=20;k_1++) {
            // JavaLine 51 <== SourceLine 288
            {
                // JavaLine 53 <== SourceLine 289
            i_1=RTS_ENVIRONMENT.randint((-(215)),215,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 55 <== SourceLine 290
            j_1=RTS_ENVIRONMENT.randint(0,4,new RTS_NAME<Integer>(){ public Integer get() { return(u2_1); } public Integer put(Integer x_) { return(u2_1=(int)x_); } });
                // JavaLine 57 <== SourceLine 291
                new simtst06_SimulaTestBegin_testmatlib_testipower((_CUR),i_1,j_1);
                // JavaLine 59 <== SourceLine 292
            x_1=RTS_ENVIRONMENT.uniform(0.0d,1.844674407370955E19d,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 61 <== SourceLine 293
            y_1=RTS_ENVIRONMENT.uniform((-(16.0d)),16.0d,new RTS_NAME<Integer>(){ public Integer get() { return(u2_1); } public Integer put(Integer x_) { return(u2_1=(int)x_); } });
                // JavaLine 63 <== SourceLine 294
                new simtst06_SimulaTestBegin_testmatlib_testrpower((_CUR),x_1,y_1);
                // JavaLine 65 <== SourceLine 295
            x_1=RTS_ENVIRONMENT.uniform(0.0d,1.7976931348623157E308d,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 67 <== SourceLine 296
                new simtst06_SimulaTestBegin_testmatlib_testsqrt((_CUR),x_1);
                // JavaLine 69 <== SourceLine 297
            y_1=RTS_ENVIRONMENT.uniform(0.0d,(1.7976931348623157E308d/(x_1)),new RTS_NAME<Integer>(){ public Integer get() { return(u2_1); } public Integer put(Integer x_) { return(u2_1=(int)x_); } });
                // JavaLine 71 <== SourceLine 298
                new simtst06_SimulaTestBegin_testmatlib_testln((_CUR),x_1,y_1);
                // JavaLine 73 <== SourceLine 299
            x_1=RTS_ENVIRONMENT.uniform((-(709.782712893384d)),709.782712893384d,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 75 <== SourceLine 300
                y_1=RTS_ENVIRONMENT.abs(x_1);
                // JavaLine 77 <== SourceLine 301
                hlexp_1=(((y_1<(1.0d)))?(709.782712893384d):((709.782712893384d/(y_1))));
                // JavaLine 79 <== SourceLine 302
            y_1=RTS_ENVIRONMENT.uniform((-(hlexp_1)),hlexp_1,new RTS_NAME<Integer>(){ public Integer get() { return(u2_1); } public Integer put(Integer x_) { return(u2_1=(int)x_); } });
                // JavaLine 81 <== SourceLine 303
                new simtst06_SimulaTestBegin_testmatlib_testexp((_CUR),x_1,y_1);
                // JavaLine 83 <== SourceLine 304
                new simtst06_SimulaTestBegin_testmatlib_testlnexp((_CUR),RTS_ENVIRONMENT.abs(x_1));
                // JavaLine 85 <== SourceLine 305
            x_1=RTS_ENVIRONMENT.uniform((-(6.2831853d)),6.2831853d,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 87 <== SourceLine 306
                new simtst06_SimulaTestBegin_testmatlib_testsincos((_CUR),x_1);
                // JavaLine 89 <== SourceLine 307
            x_1=RTS_ENVIRONMENT.uniform((-(1.5707963d)),1.5707963d,new RTS_NAME<Integer>(){ public Integer get() { return(u1_1); } public Integer put(Integer x_) { return(u1_1=(int)x_); } });
                // JavaLine 91 <== SourceLine 308
                new simtst06_SimulaTestBegin_testmatlib_testarctan((_CUR),x_1);
                // JavaLine 93 <== SourceLine 309
                new simtst06_SimulaTestBegin_testmatlib_testepsilon((_CUR),x_1);
            }
        }
        // JavaLine 97 <== SourceLine 312
        new simtst06_SimulaTestBegin_testmatlib_testepsilon((_CUR),0.0d);
        // ENDOF testmatlib INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","PrefixedBlock testmatlibBegin",9,275,17,276,19,277,22,279,25,281,27,282,29,283,40,270,42,21,46,284,48,285,51,288,53,289,55,290,57,291,59,292,61,293,63,294,65,295,67,296,69,297,71,298,73,299,75,300,77,301,79,302,81,303,83,304,85,305,87,306,89,307,91,308,93,309,97,312,102,313);
} // End of Class
