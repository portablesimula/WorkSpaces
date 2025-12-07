begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    

    Visible record R;
	begin integer   i;
    	  integer   j;
    	  variant   integer  int;
    	  variant   real rea;
    	  variant   infix(string) str;
	end;

    infix(R) IR;

    ref(R) RR;
    field(integer) fj;
      
      RR:=ref(IR);
      fj:=field(R.j);
      
      var(RR+fj):=111;
 

 end;
