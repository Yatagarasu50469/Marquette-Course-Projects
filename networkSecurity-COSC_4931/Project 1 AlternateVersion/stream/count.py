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

count = [0]*13
charCounter = 0
for i in range(0,length):
	if (charCounter == 13):
		charCounter = 0
	if(ord(contents[i]) == 10):
		count[charCounter]+=1
	charCounter+=1
print(count)
