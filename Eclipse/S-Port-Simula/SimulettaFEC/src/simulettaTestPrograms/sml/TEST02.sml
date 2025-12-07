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


Visible routine STRBUF;
import integer n; export infix(string) s;
begin s.chradr:=@bio.utbuff; --if bio.utpos <> 0 then ED_OUT endif;
--      if n <> 0 then s.nchr:=n else s.nchr:=utlng endif;
end;
	
  end;
