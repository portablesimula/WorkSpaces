begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 00";
   const infix(string) PURPOSE      ="Standard layout of test programs";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(4) = (
      "Main: Before new A",
      "Main: Before first call",
      "Main: Before second call",
      "Main: Before third call"
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

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;

-- ==============================================================================
 
      trace("Main: Before new A");
      trace("Main: Before first call");
      trace("Main: Before second call");
      trace("Main: Before third call");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

%   TERMIN(0,"NORMAL END-OF-PROGRAM");

 end;
