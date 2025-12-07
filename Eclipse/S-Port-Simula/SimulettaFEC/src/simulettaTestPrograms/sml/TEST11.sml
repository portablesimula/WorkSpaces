--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace
begin
--   SYSINSERT envir,modl1;
   SYSINSERT RT,SYSR,KNWN,UTIL;    

	Visible record REC:inst;
	begin integer   i;
    	  integer   j;
    	  variant   integer int;
    	            real    rea;
    	  variant   infix(string) str;
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
--	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
	begin ins:=nxtAdr; --bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	ref() nxtAdr;
	
	ref(REC) r1;
	
	poolsize:=SIZEIN(1,sequ);
	pool:=DWAREA(poolsize,sequ);
	nxtAdr:=pool;
	bio.nxtAdr:=pool;
	bio.lstAdr:=pool+poolsize;
	
	
	r1:=ALLOC(size(REC));
 	r1.i:=4444;
	sequ:=r1.i;
 end;
