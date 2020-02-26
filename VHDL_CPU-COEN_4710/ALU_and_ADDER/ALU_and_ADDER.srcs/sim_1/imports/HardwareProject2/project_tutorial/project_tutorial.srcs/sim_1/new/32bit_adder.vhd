-- 4-bit adder
-- Structural description of a 4-bit adder. This device
-- adds two 4-bit numbers together using four 1-bit full adders
-- described above.

-- This is just to make a reference to some common things needed.
LIBRARY IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- This describes the black-box view of the component we are
-- designing. The inputs and outputs are again described
-- inside port(). It takes two 4-bit values as input (x and y)
-- and produces a 4-bit output (ANS) and a carry out bit (Cout).

entity thirtytwobit_adder is
	port( a, b		: in	STD_LOGIC_VECTOR(31 downto 0);
	      z			: out	STD_LOGIC_VECTOR(31 downto 0);
	      cout		: out	STD_LOGIC		);
end thirtytwobit_adder;

-- Although we have already described the inputs and outputs,
-- we must now describe the functionality of the adder (ie:
-- how we produced the desired outputs from the given inputs).

architecture MY_STRUCTURE of thirtytwobit_adder is

-- We are going to need four 1-bit adders, so include the
-- design that we have already studied in full_adder.vhd.

component FULL_ADDER
	port( x, y, cin	: in  STD_LOGIC;
	      sum, cout	: out STD_LOGIC );
end component;

-- Now create the signals which are going to be necessary
-- to pass the outputs of one adder to the inputs of the next
-- in the sequence.
signal c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31 : STD_LOGIC;
begin

c0 <= '0';
b_adder0: FULL_ADDER port map (a(0), b(0), c0, z(0), c1);
b_adder1: FULL_ADDER port map (a(1), b(1), c1, z(1), c2);
b_adder2: FULL_ADDER port map (a(2), b(2), c2, z(2), c3);
b_adder3: FULL_ADDER port map (a(3), b(3), c3, z(3), c4);
b_adder4: FULL_ADDER port map (a(4), b(4), c4, z(4), c5);
b_adder5: FULL_ADDER port map (a(5), b(5), c5, z(5), c6);
b_adder6: FULL_ADDER port map (a(6), b(6), c6, z(6), c7);
b_adder7: FULL_ADDER port map (a(7), b(7), c7, z(7), c8);
b_adder8: FULL_ADDER port map (a(8), b(8), c8, z(8), c9);
b_adder9: FULL_ADDER port map (a(9), b(9), c9, z(9), c10);
b_adder10: FULL_ADDER port map (a(10), b(10), c10, z(10), c11);
b_adder11: FULL_ADDER port map (a(11), b(11), c11, z(11), c12);
b_adder12: FULL_ADDER port map (a(12), b(12), c12, z(12), c13);
b_adder13: FULL_ADDER port map (a(13), b(13), c13, z(13), c14);
b_adder14: FULL_ADDER port map (a(14), b(14), c14, z(14), c15);
b_adder15: FULL_ADDER port map (a(15), b(15), c15, z(15), c16);
b_adder16: FULL_ADDER port map (a(16), b(16), c16, z(16), c17);
b_adder17: FULL_ADDER port map (a(17), b(17), c17, z(17), c18);
b_adder18: FULL_ADDER port map (a(18), b(18), c18, z(18), c19);
b_adder19: FULL_ADDER port map (a(19), b(19), c19, z(19), c20);
b_adder20: FULL_ADDER port map (a(20), b(20), c20, z(20), c21);
b_adder21: FULL_ADDER port map (a(21), b(21), c21, z(21), c22);
b_adder22: FULL_ADDER port map (a(22), b(22), c22, z(22), c23);
b_adder23: FULL_ADDER port map (a(23), b(23), c23, z(23), c24);
b_adder24: FULL_ADDER port map (a(24), b(24), c24, z(24), c25);
b_adder25: FULL_ADDER port map (a(25), b(25), c25, z(25), c26);
b_adder26: FULL_ADDER port map (a(26), b(26), c26, z(26), c27);
b_adder27: FULL_ADDER port map (a(27), b(27), c27, z(27), c28);
b_adder28: FULL_ADDER port map (a(28), b(28), c28, z(28), c29);
b_adder29: FULL_ADDER port map (a(29), b(29), c29, z(29), c30);
b_adder30: FULL_ADDER port map (a(30), b(30), c30, z(30), c31);
b_adder31: FULL_ADDER port map (a(31), b(31), c31, z(31), cout);

END MY_STRUCTURE;
