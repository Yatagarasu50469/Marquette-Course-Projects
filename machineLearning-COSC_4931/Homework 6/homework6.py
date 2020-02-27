#Program: COSC_4931_Homework_6
#Version: 1.0
#Author: David Helminiak
#Date Created: 10 October 2018
#Date Last Modified: 23 October 2018

import numpy as np
import pandas as pd
from sklearn.utils import shuffle
import math
from scipy.stats import multivariate_normal

#Object to hold class statistical data
class class_Data:
    def __init__(self, classLabel, mu, covArray):
        self.classLabel = classLabel
        self.mu = mu
        self.covArray = covArray

trainSplit = 0.5 #Indicate portion of data to use for training; test is 1-trainSplit
filename = 'iris.csv' #Indicate filename containing dataset
classifierID = 'species' #Specify the classifier name sought for prediction

iris = pd.read_csv(filename, na_values='?') #Read in dataset
iris = shuffle(iris) #Randomize dataset
numTrain = int(len(iris.index)*trainSplit) #Find number of training examples
trainingData = iris.iloc[0:numTrain] #Split off a training dataset
testData = iris.iloc[numTrain:(len(iris.index))] #Split off a test dataset

#Get characteristic names
columnNames=list(trainingData.columns.values)
columnNames.remove(columnNames[len(columnNames)-1])
#Get list of unique values
countVals = trainingData[classifierID].value_counts().rename_axis('unique_values').reset_index(name='counts')
#Construct list for each class' data (mu and covariance Array)
classDataList = []
#Build lookup tables for each class
for i in range (0, countVals['unique_values'].count()): #For each of the classes
    #Calculate mean and covarience, then copy them into storage; covariance unbiased as normalized by N-1
    mu = trainingData[trainingData[classifierID]==countVals['unique_values'].iloc[i]].mean().values
    covArray = trainingData[trainingData[classifierID]==countVals['unique_values'].iloc[i]].cov().values
    classObject = class_Data(countVals['unique_values'].iloc[i], mu, covArray)
    classDataList.append(classObject)

successes = 0 #Create a counter for the number of times a classifier was accuratley predicted
#For each of the test data points determine which classifier has the highest probability
for h in range (0, len(testData)):
    testPoint = testData.drop([classifierID], axis=1).iloc[h].values #Create a test point without the classifierID value included
    #Create a table to hold the probabilities for each classifier being true
    prob = pd.DataFrame(countVals['unique_values'])
    prob['posterior_prob']=np.nan 
    for i in range (0, countVals['unique_values'].count()): #For each of the possible classifierIDs
        #Evaluate the probabilities, choosing the species that has the maximum probability as the test classifier
        prob['posterior_prob'][i] = multivariate_normal.pdf(testPoint, classDataList[i].mu, classDataList[i].covArray)
        testClassifier = prob.loc[prob['posterior_prob'].idxmax()]['unique_values']
    #print("probs\n",prob)
    #Compare the test classifier to the actual classifier
    if (testClassifier == testData[classifierID].iloc[h]): #If the testClassifier was correct
        successes = successes+1 #Increase the count of successes
print("Accuracy: ", (successes/len(testData))*100)
print("Successes: ", successes)
print("Failures: ", len(testData)-successes)


