begin
   SYSINSERT RT,SYSR,KNWN,UTIL;--,STRG,CENT;
   
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 30";
   const infix(string) PURPOSE      ="More Constants";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(14) = (
      "BEGIN TEST",
      "MSG(1)=BBB",
      "dst=ABC",
      "END TEST",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
      "XXXXXX",
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

 const infix(string) MSG(MAX_ENO) = ( "AAA", "BBB" );

   Visible record REC1; -- cla_pre_ptp
   begin range(0:20) plv;
--       label     dcl,stm,cntInr; -- cnt_inr
       ref(REC1) prefix(0);
   end;

   Visible const infix(REC1:5) CREC =record:REC1
       (plv=3,prefix=(ref(CREC),ref(CREC),ref(CREC),ref(CREC),none))
       
   const infix(txtent: 10 ) ent1 = record:txtent
       (sl=none, sort=S_TXTENT, misc=1, ncha = 10 , cha = ('A','B','C') )
   infix(txtent: 10 ) ent2;
   
   infix(string) s,src,dst;
   infix(txtqnt) img;
   infix(txtqnt) txt;
   integer tpos;
   
   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
        s:=MSG(1); ED_STR("MSG(1)="); ED_STR(s); trace(get_ed);
        
        txt.ent:=ref(ent1);
        img.ent:=ref(ent2);
        src.chradr:=name(txt.ent.cha(tpos));   src.nchr:=3;
        dst.chradr:=name(img.ent.cha(img.cp)); dst.nchr:=3;
        C_MOVE(src,dst);
		ED_STR("dst="); ED_STR(dst); trace(get_ed);
	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
