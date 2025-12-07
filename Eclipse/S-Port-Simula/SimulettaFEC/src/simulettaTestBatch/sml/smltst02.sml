begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 02";
   const infix(string) PURPOSE      ="Test Case Statement";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(9) = (
      "99",
      "10",
      "30",
      "30",
      "30",
      "60",
      "60",
      "99",
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

   routine testCase; import integer code;
   begin infix(string) v;
      case 0:7 (code)
      when 1:     v:="10"
      when 2,3,4: v:="30"
      when 5,6:   v:="60"
      otherwise   v:="99";
      endcase;
      trace(v);       
   end;
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================

      testCase(0);
      testCase(1);
      testCase(2);
      testCase(3);
      testCase(4);
      testCase(5);
      testCase(6);
      testCase(7);
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
