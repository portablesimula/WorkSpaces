/// The S-Port Simula Virtual Machine.
///
/// The Simula Virtual Machine consists of:
///
///	- a set of Data Segments of Values 
///
/// - a set of Program Segments of SVM-Instructions.
///
///   All SVM_Instruction have an 'execute' method which
///   will carry out its task and finally update the PSC. 
///
/// - a Runtime Stack of values
///
/// - a Program Sequence Control (PSC) pointer.
///
/// A SVM-Program is executed by a control loop like:
///
///   	try {
///			PSC = mainEntry;
///			while(true) PSC.execute();
///		} catch(EndProgram eprog) { return; }	
///
package svm;
