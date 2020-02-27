#Program: COSC_4931_Bayes Theorem
#Version: 1.0
#Author: David Helminiak
#Date Created: 31 August 2018
#Date Last Modified: 31 August 2018

from scipy.stats import binom
import numpy
import matplotlib.pyplot as plt

probability = numpy.arange(0, 1.0, 0.01)
outcome = 62
observations = 90
plausability = binom.pmf(outcome, observations, probability)
fig, ax=plt.subplots()
ax.plot(probability, plausability)
plt.show()
