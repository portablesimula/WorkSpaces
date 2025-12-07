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
 begin insert envir0,modl01;
--      sysinsert rt,sysr,knwn,util;


 Visible global profile PobSML;
 import short integer code;
 end;

 Visible entry(PobSML) obSML;

    routine GET;
    begin boolean cont;
    	  cont:=true;
    	  obSML:=nobody;
    end;
    
    GET;
end
