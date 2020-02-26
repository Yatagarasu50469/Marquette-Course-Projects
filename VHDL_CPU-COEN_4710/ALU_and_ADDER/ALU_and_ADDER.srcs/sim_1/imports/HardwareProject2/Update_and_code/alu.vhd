----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/14/2016 11:56:25 PM
-- Design Name: 
-- Module Name: alu - Behavioral
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

entity alu is
	port(a, b, cin     : in 	STD_LOGIC; 
        conflgs        : in 	STD_LOGIC_VECTOR(3 downto 0);
        res, cout      : out    	STD_LOGIC);
end alu;

architecture alu_dataflow of alu is

begin

process (a, b, cin, conflgs)

begin

-- sum <= (x xor y) xor cin;
-- cout  <= (x and y) or (x and cin) or (y and cin);

    if (conflgs = "0000") then -- AND
        res <= a and b;
        cout <= '0';
    elsif (conflgs = "0001") then -- OR
        res <= a or b;
        cout <= '0';
    elsif (conflgs = "0010") then -- ADD
        res <= ((((not a) and (not b)) and cin) or (a and ((not b) and (not cin))) or (((not a) and b) and (not cin)) or ((a and b) and cin));
        cout <= ((a and b) or (a and cin) or (b and cin));
--    elsif (conflgs = '0110') then -- SUB
--    elsif (conflgs = '0111') then -- SLT
--    elsif (conflgs = '1100') then -- NOR
    end if;
end process;

end alu_dataflow;
