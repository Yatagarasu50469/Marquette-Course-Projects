function [J, grad] = costFunctionReg(theta, X, populationList, lambda)

m = length(populationList); %Find number of training examples
J = 0; %Initialize cost
grad = zeros(size(theta)); %Initialize gradient matrix of same size as theta
g = zeros(size(X*theta)); %Initialize sigmoid matrix
g = 1./(1+(exp(-X*theta))); %Calculate sigmoid
total = (1/m) * sum(-populationList'*log(g) - (1-populationList')*log(1-g)); %Calculate first part of cost
total1 = (lambda/(2*m)) * sum(theta(2:length(theta)).^2); %Calculate second part of cost
J = total + total1; %Add costs together
gradinit = (1/m) * (g-populationList)' * X; %Find initial gradient
grad = gradinit + (lambda/m)*theta'; %Update gradient
grad(1) = gradinit(1); %Fix grad matrix entry

end