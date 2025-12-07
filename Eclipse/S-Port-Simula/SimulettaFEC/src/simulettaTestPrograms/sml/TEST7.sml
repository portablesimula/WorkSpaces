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
--  SYSINSERT envir,modl1;
	SYSINSERT RT,SYSR,KNWN,UTIL;    

    Visible record AAA; info "TYPE";
	begin integer   ax;
--	      infix(string) id;
          character   mm;
    	  variant   integer  var1(3);
    	  variant   real var2,var3;
    end;

    Visible record WithVariants:AAA; info "TYPE";
	begin integer   lb;
--	      infix(string) id;
          long real   kk;
    	  integer   ub;
    	  variant   integer  int;
    	  variant   real rea;
    	  variant   infix(string) str;
	end;
   
    routine prtBounds;
    begin ed_str("  lb="); ed_int(r.lb); ed_out;
          ed_str("  ub="); ed_int(r.ub); ed_out;
--          ed_str("  int="); ed_int(r.int); ed_out;
          ed_str("  rea="); ed_rea(r.rea); ed_out;
--          ed_str("  str="); ed_str(r.str); ed_out;
    end;

 routine EDSTR; import infix(string) str;
begin integer i,n; character c;
--      n:=str.nchr-1;
--      if n>=0 then
--          repeat
--           bio.utbuff(bio.utpos+i):=var(str.chradr)(i);
           bio.utbuff:=var(str.chradr)(i);
--          while i<n do i:=i+1 endrepeat;
--      endif;
--      bio.utpos:=bio.utpos+n+1;
end;
   
    infix(WithVariants) r;
    infix(string) s;
    character c(6);
    boolean b;
    integer i;
    
    --const
--     infix(WithVariants) z=record:WithVariants(lb=121,ub=13,str="ABRA");
--    z.ub:=78;
--    z.rea:=5.6;
--    z.var1(2):=125;
--    prt2("ABRA","CADAB");
--    edstr("ABRA");
--    s.chradr:=@c;
--    b:=false;
    i:=0;

--	r.int:=44;
--	r.rea:=4.4;
--	prtBounds;
	
--	ed_str("int="); ed_int(r.int); s:=get_ed; --trace(s);
--	ed_str("ABRA: "); prt(s);
	
end
