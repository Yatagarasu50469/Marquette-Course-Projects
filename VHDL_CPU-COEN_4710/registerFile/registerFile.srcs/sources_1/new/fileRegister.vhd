----------------------------------------------------------------------------------
-- Company: Marquette University
-- Engineer: David Helminiak
-- 
-- Create Date: 04/11/2016 11:07:25 AM
-- Design Name: 
-- Module Name: fileRegister - fileRegisterArch
-- Project Name: project2
-- Target Devices: xc7a35tcpg236-1
-- Description: 32x32 bit file register
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity fileRegister is
port (
rReg1, rReg2, wReg          : in std_logic_vector(4 downto 0);
wData                       : in std_logic_vector(31 downto 0);
clk, wEnable 		        : in std_logic;
rData1, rData2              : out std_logic_vector(31 downto 0)
);
end fileRegister;

architecture fileRegisterArch of fileRegister is
type fileRegType is array (0 to 31) of std_logic_vector(31 downto 0);
signal fileReg : fileRegType := (others => X"00000000");
begin

fileRegProcess : process(clk)
begin

rData1 <= fileReg(to_integer(unsigned(rReg1)));
rData2 <= fileReg(to_integer(unsigned(rReg2)));
--NOTE: RISING EDGE ACTUALLY FALLING EDGE...STRANGE...
if (rising_edge(clk) and wEnable = '1') then
		fileReg(to_integer(unsigned(wReg))) <= wData;
end if;
end process;
end architecture fileRegisterArch;