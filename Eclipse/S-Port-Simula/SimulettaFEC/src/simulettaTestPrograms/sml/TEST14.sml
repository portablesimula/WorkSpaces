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
--   INSERT envir,modl1;
   SYSINSERT RT,SYSR,KNWN,CENT; --,ARR,FIL,EDIT,FORM,LIBR,SMST,SML,MNTR;

   Visible entry(Palloc) XXX system "ALLOCO";
 
   XXX := entry(xalloc);

 end;
