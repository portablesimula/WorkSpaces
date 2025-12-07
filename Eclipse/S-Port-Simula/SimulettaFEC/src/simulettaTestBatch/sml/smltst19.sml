begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 19";
   const infix(string) PURPOSE      ="Remote Access";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(16) = (
      "BEGIN TEST",
      "r1.suc qua REC.j=4444",
      "r1.suc qua REC.j=6666",
      "r1.suc qua REC.j=555",
      "r1.suc qua REC.i=111",
      "r1.suc qua REC.i(0)=111",
      "r1.suc qua REC.i(1)=222",
      "r1.suc qua REC.i(2)=333",
      "r1.suc qua REC.i(3)=444",
      "r1.suc qua REC.i(4)=555",
      "var(n)(0)=333",
      "var(n)(1)=444",
      "var(n)(2)=555",
      "x.str=ABC",
      "r1.str=ABC",
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

	Visible record REC:inst;
	begin integer  i(4);
    	  integer  j;
    	  ref(inst) suc;
    	  infix(string) str;
    	  character c(3);
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr qua ref(inst);
		  bio.nxtAdr:= ( bio.nxtAdr + length) qua ref(entity);
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
	ref(REC) x,w;
%	name(ref(REC)) n;	
	name(integer) n;	

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
   
      trace("BEGIN TEST");
 
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool qua ref(entity);
		bio.lstAdr:=(pool+poolsize) qua ref(entity);
	
		r1:=ALLOC(size(REC));
		r1.suc:=ALLOC(size(REC));
		x:=r1.suc; w:=x; x.suc:=r1;
		
		x.j:=4444; ED_STR("r1.suc qua REC.j="); ED_INT(w.j); trace(get_ed);
		r1.suc qua REC.j:=6666; ED_STR("r1.suc qua REC.j="); ED_INT(r1.suc qua REC.j); trace(get_ed);
		
		x.i(0):=111; x.i(1):=222; x.i(2):=333; x.i(3):=444; x.i(4):=555;
		ED_STR("r1.suc qua REC.j="); ED_INT(r1.suc qua REC.j); trace(get_ed);
		ED_STR("r1.suc qua REC.i="); ED_INT(r1.suc qua REC.i); trace(get_ed);
		ED_STR("r1.suc qua REC.i(0)="); ED_INT(r1.suc qua REC.i(0)); trace(get_ed);
		ED_STR("r1.suc qua REC.i(1)="); ED_INT(r1.suc qua REC.i(1)); trace(get_ed);
		ED_STR("r1.suc qua REC.i(2)="); ED_INT(r1.suc qua REC.i(2)); trace(get_ed);
		ED_STR("r1.suc qua REC.i(3)="); ED_INT(r1.suc qua REC.i(3)); trace(get_ed);
		ED_STR("r1.suc qua REC.i(4)="); ED_INT(r1.suc qua REC.i(4)); trace(get_ed);
		
		n:=name(r1.suc qua REC.i(2));
		ED_STR("var(n)(0)="); ED_INT(var(n)(0)); trace(get_ed);
		ED_STR("var(n)(1)="); ED_INT(var(n)(1)); trace(get_ed);
		ED_STR("var(n)(2)="); ED_INT(var(n)(2)); trace(get_ed);
		
		x.c(0):='A'; x.c(1):='B'; x.c(2):='C';	
		x.str.chradr:=name(x.c); x.str.nchr:=3;
		ED_STR("x.str="); ED_STR(x.str); trace(get_ed);
		
		r1.str.chradr:=name(r1.c); r1.str.nchr:=3;
		C_MOVE(x.str,r1.str);
		ED_STR("r1.str="); ED_STR(r1.str); trace(get_ed);
		
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
