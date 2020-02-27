C Program: COSC_3410_Assignment_4_Problem_1
C Version: 1.0
C Author: David Helminiak
C Date Created: 1 October 2018
C Date Last Modified: 1 October 2018
C Description: Randomly places molecules in a 20x20 grid,
C              then counts how many do not have neighbors.
C

C CREATE PROGRAM
      PROGRAM A4P1
      INTEGER TIMER
      INTEGER VALUES(8)
C RUN FUNCTION 5 TIMES
      do N=1,5
C INTRODUCE ENTROPY F0R RANDOM NUMBER GENERATOR
      CALL DATE_AND_TIME(VALUES=VALUES)
      TIMER = RAND(values(8))
      TIMER = 2*RAND(0)+1
      CALL SLEEP(TIMER)
C CALL NO NEIGHBOR COUNT ROUTINE
      CALL NNCOUNT()
      END DO
      END

C CREATE SUBROUTINE NO NEIGHBOR COUNT
      SUBROUTINE NNCOUNT()
C CREATE A 20X20 INTEGER GRID AND A COUNT FOR MOLS W/ NEIGHBORS
      INTEGER SIZE,COUNT
      PARAMETER (SIZE=20)
      REAL G(SIZE,SIZE)
C MAKE SURE GRID IS EMPTY
      G(:,:) = 0
C CALL SUBROUTINE FOR PLACING MOLECULES 100 times
      DO I=1,100
      CALL MOLPUT(SIZE,G)
      END DO
C DEBUG PRINT THE RESULTING MATRIX
C      CALL LATPRINT(SIZE,G)
C CYCLE THROUGH THE LATTICE, MAKE SURE COUNT IS 0
      COUNT=0
      DO I=1,SIZE
      DO J=1,SIZE
C IF A MOLECULE IS FOUND
      IF (G(I,J).EQ.1) THEN
C CHECK ITS NEIGHBORS

C If the molecule is on the far right side
      IF ((I+1).GT.SIZE) THEN
      IF ((G(I,J+1).NE.1).AND.(G(I,J-1).NE.1).AND.(G(I-1,J).NE.1)) THEN
      COUNT=COUNT+1
      END IF
C If the molecule is on the far left side
      ELSE IF ((I-1).LT.1) THEN
      IF ((G(I,J+1).NE.1).AND.(G(I,J-1).NE.1).AND.(G(I+1,J).NE.1)) THEN
      COUNT=COUNT+1
      END IF
C If the molecule is on the top
      ELSE IF ((J-1).LT.1) THEN
      IF ((G(I,J+1).NE.1).AND.(G(I+1,J).NE.1).AND.(G(I-1,J).NE.1)) THEN
      COUNT=COUNT+1
      END IF
C If the molecule is on the bottom
      ELSE IF ((J+1).GT.SIZE) THEN
      IF ((G(I,J+1).NE.1).AND.(G(I+1,J).NE.1).AND.(G(I-1,J).NE.1)) THEN
      COUNT=COUNT+1
      END IF
C If the molecule is anywhere else
      ELSE IF ((G(I,J+1).NE.1).AND.(G(I,J-1).NE.1)) THEN
      IF ((G(I+1,J).NE.1).AND.(G(I-1,J).NE.1)) THEN
      COUNT=COUNT+1
      END IF
      END IF
      END IF
      END DO
      END DO
      PRINT *, COUNT, 'OF 100 MOLECULES ARE ISOLATED'
C END THE PROGRAM
      END

C CREATE SUBROUTINE FOR PLACING MOLECULES IN GRID
      SUBROUTINE MOLPUT(K,H)
      REAL H(K,K)
C SEED RANDOM NUMBER GENERATORS
      INTEGER :: VALUES(8)
      CALL DATE_AND_TIME(VALUES=VALUES)
      X = RAND(values(8))
      Y = RAND(values(8))
C GENERATE RANDOM NUMBERS OF X AND Y
5     X = K*RAND(0)+1.0
      Y = K*RAND(0)+1.0
C IF OCCUPIED, THEN REPEAT NUMBER GENERATION
      IF (H(X,Y).EQ.1) THEN
      GO TO 5
C ELSE, ASSIGN MOLECULE TO COORDINATE
      ELSE
      H(X,Y) = 1
      END IF
      END
C CREATE SUBROUTINE TO PRINT LATTICE
      SUBROUTINE LATPRINT(K,H)
      REAL H(K,K)
      DO I=1,K
      DO J=1,K
      IF (H(I,J) .EQ. 1) THEN
      WRITE(*,"(A)",advance="no") "1 "
      ELSE
      WRITE(*,"(A)",advance="no") "* "
      END IF 
      END DO
      PRINT *, ' '
      END DO
      END
