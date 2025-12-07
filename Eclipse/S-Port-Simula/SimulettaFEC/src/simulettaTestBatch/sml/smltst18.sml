begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 18";
   const infix(string) PURPOSE      ="Goto-Statements";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(7) = (
      "BEGIN TEST",
      "BB: Forward Jump: j=44",
      "FF: Backward Jump: j=44",
      "Backward Jump: j=0",
      "BB: Forward Jump: j=0",
      "EE: Last label: j=0",
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

		integer i,j;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
 
 		j:=44;
	BB:
	    -- Forward Jumps
		ED_STR("BB: Forward Jump: j="); ED_INT(j); trace(get_ed);
 		if j=44 then goto FF endif;
	    i:=88;
	    goto EE;

	    -- Backward Jumps
	FF:
		ED_STR("FF: Backward Jump: j="); ED_INT(j); trace(get_ed);
	    j:=0;
		ED_STR("Backward Jump: j="); ED_INT(j); trace(get_ed);
	    goto BB;
	    i:=88;
    EE: 
		ED_STR("EE: Last label: j="); ED_INT(j); trace(get_ed);
		
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
