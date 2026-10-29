
/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.builder;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import simula.lsp.DocumentManager;
import simula.lsp.builder.token.Identifier;
import simula.lsp.syntaxClass.HiddenSpecification;
import simula.lsp.syntaxClass.ProtectedSpecification;
import simula.lsp.syntaxClass.SyntaxElement;
import simula.lsp.syntaxClass.Type;
import simula.lsp.syntaxClass.declaration.ArrayDeclaration;
import simula.lsp.syntaxClass.declaration.ClassDeclaration;
import simula.lsp.syntaxClass.declaration.ConnectionBlock;
import simula.lsp.syntaxClass.declaration.ExternalDeclaration;
import simula.lsp.syntaxClass.declaration.InspectVariableDeclaration;
import simula.lsp.syntaxClass.declaration.LabelDeclaration;
import simula.lsp.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.lsp.syntaxClass.declaration.Parameter;
import simula.lsp.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.lsp.syntaxClass.declaration.ProcedureDeclaration;
import simula.lsp.syntaxClass.declaration.SimpleVariableDeclaration;
import simula.lsp.syntaxClass.declaration.StandardClass;
import simula.lsp.syntaxClass.declaration.UndefinedDeclaration;
import simula.lsp.syntaxClass.declaration.VirtualSpecification;
import simula.lsp.syntaxClass.expression.ArithmeticExpression;
import simula.lsp.syntaxClass.expression.AssignmentOperation;
import simula.lsp.syntaxClass.expression.BooleanExpression;
import simula.lsp.syntaxClass.expression.ConditionalExpression;
import simula.lsp.syntaxClass.expression.Constant;
import simula.lsp.syntaxClass.expression.LocalObject;
import simula.lsp.syntaxClass.expression.ObjectGenerator;
import simula.lsp.syntaxClass.expression.ObjectRelation;
import simula.lsp.syntaxClass.expression.QualifiedObject;
import simula.lsp.syntaxClass.expression.RelationalOperation;
import simula.lsp.syntaxClass.expression.RemoteVariable;
import simula.lsp.syntaxClass.expression.TextExpression;
import simula.lsp.syntaxClass.expression.TypeConversion;
import simula.lsp.syntaxClass.expression.UnaryOperation;
import simula.lsp.syntaxClass.expression.VariableExpression;
import simula.lsp.syntaxClass.statement.ActivationStatement;
import simula.lsp.syntaxClass.statement.BlockStatement;
import simula.lsp.syntaxClass.statement.ConditionalStatement;
import simula.lsp.syntaxClass.statement.ConnectionDoPart;
import simula.lsp.syntaxClass.statement.ConnectionStatement;
import simula.lsp.syntaxClass.statement.ConnectionWhenPart;
import simula.lsp.syntaxClass.statement.DummyStatement;
import simula.lsp.syntaxClass.statement.ForListElement;
import simula.lsp.syntaxClass.statement.ForStatement;
import simula.lsp.syntaxClass.statement.ForWhileElement;
import simula.lsp.syntaxClass.statement.GotoStatement;
import simula.lsp.syntaxClass.statement.InnerStatement;
import simula.lsp.syntaxClass.statement.LabeledStatement;
import simula.lsp.syntaxClass.statement.StandaloneExpression;
import simula.lsp.syntaxClass.statement.StepUntilElement;
import simula.lsp.syntaxClass.statement.SwitchStatement;
import simula.lsp.syntaxClass.statement.WhileStatement;
import simula.lsp.utilities.ObjectKind;
import simula.lsp.utilities.ObjectList;
import simula.lsp.utilities.ObjectReferenceMap;
import simula.lsp.utilities.Util;

/// Attribute input stream.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/AttributeInputStream.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class AttributeInputStream {
	/// The module ident.
	String moduleID;
	
	/// The underlying DataInputStream.
	DataInputStream inpt;
	
	/// The jarFile being read.
	public String jarFileName;
	
	/// The Object Reference Table.
	/// Used during Attribute File Input to fixup Object References.
	public ObjectReferenceMap objectReference;
	
	/// Debug utility.
	private boolean TRACE = false; //true;

	/// Creates a new AttributeInputStream to read data from the specified InputStream.
	/// @param inpt the underlying InputStream.
	/// @param jarFileName the name of the jarFile containing this Attribute file.
	/// @throws IOException if an I/O error occurs.
    public AttributeInputStream(InputStream inpt, String jarFileName) throws IOException {
    	this.inpt = new DataInputStream(inpt);
    	this.jarFileName = jarFileName;
		objectReference = new ObjectReferenceMap();
		
		File file = new File(jarFileName);
		String name = file.getName();
		this.moduleID = name.substring(0, name.indexOf('.'));
    }

    /// Closes this AttributeInputStream.
    /// @throws IOException if an I/O error occurs.
	public void close() throws IOException { inpt.close(); }
    
	/// Reads and returns a kind code from the underlying DataInputStream.
	/// @return the kind code read.
	/// @throws IOException if an I/O error occurs.
    public int readKind() throws IOException {
    	int kind = inpt.readByte();
    	if(TRACE) Util.println("AttributeInputStream.readKind: "+kind+':'+ObjectKind.edit(kind));
    	return kind;
	}
	
    /// Reads and returns a type from the underlying DataInputStream.
    /// @return the type read.
    /// @throws IOException if an I/O error occurs.
    public Type readType() throws IOException {
		int keyWord = inpt.readUnsignedByte();
		if(keyWord == 0) {
	    	if(TRACE) Util.println("AttributeInputStream.readType: null");
			return null;
		}
		Identifier classIdent = readIdentifier();
		Type type = new Type(keyWord,classIdent);			
    	if(TRACE) Util.println("AttributeInputStream.readType: "+type);
    	return type;
    }
	
    /// Reads and returns a boolean from the underlying DataInputStream.
    /// @return the boolean read.
    /// @throws IOException if an I/O error occurs.
    public boolean readBoolean() throws IOException {
    	boolean b = inpt.readBoolean();
    	if(TRACE) Util.println("AttributeInputStream.readBoolean: "+b);
    	return b;
    }
	
    /// Reads and returns a short from the underlying DataInputStream.
    /// @return the short read.
    /// @throws IOException if an I/O error occurs.
    public int readShort() throws IOException {
    	short i = inpt.readShort();
    	if(TRACE) Util.println("AttributeInputStream.readInt: "+i);
    	return i;
	}

    /// Reads and returns a typed constant from the underlying DataInputStream.
    /// @return the typed constant read.
    /// @throws IOException if an I/O error occurs.
    public Object readConstant() throws IOException {
    	int key = inpt.readByte();
    	if(TRACE) Util.println("AttributeInputStream.readConstant: key=" + (int)key + " \"" + key +"\"");
    	Object res = null;
		switch(key) {
			case Type.T_UNDEF:		res = null; break;
			case Type.T_BOOLEAN:	res = inpt.readBoolean(); break;
			case Type.T_CHARACTER:	res = inpt.readChar(); break;
			case Type.T_INTEGER:	res = inpt.readShort(); break;
			case Type.T_REAL:		res = inpt.readFloat(); break;
			case Type.T_LONG_REAL:	res = inpt.readDouble(); break;
			case Type.T_TEXT:		res = readString(); break;
			default: throw new RuntimeException("key = "+key);
		}
    	if(TRACE) Util.println("AttributeInputStream.readDouble: "+res);
    	return res;
	}

    /// Reads and returns a String from the underlying DataInputStream.
    /// @return the String read.
    /// @throws IOException if an I/O error occurs.
    public String readString() throws IOException {
    	int lng = inpt.readShort()-1;
    	if(lng < 0) {
        	if(TRACE) Util.println("AttributeInputStream.readString: null");
    		return null;
    	}
    	StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lng; i++) sb.append(inpt.readChar());
    	String s = sb.toString();
    	if(TRACE) Util.println("AttributeInputStream.readString: \""+s+'"');
    	return s;
    }
	
    /// Reads and returns an identifier from the underlying DataInputStream.
    /// @return the identifier read.
    /// @throws IOException if an I/O error occurs.
    public Identifier readIdentifier() throws IOException {
    	int lng = inpt.readShort()-1;
    	if(lng < 0) {
        	if(TRACE) Util.println("AttributeInputStream.readString: null");
    		return null;
    	}
    	StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lng; i++) sb.append(inpt.readChar());
    	Identifier identifier = new Identifier(sb.toString());
    	if(TRACE) Util.println("AttributeInputStream.readIdentifier: "+identifier.value);
//    	Util.IERR("SJEKK DETTE");
    	return identifier;
    }

    /// Reads and returns an Object list from the underlying DataInputStream.
    /// @return the Object list read.
    /// @throws IOException if an I/O error occurs.
	public ObjectList<?> readObjectList(final DocumentManager documentManager) throws IOException {
		return ObjectList.read(documentManager, this);
	}

	/// Reads and returns an Object sequence number from the underlying DataInputStream.
	/// @param obj the corresponding SyntaxElement object.
	/// @return the Object sequence number read.
	/// @throws IOException if an I/O error occurs.
    public int readSEQU(SyntaxElement obj) throws IOException {
    	int OBJECT_SEQU = inpt.readShort();
    	if(TRACE) Util.println("AttributeInputStream.readSEQU: " + OBJECT_SEQU + "  ====>  " + obj.getClass().getSimpleName());
		objectReference.put(OBJECT_SEQU, obj);
    	return OBJECT_SEQU;
	}
    
    /// Reads and returns an Object from the underlying DataInputStream.
    /// @return the Object read.
    /// @throws IOException if an I/O error occurs.
	public SyntaxElement readObj(final DocumentManager documentManager) throws IOException {
		int kind = readKind();
		switch(kind) {
		case ObjectKind.NULL:
			if(TRACE) Util.println("AttributeInputStream.readObj: null");
			return null;
		case ObjectKind.ObjectReference:
			int OBJECT_SEQU = inpt.readShort();
			if(TRACE) Util.println("AttributeInputStream.readObj: OBJECT_SEQU="+OBJECT_SEQU);
			SyntaxElement obj = objectReference.get(OBJECT_SEQU);
			Util.ASSERT(obj != null, "Invariant: OBJECT_SEQU="+moduleID+"#"+OBJECT_SEQU);
			if(TRACE) Util.println("AttributeInputStream.readObj: "+obj);
			return(obj);
		default:
			obj = readObj(documentManager, kind, this);
			Util.ASSERT(obj.OBJECT_SEQU != 0, "Invariant: OBJECT_SEQU="+obj.OBJECT_SEQU);
			objectReference.put(obj.OBJECT_SEQU, obj);
			obj.OBJECT_SEQU = 0;
			if(TRACE)
				Util.println("AttributeInputStream.readObj: "+moduleID+"#"+obj.OBJECT_SEQU+" obj="+obj);
			return(obj);
		}	
	}

	/// Read and return an object.
	/// @param kind the object kind code.
	/// @param inpt the AttributeInputStream to read from.
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	private SyntaxElement readObj(final DocumentManager documentManager, final int kind, final AttributeInputStream inpt) throws IOException {
		switch(kind) {
			case ObjectKind.NULL:						return null;
			
			case ObjectKind.StandardClass:				return StandardClass.readObject(documentManager, inpt);
			case ObjectKind.ConnectionBlock:			return ConnectionBlock.readObject(documentManager, inpt);
			case ObjectKind.CompoundStatement:			return MaybeBlockDeclaration.readObject(documentManager, inpt,ObjectKind.CompoundStatement);
			case ObjectKind.SubBlock:					return MaybeBlockDeclaration.readObject(documentManager, inpt,ObjectKind.SubBlock);
			case ObjectKind.Procedure:					return ProcedureDeclaration.readObject(documentManager, inpt);
//			case ObjectKind.Switch:						return SwitchDeclaration.readObject(documentManager, inpt);
//			case ObjectKind.MemberMethod:				return MemberMethod.readObject(documentManager, inpt);
//			case ObjectKind.ContextFreeMethod:			return ContextFreeMethod.readObject(documentManager, inpt);
			case ObjectKind.Class:						return ClassDeclaration.readObject(documentManager, inpt);
			case ObjectKind.PrefixedBlock:				return PrefixedBlockDeclaration.readObject(documentManager, inpt);
//			case ObjectKind.SimulaProgram:				return SimulaProgram.readObject(documentManager, inpt);
			case ObjectKind.ArrayDeclaration:			return ArrayDeclaration.readObject(documentManager, inpt);
			case ObjectKind.VirtualSpecification:		return VirtualSpecification.readObject(documentManager, inpt);
//			case ObjectKind.VirtualMatch:				return VirtualMatch.readObject(documentManager, inpt);
			case ObjectKind.Parameter:					return Parameter.readObject(documentManager, inpt);
//			case ObjectKind.Thunk:						return Thunk.readObject(documentManager, inpt);
			case ObjectKind.LabelDeclaration:			return LabelDeclaration.readObject(documentManager, inpt);
			case ObjectKind.SimpleVariableDeclaration:	return SimpleVariableDeclaration.readObject(documentManager, inpt);
			case ObjectKind.InspectVariableDeclaration:	return InspectVariableDeclaration.readObject(documentManager, inpt);
			case ObjectKind.ExternalDeclaration:		return ExternalDeclaration.readObject(documentManager, inpt);
			case ObjectKind.HiddenSpecification:		return HiddenSpecification.readObject(documentManager, inpt);
			case ObjectKind.ProtectedSpecification:		return ProtectedSpecification.readObject(documentManager, inpt);
			case ObjectKind.UndefinedDeclaration:		return UndefinedDeclaration.readObject(documentManager, inpt);

			case ObjectKind.ActivationStatement:		return ActivationStatement.readObject(documentManager, inpt);
			case ObjectKind.BlockStatement:				return BlockStatement.readObject(documentManager, inpt);
			case ObjectKind.ConditionalStatement:		return ConditionalStatement.readObject(documentManager, inpt);
			case ObjectKind.ConnectionStatement:		return ConnectionStatement.readObject(documentManager, inpt);
			case ObjectKind.ConnectionDoPart:			return ConnectionDoPart.readObject(documentManager, inpt);
			case ObjectKind.ConnectionWhenPart:			return ConnectionWhenPart.readObject(documentManager, inpt);
			case ObjectKind.DummyStatement:				return DummyStatement.readObject(documentManager, inpt);
			case ObjectKind.ForStatement:				return ForStatement.readObject(documentManager, inpt);
			case ObjectKind.ForListElement:				return ForListElement.readObject(documentManager, inpt);
			case ObjectKind.ForWhileElement:			return ForWhileElement.readObject(documentManager, inpt);
			case ObjectKind.StepUntilElement:			return StepUntilElement.readObject(documentManager, inpt);
			case ObjectKind.GotoStatement:				return GotoStatement.readObject(documentManager, inpt);
//			case ObjectKind.InlineStatement:			return InlineStatement.readObject(documentManager, inpt);
			case ObjectKind.InnerStatement:				return InnerStatement.readObject(documentManager, inpt);
			case ObjectKind.LabeledStatement:			return LabeledStatement.readObject(documentManager, inpt);
//			case ObjectKind.ProgramModule:				return ProgramModule.readObject(documentManager, inpt);
			case ObjectKind.StandaloneExpression:		return StandaloneExpression.readObject(documentManager, inpt);
			case ObjectKind.SwitchStatement:			return SwitchStatement.readObject(documentManager, inpt);
			case ObjectKind.WhileStatement:				return WhileStatement.readObject(documentManager, inpt);

			case ObjectKind.ArithmeticExpression:		return ArithmeticExpression.readObject(documentManager, inpt);
			case ObjectKind.AssignmentOperation:		return AssignmentOperation.readObject(documentManager, inpt);
			case ObjectKind.BooleanExpression:			return BooleanExpression.readObject(documentManager, inpt);
			case ObjectKind.ConditionalExpression:		return ConditionalExpression.readObject(documentManager, inpt);
			case ObjectKind.Constant:					return Constant.readObject(documentManager, inpt);
			case ObjectKind.LocalObject:				return LocalObject.readObject(documentManager, inpt);
			case ObjectKind.ObjectGenerator:			return ObjectGenerator.readObject(documentManager, inpt);
			case ObjectKind.ObjectRelation:				return ObjectRelation.readObject(documentManager, inpt);
			case ObjectKind.QualifiedObject:			return QualifiedObject.readObject(documentManager, inpt);
			case ObjectKind.RelationalOperation:		return RelationalOperation.readObject(documentManager, inpt);
			case ObjectKind.RemoteVariable:				return RemoteVariable.readObject(documentManager, inpt);
			case ObjectKind.TextExpression:				return TextExpression.readObject(documentManager, inpt);
			case ObjectKind.TypeConversion:				return TypeConversion.readObject(documentManager, inpt);
			case ObjectKind.UnaryOperation:				return UnaryOperation.readObject(documentManager, inpt);
			case ObjectKind.VariableExpression:			return VariableExpression.readObject(documentManager, inpt);

		}	
		Util.IERR("IMPOSSIBLE "+ObjectKind.edit(kind));
		return(null);
	}


}
