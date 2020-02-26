@echo off
set xv_path=C:\\Xilinx\\Vivado\\2015.4\\bin
call %xv_path%/xelab  -wto 3ba87a0deb56408fb50f45d2c7515f01 -m64 --debug typical --relax --mt 2 -L xil_defaultlib -L secureip --snapshot fileRegister_tb_behav xil_defaultlib.fileRegister_tb -log elaborate.log
if "%errorlevel%"=="0" goto SUCCESS
if "%errorlevel%"=="1" goto END
:END
exit 1
:SUCCESS
exit 0
