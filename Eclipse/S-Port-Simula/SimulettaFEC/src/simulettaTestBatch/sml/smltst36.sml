begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 36";
   const infix(string) PURPOSE      ="RT'sizes: entity, ...";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(30) = (
      "BEGIN TEST",
      "size(entity)=4",
      "size(inst)=7",
      "size(booPro)=11",
      "size(chaPro)=11",
      "size(sinPro)=11",
      "size(intPro)=11",
      "size(reaPro)=11",
      "size(lrlPro)=11",
      "size(refPro)=11",
      "size(txtPro)=11",
      "size(ptrPro)=11",
      "size(thunk)=12",
      "size(savent)=7",
      "size(txtent:4)=8",
      "size(nonObj:6)=13",
      "size(filent)=28",
      "size(prtent)=32",
      "size(bioIns)=848",
      "size(arbody)=5",
      "size(booArr:16)=21",
      "size(chaArr:16)=21",
      "size(sinArr:16)=21",
      "size(intArr:16)=21",
      "size(reaArr:16)=21",
      "size(lrlArr:16)=21",
      "size(refArr:16)=21",
      "size(txtArr:16)=69",
      "size(ptrArr:16)=21",
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
      	ed_str("size(entity)="); ed_size(size(entity)); trace(get_ed);
      	ed_str("size(inst)="); ed_size(size(inst)); trace(get_ed);
      	
      	ed_str("size(booPro)="); ed_size(size(booPro)); trace(get_ed);
      	ed_str("size(chaPro)="); ed_size(size(chaPro)); trace(get_ed);
      	ed_str("size(sinPro)="); ed_size(size(sinPro)); trace(get_ed);
      	ed_str("size(intPro)="); ed_size(size(intPro)); trace(get_ed);
      	ed_str("size(reaPro)="); ed_size(size(reaPro)); trace(get_ed);
      	ed_str("size(lrlPro)="); ed_size(size(lrlPro)); trace(get_ed);
      	ed_str("size(refPro)="); ed_size(size(refPro)); trace(get_ed);
      	ed_str("size(txtPro)="); ed_size(size(txtPro)); trace(get_ed);
      	ed_str("size(ptrPro)="); ed_size(size(ptrPro)); trace(get_ed);

      	ed_str("size(thunk)="); ed_size(size(thunk)); trace(get_ed);
      	ed_str("size(savent)="); ed_size(size(savent)); trace(get_ed);
      	ed_str("size(txtent:4)="); ed_size(size(txtent:4)); trace(get_ed);
      	ed_str("size(nonObj:6)="); ed_size(size(nonObj:6)); trace(get_ed);
      	
      	ed_str("size(filent)="); ed_size(size(filent)); trace(get_ed);
      	ed_str("size(prtent)="); ed_size(size(prtent)); trace(get_ed);
      	ed_str("size(bioIns)="); ed_size(size(bioIns)); trace(get_ed);
      	ed_str("size(arbody)="); ed_size(size(arbody)); trace(get_ed);
      	
      	ed_str("size(booArr:16)="); ed_size(size(booArr:16)); trace(get_ed);
      	ed_str("size(chaArr:16)="); ed_size(size(chaArr:16)); trace(get_ed);
      	ed_str("size(sinArr:16)="); ed_size(size(sinArr:16)); trace(get_ed);
      	ed_str("size(intArr:16)="); ed_size(size(intArr:16)); trace(get_ed);
      	ed_str("size(reaArr:16)="); ed_size(size(reaArr:16)); trace(get_ed);
      	ed_str("size(lrlArr:16)="); ed_size(size(lrlArr:16)); trace(get_ed);
      	ed_str("size(refArr:16)="); ed_size(size(refArr:16)); trace(get_ed);
      	ed_str("size(txtArr:16)="); ed_size(size(txtArr:16)); trace(get_ed);
      	ed_str("size(ptrArr:16)="); ed_size(size(ptrArr:16)); trace(get_ed);
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
 
 
 

%title ******   E  n  t  i  t  i  e  s   ******

 Visible record entity;  info "DYNAMIC";
 Visible record inst:entity;
 Visible record booPro:inst; begin variant boolean       val; variant infix(quant) qnt end;
 Visible record chaPro:inst; begin variant character     val; variant infix(quant) qnt end;
 Visible record sinPro:inst; begin variant short integer val; variant infix(quant) qnt end;
 Visible record intPro:inst; begin variant integer       val; variant infix(quant) qnt end;
 Visible record reaPro:inst; begin variant real          val; variant infix(quant) qnt end;
 Visible record lrlPro:inst; begin variant long real     val; variant infix(quant) qnt end;
 Visible record refPro:inst; begin variant ref(inst)     val; variant infix(quant) qnt end;
 Visible record txtPro:inst; begin variant infix(txtqnt) val; variant infix(quant) qnt end;
 Visible record ptrPro:inst; begin variant ref()         val; variant infix(quant) qnt end;
 Visible record thunk:inst;
 Visible record savent:inst;

 Visible record txtent:entity;
 Visible record nonObj:inst;

 Visible record filent:inst;
 Visible record prtent:filent;
 Visible record bioIns:inst;
 Visible record arbody:entity;
 Visible record booArr:arbody begin boolean         elt(0) end;
 Visible record chaArr:arbody begin character       elt(0) end;
 Visible record sinArr:arbody begin short integer   elt(0) end;
 Visible record intArr:arbody begin integer         elt(0) end;
 Visible record reaArr:arbody begin real            elt(0) end;
 Visible record lrlArr:arbody begin long real       elt(0) end;
 Visible record refArr:arbody begin ref(inst)       elt(0) end;
 Visible record txtArr:arbody begin infix(txtqnt)   elt(0) end;
 Visible record ptrArr:arbody begin ref()           elt(0) end;

 Visible record arhead:entity;
 Visible record arrbnd;  info  "TYPE";
 Visible record arent2:entity;
 Visible record booAr2:arent2 begin boolean       elt(0) end;
 Visible record chaAr2:arent2 begin character     elt(0) end;
 Visible record sinAr2:arent2 begin short integer elt(0) end;
 Visible record intAr2:arent2 begin integer       elt(0) end;
 Visible record reaAr2:arent2 begin real          elt(0) end;
 Visible record lrlAr2:arent2 begin long real     elt(0) end;
 Visible record refAr2:arent2 begin ref(inst) elt(0) end;
 Visible record txtAr2:arent2 begin infix(txtqnt) elt(0) end;
 Visible record ptrAr2:arent2 begin ref()         elt(0) end;

 Visible record arent1:entity;
 Visible record booAr1:arent1 begin boolean       elt(0) end;
 Visible record chaAr1:arent1 begin character     elt(0) end;
 Visible record sinAr1:arent1 begin short integer elt(0) end;
 Visible record intAr1:arent1 begin integer       elt(0) end;
 Visible record reaAr1:arent1 begin real          elt(0) end;
 Visible record lrlAr1:arent1 begin long real     elt(0) end;
 Visible record refAr1:arent1 begin ref(inst) elt(0) end;
 Visible record txtAr1:arent1 begin infix(txtqnt) elt(0) end;
 Visible record ptrAr1:arent1 begin ref()         elt(0) end;
