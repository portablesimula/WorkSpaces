--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

-- Module TEST2;
 begin
--  insert envir,modl1;
   SYSINSERT RT,SYSR; --,UTIL;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr qua ref(inst);
		  bio.nxtAdr:= ( bio.nxtAdr + length) qua ref(entity);
		  ins.sort:= S_SUB;
	end;

	ref() pool;
	size poolsize;
	integer sequ;

	ref(REC) refREC;

	record REC; begin
		field() ff(3);
		name() nn(4);
	end

	infix(REC) z6; --=record:REC(nn=name(refREC))

		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool qua ref(entity);
		bio.lstAdr:=(pool+poolsize) qua ref(entity);
	
		refREC:=ALLOC(size(REC)) --qua ref(REC);

	z6:=record:REC(nn=name(refREC))
	
	
 end;
