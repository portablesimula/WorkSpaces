begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

	infix(txtqnt) txt;
	infix(string) str;
	integer i;
	
	infix(txtent:6) systxt = record:txtent(sort=S_TXTENT, misc=1, ncha=6, cha="SYSOUT"); 
	infix(txtqnt) sysid = record:txtqnt(ent=ref(systxt), lp=6);
	
	txt := sysid;
	str := "ABRA CA DAB";
	i := 0;
 end;
	 