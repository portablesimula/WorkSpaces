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

Module MODL02("TST 1.0");
 begin insert envir0,modl01;


 Visible known("TRFREL") TRFREL; --- Text reference relation ---
 import infix(txtqnt) left;
        infix(txtqnt) right;
        boolean code;              -- true: =/=   false: ==
 export boolean rel;
 begin if left.sp <> right.sp then rel:=code;
    elsif left.lp <> right.lp then rel:=code;
    elsif left.ent <> right.ent then rel:=code;
    else rel:=not code endif;
 end;


Visible known("STRIP") STRIP;
import infix(txtqnt) txt; export infix(txtqnt) res;
begin boolean cont; name(character) chaadr;
      res:=txt; cont:=true;
      ---   resl.lp = 0  ===>  res = notext  ===>  res.sp = 0
      if res.lp <> 0 then chaadr:=name(res.ent.cha(res.lp-1)) endif;
      repeat --- res.lp>res.sp ==>  chaadr = name(res.ent.cha(res.lp-1))
            if res.lp <= res.sp then res:=notext; cont:=false;
            elsif var(chaadr) = ' '
            then res.lp:=res.lp - 1;            --  Strip off a blank.
                 chaadr:=name(var(chaadr)(-1)); --  Point to previous ch
            else res.cp:=res.sp; cont:=false endif;
   while cont do endrepeat;
end;
   
end;
