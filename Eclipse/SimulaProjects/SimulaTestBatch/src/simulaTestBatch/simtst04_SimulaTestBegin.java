// JavaLine 1 <== SourceLine 30
package simulaTestBatch;
// Simula-2.0 Compiled at Fri Apr 03 09:55:09 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst04_SimulaTestBegin extends SimulaTest {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=30, lastLine=385, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 32
    public RTS_TXT txt_1=null;
    public int intr_1=0;
    // JavaLine 13 <== SourceLine 35
    public int sho_1=0;
    // JavaLine 15 <== SourceLine 36
    public float rea_1=0.0f;
    // JavaLine 17 <== SourceLine 37
    public double lon_1=0.0d;
    // JavaLine 19 <== SourceLine 38
    public int realprecision_1=0;
    // JavaLine 21 <== SourceLine 40
    public int longprecision_1=0;
    // Normal Constructor
    public simtst04_SimulaTestBegin(RTS_RTObject staticLink,int sp_n,RTS_TXT sp_title) {
        super(staticLink,sp_n,sp_title);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public simtst04_SimulaTestBegin _STM() {
        // JavaLine 32 <== SourceLine 16
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
        // JavaLine 50 <== SourceLine 22
        // BEGIN SimulaTest INNER PART
        // BEGIN SimulaTestBegin INNER PART
        // ENDOF SimulaTestBegin INNER PART
        // JavaLine 54 <== SourceLine 58
        txt_1=RTS_ENVIRONMENT.blanks(30);
        ;
        // JavaLine 57 <== SourceLine 60
        realprecision_1=7;
        ;
        // JavaLine 60 <== SourceLine 63
        longprecision_1=16;
        ;
        // JavaLine 63 <== SourceLine 30
        RTS_TXT.putint(txt_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (1)."),txt_1,new RTS_TXT("                             7"));
        ;
        RTS_TXT.putint(txt_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (2)."),txt_1,new RTS_TXT("                            16"));
        ;
        // JavaLine 72 <== SourceLine 75
        intr_1=123456;
        ;
        // JavaLine 75 <== SourceLine 30
        RTS_TXT.putint(txt_1,intr_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (3)."),txt_1,new RTS_TXT("                        123456"));
        ;
        // JavaLine 80 <== SourceLine 79
        intr_1=2147483647;
        ;
        // JavaLine 83 <== SourceLine 30
        RTS_TXT.putint(txt_1,intr_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (4)."),txt_1,new RTS_TXT("                    2147483647"));
        ;
        RTS_TXT.putint(txt_1,0);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (6)."),txt_1,new RTS_TXT("                             0"));
        ;
        // JavaLine 92 <== SourceLine 86
        intr_1=-2147483648;
        ;
        // JavaLine 95 <== SourceLine 30
        RTS_TXT.putint(txt_1,intr_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (7)."),txt_1,new RTS_TXT("                   -2147483648"));
        ;
        RTS_TXT.putint(txt_1,0);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("integer operation (8)."),txt_1,new RTS_TXT("                             0"));
        ;
        RTS_TXT.putint(txt_1,sho_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("short integer operation (1)."),txt_1,new RTS_TXT("                             0"));
        ;
        // JavaLine 108 <== SourceLine 100
        sho_1=32767;
        ;
        // JavaLine 111 <== SourceLine 30
        RTS_TXT.putint(txt_1,sho_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("short integer operation (2)."),txt_1,new RTS_TXT("                         32767"));
        ;
        // JavaLine 116 <== SourceLine 106
        sho_1=13;
        ;
        // JavaLine 119 <== SourceLine 30
        RTS_TXT.putint(txt_1,sho_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("short integer operation (4)."),txt_1,new RTS_TXT("                            13"));
        ;
        // JavaLine 124 <== SourceLine 110
        sho_1=-32768;
        ;
        // JavaLine 127 <== SourceLine 30
        RTS_TXT.putint(txt_1,sho_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("short integer operation (5)."),txt_1,new RTS_TXT("                        -32768"));
        ;
        // JavaLine 132 <== SourceLine 119
        rea_1=123456.0f;
        ;
        // JavaLine 135 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (1)."),txt_1,new RTS_TXT("                  1.234560&+05"));
        ;
        // JavaLine 140 <== SourceLine 129
        rea_1=3.4028235E38f;
        ;
        // JavaLine 143 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (2)."),txt_1,new RTS_TXT("                  3.402823&+38"));
        ;
        // JavaLine 148 <== SourceLine 134
        rea_1=-3.0f;
        ;
        // JavaLine 151 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (3)."),txt_1,new RTS_TXT("                 -3.000000&+00"));
        ;
        // JavaLine 156 <== SourceLine 138
        rea_1=0.8888889f;
        ;
        // JavaLine 159 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (4)."),txt_1,new RTS_TXT("                  8.888889&-01"));
        ;
        // JavaLine 164 <== SourceLine 142
        rea_1=0.0f;
        ;
        // JavaLine 167 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (5)."),txt_1,new RTS_TXT("                  0.000000&+00"));
        ;
        // JavaLine 172 <== SourceLine 151
        rea_1=-1.192093E-7f;
        ;
        // JavaLine 175 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (6)."),txt_1,new RTS_TXT("                 -1.192093&-07"));
        ;
        // JavaLine 180 <== SourceLine 157
        rea_1=0.0f;
        ;
        // JavaLine 183 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (7)."),txt_1,new RTS_TXT("                  0.000000&+00"));
        ;
        // JavaLine 188 <== SourceLine 161
        rea_1=1235.0f;
        ;
        // JavaLine 191 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (8)."),txt_1,new RTS_TXT("                  1.235000&+03"));
        ;
        // JavaLine 196 <== SourceLine 170
        rea_1=-3.4028235E38f;
        ;
        // JavaLine 199 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (9)."),txt_1,new RTS_TXT("                 -3.402823&+38"));
        ;
        // JavaLine 204 <== SourceLine 176
        rea_1=1000.0f;
        ;
        // JavaLine 207 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (10)."),txt_1,new RTS_TXT("                  1.000000&+03"));
        ;
        RTS_TXT.putreal(txt_1,1.0E20f,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (11)."),txt_1,new RTS_TXT("                  1.000000&+20"));
        ;
        // JavaLine 216 <== SourceLine 183
        rea_1=1.0f;
        ;
        // JavaLine 219 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (12)."),txt_1,new RTS_TXT("                  1.000000&+00"));
        ;
        RTS_TXT.putreal(txt_1,1000000.0f,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (13)."),txt_1,new RTS_TXT("                  1.000000&+06"));
        ;
        // JavaLine 228 <== SourceLine 190
        rea_1=10.0f;
        ;
        // JavaLine 231 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (14)."),txt_1,new RTS_TXT("                  1.000000&+01"));
        ;
        RTS_TXT.putreal(txt_1,1.0E-37f,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (15)."),txt_1,new RTS_TXT("                  1.000000&-37"));
        ;
        // JavaLine 240 <== SourceLine 197
        rea_1=-100.0f;
        ;
        // JavaLine 243 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (16)."),txt_1,new RTS_TXT("                 -1.000000&+02"));
        ;
        // JavaLine 248 <== SourceLine 201
        rea_1=-1.0E35f;
        ;
        // JavaLine 251 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (17)."),txt_1,new RTS_TXT("                 -1.000000&+35"));
        ;
        // JavaLine 256 <== SourceLine 205
        rea_1=-0.01f;
        ;
        // JavaLine 259 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (18)."),txt_1,new RTS_TXT("                 -1.000000&-02"));
        ;
        RTS_TXT.putreal(txt_1,3.4028235E38f,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (19)."),txt_1,new RTS_TXT("                  3.402823&+38"));
        ;
        // JavaLine 268 <== SourceLine 216
        rea_1=1.234568E9f;
        ;
        // JavaLine 271 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (20)."),txt_1,new RTS_TXT("                  1.234568&+09"));
        ;
        // JavaLine 276 <== SourceLine 220
        rea_1=3.333E-5f;
        ;
        // JavaLine 279 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (21)."),txt_1,new RTS_TXT("                  3.333000&-05"));
        ;
        // JavaLine 284 <== SourceLine 224
        rea_1=2.2222223E-38f;
        ;
        // JavaLine 287 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (22)."),txt_1,new RTS_TXT("                  2.222222&-38"));
        ;
        // JavaLine 292 <== SourceLine 228
        rea_1=5.5555556E7f;
        ;
        // JavaLine 295 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (23)."),txt_1,new RTS_TXT("                  5.555556&+07"));
        ;
        // JavaLine 300 <== SourceLine 232
        rea_1=-1.0E12f;
        ;
        // JavaLine 303 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (24)."),txt_1,new RTS_TXT("                 -1.000000&+12"));
        ;
        // JavaLine 308 <== SourceLine 236
        rea_1=-0.0f;
        ;
        // JavaLine 311 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (25)."),txt_1,new RTS_TXT("                  0.000000&+00"));
        ;
        // JavaLine 316 <== SourceLine 240
        rea_1=3333333.2f;
        ;
        // JavaLine 319 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (26)."),txt_1,new RTS_TXT("                  3.333333&+06"));
        ;
        // JavaLine 324 <== SourceLine 244
        rea_1=7.777778E-11f;
        ;
        // JavaLine 327 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (27)."),txt_1,new RTS_TXT("                  7.777778&-11"));
        ;
        // JavaLine 332 <== SourceLine 248
        rea_1=1.1111111E-5f;
        ;
        // JavaLine 335 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (28)."),txt_1,new RTS_TXT("                  1.111111&-05"));
        ;
        // JavaLine 340 <== SourceLine 252
        rea_1=-0.6666667f;
        ;
        // JavaLine 343 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (29)."),txt_1,new RTS_TXT("                 -6.666667&-01"));
        ;
        // JavaLine 348 <== SourceLine 262
        rea_1=1.192093E-9f;
        ;
        // JavaLine 351 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (30)."),txt_1,new RTS_TXT("                  1.192093&-09"));
        ;
        // JavaLine 356 <== SourceLine 266
        rea_1=0.0f;
        ;
        // JavaLine 359 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (31)."),txt_1,new RTS_TXT("                  0.000000&+00"));
        ;
        // JavaLine 364 <== SourceLine 270
        rea_1=1.2345678E32f;
        ;
        // JavaLine 367 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (32)."),txt_1,new RTS_TXT("                  1.234568&+32"));
        ;
        // JavaLine 372 <== SourceLine 274
        rea_1=1.1111112f;
        ;
        // JavaLine 375 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (33)."),txt_1,new RTS_TXT("                  1.111111&+00"));
        ;
        // JavaLine 380 <== SourceLine 278
        rea_1=1.2345679E-6f;
        ;
        // JavaLine 383 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (34)."),txt_1,new RTS_TXT("                  1.234568&-06"));
        ;
        // JavaLine 388 <== SourceLine 287
        rea_1=-3.4028235E38f;
        ;
        // JavaLine 391 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (35)."),txt_1,new RTS_TXT("                 -3.402823&+38"));
        ;
        // JavaLine 396 <== SourceLine 291
        rea_1=-300000.0f;
        ;
        // JavaLine 399 <== SourceLine 30
        RTS_TXT.putreal(txt_1,rea_1,realprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("real operation (36)."),txt_1,new RTS_TXT("                 -3.000000&+05"));
        ;
        // JavaLine 404 <== SourceLine 298
        lon_1=1.2345678912345678E16d;
        ;
        // JavaLine 407 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (1)."),txt_1,new RTS_TXT("        1.234567891234568&+016"));
        ;
        // JavaLine 412 <== SourceLine 308
        lon_1=1.797693134862315E308d;
        ;
        // JavaLine 415 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (2)."),txt_1,new RTS_TXT("        1.797693134862315&+308"));
        ;
        RTS_TXT.putreal(txt_1,0.8888888888888888d,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (3)."),txt_1,new RTS_TXT("        8.888888888888888&-001"));
        ;
        // JavaLine 424 <== SourceLine 316
        lon_1=0.0d;
        ;
        // JavaLine 427 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (4)."),txt_1,new RTS_TXT("        0.000000000000000&+000"));
        ;
        // JavaLine 432 <== SourceLine 325
        lon_1=-2.220446049250313E-18d;
        ;
        // JavaLine 435 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (5)."),txt_1,new RTS_TXT("       -2.220446049250313&-018"));
        ;
        // JavaLine 440 <== SourceLine 331
        lon_1=1234.999999991111d;
        ;
        // JavaLine 443 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (6)."),txt_1,new RTS_TXT("        1.234999999991111&+003"));
        ;
        // JavaLine 448 <== SourceLine 335
        lon_1=1000.0d;
        ;
        // JavaLine 451 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (7)."),txt_1,new RTS_TXT("        1.000000000000000&+003"));
        ;
        RTS_TXT.putreal(txt_1,1.0d,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (8)."),txt_1,new RTS_TXT("        1.000000000000000&+000"));
        ;
        // JavaLine 460 <== SourceLine 342
        lon_1=10.0d;
        ;
        // JavaLine 463 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (9)."),txt_1,new RTS_TXT("        1.000000000000000&+001"));
        ;
        RTS_TXT.putreal(txt_1,1.797693134862315E308d,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (10)."),txt_1,new RTS_TXT("        1.797693134862315&+308"));
        ;
        // JavaLine 472 <== SourceLine 355
        lon_1=1.234567891234568E18d;
        ;
        // JavaLine 475 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (11)."),txt_1,new RTS_TXT("        1.234567891234568&+018"));
        ;
        // JavaLine 480 <== SourceLine 359
        lon_1=-1.0E22d;
        ;
        // JavaLine 483 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (12)."),txt_1,new RTS_TXT("       -1.000000000000000&+022"));
        ;
        // JavaLine 488 <== SourceLine 363
        lon_1=7.0E-11d;
        ;
        // JavaLine 491 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (13)."),txt_1,new RTS_TXT("        7.000000000000000&-011"));
        ;
        // JavaLine 496 <== SourceLine 367
        lon_1=1.1111111111111112E-5d;
        ;
        // JavaLine 499 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (14)."),txt_1,new RTS_TXT("        1.111111111111111&-005"));
        ;
        // JavaLine 504 <== SourceLine 371
        lon_1=1.234567891111111E32d;
        ;
        // JavaLine 507 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (15)."),txt_1,new RTS_TXT("        1.234567891111111&+032"));
        ;
        // JavaLine 512 <== SourceLine 380
        lon_1=-1.797693134862315E308d;
        ;
        // JavaLine 515 <== SourceLine 30
        RTS_TXT.putreal(txt_1,lon_1,longprecision_1);
        ;
        new simtst04_SimulaTestBegin_test((_CUR),new RTS_TXT("long real operation (16)."),txt_1,new RTS_TXT("       -1.797693134862315&+308"));
        ;
        // ENDOF SimulaTest INNER PART
        ;
        // JavaLine 522 <== SourceLine 24
        if(_VALUE(noMessage)) {
            ;
        } else {
            {
                // JavaLine 527 <== SourceLine 25
                if(_VALUE(found_error)) {
                    {
                        RTS_BASICIO.sysout().outtext(CONC(CONC(new RTS_TXT("--- "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" ERROR(S) FOUND IN TEST")));
                        ;
                        RTS_BASICIO.sysout().outint(p_n,4);
                        ;
                        RTS_BASICIO.sysout().outtext(new RTS_TXT("  "));
                        ;
                        RTS_BASICIO.sysout().outtext(p_title);
                        ;
                        RTS_ENVIRONMENT.error(CONC(CONC(new RTS_TXT("Test sample has "),RTS_ENVIRONMENT.edit(nFailed)),new RTS_TXT(" error(s)")));
                        ;
                    }
                } else {
                    // JavaLine 542 <== SourceLine 28
                    {
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
                // JavaLine 555 <== SourceLine 32
                if(_VALUE(false)) {
                    {
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
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst04.sim","PrefixedBlock SimulaTestBegin",1,30,10,32,13,35,15,36,17,37,19,38,21,40,32,16,50,22,54,58,57,60,60,63,63,30,72,75,75,30,80,79,83,30,92,86,95,30,108,100,111,30,116,106,119,30,124,110,127,30,132,119,135,30,140,129,143,30,148,134,151,30,156,138,159,30,164,142,167,30,172,151,175,30,180,157,183,30,188,161,191,30,196,170,199,30,204,176,207,30,216,183,219,30,228,190,231,30,240,197,243,30,248,201,251,30,256,205,259,30,268,216,271,30,276,220,279,30,284,224,287,30,292,228,295,30,300,232,303,30,308,236,311,30,316,240,319,30,324,244,327,30,332,248,335,30,340,252,343,30,348,262,351,30,356,266,359,30,364,270,367,30,372,274,375,30,380,278,383,30,388,287,391,30,396,291,399,30,404,298,407,30,412,308,415,30,424,316,427,30,432,325,435,30,440,331,443,30,448,335,451,30,460,342,463,30,472,355,475,30,480,359,483,30,488,363,491,30,496,367,499,30,504,371,507,30,512,380,515,30,522,24,527,25,542,28,555,32,572,385);
} // End of Class
