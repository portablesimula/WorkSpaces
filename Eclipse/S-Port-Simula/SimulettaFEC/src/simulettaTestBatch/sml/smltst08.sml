begin
   SYSINSERT RT,SYSR,KNWN,UTIL;    
-- ===============================================   Main   program =============
--
   const infix(string) PROGRAM_NAME ="SIMULETTA TEST NO 08";
   const infix(string) PURPOSE      ="Arithmetic Operators ( + - * / rem)";
--
-- ==============================================================================
   
   integer nError ;
   integer traceCase;
   const infix(string) facit(26) = (
      "BEGIN TEST",
      "i+j=0, j+k=154, k+l=227",
      "i-j=0, j-k=-154, k-l=81",
      "i*j=0, j*k=0, k*l=11242",
      "j/k=0, k/l=2",
      "j rem k=0, k rem l=8",
      "x+y=0.000, y+z=3.140, z+w=16.6500",
      "x-y=0.000, y-z=-3.140, z-w=-10.3700",
      "x*y=0.000, y*z=0.000, z*w=42.4214",
      "y/z=0.000, z/w=0.232",
      "lx+ly=0.000, ly+lz=10000.000, lz+lw=11345.0000",
      "lx-ly=0.000, ly-lz=-10000.000, lz-lw=8655.0000",
      "lx*ly=0.000, ly*lz=0.000, lz*lw=13450000.0000",
      "ly/lz=0.000, lz/lw=7.435",
      "x+y=0.00&+00, y+z=3.14&+00, z+w=1.665&+01",
      "x-y=0.00&+00, y-z=-3.14&+00, z-w=-1.037&+01",
      "x*y=0.00&+00, y*z=0.00&+00, z*w=4.242&+01",
      "y/z=0.00&+00, z/w=2.32&-01",
      "lx+ly=0.00&+000, ly+lz=1.00&+004, lz+lw=1.135&+004",
      "lx-ly=0.00&+000, ly-lz=-1.00&+004, lz-lw=8.655&+003",
      "lx*ly=0.00&+000, ly*lz=0.00&+000, lz*lw=1.345&+007",
      "ly/lz=0.00&+000, lz/lw=7.43&+000",
      "i+(-(j+k))=-154",
      "x+(-(y+z))=-3.14&+00",
      "lx+(-(ly+lz))=-1.00&+004",
      "END TEST"
   );
   
   Visible routine trace; import infix(string) msg;
   begin
      if verbose then ed_str(msg); ed_str("  TEST AGAINST FACIT:  "); prt(facit(traceCase)); endif;
      if( not STREQL(msg,facit(traceCase))) then
         nError:=nError+1; prt(" ");
         ed_str("ERROR in Case "); ed_int(traceCase); ed_out;
         ed_str("Trace: "); ed_str("|"); ed_str(msg); prt("|");
         ed_str("Facit: "); ed_str(facit(traceCase)); prt("|");
      endif;
      traceCase:=traceCase+1;
   end;

   integer i,j,k=154,l=73;
   real x,y,z=3.14,w=13.51;
   long real lx,ly,lz=1.0&&4,lw=1.345&&3;

   if verbose then prt2("--- ",PROGRAM_NAME); prt2("--- ",PURPOSE) endif;
-- ==============================================================================
 
      trace("BEGIN TEST");
      
      ed_str("i+j="); ed_int(i+j); ed_str(", j+k="); ed_int(j+k); ed_str(", k+l="); ed_int(k+l); trace(get_ed);
      ed_str("i-j="); ed_int(i-j); ed_str(", j-k="); ed_int(j-k); ed_str(", k-l="); ed_int(k-l); trace(get_ed);
      ed_str("i*j="); ed_int(i*j); ed_str(", j*k="); ed_int(j*k); ed_str(", k*l="); ed_int(k*l); trace(get_ed);
      ed_str("j/k="); ed_int(j/k); ed_str(", k/l="); ed_int(k/l); trace(get_ed);
      ed_str("j rem k="); ed_int(j rem k); ed_str(", k rem l="); ed_int(k rem l); trace(get_ed);
      
      ed_str("x+y="); ed_fix(x+y,3); ed_str(", y+z="); ed_fix(y+z,3); ed_str(", z+w="); ed_fix(z+w,4); trace(get_ed);
      ed_str("x-y="); ed_fix(x-y,3); ed_str(", y-z="); ed_fix(y-z,3); ed_str(", z-w="); ed_fix(z-w,4); trace(get_ed);
      ed_str("x*y="); ed_fix(x*y,3); ed_str(", y*z="); ed_fix(y*z,3); ed_str(", z*w="); ed_fix(z*w,4); trace(get_ed);
      ed_str("y/z="); ed_fix(y/z,3); ed_str(", z/w="); ed_fix(z/w,3); trace(get_ed);
      
      ed_str("lx+ly="); ed_lfix(lx+ly,3); ed_str(", ly+lz="); ed_lfix(ly+lz,3); ed_str(", lz+lw="); ed_lfix(lz+lw,4); trace(get_ed);
      ed_str("lx-ly="); ed_lfix(lx-ly,3); ed_str(", ly-lz="); ed_lfix(ly-lz,3); ed_str(", lz-lw="); ed_lfix(lz-lw,4); trace(get_ed);
      ed_str("lx*ly="); ed_lfix(lx*ly,3); ed_str(", ly*lz="); ed_lfix(ly*lz,3); ed_str(", lz*lw="); ed_lfix(lz*lw,4); trace(get_ed);
      ed_str("ly/lz="); ed_lfix(ly/lz,3); ed_str(", lz/lw="); ed_lfix(lz/lw,3); trace(get_ed);
      
      ed_str("x+y="); ed_rea(x+y,3); ed_str(", y+z="); ed_rea(y+z,3); ed_str(", z+w="); ed_rea(z+w,4); trace(get_ed);
      ed_str("x-y="); ed_rea(x-y,3); ed_str(", y-z="); ed_rea(y-z,3); ed_str(", z-w="); ed_rea(z-w,4); trace(get_ed);
      ed_str("x*y="); ed_rea(x*y,3); ed_str(", y*z="); ed_rea(y*z,3); ed_str(", z*w="); ed_rea(z*w,4); trace(get_ed);
      ed_str("y/z="); ed_rea(y/z,3); ed_str(", z/w="); ed_rea(z/w,3); trace(get_ed);
      
      ed_str("lx+ly="); ed_lrl(lx+ly,3); ed_str(", ly+lz="); ed_lrl(ly+lz,3); ed_str(", lz+lw="); ed_lrl(lz+lw,4); trace(get_ed);
      ed_str("lx-ly="); ed_lrl(lx-ly,3); ed_str(", ly-lz="); ed_lrl(ly-lz,3); ed_str(", lz-lw="); ed_lrl(lz-lw,4); trace(get_ed);
      ed_str("lx*ly="); ed_lrl(lx*ly,3); ed_str(", ly*lz="); ed_lrl(ly*lz,3); ed_str(", lz*lw="); ed_lrl(lz*lw,4); trace(get_ed);
      ed_str("ly/lz="); ed_lrl(ly/lz,3); ed_str(", lz/lw="); ed_lrl(lz/lw,3); trace(get_ed);

	  ed_str("i+(-(j+k))="); ed_int(i+(-(j+k))); trace(get_ed);
	  ed_str("x+(-(y+z))="); ed_rea(x+(-(y+z)),3); trace(get_ed);
	  ed_str("lx+(-(ly+lz))="); ed_lrl(lx+(-(ly+lz)),3); trace(get_ed);
      trace("END TEST");

-- ==============================================================================
   
   IF nError = 0 then prt2("--- NO ERRORS FOUND IN ",PROGRAM_NAME) endif;
   if verbose then ed_str("--- END "); ed_str(PROGRAM_NAME); ed_str(" -- nError="); ed_int(nError); ed_out endif;

 end;
