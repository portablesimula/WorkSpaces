begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 10";
   const infix(string) PURPOSE      ="Arithmetic Relations ( < , <= , = , >= , > , <> )";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(20) = (
      "BEGIN TEST",
      "(i<j)==false, (j<k)==true, (k<l)==false",
      "(i<=j)==true, (j<=k)==true, (k<=l)==false",
      "(i=j)==true, (j=k)==false, (k=l)==false",
      "(j>=k)==false, (k>=l)==true",
      "(j>k)==false, (k>l)==false",
      "(j<>k)==true, (k<>l)==true",
      "(x<y)==false, (y<z)==true, (z<w)==true",
      "(x<=y)==true, (y<=z)==true, (z<=w)==true",
      "(x=y)==true, (y=z)==false, (z=w)==false",
      "(y>=z)==false, (z>=w)==false",
      "(y>z)==false, (z>w)==false",
      "(y<>z)==true, (z<>w)==true",
      "(lx<ly)==false, (ly<lz)==true, (lz<lw)==false",
      "(lx<=ly)==true, (ly<=lz)==true, (lz<=lw)==false",
      "(lx=ly)==true, (ly=lz)==false, (lz=lw)==false",
      "(ly>=lz)==false, (lz>=lw)==true",
      "(ly>lz)==false, (lz>lw)==true",
      "(ly<>lz)==true, (lz<>lw)==true",
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

   integer i,j,k=154,l=73;
   real x,y,z=3.14,w=13.51;
   long real lx,ly,lz=1.0&&4,lw=1.345&&3;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      ed_str("(i<j)=="); ed_bool(i<j); ed_str(", (j<k)=="); ed_bool(j<k); ed_str(", (k<l)=="); ed_bool(k<l); trace(get_ed);
      ed_str("(i<=j)=="); ed_bool(i<=j); ed_str(", (j<=k)=="); ed_bool(j<=k); ed_str(", (k<=l)=="); ed_bool(k<=l); trace(get_ed);
      ed_str("(i=j)=="); ed_bool(i=j); ed_str(", (j=k)=="); ed_bool(j=k); ed_str(", (k=l)=="); ed_bool(k=l); trace(get_ed);
      ed_str("(j>=k)=="); ed_bool(j>=k); ed_str(", (k>=l)=="); ed_bool(k>=l); trace(get_ed);
      ed_str("(j>k)=="); ed_bool(j>k); ed_str(", (k>l)=="); ed_bool(k=l); trace(get_ed);
      ed_str("(j<>k)=="); ed_bool(j<>k); ed_str(", (k<>l)=="); ed_bool(k<>l); trace(get_ed);
      
      ed_str("(x<y)=="); ed_bool(x<y); ed_str(", (y<z)=="); ed_bool(y<z); ed_str(", (z<w)=="); ed_bool(z<w); trace(get_ed);
      ed_str("(x<=y)=="); ed_bool(x<=y); ed_str(", (y<=z)=="); ed_bool(y<=z); ed_str(", (z<=w)=="); ed_bool(z<=w); trace(get_ed);
      ed_str("(x=y)=="); ed_bool(x=y); ed_str(", (y=z)=="); ed_bool(y=z); ed_str(", (z=w)=="); ed_bool(z=w); trace(get_ed);
      ed_str("(y>=z)=="); ed_bool(y>=z); ed_str(", (z>=w)=="); ed_bool(z>=w); trace(get_ed);
      ed_str("(y>z)=="); ed_bool(y>z); ed_str(", (z>w)=="); ed_bool(z>w); trace(get_ed);
      ed_str("(y<>z)=="); ed_bool(y<>z); ed_str(", (z<>w)=="); ed_bool(z<>w); trace(get_ed);
      
      ed_str("(lx<ly)=="); ed_bool(lx<ly); ed_str(", (ly<lz)=="); ed_bool(ly<lz); ed_str(", (lz<lw)=="); ed_bool(lz<lw); trace(get_ed);
      ed_str("(lx<=ly)=="); ed_bool(lx<=ly); ed_str(", (ly<=lz)=="); ed_bool(ly<=lz); ed_str(", (lz<=lw)=="); ed_bool(lz<=lw); trace(get_ed);
      ed_str("(lx=ly)=="); ed_bool(lx=ly); ed_str(", (ly=lz)=="); ed_bool(ly=lz); ed_str(", (lz=lw)=="); ed_bool(lz=lw); trace(get_ed);
      ed_str("(ly>=lz)=="); ed_bool(ly>=lz); ed_str(", (lz>=lw)=="); ed_bool(lz>=lw); trace(get_ed);
      ed_str("(ly>lz)=="); ed_bool(ly>lz); ed_str(", (lz>lw)=="); ed_bool(lz>lw); trace(get_ed);
      ed_str("(ly<>lz)=="); ed_bool(ly<>lz); ed_str(", (lz<>lw)=="); ed_bool(lz<>lw); trace(get_ed);

      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
