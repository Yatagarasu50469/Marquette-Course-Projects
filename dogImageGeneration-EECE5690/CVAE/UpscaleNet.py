#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jan 16 10:16:29 2020

@author: scottstewart
"""

from __future__ import absolute_import, division, print_function, unicode_literals


import tensorflow as tf
import math
import os
import time
import numpy as np
import sys
from scipy import misc
import shutil
from sklearn.model_selection import train_test_split
from IPython import display
from imageio import imread, imsave

import cv2

IMAGE_SIZE = (64,64,3);
OUTPUT_SIZE =  (256,256,3);


def load_datasets(XDirectory, YDirectory, start=0, total_images=1800):
    
    X=[]
    Y=[]
    names = []
    images = os.listdir(XDirectory)
    i =1
    ii = 0
    #print(len(images))
    for image in images:
        ii+=1
        if(ii<start):
            pass
        else:
            img = imread(XDirectory+"/"+str(image))
            imgY = imread(YDirectory+"/"+str(image))
            #img = rgb2hsv(img)
            #img = misc.imresize(img, (64, 64))
            img = cv2.resize(img,(IMAGE_SIZE[0],IMAGE_SIZE[1]))
            #imgY = misc.imresize(imgY, (720, 720))
            imgY = cv2.resize(imgY,(OUTPUT_SIZE[0],OUTPUT_SIZE[1]))
            #r, g, b = img[:,:,0], img[:,:,1], img[:,:,2]

            X.append(img)
            Y.append(imgY)
            names.append(str(image))
            i = i+1
            if(i>total_images):
                break
            #y.append(label.index(image_label))
    #y=np.array(y)    
    return X,Y, names

def Save_Output(model,train_image,name):
    print('here')
    prediction = model.predict(train_image);
    imsave(reconstructionDirectory+"/"+names, prediction)
def Clear_Model():
    inputa = tf.keras.layers.Input(shape=(IMAGE_SIZE[0], IMAGE_SIZE[1], 3))
    
    #up = tf.keras.layers.UpSampling2D(size=(6,6), input_shape = (64,64,3), interpolation ='bilinear')(inputa)
   
    #conva =  tf.keras.layers.Conv2DTranspose(filters=6, kernel_size=3, strides=(2, 2), activation='relu',padding = 'SAME')(inputa)
    
    conva = tf.keras.layers.Conv2DTranspose(filters=24, kernel_size=3, strides=(2, 2), padding="SAME", activation='relu')(inputa)
    convb = tf.keras.layers.Conv2DTranspose(filters=12, kernel_size=5, strides=(2, 2), padding="SAME", activation='relu')(conva)
    convc = tf.keras.layers.Conv2DTranspose(filters=12, kernel_size=3, strides=(2, 2), padding="SAME", activation='relu')(convb)
    convd = tf.keras.layers.Conv2D(filters=3, kernel_size=3, strides=(2, 2), padding="SAME")(convc)
    
    #re = tf.keras.layers.Reshape(target_shape=(720,720,3))(conva)
    
    model = tf.keras.models.Model(inputs=inputa,outputs=convd)
    
    model.compile(loss='logcosh', optimizer='adam')
    return model
    
BATCH_SIZE = 1
EPOCHS = 100
masterDirectory = "/home/scottstewart/Desktop/Spring_2020/EECE5890/Final_Project/data/attempt2"
readDirectory = masterDirectory+ "/modified"#"/re-re-generated"#"/reconstruction" #

doggos = masterDirectory+"/Training_Data"
reconstructionDirectory =  masterDirectory+"/re-generated"#"/re-reconstruciton" #
modelBackup = masterDirectory+"/model"
model = Clear_Model()

#model.load_weights(modelBackup+"/epoch-clear"+str(20)+".h5")
print('start')
#for epoch in range(1,EPOCHS+1):
#   start_time = time.time()
#   for i in range(0,10000-BATCH_SIZE, BATCH_SIZE):
#      try:
#        if(i%100==0):
#             print(str(epoch) +" " +str(i))
#        train_images, Y, names = load_datasets(readDirectory, doggos, start=i, total_images=BATCH_SIZE)
#        train_images = np.asarray(train_images)
#        Y = np.asarray(Y)
#        train_images = train_images.reshape(train_images.shape[0], IMAGE_SIZE[0], IMAGE_SIZE[1], 3).astype('float32')
#     
#        Y = Y.reshape(Y.shape[0], OUTPUT_SIZE[0], OUTPUT_SIZE[1], OUTPUT_SIZE[2]).astype('float32')
#     
#        model.train_on_batch(train_images, Y)
#      except:
#         pass
#   train_images, Y, names = load_datasets(readDirectory, doggos, start=10000-BATCH_SIZE, total_images=BATCH_SIZE)
#   train_images = np.asarray(train_images)
#   Y = np.asarray(Y)
#   train_images = train_images.reshape(train_images.shape[0], IMAGE_SIZE[0], IMAGE_SIZE[1], 3).astype('float32')
#   Y = Y.reshape(Y.shape[0], OUTPUT_SIZE[0], OUTPUT_SIZE[1], OUTPUT_SIZE[2]).astype('float32')
#   j = 0;
#   
#   loss = model.evaluate(train_images, Y)
#   end_time = time.time()
#   
#   print("LOSS: "+str(loss))
#   print('Epoch: {}, time elapse for current epoch {}'.format(epoch, end_time - start_time))
#   model.save_weights(modelBackup+"/epoch-clear"+str(EPOCHS)+".h5")
#%% LOAD WEIGHTS AND RUN RE-RECONSTRUCTION
model.load_weights(modelBackup+"/epoch-clear"+str(EPOCHS)+".h5")

reconstructions = os.listdir(readDirectory)
print(readDirectory)
last = -101
print("BEGIN RECONSTRUCTION")
for i in range(0,20579, BATCH_SIZE):
  if(i>last+100):
      print((i-i%100))
      last = (i-i%100)
  try:
      #print("HERE")
      train_images, __, names = load_datasets(readDirectory, readDirectory, start=i, total_images=BATCH_SIZE)
      #print(len(train_images))
      train_images = np.asarray(train_images)
      train_images = train_images.reshape(1, IMAGE_SIZE[0], IMAGE_SIZE[1], 3).astype('float32')
      predictions = model.predict(train_images);
      for j in range(BATCH_SIZE):
          imsave(reconstructionDirectory+"/"+str(names[j]), predictions[j])
      print("saved")
  except:
      pass
  
del model 
import gc
gc.collect()