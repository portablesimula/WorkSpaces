begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 39";
   const infix(string) PURPOSE      ="Boolean Operators ( imp, eqv )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "T imp T=true",
      "T imp F=false",
      "F imp T=true",
      "F imp F=true",
      "T eqv T=true",
      "T eqv F=false",
      "F eqv T=true",
      "F eqv F=false",
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

   boolean T=true,F=false;
 
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");

      ed_str("T imp T="); ed_bool(T imp T); trace(get_ed);
      ed_str("T imp F="); ed_bool(T imp F); trace(get_ed);
      ed_str("F imp T="); ed_bool(F imp T); trace(get_ed);
      ed_str("F imp F="); ed_bool(F imp F); trace(get_ed);
      
      ed_str("T eqv T="); ed_bool(T eqv T); trace(get_ed);
      ed_str("T eqv F="); ed_bool(T eqv F); trace(get_ed);
      ed_str("F eqv T="); ed_bool(F eqv T); trace(get_ed);
      ed_str("F eqv F="); ed_bool(F eqv F); trace(get_ed);
      
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
