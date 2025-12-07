begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 12";
   const infix(string) PURPOSE      ="Size Relations ( < , <= , = , >= , > , <> )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(9) = (
      "BEGIN TEST",
      "size(R)==5, size(Q:6)==12",
      "(a<b)==false, (b<c)==true, (c<d)==false",
      "(a<=b)==true, (b<=c)==true, (c<=d)==false",
      "(a=b)==true, (b=c)==false, (c=d)==false",
      "(a>=b)==true(b>=c)==false, (c>=d)==true",
      "(a>b)==false(b>c)==false, (c>d)==false",
      "(a<>b)==false(b<>c)==true, (c<>d)==true",
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

    Visible record R;
	begin integer   i;
    	  integer   j;
    	  variant   integer  int;
    	  variant   real rea;
    	  variant   infix(string) str;
	end;

    Visible record Q:R;
	begin integer   k;
    	  integer   m(0);
 	end;

   size a,b,c=size(R),d=nosize,e;
   e:=size(Q,6);

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      ed_str("size(R)=="); ed_size(c); ed_str(", size(Q:6)=="); ed_size(e); trace(get_ed);
 
      ed_str("(a<b)=="); ed_bool(a<b); ed_str(", (b<c)=="); ed_bool(b<c); ed_str(", (c<d)=="); ed_bool(c<d); trace(get_ed);
      ed_str("(a<=b)=="); ed_bool(a<=b); ed_str(", (b<=c)=="); ed_bool(b<=c); ed_str(", (c<=d)=="); ed_bool(c<=d); trace(get_ed);
      ed_str("(a=b)=="); ed_bool(a=b); ed_str(", (b=c)=="); ed_bool(b=c); ed_str(", (c=d)=="); ed_bool(c=d); trace(get_ed);
      
      ed_str("(a>=b)=="); ed_bool(a>=b); ed_str("(b>=c)=="); ed_bool(b>=c); ed_str(", (c>=d)=="); ed_bool(c>=d); trace(get_ed);
      ed_str("(a>b)=="); ed_bool(a>b); ed_str("(b>c)=="); ed_bool(b>c); ed_str(", (c>d)=="); ed_bool(c=d); trace(get_ed);
      ed_str("(a<>b)=="); ed_bool(a<>b); ed_str("(b<>c)=="); ed_bool(b<>c); ed_str(", (c<>d)=="); ed_bool(c<>d); trace(get_ed);
 
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
