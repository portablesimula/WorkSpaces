begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 20";
   const infix(string) PURPOSE      ="Repeat-Statements";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(5) = (
      "BEGIN TEST",
      "Repeat: 5 * 4 = 20",
      "Repeat: 4 * 99 = 396",
      "Repeat: 4 * 66 = 264",
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

		integer i,j,res;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
 
 		j:=5;
	    repeat while j > 0 do j:=j-1; res:=res+4 endrepeat;   
		ED_STR("Repeat: 5 * 4 = "); ED_INT(res); trace(get_ed);
	    
	    j:=3;
	    repeat  i:=i+99; while j > 0 do j:=j-1; endrepeat;   
		ED_STR("Repeat: 4 * 99 = "); ED_INT(i); trace(get_ed);
	    
	    i:=0; j:=4;
	    repeat i:=i+66; j:=j-1 while j > 0 do endrepeat;   
		ED_STR("Repeat: 4 * 66 = "); ED_INT(i); trace(get_ed);
		
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
