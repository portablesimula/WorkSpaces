 --%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

 Global ENVIR("TST 1.0");
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
--        ref(filent)      fil;
 end;

 Visible global profile PSIMOB;
 -- implicit parameters: bio.obsEvt, bio.smbP1, bio.smbP2 (detach only)
 end;

 Visible global profile PobSML;
 import short integer code;
        ref(inst)     ins;
 export ref(inst)     result;
 end;

 Visible define inlng = 80;          -- Input file's line length
 Visible define utlng = 80;          -- Output file's line length

 Visible integer utpos;              -- Current output line's pos (0..utlng-1)
 Visible character utbuff(utlng);    -- The output buffer
 Visible character inbuff(inlng);    -- The input buffer
 Visible character dumbuf(4);        -- Dummy input buffer (full page)

 Visible integer maxint             system "MAXINT";
 Visible integer minint             system "MININT";
 Visible integer maxrank            system "MAXRNK";
 Visible real maxreal               system "MAXREA";
 Visible real minreal               system "MINREA";
 Visible long real maxlreal         system "MAXLRL";
 Visible long real minlreal         system "MINLRL";
 Visible infix(bioIns) bio;
 
 Visible ref(inst)     curins    system "CURINS";
 Visible entry(PSIMOB) smb;
 
 
-- Visible sysroutine("PRINT") PRINT; import String str; end; 
-- Visible sysroutine("TRACE") TRACE; import integer lno; String str; end; 
 
 Visible const infix(string)
       nostring=record:string(nchr=0,chradr=noname);

 Visible const infix(txtqnt)
       notext=record:txtqnt(sp=0,lp=0,cp=0,ent=none);

-- Visible const String s="Abra ca dab";


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

%title ******   E  n  t  i  t  i  e  s   ******

 Visible record entity;  info "DYNAMIC";
 begin ref(inst)                sl;   -- during GC used as GCL!!!!
       range(0:MAX_BYT)         sort;
       range(0:MAX_BYT)         misc;
--       variant ref(ptp)         pp;   -- used for instances
       variant range(0:MAX_TXT) ncha; -- used for text entities
       variant size             lng;  -- used for other entities
 end;

 Visible record inst:entity;
 begin ref(entity)              gcl;
       variant ref(inst)        dl;
               label            lsc;
       variant entry(Pmovit)    moveIt;
 end;


 Visible record bioIns:inst;
 begin ref(entity)            nxtAdr;   -- NOT in bioptp'refvec
       ref(entity)            lstAdr;   -- NOT in bioptp'refvec
       character           ebuf(40); -- edit buffer (leftadj/exactfit)
 end;

 Visible global profile PRF;
 import integer nbytes;
 export name() Nobj;
 end;

 Visible entry(PRF)   BDY0;


 end;
