----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/15/2016 02:32:36 AM
-- Design Name: 
-- Module Name: alu_fourbit - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
-- Description: 
-- 
-- Dependencies: 
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
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity alu_fourbit is
	port( a, b     		: in	STD_LOGIC_VECTOR(3 downto 0);
          conflgs       : in    STD_LOGIC_VECTOR(3 downto 0);
          res           : out    STD_LOGIC_VECTOR(3 downto 0);
          cout          : out    STD_LOGIC        );
end alu_fourbit;

architecture my_structure of alu_fourbit is

component ALU
	port( a, b, cin	   : in  STD_LOGIC;
        conflgs        : in 	STD_LOGIC_VECTOR(3 downto 0);
        res, cout      : out        STD_LOGIC);
end component;

signal c0, c1, c2, c3 : STD_LOGIC;

begin

c0 <= '0';
alu_elem0: ALU port map (a(0), b(0), c0, conflgs, res(0), c1);
alu_elem1: ALU port map (a(1), b(1), c1, conflgs, res(1), c2);
alu_elem2: ALU port map (a(2), b(2), c2, conflgs, res(2), c3);
alu_elem3: ALU port map (a(3), b(3), c3, conflgs, res(3), cout);


end my_structure;
