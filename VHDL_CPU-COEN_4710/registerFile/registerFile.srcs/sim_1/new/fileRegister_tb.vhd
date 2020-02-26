----------------------------------------------------------------------------------
-- Company: Marquette Univerity
-- Engineer: David Helminiak
-- 
-- Create Date: 04/17/2016 01:15:00 PM
-- Design Name: 
-- Module Name: fileRegister_tb - Behavioral
-- Project Name: project2
-- Target Devices: xc7a35tcpg236-1
-- Description: test bench for fileRegister.vhdl
-- 
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity fileRegister_tb is
end fileRegister_tb;

architecture Behavioral of fileRegister_tb is
component fileRegister
  port (
  rReg1, rReg2, wReg  : in std_logic_vector(4 downto 0);
  wData               : in std_logic_vector(31 downto 0);
  clk, wEnable        : in std_logic;
  rData1, rData2      : out std_logic_vector(31 downto 0)
  );
end component;

--establish default values and clock period
signal rReg1        : std_logic_vector(4 downto 0) := (others => '0');
signal rReg2        : std_logic_vector(4 downto 0) := (others => '0');
signal wReg         : std_logic_vector(4 downto 0) := (others => '0');
signal wData        : std_logic_vector(31 downto 0) := (others => '0');
signal clk          : std_logic := '0';
signal wEnable      : std_logic := '0';
signal rData1       : std_logic_vector(31 downto 0) := (others => '0');
signal rData2       : std_logic_vector(31 downto 0) := (others => '0');

constant clk_period : time := 10 ns;

begin

--define test unit
fileRegisterUnit : fileRegister port map (
rReg1 => rReg1,
rReg2 => rReg2,
wReg => wReg,
wData => wData,
clk => clk,
wEnable => wEnable,
rData1 => rData1,
rData2 => rData2
);

clk_process : process --Set up a clock
begin
clk <= '0'; --set clock equal to zero
wait for clk_period/2; --for half of the clock period
clk <= '1'; --then set clock equal to one
wait for clk_period/2; --for the other half of the clock period
end process;

--begins about 0 ns
test_bench : process 
begin
--TEST1
--check that the clock is functioning correctly and that otherwise nothing happens
wait for 100 ns; --reset period
--END TEST1

--TEST2
--begins about 100 ns
--check write with enable bit on
--max data write of 2^32 = 4294967296 or 0x100000000
--test writes 1048575 or 0xFFFFF to register 0
--32 registers avaliable

--write to register 0
wEnable <= '1'; --set the write enable bit to 1
rReg1 <= "11111"; --send 31 to read reg1
rReg2 <= "11111"; --send 31 to read reg2
wReg <= "00000"; --send 0 to write reg
wData <= X"00000001"; -- send data to be written to wData
wait for clk_period;

--write to register 1
wReg <= "00001"; --send 1 to write reg
wData <= X"00001000"; -- send data to be written to wData
wait for clk_period;

--write to register 2
wReg <= "00010"; --send 2 to write reg
wData <= X"01000001"; -- send data to be written to wData
wait for clk_period;

--write to register 3
wReg <= "00011"; --send 2 to write reg
wData <= X"01001001"; -- send data to be written to wData
wait for clk_period;

wait for 100 ns; --wait for 10 times clock period to pass
--END TEST2


--TEST3
--begins about 200 ns
--check read and for a successful write

--read registers 0 and 1
wEnable <= '0'; --set the write enable bit to 0
rReg1 <= "00000"; --send 0 to read reg1
rReg2 <= "00001"; --send 1 to read reg2
wait for clk_period;

--read registers 2 and 3
rReg1 <= "00010"; --send 2 to read reg1
rReg2 <= "00011"; --send 3 to read reg2
wait for clk_period;

wait for 100 ns; --wait for 10 times clock period to pass
--END TEST3

--TEST4
--begins about 300 ns
--clear register data

--clear register 0
wEnable <= '1'; --set the write enable bit to 0
rReg1 <= "11111"; --send 31 to read reg1
rReg2 <= "11111"; --send 31 to read reg2
wReg <= "00000"; --send 0 to write reg
wData <= X"00000000"; -- send data to be written to wData
wait for clk_period;

--clear register 1
wReg <= "00001"; --send 0 to write reg
wData <= X"00000000"; -- send data to be written to wData
wait for clk_period;

--clear register 2
wReg <= "00010"; --send 0 to write reg
wData <= X"00000000"; -- send data to be written to wData
wait for clk_period;

--clear register 3
wReg <= "00011"; --send 0 to write reg
wData <= X"00000000"; -- send data to be written to wData
wait for clk_period;

wait for 100 ns; --wait for 10 times clock period to pass
--END TEST4

--TEST5
--begins about 400 ns
--attempt to write with enable bit set to zero
wEnable <= '0'; --set the write enable bit to 0
wReg <= "00000"; --send 0 to write reg
wData <= X"1FFFFFFF"; -- send data to be written to wData
wait for clk_period;
wait for 100 ns; --wait for 10 times clock period to pass
--END TEST5

wait for 100000 ns;
end process;
end Behavioral;
