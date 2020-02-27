/*
Author: David Helminiak

Date Created: 28 January 2017

Date Modified: 30 January 2017

Description: This program takes a binary file encoded using a stream cipher with a repeated key, identifies the key and decodes the message to readable text. The key is discovered through an evaluation of the frequency of character usage ' ' being the most common and 'e' being the second mot. 
*/

//includes go here
#include <stdio.h>


//defines go here
#define MLENGTH 3000 //define the maximum length of the message


//main function
int main() {

//variable declarations
unsigned char message[MLENGTH]; //create a buffer to hold the encoded message
FILE *file_ptr;


file_ptr = fopen("out.txt", "rb"); //open the encoded file as a readable binary
 




return 0;
}
