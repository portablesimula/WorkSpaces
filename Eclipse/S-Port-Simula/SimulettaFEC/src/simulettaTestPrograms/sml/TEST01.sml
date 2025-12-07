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
    insert envir0;
	
	record REC0; begin
		integer ii(3);
		real rr(4);
		long real lr(2);
		character cc(3);
		size zz(2);
		boolean bb(4);
		field() ff(5);
		ref(REC0) rf(4);
		name() rlf(5);
	end
	  
--	ref(REC0) X;
	infix(REC0) inf;
	
--	 Visible const infix(string)
--       nostring=record:string(nchr=0,chradr=noname);

 		integer ii(3);
		real rr(4);
		long real lr(2);
		character cc(3);
		size zz(2);
		boolean bb(4);
		field() ff(5);
		ref(REC0) rf(4);
		name() rlf(5);


 Visible known("BOUTXT") BOUTXT;
 import infix(string) str;
 begin integer i,j;
 
 		integer ii(3);
		real rr(4);
		long real lr(2);
		character cc(3);
		size zz(2);
		boolean bb(4);
		field() ff(5);
		ref(REC0) rf(4);
		name() rlf(5);
 
       i:=0;
--       repeat while i < str.nchr do
              j:=var(str.chradr)(i) qua integer; i:=i+1;
--              if status<>0 then goto E endif;
--       endrepeat;
  end;
	 
--	infix(REC0) z1=record:REC0(ii=55,rr=2.4,cc=('A','B','C'),zz=size(REC0),bb=(false,true))
--	infix(REC0) z1=record:REC0(ff=(field(REC0.cc),nofield,field(REC0.zz)))  -- AADDR
--	infix(REC0) z1=record:REC0(rf=(ref(inf),none))                        -- OADDR
--	infix(REC0) z1=record:REC0(rlf=(name(inf),noname,name(inf.rlf)))        -- GADDR
		
		BOUTXT("ABRA");
		BOUTXT("CADAB");
  end;
