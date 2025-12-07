begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 27";
   const infix(string) PURPOSE      ="SYSTEM Const and Variables";
--
-- ==============================================================================
   
   Boolean verbose=FALSE;
   integer nError ;
   integer traceCase;
   const infix(string) facit(20) = (
      "BEGIN TEST",
      "TEST: curins=NONE",
      "TEST: status=0",
      "TEST: itsize=666",
      "TEST: maxlen=0",
      "TEST: inplth=155",
      "TEST: outlth=360",
      "TEST: bioref=NONE",
      "TEST: maxint=2147483647",
      "TEST: minint=-2147483648",
      "TEST: maxrnk=255",
      "TEST: maxrea=3.40282&+38",
      "TEST: minrea=-3.40282&+38",
      "TEST: maxlrl=1.7976931&+308",
      "TEST: minlrl=-1.7976931&+308",
      "END TEST",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "END TEST"
   );
   
   routine trace; import infix(string) msg;
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

	integer ii;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		ED_STR("TEST: curins="); ED_OADDR(curins); trace(get_ed);
		ED_STR("TEST: status="); ED_INT(status); trace(get_ed);
		ED_STR("TEST: itsize="); ED_INT(itsize); trace(get_ed);
		ED_STR("TEST: maxlen="); ED_SIZE(maxlen); trace(get_ed);
		ED_STR("TEST: inplth="); ED_INT(inplth); trace(get_ed);
		ED_STR("TEST: outlth="); ED_INT(outlth); trace(get_ed);
		ED_STR("TEST: bioref="); ED_OADDR(bioref); trace(get_ed);
		ED_STR("TEST: maxint="); ED_INT(maxint); trace(get_ed);
		ED_STR("TEST: minint="); ED_INT(minint); trace(get_ed);
		ED_STR("TEST: maxrnk="); ED_INT(maxrnk); trace(get_ed);
		ED_STR("TEST: maxrea="); ED_REA(maxrea,6); trace(get_ed);
		ED_STR("TEST: minrea="); ED_REA(minrea,6); trace(get_ed);
		ED_STR("TEST: maxlrl="); ED_LRL(maxlrl,8); trace(get_ed);
		ED_STR("TEST: minlrl="); ED_LRL(minlrl,8); trace(get_ed);
--		ED_STR("TEST: tmp=");    ED_INT(tmp); trace(get_ed);
--		ED_STR("TEST: errorX="); ED_ENTR(errorX,2); trace(get_ed);
--		ED_STR("TEST: allocO="); ED_ENTR(allocO,2); trace(get_ed);
--		ED_STR("TEST: freeO=");  ED_ENTR(freeO,2); trace(get_ed);
	
      EE:trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if ( verbose or ( nError > 0 ) ) then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
