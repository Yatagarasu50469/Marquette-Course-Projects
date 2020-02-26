----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/18/2016 11:43:39 PM
-- Design Name: 
-- Module Name: Control_Unit - Behavioral
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

entity Control_Unit is
    port (  instruction : in    STD_LOGIC_VECTOR(5 downto 0);
            RegDst      : out   STD_LOGIC;
            Jump        : out   STD_LOGIC;
            Branch      : out   STD_LOGIC;
            MemRead     : out   STD_LOGIC;
            MemtoReg    : out   STD_LOGIC;
            ALUOp       : out   STD_LOGIC_VECTOR(1 downto 0);
            MemWrite    : out   STD_LOGIC;
            ALUSrc      : out   STD_LOGIC;
            RegWrite    : out   STD_LOGIC      );
end Control_Unit;

architecture Control_Structure of Control_Unit is

begin
process(instruction)
begin
    RegDst <= ((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (not instruction(1)) and (not instruction(0)));
    Jump <= ((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (instruction(1)));
    Branch <= ((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (instruction(2)) and (not instruction(1)) and (not instruction(0)));
    MemRead <= ((instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (instruction(1)) and (instruction(0)));
    MemtoReg <= ((instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (instruction(1)) and (instruction(0)));
    ALUOp(0) <= ((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (instruction(2)) and (not instruction(1)) and (not instruction(0)));
    ALUOp(1) <= ((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (not instruction(1)) and (not instruction(0)));
    MemWrite <= ((instruction(5)) and (not instruction(4)) and (instruction(3)) and (not instruction(2)) and instruction(1) and instruction(0));
    ALUSrc <= (((instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (instruction(1)) and (instruction(0))) or ((instruction(5)) and (not instruction(4)) and (instruction(3)) and (not instruction(2)) and instruction(1) and instruction(0)) or ((not instruction(5)) and (instruction(3))));
    RegWrite <= (((not instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (not instruction(1)) and (not instruction(0)))or((instruction(5)) and (not instruction(4)) and (not instruction(3)) and (not instruction(2)) and (instruction(1)) and (instruction(0))) or ((not instruction(5)) and (instruction(3))));
end process;

end Control_Structure;
