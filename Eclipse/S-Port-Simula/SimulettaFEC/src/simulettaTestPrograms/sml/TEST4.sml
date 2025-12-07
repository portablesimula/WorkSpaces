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
   SYSINSERT RT,SYSR,KNWN,UTIL;    
   	ref() pool;
	size poolsize;
	integer sequ;
	
	ref(REC) r1,r2,r3;
     

	Visible record REC:inst;
	begin integer i,j,k; end;

	Visible record REC2:inst;
	begin integer i,j,k(0); end;

	Visible routine ALLOC;
	import size length;	export ref(inst) ins;
	begin ins:=bio.nxtAdr; bio.nxtAdr:= bio.nxtAdr + length;
		  ins.sort:= S_SUB;
	end;
 
    Visible global profile routineProfile;
    import integer eno; ref(filent) fil;
           export infix(string) res;
    end;
 
    Visible body(routineProfile) rbody;
     -- import integer eno; ref(filent) fil; export infix(string) res;
    begin ed_str("ERROR: "); ed_int(eno);
          res:=get_ed;
    end;

--		poolsize:=SIZEIN(1,sequ);
--		pool:=DWAREA(poolsize,sequ);
--		bio.nxtAdr:=pool;
--		bio.lstAdr:=pool+poolsize;
	
--		r1:=ALLOC(size(REC));
--		r2:=ALLOC(size(REC));
--		r3:=ALLOC(size(REC2:4));
   
	ED_BOOL(true);

%	ED_CHA('!66!');      ED_CHA('G');           
%	ED_INT(0);          ED_INT(444);           
%	ED_REA(0,2);        ED_REA(444.44,2);      
%	ED_LRL(0,2);        ED_LRL(444.44,2);      
%	ED_HEX(0);          ED_HEX(255); 
	          
--	ED_BOOL(false);
	ED_BOOL(true);         
%	ED_SIZE(NOSIZE);    ED_SIZE(size(REC));    
%	ED_OADDR(NONE);     ED_OADDR(R2);          
%	ED_PADDR(NOWHERE);  ED_PADDR(EE);          
--	ED_RADDR(NOBODY);   ED_RADDR(entry(rbody));
%	ED_AADDR(NOFIELD);  ED_AADDR(field(REC.j));
%	ED_GADDR(NONAME);   ED_GADDR(name(r1.k));  

EE:	ED_OUT;
end
