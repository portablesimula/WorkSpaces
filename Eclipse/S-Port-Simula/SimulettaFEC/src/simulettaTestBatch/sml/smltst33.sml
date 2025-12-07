begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 33";
   const infix(string) PURPOSE      ="GOTO fixup'address";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(5) = (
      "BEGIN TEST",
      "AT LL1",
      "AT LL2",
      "AT LL3",
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


	integer i;
	label lab;

	record REC4; begin label lab(4); end
	
	const infix(REC4) w1=record:REC4(lab=(LL1,LL2,NOWHERE,LL1));


   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
--		if w1.lab ne NOWHERE then goto w1.lab; endif
		if w1.lab = LL1 then goto w1.lab; endif
		trace("IMPOSSIBLE-1");
LL1:	
		trace("AT LL1");
		goto LL2;
		trace("IMPOSSIBLE-2");
LL2:	
		trace("AT LL2");
		lab:=LL3;
		goto lab;
		trace("IMPOSSIBLE-3");
LL3:
		trace("AT LL3");

	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
