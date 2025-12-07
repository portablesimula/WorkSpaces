begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 26";
   const infix(string) PURPOSE      ="Complex Variables";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(14) = (
      "BEGIN TEST",
      "TEST: var(niR).k=4444, ii=4444",
      "TEST: var(nrR).k=6666, ii=6666",
      "END TEST",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "END TEST"
   );
   
   routine trace; import infix(string) msg;
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

	record REC:inst;
	begin integer i,j,k; end;

	record REC2:inst;
	begin integer i,j,k(0); end;

	routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1,r2,r3;

	infix(REC) iREC;
	name(infix(REC)) niR;
	name(ref(REC)) nrR;
	ref(entity) z;
	integer ii;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool;
		bio.lstAdr:=pool+poolsize;
	
		r1:=ALLOC(size(REC));
		r2:=ALLOC(size(REC));
		r3:=ALLOC(size(REC2:4));
	
		niR:=name(iREC);
		var(niR).k:=4444;
		ii:=var(niR).k;
		ED_STR("TEST: var(niR).k="); ED_INT(var(niR).k); ED_STR(", ii="); ED_INT(ii); trace(get_ed);
	
		nrR:=name(r1);
		var(nrR).k:=6666;
		ii:=var(nrR).k;
		ED_STR("TEST: var(nrR).k="); ED_INT(var(nrR).k); ED_STR(", ii="); ED_INT(ii); trace(get_ed);
	
      EE:trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
