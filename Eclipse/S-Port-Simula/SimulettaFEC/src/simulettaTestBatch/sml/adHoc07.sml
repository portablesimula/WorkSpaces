begin
--   SYSINSERT envir,modl1;
   SYSINSERT RT,SYSR,KNWN,UTIL;    

% Visible routine ED_STR;   import infix(string) str;         begin bio.utpos:=bio.utpos+PUTSTR(REST,str); end;
Visible routine REST; export infix(string) s;
begin s.chradr:=@bio.utbuff(bio.utpos); s.nchr:=utlng-bio.utpos; end;

%	ed_str("ABRA");
	REST;
	
 end;
	 