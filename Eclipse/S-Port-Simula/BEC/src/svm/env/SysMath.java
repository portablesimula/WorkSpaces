/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/

package svm.env;

import bec.scode.Type;
import bec.util.Util;
import svm.RTStack;
import svm.instruction.SVM_CALL_SYS;
import svm.value.IntegerValue;
import svm.value.LongRealValue;
import svm.value.RealValue;

/// Mathematical library procedures
///
/// The following mathematical library routines are defined to cover the same standard procedures in SIMULA.
///
/// These routines may change the value of the global variable status to one of the values given in appendix C.
///
/// The routines correspond to the similarly named SIMULA Standard functions.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/SysMath.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public abstract class SysMath {

	/** Default Constructor */ public SysMath() {} 

	/// Real Addepsilon
	///
	///		Visible sysroutine("RADDEP") RADDEP;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument incremented by the smallest positive value,
	/// such that the result is not equal to the argument within the precision of the implementation.
	///
	/// Thus, for all positive values of "eps",
	///
	///		E-eps <= subepsilon(E) < E < addepsilon(E) <= E+eps
	///
	public static void raddep() {
		SVM_CALL_SYS.ENTER("RADDEP: ", 1, 1); // exportSize, importSize
		float val = RTStack.popReal();
		val = Math.nextUp(val);
		RTStack.push(RealValue.of(val));
		SVM_CALL_SYS.EXIT("RADDEP: ");
	}

	/// Long real Addepsilon
	///
	///		Visible sysroutine("DADDEP") DADDEP;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument incremented by the smallest positive value,
	/// such that the result is not equal to the argument within the precision of the implementation.
	///
	/// Thus, for all positive values of "eps",
	///
	///		E-eps <= subepsilon(E) < E < addepsilon(E) <= E+eps
	///
	public static void daddep() {
		SVM_CALL_SYS.ENTER("DADDEP: ", 1, 1); // exportSize, importSize
		double val = RTStack.popLongReal();
		val = Math.nextUp(val);
		RTStack.push(LongRealValue.of(val));
		SVM_CALL_SYS.EXIT("DADDEP: ");
	}

	/// Real Subepsilon
	///
	///		Visible sysroutine("RSUBEP") RSUBEP;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument decremented by the smallest positive value,
	/// such that the result is not equal to the argument within the precision of the implementation.
	///
	/// Thus, for all positive values of "eps",
	///
	///		E-eps <= subepsilon(E) < E < addepsilon(E) <= E+eps
	///
	public static void rsubep() {
		SVM_CALL_SYS.ENTER("RSUBEP: ", 1, 1); // exportSize, importSize
		float val = RTStack.popReal();
		val = Math.nextDown(val);
		RTStack.push(RealValue.of(val));
		SVM_CALL_SYS.EXIT("RSUBEP: ");
	}

	/// Long real Subepsilon
	///
	///		Visible sysroutine("DSUBEP") DSUBEP;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument decremented by the smallest positive value,
	/// such that the result is not equal to the argument within the precision of the implementation.
	///
	/// Thus, for all positive values of "eps",
	///
	///		E-eps <= subepsilon(E) < E < addepsilon(E) <= E+eps
	///
	public static void dsubep() {
		SVM_CALL_SYS.ENTER("DSUBEP: ", 1, 1); // exportSize, importSize
		double val = RTStack.popLongReal();
		val = Math.nextDown(val);
		RTStack.push(LongRealValue.of(val));
		SVM_CALL_SYS.EXIT("DSUBEP: ");
	}

	/// Integer raised to the integer power
	///
	///		Visible sysroutine("IIPOWR") IIPOWR; --- v:=b**x
	///		import integer b,x; export integer v  end;
	///
	/// 	Runtime Stack
	/// 	   ..., b, x →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument 'b' raised to the 'x' power
	///
	public static void iipowr() {
		SVM_CALL_SYS.ENTER("IIPOWR: ", 1, 2); // exportSize, importSize
		int x = RTStack.popInt();
		int b = RTStack.popInt();
		int res = IPOW(b, x);
		RTStack.push(IntegerValue.of(Type.T_INT, res));
		SVM_CALL_SYS.EXIT("IIPOWR: ");
	}

	/// Integer modulo operation
	///
	///	 Visible sysroutine("MODULO") MOD;
	///	 import integer x,y; export integer val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., x, y →
	/// 	   ..., result
	///
	/// The modulo operator in Java, denoted by the percent sign (%),
	/// calculates the remainder of a division operation.
	/// <br>It is used with the following syntax:
	///
	///		 operand1 % operand2,
	///
	/// where operand1 is the dividend and operand2 is the divisor.
	///
	/// The result, which is pushed onto the Runtime stack, is the remainder when operand1 is divided by operand2. 
	///
	/// Simula Standard:
	///
	///		integer procedure mod(i,j); integer i,j; begin
	///			integer res;
	///			res := i - (i//j)*j;
	///			mod := if res = 0 then 0
	///			else if sign(res) <> sign(j) then res+j
	///			else res
	///		end mod;
	///The result is the mathematical modulo value of the arguments.
	public static void modulo() {
		SVM_CALL_SYS.ENTER("MODULO: ", 1, 2); // exportSize, importSize
		int y = RTStack.popInt();
		int x = RTStack.popInt();
		int res = x % y;
//		IO.println("SysMath.modulo: "+x+" mod "+y+" ===> "+res);
		if(res == 0); //OK
		else if(sign(res) != sign(y)) res = res + y;
		
//		int res = y % x;
		RTStack.push(IntegerValue.of(Type.T_INT, res));
		SVM_CALL_SYS.EXIT("MODULO: ");
	}
	
	/// Utility: sign
	/// @param arg the argument
	/// @return +1 if arg > 0, otherwise -1
	private static int sign(int arg) {
		if(arg < 0) return -1;
		if(arg > 0) return +1;
		return 0;
	}

    //*******************************************************************************
    //*** IPOW - Integer Power: b ** x
    //*******************************************************************************
	/// Utility: Integer Power: b ** x
	/// @param base argument base
	/// @param x argument x
	/// @return Returns the value of 'base' raised to the power of 'x'
	private static int IPOW(final long base, long x) {
		if (x == 0) {
			if (base == 0)
				Util.ERROR("Exponentiation: " + base + " ** " + x + "  Result is undefined.");
			return (1); // any ** 0 ==> 1
		} else if (x < 0)
			Util.ERROR("Exponentiation: " + base + " ** " + x + "  Result is undefined.");
		else if (base == 0)
			return (0); // 0 ** non_zero ==> 0
		
		long res=(long) Math.pow((double)base,(double)x);
		if(res > Integer.MAX_VALUE || res < Integer.MIN_VALUE)
			Util.ERROR("Arithmetic overflow: "+base+" ** "+x+" ==> "+res
					+" which is outside integer value range["+Integer.MIN_VALUE+':'+Integer.MAX_VALUE+']');
		return((int)res);
	}

	/// Real raised to the integer power
	///
	///		Visible sysroutine("RIPOWR") RIPOWR; --- v:=b**x
	///		import real b; integer x; export real v  end;
	///
	/// 	Runtime Stack
	/// 	   ..., b, x →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument 'b' raised to the 'x' power
	///
	public static void ripowr() {
		SVM_CALL_SYS.ENTER("RIPOWR: ", 1, 2); // exportSize, importSize
		int x = RTStack.popInt();
		float b = RTStack.popReal();
		double res = Math.pow(b, x);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("RIPOWR: ");
	}

	/// Real raised to the real power
	///
	///		Visible sysroutine("RRPOWR") RRPOWR; --- v:=b**x
	///		import real b; real x; export real v  end;
	///
	/// 	Runtime Stack
	/// 	   ..., b, x →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument 'b' raised to the 'x' power
	///
	public static void rrpowr() {
		SVM_CALL_SYS.ENTER("RRPOWR: ", 1, 2); // exportSize, importSize
		float x = RTStack.popReal();
		float b = RTStack.popReal();
		float res = (float) Math.pow(b, x);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("RRPOWR: ");
	}

	/// Long real raised to the integer power
	///
	///		Visible sysroutine("DIPOWR") DIPOWR; --- v:=b**x
	///		import long real b; integer x; export long real v  end;
	///
	/// 	Runtime Stack
	/// 	   ..., b, x →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument 'b' raised to the 'x' power
	///
	public static void dipowr() {
		SVM_CALL_SYS.ENTER("DIPOWR: ", 1, 2); // exportSize, importSize
		int x = RTStack.popInt();
		double b = RTStack.popLongReal();
		double res = Math.pow(b, x);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("DIPOWR: ");
	}

	/// Long real raised to the long real power
	///
	///		Visible sysroutine("DDPOWR") DDPOWR; --- v:=b**x
	///		import long real b; long real x; export long real v  end;
	///
	/// 	Runtime Stack
	/// 	   ..., b, x →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the value of the argument 'b' raised to the 'x' power
	///
	public static void ddpowr() {
		SVM_CALL_SYS.ENTER("DDPOWR: ", 1, 2); // exportSize, importSize
		double x = RTStack.popLongReal();
		double b = RTStack.popLongReal();
		double res = Math.pow(b, x);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("DDPOWR: ");
	}


	/// Real Square root
	///
	///		Visible sysroutine("RSQROO") RSQROO;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Square root of the argument 'arg'
	///
	public static void rsqroo() {
		SVM_CALL_SYS.ENTER("RSQROO: ", 1, 1); // exportSize, importSize
		float r = RTStack.popReal();
		float res = (float) Math.sqrt(r);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("RSQROO: ");
	}

	/// Long real Square root
	///
	///		Visible sysroutine("SQROOT") SQROOT;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Square root of the argument 'arg'
	///
	public static void sqroot() {
		SVM_CALL_SYS.ENTER("SQROOT: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.sqrt(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("SQROOT: ");
	}

	/// Real natural logarithm 
	///
	///		Visible sysroutine("RLOGAR") RLOGAR;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Logaritms of the argument 'arg'
	///
	public static void rlogar() {
		SVM_CALL_SYS.ENTER("RLOGAR: ", 1, 1); // exportSize, importSize
		float r = RTStack.popReal();
		double res = Math.log(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("RLOGAR: ");
	}

	/// Long real  natural logarithm 
	///
	///		Visible sysroutine("LOGARI") LOGARI;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Logaritms of the argument 'arg'
	///
	public static void logari() {
		SVM_CALL_SYS.ENTER("LOGARI: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.log(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("LOGARI: ");
	}

// Visible sysroutine("RLOG10") RLOG10;
// import real arg; export real val  end;

	/// Real Logaritm base 10
	///
	///		Visible sysroutine("DLOG10") DLOG10;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Log10 of the argument 'arg'
	///
	public static void dlog10() {
		SVM_CALL_SYS.ENTER("DLOG10: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.log10(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("DLOG10: ");
	}

	/// Real Exponentiation
	///
	///		Visible sysroutine("REXPON") REXPON;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Exponentiation of the argument 'arg'
	///
	public static void rexpon() {
		SVM_CALL_SYS.ENTER("REXPON: ", 1, 1); // exportSize, importSize
//		float r = (float) RTStack.popLongReal();
		float r = RTStack.popReal();
		float res = (float) Math.exp(r);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("REXPON: ");
	}

	/// Real Exponentiation
	///
	///		Visible sysroutine("EXPONE") EXPONE;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Exponentiation of the argument 'arg'
	///
	public static void expone() {
		SVM_CALL_SYS.ENTER("EXPONE: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.exp(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("EXPONE: ");
	}

	/// Real Sine
	///
	///		Visible sysroutine("RSINUS") RSINUS;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Sine of the argument 'arg'
	///
	public static void rsinus() {
		SVM_CALL_SYS.ENTER("RSINUS: ", 1, 1); // exportSize, importSize
		float r = RTStack.popReal();
		float res = (float) Math.sin(r);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("RSINUS: ");
	}

// Visible sysroutine("SINUSR") SINUSR;
// import long real arg; export long real val  end;
	/// Long real Sine
	///
	///		Visible sysroutine("SINUSR") SINUSR;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Sine of the argument 'arg'
	///
	public static void sinusr() {
		SVM_CALL_SYS.ENTER("SINUSR: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.sin(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("SINUSR: ");
	}

	/// Real Cosine
	///
	///		Visible sysroutine("RCOSIN") RCOSIN;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Cosine of the argument 'arg'
	///
	public static void rcosin() {
		SVM_CALL_SYS.ENTER("RCOSIN: ", 1, 1); // exportSize, importSize
		float r = RTStack.popReal();
		float res = (float) Math.cos(r);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("RCOSIN: ");
	}

	/// Long real Cosine
	///
	///		Visible sysroutine("COSINU") COSINU;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Cosine of the argument 'arg'
	///
	public static void cosinu() {
		SVM_CALL_SYS.ENTER("COSINU: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.cos(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("COSINU: ");
	}

// Visible sysroutine("RTANGN") RTANGN;
// import real arg; export real val  end;

// Visible sysroutine("TANGEN") TANGEN;
// import long real arg; export long real val  end;

// Visible sysroutine("RCOTAN") COTANR;
// import real arg; export real val  end;

// Visible sysroutine("COTANG") COTANG;
// import long real arg; export long real val  end;

	/// Real Arctangent
	///
	///		Visible sysroutine("RARTAN") RARTAN;
	///		import real arg; export real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Arctangent of the argument 'arg'
	///
	public static void rartan() {
		SVM_CALL_SYS.ENTER("RARTAN: ", 1, 1); // exportSize, importSize
		float r = RTStack.popReal();
		float res = (float) Math.atan(r);
		RTStack.push(RealValue.of(res));
		SVM_CALL_SYS.EXIT("RARTAN: ");
	}

// Visible sysroutine("ARCTAN") ARCTAN;
// import long real arg; export long real val  end;
	/// Long real Arctangent
	///
	///		Visible sysroutine("RARTAN") RARTAN;
	///		import long real arg; export long real val  end;
	///
	/// 	Runtime Stack
	/// 	   ..., arg →
	/// 	   ..., result
	///
	/// The result, which is pushed onto the Runtime stack, is the Arctangent of the argument 'arg'
	///
	public static void arctan() {
		SVM_CALL_SYS.ENTER("ARCTAN: ", 1, 1); // exportSize, importSize
		double r = RTStack.popLongReal();
		double res = Math.atan(r);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("ARCTAN: ");
	}

// Visible sysroutine("RARCOS") RARCOS;
// import real arg; export real val  end;

// Visible sysroutine("ARCCOS") ARCCOS;
// import long real arg; export long real val  end;

// Visible sysroutine("RARSIN") RARSIN;
// import real arg; export real val  end;

// Visible sysroutine("ARCSIN") ARCSIN;
// import long real arg; export long real val  end;

// Visible sysroutine("RATAN2") ATAN2R;
// import real y,x; export real val  end;

// Visible sysroutine("ATAN2") ATAN2;
// import long real y,x; export long real val  end;

// Visible sysroutine("RSINH") SINHR;
// import real arg; export real val  end;

// Visible sysroutine("SINH") SINH;
// import long real arg; export long real val  end;

// Visible sysroutine("RCOSH") COSHR;
// import real arg; export real val  end;

// Visible sysroutine("COSH") COSH;
// import long real arg; export long real val  end;

// Visible sysroutine("RTANH") TANHR;
// import real arg; export real val  end;

// Visible sysroutine("TANH") TANH;
// import long real arg; export long real val  end;

}
