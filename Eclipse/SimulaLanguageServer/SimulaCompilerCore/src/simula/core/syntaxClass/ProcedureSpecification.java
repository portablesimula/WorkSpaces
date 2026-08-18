/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass;

import java.io.IOException;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.declaration.ProcedureDeclaration;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectList;
import simula.core.utilities.Util;

/// Procedure Specification.
/// <pre>
/// Simula Standard: 5.5.3 Virtual quantities
/// Simula Standard: 6.3 External procedure declaration
/// 
/// procedure-specification
///     = [ type ] PROCEDURE procedure-identifier procedure-head empty-body
///     
///    procedure-head
///        = [ formal-parameter-part ; [ mode-part ] specification-part  ] ;
///         
///    empty-body = dummy-statement
/// 
///    procedure-identifier = identifier
/// 
///       formal-parameter-part = "(" formal-parameter { , formal-parameter } ")"
///       
///          formal-parameter = identifier
///          
///       specification-part = specifier identifier-list ; { specifier identifier-list ; }
///       
///          specifier
///             = type [ array | procedure ]
///             | label
///             | switch
///             
///       mode-part 
///          = name-part [ value-part ]
///          | value-part [ name-part ]
///          
///          name-part = name identifier-list ;
///          value-part = value identifier-list ;
///          
///             identifier-list = identifier { , identifier }
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/ProcedureSpecification.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ProcedureSpecification extends SyntaxElement {
	
	/// The procedure identifier.
	private Identifier identifier;

	/// The procedure's type.
	public Type type;
	
	/// The parameter list.
	public ObjectList<Parameter> parameterList;

	// ***********************************************************************************************
	// *** CONSTRUCTORS
	// ***********************************************************************************************
	/// Create a new ProcedureSpecification
	/// @param identifier procedure-identifier
	/// @param type procedure's type or null
	/// @param pList the parameter lList
	public ProcedureSpecification(final DocumentManager documentManager, final Identifier identifier, final Type type, final ObjectList<Parameter> pList) {
		super(documentManager);
//		SimulaBuilder simBuilder = documentManager.simBuilder;
		this.identifier = identifier;
		this.type = type;
		this.parameterList = pList;
	}

	// ***********************************************************************************************
	// *** Parsing: expectProcedureSpecification
	// ***********************************************************************************************
	/// Procedure Specification.
	/// 
	/// <pre>
	/// Syntax:
	/// 
	/// procedure-specification
	///     = [ type ] PROCEDURE procedure-identifier procedure-head empty-body
	///     
	///    procedure-head
	///        = [ formal-parameter-part ; [ mode-part ] procedure-specification-part  ] ;
	///         
	///    empty-body = dummy-statement
	/// 
	///    procedure-identifier = identifier
	/// 
	/// </pre>
	/// Precondition:  [ type ] PROCEDURE  is already read.
	/// @param type procedure's type
	/// @return a newly created ProcedureSpecification
	public static ProcedureSpecification expectProcedureSpecification(final SimulaBuilder simBuilder, final Type type) {
//		IO.println("\n\nProcedureSpecification.expectProcedureSpecification: BEFORE expectProcedureDeclaration");
		ProcedureDeclaration block = ProcedureDeclaration.expectProcedureDeclaration(simBuilder, type);
//		IO.println("\n\nProcedureSpecification.expectProcedureSpecification: AFTER expectProcedureDeclaration: "+block);
		Parse.expect(simBuilder, KeyWord.SEMICOLON); // TODO: DENNE ER NY !
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("END ProcedureSpecification: " + block);
		CoreGlobal.setScope(block.declaredIn);
		ProcedureSpecification procedureSpecification = new ProcedureSpecification(simBuilder.documentManager, block.identifier, type, block.parameterList);
		return (procedureSpecification);
	}

	// ***********************************************************************************************
	// *** Utility: doChecking
	// ***********************************************************************************************
	/// Perform semantic checking.
	/// 
	/// @param scope the DeclarationScope
	public void doChecking(final DeclarationScope scope) {
		if (type != null)
			type.doChecking(scope, this);
		// Check parameters
		if (parameterList != null) {
			for (Parameter par : parameterList)
				par.doChecking();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (type != null)
			s.append(type).append(' ');
		s.append("PROCEDURE ").append(identifier.value).append(Parameter.editParameterList(parameterList));
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public ProcedureSpecification(final DocumentManager documentManager) {
		super(documentManager);
	}

	/// Write a ProcedureSpecification.
	/// @param spec the ProcedureSpecification.
	/// @param oupt the AttributeOutputStream.
	/// @throws IOException if something went wrong.
	public static void writeProcedureSpec(ProcedureSpecification spec,AttributeOutputStream oupt) throws IOException {
		if(spec == null) {
			oupt.writeBoolean(false);
		} else {
			Util.TRACE_OUTPUT("BEGIN Write ProcedureSpecification: " + spec.identifier.value);
			oupt.writeBoolean(true);
			oupt.writeIdentifier(spec.identifier);
			oupt.writeType(spec.type);
			oupt.writeObjectList(spec.parameterList);
		}
	}
	
	/// Read and return a ProcedureSpecification.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ProcedureSpecification read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static ProcedureSpecification readProcedureSpec(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		boolean present = inpt.readBoolean();
		if(!present) return(null);
		ProcedureSpecification spec = new ProcedureSpecification(documentManager);
		spec.identifier = inpt.readIdentifier();
		spec.type = inpt.readType();
		spec.parameterList = (ObjectList<Parameter>) inpt.readObjectList(documentManager);
		
		Util.TRACE_INPUT("END Read ProcedureSpecification: " + spec.identifier.value);
		return(spec);
	}

}
