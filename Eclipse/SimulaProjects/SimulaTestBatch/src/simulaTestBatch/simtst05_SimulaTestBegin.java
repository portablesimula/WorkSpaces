// JavaLine 1 <== SourceLine 24
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:55:25 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst05_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=24, lastLine=367, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 26
    public int i1_1=0;
    public int i2_1=0;
    // JavaLine 13 <== SourceLine 27
    public int s1_1=0;
    // JavaLine 15 <== SourceLine 28
    public int s2_1=0;
    // JavaLine 17 <== SourceLine 29
    public float r1_1=0.0f;
    // JavaLine 19 <== SourceLine 30
    public float r2_1=0.0f;
    // JavaLine 21 <== SourceLine 31
    public double l1_1=0.0d;
    // JavaLine 23 <== SourceLine 32
    public double l2_1=0.0d;
    // JavaLine 25 <== SourceLine 33
    public int maxpint_1=0;
    // JavaLine 27 <== SourceLine 35
    public int maxnint_1=0;
    // JavaLine 29 <== SourceLine 36
    public int maxpshort_1=0;
    // JavaLine 31 <== SourceLine 37
    public int maxnshort_1=0;
    // JavaLine 33 <== SourceLine 38
    public float maxpreal_1=0.0f;
    // JavaLine 35 <== SourceLine 39
    public float minpreal_1=0.0f;
    // JavaLine 37 <== SourceLine 40
    public float maxnreal_1=0.0f;
    // JavaLine 39 <== SourceLine 41
    public float minnreal_1=0.0f;
    // JavaLine 41 <== SourceLine 42
    public double maxplreal_1=0.0d;
    // JavaLine 43 <== SourceLine 43
    public double minplreal_1=0.0d;
    // JavaLine 45 <== SourceLine 44
    public double maxnlreal_1=0.0d;
    // JavaLine 47 <== SourceLine 45
    public double minnlreal_1=0.0d;
    // JavaLine 49 <== SourceLine 341
    public RTS_Printfile _inspect_341_1=null;
    // JavaLine 51 <== SourceLine 353
    public RTS_Printfile _inspect_353_2=null;
    // JavaLine 53 <== SourceLine 365
    public RTS_Printfile _inspect_365_3=null;
    // Normal Constructor
    public simtst05_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst05_SimulaTestBegin _STM() {
        // JavaLine 64 <== SourceLine 16
        if(_VALUE(false)) {
            {
                RTS_BASICIO.sysout().outtext(new RTS_TXT("--- START Simula a.s. TEST"));
                ;
                RTS_BASICIO.sysout().outint(p_n,4);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outtext(p_title);
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outimage();
                ;
            }
        }
        ;
        // JavaLine 82 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 86 <== SourceLine 49
        maxpint_1=2147483647;
        ;
        // JavaLine 89 <== SourceLine 50
        maxnint_1=-2147483648;
        ;
        // JavaLine 92 <== SourceLine 52
        maxpshort_1=32767;
        ;
        // JavaLine 95 <== SourceLine 53
        maxnshort_1=-32768;
        ;
        // JavaLine 98 <== SourceLine 55
        maxpreal_1=3.4028235E38f;
        ;
        // JavaLine 101 <== SourceLine 56
        minpreal_1=RTS_ENVIRONMENT.addepsilon(0.0f);
        ;
        // JavaLine 104 <== SourceLine 57
        maxnreal_1=-3.4028235E38f;
        ;
        // JavaLine 107 <== SourceLine 58
        minnreal_1=RTS_ENVIRONMENT.subepsilon(0.0f);
        ;
        // JavaLine 110 <== SourceLine 60
        maxplreal_1=1.7976931348623157E308d;
        ;
        // JavaLine 113 <== SourceLine 61
        minplreal_1=RTS_ENVIRONMENT.addepsilon(0.0d);
        ;
        // JavaLine 116 <== SourceLine 62
        maxnlreal_1=-1.7976931348623157E308d;
        ;
        // JavaLine 119 <== SourceLine 63
        minnlreal_1=RTS_ENVIRONMENT.subepsilon(0.0d);
        ;
        // JavaLine 122 <== SourceLine 67
        if(_VALUE(((((((i1_1==(0))&((!((i1_1<(i2_1))))))&((i1_1<=(i2_1))))&((!((i1_1>(i2_1))))))&((0>=(i2_1))))&((!((i1_1!=(i2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.1 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 129 <== SourceLine 72
        i1_1=50;
        ;
        // JavaLine 132 <== SourceLine 74
        if(_VALUE(((((((!((i1_1==(i2_1))))&((!((i1_1<(0))))))&((!((99<=(i2_1))))))&((i1_1>(i2_1))))&((i1_1>=(i2_1))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.2 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 139 <== SourceLine 79
        i1_1=i2_1;
        ;
        i2_1=100;
        ;
        // JavaLine 144 <== SourceLine 81
        if(_VALUE(((((((!((i1_1==(i2_1))))&((i1_1<(i2_1))))&((i1_1<=(1))))&((!((0>(i2_1))))))&((!((i1_1>=(i2_1))))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.3 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 151 <== SourceLine 86
        i1_1=i2_1;
        ;
        // JavaLine 154 <== SourceLine 88
        if(_VALUE(((((((i1_1==(i2_1))&((!((i1_1<(100))))))&((i1_1<=(i2_1))))&((!((i1_1>(i2_1))))))&((100>=(i2_1))))&((!((i1_1!=(i2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.4 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 161 <== SourceLine 93
        i2_1=maxpint_1;
        ;
        // JavaLine 164 <== SourceLine 95
        if(_VALUE(((((((!((i1_1==(i2_1))))&((i1_1<(101))))&((1<=(i2_1))))&((!((i1_1>(i2_1))))))&((!((i1_1>=(i2_1))))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.5 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 171 <== SourceLine 100
        i2_1=50;
        ;
        // JavaLine 174 <== SourceLine 102
        if(_VALUE(((((((!((i1_1==(49))))&((!((i1_1<(i2_1))))))&((!((i1_1<=(i2_1))))))&((i1_1>(3))))&((i1_1>=(i2_1))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.6 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 181 <== SourceLine 107
        i2_1=-1;
        ;
        // JavaLine 184 <== SourceLine 109
        if(_VALUE(((((((!((i1_1==(-18))))&((!((i1_1<(i2_1))))))&((!((i1_1<=(i2_1))))))&((1>(i2_1))))&((i1_1>=(i2_1))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.7 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 191 <== SourceLine 114
        i1_1=-33;
        ;
        i2_1=1;
        ;
        // JavaLine 196 <== SourceLine 116
        if(_VALUE(((((((!((i1_1==(i2_1))))&((i1_1<(i2_1))))&((i1_1<=(i2_1))))&((!((i1_1>(i2_1))))))&((!((-3>=(i2_1))))))&((i1_1!=(55)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.8 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 203 <== SourceLine 121
        i1_1=0;
        ;
        i2_1=-10;
        ;
        // JavaLine 208 <== SourceLine 123
        if(_VALUE(((((((!((0==(i2_1))))&((!((i1_1<(-23))))))&((!((i1_1<=(i2_1))))))&((i1_1>(i2_1))))&((i1_1>=(i2_1))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.9 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 215 <== SourceLine 128
        i2_1=i1_1;
        ;
        i1_1=-1003;
        ;
        // JavaLine 220 <== SourceLine 130
        if(_VALUE(((((((!((i1_1==(i2_1))))&((i1_1<(0))))&((i1_1<=(i2_1))))&((!((-4>(i2_1))))))&((!((i1_1>=(i2_1))))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.10 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 227 <== SourceLine 135
        i2_1=i1_1;
        ;
        // JavaLine 230 <== SourceLine 137
        if(_VALUE(((((((i1_1==(i2_1))&((!((i1_1<(-1003))))))&((i1_1<=(i2_1))))&((!((i1_1>(i2_1))))))&((i1_1>=(i2_1))))&((!((i1_1!=(i2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.11 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 237 <== SourceLine 142
        i2_1=-2000;
        ;
        // JavaLine 240 <== SourceLine 144
        if(_VALUE(((((((!((i1_1==(-3200))))&((!((i1_1<(i2_1))))))&((!((i1_1<=(i2_1))))))&((i1_1>(i2_1))))&((-1>=(i2_1))))&((i1_1!=(i2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("INT.12 (comparing INTEGERs)"));
        }
        ;
        // JavaLine 247 <== SourceLine 154
        s1_1=68;
        ;
        // JavaLine 250 <== SourceLine 156
        if(_VALUE(((((((!((s1_1==(s2_1))))&((!((s1_1<(s2_1))))))&((s1_1<=(100))))&((!((0>(s2_1))))))&((s1_1>=(s2_1))))&((s1_1!=(s2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("SINT.1 (comparing short INTEGERs)"));
        }
        ;
        // JavaLine 257 <== SourceLine 161
        s1_1=10;
        ;
        s2_1=maxnshort_1;
        ;
        // JavaLine 262 <== SourceLine 163
        if(_VALUE(((((((!((68==(s2_1))))&((!((s1_1<(s2_1))))))&((!((i2_1<=(s2_1))))))&((s1_1>(s2_1))))&((s1_1>=(s2_1))))&((!((s1_1!=(10)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("SINT.2 (comparing short INTEGERs)"));
        }
        ;
        // JavaLine 269 <== SourceLine 168
        s1_1=-5;
        ;
        s2_1=-800;
        ;
        // JavaLine 274 <== SourceLine 170
        if(_VALUE(((((((!((s1_1==(s2_1))))&((!((s1_1<(s2_1))))))&((s1_1<=(-5))))&((s1_1>(s2_1))))&((s1_1>=(s2_1))))&((s1_1!=(s2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("SINT.3 (comparing short INTEGERs)"));
        }
        ;
        // JavaLine 281 <== SourceLine 178
        if(_VALUE(((((((r1_1==(0.0f))&((!((0.0f<(r2_1))))))&((r1_1<=(r2_1))))&((!((r1_1>(r2_1))))))&((r1_1>=(r2_1))))&((!((r1_1!=(r2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.1 (comparing REALs)"));
        }
        ;
        // JavaLine 288 <== SourceLine 183
        r1_1=minpreal_1;
        ;
        r2_1=0.0f;
        ;
        // JavaLine 293 <== SourceLine 185
        if(_VALUE(((((((r2_1==(0.0f))&((!((r1_1<(r2_1))))))&((!((r1_1<=(r2_1))))))&((r1_1>(r2_1))))&((r1_1>=(r2_1))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.2 (comparing REALs)"));
        }
        ;
        // JavaLine 300 <== SourceLine 190
        r1_1=0.0f;
        ;
        r2_1=3.4567E7f;
        ;
        // JavaLine 305 <== SourceLine 192
        if(_VALUE(((((((r1_1==(r1_1))&((r1_1<(88.0f))))&((r1_1<=(r2_1))))&((!((r1_1>(r2_1))))))&((!((0.0f>=(r2_1))))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.3 (comparing REALs)"));
        }
        ;
        // JavaLine 312 <== SourceLine 197
        r2_1=r1_1;
        ;
        // JavaLine 315 <== SourceLine 199
        if(_VALUE(((((((r1_1==(r2_1))&((!((r1_1<(r2_1))))))&((r2_1<=(r1_1))))&((!((r1_1>(r2_1))))))&((r1_1>=(r2_1))))&((!((r2_1!=(r1_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.4 (comparing REALs)"));
        }
        ;
        // JavaLine 322 <== SourceLine 204
        r2_1=3.4568E7f;
        ;
        // JavaLine 325 <== SourceLine 206
        if(_VALUE(((((((r2_1==(r2_1))&((r1_1<(r2_1))))&((r1_1<=(r2_1))))&((!((r1_1>(r2_1))))))&((!((r1_1>=(3.4568E7f))))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.5 (comparing REALs)"));
        }
        ;
        // JavaLine 332 <== SourceLine 211
        r1_1=20.0f;
        ;
        r2_1=2.0E11f;
        ;
        // JavaLine 337 <== SourceLine 213
        if(_VALUE(((((((r1_1==(r1_1))&((r1_1<(r2_1))))&((0.1f<=(r2_1))))&((!((r1_1>(r2_1))))))&((!((r1_1>=(r2_1))))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.6 (comparing REALs)"));
        }
        ;
        // JavaLine 344 <== SourceLine 218
        r1_1=maxnreal_1;
        ;
        r2_1=minpreal_1;
        ;
        // JavaLine 349 <== SourceLine 220
        if(_VALUE(((((((r1_1==(r1_1))&((r1_1<(r2_1))))&((-0.1f<=(r2_1))))&((!((r1_1>(2.0E-5f))))))&((!((r1_1>=(r2_1))))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.7 (comparing REALs)"));
        }
        ;
        // JavaLine 356 <== SourceLine 225
        r1_1=maxpreal_1;
        ;
        r2_1=-0.00222222f;
        ;
        // JavaLine 361 <== SourceLine 227
        if(_VALUE(((((((r2_1==(r2_1))&((!((23.456f<(r2_1))))))&((!((r1_1<=(r2_1))))))&((r1_1>(-1.1f))))&((r1_1>=(r2_1))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.8 (comparing REALs)"));
        }
        ;
        // JavaLine 368 <== SourceLine 232
        r1_1=0.0f;
        ;
        r2_1=-58.9f;
        ;
        // JavaLine 373 <== SourceLine 234
        if(_VALUE(((((((r1_1==(-0.0f))&((!((0.0f<(r2_1))))))&((!((r1_1<=(r2_1))))))&((r1_1>(r2_1))))&((r1_1>=(r2_1))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.9 (comparing REALs)"));
        }
        ;
        // JavaLine 380 <== SourceLine 239
        r1_1=-58.9f;
        ;
        // JavaLine 383 <== SourceLine 241
        if(_VALUE(((((((r1_1==(r2_1))&((!((r2_1<(r1_1))))))&((r1_1<=(r2_1))))&((!((r1_1>(r2_1))))))&((r2_1>=(r1_1))))&((!((r1_1!=(r2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.10 (comparing REALs)"));
        }
        ;
        // JavaLine 390 <== SourceLine 246
        r1_1=maxnreal_1;
        ;
        // JavaLine 393 <== SourceLine 248
        if(_VALUE(((((((r2_1==(r2_1))&((r1_1<(r2_1))))&((r1_1<=(-0.22f))))&((!((r1_1>(r2_1))))))&((!((r1_1>=(r2_1))))))&((r1_1!=(r2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("real.11 (comparing REALs)"));
        }
        ;
        // JavaLine 400 <== SourceLine 256
        if(_VALUE(((((((l1_1==(l2_1))&((!((l1_1<(l2_1))))))&((0.0d<=(l2_1))))&((!((l1_1>(l2_1))))))&((l1_1>=(0.0d))))&((!((l1_1!=(l2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("long.1 (comparing long REALs)"));
        }
        ;
        // JavaLine 407 <== SourceLine 261
        l1_1=maxplreal_1;
        ;
        l2_1=280000.0d;
        ;
        // JavaLine 412 <== SourceLine 263
        if(_VALUE(((((((l1_1==(l1_1))&((!((281000.0d<(l2_1))))))&((!((l1_1<=(l2_1))))))&((l1_1>(l2_1))))&((l1_1>=(l2_1))))&((l1_1!=(l2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("long.2 (comparing long REALs)"));
        }
        ;
        // JavaLine 419 <== SourceLine 268
        l1_1=minnlreal_1;
        ;
        l2_1=0.0d;
        ;
        // JavaLine 424 <== SourceLine 270
        if(_VALUE(((((((l1_1==(l1_1))&((l1_1<(l2_1))))&((l1_1<=(l2_1))))&((!((l1_1>(l2_1))))))&((!((l1_1>=(l2_1))))))&((l1_1!=(l2_1)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("long.3 (comparing long REALs)"));
        }
        ;
        // JavaLine 431 <== SourceLine 280
        if(_VALUE(((((((l2_1==(0.0d))&((((double)(r1_1))<(l2_1))))&((!((l1_1<=(((double)(r2_1))))))))&((!((((double)(r1_1))>(l1_1))))))&((!((((double)(r1_1))>=(2.0E10d))))))&((!((r2_1!=(r2_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("RL.1 (comparing real and long real)"));
        }
        ;
        // JavaLine 438 <== SourceLine 290
        if(_VALUE(((((((i2_1==(i2_1))&((!((((float)(i1_1))<(r1_1))))))&((!((r2_1<=(((float)(i2_1))))))))&((((float)(i2_1))>(r1_1))))&((!((r1_1>=(-100000.0f))))))&((r2_1!=(((float)(i1_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("RI.1 (comparing real and integer)"));
        }
        ;
        // JavaLine 445 <== SourceLine 300
        if(_VALUE(((((((i2_1==(i2_1))&((!((l1_1<(((double)(i1_1))))))))&((!((l2_1<=(((double)(i2_1))))))))&((!((l1_1>(1.0d))))))&((!((((double)(i1_1))>=(l2_1))))))&((l2_1!=(((double)(i1_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("IL.1 (comparing integer and long real)"));
        }
        ;
        // JavaLine 452 <== SourceLine 310
        if(_VALUE(((((((s2_1==(s2_1))&((!((((float)(s1_1))<(r1_1))))))&((r1_1<=(((float)(s2_1))))))&((r2_1>(((float)(s2_1))))))&((!((r2_1>=(((float)(s1_1))))))))&((!((r1_1!=(r1_1)))))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("SR.1 (comparing short integer and real)"));
        }
        ;
        // JavaLine 459 <== SourceLine 320
        if(_VALUE(((((((l1_1==(l1_1))&((!((l2_1<(((double)(s1_1))))))))&((((double)(s2_1))<=(l2_1))))&((l1_1>(((double)(s2_1))))))&((!((l1_1>=(69.0d))))))&((((double)(s2_1))!=(-810.0d)))))) {
            ;
        } else {
            new SimulaTest_err((_CUR),new RTS_TXT("SL.1 (comparing short integer and long real)"));
        }
        ;
        // JavaLine 466 <== SourceLine 331
        i1_1=maxnint_1;
        ;
        i2_1=maxpint_1;
        ;
        // JavaLine 471 <== SourceLine 24
        RTS_BASICIO.sysout().outtext(new RTS_TXT("*** error: OVERFLOW in test MAX.1 (COMPARING maxint and minint)"));
        ;
        // JavaLine 474 <== SourceLine 334
