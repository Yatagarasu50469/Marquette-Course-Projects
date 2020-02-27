# Key length: 13
# Password: Diddledidodah
# Plaintext: Project Gutenberg's Alice's Adventures Under Ground, by Lewis Carroll

import sys

def main():
    args = sys.argv
    if (args[1] == "coincidence"):
        # inputFile, outputFile
        coincidenceAnalysis(args[2], args[3])
    elif (args[1] == "frequency"):
        # inputFile, keyLength
        frequencyAnalysis(args[2], int(args[3]))
    elif (args[1] == "decipher"):
        # inputFile, outputFile, password
        decipher(args[2], args[3], args[4])


def coincidenceAnalysis(inputFile, outputFile):
    inputBytes = bytearray()

    # Open the input file:
    with open(inputFile, "rb") as inFile:
        for line in inFile:
            for x in line:
                inputBytes.append(x)

    # Arbitrary limit to prevent extremely long calculations:
    analysisLength = 850 if len(inputBytes) > 850 else len(inputBytes)
    coincidences = [0] * analysisLength

    # Count the number of coincidences (same encoded character j distance away)
    for i in range(analysisLength):
        for j in range(1, analysisLength - i):
            if (inputBytes[i] == inputBytes[i + j]):
                coincidences[j] += 1

    # Output file as csv
    f = open(outputFile, 'w')
    for i in range(len(coincidences)):
        print(str(i) + ", " + str(coincidences[i]), file = f)
    # Graph the coincidence count and note the interval between spikes
    # This is the key length


def frequencyAnalysis(inputFile, keyLength):
    inputBytes = bytearray()
    # frequencies[character in key][ascii character]
    frequencies = [[0 for x in range(255)] for y in range(keyLength)]

    # Open the input file
    with open(inputFile, "rb") as inFile:
        for line in inFile:
            for x in line:
                inputBytes.append(x)

    # Arbitrary limit to prevent extremely long calculations:
    analysisLength = 7000 if len(inputBytes) > 7000 else len(inputBytes)

    # For each character in the key, count the frequncies of the characters it encoded
    for i in range(keyLength):
        for j in range(analysisLength):
            # Could break for small inputs...but it works for the given text:
            frequencies[i][inputBytes[i + j * keyLength]] += 1

    key = [0] * keyLength

    # Use the frequencies to determine what each character in the key is
    for i in range(keyLength):
        e_encoded = frequencies[i].index(max(frequencies[i]))
        # Assume the most common plaintext character is a space and see what the corresponding key character would need to be to get the encoded character
        key[i] = e_encoded ^ ord(' ')

    # Construct the key from the resultant bytes
    keyStr = ""
    for num in key:
        keyStr += chr(num)
    # Print the key
    print(keyStr)


# Given encipher/decipher code:
def decipher(nameIn, nameOut, password):
    toDecode = bytearray()
    result = bytearray()
    with open(nameIn, "rb") as inFile, open(nameOut, "wb") as outFile:
        for line in inFile:
            for x in line:
                toDecode.append(x)
        for i in range(len(toDecode)):
            result.append(toDecode[i] ^ ord(password[i%len(password)]))
        outFile.write(result)

# Python
if __name__ == "__main__":
    main()
