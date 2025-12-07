begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 22";
   const infix(string) PURPOSE      ="Exit and Non-Local Goto";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "Exit from call: RUT1(0)",
      "Exit from call: RUT2(F1)",
      "Exit from call: RUT2(LAB)",
      "Exit from call: RUT2(NOWHERE)",
      "END TEST",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
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

	Visible routine RUT1; import integer i;
	begin
		goto F0;
	end;


	Visible routine RUT2; import label LI; exit label LL;
	begin
		if LI=NOWHERE then goto F3 endif;
		LL:=LI;
	end;
	
	label LAB;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================call
 
      trace("BEGIN TEST");

		RUT1(0);
		trace("UMULIG Å KOMME HIT");
	F0: trace("Exit from call: RUT1(0)");

		RUT2(F1);
		trace("UMULIG Å KOMME HIT");
	F1: trace("Exit from call: RUT2(F1)");
	
		LAB:=F2;
		RUT2(LAB);
		trace("UMULIG Å KOMME HIT");
	F2: trace("Exit from call: RUT2(LAB)");

		RUT2(NOWHERE);
		trace("UMULIG Å KOMME HIT");
	F3: trace("Exit from call: RUT2(NOWHERE)");
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
