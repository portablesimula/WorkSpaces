/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.common;

import simuletta.utilities.Util;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.declaration.LabelDeclaration;

public class ProgramPoint {
	// ***********************************************************************************************
	// *** Index Handling
	// ***********************************************************************************************
	private static final int MAX=255;
    private static boolean[] usedIndex=new boolean[MAX+1]; //(0:255 !max_index;);
    private static int freeindex=MAX; //next available;

    
    public static void print() {
    	Util.println("****************************** ProgramPoint IndexHandling  ******************************");
    	for(int i=0;i<=MAX;i++) {
    		Util.println("usedIndex["+i+"] = "+usedIndex[i]);
    	}
    	Util.println("******************************     FreeIndex = "+freeindex+"       ******************************");
    }

    private static int grabIndex() {
        for(int i=1;i<=MAX;i++) {
        	if(!usedIndex[i]) {
        		//Util.println("ProgramPoint.grabIndex: "+i);
        		usedIndex[i]=true;
        		return(i);
        	}
        }
        ERROR("To many Labels");
        return(0);
    }
    
    private static void releaseIndex(int index,String cause) {
        //Util.println("ProgramPoint.releaseIndex: "+index+", Caused by "+cause);
        usedIndex[index]=false;
    }
	
    
	// ***********************************************************************************************
	// *** Destination Handling
	// ***********************************************************************************************
    private static ProgramPoint[] dest_tab= new ProgramPoint[256];//(0:255); !*** zero never used **;

    public static int curdest,ndest;

    
    public static void grabDestTabEntry(LabelDeclaration label) {
//    	println("ProgramPoint.grabDestTabEntry: "+label);
    	if(ndest==255) FATAL_ERROR("Too many labels");
    	label.destinationIndex=ndest=ndest+1;
    	if(dest_tab[label.destinationIndex]!=null) IERR();
    	dest_tab[label.destinationIndex]=new ProgramPoint(label.identifier);
    	if(label.identifier==null) {
    		Util.IERR("ProgramPoint.grabDestTabEntry: No identifier in ="+label);
    	}
    }

	public static void defineDestination(int destinationIndex) {
		ProgramPoint.dest_tab[destinationIndex].define();
	}

	public static void gotoCurrentDestination() {
		dest_tab[curdest].go_to();   // T_DEST
	}

    public static void clearDestinationTable() {
    	while(ndest != 0) {
    		dest_tab[ndest].checkDefinedAndUsed();
    		dest_tab[ndest]=null;
    		ndest=ndest-1;
    	} curdest=0;
    }

	// ***********************************************************************************************
	// *** Pass2 - ProgramPoint
	// ***********************************************************************************************
    private String ident;
	private int index;

	private State state;
	private enum State { Undefined, FixupDefined, NormalDefined, DefinedAndUsed };

          //*********************************
          //**  state=0:  Undefined        **
          //**  state=1:  Fixup-defined    **  // TODO: BRUKES DENNE ?
          //**  state=2:  Normal-defined   **
          //**  state=3:  Defined and used **
          //*********************************
	
	public ProgramPoint(String ident) {
		this.ident=ident;
		state=State.Undefined;
	}

    public void jumpif(Condition cond) {
    	Util.TRACE("ProgramPoint.jumpifDirect: state="+state+", cond="+cond+", ident="+ident+", index="+index);
    	switch(state) {
            case Undefined: {
                 state=State.FixupDefined;
                 index=grabIndex();
                 sCode.outinst(S_FJUMPIF);
                 sCode.outinst(cond.instr); sCode.outbyt(index,ident);
                 break;
            }
            case NormalDefined: {
                 state=State.DefinedAndUsed;
                 sCode.outinst(S_BJUMPIF);
                 sCode.outinst(cond.instr); sCode.outbyt(index,ident);
                 releaseIndex(index,"jumpif:BJUMPIF");
                 break;
            }
            case FixupDefined: case DefinedAndUsed: {
                 ERROR("More than one jump to local label " + ident);
                 sCode.outinst(S_POP); sCode.outinst(S_POP);
                 break;
            }
    	}
    	sCode.outcode();
    	Util.TRACE("ProgramPoint.jumpif DONE: state="+state+", cond="+cond+", ident="+ident+", index="+index);
    }

    public void go_to() {
    	Util.TRACE("ProgramPoint.go_to: state="+state+", ident="+ident+", index="+index);
    	switch(state) {
            case Undefined: {
                 state=State.FixupDefined;
                 index=grabIndex();
                 sCode.outinst(S_FJUMP); sCode.outbyt(index,ident);
                 break;
            }
            case NormalDefined: {
                 state=State.DefinedAndUsed;
                 sCode.outinst(S_BJUMP); sCode.outbyt(index,ident);
                 releaseIndex(index,"go_to:BJUMP");
                 break;
            }
            case FixupDefined: case DefinedAndUsed: {
                 ERROR("More than one goto local label " + ident);
                 break;
            }
    	}
    	sCode.outcode();
    }

    public void define() {
    	Util.TRACE("ProgramPoint.define: state="+state+", ident="+ident+", index="+index);
    	switch(state) {
            case Undefined: {
                 state=State.NormalDefined;
                 index=grabIndex();
                 sCode.outinst(S_BDEST); sCode.outbyt(index,ident);
                 break;
            }
            case FixupDefined: {
                 state=State.DefinedAndUsed;
                 sCode.outinst(S_FDEST); sCode.outbyt(index,ident);
                 releaseIndex(index,"define:FDEST");
                 break;
            }
            case NormalDefined: case DefinedAndUsed: {
                 ERROR("Local label already defined " + ident); }
    	}
    	sCode.outcode();
    	Util.TRACE("ProgramPoint.defined: state="+state+", ident="+ident+", index="+index);
    }

	
	public void checkDefinedAndUsed() {
		if(state != State.DefinedAndUsed) ERROR("Illegal use of label " + ident+", state="+state);
		index=0; state=State.Undefined;
	}

	public String toString() {
	    return("ProgramPoint: "+ident+", index="+index+", state="+state);
	}
	
}
