--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

 -- MAIN Program:
 begin
--    insert envir,modl1;
   SYSINSERT RT,SYSR,KNWN,UTIL;    
   
   ref(REC2) refREC2;
		name() nn(4);
		integer i;

	infix(string) str="ABRA CA DAB";
	
	record REC1; begin
		infix(string) ss(6);
		real rr(4);
		integer ii(4);
		character cc(4);
		size zz(4);
		boolean bb(4);
	  end
	  
	record REC2; begin
		infix(string) ff;
		infix(LEVEL2) lv2;
	  end
	
	record LEVEL2; begin
		character cc2(4);
		infix(LEVEL3) lv3;
	  end
	
	record LEVEL3; begin
		character cc3(4);
	  end
	  
	record REC3; begin
		field() ff(3);
		name() nn(4);
	  end
	  
	record REC4; begin
		label lab(4);
		entry() entr(4);
		ref() pnt(4);
	  end
	  
	entry(routineProfile) routineRef;
	infix(string) res;

   Visible global profile routineProfile;
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
	  
--	infix(REC1) z1=record:REC1(ii=55)
--	infix(REC1) z2=record:REC1(ss=("1.0","222","333"),ii=55,cc=('A','B'),zz=size(REC2),bb=(false,true))

--	infix(REC2) z3=record:REC2(ff="0.0",lv2=record:LEVEL2(cc2=('A','B'),lv3=record:LEVEL3(cc3=('X','Y'))))
	

--	infix(REC3) z4=record:REC3(ff=field(REC2.lv2))
	infix(REC3) z5=record:REC3(ff=field(REC2.lv2.lv3.cc3))
	
	infix(REC3) z6=record:REC3(nn=name(refREC2))
--	infix(REC2) z7=record:REC3(nn=name(refREC2.lv2))
--	infix(REC2) z8=record:REC3(nn=name(refREC2.lv2.cc2))
--	infix(REC3) z9=record:REC3(nn=name(refREC2.lv2.lv3.cc3))
	
--	const infix(REC4) w1=record:REC4(lab=(LL1,LL2,NOWHERE));
--	const infix(REC4) w2=record:REC4(entr=(entry(routineBody),NOBODY));
--	const infix(REC4) w3=record:REC4(pnt=(ref(str),NONE));

LL1:	i:=0;
		i:=z1.ii;
LL2:	
 end;
