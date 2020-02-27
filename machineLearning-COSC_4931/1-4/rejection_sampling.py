#Program: COSC_4931_Rejection_Sampling
#Version: 1.0
#Author: David Helminiak
#Date Created: 31 August 2018
#Date Last Modified: 31 August 2018

from scipy.stats import binom
import numpy as np
import matplotlib.pyplot as plt
import random

def ts(sampleLength, inputFunction, xmin, xmax, height): #Define a time series for a function
	func = lambda x: eval(inputFunction)
	sampleList = np.zeros(sampleLength) #Create empty list for number of samples desired
	aSampleCount = 0 #Define the number of acceptable samples found
	while (aSampleCount < sampleLength): #While the number of samples desired has not been reached
		x = np.random.uniform(xmin, xmax) #Generate a random value between 0 and 1
		y = np.random.uniform(0, height) #Choose a random value between (and including) 0 and the upper function limit
		if y < func(x): #If the random y value (limited by height) is under the function curve for a random x value
			sampleList[aSampleCount] = x #Accept the value to the sample list
			aSampleCount += 1 #Increment the count of accepted values
	hinfo = np.histogram(sampleList, 100) #Collect histogram information for the sample list
	plt.hist(sampleList, bins = 100, label = 'Accepted Samples') #Plot the actual histogram
	plt.xlabel('x')
	plt.ylabel('P(x)')
	plt.title('Rejection Sampling of pdf(x) = e^((-x^2)/2)')
	plt.show()

ts(10000, "np.exp((-x**2)/2)", -5, 5, 1)

