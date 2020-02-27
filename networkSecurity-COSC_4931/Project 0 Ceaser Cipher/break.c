/*
Author: David Helminiak

Date Created: 28 January 2017

Date Modified: 30 January 2017

Description: This program accepts an input text file in the same folder that is encrypted with a simple ceaser cipher and outputs the most likely key. This is calculated on the basis on the frequency of how many "e"'s are found when the file is decrypted with each possible alphabet letter. 
*/

#include <stdio.h>
#define MLENGTH 3000 //define the message character length
int main() {

char message[MLENGTH] = ""; //array to hold the encoded message
char testHold[MLENGTH] = ""; //array to hold the temporarily translated message
char decrypted[MLENGTH] = ""; //array to hold the decrypted message
int eCount[26]; //array to hold the number of "e"'s found in each translated message
char charlist[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //list of the possible characters repeated twice in a row
char key; //what is the current key's char value
int lposition = 0; //cursor position for charlist decryption
int bestKeyIndex = -1; //what is the optimum key found
int maxE = -1;
int dFlag = 0; //flag to indicate if a character was unable to be decrypted or not
char dKey = 0; //which decoded character is to be used from the array
int i = 0, j = 0, k = 0; //basic index counters and variables for loops and decryption
FILE *ptr_file; //create a file pointer

//initialize eCount array to 0
for (i = 0; i < 26; i++) {
	eCount[i] = 0;
}

//read in the file into the buffer
ptr_file = fopen("encoded.txt","r"); //open the encoded file as read
if (!ptr_file) { //if the file does not exist
	printf("The file specified was not found\n"); //inform the user
	return 1; //return an error, exit the program
}
while (fgets(message,MLENGTH,ptr_file) != NULL) { //while each of the characters does existm put it into the message array
}
fclose (ptr_file); //close the file

for (i = 0; i < 26; i++) { //for each of the possible key characters
	key = charlist[i]; //set the key to the current char involved

	//decrypt the message
	for (j = 0; j < MLENGTH; j++) { //for each of the letters in the message
		dFlag = 0; //reset flag to indicate character has not yet been decrypted
		for (k = 0; k < 26; k++) { //for each of the possible characters
			//find the character's index if it exists
			if (message[j] == charlist[k]) { //if the current character is the same as the character in the list
				dFlag = 1; //indicate that the character was decrypted
				//k is the position of the found character within the character array
				lposition = k-i; //move the char list cursor and subtract the key value
				if (lposition < 0) { //if past the power bound of the charlist
					lposition += 26; //loop back around
				}
				testHold[j] = charlist[lposition]; //add that index to the current key value to find the resulting character
			}
		}
			if (!dFlag) { //if the character was unable to be decrypted
				testHold[j] = message[j]; //copy it straight from the original message
			}
	}

	//check the decrypted message for "E"'s
	for (j = 0; j < MLENGTH; j++) { //for each of the letters in the message
		if (testHold[j] == 'E') { //if an E is found
			eCount[i]++; //increase the number of "E"'s that were found for the character
		}
	
	}	
	printf("For key: %c, the eCount was %d\n",charlist[i],eCount[i]); //print out eCount in case alternatives might be more optimum

        if (eCount[i] >= maxE) { //if the current eCount vlaue is greater than the presently best key
                maxE = eCount[i]; //update the maxE count
                bestKeyIndex = i; //set the bestKeyIndex to the more optimum eCount index
                dKey = charlist[i]; //copy the optimum char to dKey for printout
		//copy the testHold array into the decrypted array
        	for (k = 0; k < MLENGTH; k++) { //for the length of the message
			decrypted[k] = testHold[k]; //copy each character from testHold to decrypted
		}
	}

}

printf("\n\nThe optimum key is: %c\n\n", dKey); //m to user
printf("The decrypted message therefore is:\n\n"); //m to user
for (i = 0; i < MLENGTH; i++) { //for each of the characters in the message
	printf("%c",decrypted[i]); //print out the decrypted character
}
printf("\n\n"); //print some space
return 0; //exit the program
}

