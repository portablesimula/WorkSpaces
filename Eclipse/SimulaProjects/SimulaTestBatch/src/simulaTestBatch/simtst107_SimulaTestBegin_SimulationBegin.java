// JavaLine 1 <== SourceLine 11
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin extends RTS_Simulation {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=2, firstLine=11, lastLine=560, hasLocalClasses=true, System=true, detachUsed=false
public boolean isQPSystemBlock() { return(true); }
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 14
    public boolean failed_2=false;
    public RTS_TEXT_ARRAY ut=null;
    // JavaLine 15 <== SourceLine 16
    public RTS_TEXT_ARRAY answer=null;
    public int utno_2=0;
    // JavaLine 18 <== SourceLine 85
    public RTS_REF_ARRAY<simtst107_SimulaTestBegin_SimulationBegin_p> pa=null;
    // JavaLine 20 <== SourceLine 87
    public RTS_BOOLEAN_ARRAY active=null;
    // JavaLine 22 <== SourceLine 88
    public RTS_BOOLEAN_ARRAY passive=null;
    public RTS_BOOLEAN_ARRAY terminatd=null;
    public int i_2=0;
    // JavaLine 26 <== SourceLine 89
    public int testno_2=0;
    public RTS_TXT activationtimes_2=null;
    // JavaLine 29 <== SourceLine 90
    public RTS_TXT delaytimes_2=null;
    public RTS_TXT actime_2=null;
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
        // JavaLine 37 <== SourceLine 14
        ut=new RTS_TEXT_ARRAY(new RTS_BOUNDS(1,250));
        // JavaLine 39 <== SourceLine 16
        answer=new RTS_TEXT_ARRAY(new RTS_BOUNDS(1,250));
        // JavaLine 41 <== SourceLine 85
        pa=new RTS_REF_ARRAY<simtst107_SimulaTestBegin_SimulationBegin_p>(new RTS_BOUNDS(1,10));
        // JavaLine 43 <== SourceLine 87
        active=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
        // JavaLine 45 <== SourceLine 88
        passive=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
        terminatd=new RTS_BOOLEAN_ARRAY(new RTS_BOUNDS(1,10));
    }
    // Class Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin _STM() {
        // BEGIN Simset INNER PART
        // BEGIN Simulation INNER PART
        // BEGIN SimulationBegin INNER PART
        // ENDOF SimulationBegin INNER PART
        // JavaLine 56 <== SourceLine 94
        answer.putELEMENT(answer.index(1),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p(10) activated at time =    0.000"))));
        ;
        // JavaLine 59 <== SourceLine 96
        answer.putELEMENT(answer.index(2),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1,10): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s    a"))));
        ;
        // JavaLine 62 <== SourceLine 98
        answer.putELEMENT(answer.index(3),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     p     p     p     p     p     a"))));
        ;
        // JavaLine 65 <== SourceLine 100
        answer.putELEMENT(answer.index(4),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  idle  idle  idle  idle  idle  0.00"))));
        ;
        // JavaLine 68 <== SourceLine 102
        answer.putELEMENT(answer.index(5),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 9) activated at time =    0.000"))));
        ;
        // JavaLine 71 <== SourceLine 104
        answer.putELEMENT(answer.index(6),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 9): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s    a     t"))));
        ;
        // JavaLine 74 <== SourceLine 106
        answer.putELEMENT(answer.index(7),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     p     p     p     p     a     t"))));
        ;
        // JavaLine 77 <== SourceLine 108
        answer.putELEMENT(answer.index(8),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  idle  idle  idle  idle  0.00  idle"))));
        ;
        // JavaLine 80 <== SourceLine 110
        answer.putELEMENT(answer.index(9),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 8) activated at time =    0.000"))));
        ;
        // JavaLine 83 <== SourceLine 112
        answer.putELEMENT(answer.index(10),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 8): p/s   p/s   p/s   p/s   p/s   p/s   p/s    a     t     t"))));
        ;
        // JavaLine 86 <== SourceLine 114
        answer.putELEMENT(answer.index(11),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     p     p     p     a     t     t"))));
        ;
        // JavaLine 89 <== SourceLine 116
        answer.putELEMENT(answer.index(12),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  idle  idle  idle  0.00  idle  idle"))));
        ;
        // JavaLine 92 <== SourceLine 118
        answer.putELEMENT(answer.index(13),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 7) activated at time =    0.000"))));
        ;
        // JavaLine 95 <== SourceLine 120
        answer.putELEMENT(answer.index(14),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 7): p/s   p/s   p/s   p/s   p/s   p/s    a     t     t     t"))));
        ;
        // JavaLine 98 <== SourceLine 122
        answer.putELEMENT(answer.index(15),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     p     p     a     t     t     t"))));
        ;
        // JavaLine 101 <== SourceLine 124
        answer.putELEMENT(answer.index(16),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  idle  idle  0.00  idle  idle  idle"))));
        ;
        // JavaLine 104 <== SourceLine 126
        answer.putELEMENT(answer.index(17),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 6) activated at time =    0.000"))));
        ;
        // JavaLine 107 <== SourceLine 128
        answer.putELEMENT(answer.index(18),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 6): p/s   p/s   p/s   p/s   p/s    a     t     t     t     t"))));
        ;
        // JavaLine 110 <== SourceLine 130
        answer.putELEMENT(answer.index(19),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     p     a     t     t     t     t"))));
        ;
        // JavaLine 113 <== SourceLine 132
        answer.putELEMENT(answer.index(20),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  idle  0.00  idle  idle  idle  idle"))));
        ;
        // JavaLine 116 <== SourceLine 134
        answer.putELEMENT(answer.index(21),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 5) activated at time =    0.000"))));
        ;
        // JavaLine 119 <== SourceLine 136
        answer.putELEMENT(answer.index(22),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 5): p/s   p/s   p/s   p/s    a     t     t     t     t     t"))));
        ;
        // JavaLine 122 <== SourceLine 138
        answer.putELEMENT(answer.index(23),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     p     a     t     t     t     t     t"))));
        ;
        // JavaLine 125 <== SourceLine 140
        answer.putELEMENT(answer.index(24),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  idle  0.00  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 128 <== SourceLine 142
        answer.putELEMENT(answer.index(25),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 4) activated at time =    0.000"))));
        ;
        // JavaLine 131 <== SourceLine 144
        answer.putELEMENT(answer.index(26),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 4): p/s   p/s   p/s    a     t     t     t     t     t     t"))));
        ;
        // JavaLine 134 <== SourceLine 146
        answer.putELEMENT(answer.index(27),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     p     a     t     t     t     t     t     t"))));
        ;
        // JavaLine 137 <== SourceLine 148
        answer.putELEMENT(answer.index(28),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  idle  0.00  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 140 <== SourceLine 150
        answer.putELEMENT(answer.index(29),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 3) activated at time =    0.000"))));
        ;
        // JavaLine 143 <== SourceLine 152
        answer.putELEMENT(answer.index(30),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 3): p/s   p/s    a     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 146 <== SourceLine 154
        answer.putELEMENT(answer.index(31),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     p     a     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 149 <== SourceLine 156
        answer.putELEMENT(answer.index(32),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  idle  0.00  idle  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 152 <== SourceLine 158
        answer.putELEMENT(answer.index(33),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 2) activated at time =    0.000"))));
        ;
        // JavaLine 155 <== SourceLine 160
        answer.putELEMENT(answer.index(34),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 2): p/s    a     t     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 158 <== SourceLine 162
        answer.putELEMENT(answer.index(35),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          p     a     t     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 161 <== SourceLine 164
        answer.putELEMENT(answer.index(36),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     idle  0.00  idle  idle  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 164 <== SourceLine 166
        answer.putELEMENT(answer.index(37),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 1) activated at time =    0.000"))));
        ;
        // JavaLine 167 <== SourceLine 168
        answer.putELEMENT(answer.index(38),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(1, 1):  a     t     t     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 170 <== SourceLine 170
        answer.putELEMENT(answer.index(39),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          a     t     t     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 173 <== SourceLine 172
        answer.putELEMENT(answer.index(40),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     0.00  idle  idle  idle  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 176 <== SourceLine 174
        answer.putELEMENT(answer.index(41),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 0): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 179 <== SourceLine 176
        answer.putELEMENT(answer.index(42),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          s     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 182 <== SourceLine 178
        answer.putELEMENT(answer.index(43),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  0.00 :     1.53  7.60  2.00  4.19  2.01  1.84  2.00  8.00  9.25  5.40"))));
        ;
        // JavaLine 185 <== SourceLine 180
        answer.putELEMENT(answer.index(44),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 1) activated at time =    1.530"))));
        ;
        // JavaLine 188 <== SourceLine 182
        answer.putELEMENT(answer.index(45),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 1):  a    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 191 <== SourceLine 184
        answer.putELEMENT(answer.index(46),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          a     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 194 <== SourceLine 186
        answer.putELEMENT(answer.index(47),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  1.53 :     1.53  7.60  2.00  4.19  2.01  1.84  2.00  8.00  9.25  5.40"))));
        ;
        // JavaLine 197 <== SourceLine 188
        answer.putELEMENT(answer.index(48),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 6) activated at time =    1.840"))));
        ;
        // JavaLine 200 <== SourceLine 190
        answer.putELEMENT(answer.index(49),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 6):  t    p/s   p/s   p/s   p/s    a    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 203 <== SourceLine 192
        answer.putELEMENT(answer.index(50),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     s     s     s     a     s     s     s     s"))));
        ;
        // JavaLine 206 <== SourceLine 194
        answer.putELEMENT(answer.index(51),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  1.84 :     idle  7.60  2.00  4.19  2.01  1.84  2.00  8.00  9.25  5.40"))));
        ;
        // JavaLine 209 <== SourceLine 196
        answer.putELEMENT(answer.index(52),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 3) activated at time =    2.000"))));
        ;
        // JavaLine 212 <== SourceLine 198
        answer.putELEMENT(answer.index(53),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 3):  t    p/s    a    p/s   p/s    t    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 215 <== SourceLine 200
        answer.putELEMENT(answer.index(54),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     a     s     s     t     s     s     s     s"))));
        ;
        // JavaLine 218 <== SourceLine 202
        answer.putELEMENT(answer.index(55),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  2.00 :     idle  7.60  2.00  4.19  2.01  idle  2.00  8.00  9.25  5.40"))));
        ;
        // JavaLine 221 <== SourceLine 204
        answer.putELEMENT(answer.index(56),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 7) activated at time =    2.000"))));
        ;
        // JavaLine 224 <== SourceLine 206
        answer.putELEMENT(answer.index(57),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 7):  t    p/s    t    p/s   p/s    t     a    p/s   p/s   p/s"))));
        ;
        // JavaLine 227 <== SourceLine 208
        answer.putELEMENT(answer.index(58),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     s     s     t     a     s     s     s"))));
        ;
        // JavaLine 230 <== SourceLine 210
        answer.putELEMENT(answer.index(59),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  2.00 :     idle  7.60  idle  4.19  2.01  idle  2.00  8.00  9.25  5.40"))));
        ;
        // JavaLine 233 <== SourceLine 212
        answer.putELEMENT(answer.index(60),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 5) activated at time =    2.010"))));
        ;
        // JavaLine 236 <== SourceLine 214
        answer.putELEMENT(answer.index(61),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 5):  t    p/s    t    p/s    a     t     t    p/s   p/s   p/s"))));
        ;
        // JavaLine 239 <== SourceLine 216
        answer.putELEMENT(answer.index(62),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     s     a     t     t     s     s     s"))));
        ;
        // JavaLine 242 <== SourceLine 218
        answer.putELEMENT(answer.index(63),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  2.01 :     idle  7.60  idle  4.19  2.01  idle  idle  8.00  9.25  5.40"))));
        ;
        // JavaLine 245 <== SourceLine 220
        answer.putELEMENT(answer.index(64),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 4) activated at time =    4.190"))));
        ;
        // JavaLine 248 <== SourceLine 222
        answer.putELEMENT(answer.index(65),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 4):  t    p/s    t     a     t     t     t    p/s   p/s   p/s"))));
        ;
        // JavaLine 251 <== SourceLine 224
        answer.putELEMENT(answer.index(66),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     a     t     t     t     s     s     s"))));
        ;
        // JavaLine 254 <== SourceLine 226
        answer.putELEMENT(answer.index(67),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  4.19 :     idle  7.60  idle  4.19  idle  idle  idle  8.00  9.25  5.40"))));
        ;
        // JavaLine 257 <== SourceLine 228
        answer.putELEMENT(answer.index(68),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p(10) activated at time =    5.400"))));
        ;
        // JavaLine 260 <== SourceLine 230
        answer.putELEMENT(answer.index(69),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2,10):  t    p/s    t     t     t     t     t    p/s   p/s    a"))));
        ;
        // JavaLine 263 <== SourceLine 232
        answer.putELEMENT(answer.index(70),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     t     t     t     t     s     s     a"))));
        ;
        // JavaLine 266 <== SourceLine 234
        answer.putELEMENT(answer.index(71),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  5.40 :     idle  7.60  idle  idle  idle  idle  idle  8.00  9.25  5.40"))));
        ;
        // JavaLine 269 <== SourceLine 236
        answer.putELEMENT(answer.index(72),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 2) activated at time =    7.600"))));
        ;
        // JavaLine 272 <== SourceLine 238
        answer.putELEMENT(answer.index(73),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 2):  t     a     t     t     t     t     t    p/s   p/s    t"))));
        ;
        // JavaLine 275 <== SourceLine 240
        answer.putELEMENT(answer.index(74),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     a     t     t     t     t     t     s     s     t"))));
        ;
        // JavaLine 278 <== SourceLine 242
        answer.putELEMENT(answer.index(75),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  7.60 :     idle  7.60  idle  idle  idle  idle  idle  8.00  9.25  idle"))));
        ;
        // JavaLine 281 <== SourceLine 244
        answer.putELEMENT(answer.index(76),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 8) activated at time =    8.000"))));
        ;
        // JavaLine 284 <== SourceLine 246
        answer.putELEMENT(answer.index(77),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 8):  t     t     t     t     t     t     t     a    p/s    t"))));
        ;
        // JavaLine 287 <== SourceLine 248
        answer.putELEMENT(answer.index(78),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     t     t     t     t     t     a     s     t"))));
        ;
        // JavaLine 290 <== SourceLine 250
        answer.putELEMENT(answer.index(79),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  8.00 :     idle  idle  idle  idle  idle  idle  idle  8.00  9.25  idle"))));
        ;
        // JavaLine 293 <== SourceLine 252
        answer.putELEMENT(answer.index(80),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 9) activated at time =    9.250"))));
        ;
        // JavaLine 296 <== SourceLine 254
        answer.putELEMENT(answer.index(81),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(2, 9):  t     t     t     t     t     t     t     t     a     t"))));
        ;
        // JavaLine 299 <== SourceLine 256
        answer.putELEMENT(answer.index(82),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     t     t     t     t     t     t     a     t"))));
        ;
        // JavaLine 302 <== SourceLine 258
        answer.putELEMENT(answer.index(83),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("  9.25 :     idle  idle  idle  idle  idle  idle  idle  idle  9.25  idle"))));
        ;
        // JavaLine 305 <== SourceLine 260
        answer.putELEMENT(answer.index(84),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 0): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 308 <== SourceLine 262
        answer.putELEMENT(answer.index(85),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          s     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 311 <== SourceLine 264
        answer.putELEMENT(answer.index(86),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 20.00 :    21.53 27.60 22.00 24.19 22.01 21.84 22.00 28.00 29.25 25.40"))));
        ;
        // JavaLine 314 <== SourceLine 266
        answer.putELEMENT(answer.index(87),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 1) activated at time =   21.530"))));
        ;
        // JavaLine 317 <== SourceLine 268
        answer.putELEMENT(answer.index(88),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 1):  a    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 320 <== SourceLine 270
        answer.putELEMENT(answer.index(89),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          a     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 323 <== SourceLine 272
        answer.putELEMENT(answer.index(90),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 21.53 :    21.53 27.60 22.00 24.19 22.01 21.84 22.00 28.00 29.25 25.40"))));
        ;
        // JavaLine 326 <== SourceLine 274
        answer.putELEMENT(answer.index(91),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 6) activated at time =   21.840"))));
        ;
        // JavaLine 329 <== SourceLine 276
        answer.putELEMENT(answer.index(92),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 6):  t    p/s   p/s   p/s   p/s    a    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 332 <== SourceLine 278
        answer.putELEMENT(answer.index(93),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     s     s     s     a     s     s     s     s"))));
        ;
        // JavaLine 335 <== SourceLine 280
        answer.putELEMENT(answer.index(94),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 21.84 :     idle 27.60 22.00 24.19 22.01 21.84 22.00 28.00 29.25 25.40"))));
        ;
        // JavaLine 338 <== SourceLine 282
        answer.putELEMENT(answer.index(95),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 7) activated at time =   22.000"))));
        ;
        // JavaLine 341 <== SourceLine 284
        answer.putELEMENT(answer.index(96),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 7):  t    p/s   p/s   p/s   p/s    t     a    p/s   p/s   p/s"))));
        ;
        // JavaLine 344 <== SourceLine 286
        answer.putELEMENT(answer.index(97),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     s     s     s     t     a     s     s     s"))));
        ;
        // JavaLine 347 <== SourceLine 288
        answer.putELEMENT(answer.index(98),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 22.00 :     idle 27.60 22.00 24.19 22.01  idle 22.00 28.00 29.25 25.40"))));
        ;
        // JavaLine 350 <== SourceLine 290
        answer.putELEMENT(answer.index(99),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 3) activated at time =   22.000"))));
        ;
        // JavaLine 353 <== SourceLine 292
        answer.putELEMENT(answer.index(100),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 3):  t    p/s    a    p/s   p/s    t     t    p/s   p/s   p/s"))));
        ;
        // JavaLine 356 <== SourceLine 294
        answer.putELEMENT(answer.index(101),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     a     s     s     t     t     s     s     s"))));
        ;
        // JavaLine 359 <== SourceLine 296
        answer.putELEMENT(answer.index(102),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 22.00 :     idle 27.60 22.00 24.19 22.01  idle  idle 28.00 29.25 25.40"))));
        ;
        // JavaLine 362 <== SourceLine 298
        answer.putELEMENT(answer.index(103),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 5) activated at time =   22.010"))));
        ;
        // JavaLine 365 <== SourceLine 300
        answer.putELEMENT(answer.index(104),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 5):  t    p/s    t    p/s    a     t     t    p/s   p/s   p/s"))));
        ;
        // JavaLine 368 <== SourceLine 302
        answer.putELEMENT(answer.index(105),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     s     a     t     t     s     s     s"))));
        ;
        // JavaLine 371 <== SourceLine 304
        answer.putELEMENT(answer.index(106),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 22.01 :     idle 27.60  idle 24.19 22.01  idle  idle 28.00 29.25 25.40"))));
        ;
        // JavaLine 374 <== SourceLine 306
        answer.putELEMENT(answer.index(107),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 4) activated at time =   24.190"))));
        ;
        // JavaLine 377 <== SourceLine 308
        answer.putELEMENT(answer.index(108),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 4):  t    p/s    t     a     t     t     t    p/s   p/s   p/s"))));
        ;
        // JavaLine 380 <== SourceLine 310
        answer.putELEMENT(answer.index(109),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     a     t     t     t     s     s     s"))));
        ;
        // JavaLine 383 <== SourceLine 312
        answer.putELEMENT(answer.index(110),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 24.19 :     idle 27.60  idle 24.19  idle  idle  idle 28.00 29.25 25.40"))));
        ;
        // JavaLine 386 <== SourceLine 314
        answer.putELEMENT(answer.index(111),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p(10) activated at time =   25.400"))));
        ;
        // JavaLine 389 <== SourceLine 316
        answer.putELEMENT(answer.index(112),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3,10):  t    p/s    t     t     t     t     t    p/s   p/s    a"))));
        ;
        // JavaLine 392 <== SourceLine 318
        answer.putELEMENT(answer.index(113),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     s     t     t     t     t     t     s     s     a"))));
        ;
        // JavaLine 395 <== SourceLine 320
        answer.putELEMENT(answer.index(114),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 25.40 :     idle 27.60  idle  idle  idle  idle  idle 28.00 29.25 25.40"))));
        ;
        // JavaLine 398 <== SourceLine 322
        answer.putELEMENT(answer.index(115),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 2) activated at time =   27.600"))));
        ;
        // JavaLine 401 <== SourceLine 324
        answer.putELEMENT(answer.index(116),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 2):  t     a     t     t     t     t     t    p/s   p/s    t"))));
        ;
        // JavaLine 404 <== SourceLine 326
        answer.putELEMENT(answer.index(117),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     a     t     t     t     t     t     s     s     t"))));
        ;
        // JavaLine 407 <== SourceLine 328
        answer.putELEMENT(answer.index(118),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 27.60 :     idle 27.60  idle  idle  idle  idle  idle 28.00 29.25  idle"))));
        ;
        // JavaLine 410 <== SourceLine 330
        answer.putELEMENT(answer.index(119),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 8) activated at time =   28.000"))));
        ;
        // JavaLine 413 <== SourceLine 332
        answer.putELEMENT(answer.index(120),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 8):  t     t     t     t     t     t     t     a    p/s    t"))));
        ;
        // JavaLine 416 <== SourceLine 334
        answer.putELEMENT(answer.index(121),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     t     t     t     t     t     a     s     t"))));
        ;
        // JavaLine 419 <== SourceLine 336
        answer.putELEMENT(answer.index(122),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 28.00 :     idle  idle  idle  idle  idle  idle  idle 28.00 29.25  idle"))));
        ;
        // JavaLine 422 <== SourceLine 338
        answer.putELEMENT(answer.index(123),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 9) activated at time =   29.250"))));
        ;
        // JavaLine 425 <== SourceLine 340
        answer.putELEMENT(answer.index(124),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(3, 9):  t     t     t     t     t     t     t     t     a     t"))));
        ;
        // JavaLine 428 <== SourceLine 342
        answer.putELEMENT(answer.index(125),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     t     t     t     t     t     t     a     t"))));
        ;
        // JavaLine 431 <== SourceLine 344
        answer.putELEMENT(answer.index(126),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 29.25 :     idle  idle  idle  idle  idle  idle  idle  idle 29.25  idle"))));
        ;
        // JavaLine 434 <== SourceLine 346
        answer.putELEMENT(answer.index(127),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 0): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 437 <== SourceLine 348
        answer.putELEMENT(answer.index(128),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          s     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 440 <== SourceLine 350
        answer.putELEMENT(answer.index(129),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 40.00 :    41.53 41.53 47.60 47.60 42.00 42.00 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 443 <== SourceLine 352
        answer.putELEMENT(answer.index(130),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 2) activated at time =   41.530"))));
        ;
        // JavaLine 446 <== SourceLine 354
        answer.putELEMENT(answer.index(131),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 2): p/s    a    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 449 <== SourceLine 356
        answer.putELEMENT(answer.index(132),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          s     a     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 452 <== SourceLine 358
        answer.putELEMENT(answer.index(133),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 41.53 :    41.53 41.53 47.60 47.60 42.00 42.00 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 455 <== SourceLine 360
        answer.putELEMENT(answer.index(134),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 1) activated at time =   41.530"))));
        ;
        // JavaLine 458 <== SourceLine 362
        answer.putELEMENT(answer.index(135),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 1):  a     t    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 461 <== SourceLine 364
        answer.putELEMENT(answer.index(136),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          a     t     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 464 <== SourceLine 366
        answer.putELEMENT(answer.index(137),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 41.53 :    41.53  idle 47.60 47.60 42.00 42.00 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 467 <== SourceLine 368
        answer.putELEMENT(answer.index(138),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 6) activated at time =   42.000"))));
        ;
        // JavaLine 470 <== SourceLine 370
        answer.putELEMENT(answer.index(139),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 6):  t     t    p/s   p/s   p/s    a    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 473 <== SourceLine 372
        answer.putELEMENT(answer.index(140),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     s     a     s     s     s     s"))));
        ;
        // JavaLine 476 <== SourceLine 374
        answer.putELEMENT(answer.index(141),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 42.00 :     idle  idle 47.60 47.60 42.00 42.00 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 479 <== SourceLine 376
        answer.putELEMENT(answer.index(142),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 5) activated at time =   42.000"))));
        ;
        // JavaLine 482 <== SourceLine 378
        answer.putELEMENT(answer.index(143),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 5):  t     t    p/s   p/s    a     t    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 485 <== SourceLine 380
        answer.putELEMENT(answer.index(144),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     a     t     s     s     s     s"))));
        ;
        // JavaLine 488 <== SourceLine 382
        answer.putELEMENT(answer.index(145),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 42.00 :     idle  idle 47.60 47.60 42.00  idle 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 491 <== SourceLine 384
        answer.putELEMENT(answer.index(146),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p(10) activated at time =   42.010"))));
        ;
        // JavaLine 494 <== SourceLine 386
        answer.putELEMENT(answer.index(147),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4,10):  t     t    p/s   p/s    t     t    p/s   p/s   p/s    a"))));
        ;
        // JavaLine 497 <== SourceLine 388
        answer.putELEMENT(answer.index(148),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     s     s     s     a"))));
        ;
        // JavaLine 500 <== SourceLine 390
        answer.putELEMENT(answer.index(149),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 42.01 :     idle  idle 47.60 47.60  idle  idle 44.19 44.19 42.01 42.01"))));
        ;
        // JavaLine 503 <== SourceLine 392
        answer.putELEMENT(answer.index(150),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 9) activated at time =   42.010"))));
        ;
        // JavaLine 506 <== SourceLine 394
        answer.putELEMENT(answer.index(151),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 9):  t     t    p/s   p/s    t     t    p/s   p/s    a     t"))));
        ;
        // JavaLine 509 <== SourceLine 396
        answer.putELEMENT(answer.index(152),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     s     s     a     t"))));
        ;
        // JavaLine 512 <== SourceLine 398
        answer.putELEMENT(answer.index(153),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 42.01 :     idle  idle 47.60 47.60  idle  idle 44.19 44.19 42.01  idle"))));
        ;
        // JavaLine 515 <== SourceLine 400
        answer.putELEMENT(answer.index(154),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 8) activated at time =   44.190"))));
        ;
        // JavaLine 518 <== SourceLine 402
        answer.putELEMENT(answer.index(155),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 8):  t     t    p/s   p/s    t     t    p/s    a     t     t"))));
        ;
        // JavaLine 521 <== SourceLine 404
        answer.putELEMENT(answer.index(156),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     s     a     t     t"))));
        ;
        // JavaLine 524 <== SourceLine 406
        answer.putELEMENT(answer.index(157),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 44.19 :     idle  idle 47.60 47.60  idle  idle 44.19 44.19  idle  idle"))));
        ;
        // JavaLine 527 <== SourceLine 408
        answer.putELEMENT(answer.index(158),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 7) activated at time =   44.190"))));
        ;
        // JavaLine 530 <== SourceLine 410
        answer.putELEMENT(answer.index(159),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 7):  t     t    p/s   p/s    t     t     a     t     t     t"))));
        ;
        // JavaLine 533 <== SourceLine 412
        answer.putELEMENT(answer.index(160),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     a     t     t     t"))));
        ;
        // JavaLine 536 <== SourceLine 414
        answer.putELEMENT(answer.index(161),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 44.19 :     idle  idle 47.60 47.60  idle  idle 44.19  idle  idle  idle"))));
        ;
        // JavaLine 539 <== SourceLine 416
        answer.putELEMENT(answer.index(162),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 4) activated at time =   47.600"))));
        ;
        // JavaLine 542 <== SourceLine 418
        answer.putELEMENT(answer.index(163),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 4):  t     t    p/s    a     t     t     t     t     t     t"))));
        ;
        // JavaLine 545 <== SourceLine 420
        answer.putELEMENT(answer.index(164),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     a     t     t     t     t     t     t"))));
        ;
        // JavaLine 548 <== SourceLine 422
        answer.putELEMENT(answer.index(165),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 47.60 :     idle  idle 47.60 47.60  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 551 <== SourceLine 424
        answer.putELEMENT(answer.index(166),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 3) activated at time =   47.600"))));
        ;
        // JavaLine 554 <== SourceLine 426
        answer.putELEMENT(answer.index(167),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(4, 3):  t     t     a     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 557 <== SourceLine 428
        answer.putELEMENT(answer.index(168),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     a     t     t     t     t     t     t     t"))));
        ;
        // JavaLine 560 <== SourceLine 430
        answer.putELEMENT(answer.index(169),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 47.60 :     idle  idle 47.60  idle  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 563 <== SourceLine 432
        answer.putELEMENT(answer.index(170),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 0): p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 566 <== SourceLine 434
        answer.putELEMENT(answer.index(171),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          s     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 569 <== SourceLine 436
        answer.putELEMENT(answer.index(172),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 60.00 :    61.53 61.53 67.60 67.60 62.00 62.00 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 572 <== SourceLine 438
        answer.putELEMENT(answer.index(173),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 1) activated at time =   61.530"))));
        ;
        // JavaLine 575 <== SourceLine 440
        answer.putELEMENT(answer.index(174),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 1):  a    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 578 <== SourceLine 442
        answer.putELEMENT(answer.index(175),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          a     s     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 581 <== SourceLine 444
        answer.putELEMENT(answer.index(176),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 61.53 :    61.53 61.53 67.60 67.60 62.00 62.00 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 584 <== SourceLine 446
        answer.putELEMENT(answer.index(177),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 2) activated at time =   61.530"))));
        ;
        // JavaLine 587 <== SourceLine 448
        answer.putELEMENT(answer.index(178),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 2):  t     a    p/s   p/s   p/s   p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 590 <== SourceLine 450
        answer.putELEMENT(answer.index(179),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     a     s     s     s     s     s     s     s     s"))));
        ;
        // JavaLine 593 <== SourceLine 452
        answer.putELEMENT(answer.index(180),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 61.53 :     idle 61.53 67.60 67.60 62.00 62.00 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 596 <== SourceLine 454
        answer.putELEMENT(answer.index(181),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 5) activated at time =   62.000"))));
        ;
        // JavaLine 599 <== SourceLine 456
        answer.putELEMENT(answer.index(182),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 5):  t     t    p/s   p/s    a    p/s   p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 602 <== SourceLine 458
        answer.putELEMENT(answer.index(183),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     a     s     s     s     s     s"))));
        ;
        // JavaLine 605 <== SourceLine 460
        answer.putELEMENT(answer.index(184),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 62.00 :     idle  idle 67.60 67.60 62.00 62.00 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 608 <== SourceLine 462
        answer.putELEMENT(answer.index(185),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 6) activated at time =   62.000"))));
        ;
        // JavaLine 611 <== SourceLine 464
        answer.putELEMENT(answer.index(186),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 6):  t     t    p/s   p/s    t     a    p/s   p/s   p/s   p/s"))));
        ;
        // JavaLine 614 <== SourceLine 466
        answer.putELEMENT(answer.index(187),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     a     s     s     s     s"))));
        ;
        // JavaLine 617 <== SourceLine 468
        answer.putELEMENT(answer.index(188),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 62.00 :     idle  idle 67.60 67.60  idle 62.00 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 620 <== SourceLine 470
        answer.putELEMENT(answer.index(189),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 9) activated at time =   62.010"))));
        ;
        // JavaLine 623 <== SourceLine 472
        answer.putELEMENT(answer.index(190),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 9):  t     t    p/s   p/s    t     t    p/s   p/s    a    p/s"))));
        ;
        // JavaLine 626 <== SourceLine 474
        answer.putELEMENT(answer.index(191),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     s     s     a     s"))));
        ;
        // JavaLine 629 <== SourceLine 476
        answer.putELEMENT(answer.index(192),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 62.01 :     idle  idle 67.60 67.60  idle  idle 64.19 64.19 62.01 62.01"))));
        ;
        // JavaLine 632 <== SourceLine 478
        answer.putELEMENT(answer.index(193),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p(10) activated at time =   62.010"))));
        ;
        // JavaLine 635 <== SourceLine 480
        answer.putELEMENT(answer.index(194),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5,10):  t     t    p/s   p/s    t     t    p/s   p/s    t     a"))));
        ;
        // JavaLine 638 <== SourceLine 482
        answer.putELEMENT(answer.index(195),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     s     s     t     a"))));
        ;
        // JavaLine 641 <== SourceLine 484
        answer.putELEMENT(answer.index(196),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 62.01 :     idle  idle 67.60 67.60  idle  idle 64.19 64.19  idle 62.01"))));
        ;
        // JavaLine 644 <== SourceLine 486
        answer.putELEMENT(answer.index(197),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 7) activated at time =   64.190"))));
        ;
        // JavaLine 647 <== SourceLine 488
        answer.putELEMENT(answer.index(198),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 7):  t     t    p/s   p/s    t     t     a    p/s    t     t"))));
        ;
        // JavaLine 650 <== SourceLine 490
        answer.putELEMENT(answer.index(199),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     a     s     t     t"))));
        ;
        // JavaLine 653 <== SourceLine 492
        answer.putELEMENT(answer.index(200),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 64.19 :     idle  idle 67.60 67.60  idle  idle 64.19 64.19  idle  idle"))));
        ;
        // JavaLine 656 <== SourceLine 494
        answer.putELEMENT(answer.index(201),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 8) activated at time =   64.190"))));
        ;
        // JavaLine 659 <== SourceLine 496
        answer.putELEMENT(answer.index(202),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 8):  t     t    p/s   p/s    t     t     t     a     t     t"))));
        ;
        // JavaLine 662 <== SourceLine 498
        answer.putELEMENT(answer.index(203),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     s     s     t     t     t     a     t     t"))));
        ;
        // JavaLine 665 <== SourceLine 500
        answer.putELEMENT(answer.index(204),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 64.19 :     idle  idle 67.60 67.60  idle  idle  idle 64.19  idle  idle"))));
        ;
        // JavaLine 668 <== SourceLine 502
        answer.putELEMENT(answer.index(205),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 3) activated at time =   67.600"))));
        ;
        // JavaLine 671 <== SourceLine 504
        answer.putELEMENT(answer.index(206),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 3):  t     t     a    p/s    t     t     t     t     t     t"))));
        ;
        // JavaLine 674 <== SourceLine 506
        answer.putELEMENT(answer.index(207),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     a     s     t     t     t     t     t     t"))));
        ;
        // JavaLine 677 <== SourceLine 508
        answer.putELEMENT(answer.index(208),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 67.60 :     idle  idle 67.60 67.60  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 680 <== SourceLine 510
        answer.putELEMENT(answer.index(209),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("p( 4) activated at time =   67.600"))));
        ;
        // JavaLine 683 <== SourceLine 512
        answer.putELEMENT(answer.index(210),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("State(5, 4):  t     t     t     a     t     t     t     t     t     t"))));
        ;
        // JavaLine 686 <== SourceLine 514
        answer.putELEMENT(answer.index(211),RTS_ENVIRONMENT.copy(copy(new RTS_TXT("SQS:          t     t     t     a     t     t     t     t     t     t"))));
        ;
        // JavaLine 689 <== SourceLine 516
        answer.putELEMENT(answer.index(212),RTS_ENVIRONMENT.copy(copy(new RTS_TXT(" 67.60 :     idle  idle  idle 67.60  idle  idle  idle  idle  idle  idle"))));
        ;
        // JavaLine 692 <== SourceLine 519
        activationtimes_2=RTS_ENVIRONMENT.copy(copy(new RTS_TXT("1.53 7.6 2 4.19 2.01 1.84 2 8.0 9.25 5.4")));
        ;
        // JavaLine 695 <== SourceLine 520
        delaytimes_2=RTS_ENVIRONMENT.copy(copy(new RTS_TXT(".01 .02 .05 .03 .08 .09 .04 .06 .06 .07")));
        ;
        // JavaLine 698 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_createobjects((_CUR));
        ;
        for(i_2=10;i_2>=1;i_2--) {
            // JavaLine 702 <== SourceLine 523
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateDirect(false,(RTS_Process)pa.getELEMENT(i_2));
        }
        ;
        // JavaLine 706 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_createobjects((_CUR));
        ;
        // JavaLine 709 <== SourceLine 526
        actime_2=RTS_ENVIRONMENT.copy(copy(activationtimes_2));
        ;
        for(i_2=1;i_2<=10;i_2++) {
            // JavaLine 713 <== SourceLine 527
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateAt(false,(RTS_Process)pa.getELEMENT(i_2),(time()+(((double)(new simtst107_SimulaTestBegin_SimulationBegin_getime((_CUR))._RESULT)))),false);
        }
        ;
        // JavaLine 717 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_startup((_CUR));
        ;
        new simtst107_SimulaTestBegin_SimulationBegin_createobjects((_CUR));
        ;
        // JavaLine 722 <== SourceLine 531
        actime_2=RTS_ENVIRONMENT.copy(copy(activationtimes_2));
        ;
        for(i_2=1;i_2<=10;i_2++) {
            // JavaLine 726 <== SourceLine 532
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateAt(false,(RTS_Process)pa.getELEMENT(i_2),(time()+(((double)(new simtst107_SimulaTestBegin_SimulationBegin_getime((_CUR))._RESULT)))),true);
        }
        ;
        // JavaLine 730 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_startup((_CUR));
        ;
        new simtst107_SimulaTestBegin_SimulationBegin_createobjects((_CUR));
        ;
        // JavaLine 735 <== SourceLine 536
        actime_2=RTS_ENVIRONMENT.copy(copy(activationtimes_2));
        ;
        for(i_2=1;i_2<=9;i_2=i_2+2) {
            // JavaLine 739 <== SourceLine 537
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateAt(false,(RTS_Process)pa.getELEMENT(i_2),(time()+(((double)(new simtst107_SimulaTestBegin_SimulationBegin_getime((_CUR))._RESULT)))),false);
        }
        ;
        for(i_2=2;i_2<=10;i_2=i_2+2) {
            // JavaLine 744 <== SourceLine 538
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateBefore(false,(RTS_Process)pa.getELEMENT(i_2),(RTS_Process)pa.getELEMENT(RTS_UTIL._ISUB(i_2,1)));
        }
        ;
        // JavaLine 748 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_startup((_CUR));
        ;
        new simtst107_SimulaTestBegin_SimulationBegin_createobjects((_CUR));
        ;
        // JavaLine 753 <== SourceLine 542
        actime_2=RTS_ENVIRONMENT.copy(copy(activationtimes_2));
        ;
        for(i_2=1;i_2<=9;i_2=i_2+2) {
            // JavaLine 757 <== SourceLine 543
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateAt(false,(RTS_Process)pa.getELEMENT(i_2),(time()+(((double)(new simtst107_SimulaTestBegin_SimulationBegin_getime((_CUR))._RESULT)))),false);
        }
        ;
        for(i_2=2;i_2<=10;i_2=i_2+2) {
            // JavaLine 762 <== SourceLine 544
            ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR)).ActivateAfter(false,(RTS_Process)pa.getELEMENT(i_2),(RTS_Process)pa.getELEMENT(RTS_UTIL._ISUB(i_2,1)));
        }
        ;
        // JavaLine 766 <== SourceLine 11
        new simtst107_SimulaTestBegin_SimulationBegin_startup((_CUR));
        ;
        for(i_2=1;i_2<=utno_2;i_2++) {
            // JavaLine 770 <== SourceLine 548
            if(_VALUE(RTS_UTIL._TXTREL_NE(ut.getELEMENT(i_2),answer.getELEMENT(i_2)))) {
                failed_2=true;
            }
        }
        ;
        // JavaLine 776 <== SourceLine 550
        if(_VALUE(failed_2)) {
            // JavaLine 778 <== SourceLine 551
            {
                RTS_BASICIO.sysout().outtext(new RTS_TXT("*** error : output"));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                RTS_BASICIO.sysout().outtext(new RTS_TXT("            outputlines which did not match:"));
                ;
                RTS_BASICIO.sysout().outimage();
                ;
                for(i_2=1;i_2<=utno_2;i_2++) {
                    // JavaLine 789 <== SourceLine 555
                    if(_VALUE(RTS_UTIL._TXTREL_NE(answer.getELEMENT(i_2),ut.getELEMENT(i_2)))) {
                        // JavaLine 791 <== SourceLine 556
                        {
                            RTS_BASICIO.sysout().outchar('*');
                            ;
                            RTS_BASICIO.sysout().outtext(ut.getELEMENT(i_2));
                            ;
                            RTS_BASICIO.sysout().outimage();
                            ;
                            RTS_BASICIO.sysout().outchar('-');
                            ;
                            RTS_BASICIO.sysout().outtext(answer.getELEMENT(i_2));
                            ;
                            RTS_BASICIO.sysout().outimage();
                        }
                    }
                }
            }
        }
        ;
        // ENDOF Simulation INNER PART
        // ENDOF Simset INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","PrefixedBlock SimulationBegin",1,11,12,14,15,16,18,85,20,87,22,88,26,89,29,90,37,14,39,16,41,85,43,87,45,88,56,94,59,96,62,98,65,100,68,102,71,104,74,106,77,108,80,110,83,112,86,114,89,116,92,118,95,120,98,122,101,124,104,126,107,128,110,130,113,132,116,134,119,136,122,138,125,140,128,142,131,144,134,146,137,148,140,150,143,152,146,154,149,156,152,158,155,160,158,162,161,164,164,166,167,168,170,170,173,172,176,174,179,176,182,178,185,180,188,182,191,184,194,186,197,188,200,190,203,192,206,194,209,196,212,198,215,200,218,202,221,204,224,206,227,208,230,210,233,212,236,214,239,216,242,218,245,220,248,222,251,224,254,226,257,228,260,230,263,232,266,234,269,236,272,238,275,240,278,242,281,244,284,246,287,248,290,250,293,252,296,254,299,256,302,258,305,260,308,262,311,264,314,266,317,268,320,270,323,272,326,274,329,276,332,278,335,280,338,282,341,284,344,286,347,288,350,290,353,292,356,294,359,296,362,298,365,300,368,302,371,304,374,306,377,308,380,310,383,312,386,314,389,316,392,318,395,320,398,322,401,324,404,326,407,328,410,330,413,332,416,334,419,336,422,338,425,340,428,342,431,344,434,346,437,348,440,350,443,352,446,354,449,356,452,358,455,360,458,362,461,364,464,366,467,368,470,370,473,372,476,374,479,376,482,378,485,380,488,382,491,384,494,386,497,388,500,390,503,392,506,394,509,396,512,398,515,400,518,402,521,404,524,406,527,408,530,410,533,412,536,414,539,416,542,418,545,420,548,422,551,424,554,426,557,428,560,430,563,432,566,434,569,436,572,438,575,440,578,442,581,444,584,446,587,448,590,450,593,452,596,454,599,456,602,458,605,460,608,462,611,464,614,466,617,468,620,470,623,472,626,474,629,476,632,478,635,480,638,482,641,484,644,486,647,488,650,490,653,492,656,494,659,496,662,498,665,500,668,502,671,504,674,506,677,508,680,510,683,512,686,514,689,516,692,519,695,520,698,11,702,523,706,11,709,526,713,527,717,11,722,531,726,532,730,11,735,536,739,537,744,538,748,11,753,542,757,543,762,544,766,11,770,548,776,550,778,551,789,555,791,556,814,560);
} // End of Class
