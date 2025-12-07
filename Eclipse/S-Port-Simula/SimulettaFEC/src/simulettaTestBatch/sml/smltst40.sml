begin
   SYSINSERT RT,SYSR,KNWN,UTIL;--,STRG,CENT;
   
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 40";
   const infix(string) PURPOSE      ="Fixup Object Address";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(9) = (
      "BEGIN TEST",
      "x=CSEG_SMLTST40[38]",
      "i=4444",
      "j=8888",
      "CREC1.plv=666",
      "CREC1.next=CSEG_SMLTST40[38]",
      "CREC2.plv=777",
      "CREC2.next=CSEG_SMLTST40[35]",
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

	Visible record REC; begin
		range(0:20) plv;
		ref(REC) next;
	end;

	ref(REC) x = ref(CREC2);
	integer i = 4444;
	const infix(REC) CREC1 =record:REC(plv=666,next=ref(CREC2))
	integer j = 8888;       
	const infix(REC) CREC2 =record:REC(plv=777,next=ref(CREC1))
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
        ED_STR("x="); ED_OADDR(x); trace(get_ed);
		ED_STR("i="); ED_INT(i); trace(get_ed);
		ED_STR("j="); ED_INT(j); trace(get_ed);
 
 		ED_STR("CREC1.plv=");  ED_INT(CREC1.plv); trace(get_ed);
        ED_STR("CREC1.next="); ED_OADDR(CREC1.next); trace(get_ed);
        
 		ED_STR("CREC2.plv=");  ED_INT(CREC2.plv); trace(get_ed);
        ED_STR("CREC2.next="); ED_OADDR(CREC2.next); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
