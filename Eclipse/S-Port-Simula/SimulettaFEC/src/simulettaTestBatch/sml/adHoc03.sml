begin
   SYSINSERT RT,SYSR,KNWN,UTIL,strg,cent,cint,arr,form,libr,fil,smst,sml,edit,mntr;
%   SYSINSERT RT,SYSR,KNWN,UTIL;    

	Routine TEST; import name(integer) ni; begin
		integer k;
		k := var(ni);
	end;

	Visible routine TEST2; -- VAR: EncInt
	begin
		integer i;
		name(integer) ni;
		
		ni := name(i);
		TEST(ni);
	end;

	
	B_PROG;
	
	TEST2;

 end;
		 