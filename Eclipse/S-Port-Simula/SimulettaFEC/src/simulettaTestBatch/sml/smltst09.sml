begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 09";
   const infix(string) PURPOSE      ="Boolean Operators ( and, or, xor, not )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "a and b=false, b and c=false, c and e=true",
      "a or b=false, b or c=true, c or e=true",
      "a xor b=false, b xor c=true, c xor e=false",
      "i and j=0x0, j and k=0x0, k and l=0xAD",
      "i or j=0x0, j or k=0xFF, k or l=0xFF",
      "i xor j=0x0, j xor k=0xFF, k xor l=0x52",
      "( not l)=0xFFFFFF52",
      "k and ( not l))=0x52",
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

   boolean a,b,c=true,d=false,e=true;
   integer i,j,k=255,l=173;
 
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      ed_str("a and b="); ed_bool(a and b); ed_str(", b and c="); ed_bool(b and c); ed_str(", c and e="); ed_bool(c and e); trace(get_ed);
      ed_str("a or b=");  ed_bool(a or b);  ed_str(", b or c=");  ed_bool(b or c);  ed_str(", c or e=");  ed_bool(c or e);  trace(get_ed);
      ed_str("a xor b="); ed_bool(a xor b); ed_str(", b xor c="); ed_bool(b xor c); ed_str(", c xor e="); ed_bool(c xor e); trace(get_ed);
 
      ed_str("i and j="); ed_hex(i and j); ed_str(", j and k="); ed_hex(j and k); ed_str(", k and l="); ed_hex(k and l); trace(get_ed);
      ed_str("i or j=");  ed_hex(i or j);  ed_str(", j or k=");  ed_hex(j or k);  ed_str(", k or l=");  ed_hex(k or l);  trace(get_ed);
      ed_str("i xor j="); ed_hex(i xor j); ed_str(", j xor k="); ed_hex(j xor k); ed_str(", k xor l="); ed_hex(k xor l); trace(get_ed);
 
	  ed_str("( not l)="); ed_hex( not l); trace(get_ed);
	  ed_str("k and ( not l))="); ed_hex(k and ( not l)); trace(get_ed);
       
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
