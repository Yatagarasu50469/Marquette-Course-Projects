----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/20/2016 08:53:15 AM
-- Design Name: 
-- Module Name: alu_top_level_tb - Behavioral
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

entity TEST_TOP_LEVEL is
end TEST_TOP_LEVEL;

architecture MY_TEST of TEST_TOP_LEVEL is

component alu_top_level is
	port( a, b     	              : in     STD_LOGIC_VECTOR(31 downto 0);
          ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
          Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
          Shamt                       : in     STD_LOGIC_VECTOR(4 downto 0);
          Result                      : out    STD_LOGIC_VECTOR(31 downto 0);
          CarryOut, Overflow, Zero    : out    STD_LOGIC        );
end component;

	for U1: alu_top_level use entity WORK.ALU_TOP_LEVEL(MY_STRUCTURE);
    signal      A_s, B_s          :    STD_LOGIC_VECTOR(31 downto 0);
    signal      ALUOP_s           :    STD_LOGIC_VECTOR(1 downto 0);
    signal      FUNCT_s           :    STD_LOGIC_VECTOR(5 downto 0);
    signal      SHAMT_s           :    STD_LOGIC_VECTOR(4 downto 0);
    signal      RES_s             :    STD_LOGIC_VECTOR(31 downto 0);
    signal      CARRYOUT_s        :    STD_LOGIC;    
    signal      OVERFLOW_s        :    STD_LOGIC;    
    signal      ZERO_s            :    STD_LOGIC        ;
	
	begin
	U1: alu_top_level port map (A_S,B_s,ALUOP_s,FUNCT_s,SHAMT_s,RES_s,CARRYOUT_s,OVERFLOW_s,ZERO_s);
	
		process
		begin
        
-- AND tests 
  		ALUOP_s <= "10";
  		FUNCT_s <= "000100";
        SHAMT_s <= "00000";
  		A_s <= "00000000000000000000000000001111";
  		B_s <= "00000000000000000000000000000011";

		wait for 50 ns;
		
-- OR tests
  		ALUOP_s <= "11";
  		FUNCT_s <= "000101";
        SHAMT_s <= "00000";
  		A_s <= "00000000000000000000000000000100";
  		B_s <= "00000000000000000000000000000001";

		wait for 50 ns;



-- ADD tests
  		ALUOP_s <= "00";
        SHAMT_s <= "00000";
  		A_s <= "00000000000000000000000000000100";
  		B_s <= "00000000000000000000000000000001";

		wait for 50 ns;


-- SUB tests
  		ALUOP_s <= "01";
        SHAMT_s <= "00000";
  		A_s <= "00000000000000000000000000000100";
  		B_s <= "00000000000000000000000000000001";

		wait for 50 ns;


-- SLT tests
  		ALUOP_s <= "11";
        SHAMT_s <= "00000";
  		A_s <= "00000000000000000000000000000100";
  		B_s <= "00000000000000000000000000000001";

		wait for 50 ns;


-- SRL tests
  		ALUOP_s <= "10";
  		FUNCT_s <= "001111";
        SHAMT_s <= "00010";
  		A_s <= "00000000000000000000000010101010";
  		B_s <= "00000000000000000000000000000011";

		wait for 50 ns;


-- SLL tests

  		ALUOP_s <= "10";
  		FUNCT_s <= "001110";
        SHAMT_s <= "00010";
  		A_s <= "00000000000000000000000010101010";
  		B_s <= "00000000000000000000000000000011";

		wait for 50 ns;

        
    end process;
end MY_TEST;
