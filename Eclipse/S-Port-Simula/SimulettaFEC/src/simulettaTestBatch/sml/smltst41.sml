begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 41";
   const infix(string) PURPOSE      ="Shift opr: LSHIFTL, RSHIFTL, LSHIFTA, RSHIFTA";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(26) = (
      "BEGIN TEST",
      "1 LSHIFTL 2=4",
      "1 LSHIFTA 2=4",
      "4 RSHIFTL 2=1",
      "4 RSHIFTA 2=1",
      "-1 LSHIFTL 2=-4",
      "-1 LSHIFTA 2=0xFFFFFFFC",
      "-4 RSHIFTL 2=0x3FFFFFFF",
      "-4 RSHIFTA 2=-1",
      "0 LSHIFTL 2=0",
      "0 LSHIFTA 2=0",
      "0 RSHIFTL 2=0",
      "0 RSHIFTA 2=0",
      "1 LSHIFTL 0=1",
      "1 LSHIFTA 0=1",
      "4 RSHIFTL 0=4",
      "4 RSHIFTA 0=4",
      "END TEST"
   );
   
   Visible routine trace; import infix(string) msg;
   begin
      if verbose then ed_str(msg); ed_str("  TEST AGAINST FACIT:  "); prt(facit(traceCase)); endif;
      if( not STREQL(msg,facit(traceCase))) then
         nError:=nError+1; prt(" ");
         ed_str("ERROR in Case "); ed_int(traceCase); ed_out;
         ed_str("Trace: "); ed_str("|"); ed_str(msg); prt("|");
         ed_str("Facit: "); ed_str(facit(traceCase)); prt("|");
      endif;
      traceCase:=traceCase+1;
   end;

   integer i,j;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      i := 1; j := 2; ed_str("1 LSHIFTL 2="); ed_int(i LSHIFTL j); trace(get_ed);
      i := 1; j := 2; ed_str("1 LSHIFTA 2="); ed_int(i LSHIFTA j); trace(get_ed);
      i := 4; j := 2; ed_str("4 RSHIFTL 2="); ed_int(i RSHIFTL j); trace(get_ed);
      i := 4; j := 2; ed_str("4 RSHIFTA 2="); ed_int(i RSHIFTA j); trace(get_ed);
      
      i := -1; j := 2; ed_str("-1 LSHIFTL 2="); ed_int(i LSHIFTL j); trace(get_ed);
      i := -1; j := 2; ed_str("-1 LSHIFTA 2="); ed_hex(i LSHIFTA j); trace(get_ed);
      i := -4; j := 2; ed_str("-4 RSHIFTL 2="); ed_hex(i RSHIFTL j); trace(get_ed);
      i := -4; j := 2; ed_str("-4 RSHIFTA 2="); ed_int(i RSHIFTA j); trace(get_ed);
      
      i := 0; j := 2; ed_str("0 LSHIFTL 2="); ed_int(i LSHIFTL j); trace(get_ed);
      i := 0; j := 2; ed_str("0 LSHIFTA 2="); ed_int(i LSHIFTA j); trace(get_ed);
      i := 0; j := 2; ed_str("0 RSHIFTL 2="); ed_int(i RSHIFTL j); trace(get_ed);
      i := 0; j := 2; ed_str("0 RSHIFTA 2="); ed_int(i RSHIFTA j); trace(get_ed);
      
      i := 1; j := 0; ed_str("1 LSHIFTL 0="); ed_int(i LSHIFTL j); trace(get_ed);
      i := 1; j := 0; ed_str("1 LSHIFTA 0="); ed_int(i LSHIFTA j); trace(get_ed);
      i := 4; j := 0; ed_str("4 RSHIFTL 0="); ed_int(i RSHIFTL j); trace(get_ed);
      i := 4; j := 0; ed_str("4 RSHIFTA 0="); ed_int(i RSHIFTA j); trace(get_ed);
      
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
