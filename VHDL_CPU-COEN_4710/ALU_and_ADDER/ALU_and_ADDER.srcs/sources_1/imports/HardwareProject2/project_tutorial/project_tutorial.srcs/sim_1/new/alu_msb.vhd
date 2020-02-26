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

entity alu_msb is
	port(a, b, CarryIn                    : in 	STD_LOGIC; 
        Operation                         : in 	STD_LOGIC_VECTOR(1 downto 0);
        AInvert, BInvert, Less            : in    STD_LOGIC;
        Result, CarryOut, Set, Overflow   : out   STD_LOGIC);
end alu_msb;



architecture arch_functional of alu_msb is

signal s_and, s_or, s_add, s_sub, muxA, muxB, carryOut_s : STD_LOGIC;

begin

--process (AInvert, BInvert, Operation)
process(a,b,CarryIn,Operation,AInvert,BInvert,Less,s_add,s_sub,s_and,s_or,muxA,muxB, carryOut_s)

begin
    -- use signals for each wire of the diagram, so the output for each mux or logic gate should be a new signal
    -- some signals get set no matter what, and some use if statements, like the mux outputs 
    -- set the signals based on the inputs, and set the result with the signals 

    if(AInvert = '0') then
        muxA <= a;
    elsif(AInvert = '1') then
        muxA <= not a;
    end if;
    
    if(BInvert = '0') then 
        muxB <= b;
    elsif(BInvert = '1') then
        muxB <= not b;
    end if;
    
    s_and <= muxA and muxB;
    s_or <= muxA or muxB;
    s_add <= ((((not a) and (not b)) and CarryIn) or (a and ((not b) and (not CarryIn))) or (((not a) and b) and (not CarryIn)) or ((a and b) and CarryIn));
    s_sub <= ((((not a) and b) and CarryIn) or (a and (b and (not CarryIn))) or (((not a) and (not b)) and (not CarryIn)) or ((a and (not b)) and CarryIn));
--    s_sub <= a or b or CarryIn;
    Set <= '0';
--    if (AInvert = '0' and BInvert ='0' and Operation = "00") then -- AND
--        Result <= s1_and;
--        CarryOut <= ;

    if(AInvert = '0' and BInvert = '0' and Operation = "00") then -- AND
        Result <= s_and;
        CarryOut <= '0';
        Overflow <= '0';
    elsif (AInvert = '0' and BInvert = '0' and Operation = "01") then -- OR
        Result <= s_or;
        CarryOut <= '0';
        Overflow <= '0';
    elsif (AInvert = '0' and BInvert = '0' and Operation = "10") then -- ADD
        Result <= s_add;
        CarryOut <= ((a and b) or (a and CarryIn) or (b and CarryIn));
        
        if( (a = b) and (not (a = s_add))   ) then 
            Overflow <= '1';
        else 
            Overflow <= '0';
        end if;
        
    elsif (AInvert = '0' and BInvert = '1' and Operation = "10") then -- SUB
        Result <= s_sub;
        CarryOut <= ((a and (not b)) or (a and CarryIn) or ((not b) and CarryIn));
        Set <= s_sub;
        
        if( (a = (not b)) and (not (a = s_sub))   ) then 
                Overflow <= '1';
            else 
                Overflow <= '0';
            end if;
        
    elsif (AInvert = '1' and BInvert = '1' and Operation = "00") then -- NOR
        Result <= (not a) and (not b);
        CarryOut <= '0';
        Overflow <= '0';
    elsif (AInvert = '0' and BInvert = '1' and Operation = "11") then -- SLT
        Result <= Less;
        CarryOut <= '0';
        Set <= s_sub;
        Overflow <= '0';
    else   
        Result <= '0';
        CarryOut <= '0';
        Overflow <= '0';
    end if;

--    if(AInvert = '0' and BInvert = '0' and Operation = "00") then -- AND
--        Result <= a and b;
--        CarryOut <= '0';
--    elsif (AInvert = '0' and BInvert = '0' and Operation = "01") then -- OR
--        Result <= a or b;
--        CarryOut <= '0';
--    elsif (AInvert = '0' and BInvert = '0' and Operation = "10") then -- ADD
--        Result <= ((((not a) and (not b)) and CarryIn) or (a and ((not b) and (not CarryIn))) or (((not a) and b) and (not CarryIn)) or ((a and b) and CarryIn));
--        CarryOut <= ((a and b) or (a and CarryIn) or (b and CarryIn));
--    elsif (AInvert = '0' and BInvert = '1' and Operation = "10") then -- SUB
--        Result <= ((((not a) and b) and CarryIn) or (a and (b and (not CarryIn))) or (((not a) and (not b)) and (not CarryIn)) or ((a and (not b)) and CarryIn));
--        CarryOut<= ((a and (not b)) or (a and CarryIn) or ((not b) and CarryIn));
--    elsif (AInvert = '1' and BInvert = '1' and Operation = "00") then -- NOR
--            Result <= (not a) and (not b);
--            CarryOut <= '0';
      
--        not_b := to_bit(not b);
--        res <= ((((not a) and (not not_b)) and cin) or (a and ((not not_b) and (not cin))) or (((not a) and not_b) and (not cin)) or ((a and not_b) and cin));
--        cout <= ((a and not_b) or (a and cin) or (not_b and cin));

--        cout <= to_std_logic(not_b);

--    elsif (conflgs = '0110') then -- SUB
--    elsif (conflgs = '0111') then -- SLT
--    elsif (conflgs = '1100') then -- NOR
    
    
    
    
    
    
    
end process;

end arch_functional;
