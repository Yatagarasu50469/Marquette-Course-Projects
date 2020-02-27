#Program: COSC_4931_Homework_5
#Version: 1.0
#Author: David Helminiak
#Date Created: 2 October 2018
#Date Last Modified: 23 October 2018

import sys, os, math, pydot, multiprocessing, random
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.utils import shuffle
from IPython.display import Image
from joblib import Parallel, delayed
from scipy import stats
from datetime import datetime

#Calculate gini index for a particular characteristic
def gini(df, column):
        gini=1
        #Get count of number of rows
        rowCount = (len(df))
        #Get list and count of unique values
        countVals = df[column].value_counts().rename_axis('unique_values').reset_index(name='counts')
        for i in range(0, (len(countVals))):
            gini=gini-(countVals.iloc[i,1]/rowCount)**2   
        return gini

#Determine split point information gain results for each projection value
def bestSplit_parhelper(i, df, originalGini, characteristic, classifierID, projectVals, splits):
    #Load dataframe into left and right nodes
    leftData = pd.DataFrame(columns = df.columns.values)
    rightData = pd.DataFrame(columns = df.columns.values)
   
    for j in range(0, len(df)): #For the length of the dataframe
        if (df[characteristic].iloc[j] < projectVals[i]): #For any values less than projectVals[i]
            leftData=leftData.append(df.iloc[j], ignore_index=True)
        else: #Otherwise, values are greater than or equal to projected value
            rightData=rightData.append(df.iloc[j], ignore_index=True)
    #Calculate gini values for left and right nodes
    leftGini=gini(leftData, classifierID)
    rightGini=gini(rightData, classifierID)
    #Calculate information gain and append to splits df
    combinedGini=((len(leftData)/len(df))*leftGini)+((len(rightData)/len(df))*rightGini)
    informationGain=originalGini-combinedGini
    splits['information_gain'].iloc[i]=informationGain
    return splits
    
#Determine the best possible information gain and splitting point for a characteristic
def bestSplit(df, originalGini, characteristic, classifierID):
    #Get list and count of unique values
    countVals = df[characteristic].value_counts().rename_axis('unique_values').reset_index(name='counts')
    countVals = countVals.sort_values(by='unique_values') #Sort countVals by values rather than count
    #Project mean values to find candidate splitting points
    projectVals=[]
    for i in range(0, len(countVals['unique_values'])-1):
        projectVals.append((countVals['unique_values'].iloc[i]+countVals['unique_values'].iloc[i+1])/2)
    #Test data splits
    splits = pd.DataFrame(data={'projection_values': projectVals, 'information_gain': np.nan})
    splitsCompiled = pd.DataFrame(data={'projection_values': projectVals, 'information_gain': np.nan})
    #For each of the possible splitting points calculate the resulting information gain
    num_threads = multiprocessing.cpu_count() #Determine number of available threads
    splits = Parallel(n_jobs=num_threads)(delayed(bestSplit_parhelper)(i, df, originalGini, characteristic, classifierID, projectVals, splits) for i in range(0, len(projectVals))) #Perform task in parallel
    #Splits returns as a list with every ith row's ith value being the next value desired
    #Transform splits list back into dataframe
    for i in range (0, len(splits)):
        splitsCompiled['information_gain'].iloc[i] = splits[i].iloc[i]['information_gain']
    #Locate the best split point if there is one
    if (len(splitsCompiled) is 0): #If there is no data to split
        return 0, 0 #Then there is no information to be gained and the split point is negligable
    splitPoint=splitsCompiled['projection_values'].iloc[splitsCompiled['information_gain'].idxmax()]
    maxGain = splitsCompiled['information_gain'].value_counts().idxmax()
    return maxGain, splitPoint

#Find best information gain over all of the characteristics and then split the data accordingly
def split(df, classifierID):
    #Calculate original gini
    originalGini = gini(df, classifierID)

    #Get characteristic names
    columnNames=list(df.columns.values)
    columnNames.remove(columnNames[len(columnNames)-1])

    #Determine which is best to perform split
    charSplit = pd.DataFrame(data={'characteristic': columnNames, 'information_gain': np.nan, 'splitting_point': np.nan})
    for i in range (0, len(columnNames)): 
        print('Split Evaluation: ', i/len(columnNames)*100, '%')
        charInformationGain, charSplitPoint = bestSplit(df, originalGini, columnNames[i], classifierID)
        charSplit['information_gain'].iloc[i]=charInformationGain
        charSplit['splitting_point'].iloc[i]=charSplitPoint
    splitChar=charSplit['characteristic'].iloc[charSplit['information_gain'].idxmax()]
    splitPoint=charSplit['splitting_point'].iloc[charSplit['information_gain'].idxmax()]

    #Actually split the data
    #Load dataframe into left and right nodes
    leftData = df.copy()
    rightData = df.copy()
    for i in range(0, len(df)): #For the length of the dataframe
        if (rightData[splitChar].iloc[i] < splitPoint): #For any values less than projectVals[i]
            rightData[splitChar].iloc[i] = np.nan #Set row in right side as NaN
        else: #Otherwise, values are greater than or equal to projected value
            leftData[splitChar].iloc[i] = np.nan #Set row in left side as NaN
    #Delete rows with nan values for both left and right side
    leftData=leftData.dropna()
    rightData=rightData.dropna()
    return splitChar, splitPoint, leftData, rightData

#Define a descision tree object
class DecisionTree:
    def __init__(self, df, identifier):
        #Define graph
        self.graph = pydot.Dot(graph_type='graph')
        #Define internal node variables
        self.data=df
        self.classifierID=df[identifier].value_counts().idxmax()
        self.species=df[identifier].value_counts().idxmax()
        self.gini=gini(df, identifier)
        if ((self.gini>0.01) and (len(self.data)>=5)):
            #print('Splitting with gini: ',self.gini,' and length: ', len(self.data))
            self.splitChar, self.splitPoint, self.leftChildData, self.rightChildData = split(df, identifier) 
            self.leftChild = DecisionTree(self.leftChildData, identifier)               
            self.rightChild = DecisionTree(self.rightChildData, identifier)
        else:
            #print('Done Splitting')
            self.leftChild = None
            self.rightChild = None
        #Define nodal information
        self.nodeInformation='Major Species: '+str(self.species)
        self.nodeInformation=self.nodeInformation+'\nNumber of Members: '+str(len(self.data))
        self.nodeInformation=self.nodeInformation+'\nGini: '+str(self.gini)
        if (self.leftChild is not None):
            self.nodeInformation = self.nodeInformation+'\n(Left): '+self.splitChar+'<'+str(self.splitPoint)
            self.leftEdge = pydot.Edge(self.nodeInformation, self.leftChild.nodeInformation)
            self.graph.add_edge(self.leftEdge)
            self.rightEdge = pydot.Edge(self.nodeInformation, self.rightChild.nodeInformation)
            self.graph.add_edge(self.rightEdge)

#Build the full tree from each sub-tree found for each node within a decision tree object
def buildGraph(tree):
    finalGraph = pydot.Dot(graph_type='graph') #Create a blank tree to hold all sub-trees
    root = tree.graph #Establish the tree's root sub-tree
    for i in range(0,len(root.get_edges())):
        finalGraph.add_edge(root.get_edges()[i])
    if (tree.leftChild is not None): #If there is a further sub-tree
        a = buildGraph(tree.leftChild) #Recursive call for left hand child 
        for i in range(0,len(a.get_edges())): #For all of the left hand child's edges
            finalGraph.add_edge(a.get_edges()[i])  #Add them to the final graph
        b = buildGraph(tree.rightChild) #Recursive call for right hand child 
        for i in range(0,len(b.get_edges())): #For all of the right hand child's edges
            finalGraph.add_edge(b.get_edges()[i]) #Add them to the final tree
    return finalGraph #Return back up the final tree

#Determine what the tree says the species should be 
def determine(startNode, dataPoint, classifierID):
    if (startNode.leftChild is not None):
        if (dataPoint[startNode.splitChar]<startNode.splitPoint):
            startNode = startNode.leftChild
        else:
            startNode = startNode.rightChild
        if (startNode.leftChild is not None):
            startNode = determine(startNode, dataPoint, classifierID)
        return startNode
    
#Test if a correct answer is obtained through the decision tree for a sample
def test(tree, testData, classifierID):
    successes=0
    for i in range (0,len(testData)): #For each of the test data cases
        if (testData.iloc[i][classifierID] == determine(tree, testData.iloc[i], classifierID).classifierID):
            successes=successes+1
    return successes 

#For each characteristic, find the greatest information gain possible, then split the data adding that node to a tree
#Repeat until termination criteria and then print out the tree to tree.png
print('ALLOCATING TRAINING/TESTING SETS')
trainSplit = 0.9 #Indicate portion of data to use for training; test is 1-trainSplit
filename = 'iris.csv' #Indicate filename containing dataset
iris = pd.read_csv(filename, na_values='?') #Read in dataset
iris = shuffle(iris) #Randomize iris dataset
numTrain = int(len(iris.index)*trainSplit) #Find number of training examples
trainingData =iris.iloc[0:numTrain] #Split off a training dataset
testData = iris.iloc[numTrain:(len(iris.index))] #Split off a test dataset

print('BUILDING TREE (THIS WILL TAKE A WHILE)')
tree = DecisionTree(trainingData,'species') #Construct a decision tree for determining class
finalGraph = buildGraph(tree) #Reconstruct full tree from descision tree object's sub-trees
finalGraph.write_png('tree.png') #Write the full tree to a file
Image(filename='tree.png') 

print('EVALUATING SUCCESS OF TREE FOR TESTING SET')
successes = test(tree, testData, 'species')
print('Successes: ',successes)
print('Failures:',len(testData)-successes)
print('Accuracy (%):',(successes/len(testData))*100)

