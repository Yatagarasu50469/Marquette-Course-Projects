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

entity alu_32bit is
	port( a, b     	              : in     STD_LOGIC_VECTOR(31 downto 0);
      ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
      Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
      Result                      : out    STD_LOGIC_VECTOR(31 downto 0);
      CarryOut, Overflow, Zero    : out    STD_LOGIC        );
end alu_32bit;

architecture my_structure of alu_32bit is

component ALU_NORMAL
	port(a, b, CarryIn          : in 	 STD_LOGIC; 
        Operation               : in     STD_LOGIC_VECTOR(1 downto 0);
        AInvert, BInvert, Less  : in     STD_LOGIC;
        Result, CarryOut        : out    STD_LOGIC);
end component;


component ALU_MSB
	port(a, b, CarryIn                    : in 	STD_LOGIC; 
        Operation                         : in     STD_LOGIC_VECTOR(1 downto 0);
        AInvert, BInvert, Less            : in    STD_LOGIC;
        Result, CarryOut, Set, Overflow   : out   STD_LOGIC);
end component;



signal c0,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31 : STD_LOGIC;
signal r0,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31 : STD_LOGIC;
signal ainvert, binvert,set_s : STD_LOGIC;
signal operation : STD_LOGIC_VECTOR(1 downto 0);

begin
process (a,b,ALUOp,Funct, ainvert, binvert, operation, set_s)

begin
	if ((ALUOp(1) = '1') and (Funct(3 downto 0) = "0100")) then  -- AND
        c0 <= '0';
        operation <= "00";
        ainvert <= '0';
        binvert <= '0';
	elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "0101")) then  -- OR
		c0 <= '0';
        operation <= "01";
        ainvert <= '0';
        binvert <= '0';
    elsif (ALUOp = "00") then  -- ADD
        c0 <= '0';        
        operation <= "10";
        ainvert <= '0';
        binvert <= '0';
    elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1010")) then  -- SLT 
        c0 <= '1';
        operation <= "11";
        ainvert <= '0';
        binvert <= '1';
    elsif (ALUOp(0) = '1') then  -- SUB
        c0 <= '1';
        operation <= "10";
        ainvert <= '0';
        binvert <= '1';

--   elsif (conflgs = "1100") then  -- NOR
--        c0 <= '0';
--        operation <= "00";
--        ainvert <= '1';
--        binvert <= '1';
    else                    -- none of the inputs match, error case
        c0 <= '0';
        operation <= "11";
        ainvert <= '1';
        binvert <= '0';
	end if;

    end process;


--c0 <= '0';
alu_elem0: ALU_NORMAL port map (a(0), b(0), c0, operation, ainvert, binvert, set_s, r0, c1);
alu_elem1: ALU_NORMAL port map (a(1), b(1), c1, operation, ainvert, binvert, '0', r1, c2);
alu_elem2: ALU_NORMAL port map (a(2), b(2), c2, operation, ainvert, binvert, '0', r2, c3);
alu_elem3: ALU_NORMAL port map (a(3), b(3), c3, operation, ainvert, binvert, '0', r3, c4);
alu_elem4: ALU_NORMAL port map (a(4), b(4), c4, operation, ainvert, binvert, '0', r4, c5);
alu_elem5: ALU_NORMAL port map (a(5), b(5), c5, operation, ainvert, binvert, '0', r5, c6);
alu_elem6: ALU_NORMAL port map (a(6), b(6), c6, operation, ainvert, binvert, '0', r6, c7);
alu_elem7: ALU_NORMAL port map (a(7), b(7), c7, operation, ainvert, binvert, '0', r7, c8);
alu_elem8: ALU_NORMAL port map (a(8), b(8), c8, operation, ainvert, binvert, '0', r8, c9);
alu_elem9: ALU_NORMAL port map (a(9), b(9), c9, operation, ainvert, binvert, '0', r9, c10);
alu_elem10: ALU_NORMAL port map (a(10), b(10), c10, operation, ainvert, binvert, '0', r10, c11);
alu_elem11: ALU_NORMAL port map (a(11), b(11), c11, operation, ainvert, binvert, '0', r11, c12);
alu_elem12: ALU_NORMAL port map (a(12), b(12), c12, operation, ainvert, binvert, '0', r12, c13);
alu_elem13: ALU_NORMAL port map (a(13), b(13), c13, operation, ainvert, binvert, '0', r13, c14);
alu_elem14: ALU_NORMAL port map (a(14), b(14), c14, operation, ainvert, binvert, '0', r14, c15);
alu_elem15: ALU_NORMAL port map (a(15), b(15), c15, operation, ainvert, binvert, '0', r15, c16);
alu_elem16: ALU_NORMAL port map (a(16), b(16), c16, operation, ainvert, binvert, '0', r16, c17);
alu_elem17: ALU_NORMAL port map (a(17), b(17), c17, operation, ainvert, binvert, '0', r17, c18);
alu_elem18: ALU_NORMAL port map (a(18), b(18), c18, operation, ainvert, binvert, '0', r18, c19);
alu_elem19: ALU_NORMAL port map (a(19), b(19), c19, operation, ainvert, binvert, '0', r19, c20);
alu_elem20: ALU_NORMAL port map (a(20), b(20), c20, operation, ainvert, binvert, '0', r20, c21);
alu_elem21: ALU_NORMAL port map (a(21), b(21), c21, operation, ainvert, binvert, '0', r21, c22);
alu_elem22: ALU_NORMAL port map (a(22), b(22), c22, operation, ainvert, binvert, '0', r22, c23);
alu_elem23: ALU_NORMAL port map (a(23), b(23), c23, operation, ainvert, binvert, '0', r23, c24);
alu_elem24: ALU_NORMAL port map (a(24), b(24), c24, operation, ainvert, binvert, '0', r24, c25);
alu_elem25: ALU_NORMAL port map (a(25), b(25), c25, operation, ainvert, binvert, '0', r25, c26);
alu_elem26: ALU_NORMAL port map (a(26), b(26), c26, operation, ainvert, binvert, '0', r26, c27);
alu_elem27: ALU_NORMAL port map (a(27), b(27), c27, operation, ainvert, binvert, '0', r27, c28);
alu_elem28: ALU_NORMAL port map (a(28), b(28), c28, operation, ainvert, binvert, '0', r28, c29);
alu_elem29: ALU_NORMAL port map (a(29), b(29), c29, operation, ainvert, binvert, '0', r29, c30);
alu_elem30: ALU_NORMAL port map (a(30), b(30), c30, operation, ainvert, binvert, '0', r30, c31);
alu_elem31_msb: ALU_MSB port map (a(31), b(31), c31, operation, ainvert, binvert, '0', r31, CarryOut, set_s, Overflow);


--    process (a,b,ALUOp,Funct, ainvert, binvert, operation, set_s)
--    begin
--        if ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1010")) then
--            r0 <= set_s;
--        else
--            r0 <= r0;
--        end if;
--    end process;

    Result(0) <= r0;
    Result(1) <= r1;
    Result(2) <= r2;
    Result(3) <= r3;
    Result(4) <= r4;
    Result(5) <= r5;
    Result(6) <= r6;
    Result(7) <= r7;
    Result(8) <= r8;
    Result(9) <= r9;
    Result(10) <= r10;
    Result(11) <= r11;
    Result(12) <= r12;
    Result(13) <= r13;
    Result(14) <= r14;
    Result(15) <= r15;
    Result(16) <= r16;
    Result(17) <= r17;
    Result(18) <= r18;
    Result(19) <= r19;
    Result(20) <= r20;
    Result(21) <= r21;
    Result(22) <= r22;
    Result(23) <= r23;
    Result(24) <= r24;
    Result(25) <= r25;
    Result(26) <= r26;
    Result(27) <= r27;
    Result(28) <= r28;
    Result(29) <= r29;
    Result(30) <= r30;
    Result(31) <= r31;



    Zero <= not (r0 or r1 or r2 or r3 or r4 or r5 or r6 or r7 or r8 or r9 or r10 or r11 or r12 or r13 or r14 or r15 or r16 or r17 or r18 or r19 or r20 or r21 or r22 or r23 or r24 or r25 or r26 or r27 or r28 or r29 or r30 or r31);
    
    -- when using port map, it is putting the contents of each individual component that is being created into the arguments passed into it
    -- so in this case, for each of the 4 1 bit ALU components, whatever its personal a, b, carryin, conflgs, result, and carryout are, they
    -- get set to the a(), b(), c#, conflgs, res(#), and c# of this 4 bit alu entity 
    
    -- Cris said to use signals as each individual wire in the diagram, so create a signal for the result of each 1 bit alu, put that into 
    -- the arguments in the port map above so that the signal is set to the actual value of the result of the individual component, and then
    -- use that signal to do further stuff, like setting the zero bit, don't use res(#) itself to set the zero bit, use the signals 
    
    
--    zero <= res(0) or res(1); --make a local signal to assign res to, and use the signal for the zero or 

end my_structure;
