----------------------------------------------------------------------------------
-- Company: Marquette University
-- Engineer: David Helminiak
-- 
-- Create Date: 04/10/2016 07:21:54 PM
-- Design Name: 
-- Module Name: memoryUnit - memoryUnitArch
-- Project Name: project2
-- Target Devices: xc7a35tcpg236-1
-- Description: 16k x 32 Static Random Access Memory Unit
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

entity memoryUnit is
port (
address				: in std_logic_vector(13 downto 0);
data				: in std_logic_vector(31 downto 0);
clk, wEnable		: in std_logic;
output				: out std_logic_vector(31 downto 0)
);
end memoryUnit;

architecture memoryUnitArch of memoryUnit is
type memUnitType is array (1023 downto 0) of std_logic_vector (31 downto 0);
type memArrayType is array (15 downto 0) of memUnitType;
signal memArray : MemArrayType; 


type array_type is array (3 downto 0) of std_logic_vector (9 downto 0);
signal MY_ARRAY : array_type;
shared variable i : integer := 0;
shared variable blk : integer := 0;
shared variable blk2 : integer := 0;
begin

--START MEMORY PRE-LOAD
--Memory should be preloaded here
--memArray(specify block from 0 to 15 (1-16Kb)(specify block from 0 to 1023 (in each single Kb) <= "32bitdata";
memArray(0)(0)<= "00000000000000000000000000000010";


--END MEMORY PRE-LOAD

memUnitProcess : process(clk)
begin
i := to_integer(unsigned(address));

if (i >= 1023) then --1
blk := blk + 1;
blk2 := i - 1023;
end if;

if (i >= 2047) then --2
blk := blk + 1;
blk2 := i - 2047;
end if;

if (i >= 3071) then --3
blk := blk + 1;
blk2 := i - 3071;
end if;

if (i >= 4095) then --4
blk := blk + 1;
blk2 := i - 4095;
end if;

if (i >= 5119) then --5
blk := blk + 1;
blk2 := i - 5119;
end if;

if (i >= 6143) then --6
blk := blk + 1;
blk2 := i - 6143;
end if;

if (i >= 7167) then --7
blk := blk + 1;
blk2 := i - 7167;
end if;

if (i >= 8191) then --8
blk := blk + 1;
blk2 := i - 8191;
end if;

if (i >= 9215) then --9
blk := blk + 1;
blk2 := i - 9215;
end if;

if (i >= 10239) then --10
blk := blk + 1;
blk2 := i - 10239;
end if;

if (i >= 11263) then --11
blk := blk + 1;
blk2 := i - 11263;
end if;

if (i >= 12287) then --12
blk := blk + 1;
blk2 := i - 12287;
end if;

if (i >= 13311) then --13
blk := blk + 1;
blk2 := i - 13311;
end if;

if (i >= 14335) then --14
blk := blk + 1;
blk2 := i - 14335;
end if;

if (i >= 15359) then --15
blk := blk + 1;
blk2 := i - 15359;
end if;

if (wEnable = '0') then
    --disable either the following line or the next such labeled item to allow Synthesis
    output <= memArray(blk)(blk2);
end if;

if (rising_edge(clk) and wEnable = '1') then
    --disable either the following line or the previously such labeled item to allow Synthesis
    memArray(blk)(blk2) <= data;
end if;

end process;
end architecture memoryUnitArch;