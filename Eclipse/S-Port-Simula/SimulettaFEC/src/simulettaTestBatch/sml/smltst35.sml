begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 35";
   const infix(string) PURPOSE      ="RT'sizes: string, ptp, entity, ...";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(18) = (
      "BEGIN TEST",
      "size(string)=3",
      "size(ptp)=4",
      "size(subptp)=5",
      "size(proptp)=6",
      "size(claptp:2)=11",
      "size(pntvec:3)=5",
      "size(rptvec:2)=8",
      "size(repdes)=3",
      "size(virvec:4)=9",
      "size(virdes)=2",
      "size(atrvec:2)=3",
      "size(atrdes)=5",
      "size(refdes)=6",
      "size(litdes)=9",
      "size(swtdes:4)=21",
      "size(swtelt)=5",
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


   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      	ed_str("size(string)="); ed_size(size(string)); trace(get_ed);
      	ed_str("size(ptp)="); ed_size(size(ptp)); trace(get_ed);

      	ed_str("size(subptp)="); ed_size(size(subptp)); trace(get_ed);
      	ed_str("size(proptp)="); ed_size(size(proptp)); trace(get_ed);
      	ed_str("size(claptp:2)="); ed_size(size(claptp:2)); trace(get_ed);
      	
      	ed_str("size(pntvec:3)="); ed_size(size(pntvec:3)); trace(get_ed);
      	ed_str("size(rptvec:2)="); ed_size(size(rptvec:2)); trace(get_ed);
      	ed_str("size(repdes)="); ed_size(size(repdes)); trace(get_ed);
      	ed_str("size(virvec:4)="); ed_size(size(virvec:4)); trace(get_ed);
      	ed_str("size(virdes)="); ed_size(size(virdes)); trace(get_ed);
      	ed_str("size(atrvec:2)="); ed_size(size(atrvec:2)); trace(get_ed);
 
       	ed_str("size(atrdes)="); ed_size(size(atrdes)); trace(get_ed);
       	ed_str("size(refdes)="); ed_size(size(refdes)); trace(get_ed);
       	ed_str("size(litdes)="); ed_size(size(litdes)); trace(get_ed);
       	ed_str("size(swtdes:4)="); ed_size(size(swtdes:4)); trace(get_ed);
       	ed_str("size(swtelt)="); ed_size(size(swtelt)); trace(get_ed);
      	
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
 
 
 
 
 Visible record ptp;
 Visible record subptp:ptp; -- sub_ptp
 Visible record proptp:ptp; -- pro_ptp
 Visible record claptp:ptp; -- cla_pre_ptp
 Visible record pntvec;
 Visible record rptvec;
 Visible record repdes;   info  "TYPE";
 Visible record virvec;
 Visible record virdes;   info  "TYPE";
 Visible record atrvec;
 
 Visible record atrdes;
 Visible record refdes:atrdes;
 Visible record litdes:atrdes; -- descriptor of named const
 Visible record swtdes;
 Visible record swtelt; info  "TYPE";
