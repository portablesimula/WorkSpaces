--% %pass 2 input = 4
--% %pass 2 trace = 4
--% %pass 2 modtrc = 4

--%PASS 1 INPUT=5 -- Input Trace
--%PASS 1 OUTPUT=1 -- Output Trace
--%PASS 1 MODTRC=4 -- Module I/O Trace
--%PASS 1 TRACE=4 -- Trace level
--%PASS 2 INPUT=1 -- Input Trace
%PASS 2 OUTPUT=1 -- S-Code Output Trace
--%PASS 2 MODTRC=1 -- Module I/O Trace
--%PASS 2 TRACE=1 -- Trace level
--%TRACE 2 -- Output Trace

Module MODL01("TST 1.0");
 begin insert envir0;

       -----------------------------------------------------------------------
       ---                                                                 ---
       ---            S I M U L E T T A    T E S T B A T C H               ---
       ---                                                                 ---
       ---                                                                 ---
       -----------------------------------------------------------------------

-- Visible sysroutine("GTOUTM") GTOUTM;
 --- psc must always correspond to an address in the user program
 --- GTOUTM is not called from the exception handler
 --- Status must not be changed
-- export label psc  end;


 Visible sysroutine("STREQL") STREQL;
 import infix(string) str1,str2; export boolean res  end;

Visible routine ED_STR; import infix(string) str;
begin integer i,n;
      n:=str.nchr-1;
end;


 Visible sysroutine("PRINTO") PRINTO;
 import integer key; infix(string) image; integer spc  end;

-- Visible known("SYSPRI") SYSPRI; import infix(string) img;
-- begin PRINTO(0,img,1) end;

 Visible body(PRF) BDY;
 begin
 end;
   
end;
