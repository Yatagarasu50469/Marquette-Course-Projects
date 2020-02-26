----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 04/20/2016 05:44:42 PM
-- Design Name: 
-- Module Name: logic_unit - Behavioral
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

entity logic_unit is
	port( a, b     	                : in     STD_LOGIC_VECTOR(31 downto 0);
        ALUOp                       : in     STD_LOGIC_VECTOR(1 downto 0);
        Funct                       : in     STD_LOGIC_VECTOR(5 downto 0); 
        Shamt                       : in     STD_LOGIC_VECTOR(4 downto 0);
        Result                      : out    STD_LOGIC_VECTOR(31 downto 0));
end logic_unit;

architecture my_structure of logic_unit is

signal v,w,x,y,z    : STD_LOGIC_VECTOR(31 downto 0);
--signal v0,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v21,v22,v23,v24,v25,v26,v27,v28,v29,v30,v31    : STD_LOGIC;
--signal w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23,w24,w25,w26,w27,w28,w29,w30,w31    : STD_LOGIC;
--signal x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30,x31    : STD_LOGIC;
--signal y0,y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12,y13,y14,y15,y16,y17,y18,y19,y20,y21,y22,y23,y24,y25,y26,y27,y28,y29,y30,y31    : STD_LOGIC;
--signal z0,z1,z2,z3,z4,z5,z6,z7,z8,z9,z10,z11,z12,z13,z14,z15,z16,z17,z18,z19,z20,z21,z22,z23,z24,z25,z26,z27,z28,z29,z30,z31    : STD_LOGIC;

begin

--    Result(0) <= a(0);
process (a,b,ALUOp,Funct, Shamt,v,w,x,y,z)

begin
	if ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1111")) then  -- SRL

	   if (Shamt(0) = '1') then        -- if shifting 1 bit
            for I in 0 to 30 loop
                v(I) <= a(I+1);
            end loop;
            v(31) <= '0';
        else
            v <= a;
        end if;
        
        if (Shamt(1) = '1') then        -- if shifting 2 bits
            for I in 0 to 29 loop
                w(I) <= v(I+2);
            end loop;
            w(31) <= '0';
            w(30) <= '0';
        else
            w <= v;
        end if;

        if (Shamt(2) = '1') then        -- if shifting 4 bits
            for I in 0 to 27 loop
                x(I) <= w(I+4);
            end loop;
            x(31) <= '0';
            x(30) <= '0';
            x(29) <= '0';
            x(28) <= '0';
        else
            x <= w;
        end if;

        if (Shamt(3) = '1') then        -- if shifting 8 bits
            for I in 0 to 23 loop
                y(I) <= x(I+8);
            end loop;
            y(31) <= '0';
            y(30) <= '0';
            y(29) <= '0';
            y(28) <= '0';
            y(27) <= '0';
            y(26) <= '0';
            y(25) <= '0';
            y(24) <= '0';
        else
            y <= x;
        end if;

        if (Shamt(4) = '1') then        -- if shifting 16 bits
            for I in 0 to 15 loop
                z(I) <= y(I+16);
            end loop;
            z(31) <= '0';
            z(30) <= '0';
            z(29) <= '0';
            z(28) <= '0';
            z(27) <= '0';
            z(26) <= '0';
            z(25) <= '0';
            z(24) <= '0';
            z(23) <= '0';
            z(22) <= '0';
            z(21) <= '0';
            z(20) <= '0';
            z(19) <= '0';
            z(18) <= '0';
            z(17) <= '0';
            z(16) <= '0';         
        else
            z <= y;
        end if;
            
            
        Result <= z;
            
--            for J in 0 to 31 loop
--                Result(J) <= v(J);
--            end loop;
            
--            Result(0) <= '0';
	
	
    elsif ((ALUOp(1) = '1') and (Funct(3 downto 0) = "1110")) then  -- SLL


	   if (Shamt(0) = '1') then        -- if shifting 1 bit
            for I in 0 to 30 loop
                v(I+1) <= a(I);
            end loop;
            v(0) <= '0';
        else
            v <= a;
        end if;
        
        if (Shamt(1) = '1') then        -- if shifting 2 bits
            for I in 0 to 29 loop
                w(I+2) <= v(I);
            end loop;
            w(0) <= '0';
            w(1) <= '0';
        else
            w <= v;
        end if;

        if (Shamt(2) = '1') then        -- if shifting 4 bits
            for I in 0 to 27 loop
                x(I+4) <= w(I);
            end loop;
            x(0) <= '0';
            x(1) <= '0';
            x(2) <= '0';
            x(3) <= '0';
        else
            x <= w;
        end if;

        if (Shamt(3) = '1') then        -- if shifting 8 bits
            for I in 0 to 23 loop
                y(I+8) <= x(I);
            end loop;
            y(0) <= '0';
            y(1) <= '0';
            y(2) <= '0';
            y(3) <= '0';
            y(4) <= '0';
            y(5) <= '0';
            y(6) <= '0';
            y(7) <= '0';
        else
            y <= x;
        end if;

        if (Shamt(4) = '1') then        -- if shifting 16 bits
            for I in 0 to 15 loop
                z(I+16) <= y(I);
            end loop;
            z(0) <= '0';
            z(1) <= '0';
            z(2) <= '0';
            z(3) <= '0';
            z(4) <= '0';
            z(5) <= '0';
            z(6) <= '0';
            z(7) <= '0';
            z(8) <= '0';
            z(9) <= '0';
            z(10) <= '0';
            z(11) <= '0';
            z(12) <= '0';
            z(13) <= '0';
            z(14) <= '0';
            z(15) <= '0';         
        else
            z <= y;
        end if;
            
            
        Result <= z;





    end if;
    
    end process;



end my_structure;
