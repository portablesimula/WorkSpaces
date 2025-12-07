/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/

package svm.env;

import java.util.Vector;

import bec.Global;
import bec.Option;
import bec.scode.Type;
import bec.util.Util;
import svm.RTStack;
import svm.RTUtil;
import svm.env.dataset.Dataset;
import svm.env.dataset.DirectBytefile;
import svm.env.dataset.Directfile;
import svm.env.dataset.ImageFile;
import svm.env.dataset.Inbytefile;
import svm.env.dataset.Infile;
import svm.env.dataset.Outbytefile;
import svm.env.dataset.Outfile;
import svm.env.dataset.Printfile;
import svm.instruction.SVM_CALL_SYS;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;

/// File Handling.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/SysFile.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public abstract class SysFile {
	
	/** Default Constructor */ public SysFile() {} 

	/// The set of DatasetSpecs
	private static Vector<Dataset> datasetSpecs = new Vector<Dataset>();
	
	/// Add a DatasetSpec to the set of DatasetSpec
	/// @param spec the DatasetSpec to add
	/// @return key suitable to lookup the Dataset
	private static int addRTFile(Dataset spec) {
		datasetSpecs.add(spec);
		return(datasetSpecs.size()+3);
	}
	
	/// Lookup a DatasetSpec by its 'key'
	/// @param key the DatasetSpec key to use
	///@return a DatasetSpec by its 'key'
	private static Dataset lookup(int key) {
//		IO.println("SysFile.lookup: key="+key);
		return datasetSpecs.get(key-4);
	}
	
	/// Get data set specification of system files
	///
	///		Visible sysroutine("GDSPEC") GDSPEC;
	///		import range(1:3) code; infix(string) spec;
	///		export integer filled  end;
	///
	/// 	Runtime Stack
	/// 	   ..., code, spec'addr, spec'ofst, specnchr →
	/// 	   ..., filled
	///
	/// Depending on 'code', the DatasetSpec String is returned in the given 'spec' and
	/// the length 'filled' is pushed onto the Runtime stack.
	///
	///		Code Dsetspec
	///		  1  What is the data set specification for SYSIN, the file will be opened as infile.
	///		  2  What is the data set specification for SYSOUT, the file will be opened as printfile.
	///		  3  What is the data set specification for SYSTRACE, the file will be opened as printfile.
	///
	public static void gdspec() {
		SVM_CALL_SYS.ENTER("GDSPEC: ", 1, 4); // exportSize, importSize
		@SuppressWarnings("unused")
		int itemNchr = RTStack.popInt();
		ObjectAddress itemAddr = RTStack.popGADDRasOADDR();
		int index = RTStack.popInt();
		String result = null;
		switch(index) {
			case 1: // What is the data set specification for SYSIN.
				result = "SYSIN"; break;
			case 2: // What is the data set specification for SYSOUT.
				result = "SYSOUT"; break;
			case 3: // What is the data set specification for SYSTRACE.
				result = "SYSTRACE"; break;
			default: RTUtil.set_STATUS(19);
		}
		RTUtil.move(result, itemAddr);

		RTStack.push(IntegerValue.of(Type.T_INT, result.length()));
		SVM_CALL_SYS.EXIT("GDSPEC: ");
	}
	
	/// Get lines per page
	///
	///		Visible sysroutine("GETLPP") GETLPP;
	///		import range(1:MAX_KEY) key; export integer lpp end;
	///
	/// 	Runtime Stack
	/// 	   ..., key →
	/// 	   ..., lpp
	///
	/// Pop off the argument 'key' and
	/// <br>push the 'lpp' onto the Runtime stack
	public static void getlpp() {
		SVM_CALL_SYS.ENTER("GETLPP: ", 1, 1); // exportSize, importSize
		@SuppressWarnings("unused")
		int key = RTStack.popInt();
		RTStack.push(IntegerValue.of(Type.T_INT, 66));
		SVM_CALL_SYS.EXIT("GETLPP: ");
	}
	
	/// Open data set
	///
	///		Visible sysroutine("OPFILE") OPFILE;
	///		import infix(string)    spec;   -- dsetspec;
	///		       range(0:MAX_FIL) type;   -- dsettype;
	///		       infix(string)    action;
	///		       integer          imglng; -- img_lng;
	///		export range(0:MAX_KEY) key;    -- filekey;
	///		end;
	///
	/// 	Runtime Stack
	/// 	   ..., spec, type, action, imglng →
	/// 	   ..., key
	///
	/// A new DatasetSpec is created based upon the arguments.
	/// That spec is added and the lookup 'key' is returned.
	/// <pre>
	///		spec:	Identification of a data set.
	///		type:	The type code of the corresponding file.
	///		action:	A copy of the second parameter to the Simula open procedure, see below.
	///		imglng:	The length of the images in the file.
	///		Key:	The key associated with the data set, or zero.
	/// </pre>
	public static void opfile() {
		SVM_CALL_SYS.ENTER("OPFILE: ", 1, 8); // exportSize, importSize
		int imglng = RTStack.popInt();
		String action = RTStack.popString();
		int type = RTStack.popInt();
		String spec = RTStack.popString();
		int key = 0;
		if(spec.equalsIgnoreCase("SYSIN"))         key = Dataset.KEY_SYSIN;
		else if(spec.equalsIgnoreCase("SYSOUT"))   key = Dataset.KEY_SYSOUT;
		else if(spec.equalsIgnoreCase("SYSTRACE")) key = Dataset.KEY_SYSTRACE;
		else {
			Dataset fileSpec = null;
			switch(type) {
				case Dataset.FIL_INFILE ->		   fileSpec = new Infile        (spec, type, action, imglng);
				case Dataset.FIL_OUTFILE ->		   fileSpec = new Outfile       (spec, type, action, imglng);
				case Dataset.FIL_PRINTFILE ->	   fileSpec = new Printfile     (spec, type, action, imglng);
				case Dataset.FIL_DIRECTFILE ->	   fileSpec = new Directfile    (spec, type, action, imglng);
				case Dataset.FIL_INBYTEFILE ->	   fileSpec = new Inbytefile    (spec, type, action);
				case Dataset.FIL_OUTBYTEFILE ->    fileSpec = new Outbytefile   (spec, type, action);
				case Dataset.FIL_DIRECTBYTEFILE -> fileSpec = new DirectBytefile(spec, type, action);
				default -> Util.IERR(""+Dataset.edFileType(type));
			}
			key = addRTFile(fileSpec);
			if(Option.execVerbose) IO.println("SVM_SYSCALL.opfile: key=" + key + ", spec="+spec);
		}
		RTStack.push(IntegerValue.of(Type.T_INT, key));
		SVM_CALL_SYS.EXIT("OPFILE: ");
	}

	/// Close data set
	///
	///		Visible sysroutine("CLFILE") OPFILE;
	///		import range(0:MAX_KEY) key;    -- filekey;
	///		       infix(string) action;
	///		end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, action →
	/// 	   ...
	///
	/// The data set associated with 'key' is closed using the given 'action'.
	public static void clfile() {
		SVM_CALL_SYS.ENTER("CLFILE: ", 0, 4); // exportSize, importSize
		String action = RTStack.popString();
		int key = RTStack.popInt();
		if(key > 3) {
			Dataset fileSpec = lookup(key);
			fileSpec.clfile();
			FileAction fileAction = new FileAction(action);
			fileAction.doPurgeAction(fileSpec.fileName);
		}
		SVM_CALL_SYS.EXIT("CLFILE: ");
	}

	/// Printoutimage
	///
	///		Visible sysroutine("PRINTO") PRINTO;
	///		import range(1:MAX_KEY) key; infix(string) image; integer spc;  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, image'oaddr, image'ofst, image'nchr, spc →
	/// 	   ...
	///
	///	The image is printed from the current line position.
	///
	///		key:	The key associated with the data set.
	///		image:	The image to be printed.
	///		spc:	Vertical spacing.
	///
	/// Note: Key is ignored. 'key == 2' SYSOUT is always used.
	///
	/// Note: Negative Vertical spacing is not implemented. Only spc >= 1 is accepted, otherwise error.
	public static void printo() {
		SVM_CALL_SYS.ENTER("PRINTO: ", 0, 5); // exportSize, importSize

		int spc = RTStack.popInt();
		int nchr = RTStack.popInt();
		int ofst = RTStack.popInt();
		ObjectAddress chradr = (ObjectAddress) RTStack.pop();
		@SuppressWarnings("unused")
		int key = RTStack.popInt();
		ObjectAddress x = chradr.addOffset(ofst);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<nchr;i++) {
			IntegerValue ival = (IntegerValue) x.load(i);
			char c = (ival == null)? '.' : ((char) ival.value);
			sb.append(c);
		}
		String res = sb.toString().stripTrailing();
		if(Global.console != null)
			 Global.console.write(res+'\n');
		else IO.println(res);
		
		if(spc != 1) {
			if(spc < 1) {
				RTUtil.set_STATUS(19);
			} else {
				for(int i=1;i<spc;i++) IO.println();
			}
		}
		SVM_CALL_SYS.EXIT("PRINTO: ");
	}

	/// Inimage
	///
	///		Visible sysroutine("INIMAG") fINIMA;
	///		import range(1:MAX_KEY) key; infix(string) image;
	///		export integer filled  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, image'oaddr, image'ofst, image'nchr →
	/// 	   ..., filled
	///
	/// <pre>
	/// Key:    The key associated with the data set.
	/// Image:  Input buffer.
	/// Filled: The number of characters placed in image.
	/// </pre>
	///
	/// A record is read from the current position of the data set into the image. If the number of characters
	/// in the record exceeds the image length, the action taken is system dependent:
	///
	/// - If the system permits partial record read, image.length characters are read, filled := image.length,
	///   and status 34 is returned. In this case the next inimage (on this data set) should continue reading
	///   from the next position in the partially read record.
	///
	/// - If partial record reading is not possible, status error 12 is set and filled is set to zero; the remainder
	///   of the record is in this case skipped.
	///
	/// Except for the case of partial record reading the data set will be positioned at the sequentially next record.
	///
	/// Inimage is legal on infiles and directfiles only.
	///
	/// This routine may change the value of the global variable status to one of the values given in app. C.
	/// If the status returned is non-zero, filled must be zero, except for the partial read case discussed
	/// above (status 12).
	public static void INIMAG() {
		SVM_CALL_SYS.ENTER("INIMAG: ", 1, 4); // exportSize, importSize
		int nchr = RTStack.popInt();
		ObjectAddress chrAddr = RTStack.popGADDRasOADDR();
		int key = RTStack.popInt();
		int filled = 0;
		if(key == Dataset.KEY_SYSIN) {
			filled = Infile.sysinInimage(chrAddr, nchr);
		} else if(key > 3) {
			ImageFile spec = (ImageFile) lookup(key);
			filled = spec.inimage(chrAddr, nchr);
		} else {
			Util.IERR("");
		}
		RTStack.push(IntegerValue.of(Type.T_INT, filled));
		SVM_CALL_SYS.EXIT("INIMAG: ");
	}

	/// Outimage
	///
	///		Visible sysroutine("OUTIMA")  fUTIMA;
	///		import range(1:MAX_KEY) key; infix(string) img  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, img'oaddr, img'ofst, img'nchr →
	/// 	   ...
	///
	///
	///		Key: The key associated with the data set.
	/// 	img: A string to be output.
	///
	/// If the file is of type 2 or 4, image is copied into the record at the current position of the data set, and
	/// the data set is positioned at the sequentially next record. On printfiles the image is printed from the
	/// current print position, without vertical spacing (i.e. the next print position is the next position on the
	/// same line); this is utilised by the Simula breakoutimage procedure.
	///
	/// Outimage is legal on outfiles, printfiles and directfiles only.
	/// This routine may change the value of the global variable status to one of the values given in app. C.
	public static void OUTIMA() {
		SVM_CALL_SYS.ENTER("OUTIMA: ", 0, 4); // exportSize, importSize
		String image = RTStack.popString();
		int key = RTStack.popInt();
		if(key == Dataset.KEY_SYSOUT || key == Dataset.KEY_SYSTRACE) {
			Util.IERR("");
		} else if(key > 3) {
			ImageFile spec = (ImageFile) lookup(key);
			spec.outimage(image);
		} else {
			Util.IERR("");
		}
		SVM_CALL_SYS.EXIT("OUTIMA: ");
	}
	
	/// Breakoutimage
	///
	///		Visible sysroutine("BREAKO") BREAKO;
	///		import range(1:MAX_KEY) key; infix(string) img  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, img'oaddr, img'ofst, img'nchr →
	/// 	   ...
	///
	///
	///		Key: The key associated with the data set.
	/// 	img: A string to be output
	///
	/// The routine Breako will output the string img to the current record of the data set, beginning at the
	/// current position. The record should not be "closed" i.e. the next Breako (or Outima) will output to
	/// the same record, beginning at the new position. If this is not possible, Breako shall perform as Outima.
	///
	/// On display terminals, Breako will output the string from the current cursor position, and leave the
	/// cursor positioned after the last charcter of img. Note that trailing blanks (of img) shall be output.
	///
	/// On other external data sets, it may be necessary for the environment to buffer the output internally.
	///
	/// The routine is legal for outfiles and printfiles only.
	public static void BREAKO() {
		SVM_CALL_SYS.ENTER("BREAKO: ", 0, 4); // exportSize, importSize
		String image = RTStack.popString();
		int key = RTStack.popInt();
		if(key == Dataset.KEY_SYSOUT || key == Dataset.KEY_SYSTRACE) {
			if(Global.console != null)
				 Global.console.write(image);
			else
				System.out.print(image);
		} else if(key > 3) {
			ImageFile spec = (ImageFile) lookup(key);
			spec.breakOutimage(image);
		} else {
			Util.IERR("");
		}
		SVM_CALL_SYS.EXIT("BREAKO: ");
	}

	/// Inbyte
	///
	///		Visible sysroutine("INBYTE") fINBYT;
	///		import range(1:MAX_KEY) key; export range(0:MAX_BYT) byte  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key →
	/// 	   ..., byte
	///
	///	One byte is input from the current position of the data set,
	/// and the data set is positioned at the following byte.
	///
	///		Key: The key associated with the data set.
	///		Byte: Value input.
	public static void INBYTE() {
		SVM_CALL_SYS.ENTER("INBYTE: ", 1, 1); // exportSize, importSize
		int key = RTStack.popInt();
		Dataset spec = lookup(key);
		int byt = 0;
		if(spec instanceof Inbytefile ifile) {
			byt =ifile.inbyte();
		}
		else if(spec instanceof DirectBytefile dbfile) {
			byt = dbfile.inbyte();
		}
		else Util.IERR("");
		RTStack.push(IntegerValue.of(Type.T_INT, byt));
		SVM_CALL_SYS.EXIT("INBYTE: ");
	}

	/// Outbyte
	///
	///		Visible sysroutine("OUTBYT") fUTBYT;
	///		import range(1:MAX_KEY) key; range(0:MAX_BYT) byte  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, byte →
	/// 	   ...
	///
	///	One byte is output to the current position of the data set,
	/// and the data set is positioned at the following byte.
	///
	///		Key: The key associated with the data set.
	///		Byte: Value output.
	public static void OUTBYT() {
		SVM_CALL_SYS.ENTER("OUTBYT: ", 0, 2); // exportSize, importSize
		int byt = RTStack.popInt();
		int key = RTStack.popInt();
		Dataset spec = lookup(key);
		if(spec instanceof Outbytefile ofile) {
			ofile.outbyte(byt);
		}
		else if(spec instanceof DirectBytefile dbfile) {
			dbfile.outbyte(byt);
		}
		else Util.IERR("");
		SVM_CALL_SYS.EXIT("OUTBYT: ");
	}

	/// New page
	///
	///		Visible sysroutine("NEWPAG") NEWPAG;
	///		import range(1:MAX_KEY) key; end;
	///
	/// 	Runtime Stack
	/// 	   ..., key →
	/// 	   ...
	///
	///	The top-of-form action is performed on the data set, so that the next image will be printed on the
	///	first printable line of the following page.
	///
	/// This is legal on printfiles only.
	public static void newpag() {
		SVM_CALL_SYS.ENTER("NEWPAG: ", 0, 1); // exportSize, importSize
		int key = RTStack.popInt();
		if(key > 3) {
			Util.IERR("NOT IMPL");
		}
		SVM_CALL_SYS.EXIT("NEWPAG: ");
	}

	/// Locate record
	///
	///		Visible sysroutine("LOCATE") fLOCAT;
	///		import range(1:MAX_KEY) key; integer loc  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key, loc →
	/// 	   ...
	///
	///	key: The key associated with the data set.
	///	<br>loc: Indicates the next record (directfile) or byte (directbytefile) position to be accessed.
	///
	///	The position of the data set is changed so that the next record read or written will be record number
	///	loc of the data set (the first record is numbered 1).
	///
	/// Locate is legal on directfiles and directbytefiles only
	public static void LOCATE() {
		SVM_CALL_SYS.ENTER("LOCATE: ", 0, 2); // exportSize, importSize
		int loc = RTStack.popInt();
		int key = RTStack.popInt();
		if(key > 3) {
			Dataset spec = lookup(key);
			if(spec instanceof Directfile dfile) {
				dfile.locate(loc);
			}
			else if(spec instanceof DirectBytefile dbfile) {
				dbfile.locate(loc);
			}
			else Util.IERR("");
		}
		SVM_CALL_SYS.EXIT("LOCATE: ");
	}

	/// Maxloc
	///
	///		Visible sysroutine("MAXLOC") MXLOC;
	///		import range(1:MAX_KEY) key; export integer res  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key →
	/// 	   ..., res
	///
	///	key: The key associated with the data set.
	/// <br>res: The result
	///
	///	The routine Maxloc will give acces to the maximum value that can be used as parameter to Locate
	///	on the file referenced by key.
	///
	///	Maxloc is legal on directfiles and directbytefiles only.
	public static void MXLOC() {
		SVM_CALL_SYS.ENTER("MXLOC: ", 1, 1); // exportSize, importSize
		int key = RTStack.popInt();
		int maxloc = 0;
		if(key > 3) {
			Dataset spec = lookup(key);
			if(spec instanceof Directfile dfile) {
				maxloc = dfile.MAXLOC;
			}
			else if(spec instanceof DirectBytefile dbfile) {
				maxloc = dbfile.MAXLOC;
			}
			else Util.IERR("");
		}
		RTStack.push(IntegerValue.of(Type.T_INT, maxloc));
		SVM_CALL_SYS.EXIT("MXLOC: ");
	}

	/// Last location
	///
	///		Visible sysroutine("LSTLOC") LSTLOC;
	///		import range(1:MAX_KEY) key; export integer res  end;
	///
	/// 	Runtime Stack
	/// 	   ..., key →
	/// 	   ..., res
	///
	///	key: The key associated with the data set.
	/// <br>res: The result
	///
	///	The routine Lstloc will give access to the largest location of
	/// any written image in the file referenced by key.
	///
	///	Lstloc is legal on directfiles and directbytefiles only.
	public static void LSTLOC() {
		SVM_CALL_SYS.ENTER("LSTLOC: ", 1, 1); // exportSize, importSize
		int key = RTStack.popInt();
		int maxloc = 0;
		if(key > 3) {
			Dataset spec = lookup(key);
			if(spec instanceof Directfile dfile) {
				maxloc = dfile.lastloc();
			}
			else if(spec instanceof DirectBytefile dbfile) {
				maxloc = dbfile.lastloc();
			}
			else Util.IERR("");
		}
		RTStack.push(IntegerValue.of(Type.T_INT, maxloc));
		SVM_CALL_SYS.EXIT("LSTLOC: ");
	}

}
