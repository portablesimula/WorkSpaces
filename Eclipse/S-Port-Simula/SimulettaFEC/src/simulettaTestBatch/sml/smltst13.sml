begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 13";
   const infix(string) PURPOSE      ="General Reference Expression";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(10) = (
      "BEGIN TEST",
      "var(RR+fj)=111",
      "var(RR+fj)=222",
      "var(NQ+fm)(2)=333",
      "var(NQ+fm)(2)=444",
      "Updated IR.str=ABRA",
      "Updated nxt=ABRA",
      "IQ.b=777",
      "var(NQ+fb)=666",
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
    	  integer   m(8);
    	  variant   integer a,b,c;
    	  variant   long real lrl;
 	end;

    infix(Q) IQ;
    infix(R) IR;
    infix(string) abr="ABRA";
    infix(string) nxt;

    ref(R) RR;
    name(infix(Q)) NQ;

	field(integer) fi;
    field(integer) fj;
    field(integer) fb;
    field(real) frea;
    field(infix(string)) fstr;
	field(name(character)) fchradr;
	field(integer) fnchr;
	field(integer) fk;
    field(integer) fm;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      RR:=ref(IR);
      NQ:=name(IQ);
      fj:=field(R.j);
      fstr:=field(R.str);
      fchradr:=field(R.str.chradr);
      fnchr:=field(R.str.nchr);
      fm:=field(Q.m);
      fb:=field(Q.b);
      
      var(RR+fj):=111; ed_str("var(RR+fj)="); ed_int(IR.j); trace(get_ed);
      IR.j:=222; ed_str("var(RR+fj)="); ed_int(var(RR+fj)); trace(get_ed);

      var(NQ+fm)(2):=333; ed_str("var(NQ+fm)(2)="); ed_int(IQ.m(2)); trace(get_ed);
      IQ.m(2):=444; ed_str("var(NQ+fm)(2)="); ed_int(var(NQ+fm)(2)); trace(get_ed);
      
      var(RR+fnchr):=abr.nchr; var(RR+fchradr):=abr.chradr; ed_str("Updated IR.str="); ed_str(IR.str); trace(get_ed);
      nxt.nchr:=var(RR+fnchr); nxt.chradr:=var(RR+fchradr); ed_str("Updated nxt="); ed_str(IR.str); trace(get_ed);
      
      var(NQ+fb):=777; ed_str("IQ.b="); ed_int(IQ.b); trace(get_ed);
      var(NQ+fb):=666; ed_str("var(NQ+fb)="); ed_int(var(NQ+fb)); trace(get_ed);
 
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
