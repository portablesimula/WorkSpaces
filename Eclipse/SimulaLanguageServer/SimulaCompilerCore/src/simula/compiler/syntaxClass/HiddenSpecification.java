/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass;

import java.io.IOException;

import simula.builder.SimulaBuilder;
import simula.builder.SyntaxTree;
import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.token.Identifier;

/// Hidden Specification.
/// <pre>
/// Syntax: 
///     protection-specification
///         = hidden identifier-list
///         | protected identifier-list
///         | hidden protected identifier-list
///         | protected hidden identifier-list
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/HiddenSpecification.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class HiddenSpecification extends SyntaxElement {

	/// The hidden identifier.
	public Identifier identifier;

	/// The class in which this HiddenSpecification occur.
	ClassDeclaration definedIn;
	
	/// The ProtectedSpecification set during doChecking
	private ProtectedSpecification protectedBy; // Set during doChecking

	/// Returns the ProtectedSpecification which protect this hidden.
	/// @return the ProtectedSpecification which protect this hidden.
	private ProtectedSpecification getProtectedBy() {
		if (protectedBy == null)
			doChecking();
		return (protectedBy);
	}

	/// Create a new HiddenSpecification.
	/// @param definedIn  the class where Hidden is specified
	/// @param identifier the hidden identifier
	public HiddenSpecification(final SimulaBuilder simBuilder, final ClassDeclaration definedIn, final Identifier identifier) {
		super(simBuilder);
		this.definedIn = definedIn;
		this.identifier = identifier;
	}

	/// Utility: Perform semantic checking.
	/// Called from ClassDeclaration.checkHiddenList.
	@Override
	public void doChecking() {
		protectedBy = getMatchingProtected();
		if (protectedBy != null)
			protectedBy.hiddenBy = this;
	}

	/// Utility: Find protected attribute and update pointers.
	/// @return the resulting ProtectedSpecification
	private ProtectedSpecification getMatchingProtected() {
		ClassDeclaration scope = this.definedIn;
		ProtectedSpecification gotProtected = scope.searchProtectedList(identifier);
		if (gotProtected != null) {
			return (gotProtected);
		}
		scope = scope.getPrefixClass();
		SEARCH: while (scope != null) {
			HiddenSpecification gotHidden = findHidden(scope, identifier);
			if (gotHidden != null) {
				scope = gotHidden.getScopeBehindHidden();
				continue SEARCH;
			}
			gotProtected = scope.searchProtectedList(identifier);
			if (gotProtected != null) {
				return (gotProtected);
			}
			scope = scope.getPrefixClass();
		}
		Util.semanticError(this, identifier + " is specified HIDDEN without being PROTECTED");
		return (null);
	}

	/// Utility: getScopeBehindHidden
	/// -- Search backwards from 'hidden' ...
	/// @return the ClassDeclaration found
	public ClassDeclaration getScopeBehindHidden() {
		ProtectedSpecification protectedBy = getProtectedBy();
		ClassDeclaration definedIn = protectedBy.definedIn;
		return (definedIn.getPrefixClass());
	}

	/// Utility: findHidden -- Search scope's hidden-list for 'ident'
	/// @param scope the given scope
	/// @param ident the given ident
	/// @return the resulting HiddenSpecification
	private static HiddenSpecification findHidden(final ClassDeclaration scope, final Identifier ident) {
		for (HiddenSpecification hdn : scope.hiddenList)
			if (Util.equals(ident, hdn.identifier))
				return (hdn);
		return (null);
	}

	@Override
	public void printTree(final int indent, final Object head) {
		IO.println(SyntaxElement.edIndent(indent)+this.getClass().getSimpleName()+"    "+this);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Hidden ").append(identifier);
		s.append("[Defined in ");
		s.append((definedIn != null) ? definedIn.identifier : "UNKNOWN");
		if (protectedBy != null) {
			// public ProtectedSpecification protectedBy; // Set during doChecking
			s.append(", Protected by ").append(protectedBy.identifier);
			s.append("[defined in ");
			s.append((protectedBy.definedIn != null) ? protectedBy.definedIn.identifier : "MISSING");
			s.append("]");
		}
		s.append("]");
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private HiddenSpecification() {
		super(null);
	}
	
	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeHiddenSpecification: " + identifier);
		oupt.writeKind(ObjectKind.HiddenSpecification);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** HiddenSpecification
		oupt.writeIdentifier(identifier);
		oupt.writeObj(definedIn);
	}

	/// Read and return a HiddenSpecification object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static HiddenSpecification readObject(AttributeInputStream inpt) throws IOException {
		HiddenSpecification spec = new HiddenSpecification();
		spec.OBJECT_SEQU = inpt.readSEQU(spec);
		// *** SyntaxElement
//		spec.astData = readAstData(inpt);
		spec.astData = readAstData(inpt);

		// *** HiddenSpecification
		spec.identifier = inpt.readIdentifier();
		spec.definedIn = (ClassDeclaration) inpt.readObj();
		Util.TRACE_INPUT("HiddenSpecification: " + spec.identifier);
		return(spec);
	}

}
