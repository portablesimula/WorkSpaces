--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

 -- MAIN Program:
 begin
      insert envir,modl1;
--      sysinsert rt,sysr,knwn,util;

	Visible routine ED_STRx; import infix(string) str;
	begin integer i,n;
--	      n:=str.nchr-1;
--	      if n>=0 then
--	          repeat bio.utbuff(bio.utpos+i):=var(str.chradr)(i);
--	          while i<n do i:=i+1 endrepeat;
--	      endif;
--	      bio.utpos:=bio.utpos+n+1;
	end;

	Visible routine PRT2x; import infix(string) t,t2;
	begin
			 ED_STRx(t);
--		 	 ED_STR(t2);
--		 	 ED_OUT
	end;

	PRT2x("ABRA","CADAB");

end
