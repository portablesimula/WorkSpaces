/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.common;

import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

public class Condition {
	public int instr;
    
    public Condition(int instr) {
    	this.instr=instr;
    }
    
    public Condition inverse() {
    	int invInstr=0;
    	if(instr==S_LT) invInstr=S_GE;
    	else if(instr==S_LE) invInstr=S_GT;
    	else if(instr==S_EQ) invInstr=S_NE;
    	else if(instr==S_GT) invInstr=S_LE;
    	else if(instr==S_GE) invInstr=S_LT;
    	else if(instr==S_NE) invInstr=S_EQ;
    	else FATAL_ERROR("FOREKOMMER DETTE ???");
    	return(new Condition(invInstr));
    }
    
    public boolean isTrueRelationship() {
    	if(instr==S_LT) return(true);
    	if(instr==S_LE) return(true);
        if(instr==S_EQ) return(true);
    	if(instr==S_GT) return(true);
        if(instr==S_GE) return(true);
        if(instr==S_NE) return(true);
        return(false);
    } 

    public static Condition relation(int instr) {
      return(new Condition(instr));
    }

	
	public String toString() {
		return("Condition "+edSymbol(instr));
	}
	

}
