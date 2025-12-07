begin
   SYSINSERT RT,SYSR,KNWN,UTIL,STRG,CENT,CINT,ARR,FORM,LIBR,FIL,SMST,SML,EDIT,MNTR;    

       record area;                 -- Definition of storage pool
       begin ref(area) suc;         -- Used to organize the pool list
             ref(entity) nxt,lim;   -- Boundary pointers within the pool
             ref(entity) startgc;   -- "freeze-address" for the pool
             size stepsize;         -- extend/contract step
             size mingap;           -- for this pool
             short integer sequ;    -- Sequence number (1,2, ... )
       end;

        ref(area) last_pool;         -- Pointer to last pool in chain
       ref(area) last_alloc;        -- Pointer to last allocated pool
       size request;
       size minsize;                -- Minimum size of a pool
       boolean   frozen;            -- True if FREEZE

 Routine GetNewPool;
 import size request; export ref(area) result;
 begin ref(area) p; size poolsize; short integer sequ;
       result:=none;
       if last_alloc<>none
       then sequ:=last_alloc.sequ+1 else sequ:=1 endif;
       poolsize:=SIZEIN(1,sequ);
       ED_STR("STRG.GETNEWPOOL: poolsize="); ED_SIZE(poolsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: minsize="); ED_SIZE(minsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: request="); ED_SIZE(request); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: (none+poolsize)="); ED_OADDR(none+poolsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: (none+minsize+request)="); ED_OADDR(none+minsize+request); ED_OUT;   
       if (status<>0) or ((none+poolsize)<=(none+minsize+request))
       then goto E1 endif;
       p:=DWAREA(poolsize,sequ);
       ED_STR("STRG.GETNEWPOOL: status="); ED_INT(status); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: p="); ED_OADDR(p); ED_OUT; 
--	   SETOPT(2,1);  
       if (status=0) and (p<>none)
       then ZEROAREA(p,p+poolsize);
            p.sequ:=sequ;
            p.startgc:=p.nxt:=p+size(area);
            -- all pools must have a dummy object, if frozen
            if frozen then insertDummy(p) endif;
            p.lim:=p+poolsize-size(entity); -- reserve space for GAP
            p.stepsize:=SIZEIN(2,sequ);
            if status<>0 then status:=0; p.stepsize:=nosize endif;
            p.mingap:=SIZEIN(3,sequ);
            if status<>0 then status:=0; p.mingap:=nosize endif;
            if last_pool<>none then last_pool.suc:=p;
               --- cannot trace first time because bio is not set!!!!
               if bio.trc then bio.obsEvt:=EVT_gcNP; observ endif;
            endif;
            result:=last_alloc:=last_pool:=p;
            if (p.lim-maxlen) < (p.nxt+request)
            then if NoExtendPool(request) then result:=none endif endif;
       endif;
    E1: status:=0; -- result reports, no status possible
 end;

 Routine MINI_GetNewPool;
 import size request; export ref(area) result;
 begin ref(area) p; size poolsize; short integer sequ;
       result:=none;
       if last_alloc<>none
       then sequ:=last_alloc.sequ+1 else sequ:=1 endif;
       poolsize:=SIZEIN(1,sequ);
       ED_STR("STRG.GETNEWPOOL: poolsize="); ED_SIZE(poolsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: minsize="); ED_SIZE(minsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: request="); ED_SIZE(request); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: (none+poolsize)="); ED_OADDR(none+poolsize); ED_OUT;   
       ED_STR("STRG.GETNEWPOOL: (none+minsize+request)="); ED_OADDR(none+minsize+request); ED_OUT;   
       if (status<>0) or ((none+poolsize)<=(none+minsize+request))
       then goto E1 endif;
       p:=DWAREA(poolsize,sequ);
            result:=last_alloc:=last_pool:=p;
            if (p.lim-maxlen) < (p.nxt+request)
            then if NoExtendPool(request) then result:=none endif endif;
    E1: status:=0; -- result reports, no status possible
 end;

 routine NoExtendPool; import size request; export boolean res;
 begin ref(area) p; ref(entity) lim; boolean notzeroed;
 end;

%   STRG.GETNEWPOOL: poolsize=150000
%   STRG.GETNEWPOOL: minsize=18
%   STRG.GETNEWPOOL: request=618
%   STRG.GETNEWPOOL: (none+poolsize)=RELADR[callStackTop+150000]
%   STRG.GETNEWPOOL: (none+minsize+request)=RELADR[callStackTop+636]
%   STRG.GETNEWPOOL: status=0
%   STRG.GETNEWPOOL: p=POOL_1[0]

	minsize := 18 qua size;
	request := 618 qua size;

	B_PROG;	
%	GetNewPool(request);
%	MINI_GetNewPool(request);

 end;
	 