set
pause

rem echo off

cd "C:\Program Files\Java\jdk-17\bin"
dir
pause


set SRC=C:\GitHub\SimulaCompiler3\Simula\src
set BIN=C:\GitHub\SimulaCompiler3\Simula\bin17

javac -Xlint:deprecation -sourcepath %src% -d %BIN% %SRC%\simula\compiler\Simula.java

pause
