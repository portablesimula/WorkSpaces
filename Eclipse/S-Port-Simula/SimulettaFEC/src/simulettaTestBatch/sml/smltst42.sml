begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 42";
   const infix(string) PURPOSE      ="Test routine MOVEIN";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(16) = (
      "BEGIN TEST",
      "w.j=5555",
      "r1.j=0",
      "w.sl=POOL_1[19]",
      "r1.sl=NONE",
      "w.j=5555",
      "r1.j=5555",
      "w.sl=POOL_1[19]",
      "r1.sl=POOL_1[19]",
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

	Visible record REC:inst;
	begin integer  i(4);
    	  integer  j;
    	  ref(inst) suc;
    	  infix(string) str;
    	  character c(3);
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr qua ref(inst);
		  bio.nxtAdr:= ( bio.nxtAdr + length) qua ref(entity);
		  ins.sort:= S_SUB;
	end;
      
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
	ref(REC) x,w;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
   
      trace("BEGIN TEST");

 	    sequ := 1;
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool qua ref(entity);
		bio.lstAdr:=(pool+poolsize) qua ref(entity);
	
		r1:=ALLOC(size(REC));
		x:=ALLOC(size(REC));
		w:=ALLOC(size(REC));
		
		w.i(0):=1111;
		w.i(1):=2222;
		w.i(2):=3333;
		w.i(3):=4444;
		w.j:=5555;
		w.sl := x;
		ED_STR("w.j="); ED_INT(w.j); trace(get_ed);
		ED_STR("r1.j="); ED_INT(r1.j); trace(get_ed);
		ED_STR("w.sl="); ED_OADDR(w.sl); trace(get_ed);
		ED_STR("r1.sl="); ED_OADDR(r1.sl); trace(get_ed);
		
		MOVEIN(w, r1, size(REC));
		
		ED_STR("w.j="); ED_INT(w.j); trace(get_ed);
		ED_STR("r1.j="); ED_INT(r1.j); trace(get_ed);
		ED_STR("w.sl="); ED_OADDR(w.sl); trace(get_ed);
		ED_STR("r1.sl="); ED_OADDR(r1.sl); trace(get_ed);

       trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
