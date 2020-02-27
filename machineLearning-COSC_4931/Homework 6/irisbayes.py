from scipy.stats import multivariate_normal
import numpy as np
from numpy.linalg import inv, det

class Gl:
    mu_setosa, sigma_setosa = 0,0
    mu_ver, sigma_ver = 0,0
    mu_vgc, sigma_vgc = 0,0
    

def get_points():
    with open("iris.csv") as iris:
        result = []
        iris.readline()
        for line in iris:
            elements = line.strip().split(',')
            tupla = (float(elements[1]), float(elements[2]), float(elements[3]), float(elements[4]), elements[5])
            result.append(tupla)
    return result
            
def mean(points, typo):
    meansl, meansw, meanpl, meanpw = 0, 0, 0, 0
    sig_sl_sl, sig_sl_sw, sig_sl_pl, sig_sl_pw = 0, 0, 0, 0
    sig_sw_sw, sig_sw_pl, sig_sw_pw = 0,0,0
    sig_pl_pl, sig_pl_pw = 0,0
    sig_pw_pw = 0
    count = 0
    for x in points:
        sl, sw, pl, pw, t = x
        if t == typo:
            count += 1
            meansl += sl
            meansw += sw
            meanpl += pl
            meanpw += pw
    meansl = meansl / count
    meansw = meansw / count
    meanpl = meanpl / count
    meanpw = meanpw / count
    for x in points:
        sl, sw, pl, pw, t = x
        if t == typo:
            sig_sl_sl += (sl-meansl)*(sl-meansl)
            sig_sl_sw += (sl-meansl)*(sw-meansw)
            sig_sl_pl += (sl-meansl)*(pl-meanpl)
            sig_sl_pw += (sl-meansl)*(pw-meanpw)
            sig_sw_sw += (sw-meansw)*(sw-meansw)
            sig_sw_pl += (sw-meansw)*(pl-meanpl)
            sig_sw_pw += (sw-meansw)*(pw-meanpw)
            sig_pl_pl += (pl-meanpl)*(pl-meanpl)
            sig_pl_pw += (pl-meanpl)*(pw-meanpw)
            sig_pw_pw += (pw-meanpw)*(pw-meanpw)
    sig_sl_sl = sig_sl_sl/count
    sig_sl_sw = sig_sl_sw/count
    sig_sl_pl = sig_sl_pl/count
    sig_sl_pw = sig_sl_pw/count
    sig_sw_sw = sig_sw_sw/count
    sig_sw_pl = sig_sw_pl/count
    sig_sw_pw = sig_sw_pw/count
    sig_pl_pl = sig_pl_pl/count
    sig_pl_pw = sig_pl_pw/count
    sig_pw_pw = sig_pw_pw/count
 
    means = np.array([meansl, meansw, meanpl, meanpw])
    sig_sl = (sig_sl_sl, sig_sl_sw, sig_sl_pl, sig_sl_pw)
    sig_sw = (sig_sl_sw, sig_sw_sw, sig_sw_pl, sig_sw_pw)
    sig_pl = (sig_sl_pl, sig_sw_pl, sig_pl_pl, sig_pl_pw)
    sig_pw = (sig_sl_pw, sig_sw_pw, sig_pl_pw, sig_pw_pw)
    sigma = np.array([[sig_sl_sl, sig_sl_sw, sig_sl_pl, sig_sl_pw],
                     [sig_sl_sw, sig_sw_sw, sig_sw_pl, sig_sw_pw],
                     [sig_sl_pl, sig_sw_pl, sig_pl_pl, sig_pl_pw],
                     [sig_sl_pw, sig_sw_pw, sig_pl_pw, sig_pw_pw]])
    return means, sigma

def initialize():
    points = get_points()
    Gl.mu_setosa, Gl.sigma_setosa = mean(points, "Iris-setosa")
    Gl.mu_ver, Gl.sigma_ver = mean(points, "Iris-versicolor")
    Gl.mu_vgc, Gl.sigma_vgc = mean(points, "Iris-virginica")
    print("Iris-setosa mean:", Gl.mu_setosa, " sigma: \n", Gl.sigma_setosa)

def get_probs(tupla):
    print("setosa", multivariate_normal.pdf(tupla,mean=Gl.mu_setosa, cov=Gl.sigma_setosa))
    print("versicolor", multivariate_normal.pdf(tupla,mean=Gl.mu_ver, cov=Gl.sigma_ver))
    print("virginica", multivariate_normal.pdf(tupla,mean=Gl.mu_vgc, cov=Gl.sigma_vgc))
    

if __name__=="__main__":
	initialize()

