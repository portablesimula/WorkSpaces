 --%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

 Global ENVIR0("TST 1.0");
 begin
       -----------------------------------------------------------------------
       ---                                                                 ---
       ---            S I M U L E T T A    T E S T B A T C H               ---
       ---                                                                 ---
       ---              G l o b a l     D e f i n i t i o n s              ---
       ---                                                                 ---
       -----------------------------------------------------------------------

Visible define
      MAX_BYT = 255 ,     -- Value of a byte
      MAX_EXC = 17 ,
      MAX_ENO = 139 ,     -- Number of rts-msg
      MAX_TXT = 32000     -- Number of characters in a text object
 ;

 Visible define utlng = 80;    -- Output file's line length

%title ******   I n t e r f a c e   ******

 Visible record string;  info "TYPE";
 begin name(character)   chradr;
       integer           nchr;
 end;


 Visible global profile PXCHDL; -- PEXCHDL
 import range(0:MAX_EXC) code;
        infix(string)    msg;
        label            addr;
 export label            cont;
 end;


 Visible global profile PEXERR; -- PINIERR
 import range(0:MAX_ENO) eno;
        ref(string)      fil;
 end;

 Visible       integer          maxint    system "MAXINT";
 Visible       integer          minint    system "MININT";
 Visible const size             maxlen    system "MAXLEN";
 Visible const integer          inplth    system "INPLTH";-- inptlinelng
 Visible infix(bioIns) bio;
 
 Visible const infix(string)
       nostring=record:string(nchr=0,chradr=noname);

 Visible const infix(txtqnt)
       notext=record:txtqnt(sp=0,lp=0,cp=0,ent=none);

 Visible record txtqnt;  info "TYPE";
 begin ref(txtent)        ent;
       range(0:MAX_TXT)   cp;
       range(0:MAX_TXT)   sp;
       range(0:MAX_TXT)   lp;
 end;

 Visible record txtent;
 begin range(0:MAX_TXT)    ncha;
       character           cha(0);
 end;

 Visible record identifier;
 begin range(0:MAX_BYT)   ncha;
       character          cha(0);
 end;


 Visible record bioIns;--:inst;
 begin ref(txtent)            nxtAdr;   -- NOT in bioptp'refvec
       ref(txtent)            lstAdr;   -- NOT in bioptp'refvec
       integer        utpos;  -- Current output pos (0..utlng-1)
       character      utbuff(utlng);    -- The output buffer
 end;

 Visible global profile PRF;
 import integer nbytes;
 export name() Nobj;
 end;

 Visible entry(PRF)   BDY0;

 end;
