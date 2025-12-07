begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 44";
   const infix(string) PURPOSE      ="Test Routine MODULO";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(5) = (
      "BEGIN TEST",
      "(4 * 6) + 2 = 26",
      "26 MOD 6 ==> 2",
      "26 modulo 6 ==> 2",
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
   
   Routine modulo; import integer x, y; export integer res; begin
   		res := x - ((x / y)*y);
   end;
   
   integer i, j, k;
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
   
      trace("BEGIN TEST");

		i := (4 * 6) + 2;
		j := 6;
		ed_str("(4 * 6) + 2 = "); ed_int(i); trace(get_ed);
		
		k := MOD(i, j);
		ed_int(i); ed_str(" MOD "); ed_int(j); ed_str(" ==> "); ed_int(k);  trace(get_ed);
		
		k := modulo(i, j);
		ed_int(i); ed_str(" modulo "); ed_int(j); ed_str(" ==> "); ed_int(k);  trace(get_ed);
		
		
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
