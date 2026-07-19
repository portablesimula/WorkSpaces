/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import simula.Option;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.declaration.BlockDeclaration;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.syntaxClass.declaration.ExternalDeclaration;
import simula.compiler.syntaxClass.declaration.ProcedureDeclaration;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.ClassHierarchy;
import simula.compiler.utilities.DeclarationList;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;

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
	static void writeAttributeFile(final ProgramModule program) throws IOException {
		String relativeAttributeFileName = program.getRelativeAttributeFileName();
		if (relativeAttributeFileName == null) return;
		File file = new File(SimulaCompiler.tempClassFileDir,relativeAttributeFileName);
		if (SimulaCompiler.verbose)
			Util.println("*** BEGIN Generate SimulaAttributeFile: \"" + file+"\"");
		byte[] bytes = buildAttrFile(program);
		String entryName = program.getRelativeAttributeFileName();

   		if(SimulaCompiler.compilerMode == SimulaCompiler.CompilerMode.simulaClassLoader) {
			if(SimulaCompiler.jarFileBuilder!=null) {
				SimulaCompiler.jarFileBuilder.writeJarEntry(entryName, bytes);
			}
			else Util.IERR();
			
		} else {
			SimulaCompiler.jarFileBuilder.writeJarEntry(entryName, bytes);
		}
		if (SimulaCompiler.verbose)	Util.TRACE("*** ENDOF Generate SimulaAttributeFile: " + file);
	}

	/// Build a module's attribute file.
	/// @param program the program module.
	/// @return the attribute file's bytes.
	/// @throws IOException if an io-error occurs.
	private static byte[] buildAttrFile(final ProgramModule program) throws IOException {
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

	
	/// Read an attribute file.
	/// @param identifier class or procedure identifier
	/// @param file the .jar file to read
	/// @param enclosure the declaration list to update
	/// @return the module type
	public static Type readAttributeFile(final SimulaBuilder simBuilder,final String identifier, final File file, final BlockDeclaration enclosure) {
		Type moduleType = null;
		Util.generalWarning("Separate Compiled Module is read from: \"" + file + "\"");
		if (!(file.exists() && file.canRead())) {
			Util.generalError("Can't read attribute file: " + file);
			return (null);
		}
		try {
			JarFile jarFile = new JarFile(file);
			CoreGlobal.externalJarFiles.add(file);
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
				if (SimulaCompiler.verbose)
					IO.println("***       Read External " + ObjectKind.edit(module.declarationKind) + ' ' + module.identifier
							+ '[' + module.externalIdent + ']' +"  ==>  "+declarationList.debugName);
			}
    			
	   		if(SimulaCompiler.compilerMode == SimulaCompiler.CompilerMode.simulaClassLoader) {
				JarFileBuilder.addToIncludeQueue(jarFile);
			} else {
				if(SimulaCompiler.jarFileBuilder == null) {
			   		if(SimulaCompiler.compilerMode != SimulaCompiler.CompilerMode.simulaClassLoader) {
						SimulaCompiler.jarFileBuilder = new JarFileBuilder();
					}
				}
				SimulaCompiler.jarFileBuilder.expandJarFile(jarFile);
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
	public static boolean checkJarFiles(File jarFile) {
		for(File f:CoreGlobal.externalJarFiles) if(f.equals(jarFile)) {
			Util.generalWarning("External already included: "+jarFile.getName());
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

		ClassHierarchy.readObject(inpt);

		int declarationKind = inpt.readKind();
		while(declarationKind == ObjectKind.ExternalDeclaration) {
			ExternalDeclaration xdecl = ExternalDeclaration.readObject(inpt);
//			xdecl.readExternalAttributeFile();
			/// Read external Attribute file.
//			public void readExternalAttributeFile() {
				File jarFile = JarFileBuilder.findJarFile(xdecl.identifier.value, xdecl.externalIdent);
				if (jarFile == null) {
					Util.syntaxError(simBuilder, "Can't find attribute file: " + xdecl.identifier + '[' + xdecl.externalIdent + ']');
				} else {
					if(checkJarFiles(jarFile)) {
						BlockDeclaration enclosure = StandardClass.BASICIO;
						AttributeFileIO.readAttributeFile(simBuilder, xdecl.identifier.value, jarFile, enclosure);
					}
				}		
//			}
			declarationKind = inpt.readKind();
		}
		
		BlockDeclaration module=null;
		if(declarationKind == ObjectKind.Procedure)  module = ProcedureDeclaration.readObject(inpt);
		else if(declarationKind == ObjectKind.Class) module = ClassDeclaration.readObject(inpt);
		else Util.IERR();
		inpt.close();
		if (SimulaCompiler.verbose)	Util.TRACE("*** ENDOF Read SimulaAttributeFile: " + fileID);
		module.isPreCompiledFromFile = fileID;
		return module;
	}

}
