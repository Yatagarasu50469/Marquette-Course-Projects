----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/14/2016 11:56:25 PM
-- Design Name: 
-- Module Name: alu_tb - Behavioral
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

entity TEST_ALU is
end TEST_ALU;

architecture MY_TEST of TEST_ALU is

	component ALU_NORMAL
    	port(a, b, CarryIn     : in 	STD_LOGIC; 
            Operation        : in     STD_LOGIC_VECTOR(1 downto 0);
            AInvert, BInvert    : in   STD_LOGIC;
            Result, CarryOut      : out        STD_LOGIC);
	end component;
	
	for U1: ALU_NORMAL use entity WORK.ALU_NORMAL(ARCH_FUNCTIONAL);

	signal A_s, B_s	: STD_LOGIC;
	signal CIN_s	: STD_LOGIC;
	signal OPERATION: STD_LOGIC_VECTOR(1 downto 0);
    signal AINVERT, BINVERT: STD_LOGIC;
	signal RES_s	: STD_LOGIC;
	signal COUT_s	: STD_LOGIC;

begin

	U1: ALU_NORMAL port map (A_s, B_s, CIN_s, OPERATION, AINVERT, BINVERT, RES_s, COUT_s);

    process

    begin

--  AND tests
  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "00";
  		A_s <= '0';
  		B_s <= '0';
  		CIN_s <= '0';

		wait for 10 ns;
		assert ( RES_s = '0'  ) report "Failed Case 0 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 0 - COUT" severity error;
		wait for 40 ns;

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "00";
  		A_s <= '0';
  		B_s <= '1';
  		CIN_s <= '0';

		wait for 10 ns;
		assert ( RES_s = '0'  ) report "Failed Case 0 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 0 - COUT" severity error;
		wait for 40 ns;
		
  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "00";
  		A_s <= '1';
  		B_s <= '0';
  		CIN_s <= '0';

		wait for 10 ns;
		assert ( RES_s = '0'  ) report "Failed Case 0 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 0 - COUT" severity error;
		wait for 40 ns;

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "00";
  		A_s <= '1';
  		B_s <= '1';
  		CIN_s <= '0';

		wait for 10 ns;
		assert ( RES_s = '1'  ) report "Failed Case 0 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 0 - COUT" severity error;
		wait for 40 ns;


-- OR tests

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "01";
  		A_s <= '0';
        B_s <= '0';
        CIN_s <= '0';
          
		wait for 10 ns;
		assert ( RES_s = '0'  ) report "Failed Case 1 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
		wait for 40 ns;

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "01";
  		A_s <= '0';
        B_s <= '1';
        CIN_s <= '0';
          
		wait for 10 ns;
		assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
		wait for 40 ns;

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "01";
  		A_s <= '1';
        B_s <= '0';
        CIN_s <= '0';
          
		wait for 10 ns;
		assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
		wait for 40 ns;

  		AINVERT <= '0';
  		BINVERT <= '0';
  		OPERATION <= "01";
  		A_s <= '1';
        B_s <= '1';
        CIN_s <= '0';
          
		wait for 10 ns;
		assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
		assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
		wait for 40 ns;
		
		

-- ADD tests		
		
  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '0';
        CIN_s <= '0';

        wait for 10 ns;
        assert ( RES_s = '0'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;		

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '0';
        CIN_s <= '1';

        wait for 10 ns;
        assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;        

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '1';
        CIN_s <= '0';

        wait for 10 ns;
        assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '1';
        CIN_s <= '1';

        wait for 10 ns;
        assert ( RES_s = '0'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '1' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '0';
        CIN_s <= '0';

        wait for 10 ns;
        assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '0' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '0';
        CIN_s <= '1';

        wait for 10 ns;
        assert ( RES_s = '0'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '1' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '1';
        CIN_s <= '0';

        wait for 10 ns;
        assert ( RES_s = '0'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '1' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;

  		AINVERT <= '0';
        BINVERT <= '0';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '1';
        CIN_s <= '1';

        wait for 10 ns;
        assert ( RES_s = '1'  ) report "Failed Case 1 - RES" severity error;
        assert ( COUT_s = '1' ) report "Failed Case 1 - COUT" severity error;
        wait for 40 ns;	               


-- SUB Tests

  		AINVERT <= '0';
        BINVERT <= '1';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '0';
        CIN_s <= '1';
        
        wait for 50 ns;

  		AINVERT <= '0';
        BINVERT <= '1';
        OPERATION <= "10";
  		A_s <= '0';
        B_s <= '1';
        CIN_s <= '1';
        
        wait for 50 ns;

  		AINVERT <= '0';
        BINVERT <= '1';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '0';
        CIN_s <= '1';
        
        wait for 50 ns;

  		AINVERT <= '0';
        BINVERT <= '1';
        OPERATION <= "10";
  		A_s <= '1';
        B_s <= '1';
        CIN_s <= '1';
        
        wait for 50 ns;


-- NOR tests

  		AINVERT <= '1';
        BINVERT <= '1';
        OPERATION <= "00";
  		A_s <= '0';
        B_s <= '0';
        CIN_s <= '0';
        
        wait for 50 ns;

  		AINVERT <= '1';
        BINVERT <= '1';
        OPERATION <= "00";
  		A_s <= '0';
        B_s <= '1';
        CIN_s <= '0';
        
        wait for 50 ns;

  		AINVERT <= '1';
        BINVERT <= '1';
        OPERATION <= "00";
  		A_s <= '1';
        B_s <= '0';
        CIN_s <= '0';
        
        wait for 50 ns;

  		AINVERT <= '1';
        BINVERT <= '1';
        OPERATION <= "00";
  		A_s <= '1';
        B_s <= '1';
        CIN_s <= '0';
        
        wait for 50 ns;

    end process;
end MY_TEST;
