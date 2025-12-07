begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 31";
   const infix(string) PURPOSE      ="Test CALL, EXIT, RETURN and GOTO";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(6) = (
      "BEGIN TEST",
      "Call RUT",
      "After label LLL",
      "GOTO saved address",
      "After Call RUT",
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

	label returnAddress;
	integer i;

	Visible routine RUT; exit label psc;
	begin
        returnAddress:=psc;
        psc:=LLL;
	end;

	if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
     
        trace("Call RUT");
		RUT;
        trace("After Call RUT");
		i:=6666;

		if i < 0 then
LLL:
			trace("After label LLL");
	    	i:=4444;
	    	trace("GOTO saved address");
			goto returnAddress;
		endif;
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
