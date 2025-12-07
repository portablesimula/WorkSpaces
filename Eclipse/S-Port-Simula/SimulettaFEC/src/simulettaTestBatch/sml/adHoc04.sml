begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

   infix(string) s,	q;
 
      q:=s:="ABRA CA DAB";
      
      s.chradr:=name(var(s.chradr)(3));
%      s.nchr:=s.nchr-3;  -- Increment 3 char.

 end;
	 