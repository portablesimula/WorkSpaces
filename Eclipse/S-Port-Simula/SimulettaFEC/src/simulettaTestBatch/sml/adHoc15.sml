begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

	Visible record REC:inst; begin
%		long
		 real rnk;
	end;

	Visible routine xALLOC;
	import size length;	export ref(inst) ins;
	begin ins := bio.nxtAdr;
		  bio.nxtAdr := bio.nxtAdr + length;
		  ins.sort := S_SUB;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
%	long
	 real time;
	integer i,k,l,m;
	infix(String) nam, nux, nuy;

    sequ := 1;   
   	poolsize:=SIZEIN(1,sequ);
	pool:=DWAREA(poolsize,sequ);
	bio.nxtAdr:=pool;
	bio.lstAdr:=pool+poolsize;
	
	r1 := xALLOC(size(REC));
		
	r1.rnk := 44.0;
	time := 200.0;
	
	time := r1.rnk := r1.rnk+time;
	
	ed_str("time="); ed_fix(time, 2); ed_out;
	ed_str("r1.rnk="); ed_fix(r1.rnk, 2); ed_out;
	
 end;
