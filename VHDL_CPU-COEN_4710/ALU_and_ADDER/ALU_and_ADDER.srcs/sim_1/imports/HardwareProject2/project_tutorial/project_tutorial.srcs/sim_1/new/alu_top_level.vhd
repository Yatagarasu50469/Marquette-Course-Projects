----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/20/2016 08:53:15 AM
-- Design Name: 
-- Module Name: alu_top_level - Behavioral
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

entity alu_top_level is
	port( a, b     	                  : in     STD_LOGIC_VECTOR(31 downto 0);
          ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
          Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
          Shamt                       : in     STD_LOGIC_VECTOR(4 downto 0);
          Result                      : out    STD_LOGIC_VECTOR(31 downto 0);
          CarryOut, Overflow, Zero    : out    STD_LOGIC        );
end alu_top_level;

architecture my_structure of alu_top_level is

component alu_32bit is
	port( a, b     	              : in     STD_LOGIC_VECTOR(31 downto 0);
      ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
      Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
      Result                      : out    STD_LOGIC_VECTOR(31 downto 0);
      CarryOut, Overflow, Zero    : out    STD_LOGIC        );
end component;

component logic_unit is
	port( a, b     	                : in     STD_LOGIC_VECTOR(31 downto 0);
        ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
        Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
        Shamt                       : in     STD_LOGIC_VECTOR(4 downto 0);
        Result                      : out    STD_LOGIC_VECTOR(31 downto 0));
end component;

signal result_arithmetic, result_logic  : STD_LOGIC_VECTOR(31 downto 0);
signal carryout_s, overflow_s, zero_s   : STD_LOGIC;

begin




    process(a,b,ALUOp,Funct,Shamt,result_arithmetic,result_logic,carryout_s,overflow_s,zero_s)
    
    begin
        if ((ALUOp(1) = '1') and (Funct(3 downto 0) = "0100")) then  -- AND
            Result <= result_arithmetic;
            CarryOut <= carryout_s;
            Overflow <= overflow_s;
            Zero <= zero_s;
        elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "0101")) then  -- OR
            Result <= result_arithmetic;
            CarryOut <= carryout_s;
            Overflow <= overflow_s;
            Zero <= zero_s;
        elsif (ALUOp = "00") then  -- ADD
            Result <= result_arithmetic;
            CarryOut <= carryout_s;
            Overflow <= overflow_s;
            Zero <= zero_s;
        elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1010")) then  -- SLT 
            Result <= result_arithmetic;
            CarryOut <= carryout_s;
            Overflow <= overflow_s;
            Zero <= zero_s;
        elsif (ALUOp(0) = '1') then  -- SUB
            Result <= result_arithmetic;
            CarryOut <= carryout_s;
            Overflow <= overflow_s;
            Zero <= zero_s;
	    elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1111")) then  -- SRL
            Result <= result_logic;
            CarryOut <= '0';        -- Assuming that with logical operations, CarryOut, Overflow, and Zero are all set to 0
            Overflow <= '0';
            Zero <= '0';
        elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1110")) then  -- SLL
            Result <= result_logic;
            CarryOut <= '0';
            Overflow <= '0';
            Zero <= '0';            

    --   elsif (conflgs = "1100") then  -- NOR
    --        c0 <= '0';
    --        operation <= "00";
    --        ainvert <= '1';
    --        binvert <= '1';
        else                    -- none of the inputs match, error case
            Result <= "00000000000000000000000000000000";
            CarryOut <= '0';
            Overflow <= '0';
            Zero <= '0';
        end if;
    
    end process;
    
arithmeticUnit: alu_32bit port map (a,b,ALUOp,Funct,result_arithmetic,carryout_s,overflow_s,zero_s);
logicUnit: logic_unit port map (a,b,ALUOp,Funct,Shamt,result_logic);
    
end my_structure;
