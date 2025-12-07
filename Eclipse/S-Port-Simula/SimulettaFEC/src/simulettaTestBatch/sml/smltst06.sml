begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 06";
   const infix(string) PURPOSE      ="Records with Variant(ALT)";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(8) = (
      "BEGIN TEST",
      "i=120",
      "j=111",
      "int=44",
      "rea=4.4&+00",
      "str=ABRA",
      "bnd1.dope=66, bnd1.negbas=66",
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
 
    Visible record WithVariants; info "TYPE";
	begin integer   i;
    	  integer   j;
    	  variant   integer  int;
    	  variant   real rea;
    	  variant   infix(string) str;
	end;
   
   routine prtArrbnd;
   begin ed_str("  lb="); ed_int(bnd1.lb); ed_out;
         ed_str("  ub="); ed_int(bnd1.ub); ed_out;
         ed_str("  dope="); ed_int(bnd1.dope); ed_out;
         ed_str("  negbas="); ed_int(bnd1.negbas); ed_out;
   end;
   
   infix(arrbnd) bnd1;
   infix(WithVariants) r;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      r.i:=120;
      r.j:=111;
	  r.int:=44;
	  
      ed_str("i="); ed_int(r.i); trace(get_ed);
      ed_str("j="); ed_int(r.j); trace(get_ed);
      ed_str("int="); ed_int(r.int); trace(get_ed);
 	  
	  r.rea:=4.4;
      ed_str("rea="); ed_rea(r.rea,2); trace(get_ed);
      
	  r.str:="ABRA";
      ed_str("str="); ed_str(r.str); trace(get_ed);
      
      bnd1.dope:=66;
      ed_str("bnd1.dope="); ed_int(bnd1.dope);
      ed_str(", bnd1.negbas="); ed_int(bnd1.negbas);
      trace(get_ed);

      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
