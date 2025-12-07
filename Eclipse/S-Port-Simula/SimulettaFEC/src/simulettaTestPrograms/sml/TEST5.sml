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
--      sysinsert envir,modl1;
      SYSINSERT RT,SYSR,KNWN,UTIL;    
	  
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

	infix(REC2) z3=record:REC2(ff="0.0")

	ref(REC2) refREC2=ref(z3);
	  
	infix(REC3) z6=record:REC3(nn=name(refREC2))
--	infix(REC2) z7=record:REC3(nn=name(refREC2.lv2))
	infix(REC3) z7=record:REC3(nn=name(refREC2))
--	infix(REC2) z8=record:REC3(nn=name(refREC2.lv2.cc2))
--	infix(REC3) z9=record:REC3(nn=name(refREC2.lv2.lv3.cc3))

--	ED_GADDR(z6.nn);
	if verbose then ED_GADDR(z7.nn); endif
end
