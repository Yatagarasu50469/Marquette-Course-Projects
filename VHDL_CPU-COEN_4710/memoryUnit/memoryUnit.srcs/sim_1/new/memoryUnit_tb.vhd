----------------------------------------------------------------------------------
-- Company: Marquette Univerity
-- Engineer: David Helminiak
-- 
-- Create Date: 04/17/2016 07:54:59 PM
-- Design Name: 
-- Module Name: memoryUnit_tb - Behavioral
-- Project Name: project2
-- Target Devices: xc7a35tcpg236-1
-- Description: test bench for memoryUnit.vhdl
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

entity memoryUnit_tb is
end memoryUnit_tb;

architecture Behavioral of memoryUnit_tb is
component memoryUnit
port (
address				: in std_logic_vector(13 downto 0);
data				: in std_logic_vector(31 downto 0);
clk, wEnable		: in std_logic;
output				: out std_logic_vector(31 downto 0)
);
end component;

--establish default values and clock period

signal address		: std_logic_vector(13 downto 0) := (others => '0');
signal data		    : std_logic_vector(31 downto 0) := (others => '0');
signal clk   		: std_logic := '0';
signal wEnable 		: std_logic := '0';
signal output		: std_logic_vector(31 downto 0) := (others => '0');

constant clk_period : time := 10 ns;

begin


--define test unit
memoryTestUnit : memoryUnit port map (
address => address,
clk => clk,
data => data,
wEnable => wEnable,
output => output
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
wait for 100 ns; --10 times clock period
--END TEST1

--TEST2
--begins about 100 ns
--output preloaded data
address <= "00000000000001"; --set address to 1
wait for clk_period;
address <= "00000000000010"; --set address to 2
wait for clk_period;
--END TEST2

--TEST3
--begins about 100 ns
--test write data to address
wEnable <= '1'; --set write Enable to 1
address <= "00000000000011"; --set address to 3
data <= "00000000000000000100000000000011"; --set data
wait for clk_period;
--now read the data back
wEnable <= '0'; --set write Enable to 0
data <= "00000100000000000000000000000011"; 
address <= "00000000000011"; --set address to 3
--END TEST3

wait for 100000 ns;
end process;
end Behavioral;
