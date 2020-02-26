@echo off
set xv_path=C:\\Xilinx\\Vivado\\2015.4\\bin
call %xv_path%/xelab  -wto ed55cd18f1a8486181eebc74a84092fb -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot memoryUnit_tb_behav xil_defaultlib.memoryUnit_tb -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
