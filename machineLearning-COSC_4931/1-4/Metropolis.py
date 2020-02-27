#Program: COSC_4931_Metropolis
#Version: 1.0
#Author: David Helminiak
#Date Created: 19 September 2018
#Date Last Modified: 19 September 2018

from scipy.stats import binom
import numpy as np
import matplotlib.pyplot as plt
import csv
import pandas as pd
import math

def pdf(mu, sigma, x):
	return (1/(math.sqrt(2*math.pi*sigma**2)))*(math.e**(-((x-mu)**2)/(2*sigma**2)))

def metropolis(raw_data, N):
	mu = raw_data['height'].mean() #Find the mean of the data
	print('Mean: ', mu)
	sigma = raw_data['height'].std() #Find the standard deviation of the data
	print('Sigma: ', sigma)
	acceptedPoints = [] #Create an empty array to hold accepted points
	initial = 0 #Initialize an initial point

	baseResult = pdf(mu, sigma, initial)

	for i in range(N): #For the number of samples to be collected
		destPoint = initial + np.random.uniform(-1,1) #Add/subtact point by a random number
		evalDestPoint = pdf(mu, sigma, destPoint) #Evaluate the new destination point
		if evalDestPoint >= baseResult: #If the destination point evaluation is larger than the base result
			baseResult = evalDestPoint #Set the evaluated value as the new base result
			initial = destPoint #Set the destination point as the new initial point
		else: #If the evaluated destination point is less than the base result
			u = np.random.rand() #Calculate a random value
			if u < (evalDestPoint/baseResult): #If this value is less than the ratio of the evaluated to base result
				baseResult = evalDestPoint #Set the evaluated value as the new base result
				initial = destPoint #Set the destination point as the new initial point
		acceptedPoints.append(destPoint) #If acceptable, append the destination point to the accepted values
	return (acceptedPoints)

filename = 'howell1.csv'
raw_data = pd.read_csv(filename, na_values='?')
maleData = raw_data.loc[raw_data['male'] == 1]
maleUp18Data = maleData.loc[maleData['age'] >= 18]
femaleData = raw_data.loc[raw_data['male'] == 0]
femaleUp18Data = femaleData.loc[femaleData['age'] >= 18]

print('Males above age 18')
maleUp18Samples = metropolis(maleUp18Data, 10000)
print('Sample mean: ', np.asarray(maleUp18Samples).mean())
print('Sample sigma: ', np.asarray(maleUp18Samples).std())

print('\n\nFemales above age 18')
femaleUp18Samples = metropolis(femaleUp18Data, 10000)
print('Sample mean: ', np.asarray(femaleUp18Samples).mean())
print('Sample sigma: ', np.asarray(femaleUp18Samples).std())

plt.figure(1)
n, bins, patches = plt.hist(maleUp18Samples, bins='auto')
plt.xlabel('Height (cm)')
plt.ylabel('Frequency')
plt.title('Howell 1 Metropolis Height Distribution For Males >= 18')

plt.figure(2)
n, bins, patches = plt.hist(femaleUp18Samples, bins='auto')
plt.xlabel('Height (cm)')
plt.ylabel('Frequency')
plt.title('Howell 1 Metropolis Height Distribution For Females >= 18')
plt.show()
