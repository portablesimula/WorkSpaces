begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 38";
   const infix(string) PURPOSE      ="INITO, GETO, SETO";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(7) = (
      "BEGIN TEST",
      "r1.p1=POOL_0[19]",
      "r1.i=4444",
      "r1.p2=POOL_0[19]",
      "r1.j=8888",
      "r1.p3=POOL_0[19]",
      "END TEST"
   );
   
   Visible routine trace; import infix(string) msg;
   begin
      if verbose then ed_str(msg); ed_str("  TEST AGAINST FACIT:  "); prt(facit(traceCase)); endif;
      if( not STREQL(msg,facit(traceCase))) then
         nError:=nError+1; prt(" ");
         ed_str("ERROR in Case "); ed_int(traceCase); ed_out;
         ed_str("Trace: "); prt(msg);
         ed_str("Facit: "); prt(facit(traceCase));
      endif;
      traceCase:=traceCase+1;
   end;

	Visible record REC:inst; begin
		ref(REC) p1;
		integer   i;
		ref(REC) p2;
    	integer   j;
		ref(REC) p3;
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
	ref(inst) space;
	ref(REC) r2,y;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
%		poolsize:=SIZEIN(1,sequ);
		poolsize := none + size(REC) + size(REC) + size(REC) + size(REC) - none;
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool;
		bio.lstAdr:=pool+poolsize;
	
		r1:=ALLOC(size(REC));
		space:=ALLOC(size(inst));
		r2:=ALLOC(size(REC));
	
		r1.p1 := r1;
		r1.i  := 4444;
		r1.p2 := r1;
		r1.j  := 8888
		r1.p3 := r1;
		r1.lng := size(REC);
		
		init_pointer(r1);
		repeat y:= get_pointer while y <> none
			do set_pointer(r2);
		endrepeat
		
		ED_STR("r1.p1="); ED_OADDR(r1.p1); trace(get_ed);
		ED_STR("r1.i=");  ED_INT(r1.i);    trace(get_ed);
		ED_STR("r1.p2="); ED_OADDR(r1.p2); trace(get_ed);
		ED_STR("r1.j=");  ED_INT(r1.j);    trace(get_ed);
		ED_STR("r1.p3="); ED_OADDR(r1.p3); trace(get_ed);
			
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
