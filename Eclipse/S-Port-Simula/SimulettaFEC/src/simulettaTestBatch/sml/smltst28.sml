begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 28";
   const infix(string) PURPOSE      ="Structured Constants";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(24) = (
      "BEGIN TEST",
      "TEST: z1.ss=(,,) AND: z2.ss=(1.0,222,333)",
      "TEST: z1.rr=(0.00,0.00) AND: z2.rr=(0.00,3.14)",
      "TEST: z1.ii=(55,0) AND: z2.ii=(0,888)",
      "TEST: z1.cc=(0,0) AND: z2.cc=(A,B)",
      "TEST: z1.zz=(0,0) AND: z2.zz=(9,0)",
      "TEST: z1.bb=(false,false) AND: z2.bb=(false,true)",
      "TEST: z3.ff=0.0,TEST: z3.lv2=( (A,B), lv3=(X,Y) )",
      "TEST: z4.ff=(FIELD:3,NOFIELD)",
      "TEST: z5.ff=(FIELD:5,NOFIELD)",
      "TEST: z6.nn=(CSEG_SMLTST28[141][0],GNONE,GNONE)",
      "TEST: z7.nn=(CSEG_SMLTST28[141][3],GNONE,GNONE)",
      "TEST: z8.nn=(CSEG_SMLTST28[141][3],GNONE,GNONE)",
      "TEST: z9.nn=(CSEG_SMLTST28[141][5],GNONE,GNONE)",
      "TEST: w1.lab=(PSEG_SMLTST28[15],PSEG_SMLTST28[22],NOWHERE)",
      "TEST: w1.entr=(NOBODY,NOBODY,NOBODY)",
      "TEST: w1.pnt=(NONE,NONE,NONE)",
      "TEST: w2.lab=(NOWHERE,NOWHERE,NOWHERE)",
      "TEST: w2.entr=(PSEG_ROUTINEBODY[0],NOBODY,NOBODY)",
      "TEST: w2.pnt=(NONE,NONE,NONE)",
      "TEST: w3.lab=(NOWHERE,NOWHERE,NOWHERE)",
      "TEST: w3.entr=(NOBODY,NOBODY,NOBODY)",
      "TEST: w3.pnt=(DSEG_SMLTST28[7],NONE,NONE)",
      "END TEST"
   );
   
   routine trace; import infix(string) msg;
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

	
	record REC1; begin
		infix(string) ss(3);
		real rr(2);
		integer ii(2);
		character cc(2);
		size zz(2);
		boolean bb(2);
	  end
	  
	record REC2; begin
		infix(string) ff;
		infix(LEVEL2) lv2;
	  end
	
	record LEVEL2; begin
		character cc2(2);
		infix(LEVEL3) lv3;
	  end
	
	record LEVEL3; begin
		character cc3(4);
	  end
	  
	record REC3; begin
		field() ff(2);
		name() nn(3);
	  end
	  
	record REC4; begin
		label lab(3);
		entry() entr(3);
		ref() pnt(3);
	  end
	  
	entry(routineProfile) routineRef;
	infix(string) res;

%   Visible global profile routineProfile;
   Visible profile routineProfile;
    import integer eno; ref() fil;
           export infix(string) res;
   end;
	  
   	Visible body(routineProfile) routineBody;
    	-- import integer eno; ref(filent) fil; export infix(string) res;
    	begin
    	 --ed_str("ERROR: ");
    	 -- ed_int(eno);
    	 --     res:=get_ed;
    	end;
	  
	infix(REC1) z1=record:REC1(ii=55)
	infix(REC1) z2=record:REC1(rr=(0.0,3.14),ss=("1.0","222","333"),ii=(0,888),cc=('A','B'),zz=size(REC2),bb=(false,true))

	infix(REC2) z3=record:REC2(ff="0.0",lv2=record:LEVEL2(cc2=('A','B'),lv3=record:LEVEL3(cc3=('X','Y'))))
	

	infix(REC3) z4=record:REC3(ff=field(REC2.lv2))
	infix(REC3) z5=record:REC3(ff=field(REC2.lv2.lv3.cc3))
	
	ref(REC2) refREC2=ref(z3);
	
	infix(REC3) z6=record:REC3(nn=name(refREC2))
	infix(REC3) z7=record:REC3(nn=name(refREC2.lv2))
	infix(REC3) z8=record:REC3(nn=name(refREC2.lv2.cc2))
	infix(REC3) z9=record:REC3(nn=name(refREC2.lv2.lv3.cc3))
	
	const infix(REC4) w1=record:REC4(lab=(LL1,LL2,NOWHERE));
	const infix(REC4) w2=record:REC4(entr=(entry(routineBody),NOBODY));
	const infix(REC4) w3=record:REC4(pnt=(ref(str),NONE));

	integer i;
	infix(string) str; --="ABRACADAB";

		i:=99;
LL1:	i:=0;
		i:=z1.ii;
LL2:	

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
        ED_STR("TEST: z1.ss=("); ED_STR(z1.ss); ED_CHA(','); ED_STR(z1.ss(1)); ED_CHA(','); ED_STR(z1.ss(2)); ED_CHA(')');
        ED_STR(" AND: z2.ss=("); ED_STR(z2.ss); ED_CHA(','); ED_STR(z2.ss(1)); ED_CHA(','); ED_STR(z2.ss(2)); ED_CHA(')'); trace(get_ed);
        
        ED_STR("TEST: z1.rr=("); ED_FIX(z1.rr,2); ED_CHA(','); ED_FIX(z1.rr(1),2); ED_CHA(')');
        ED_STR(" AND: z2.rr=("); ED_FIX(z2.rr,2); ED_CHA(','); ED_FIX(z2.rr(1),2); ED_CHA(')'); trace(get_ed);
        
        ED_STR("TEST: z1.ii=("); ED_INT(z1.ii); ED_CHA(','); ED_INT(z1.ii(1)); ED_CHA(')');
        ED_STR(" AND: z2.ii=("); ED_INT(z2.ii); ED_CHA(','); ED_INT(z2.ii(1)); ED_CHA(')'); trace(get_ed);
        
        ED_STR("TEST: z1.cc=("); ED_INT(z1.cc qua integer); ED_CHA(','); ED_INT(z1.cc(1) qua integer); ED_CHA(')');
        ED_STR(" AND: z2.cc=("); ED_CHA(z2.cc); ED_CHA(','); ED_CHA(z2.cc(1)); ED_CHA(')'); trace(get_ed);
        
        ED_STR("TEST: z1.zz=("); ED_SIZE(z1.zz); ED_CHA(','); ED_SIZE(z1.zz(1)); ED_CHA(')');
        ED_STR(" AND: z2.zz=("); ED_SIZE(z2.zz); ED_CHA(','); ED_SIZE(z2.zz(1)); ED_CHA(')'); trace(get_ed);
        
        ED_STR("TEST: z1.bb=("); ED_BOOL(z1.bb); ED_CHA(','); ED_BOOL(z1.bb(1)); ED_CHA(')');
        ED_STR(" AND: z2.bb=("); ED_BOOL(z2.bb); ED_CHA(','); ED_BOOL(z2.bb(1)); ED_CHA(')'); trace(get_ed);
        
		ED_STR("TEST: z3.ff="); ED_STR(z3.ff); ED_CHA(',');
		ED_STR("TEST: z3.lv2=( ("); ED_CHA(z3.lv2.cc2); ED_CHA(','); ED_CHA(z3.lv2.cc2(1)); ED_STR("), lv3=(");
		ED_CHA(z3.lv2.lv3.cc3); ED_CHA(','); ED_CHA(z3.lv2.lv3.cc3(1)); ED_STR(") )"); trace(get_ed);
      
        ED_STR("TEST: z4.ff=("); ED_AADDR(z4.ff); ED_CHA(','); ED_AADDR(z4.ff(1)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: z5.ff=("); ED_AADDR(z5.ff); ED_CHA(','); ED_AADDR(z5.ff(1)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: z6.nn=("); ED_GADDR(z6.nn); ED_CHA(','); ED_GADDR(z6.nn(1)); ED_CHA(','); ED_GADDR(z6.nn(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: z7.nn=("); ED_GADDR(z7.nn); ED_CHA(','); ED_GADDR(z7.nn(1)); ED_CHA(','); ED_GADDR(z7.nn(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: z8.nn=("); ED_GADDR(z8.nn); ED_CHA(','); ED_GADDR(z8.nn(1)); ED_CHA(','); ED_GADDR(z8.nn(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: z9.nn=("); ED_GADDR(z9.nn); ED_CHA(','); ED_GADDR(z9.nn(1)); ED_CHA(','); ED_GADDR(z9.nn(2)); ED_CHA(')'); trace(get_ed);
      
        ED_STR("TEST: w1.lab=("); ED_PADDR(w1.lab); ED_CHA(','); ED_PADDR(w1.lab(1)); ED_CHA(','); ED_PADDR(w1.lab(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w1.entr=("); ED_RADDR(w1.entr); ED_CHA(','); ED_RADDR(w1.entr(1)); ED_CHA(','); ED_RADDR(w1.entr(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w1.pnt=("); ED_OADDR(w1.pnt); ED_CHA(','); ED_OADDR(w1.pnt(1)); ED_CHA(','); ED_OADDR(w1.pnt(2)); ED_CHA(')'); trace(get_ed);
      
        ED_STR("TEST: w2.lab=("); ED_PADDR(w2.lab); ED_CHA(','); ED_PADDR(w2.lab(1)); ED_CHA(','); ED_PADDR(w2.lab(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w2.entr=("); ED_RADDR(w2.entr); ED_CHA(','); ED_RADDR(w2.entr(1)); ED_CHA(','); ED_RADDR(w2.entr(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w2.pnt=("); ED_OADDR(w2.pnt); ED_CHA(','); ED_OADDR(w2.pnt(1)); ED_CHA(','); ED_OADDR(w2.pnt(2)); ED_CHA(')'); trace(get_ed);
       
        ED_STR("TEST: w3.lab=("); ED_PADDR(w3.lab); ED_CHA(','); ED_PADDR(w3.lab(1)); ED_CHA(','); ED_PADDR(w3.lab(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w3.entr=("); ED_RADDR(w3.entr); ED_CHA(','); ED_RADDR(w3.entr(1)); ED_CHA(','); ED_RADDR(w3.entr(2)); ED_CHA(')'); trace(get_ed);
        ED_STR("TEST: w3.pnt=("); ED_OADDR(w3.pnt); ED_CHA(','); ED_OADDR(w3.pnt(1)); ED_CHA(','); ED_OADDR(w3.pnt(2)); ED_CHA(')'); trace(get_ed);
	
      EE:trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
