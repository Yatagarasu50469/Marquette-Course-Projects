#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Feb 10 17:09:17 2020

@author: scottstewart
"""
import numpy as np
import os
import math
import time
import random
import sys
import matplotlib.pyplot as plt
#k-means

def find_nearest(means, X):
    x_nearest = np.zeros((X.shape[0]))
    j = -1
    avg_distance = 0
    for x in X:
        j+=1
        min_dist = euclid_distance(means[0,:],x)
        min_choice = 0
        for i in range(1,means.shape[0]):
            dist = euclid_distance(means[i,:],x)
            if(dist< min_dist):
                min_dist = dist
                min_choice = i
        avg_distance += min_dist/k
        x_nearest[j] = min_choice
    return x_nearest, avg_distance
                
def euclid_distance(mean, x):
    distance = np.subtract(mean,x)
    distance = np.multiply(distance,distance)
    return np.sum(distance)

def new_centers(means,X,x_nearest):
    j = -1
    for mean in means:
        j+=1
        totals = np.zeros((feature_dim))
        count = 0
        for i in range(x_nearest.shape[0]):
            if(x_nearest[i]==j):
                totals = np.add(totals,X[i,:])
                count+=1
                
        if(count!=0):
            mean = totals/count
        else:
            mean = np.random.random_sample((feature_dim))
        means[j] = mean
    return means

def std_calc(means,X,x_nearest, std):
    j = -1
    
    for mean in means:
        j+=1
        
        count = 0
        
        for i in range(x_nearest.shape[0]):
            if(x_nearest[i]==j):
                count=count+1
        std_totals = np.zeros((count,feature_dim))     
       
        for i in range(x_nearest.shape[0]):
            if(x_nearest[i] == j):
                count =count- 1
                std_totals[count,:] = X[i]
        
        std[j,:] = np.std(std_totals, axis = 0)

    return std

distortion = []
max_k = 3000
min_k = 3000
for k in range(min_k,max_k+1):
    k_start_time = time.time()
    #k = 10
    
    feature_dim = 600
    
    max_iterations = 100
    
    means = np.random.random_sample((k,feature_dim))*2-1
    
    std = np.random.random_sample((k,feature_dim))
    
    X = []
    numpy_dir = "/home/scottstewart/Desktop/Spring_2020/EECE5890/Final_Project/data/attempt2/numpyarrays"
    na_saves = "/home/scottstewart/Desktop/Spring_2020/EECE5890/Final_Project/data/attempt2/na-saves"
    
    X_titles = os.listdir(numpy_dir)
    TOTAL = len(X_titles)
    X_vals = np.zeros((len(X_titles)))
    for xt in X_titles:
        X.append(np.load(numpy_dir+"/"+xt)[0,:])
    X = np.asarray(X)
    
    max_iterations = 100
    old_distance = sys.float_info.max
    for iteration in range(1,max_iterations+1):
        start_time = time.time()
        x_nearest,avg_distance = find_nearest(means,X)
        means = new_centers(means,X,x_nearest)
        std = std_calc(means,X,x_nearest, std)
        np.save(na_saves+"/k"+str(k)+".npy",means)
        np.save(na_saves+"/k"+str(k)+"-std.npy",std)

        end_time = time.time()
        print("Iteration: " +str(iteration)+", time: "+str(end_time-start_time)+", Average Distance: "+str(avg_distance))
        if(old_distance<=avg_distance):
            old_distance = avg_distance
            break;
        old_distance = avg_distance
    k_end_time = time.time()
    total_time = k_end_time - k_start_time
    minutes = int(total_time/60 -((total_time/60)%1))
    seconds = (total_time - minutes*60)%1
    milliseconds = (total_time - minutes*60 - seconds) -((total_time - minutes*60 - seconds)%0.001)
    sms = "{}".format('%06.3F' % (seconds+milliseconds))
    print("K-Value: " +str(k)+",\t time: "+str(minutes)+":"+str(sms)+",\t Average Distance: "+str(old_distance))
    distortion.append(old_distance)
    
fig=plt.figure()
ax=fig.add_axes([0,0,1,1])
ax.scatter(range(min_k,max_k+1), distortion, color='r')
ax.set_xlabel('K value')
ax.set_ylabel('Distortion')
ax.set_title('scatter plot')
plt.ylabel('Distortion')
plt.xlabel('K Value')
plt.title('Elbow method for optimal K')
plt.grid()
plt.show()