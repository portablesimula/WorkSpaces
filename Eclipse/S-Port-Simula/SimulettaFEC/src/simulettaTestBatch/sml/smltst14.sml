begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 14";
   const infix(string) PURPOSE      ="Object Reference and Size Expression";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(11) = (
      "BEGIN TEST",
      "POOLSIZE=150000, SIZE(REC)=12, SIZE(inst)=7",
      "DIST(r2-r1)=19",
      "DIST((r1+NOSIZE)-r1)=0",
      "DIST((r1+size(inst))-r1)=7",
      "DIST((r4-size(inst))-r1)=19",
      "r5.i=4444",
      "r5.rea=3",
      "r2.j=6666",
      "dist=19",
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
	begin integer   i;
    	  integer   j;
    	  variant   integer int;
    	            real    rea;
    	  variant   infix(string) str;
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
	ref(REC) r2,r3,r4,r5;
	size dist;
	size spsize;
	integer i;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool;
		bio.lstAdr:=pool+poolsize;
	
		r1:=ALLOC(size(REC));
		space:=ALLOC(size(inst));
		r2:=ALLOC(size(REC));
	
		ED_STR("POOLSIZE="); ED_SIZE(poolsize); ED_STR(", SIZE(REC)="); ED_SIZE(size(REC)); ED_STR(", SIZE(inst)="); ED_SIZE(size(inst)); trace(get_ed);
		dist:=r2 - r1; ED_STR("DIST(r2-r1)="); ED_SIZE(dist); trace(get_ed);
	
		r3:=r1+spsize; dist:=r3 - r1; ED_STR("DIST((r1+NOSIZE)-r1)="); ED_SIZE(dist); trace(get_ed);
		r3:=r1+size(inst); dist:=r3 - r1; ED_STR("DIST((r1+size(inst))-r1)="); ED_SIZE(dist); trace(get_ed);
		r4:=r2-spsize; dist:=r4 - r1; ED_STR("DIST((r4-size(inst))-r1)="); ED_SIZE(dist); trace(get_ed);
	
		r2.i:=4444;
		r2.rea:=3.14;
		r5:=(r1+size(REC))+size(inst);
	
		ED_STR("r5.i="); ED_INT(r5.i); trace(get_ed);
		ED_STR("r5.rea="); ED_INT(r5.rea); trace(get_ed);
		
		r5.j:=6666;
		ED_STR("r2.j="); ED_INT(r2.j); trace(get_ed);
		
		dist:=none + size(REC) + size(inst) - none;
		ED_STR("dist="); ED_SIZE(dist); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
