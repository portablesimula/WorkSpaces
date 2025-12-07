
@echo off
cd C:\Users\omyhr\Simula\Simula-2.0
set OPTION=-compilerMode viaJavaSource
set OPTION=%OPTION% -runtimeUserDir C:\GitHub\EclipseWorkSpaces\SimulaCompiler_JDK21\SimulaTestBatch\src\simulaSamples
::echo The Options are: %OPTION%

java -jar simula.jar %OPTION% C:/Users/omyhr/Simula/Simula-2.0/samples/InfectionDisease.sim

pause