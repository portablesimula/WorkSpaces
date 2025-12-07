
    Source of the Simula                                                         Simula source program
      Run Time System
            :                                                                        |     |     |
            V                                                                        V     V     V
   -----------------------                                                     --------------------------
   | Simuletta compiler  |           (attribute definitions)  <===>            |   Front end compiler   |
   -----------------------                                                     --------------------------
             :                                                                            |
             :                                                                            |
         S-program                                                                     S-program
             :                                                                            |
             :.............................                  -----------------------------'
                                          :                  |
                                          V                  V
                                      ----------------------------
      (module descr.) <===>           |      Code generator      |
                                      |      ( S-Compiler )      |
                                      ----------------------------
                                          :                  |
                                          :                  |
                                       Machine code / Object code
                                    depending upon the target system
                                          :                  |
                                          :                  |
                 ….........................                  |
                 :                                           |
                 V                                           V
               Simula                             ------------------------
              Run Time  ========>                 |  Executable program  |
               System                             ------------------------
               Library




To 'Bootstrap' the old SPORT Simula System, perform following steps:

1) Ensure that the new Open Source Simula System is available in  USR/Simula/Simula-2.0

2) Make the Simuletta FrontEnd Compiler:
	- In Project SimulettaFEC
		- Run: Make_SimulettaFEC_Jarfile.java
			- Output:  SimulettaFEC.jar   ===>   RELEASE_HOME: C:/SPORT
		- Run: FECmpTBatch.java
		- And finally run: FECmpRTS.java   to produce
			- SCode of the Simula Runtime System in C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/simulaRTS/SCode
			- Interface files:
				- "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/RTSINIT.ini"
				   C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FEC/src/fec/source/RTSINIT.ini
	
				- "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/RTS-FEC-INTERFACE1.def"
				- "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/RTS-FEC-INTERFACE2.def"
			
				- "C:/GitHub/EclipseWorkSpaces/S-Port-Simula/FILES/Attrs/FEC/PREDEF.atr"
		
3) Make the SPORT Simula FrontEnd Compiler:
	- In Project SimulaFEC run: Make_SimulaFEC_Jarfile.java
		- Output: C:/SPORT/SimulaFEC.jar
		
4) Compile Simula TestBatch to SCode:
	- In Project SimulaTestBatch run: Full_TestBatch2Scode.java   -- GIR MASSE FEIL-MELDINGER !!!
		- Output in: C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaTestBatch_FEC/src/simulaTestBatch/scode
	
5) Make and Test Common BackEnd Compiler:
	- In Project BEC
		- Run: Make_BEC_Jarfile.java
			- Output: CommonBEC.jar   ===>   RELEASE_HOME: C:/SPORT
		- Open bec.inlineTest in Project BEC
			- Run: RunMake_RTS.java
		- Run: RunFull_SML_TestBatch.java
			
6) Make S-PORT SIM Compiler
	- In Project SIM
		- Run: Make_SIM_Jarfile.java
		     - Output: C:/SPORT/SIM.jar  and an INLINE test is executed.
		- Run: RunFull_Simula_TestBatch
		- Run: C:/SPORT/runSimulaEditor.bat  ???
		
7) Make SPortSetup.jar
	- In Project MakeSetup
		- Run: MakeSetup
			- Output: SPortSetup.jar   or  SPortSetupEA.jar   in  C:/GitHub/github.io/setup
			- Output: sPortSetup.xml   in  C:/users/omyhr/.simula
			- Output: SPort-1.0 folder in  C:/users/omyhr/.simula













		
		
