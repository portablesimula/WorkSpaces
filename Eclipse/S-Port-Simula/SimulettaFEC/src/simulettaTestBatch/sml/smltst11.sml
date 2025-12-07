begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 11";
   const infix(string) PURPOSE      ="Boolean Relations ( = , <> )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(4) = (
      "BEGIN TEST",
      "(a=b)==true, (b=c)==false, (c=d)==false",
      "(a<>b)==false, (b<>c)==true, (c<>d)==true",
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

   boolean a,b,c=true,d=false;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      ed_str("(a=b)==");  ed_bool(a=b);  ed_str(", (b=c)==");  ed_bool(b=c);  ed_str(", (c=d)==");  ed_bool(c=d);  trace(get_ed);
      ed_str("(a<>b)=="); ed_bool(a<>b); ed_str(", (b<>c)=="); ed_bool(b<>c); ed_str(", (c<>d)=="); ed_bool(c<>d); trace(get_ed);
 
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
