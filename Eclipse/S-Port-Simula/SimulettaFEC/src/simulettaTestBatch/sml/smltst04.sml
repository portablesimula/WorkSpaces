begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 04";
   const infix(string) PURPOSE      ="Test Parameter Transmission";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(8) = (
      "Parameter i=4513",
      "Parameter r=3.14&+00",
      "Parameter c=R",
      "Parameter s=En String",
      "Sum j=80",
      "Par1=(111,112,113,,,)",
      "Par2(0)=444,0,0,0)",
      "END TEST"
   );
   
   Visible routine trace; import infix(string) msg;
   begin
      if verbose then ed_str(msg); ed_str("  TEST AGAINST FACIT:  "); prt(facit(traceCase)); endif;
      if( not STREQL(msg,facit(traceCase))) then
         nError:=nError+1; prt(" ");
         ed_str("ERROR in Case "); ed_int(traceCase); ed_out;
         ed_str("Trace: "); prt(msg);
         ed_str("Facit: "); prt(facit(traceCase));
      endif;
      traceCase:=traceCase+1;
   end;
   
   Routine testCases; import
      integer i;
      real r;
      character c;
      infix(string) s;
   begin
      infix(string) q;   
      ed_str("Parameter i="); ed_int(i); trace(get_ed);
      ed_str("Parameter r="); ed_rea(r,3); trace(get_ed);
      ed_str("Parameter c="); ed_cha(c); trace(get_ed);
      ed_str("Parameter s="); ed_str(s); trace(get_ed);
   end;   

   Routine testRepIntParam;
   import integer npar; integer par1(6); integer par2(4);
   export integer sum;   
   begin integer i;
         repeat sum := sum+par1(i); i:=i+1;
   		 while i < npar do endrepeat;
   		 sum:=sum+par2;
   end;   

   Routine testRepStrParam;
   import integer npar; infix(string) par1(6); integer par2(4);
   begin integer i;
   		 i:=0; ed_str("Par1=("); ed_str(par1(0));
   		 repeat i:=i+1; while i<6 do
			ed_str(","); ed_str(par1(i));
    	 endrepeat;
    	 ed_str(")"); trace(get_ed);
    	 
   		 i:=0; ed_str("Par2(0)="); ed_int(par2(0));
   		 repeat i:=i+1; while i<4 do
			ed_str(","); ed_int(par2(i));
    	 endrepeat;
   	     ed_str(")"); trace(get_ed);
   end;   

   infix(string) n; boolean cond;
   integer j;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      testCases(4513,3.14,'R',"En String");

      j := testRepIntParam(3,(11,12,13),44);
      ed_str("Sum j="); ed_int(j); trace(get_ed);
        
      
      testRepStrParam(3,("111","112","113"),444);
            
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
