begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 37";
   const infix(string) PURPOSE      ="RT'sizes: quantities and simob records";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(13) = (
      "BEGIN TEST",
      "size(txtqnt)=4",
      "size(labqnt)=3",
      "size(proqnt)=3",
      "size(swtqnt)=2",
      "size(pardes)=3",
      "size(refPar)=4",
      "size(litPar)=4",
      "size(thkPar)=4",
      "size(refThk)=5",
      "size(PARQNT)=6",
      "size(quant)=4",
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
      	ed_str("size(txtqnt)="); ed_size(size(txtqnt)); trace(get_ed);
      	ed_str("size(labqnt)="); ed_size(size(labqnt)); trace(get_ed);
      	ed_str("size(proqnt)="); ed_size(size(proqnt)); trace(get_ed);
      	ed_str("size(swtqnt)="); ed_size(size(swtqnt)); trace(get_ed);
      	ed_str("size(pardes)="); ed_size(size(pardes)); trace(get_ed);
      	ed_str("size(refPar)="); ed_size(size(refPar)); trace(get_ed);
      	ed_str("size(litPar)="); ed_size(size(litPar)); trace(get_ed);
      	ed_str("size(thkPar)="); ed_size(size(thkPar)); trace(get_ed);
      	ed_str("size(refThk)="); ed_size(size(refThk)); trace(get_ed);
      	ed_str("size(PARQNT)="); ed_size(size(PARQNT)); trace(get_ed);
      	ed_str("size(quant)="); ed_size(size(quant)); trace(get_ed);
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
 
 
 

%title ******   Q  u  a  n  t  i  t  i  e  s   ******

 Visible record txtqnt;  info "TYPE";
 Visible record labqnt;  info "TYPE";
 Visible record proqnt;  info "TYPE";
 Visible record swtqnt;  info "TYPE";
 Visible record pardes;
 Visible record refPar:pardes;
 Visible record litPar:pardes;
 Visible record thkPar:pardes;
 Visible record refThk:refPar;
 Visible record PARQNT;  info  "TYPE";
 Visible record quant;   info "TYPE";

%title ***   M o d u l e   a n d   S i m o b   I  n f o   ***

 Visible record ptpExt;  --- Prototype extension
 Visible record idfier;   -- identifier
 Visible record modinf;
 Visible record modvec;
 Visible record moddes;
