----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/15/2016 02:51:12 AM
-- Design Name: 
-- Module Name: alu_fourbit_tb - Behavioral
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

entity TEST_ALU_FOURBIT is
end TEST_ALU_FOURBIT;

architecture MY_TEST of TEST_ALU_FOURBIT is

    component alu_fourbit is
    	port( a, b     		: in	STD_LOGIC_VECTOR(3 downto 0);
              conflgs       : in    STD_LOGIC_VECTOR(3 downto 0);
              res           : out    STD_LOGIC_VECTOR(3 downto 0);
              cout          : out    STD_LOGIC        );
    end component;


	for U1: alu_fourbit use entity WORK.ALU_FOURBIT(MY_STRUCTURE);
    signal      A_s, B_s          :    STD_LOGIC_VECTOR(3 downto 0);
    signal      CONFLGS_s         :    STD_LOGIC_VECTOR(3 downto 0);
    signal      RES_s             :    STD_LOGIC_VECTOR(3 downto 0);
    signal      COUT_s            :    STD_LOGIC        ;
	
	begin
	U1: alu_fourbit port map (A_S,B_s,CONFLGS_s,RES_s,COUT_s);
	
		process
		begin

-- AND tests
  		CONFLGS_s <= "0000";
  		A_s <= "0000";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0000";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0001";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0000";
  		B_s <= "0011";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0001";
  		B_s <= "0011";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0011";
  		B_s <= "0011";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0011";
  		B_s <= "0111";

		wait for 50 ns;

  		CONFLGS_s <= "0000";
  		A_s <= "0111";
  		B_s <= "0111";

		wait for 50 ns;



-- OR tests
  		CONFLGS_s <= "0001";
  		A_s <= "0000";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0000";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0000";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0000";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0001";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0001";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0001";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0001";
  		A_s <= "0001";
  		B_s <= "0001";

		wait for 50 ns;



-- ADD tests
  		CONFLGS_s <= "0010";
  		A_s <= "0000";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0000";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0000";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0000";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0001";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0001";
  		B_s <= "0000";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0001";
  		B_s <= "0001";

		wait for 50 ns;

  		CONFLGS_s <= "0010";
  		A_s <= "0001";
  		B_s <= "0001";

		wait for 50 ns;
		
  		CONFLGS_s <= "0010";
        A_s <= "1111";
        B_s <= "1111";

        wait for 50 ns;
  

    end process;
end MY_TEST;
