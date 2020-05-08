#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Nov  3 16:28:56 2019

@author: scottstewart
"""

#%% IMPORTS
from __future__ import absolute_import, division, print_function, unicode_literals


import tensorflow as tf
import math
import os
import time
import numpy as np
import sys
import shutil
from sklearn.model_selection import train_test_split
from IPython import display
from imageio import imread, imsave
import cv2
import matplotlib.pyplot as plt
import random
#print(sys.version_info[0])
print("Num GPUs Available: ", len(tf.config.experimental.list_physical_devices('GPU')))

IMAGE_SIZE = (64,64,3);
#%% LOAD FUNCTIONS
def load_datasets(Directory, start=0, total_images=1800, red = True, green = True , blue = True, hsv = False, average= False ):
    
    X=[]
    images = os.listdir(Directory)
    i =1
    t =1
    avgRed = 0;
    stdRed =0;
    avgGreen =0;
    stdGreen =0;
    avgBlue =0;
    stdBlue =0;
    ii = 0
    print
    for image in images:
        ii+=1
        if(ii<start):
            pass
        else:
            img = imread(Directory+"/"+str(image))
            
            #img = rgb2hsv(img)
            img = cv2.resize(img, dsize=(IMAGE_SIZE[0], IMAGE_SIZE[1]), interpolation=cv2.INTER_CUBIC)
            #r, g, b = img[:,:,0], img[:,:,1], img[:,:,2]
            
            if(not red):
                img[:,:,0] = np.zeros([img.shape[0], img.shape[1]])
            if(not green):
                img[:,:,1] = np.zeros([img.shape[0], img.shape[1]])
            if(not blue):
                img[:,:,2] = np.zeros([img.shape[0], img.shape[1]])
            if(hsv):
                hsv=False
                #TODO actually implement a fast hsv conversion
            
            #gray = 0.2989 * r + 0.5870 * g + 0.1140 * b
            
            if(average):
                avgRed = avgRed*t/i+np.average(img[:,:,0])*1/i  
                stdRed = stdRed*t/i+np.std(img[:,:,0])*1/i
                
                avgGreen = avgGreen*t/i+np.average(img[:,:,1])*1/i
                stdGreen = stdGreen*t/i+np.std(img[:,:,1])*1/i
                
                avgBlue = (avgBlue*t/i)+(np.average(img[:,:,2])*1/i)
                stdBlue = stdBlue*t/i+np.std(img[:,:,2])*1/i
                
            X.append(img)
            
            if(i!=1):
                t+=1;
                
            i = i+1
            if(i>total_images):
                break
            #y.append(label.index(image_label))
    #y=np.array(y)
    if(average):
        for im in X:
            im[:,:,0] = (im[:,:,0]-avgRed)/stdRed
            im[:,:,1] = (im[:,:,1]-avgGreen)/stdGreen
            im[:,:,2] = (im[:,:,2]-avgBlue)/stdBlue
    
    return X



    
#%% CONSTANTS
BATCH_SIZE = 1 #originally 100, was 30 for a 64 by 64
TRAIN_BUF = 150;

epochs = 200
latent_dim = 600 #150 at 360 360
num_examples_to_generate = 16
#%% LOAD IMAGES
masterDirectory = "/home/scottstewart/Desktop/Spring_2020/EECE5890/Final_Project/data/attempt2"
#"/home/scottstewart/Desktop/USDA Project/AutoEncoder/mp3"#
readDirectory = masterDirectory+"/Training_Data"
saveDirectory = masterDirectory+"/re-re-generated"
reconstructionDirectory =  masterDirectory+"/reconstruction"
latentDirectory =  masterDirectory+"/latent_dim"
modelBackup = masterDirectory+"/model"
numpyfiles = masterDirectory+"/numpyarrays"
kmeanssaves = masterDirectory+"/na-saves"
#if os.path.exists(reconstructionDirectory):
#    shutil.rmtree(reconstructionDirectory)
#os.mkdir(reconstructionDirectory)
#
#if os.path.exists(saveDirectory):
#    shutil.rmtree(saveDirectory)
#os.mkdir(saveDirectory)
#try:
#    os.mkdir(modelBackup)
#except:
#    pass

#%% The Model
class CVAE(tf.keras.Model):
  def __init__(self, latent_dim):
    super(CVAE, self).__init__()
    self.latent_dim = latent_dim
    self.inference_net = tf.keras.Sequential(
      [
          tf.keras.layers.InputLayer(input_shape=(IMAGE_SIZE[0], IMAGE_SIZE[1], IMAGE_SIZE[2])),
          tf.keras.layers.Conv2D(
              filters=96, kernel_size=3, strides=(2, 2), activation='relu'), #24
          tf.keras.layers.Conv2D(
              filters=192, kernel_size=3, strides=(2, 2), activation='relu'),#48
          tf.keras.layers.Flatten(),
          # No activation
          tf.keras.layers.Dense((latent_dim+latent_dim)),
      ]
    )

    self.generative_net = tf.keras.Sequential(
        [
          tf.keras.layers.InputLayer(input_shape=(latent_dim,)),
          tf.keras.layers.Dense(units=2*IMAGE_SIZE[0]*IMAGE_SIZE[1]*IMAGE_SIZE[2], activation=tf.nn.relu),
          tf.keras.layers.Reshape(target_shape=(round(IMAGE_SIZE[0]/4), round(IMAGE_SIZE[1]/4), 32*IMAGE_SIZE[2])),
          tf.keras.layers.Conv2DTranspose(
              filters=192,
              kernel_size=3,
              strides=(2, 2),
              padding="SAME",
              activation='relu'),
          tf.keras.layers.Conv2DTranspose(
              filters=96,
              kernel_size=3,
              strides=(2, 2),
              padding="SAME",
              activation='relu'),
          # No activation
          tf.keras.layers.Conv2DTranspose(
              filters=3, kernel_size=3, strides=(1, 1), padding="SAME"),
        ]
    )

  @tf.function
  def sample(self, eps=None):
    if eps is None:
      eps = tf.random.normal(shape=(100, self.latent_dim))
    return self.decode(eps, apply_sigmoid=True)

  def encode(self, x):
    mean, logvar = tf.split(self.inference_net(x), num_or_size_splits=2, axis=1)
    return mean, logvar

  def reparameterize(self, mean, logvar):
    eps = tf.random.normal(shape=mean.shape)
    return eps * tf.exp(logvar * .5) + mean

  def decode(self, z, apply_sigmoid=False):
    logits = self.generative_net(z)
    if apply_sigmoid:
      probs = tf.sigmoid(logits)
      return probs

    return logits

#%% MODEL HELPER FUNCTIONS

optimizer = tf.keras.optimizers.Adam(1e-4)

def log_normal_pdf(sample, mean, logvar, raxis=1):
  log2pi = tf.math.log(2. * np.pi)
  return tf.reduce_sum(
      -.5 * ((sample - mean) ** 2. * tf.exp(-logvar) + logvar + log2pi),
      axis=raxis)

@tf.function
def compute_loss(model, x):
  mean, logvar = model.encode(x)
  z = model.reparameterize(mean, logvar)
  x_logit = model.decode(z)

  cross_ent = tf.nn.sigmoid_cross_entropy_with_logits(logits=x_logit, labels=x)
  logpx_z = -tf.reduce_sum(cross_ent, axis=[1, 2, 3])
  logpz = log_normal_pdf(z, 0., 0.)
  logqz_x = log_normal_pdf(z, mean, logvar)
  return -tf.reduce_mean(logpx_z + logpz - logqz_x)

@tf.function
def compute_apply_gradients(model, x, optimizer):
  with tf.GradientTape() as tape:
    loss = compute_loss(model, x)
  gradients = tape.gradient(loss, model.trainable_variables)
  optimizer.apply_gradients(zip(gradients, model.trainable_variables))

#%% run model
model = CVAE(latent_dim)
model.load_weights(modelBackup+"/epoch"+str(20)+"-80.h5")# is the reverse

#for epoch in range(1, epochs + 1):
# print("begin epoch "+str(epoch))
# start_time = time.time()
# for i in range(0,20580-TRAIN_BUF,TRAIN_BUF):
#     train_images = load_datasets(readDirectory,start=i, total_images=150, blue=True, average=False)
#     train_images = np.asarray(train_images)
#     train_images = train_images.reshape(train_images.shape[0], IMAGE_SIZE[0], IMAGE_SIZE[1], IMAGE_SIZE[2]).astype('float32')
#     train_images /= 255.
# 
#     train_dataset = tf.data.Dataset.from_tensor_slices(train_images).shuffle(TRAIN_BUF).batch(BATCH_SIZE)
#     for train_x in train_dataset:
#       compute_apply_gradients(model, train_x, optimizer)
#       pass
#     end_time = time.time()
#
#
# loss = tf.keras.metrics.Mean()
# test_images = load_datasets(readDirectory,start=20580-TRAIN_BUF, total_images=TRAIN_BUF)
# test_images = np.asarray(test_images)
# test_images = train_images.reshape(test_images.shape[0],  IMAGE_SIZE[0],  IMAGE_SIZE[1],  IMAGE_SIZE[2]).astype('float32')
# test_images /= 255.
# test_dataset = tf.data.Dataset.from_tensor_slices(test_images).shuffle(TRAIN_BUF).batch(BATCH_SIZE)
# for test_x in test_dataset:
#   loss(compute_loss(model, test_x))
#   pass
# elbo = 0;
# elbo = -loss.result()
# display.clear_output(wait=False)
# print('Epoch: {}, Test set ELBO: {}, '
#       'time elapse for current epoch {}'.format(epoch,
#                                                 elbo,
#                                                 end_time - start_time))
#model.save_weights(modelBackup+"/epoch"+str(epoch)+"-80.h5")
epoch = epochs
model.load_weights(modelBackup+"/epoch"+str(epoch)+"-80.h5")# is the reverse  

#%% Run Model on All Images
def generate_and_save_images(model, e, test_input):
  predictions = model.sample(test_input)
  for prediction in predictions:
      e+=1
      imsave(masterDirectory+'/generated/'+str(e)+'.png',prediction[:,:,:])
  
  return e;
  # tight_layout minimizes the overlap between 2 sub-plots
  #plt.savefig(masterDirectory+'/generated/image_at_epoch_{:04d}.png'.format(epoch))
  #plt.show()
def Random(i):
    random_vector_for_generation = tf.random.normal(
    shape=[num_examples_to_generate, latent_dim])
    i = generate_and_save_images(model, i, random_vector_for_generation)
    return i;
def OneAtATime(Directory, model, i, saveDirectory):
    images = os.listdir(Directory)
    image = images[i];
    im = imread(Directory+"/"+str(image))
    im = cv2.resize(im, dsize=(IMAGE_SIZE[0], IMAGE_SIZE[1]), interpolation=cv2.INTER_CUBIC)
    #imsave(reconstructionDirectory+"/"+str(image),im)
    im = im.astype(np.float32)
    #im = im.astype('float32')/255
    im = ((im.reshape(1, IMAGE_SIZE[0], IMAGE_SIZE[1], IMAGE_SIZE[2])).astype('float32'))/255
    
    imd = tf.data.Dataset.from_tensor_slices(im).batch(BATCH_SIZE)
    for t in imd:
        mean, logvar = model.encode(t)
        
    z = model.reparameterize(mean, logvar)
        
    x_logit = model.sample(z)
    narray = np.asarray(z)
    np.save(numpyfiles+"/"+str(image[:-4])+".npy",narray)
       
    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,x_logit.shape[0]):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    imsave(reconstructionDirectory+"/"+str(image),x_logit2)
    return


    #a3 = cv2.subtract(a1,a2)
    #cv2.imwrite(saveDirectory+"/"+str(image),a3)
    #imsave(saveDirectory+"/"+str(image),z1)
def FAKE_OneAtATime(Directory, model, j, saveDirectory):
    #images = os.listdir(Directory)
    #image = images[i];
    #im = imread(Directory+"/"+str(image))
    #im = rgb2hsv(im)
#    im = misc.imresize(im, (IMAGE_SIZE[0], IMAGE_SIZE[1]))
#    im = np.asarray(im)
#    im = im.astype(np.float32)
#    im = ((im.reshape(1, IMAGE_SIZE[0], IMAGE_SIZE[1], IMAGE_SIZE[2])).astype('float32'))/255
#    
#    imd = tf.data.Dataset.from_tensor_slices(im).batch(BATCH_SIZE)
#    for t in imd:
#        mean, logvar = model.encode(t)
    k = 3000;
    while(True):
        randomInt = random.randint(0,k-1)
        mean = np.load(kmeanssaves+"/k"+str(k)+".npy")
        mean = mean[randomInt,:]
        logvar = np.load(kmeanssaves+"/k"+str(k)+"-std.npy")
        logvar = logvar[randomInt,:]
        if(not(logvar[0] == 0 or math.isnan(logvar[0]))):
            break;
    
    mean = tf.convert_to_tensor(mean, dtype=np.float32)
    logvar = tf.convert_to_tensor(logvar, dtype=np.float32)
    
    mean = tf.reshape(mean,(1,latent_dim))
    logvar = tf.reshape(logvar,(1,latent_dim))
    
    z = model.reparameterize(mean, logvar)
        
    x_logit = model.sample(z)
    #narray = np.asarray(z)
    #np.save(numpyfiles+"/"+str(image[:-4])+".npy",narray)
       
    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,x_logit.shape[0]):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    
    imsave(saveDirectory+'/'+str(j)+'.png',x_logit2)
    return
def re_generated_OneAtATime(Directory, model, i, saveDirectory):
    images = os.listdir(Directory)
    image = images[i];
    im = imread(Directory+"/"+str(image))
    #im = rgb2hsv(im)
    #im = misc.imresize(im, (IMAGE_SIZE[0], IMAGE_SIZE[1]))
    im = cv2.resize(im, dsize=(IMAGE_SIZE[0], IMAGE_SIZE[1]), interpolation=cv2.INTER_CUBIC)
    im = np.asarray(im)
    im = im.astype(np.float32)
    im = ((im.reshape(1, IMAGE_SIZE[0], IMAGE_SIZE[1], IMAGE_SIZE[2])).astype('float32'))/255
    
    imd = tf.data.Dataset.from_tensor_slices(im).batch(BATCH_SIZE)
    for t in imd:
        mean, logvar = model.encode(t)
        
    z = model.reparameterize(mean, logvar)
        
    x_logit = model.sample(z)
    #narray = np.asarray(z)
    #np.save(numpyfiles+"/"+str(image[:-4])+".npy",narray)
       
    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,x_logit.shape[0]):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    imsave(saveDirectory+"/"+str(image),x_logit2)
    return



def modified_OneAtATime(Directory, model, i, saveDirectory):
    images = os.listdir(numpyfiles)
    image = images[i];
    z = np.load(numpyfiles+"/"+str(image[:-4])+".npy")
    for i in range(0,random.randint(100,latent_dim/2)):
        z[0,random.randint(0,latent_dim-1)] += (random.randint(0,1)-0.5)*2
    #print(max(z[0,:]))
    z = tf.convert_to_tensor(z, dtype=np.float32)
    x_logit = model.sample(z)

    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,1):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    #imsave(saveDirectory+"/"+str(image[:-4])+"-changed.png",x_logit2)
	
    z = np.load(numpyfiles+"/"+str(image[:-4])+".npy")
    #print(z.shape)
    z = tf.convert_to_tensor(z, dtype=np.float32)
    x_logit = model.sample(z)

    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,1):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    imsave(saveDirectory+"/"+str(image[:-4])+".png",x_logit2)
    return
	
def modified_MakeTwoAtATime(Directory, model, i, saveDirectory):
    images = os.listdir(numpyfiles)
    image = images[i]
    image2 = images[(i+1)%(len(images))]
    z = np.load(numpyfiles+"/"+str(image[:-4])+".npy")
    z2 = np.load(numpyfiles+"/"+str(image2[:-4])+".npy")
    for i in range(0,random.randint(100,latent_dim-1)):
        split = random.randint(0,latent_dim-1)
        z[0,split:] = z2[0,split:]
    #print(max(z[0,:]))
    z = tf.convert_to_tensor(z, dtype=np.float32)
    x_logit = model.sample(z)

    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,1):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    imsave(saveDirectory+"/"+str(image[:-4])+"-changed.png",x_logit2)
	
    z = np.load(numpyfiles+"/"+str(image[:-4])+".npy")
    #print(z.shape)
    z = tf.convert_to_tensor(z, dtype=np.float32)
    x_logit = model.sample(z)

    x_logit = (x_logit.numpy()).astype(np.float32)
    for i in range(0,1):
        x_logit2 = np.zeros(x_logit[i,:,:,:].shape)
        x_logit2[:,:,0] = ((x_logit[i,:,:,0]))
        x_logit2[:,:,1] = ((x_logit[i,:,:,1]))
        x_logit2[:,:,2] = ((x_logit[i,:,:,2]))
    x_logit2 = x_logit2*255
    x_logit2 = x_logit2.astype(np.uint8)
    imsave(saveDirectory+"/"+str(image[:-4])+".png",x_logit2)
    return

stdRed, stdGreen, stdBlue = 1,1,1
avgRed, avgGreen, avgBlue = 0,0,0
n=0

for i in range(0,len(os.listdir(readDirectory))):
    if(i%100==0):
        print("image: #"+str(i))
    FAKE_OneAtATime(kmeanssaves,model,i,saveDirectory)
    #i = Random(i)       
    #l = OneAtATime(readDirectory, model,i,saveDirectory)
    #l = re_generated_OneAtATime(masterDirectory+"/re-generated", model,i,masterDirectory+"/re-re-generated")
    #l = modified_OneAtATime(numpyfiles, model, i, masterDirectory+"/modified")
    #l = modified_MakeTwoAtATime(numpyfiles, model, i, masterDirectory+"/modified")
    
