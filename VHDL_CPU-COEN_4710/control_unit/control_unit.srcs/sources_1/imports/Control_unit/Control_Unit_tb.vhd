----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/18/2016 11:48:03 PM
-- Design Name: 
-- Module Name: Control_Unit_tb - Behavioral
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

entity Control_Unit_tb is
end Control_Unit_tb;

architecture My_Test of Control_Unit_tb is
    component Control_Unit
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
    end component;
    
    for U1 : Control_Unit use entity WORK.CONTROL_UNIT(CONTROL_STRUCTURE);
    signal      INSTRUCTION_s   :   STD_LOGIC_VECTOR(5 downto 0);
    signal      REGDST_s        :   STD_LOGIC;
    signal      JUMP_s          :   STD_LOGIC;
    signal      BRANCH_s        :   STD_LOGIC;
    signal      MEMREAD_s       :   STD_LOGIC;
    signal      MEMTOREG_s      :   STD_LOGIC;
    signal      ALUOP_s         :   STD_LOGIC_VECTOR(1 downto 0);
    signal      MEMWRITE_s      :   STD_LOGIC;
    signal      ALUSRC_s        :   STD_LOGIC;
    signal      REGWRITE_s      :   STD_LOGIC;
begin
    U1: Control_Unit port map (INSTRUCTION_s, REGDST_s, JUMP_s, BRANCH_s, MEMREAD_s, MEMTOREG_s, ALUOP_s, MEMWRITE_s, ALUSRC_s, REGWRITE_s);
 
    process
    begin
    
    --test for R types
    INSTRUCTION_s <= "000000";
    wait for 50 ns;
    
    --test for ADDI
    INSTRUCTION_s <= "001000";
    wait for 50 ns;
    
    --test for ORI
    INSTRUCTION_s <= "001101";
    wait for 50 ns;
    
    --test for LUI
    INSTRUCTION_s <= "001111";
    wait for 50 ns;
    
    --test for LW 
    INSTRUCTION_s <= "100011";
    wait for 50 ns;
    
    --test for SW
    INSTRUCTION_s <= "101011";
    wait for 50 ns;
    
    --test for BEQ
    INSTRUCTION_s <= "000100";
    wait for 50 ns;
    
    --test for JAL
    INSTRUCTION_s <= "000011";
    wait for 50 ns;
    
    
    end process;
end My_Test;
