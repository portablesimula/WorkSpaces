begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

	Visible record REC:inst; begin
		integer   i(4);
		integer   j;
    	variant   integer int;
    	          real    rea;
    	variant   infix(string) str;
	end;

	Visible routine xALLOC;
	import size length;	export ref(inst) ins;
	begin ins := bio.nxtAdr;
		  bio.nxtAdr := bio.nxtAdr + length;
		  ins.sort := S_SUB;
	end;
	
	routine TEST; import ref(REC) x; export ref(REC) y; begin
		y := x;
	end;
	
	routine TEST2; import infix(string) x; export infix(string) y; begin
		y := x;
	end;
       
	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1;
	integer i,k,l,m;
	infix(String) nam, nux, nuy;

    sequ := 1;   
   	poolsize:=SIZEIN(1,sequ);
	pool:=DWAREA(poolsize,sequ);
	bio.nxtAdr:=pool;
	bio.lstAdr:=pool+poolsize;
	
	r1 := xALLOC(size(REC));
		
	r1.j := 4444;
		
	i := r1.j;
	
	k := TEST(r1).j;
	
%	nam := "ABRA";
%	r1.str := nam;
%	nux := r1.str;
%	nuy := TEST2(nam);
	
 end;
