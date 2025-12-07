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
 begin sysinsert envir,modl1;

       -----------------------------------------------------------------------
       ---                                                                 ---
       ---            S I M U L E T T A    T E S T B A T C H               ---
       ---                                                                 ---
       ---                                                                 ---
       -----------------------------------------------------------------------


%title ***   T  r  a  c  i  n  g   ***
	boolean tst;
	integer i,j,k(4);
	size minsize;

	Visible routine observ;
	begin label lsc;
	   lsc:=curins.lsc;
	   curins.lsc:=GTOUTM; call PSIMOB(smb); curins.lsc:=lsc;
	end;
	
	Visible sysroutine("PRINTO")  envir_PRINT;
	import range(1:20) filekey; infix(string) image; integer spc  end;

 Visible known("CMOVE") C_MOVE; import infix(string) src,dst;
 begin integer i,n,rst; i:= -1; n:=src.nchr; rst:=dst.nchr-n;
       if rst < 0 then n:=dst.nchr; rst:=0 endif;
       repeat i:=i+1 while i < n     --- Move characters
       do var(dst.chradr)(i):=var(src.chradr)(i) endrepeat;
       repeat while rst > 0          --- Blank fill
       do var(dst.chradr)(n):=' '; n:=n+1; rst:=rst-1 endrepeat;
 end;
	
    Visible routine MULT; import integer x,y; export integer result;
    begin integer diff; real q1,q2,q3;
          --diff := i - j;
          --res := diff; 
          result := x * y;
     end;
	
    Visible routine SUBTRACT; import integer i,j; export integer res;
    begin integer diff; real r1,r2,r3;
          --diff := i - j;
          --res := diff; 
          --res := i - j;
          res := MULT( i , 2 ) - j;
     end;
	
BB:	j:=i+4;
--	i:=k(2);
--	j:=k(3);
    j:=j/2;
    j:=j-6;
	
--	if i < j then j:=i+4 else i:=j+13;
	    goto EE;
--	endif;
	if tst then j:=i+4 else goto BB; endif;
	
--	envir_PRINT(5,"AAA",10);
	
--	k(3) := SUBTRACT(i,13);
    i := 27;
	k := SUBTRACT(i,13);
EE:	
	observ;
end;
