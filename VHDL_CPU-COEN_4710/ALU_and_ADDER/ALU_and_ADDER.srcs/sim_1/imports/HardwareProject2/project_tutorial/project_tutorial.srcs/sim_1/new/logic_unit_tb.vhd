----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/20/2016 05:44:42 PM
-- Design Name: 
-- Module Name: logic_unit_tb - Behavioral
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

entity TEST_LOGIC_UNIT is
end TEST_LOGIC_UNIT;

architecture MY_TEST of TEST_LOGIC_UNIT is


    component logic_unit is
	   port( a, b     	                : in     STD_LOGIC_VECTOR(31 downto 0);
            ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
            Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
            Shamt                       : in     STD_LOGIC_VECTOR(4 downto 0);
            Result                      : out    STD_LOGIC_VECTOR(31 downto 0));
    end component;


	for U1: logic_unit use entity WORK.LOGIC_UNIT(MY_STRUCTURE);
    signal      A_s, B_s          :    STD_LOGIC_VECTOR(31 downto 0);
    signal      ALUOP_s           :    STD_LOGIC_VECTOR(1 downto 0);
    signal      FUNCT_s           :    STD_LOGIC_VECTOR(5 downto 0);
    signal      SHAMT_s           :    STD_LOGIC_VECTOR(4 downto 0);
    signal      RES_s             :    STD_LOGIC_VECTOR(31 downto 0);
	
	begin
	U1: logic_unit port map (A_S,B_s,ALUOP_s,FUNCT_s,SHAMT_s,RES_s);
	
		process
	
		begin
		
-- SRL Tests		
  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00000";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;
 
  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00001";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00010";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00100";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "01000";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "10000";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00011";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "00110";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "01100";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "11000";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001111";
        SHAMT_s <= "11111";
        A_s <= "10101010101010101010101010101010";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;
        
        
-- SLL Test

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "00001";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "00010";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "00100";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "01000";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "10000";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "00011";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "00110";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "01100";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;

  		ALUOP_s <= "10";
        FUNCT_s <= "001110";
        SHAMT_s <= "11000";
        A_s <= "01010101010101010101010101010101";
        B_s <= "00000000000000000000000000000000";

        wait for 50 ns;





    end process;

end MY_TEST;
