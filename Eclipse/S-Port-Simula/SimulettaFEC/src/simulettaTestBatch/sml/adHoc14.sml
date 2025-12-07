begin
   SYSINSERT RT,SYSR;    

 Visible known("ARGIND") ARGIND;
 import ref(arhead) head; integer dim,ind(MAX_DIM);
 begin integer i,negbas,subscript; infix(arrbnd) bnd;
       dim:=dim-1; bnd:=head.bound(dim);
       negbas:=bnd.negbas; subscript:=ind(dim);
        dim:=dim - 1;
         bnd:=head.bound(dim);
       i:=ind(dim);
 end;

 end;
