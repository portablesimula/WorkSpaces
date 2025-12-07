begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 15";
   const infix(string) PURPOSE      ="Type Conversion";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(13) = (
      "BEGIN TEST",
      "'A' qua integer = 65, 66 qua character = B",
      "3.49 qua integer = 3",
      "3.50 qua integer = 4",
      "3.51 qua integer = 4",
      "7.3&+001 qua integer = 73",
      "7.4&+001 qua integer = 74",
      "7.4&+001 qua integer = 74",
      "7.35&+001 qua real = 7.35&+01",
      "357 qua real = 3.57&+02",
      "7.349 qua long real = 7.35&+000",
      "357 qua long real = 3.57&+002",
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

	character c;
	integer i;
	real r;
	long real d;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		i:=66; c:='A'; ED_STR("'A' qua integer = "); ED_INT(c qua integer);
		ED_STR(", 66 qua character = "); ED_CHA(i qua character); trace(get_ed);
		
		r:=3.49; ED_STR("3.49 qua integer = "); ED_INT(r qua integer); trace(get_ed);
		r:=3.50; ED_STR("3.50 qua integer = "); ED_INT(r qua integer); trace(get_ed);
		r:=3.51; ED_STR("3.51 qua integer = "); ED_INT(r qua integer); trace(get_ed);
		
		d:=7.349&&1; ED_LRL(d,2); ED_STR(" qua integer = "); ED_INT(d qua integer); trace(get_ed);
		d:=7.350&&1; ED_LRL(d,2); ED_STR(" qua integer = "); ED_INT(d qua integer); trace(get_ed);
		d:=7.351&&1; ED_LRL(d,2); ED_STR(" qua integer = "); ED_INT(d qua integer); trace(get_ed);

		d:=7.349&&1; ED_LRL(d,3); ED_STR(" qua real = "); ED_REA(d qua real,3); trace(get_ed);
		i:=357; ED_STR("357 qua real = "); ED_REA(i qua real,3); trace(get_ed);

		r:=7.349; ED_STR("7.349 qua long real = "); ED_LRL(r qua long real,3); trace(get_ed);
		i:=357; ED_STR("357 qua long real = "); ED_LRL(i qua long real,3); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
