begin
   SYSINSERT RT,SYSR,KNWN,UTIL
   ,strg,cent,cint,arr,form,libr,fil,smst,sml,edit,mntr;
      
 Routine xOUTTXT;
 import ref(filent) fil; infix(txtqnt) txt;
 begin infix(txtqnt) img;           --  Local copy here for efficiency.
       infix(string) src;           --  Copy from this string.
       infix(string) dst;           --  Copy to this string.
       integer imlength,tpos,tlen;  --  Used for long strings
       
%	   ed_str("*** OUTTXT: "); ed_oaddr(txt.ent);
%	   ed_str(", CP: "); ed_int(txt.cp);
%	   ed_str(", SP: "); ed_int(txt.sp);
%	   ed_str(", LP: "); ed_int(txt.lp); ed_out;
       
%      img:=fil.img;
       tpos:=txt.sp; tlen:=txt.lp-tpos;
 end;
 
 	infix(txtent:6) systxt = record:txtent(sort=S_TXTENT, misc=1, ncha=6, cha="SYSOUT"); 
	infix(txtqnt) sysid = record:txtqnt(ent=ref(systxt), lp=6);
 
 	xOUTTXT(none, sysid);

 end;
	 