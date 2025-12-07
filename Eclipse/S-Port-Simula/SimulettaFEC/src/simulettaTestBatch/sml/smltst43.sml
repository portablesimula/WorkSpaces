begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 43";
   const infix(string) PURPOSE      ="Test name(infix) parameter";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(5) = (
      "BEGIN TEST",
      "dst.sp=4",
      "dst.lp=8",
      "dst.nchr=4",
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

   routine TEST; import name(infix(txtqnt)) dst; begin
   	  integer nchr;
	  ED_STR("dst.sp="); ED_INT(var(dst).sp); trace(get_ed);
	  ED_STR("dst.lp="); ED_INT(var(dst).lp); trace(get_ed);
      nchr := var(dst).lp - var(dst).sp;
	  ED_STR("dst.nchr="); ED_INT(nchr); trace(get_ed);
   end;
   
   infix(txtent) ent1 = record:txtent(cha="   12345");
   infix(txtqnt) txt1;
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
   
      trace("BEGIN TEST");

	  txt1.ent := ref(ent1);
	  txt1.sp  := 4;
	  txt1.lp  := 8;

	  TEST(name(txt1));

      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
