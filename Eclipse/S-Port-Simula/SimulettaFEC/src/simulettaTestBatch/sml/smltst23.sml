begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 23";
   const infix(string) PURPOSE      ="Export treatment";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "call test_CALL_TOS(30), s=ERROR: 30",
      "call P(R)=ERROR: 31",
      "call test_CALL_TOS(32), str=ERROR: 32",
      "call P(R)=ERROR: 33",
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

		infix(string) str;
		
%    	Visible global profile routineProfile;
    	Visible profile routineProfile;
    	import integer eno; ref(filent) fil;
    	       export infix(string) res;
    	end;

    	Visible routine test_CALL_TOS; import integer eno; export infix(string) res;
    	begin str := res := call routineProfile(routineRef)(eno,none) end;
 
    	Visible body(routineProfile) routineBody;
    	-- import integer eno; ref(filent) fil; export infix(string) res;
    	begin ed_str("ERROR: "); ed_int(eno);
    	      res:=get_ed;
    	end;
    	
	    Visible entry(routineProfile) routineRef;
	    infix(string) s;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");

	    if routineRef=NOBODY then routineRef:=entry(routineBody) endif;
	    
	    s:=test_CALL_TOS(30);
		ED_STR("call test_CALL_TOS(30), s="); ED_STR(s); trace(get_ed);
		
		call routineProfile(routineRef)(31,none);
		ED_STR("call P(R)="); ED_STR(str); trace(get_ed);

	    
	    test_CALL_TOS(32);
		ED_STR("call test_CALL_TOS(32), str="); ED_STR(str); trace(get_ed);
		
		s:=call routineProfile(routineRef)(33,none);
		ED_STR("call P(R)="); ED_STR(s); trace(get_ed);
		
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
