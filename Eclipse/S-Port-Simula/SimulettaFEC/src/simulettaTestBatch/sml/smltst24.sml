begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 24";
   const infix(string) PURPOSE      ="Zeroarea";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "REC1: r1.k=2222",
      "REC2: r2.sl=NONE",
      "REC2: r2.misc=0",
      "REC2: r2.ncha=0",
      "REC2: r2.i=0",
      "REC2: r2.j=0",
      "REC2: r2.k=0",
      "REC3: (r3.sl=r2)=true",
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
	begin integer i,j,k; end;

	Visible record REC2:inst;
	begin integer i,j,k(0); end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1,r2,r3;

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
	
--		ED_STR("POOLSIZE="); ED_SIZE(poolsize); ED_STR(", SIZE(REC)="); ED_SIZE(size(REC)); ED_STR(", SIZE(inst)="); ED_SIZE(size(inst)); trace(get_ed);
--		dist:=r2 - r1; ED_STR("DIST(r2-r1)="); ED_SIZE(dist); trace(get_ed);
	
--		ED_STR("r2.j="); ED_INT(r2.j); trace(get_ed);

		r1.k:=2222; -- Last cell of r1'object
		r3.sl:=r2;  -- First cell of r3'object
		
		r2.sl:=r1;  -- First cell of r3'object
		r2.misc:=4444; -- cell 2
		r2.ncha:=5555; -- cell 3
		r2.i:=7777;    -- cell 8
		r2.j:=8888;    -- cell 9
		r2.k:=9999; -- Last cell 10 of r1'object
		
		
		Zeroarea(r2,r2+size(REC));
		goto L; L: -- To check StackEmpty
--		Zeroarea(r1+size(REC),r1);

		-- Check r1 untouched
		ED_STR("REC1: r1.k="); ED_INT(r1.k); trace(get_ed);

		-- Check r2 zero-filled
		ED_STR("REC2: r2.sl="); ED_OADDR(r2.sl); trace(get_ed);
		ED_STR("REC2: r2.misc="); ED_INT(r2.misc); trace(get_ed);
		ED_STR("REC2: r2.ncha="); ED_INT(r2.ncha); trace(get_ed);
		ED_STR("REC2: r2.i="); ED_INT(r2.i); trace(get_ed);
		ED_STR("REC2: r2.j="); ED_INT(r2.j); trace(get_ed);
		ED_STR("REC2: r2.k="); ED_INT(r2.k); trace(get_ed);

		-- Check r3 untouched
		ED_STR("REC3: (r3.sl=r2)="); ED_BOOL(r3.sl=r2); trace(get_ed);
		
		initarea(REC,r1);
		goto L2; L2: -- To check StackEmpty
		
		dinitarea(REC2,4,r3);
		goto L3; L3: -- To check StackEmpty
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
