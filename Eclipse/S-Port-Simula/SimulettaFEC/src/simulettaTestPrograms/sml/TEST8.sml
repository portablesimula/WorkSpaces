--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace
begin
   SYSINSERT envir,modl1;
--   SYSINSERT RT,SYSR,KNWN,UTIL;    

    Visible record Q:R;
	begin integer   k;
    	  integer   m(8);
    	  variant   integer a,b,c;
    	  variant   long real lrl;
 	end;

    Visible record R;
	begin integer   i;
    	  integer   j;
    	  variant   integer  int;
    	  variant   real rea;
    	  variant   infix(string) str;
	end;

    infix(Q) IQ;
--    infix(R) IR;
--    infix(string) abr="ABRA";

--    ref(R) RR;
    name(infix(Q)) NQ;

--	field(name(character)) fchradr;
	field(integer) fnchr;
    field(integer) fm;

      NQ:=name(IQ);
--      IR.str:=abr;
--      RR:=ref(IR);
--      fchradr:=field(R.str.chradr);
--      fnchr:=field(R.str.nchr);
      fm:=field(Q.m);
      
--      var(RR+fchradr):=abr.chradr;
--      var(RR+fnchr):=abr.nchr;
      
      var(NQ+fm)(2):=666;
--	  ed_int(var(NQ+fm)(2));
	  ed_int(IQ.m(2));
	  ed_out;
 end;
