begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 03";
   const infix(string) PURPOSE      ="Test Assert Statement";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(3) = (
      "Assertion hold",
      "Assertion FAILED",
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

   infix(string) n; boolean cond;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      n:="Assertion hold"; cond:=true;
	  ASSERT cond SKIP n:="Assertion FAILED" ENDSKIP
      trace(n);
      
      n:="Assertion hold"; cond:=false;
	  ASSERT cond SKIP n:="Assertion FAILED" ENDSKIP
      trace(n);
      
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
