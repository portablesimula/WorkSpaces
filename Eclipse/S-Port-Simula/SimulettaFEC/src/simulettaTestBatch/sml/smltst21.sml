begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 21";
   const infix(string) PURPOSE      ="Call: Profile(Body)";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "call P(R)=ERROR: 34",
      "END TEST",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
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

%    	Visible global profile routineProfile;
    	Visible profile routineProfile;
    	import integer eno; ref(filent) fil;
    	       export infix(string) res;
    	end;

    	Visible routine test_CALL_TOS; export infix(string) res;
    	begin res := call routineProfile(routineRef)(34,none) end;
 
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

	    routineRef:=entry(routineBody);
	    s:=test_CALL_TOS;
		ED_STR("call P(R)="); ED_STR(s); trace(get_ed);
		
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
