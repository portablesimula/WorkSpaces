begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

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
	integer a,b;
 
		poolsize:=SIZEIN(1,sequ);
		pool:=DWAREA(poolsize,sequ);
		bio.nxtAdr:=pool qua ref(entity);
		bio.lstAdr:=(pool+poolsize) qua ref(entity);
	
		x := ALLOC(size(REC));
%		r1.suc:=ALLOC(size(REC));
%		x:=r1.suc; w:=x; x.suc:=r1;
		
%		x.j:=4444; ED_STR("r1.suc qua REC.j="); ED_INT(w.j); trace(get_ed);
%		r1.suc qua REC.j:=6666; ED_STR("r1.suc qua REC.j="); ED_INT(r1.suc qua REC.j); trace(get_ed);
		
%		x.i(0):=111;
%		x.i(1) := x.i(2) := 222;
		a := 11; b := -6;
		x.i(a+b) := x.i(a+b+33) := 222;
%		x.i(2):=333; x.i(3):=444; x.i(4):=555;

 end;
	 