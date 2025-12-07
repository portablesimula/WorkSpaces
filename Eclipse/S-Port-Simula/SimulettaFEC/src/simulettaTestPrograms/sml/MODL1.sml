--% %pass 2 input = 4
--% %pass 2 trace = 4
--% %pass 2 modtrc = 4

--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

Module MODL1("TST 1.0");
 begin insert envir;

       -----------------------------------------------------------------------
       ---                                                                 ---
       ---            S I M U L E T T A    T E S T B A T C H               ---
       ---                                                                 ---
       ---                                                                 ---
       -----------------------------------------------------------------------

 Visible sysroutine("GTOUTM") GTOUTM;
 --- psc must always correspond to an address in the user program
 --- GTOUTM is not called from the exception handler
 --- Status must not be changed
 export label psc  end;


 Visible sysroutine("PUTINT2") PUTINT2;
    import infix (string) item; integer val
    export integer lng;
 end;

 Visible sysroutine("PTREAL2") PTREAL2;
    import infix (string) item; real val
    export integer lng;
 end;

 Visible sysroutine("PRINTO") PRINTO;
 import integer key; infix(string) image; integer spc  end;

 Visible sysroutine("STREQL") STREQL;
 import infix(string) str1,str2; export boolean res  end;

 Visible sysroutine ("SIZEIN") SIZEIN;
 import range(0:127) index; range(0:255) ano;
 export size result  end;

 Visible sysroutine ("DWAREA")  DWAREA;
 import size lng; range(0:255) ano;
 export ref() pool  end;


 Visible known("SYSPRI") SYSPRI; import infix(string) img;
 begin
       -----------------------------
       PRINTO(0,img,1);
       -----------------------------
 end;


 Visible body(PRF) BDY;
 begin
 end;

%title ***  B a s i c   O u t p u t    H a n d l i n g  ***

-- Editing system output messages
-- All other editing routines are used only in MNTR

-- integer utpos;
-- character utbuff(40);


Visible routine ED_CHR; import character chr;
begin utbuff(utpos):=chr; utpos:=utpos+1; end;

Visible routine ED_STR; import infix(string) str;
begin integer i,n;
      n:=str.nchr-1;
      if n>=0 then
          repeat utbuff(utpos+i):=var(str.chradr)(i);
          while i<n do i:=i+1 endrepeat;
      endif;
      utpos:=utpos+n+1;
end;

Visible routine ED_OUT;
begin infix(string) im; 
      if utpos > 0 then
           im.chradr:=@utbuff; im.nchr:=utpos;
           SYSPRI(im); utpos:=0;
--           SYSPRI(GET_ED);
      endif;
end;

Visible routine GET_ED; export infix(string) res;
begin res.chradr:=@utbuff; res.nchr:=utpos;
      utpos:=0;
end;


Visible routine PRT; import infix(string) t;
begin ED_STR(t); ED_OUT end;

Visible routine PRT2; import infix(string) t,t2;
begin ED_STR(t); ED_STR(t2); ED_OUT end;

Visible routine PRT3; import infix(string) t,t2,t3;
begin ED_STR(t); ED_STR(t2); ED_STR(t3); ED_OUT end;
   

Visible routine REST; export infix(string) s;
begin s.chradr:=@utbuff(utpos); s.nchr:=utlng-utpos;
end;

Visible routine ED_INT; import integer i;
begin
     utpos:=utpos+PUTINT2(REST,i);
end;
   

Visible routine ED_REA; import real r;
begin
     utpos:=utpos+PTREAL2(REST,r);
end;
   
end;
