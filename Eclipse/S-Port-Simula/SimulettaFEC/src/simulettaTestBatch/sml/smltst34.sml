begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 34";
   const infix(string) PURPOSE      ="Text quant relations ( =, <> )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(6) = (
      "BEGIN TEST",
      "RIKTIG: notext = notext",
      "RIKTIG: text = text",
      "RIKTIG: text ne notext",
      "RIKTIG: notext ne text",
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


	const infix(txtent:10) ident = record:txtent(sl=none, sort=S_TXTENT, misc=1, ncha=10, cha=('A','B','C','D','E','F') );
 	const infix(txtqnt) text=record:txtqnt(ent=ref(ident), cp=0, sp=0, lp = 10);


   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		if notext = notext then trace("RIKTIG: notext = notext") else trace("FEIL: notext ne notext") endif; 

		if text = text     then trace("RIKTIG: text = text") else trace("FEIL: text ne text") endif; 

		if text ne notext  then trace("RIKTIG: text ne notext") else trace("FEIL: text = notext") endif; 

		if notext ne text  then trace("RIKTIG: notext ne text") else trace("FEIL: notext = text") endif; 
		
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
