--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

--Module TEST;
 begin
--  insert envir,modl1;
   SYSINSERT RT,SYSR; --,UTIL;


	Visible record REC:inst;
	begin integer   i;
    	  integer   j;
    	  character c;
    	  variant   integer int;
    	            real    rea;
    	  variant   infix(string) str;
	end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr qua ref(inst);
		  bio.nxtAdr:= ( bio.nxtAdr + length) qua ref(entity);
		  ins.sort:= S_SUB;
	end;

	character c;
	integer i;
	real r;
	long real d;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
	ref(REC) x;
	field(character) f;
	name(ref(REC)) n;	
	field(integer) fnchr;
    field(integer) fj;

 
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool qua ref(entity);
		bio.lstAdr:=(pool+poolsize) qua ref(entity);
	
		r1:=ALLOC(size(REC)) --qua ref(REC);
		
		r1.c:='Q';
		n:=name(r1.c);
		i:=99999;
		x:=n qua ref(REC);
		f:=n qua field(character);
		c:=var(x+f) -- qua character;
--		fnchr:=field(REC.str.nchr);
		fj:=field(REC.j);
		var(r1+fj):=8888;
	
 end;
