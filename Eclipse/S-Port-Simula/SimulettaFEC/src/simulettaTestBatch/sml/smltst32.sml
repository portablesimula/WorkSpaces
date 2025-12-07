begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 32";
   const infix(string) PURPOSE      ="More Structured Constants";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(5) = (
      "BEGIN TEST",
      "NOTEXT=""""",
      "TEXT=""ABCDEFGHIJ""",
      "TEXT=""ABCDEFGHIJ""",
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


 Visible const infix(txtqnt)
       notext=record:txtqnt(sp=0,lp=0,cp=0,ent=none);
       
 const infix(txtent:10) defident1 = record:txtent(sl=none, sort=S_TXTENT, misc=1, ncha=10, cha=('A','B','C','D','E','F','G','H','I','J') );
 const infix(txtent:10) defident2 = record:txtent(sl=none, sort=S_TXTENT, misc=1, ncha=10, cha="ABCDEFGHIJ" );

 const infix(txtqnt) acmdir1=record:txtqnt(ent=ref(defident1), cp=0, sp=0, lp = 10);
 const infix(txtqnt) acmdir2=record:txtqnt(ent=ref(defident2), cp=0, sp=0, lp = 10);


   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
		ED_STR("NOTEXT="""); ED_TXT(notext); ED_CHA('"'); trace(get_ed); 
		ED_STR("TEXT="""); ED_TXT(acmdir1); ED_CHA('"'); trace(get_ed); 
		ED_STR("TEXT="""); ED_TXT(acmdir2); ED_CHA('"'); trace(get_ed); 
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
