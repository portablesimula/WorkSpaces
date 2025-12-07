begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 01";
   const infix(string) PURPOSE      ="Simple Assignments";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(4) = (
      "ABRA CA DAB",
      "ABRA CA DAB",
      "A CA DAB",
--      "A CA-DAB",
      "123456"
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

   infix(string) s,	q;
   character c(6);
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      q:=s:="ABRA CA DAB";
      trace(s);
      trace(q);
      
      s.chradr:=name(var(s.chradr)(3)); s.nchr:=s.nchr-3;  -- Increment 3 char.
      trace(s);
      
--      var(s.chradr)(4) := '-';
--      trace(s);
      
      c(0):='1'; c(1):='2'; c(2):='3';
      c(3):='4'; c(4):='5'; c(5):='6';
      q.chradr:=@c; q.nchr:=6;
      trace(q);

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
