begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 25";
   const infix(string) PURPOSE      ="Editing Utilities";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(14) = (
      "BEGIN TEST",
      "CHA:  B  G",
      "INT:  0  444",
      "REA:  0.0&+00  4.4444&+02",
      "LRL:  0.0&+000  4.4444&+002",
      "HEX:  0x0  0xFF",
      "BOOL: false  true",
      "SIZE: 0  10",
      "OADDR: NONE  POOL_0[10]",
      "PADDR: NOWHERE  PSEG_SMLTST25[311]",
      "RADDR: NOBODY  PSEG_RBODY[0]",
      "AADDR: NOFIELD  FIELD:8",
      "GADDR: GNONE  POOL_0[0][9]",
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
	begin integer i,j,k; end;

	Visible record REC2:inst;
	begin integer i,j,k(0); end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
 
%    Visible global profile routineProfile;
    Visible profile routineProfile;
    import integer eno; ref(filent) fil;
           export infix(string) res;
    end;
 
    Visible body(routineProfile) rbody;
     -- import integer eno; ref(filent) fil; export infix(string) res;
    begin ed_str("ERROR: "); ed_int(eno);
          res:=get_ed;
    end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1,r2,r3;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool;
		bio.lstAdr:=pool+poolsize;
	
		r1:=ALLOC(size(REC));
		r2:=ALLOC(size(REC));
		r3:=ALLOC(size(REC2:4));
	
--		ED_STR("POOLSIZE="); ED_SIZE(poolsize); ED_STR(", SIZE(REC)="); ED_SIZE(size(REC)); ED_STR(", SIZE(inst)="); ED_SIZE(size(inst)); trace(get_ed);
--		dist:=r2 - r1; ED_STR("DIST(r2-r1)="); ED_SIZE(dist); trace(get_ed);
	
--		ED_STR("r2.j="); ED_INT(r2.j); trace(get_ed);

		Ed_STR("CHA:  ");  ED_CHA('!66!');     ED_STR("  "); ED_CHA('G');            trace(get_ed);
		Ed_STR("INT:  ");  ED_INT(0);         ED_STR("  "); ED_INT(444);            trace(get_ed);
		Ed_STR("REA:  ");  ED_REA(0,2);       ED_STR("  "); ED_REA(444.44,5);       trace(get_ed);
		Ed_STR("LRL:  ");  ED_LRL(0,2);       ED_STR("  "); ED_LRL(444.44,5);       trace(get_ed);
		Ed_STR("HEX:  ");  ED_HEX(0);         ED_STR("  "); ED_HEX(255);            trace(get_ed);
		Ed_STR("BOOL: ");  ED_BOOL(false);    ED_STR("  "); ED_BOOL(true);          trace(get_ed);
		Ed_STR("SIZE: ");  ED_SIZE(NOSIZE);   ED_STR("  "); ED_SIZE(size(REC));     trace(get_ed);
		Ed_STR("OADDR: "); ED_OADDR(NONE);    ED_STR("  "); ED_OADDR(R2);           trace(get_ed);
		Ed_STR("PADDR: "); ED_PADDR(NOWHERE); ED_STR("  "); ED_PADDR(EE);           trace(get_ed);
		Ed_STR("RADDR: "); ED_RADDR(NOBODY);  ED_STR("  "); ED_RADDR(entry(rbody)); trace(get_ed);
		Ed_STR("AADDR: "); ED_AADDR(NOFIELD); ED_STR("  "); ED_AADDR(field(REC.j)); trace(get_ed);
		Ed_STR("GADDR: "); ED_GADDR(NONAME);  ED_STR("  "); ED_GADDR(name(r1.k));   trace(get_ed);
	
      EE:trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
