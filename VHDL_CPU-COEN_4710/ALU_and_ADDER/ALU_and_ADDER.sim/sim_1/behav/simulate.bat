@echo off
set xv_path=C:\\Xilinx\\Vivado\\2015.4\\bin
call %xv_path%/xsim TEST_TOP_LEVEL_behav -key {Behavioral:sim_1:Functional:TEST_TOP_LEVEL} -tclbatch TEST_TOP_LEVEL.tcl -log simulate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
