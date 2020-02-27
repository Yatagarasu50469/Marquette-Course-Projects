#Program: COSC_4931_Inversion_Sampling_Algorithm
#Version: 1.0
#Author: David Helminiak
#Date Created: 11 September 2018
#Date Last Modified: 11 Semptember 2018

from scipy.stats import binom
import numpy as np
import matplotlib.pyplot as plt
import random
import math


def isa(sampleLength, alpha, b): 
	alphaValue = 1 #Define alpha value
	sampleList = np.zeros(sampleLength) #Create empty list for number of samples desired
	aSampleCount = 0 #Define the number of acceptable samples found
	for aSampleCount in range(sampleLength):
		y = random.random()
		sampleList[aSampleCount] =
	hinfo = np.histogram(sampleList, 100) #Collect histogram information for the sample list
	plt.hist(sampleList, bins = 100, label = 'Accepted Samples') #Plot the actual histogram
	plt.xlabel('x')
	plt.ylabel('P(x)')
	plt.title('Inversion Sampling of pdf(x) = lamba*e^(-lambda*x), lambda=5')
	plt.show()
isa(10000)
