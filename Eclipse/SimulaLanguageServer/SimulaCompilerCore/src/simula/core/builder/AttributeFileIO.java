/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import simula.core.DocumentManager;
import simula.core.coder.JarFileBuilder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.BlockDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.ExternalDeclaration;
import simula.core.syntaxClass.declaration.ProcedureDeclaration;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.ClassHierarchy;
import simula.core.utilities.DeclarationList;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Simula attribute file input/output.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/AttributeFileIO.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class AttributeFileIO {
	/// The Simula version.
	private final static String version="SimulaAttributeFile: Version 2.0";
	
	/// Default Constructor: NOT USED
	private AttributeFileIO() {}

	/// Write an attribute file.
	/// @param program the program module
	/// @throws IOException if an output operation fail
//	public static void writeAttributeFile(final JarFileBuilder jarFileBuilder, final ProgramModule programModule) throws IOException {
//		String entryName = programModule.getRelativeAttributeFileName();
//		if (entryName != null) {
//			byte[] bytes = buildAttrFile(programModule);
//			jarFileBuilder.writeEntryToJarOutput(entryName, bytes);
//		}
//	}

	/// Build a module's attribute entry.
	/// @param program the program module.
	/// @return the attribute entry's bytes.
	/// @throws IOException if an io-error occurs.
	public static byte[] buildAttrEntry(final ProgramModule program) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		AttributeOutputStream oupt = new AttributeOutputStream(byteArrayOutputStream);
		oupt.writeString(version);
		ClassHierarchy.writeObject(oupt);
		if(program.externalHead != null) {
//			for(ExternalDeclaration xdecl:program.externalHead) {
			for(SyntaxElement xdecl:program.externalHead) {
				oupt.writeObj(xdecl);
			}
		}
		oupt.writeObj(program.mainModule);
		oupt.close();
		return(byteArrayOutputStream.toByteArray());
	}

	
	/// Read an attribute entry from a .jar file.
	/// @param identifier class or procedure identifier
	/// @param file the .jar file to read
	/// @param enclosure the declaration list to update
	/// @return the module type
	public static Type readAttributeEntry(final SimulaBuilder simBuilder,final String identifier, final File file, final BlockDeclaration enclosure) {
		Type moduleType = null;
		Util.generalWarning("Separate Compiled Module is read from: \"" + file + "\"");
		if (!(file.exists() && file.canRead())) {
			Util.generalError("Can't read attribute file: " + file);
			return (null);
		}
		try (JarFile jarFile = new JarFile(file)) {
//			JarFileBuilder.listJarFile("AttributeFileIO.readAttributeFile: ", file);
			
			simBuilder.documentManager.externalJarFileNames.add(file.toString());
//			IO.println("AttributeFileIO.readAttributeFile: " + DocumentManager.externalJarFileNames);
			
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			String simulaInfo = mainAttributes.getValue("SIMULA-INFO");
			ZipEntry zipEntry = jarFile.getEntry(simulaInfo);
			if(zipEntry == null)
				Util.IERR("No Attribute File found in "+file);

			DeclarationList declarationList=enclosure.declarationList;
			Util.TRACE_INPUT("*** BEGIN Read SimulaAttributeFile: " + file);

			InputStream inputStream = jarFile.getInputStream(zipEntry);
			byte[] bytes = inputStream.readAllBytes(); inputStream.close();
			BlockDeclaration module = AttributeFileIO.readPrecompiled(simBuilder, file.toString(),bytes);
			moduleType = module.type;

			Declaration d=declarationList.find(module.identifier);
			if(d!=null) {
				Util.generalWarning("Multiple declarations with the same name: "+module+" and "+d);
			} else {
				declarationList.add(module);
				if (DocumentManager.verbose)
					IO.println("***       Read External " + ObjectKind.edit(module.declarationKind) + ' ' + module.identifier
							+ '[' + module.externalIdent + ']' +"  ==>  "+declarationList.debugName);
			}
		} catch (IOException e) {
			Util.generalError("Unable to read Attribute File: " + file + " caused by: " + e);
			Util.generalWarning("It may be necessary to recompile '" + identifier + "'");
			Util.IERR("Caused by:", e);
		}
		return (moduleType);
	}
	
	/// Check if the jarFile is already included.
	/// @param jarFile the jarFile.
	/// @return false: if the jarFile is already included.
	public static boolean checkJarFiles(final SimulaBuilder simBuilder, final String jarFileName) {
//		for(File f:SimulaCompiler.externalJarFiles) if(f.equals(jarFile)) {
		for(String f:simBuilder.documentManager.externalJarFileNames) if(f.equals(jarFileName)) {
			Util.generalWarning("External already included: "+jarFileName);
			return(false);
		}
		return true;
	}

	/// Read and return precompiled class or procedure.
	/// @param fileID the file ident.
	/// @param attrFile the attribute file.
	/// @return the resulting class or procedure.
	/// @throws IOException if somthing went wrong.
	private static BlockDeclaration readPrecompiled(SimulaBuilder simBuilder, String fileID,byte[] attrFile) throws IOException {
		AttributeInputStream inpt = new AttributeInputStream(new ByteArrayInputStream(attrFile), fileID);

		String vers = inpt.readString();
		if(!(vers.equals(version))) Util.generalError("Malformed SimulaAttributeFile: " + fileID);

		ClassHierarchy.readObject(simBuilder.documentManager, inpt);

		int declarationKind = inpt.readKind();
		while(declarationKind == ObjectKind.ExternalDeclaration) {
			ExternalDeclaration xdecl = ExternalDeclaration.readObject(simBuilder.documentManager, inpt);
			/// Read external Attribute file.
			File jarFile = JarFileBuilder.findJarFile(simBuilder, xdecl.identifierValue(), xdecl.externalIdent);
			if (jarFile == null) {
				Util.syntaxError(simBuilder, "Can't find attribute file: " + xdecl.identifier + '[' + xdecl.externalIdent + ']');
			} else {
				if(checkJarFiles(simBuilder, jarFile.toString())) {
					BlockDeclaration enclosure = StandardClass.BASICIO;
					AttributeFileIO.readAttributeEntry(simBuilder, xdecl.identifierValue(), jarFile, enclosure);
				}
			}		
			declarationKind = inpt.readKind();
		}
		
		BlockDeclaration module=null;
		if(declarationKind == ObjectKind.Procedure)  module = ProcedureDeclaration.readObject(simBuilder.documentManager, inpt);
		else if(declarationKind == ObjectKind.Class) module = ClassDeclaration.readObject(simBuilder.documentManager, inpt);
		else Util.IERR();
		inpt.close();
		if (DocumentManager.verbose)	Util.TRACE("*** ENDOF Read SimulaAttributeFile: " + fileID);
		module.isPreCompiledFromFile = fileID;
		return module;
	}

}
