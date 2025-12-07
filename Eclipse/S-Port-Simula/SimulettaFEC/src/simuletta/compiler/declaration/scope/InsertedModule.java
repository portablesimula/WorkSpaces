package simuletta.compiler.declaration.scope;

import static simuletta.compiler.Global.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import simuletta.compiler.Global;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.parsing.Mnemonic;
import simuletta.compiler.parsing.SimulettaScanner;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * Routine Declaration.
 * 
 * <pre>
 * Syntax:
 * 
 * module_inclusion
 * 		::= insert    module_ident < , module_ident >*
 * 		::= sysinsert module_ident < , module_ident >*
 * 
 *  	module_ident
 *  		::= module'identifier < = external_id'string_value >?
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class InsertedModule extends DeclarationScope {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public static final String version="Simuletta AtributeFile Layout version 1.0";
	
	public String modcode; // External id
	public boolean system;
	
//	The visible objects of the module (as specified in the tag_list) are now brought into the current
//	compilation unit by adding tagbase to the external numbers specified, checking that taglimit is not
//	exceeded. Thus tags are allocated from the range (tagbase..taglimit).
	public int tagbase;
	public int taglimit;
	public int nxtag;

	public InsertedModule(String modid,String modcode) {
		super(modid,false);
		this.modcode=modcode;
		modset.add(this);
	}
	
	public Tag findTag(String tagIdent) {
		Declaration decl=lookup(tagIdent);
		if(decl!=null) return(decl.getTag());
		return(null);
	}

	
	// ***********************************************************************************************
	// *** doInsertModule
	// ***********************************************************************************************
	public void doInsertModule(boolean sys) {
		this.system=sys;
		//Util.BREAK("Module.doInsertModule: "+identifier+" = "+modcode);
		if(currentModule.global) currentModule.combined=true;
		Tag.currentInsert=this;
		tagbase=Tag.lastTag+1;
		File attributeFile;
		if(sys) { attributeFile=new File(Global.simulaRtsLib+"/"+identifier+".atr");
		} else
		if(modcode!=null && modcode.length() > 0) {
			attributeFile = new File(modcode);
		} else attributeFile=new File(Global.simulettaTESTLib+"/"+identifier+".atr");
		
		String insertID=((sys)?"SYSINSERT ":"INSERT ")+identifier;
//		if (Option.verbose)	Util.println("InsertedModule.doInsertModule: "+insertID+" from \"" + attributeFile+"\"");
		try {
//			listAttributeFile(attributeFile);
			if (Option.verbose)	Util.TRACE("*** BEGIN listAttributeFile: " + attributeFile);
			FileInputStream fileInputStream = new FileInputStream(attributeFile);
			// BufferedInputStream uses a default buffer size of 2048 bytes.
//			AttributeInput inpt = new AttributeInput(identifier,new BufferedInputStream(fileInputStream));
			ObjectInput inpt = new ObjectInputStream(new BufferedInputStream(fileInputStream));
		
			if (!inpt.readObject().equals(version)) Util.ERROR("Malformed SimulettaAttributeFile: " + attributeFile);
//			Hashtable<String, Mnemonic> mnemonics=(Hashtable<String, Mnemonic>)inpt.readObject();
			int nMnemonics=inpt.readShort();
//			IO.println("InsertedModule.doInsertModule: "+identifier+"   nMnemonics="+nMnemonics);
			for(int i=0;i<nMnemonics;i++) {
//				Mnemonic mnemonic=Mnemonic.createAndRead(inpt);
				Mnemonic mnemonic=(Mnemonic) inpt.readObject();
//				IO.println("InsertedModule.doInsertModule: "+identifier+"   ADD "+mnemonic);
				SimulettaScanner.mnemonics.put(mnemonic.identifier,mnemonic);
			}

			int nDecl=inpt.readShort();
			for(int i=0;i<nDecl;i++) {
//				Declaration decl=Declaration.createAndRead(inpt);
				Declaration decl=(Declaration) inpt.readObject();
				this.declarationList.add(decl);
			}
			this.nxtag=inpt.readShort();
//			Util.STOP();
//			this.declarationList = (Vector<Declaration>)inpt.readObject();
//			this.nxtag=(int) inpt.readObject();
//			
//			SimulettaScanner.mnemonics.putAll(mnemonics);
			if(Option.TRACE_ATTRIBUTE_INPUT) {
				IO.println("************************** MODULE "+identifier+": "+insertID+"'MNEMONICS **************************");
				SimulettaScanner.listMnemonics();
				IO.println("************************** MODULE "+identifier+": "+insertID+"'DECLARATIONS **************************");
				//for(Declaration d:declarationList) d.print("",0);
			}
			
			inpt.close();
			if (Option.verbose) {
				if (Option.TRACE_ATTRIBUTE_INPUT) {
					for(Declaration d:declarationList) d.print("",20);
					Util.TRACE("*** ENDOF listAttributeFile: " + attributeFile);
				}
			}
		} catch (ClassNotFoundException | IOException e) { e.printStackTrace();	}
		
		taglimit=Tag.lastTag;
//		taglimit=tagbase+Tag.nxtag-1;+
		if (Option.verbose)	Util.println(insertID+"  tagbase="+tagbase+", taglimit="+taglimit+" from \"" + attributeFile+"\"");
		Util.ASSERT(nxtag==(taglimit-tagbase+1),"nxtag==(taglimit-tagbase+1): nxtag="+nxtag+", (taglimit-tagbase+1)="+(taglimit-tagbase+1));
		Tag.currentInsert=null;
	}
    
	public String toString() {
		return("Module "+identifier+"  tagbase="+tagbase+", taglimit="+taglimit+", nxtag="+nxtag+", (taglimit-tagbase+1)="+(taglimit-tagbase+1));
	}
	
}
