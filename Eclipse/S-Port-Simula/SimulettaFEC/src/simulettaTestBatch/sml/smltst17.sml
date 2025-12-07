begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 17";
   const infix(string) PURPOSE      ="If-Statements";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(20) = (
      "BEGIN TEST",
      "TEST1(1)=0",
      "TEST1(2)=4444",
      "TEST1(3)=0",
      "TEST2(1)=6666",
      "TEST2(2)=4444",
      "TEST2(3)=6666",
      "TEST3(1)=0",
      "TEST3(2)=4444",
      "TEST3(3)=6666",
      "TEST3(4)=0",
      "TEST3(5)=8888",
      "TEST3(6)=0",
      "TEST4(1)=9999",
      "TEST4(2)=4444",
      "TEST4(3)=6666",
      "TEST4(4)=9999",
      "TEST4(5)=8888",
      "TEST4(6)=9999",
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

	Visible routine TEST1;
	import integer code;
	export integer res;
	begin if code = 2 then res:=4444 endif;
	end;

	Visible routine TEST2;
	import integer code;
	export integer res;
	begin if code = 2 then res:=4444;
          else res:=6666 endif;
	end;

	Visible routine TEST3;
	import integer code;
	export integer res;
	begin if code = 2 then res:=4444;
          elsif code = 3 then res:=6666
          elsif code = 5 then res:=8888 endif;
	end;

	Visible routine TEST4;
	import integer code;
	export integer res;
	begin if code = 2 then res:=4444;
          elsif code = 3 then res:=6666
          elsif code = 5 then res:=8888
          else res:=9999 endif;
	end;

	integer code,res;
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
 
 		res:=TEST1(1); ED_STR("TEST1(1)="); ED_INT(res); trace(get_ed);
 		res:=TEST1(2); ED_STR("TEST1(2)="); ED_INT(res); trace(get_ed);
 		res:=TEST1(3); ED_STR("TEST1(3)="); ED_INT(res); trace(get_ed);
		
 		res:=TEST2(1); ED_STR("TEST2(1)="); ED_INT(res); trace(get_ed);
 		res:=TEST2(2); ED_STR("TEST2(2)="); ED_INT(res); trace(get_ed);
 		res:=TEST2(3); ED_STR("TEST2(3)="); ED_INT(res); trace(get_ed);
		
 		res:=TEST3(1); ED_STR("TEST3(1)="); ED_INT(res); trace(get_ed);
 		res:=TEST3(2); ED_STR("TEST3(2)="); ED_INT(res); trace(get_ed);
 		res:=TEST3(3); ED_STR("TEST3(3)="); ED_INT(res); trace(get_ed);
 		res:=TEST3(4); ED_STR("TEST3(4)="); ED_INT(res); trace(get_ed);
 		res:=TEST3(5); ED_STR("TEST3(5)="); ED_INT(res); trace(get_ed);
 		res:=TEST3(6); ED_STR("TEST3(6)="); ED_INT(res); trace(get_ed);
		
 		res:=TEST4(1); ED_STR("TEST4(1)="); ED_INT(res); trace(get_ed);
 		res:=TEST4(2); ED_STR("TEST4(2)="); ED_INT(res); trace(get_ed);
 		res:=TEST4(3); ED_STR("TEST4(3)="); ED_INT(res); trace(get_ed);
 		res:=TEST4(4); ED_STR("TEST4(4)="); ED_INT(res); trace(get_ed);
 		res:=TEST4(5); ED_STR("TEST4(5)="); ED_INT(res); trace(get_ed);
 		res:=TEST4(6); ED_STR("TEST4(6)="); ED_INT(res); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
