#Program:  COSC Project 1
#Version: 1.0
#Author: David Helminiak
#Date Created: 28 January 2018
#Date Last Modified: 28 January 2018
#Purpose:This program accepts an input text file in the same folder that is encrypted with a vigenere cipher and outputs the most likely password based on regularities within the English language. 
#Example: python ccdecode.py challenge1.txt decrypted1.txt
#References:
#

import sys

f = open(sys.argv[1],"r") #Read in the file given as the first command line argument
contents = f.read() #Read the file contents into a variable
length = len(contents) #Find the length of the cipher text block
maxFreq = 0 #What is the greatest number of occurances
keyLengthFreq = [0]*length #Array to hold the number of coincidences for different key lengths, indexed
sortedkeyLengthFreq = [0]*5 #Array to hold the number of coincidences for different key lengths, sorted
distance = 0 #Distance counter between characters
start = 0 #Boolean for whether the count for distance measurement has started yet
end = 0 #Boolean for whether the current distance measurement has been concluded
keyLength = 0 #Most probable key length

#Determine the keylength based on coincidence of encoded characters
for i in range(0,255): #For each of the possible characters
	start = 0 #Reset boolean for starting the character
	end = 0 #Reset boolean for stopping the character
	distance = 0 #Reset distance counter
	for j in range(0,length): #For the length of the message
		if(start == 1): #If the counter is currently on
			if (ord(contents[j]) == i): #If the next occurance has been detected
				end = 1 #Stop the count
			else: #The next occurance has not yet been detected
				distance+=1 #Increase the distance count by 1
		else: #The counter is not yet on
			if (ord(contents[j]) == i): #If the first occurance has been detected
				start = 1 #Then start counting
		if(end == 1): #If the next occurance had been detected
			keyLengthFreq[distance]+=1 #Increase the frequency that a coincidence occured
			distance = 0 #Reset the distance counter
			end = 0 #Start the next count

#Pull the top 5 keylength likelihoods
for i in range(0,5):
	maxFreq = 0 #Reset the max frequency counter
	for j in range(3,(int)(length*0.01)): #For each of the reasonable key lengths
		if (keyLengthFreq[j] > maxFreq): #If there is a higher frequency avaliable
			maxFreq = keyLengthFreq[j] #Update the max frequency counter
			sortedkeyLengthFreq[i] = j #Update the sorted optimum key length list
	keyLengthFreq[sortedkeyLengthFreq[i]] = 0 #Remove the determined most frequent from contention in the next sort

#For the keylengths found find the frequencies of each character for each of the keys
#The most common character found for each will be a space character
#Find the most frequent character in the ciphertext for each of the keys within the key length
charFreq = {} #Frequency of the characters for each key of the keylength
keylength=13
for i in range(0,keylength):
	for j in range(0,255):
		charFreq[i,j]=0


for i in range(0,keylength):
	charCounter = i
	while(charCounter<length): 
		for j in range(0,255):
			if(ord(contents[charCounter]) == ord(' ')):
				charFreq[i,j]+=1
		charCounter+=keylength



key = [0]*keylength
#charCounter = 0
#i = 0
#while (i < length-keylength):
#	if (charCounter == keylength):
#		charCounter = 0
#		i+=keylength
#	for j in range(0,255):
#		if(ord(contents[i+charCounter]) == ord(' ')):
#			charFreq[charCounter,j]+=1
#	charCounter+=1

#topFreq=0
for i in range(0,keylength):
	maxFreq=0
	for j in range(0,255):
		if (charFreq[i,j] > maxFreq):
			maxFreq = charFreq[i,j]
			key[i] = j
	print("Key",i,"has maxFreq char",key[i])
#
#for i in range(0,13):
#	print(chr(key[i]))
#print("Top key was:",topkey)



#                       if (charFreq[j,k] > maxFreq): #If the found frequency for a particular character was higher than the last found
#                               maxFreq = charFreq[j,k] #Update the last found maximum frequency
#                               key[j] = (k ^ ord(' ')) #Update the particular key information
#                       #print(charFreq[j][k])
#       for j in range(0,sortedkeyLengthFreq[i]):
#               print(chr(key[j]))





#while ((charCounter+13) < length):
#	for j in range(0,13):
#		for character in range(0,255):
#			if(ord(contents[charCounter+j]) == character):
#				charFreq[j][character]+=1
#	charCounter+=13
#for j in range(0,13):
#	print ("Key:",j)
#	maxFreq = 0
#	for k in range(0,255):
#		if (charFreq[j][character] > maxFreq):
#			maxFreq = charFreq[j][k]
#			key[j] = chr((character ^ ord(' ')))
#		print(charFreq[j][k])
#print(key)

#for i in range(0,1): #For each of the possible key lengths found
#	key = [0] * sortedkeyLengthFreq[i] #Create an array for determined keys
#	charCounter = 0 #Reset the character read pointer 
#	print("keylength:",sortedkeyLengthFreq[i])
#	while((charCounter+sortedkeyLengthFreq[i]) < length): #While there are still enough characters to make up a decoding cycle 
#		for j in range(0,sortedkeyLengthFreq[i]): #For each of the key spots in the key length
#			for k in range(0,255): #For each of the possible ASCII characters
#				if (ord(contents[charCounter+j]) == k): #If the content matches the character found
#					charFreq[j,k]+=1 #Increment the character frequency for that key spot
#		charCounter+=sortedkeyLengthFreq[i] #Increase the char counter by the keylength
#	for j in range(0,sortedkeyLengthFreq[i]): #For each of the keys in the key length
#		print("Key:",j)
#		maxFreq = 0 #Reset the maximum frequency counter
#		for k in range(0,255): #For each of the possible characters
#			if (charFreq[j,k] > maxFreq): #If the found frequency for a particular character was higher than the last found
#				maxFreq = charFreq[j,k] #Update the last found maximum frequency
#				key[j] = (k ^ ord(' ')) #Update the particular key information
#			#print(charFreq[j][k])
#	for j in range(0,sortedkeyLengthFreq[i]):
#		print(chr(key[j]))


