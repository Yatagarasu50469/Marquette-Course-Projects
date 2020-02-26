@echo off
set xv_path=C:\\Xilinx\\Vivado\\2015.4\\bin
call %xv_path%/xelab  -wto bff4f492abe44009a3606ac3cdc5493a -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot TEST_TOP_LEVEL_behav xil_defaultlib.TEST_TOP_LEVEL -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
