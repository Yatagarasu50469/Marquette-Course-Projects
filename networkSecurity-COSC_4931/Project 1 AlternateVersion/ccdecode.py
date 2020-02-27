#Program:  COSC Project 1
#Version: 1.0
#Author: David Helminiak
#Date Created: 23 January 2018
#Date Last Modified 23 January 2018
#Purpose: This program accepts an input text file in the same folder that is encrypted with a simple ceaser cipher and outputs the most likely key. This is calculated on the basis on the frequency of how many "e"'s are found when the file is decrypted with each possible alphabet letter. The program also takes the second argument as a file name to hold the decrypted message.
#Example python ccdecode.py challenge3.txt decrypted3.txt
#References:
#

import sys

f = open(sys.argv[1],"r") #Read in the file given as a command line argument
contents = f.read() #Read the file contents into a variable
charlist = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'] #Create list of possible letter changes
eCount = [0] * 26 #Initialize an array to hold a cound of how many e's are found for each possible shift
length = len(contents) #Find the length of the message
testHold = [''] * length #Array to hold a decrypted message for an E cound
decrypted = [''] * length #Array to hold the decrypted message
maxE = -1 #Maximum number of E's found

for i in range(0, 26): #For each of the possible keys
	key = charlist[i] #Set the key to the current character of interest
	for j in range(0, length): #For each of the characters in the message
		dFlag=0 #Reset flag to indicate character has not yet been decrypted
		for k in range(0, 26): #For each of the possible characters
			#Find the character's index if it exists
			if(contents[j] == charlist[k]): #If the index is found for the current character
				dFlag = 1 #Indicate that the character was decrypted
				#k is the position of the found character within the character array
				lposition = k-i #Move the char list cursor and subtract the key value
				if(lposition<0): #If past the power bound of the charlist
					lposition+= 26; #Loop back around
				testHold[j] = charlist[lposition] #Add that index to the current key value to find the resulting character
			if(dFlag == 0): #If the character was unable to be decrypted
				testHold[j] = contents[j] #Copy it straight from the original message 
	#Check the decrypted message for "E"'s
	for j in range(0, length): #For each of the letters in the message
		if(testHold[j] == 'E'): #If an E is found
			eCount[i]+=1 #Increase the number of "E"'s that were found for the character



	print("For key:",charlist[i],"the eCount was",eCount[i],"\n") #Print out eCount in case alternative
	if(eCount[i] >= maxE): #If the current eCount value is greater than the presently best key
		maxE = eCount[i] #Update the maxE Count
		bestKeyIndex = i #Set the bestKeyIndex to the more optimum eCount index
		dKey = charlist[i] #Copy the optimum char to dKey for printout
		#Copy the testHold array into the decrypted array
		for k in range(0, length):
			decrypted[k] = testHold[k]
print("The optimum key is:",dKey,"\n\n") #Inform the user of the optimum key
outf = open(sys.argv[2],'w')
for string in decrypted:
	outf.write(string)
outf.close()

