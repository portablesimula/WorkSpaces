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
   SYSINSERT RT,SYSR;
 
 		integer status;
		ref  (bioIns) bioref;
--		infix(bioIns) bio;
		
		record bioIns;
		begin
			  integer utpos;
			  character utbuff(40);
			  integer   pgleft; -- SIMOB-page lines left to write
       		  integer   pgsize; -- SIMOB-page lines per page
       		  boolean   logging;
			  integer   logfile;
		end;
		infix(bioIns) bio;

 Visible sysroutine("OUTIMA")  fUTIMA;
 import integer key; infix(string) img  end;

 Visible known("SYSPRI") SYSPRI; import infix(string) img;
 begin
 end;

 Visible known("SYSPRO") SYSPRO;
 import infix(string) msg,img; export integer filled;
 begin
 end;

Visible routine OLD_ED_OUT;
begin infix(string) im; 
	  integer n;
                if bio.pgleft = 0
                then im.chradr:=@dumbuf; im.nchr:=4;
                     SYSPRO("Enter CR to continue: ",im);
                     status:=0; bio.pgleft:=bio.pgsize;
                else bio.pgleft:=bio.pgleft-1 endif;
end;

 end;
