begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 29";
   const infix(string) PURPOSE      ="Object Address Relations ( < , <= , = , >= , > , <> )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(26) = (
      "BEGIN TEST",
      "r1 =  r2  ==> false  GOT: false",
      "r1 >  r2  ==> false  GOT: false",
      "r1 >= r2  ==> false  GOT: false",
      "r1 <> r2  ==> true  GOT: true",
      "r1 <= r2  ==> true  GOT: true",
      "r1 <  r2  ==> true  GOT: true",
      
      "rNone =  r2  ==> false  GOT: false",
      "rNone >  r2  ==> false  GOT: false",
      "rNone >= r2  ==> false  GOT: false",
      "rNone <> r2  ==> true  GOT: true",
      "rNone <= r2  ==> true  GOT: true",
      "rNone <  r2  ==> true  GOT: true",
      
      "pool =  none  ==> false  GOT: false",
      "pool >  none  ==> true  GOT: true",
      "pool >= none  ==> true  GOT: true",
      "pool <> none  ==> true  GOT: true",
      "pool <= none  ==> false  GOT: false",
      "pool <  none  ==> false  GOT: false",
     
      "none =  pool  ==> false  GOT: false",
      "none >  pool  ==> false  GOT: false",
      "none >= pool  ==> false  GOT: false",
      "none <> pool  ==> true  GOT: true",
      "none <= pool  ==> true  GOT: true",
      "none <  pool  ==> true  GOT: true",

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
	begin integer   i;
    	  integer   j;
    	  variant   integer int;
    	            real    rea;
    	  variant   infix(string) str;
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	boolean b;
	ref(REC) r1;
	ref(inst) space;
	ref(REC) r2,r3,rNone;
	size dist;
	size spsize;
	integer i;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool;
		bio.lstAdr:=pool+poolsize;
	
		r1:=ALLOC(size(REC));
		space:=ALLOC(size(inst));
		r2:=ALLOC(size(REC));
	
		b := r1 =  r2; ED_STR("r1 =  r2  ==> "); ED_BOOL(1 =  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := r1 >  r2; ED_STR("r1 >  r2  ==> "); ED_BOOL(1 >  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := r1 >= r2; ED_STR("r1 >= r2  ==> "); ED_BOOL(1 >= 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := r1 <> r2; ED_STR("r1 <> r2  ==> "); ED_BOOL(1 <> 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := r1 <= r2; ED_STR("r1 <= r2  ==> "); ED_BOOL(1 <= 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := r1 <  r2; ED_STR("r1 <  r2  ==> "); ED_BOOL(1 <  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
	
		b := rNone =  r2; ED_STR("rNone =  r2  ==> "); ED_BOOL(0 =  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := rNone >  r2; ED_STR("rNone >  r2  ==> "); ED_BOOL(0 >  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := rNone >= r2; ED_STR("rNone >= r2  ==> "); ED_BOOL(0 >= 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := rNone <> r2; ED_STR("rNone <> r2  ==> "); ED_BOOL(0 <> 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := rNone <= r2; ED_STR("rNone <= r2  ==> "); ED_BOOL(0 <= 2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := rNone <  r2; ED_STR("rNone <  r2  ==> "); ED_BOOL(0 <  2); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
	
		b := pool =  none; ED_STR("pool =  none  ==> "); ED_BOOL(2 =  0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := pool >  none; ED_STR("pool >  none  ==> "); ED_BOOL(2 >  0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := pool >= none; ED_STR("pool >= none  ==> "); ED_BOOL(2 >= 0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := pool <> none; ED_STR("pool <> none  ==> "); ED_BOOL(2 <> 0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := pool <= none; ED_STR("pool <= none  ==> "); ED_BOOL(2 <= 0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := pool <  none; ED_STR("pool <  none  ==> "); ED_BOOL(2 <  0); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
	
		b := none =  pool; ED_STR("none =  pool  ==> "); ED_BOOL(0 =  3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := none >  pool; ED_STR("none >  pool  ==> "); ED_BOOL(0 >  3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := none >= pool; ED_STR("none >= pool  ==> "); ED_BOOL(0 >= 3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := none <> pool; ED_STR("none <> pool  ==> "); ED_BOOL(0 <> 3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := none <= pool; ED_STR("none <= pool  ==> "); ED_BOOL(0 <= 3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
		b := none <  pool; ED_STR("none <  pool  ==> "); ED_BOOL(0 <  3); ED_STR("  GOT: "); ED_BOOL(b); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
