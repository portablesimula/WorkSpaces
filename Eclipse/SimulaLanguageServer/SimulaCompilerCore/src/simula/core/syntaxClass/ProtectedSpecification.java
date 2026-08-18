/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass;

import java.io.IOException;

import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.token.Identifier;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.VirtualSpecification;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Protected Specification.
/// <pre>
/// Syntax: 
///     protection-specification
///         = hidden identifier-list
///         | protected identifier-list
///         | hidden protected identifier-list
///         | protected hidden identifier-list
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/ProtectedSpecification.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ProtectedSpecification extends SyntaxElement {
	
	/// The protected identifier.
	public Identifier identifier;
	
	/// The owner.
	public ClassDeclaration definedIn;
	
	/// Set during doChecking.
    HiddenSpecification hiddenBy;

    /// Create a new ProtectedSpecification.
    /// @param definedIn the class it is defined in
    /// @param identifier the protected identifier
	public ProtectedSpecification(final DocumentManager documentManager, final ClassDeclaration definedIn,final Identifier identifier) {
		super(documentManager);
		this.definedIn=definedIn;
		this.identifier=identifier;
	}
	
	/// Returns the Attribute being Protected.
	/// @return the Attribute being Protected
	private Declaration getAttribute() {
		return(definedIn.findLocalAttribute(identifier));
	}
	
	/// Returns the virtual specification or null.
	/// 
	/// The Attribute being Protected may be Virtual.
	/// @return the virtual specification or null
	private VirtualSpecification getVirtualSpecification() {
		return(definedIn.searchVirtualSpecList(identifier));
	}
	
	// ***********************************************************************************************
	// *** Utility: doChecking -- Called from ClassDeclaration.checkProtectedList
	// ***********************************************************************************************
	/// Perform semantic checking.
	@Override
	public void doChecking() {
		Declaration attribute=getAttribute();
		if(attribute!=null) attribute.isProtected=this;
		else Util.semanticError(this, "No Attribute " + identifier.value + " match 'protected' specification: " + this);
		VirtualSpecification virtSpec=VirtualSpecification.getVirtualSpecification(attribute);
		if(virtSpec!=null) {
			if( virtSpec.declaredIn != attribute.declaredIn )
				Util.semanticError(this, "A virtual attribute may only be specified protected in the class heading in which the virtual specification occurs.");
		}
		// Virtual specification together with Attribute definition.
		VirtualSpecification vir=getVirtualSpecification();
		if(vir!=null) vir.isProtected=this;
	}


	@Override
	public void printTree(final int indent) {
		IO.println(SyntaxElement.edIndent(indent)+this.getClass().getSimpleName()+"    "+this);
	}

	@Override
	public String toString()
	{ StringBuilder s=new StringBuilder();
	  s.append("Protected ").append(identifier.value);
	  s.append("[ Defined in ");
	  s.append((definedIn!=null)?definedIn.identifier:"UNKNOWN");
	  if(hiddenBy!=null) {
		  s.append(", Hidden by ").append(hiddenBy.identifier.value);
		  s.append(" defined in ");
		  s.append((hiddenBy.definedIn!=null)?hiddenBy.definedIn.identifier:"MISSING");
	  }
	  s.append("]");
	  return(s.toString());
	}


	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private ProtectedSpecification(final DocumentManager documentManager) {
		super(documentManager);
	}
	
	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeProtectedSpecification: " + identifier.value);
		oupt.writeKind(ObjectKind.ProtectedSpecification);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** ProtectedSpecification
		oupt.writeIdentifier(identifier);
		oupt.writeObj(definedIn);
	}

	/// Read and return a ProtectedSpecification object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static ProtectedSpecification readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ProtectedSpecification spec = new ProtectedSpecification(documentManager);
		spec.OBJECT_SEQU = inpt.readSEQU(spec);
		// *** SyntaxElement
		spec.astData = readAstData(inpt);
		// *** ProtectedSpecification
		spec.identifier = inpt.readIdentifier();
		spec.definedIn = (ClassDeclaration) inpt.readObj(documentManager);
		Util.TRACE_INPUT("ProtectedSpecification: " + spec.identifier.value);
		return(spec);
	}

}
