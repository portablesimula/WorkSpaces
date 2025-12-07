/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;
import svm.value.IntegerValue;
import svm.value.LongRealValue;
import svm.value.ObjectAddress;
import svm.value.RealValue;
import svm.value.Value;

/// Global or local Variable.
///
/// S-CODE:
/// <pre>
/// 	global_definition ::= global internal:newtag quantity_descriptor
/// 
/// 	local_quantity ::= local var:newtag quantity_descriptor
/// 
/// 	import_definition ::= import parm:newtag quantity_descriptor
/// 
///		export parm:newtag resolved_type
/// 
///		exit return:newtag
///
///			quantity_descriptor ::= resolved_type < Rep count:number >?
///  
/// 		resolved_type
/// 	 		::= resolved_structure | simple_type
/// 	 		::= INT range lower:number upper:number
/// 	 		::= SINT
///  
/// 	 		resolved_structure ::= structured_type < fixrep count:ordinal >?
///  
///  				structured_type ::= record_tag:tag
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/Variable.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Variable extends Descriptor {
	
	/// The address of this Variable
	public ObjectAddress address;
	
	/// The Type of this Variable
	public Type type;
	
	/// The repCount of this Variable
	public int repCount;

	/// Create a new Attribute with the given 'kind' and 'tag'
	/// @param kind the kind
	/// @param tag used to lookup descriptors
	private Variable(final int kind, final Tag tag) {
		super(kind, tag);
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	/// @return an Variable instance.
	public static Variable ofIMPORT() {
		Tag tag = Tag.ofScode();
		Variable var = new Variable(Kind.K_Import, tag);
		var.type = Type.ofScode();
		var.repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		return var;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	/// @return an Variable instance.
	public static Variable ofEXPORT() {
		Tag tag = Tag.ofScode();
		Variable var = new Variable(Kind.K_Export, tag);
		var.type = Type.ofScode();
		var.repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		return var;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	/// @return an Variable instance.
	public static Variable ofEXIT() {
		Tag tag = Tag.ofScode();
		Variable var = new Variable(Kind.K_Exit, tag);
		var.type = Type.T_PADDR;
		return var;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	///@param returAddr the return address
	/// @return an Variable instance.
	public static Variable ofRETUR(final ObjectAddress returAddr) {
		Variable var = new Variable(Kind.K_Retur, null);
		var.type = Type.T_PADDR;
		var.address = returAddr;
		return var;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	/// @param rela the relative address
	/// @return an Variable instance.
	public static Variable ofLocal(final int rela) {
		Tag tag = Tag.ofScode();
		Variable var = new Variable(Kind.K_LocalVar, tag);
		var.type = Type.ofScode();
		var.repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		var.address = ObjectAddress.ofRelFrameAddr(rela);
		return var;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Variable instance.
	/// @param seg the DataSegment
	/// @return an Variable instance.
	public static Variable ofGlobal(final DataSegment seg) {
		Tag tag = Tag.ofScode();
		Variable var = new Variable(Kind.K_GlobalVar, tag);
		var.type = Type.ofScode();
		var.repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		var.address = seg.nextAddress();
		if(Scode.accept(Sinstr.S_SYSTEM)) {
			String system = Scode.inString();
			Value value = null;
			if(system.equalsIgnoreCase("CURINS")) value = null;//new ObjectAddress(true);
			else if(system.equalsIgnoreCase("STATUS")) value = null;//IntegerValue.of(0);
			else if(system.equalsIgnoreCase("ITSIZE")) value = IntegerValue.of(Type.T_INT, 666);
			else if(system.equalsIgnoreCase("MAXLEN")) value = IntegerValue.of(Type.T_SIZE, 20);
			else if(system.equalsIgnoreCase("INPLTH")) value = IntegerValue.of(Type.T_INT, 155);
			else if(system.equalsIgnoreCase("OUTLTH")) value = IntegerValue.of(Type.T_INT, 360);
			else if(system.equalsIgnoreCase("BIOREF")) value = null;//new ObjectAddress(true);
			else if(system.equalsIgnoreCase("MAXINT")) value = IntegerValue.of(Type.T_INT, Integer.MAX_VALUE);
			else if(system.equalsIgnoreCase("MININT")) value = IntegerValue.of(Type.T_INT, Integer.MIN_VALUE);
			else if(system.equalsIgnoreCase("MAXRNK")) value = IntegerValue.of(Type.T_INT, 255);
			else if(system.equalsIgnoreCase("MAXREA")) value = RealValue.of(Float.MAX_VALUE);
//			else if(system.equalsIgnoreCase("MINREA")) value = RealValue.of(Float.MIN_VALUE);
			else if(system.equalsIgnoreCase("MINREA")) value = RealValue.of(-Float.MAX_VALUE);
			else if(system.equalsIgnoreCase("MAXLRL")) value = LongRealValue.of(Double.MAX_VALUE);
//			else if(system.equalsIgnoreCase("MINLRL")) value = LongRealValue.of(Double.MIN_VALUE);
			else if(system.equalsIgnoreCase("MINLRL")) value = LongRealValue.of(-Double.MAX_VALUE);
			else if(system.equalsIgnoreCase("INIERR")) value = null;//new RoutineAddress(true);
			else if(system.equalsIgnoreCase("ALLOCO")) value = null;//new RoutineAddress(true);
			else if(system.equalsIgnoreCase("FREEOB")) value = null;//new RoutineAddress(true);
			else Util.IERR("MISSING: " + system);
			Global.DSEG.emit(value);
		} else {
			int count = var.type.size();
			if(Scode.accept(Sinstr.S_FIXREP)) {
				int fixrep = Scode.inNumber();
				RecordDescr rec = (RecordDescr) Display.getMeaning(var.type.tag);
				count = count + rec.rep0size * fixrep;
			}
			if(count == 0) Util.IERR("");
			seg.emitDefaultValue(count, var.repCount);
		}
		return var;
	}
	
	@Override
	public String toString() {
		return "Variable " +Kind.edKind(kind) + " " + tag + ", type=" + type + ", repCount=" + repCount+ " " + address;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Variable.Write: " + this);
		oupt.writeByte(kind); // K_GLOBAL, K_LOCAL, K_IMPORT, K_EXPORT, K_EXIT, K_RETUR
		tag.write(oupt);
		type.write(oupt);
		oupt.writeShort(repCount);
		if(address != null) {
			oupt.writeBoolean(true);
			address.write(oupt);
		} else oupt.writeBoolean(false);
	}

	/// Reads a Variable from the given input.
	/// @param inpt the input stream
	/// @param kind the Variable kind: Import, Export, Exit, Retur, LovalVar
	/// @return The Variable read
	/// @throws IOException if IOException occur
	public static Variable read(final AttributeInputStream inpt, int kind) throws IOException {
		Tag tag = Tag.read(inpt);
		Variable var = new Variable(kind, tag);
		var.type = Type.read(inpt);
		var.repCount = inpt.readShort();
		boolean present = inpt.readBoolean();
		if(present) {
			var.address = (ObjectAddress) Value.read(inpt);
		}
		if(Option.ATTR_INPUT_TRACE) IO.println("Variable.Read: " + var);
		return var;
	}


}
