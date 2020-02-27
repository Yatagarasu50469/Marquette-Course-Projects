%Name: stochasticGradientDescent
%Author: David Helminiak
%Date Created: 28 November 2018
%Date Modified: 28 November 2018
%Reference: Basic functions have been reused from Coursera Machine Leearning
%course produced by Andrew Ng. 

function stochasticGradientDescent

    %WORKSPACE SETUP
    %-------------------------------------------------------------------------------------------------
    clc; clear all; %Clean the workspace
    set(0,'DefaultFigureVisible','on') %Set all figure vizualization on

    %FUNCTION SETUP
    %-------------------------------------------------------------------------------------------------
    %Set global population generation limits
    xmin = 1; xmax = 10;  ymin = 0; ymax = 10;
    maxRadius = 7; %Set maximum distance between initial point of a population and all other members
    populationSize = 500; %Set number of members for each population to be generated 
    numPopulations = 2; %Set number of populations to generate
    lambda = 10; %Set regularization parameter
    degree = 4; %Polynomial fitting degree
    trainingSize=0.8; %About how many of the points from each population should be used for training

    %MAIN PROGRAM
    %-------------------------------------------------------------------------------------------------
    populationList =[]; %Initialize empty matrix for population x and y values
    populationLabels = []; %Initialize empty matrix for population labels
    genPlot1 = figure; %Open a figure
    hold on; %Hold that figure
    %Generate categories through artificial normal-distritubiton
    for j = 1:1:numPopulations %For each of the populations to generate
        subPopulationList = zeros(populationSize,2); %Create an empty population matrix for the whole population
        subPopulationList(1,1) = (xmax-xmin).*rand() + xmin; %Choose a random initial x point within the range
        subPopulationList(1,2) = (ymax-ymin).*rand() + ymin; %Choose a random initial y point within the range

        popRadius = (maxRadius).*rand(); %Choose a random radius for the population within thel limit provided
        for i = 2:1:populationSize %For each of the additional population members
            %Geneate a random normally distributed point
            theta = rand()*popRadius; %Determine a random angle
            mag = sqrt(rand())*popRadius; %Determine a random magnitude within the radius
            subPopulationList(i,1) = subPopulationList(1,1) + mag.*cos(theta); %Determine an x coordinate
            subPopulationList(i,2) = subPopulationList(1,2) + mag.*sin(theta);%Determine a y coordinate
        end
        populationList = vertcat(populationList, subPopulationList); %Append the generated population to the global matrix
        populationLabel(1:populationSize,1) = j-1;
        populationLabels = vertcat(populationLabels, populationLabel);

        plot(subPopulationList(:,1), subPopulationList(:,2),'.')
    end

    trainingPopulationList = []; %Create an empty population matrix for the training data
    testingPopulationList = []; %Create an empty population matrix for the testing data
    trainingPopulationLabels = []; %Create an empty population matrix for the training data labels
    testingPopulationLabels = []; %Create an empty population matrix for the testing data labels
    numTraining = populationSize*trainingSize*numPopulations; %Find the number of points that should be used for training
    trainingPopulationList = populationList(1:numTraining,:);
    trainingPopulationLabels = populationLabels(1:numTraining,:);
    testingPopulationList = populationList((numTraining+1):length(populationList),:);
    testingPopulationLabels = populationLabels((numTraining+1):length(populationLabels),:);

    %Compute cost and gradient
    %For this to be stochastic it needs to be a random sample of populationList

    X = trainingPopulationList; y = trainingPopulationLabels; %Shorten names for sample and labels

    %Map inputs to polynomial features 
    X = mapFeature(X(:,1), X(:,2), degree);

    %Calculate gradient and cost in two-dimensions
    initial_theta = zeros(size(X, 2), 1); %Build an initial theta matrix
    options = optimset('GradObj', 'on', 'MaxIter', 1000);
    [theta, J, exit_flag] = fminunc(@(t)(costFunctionReg(t, X, y, lambda)), initial_theta, options);

    u = xmin:0.1:xmax; v = ymin:0.1:ymax; %Setup a grid of x's and y's for polynomial
    z = zeros(length(u), length(v));  %Setup a grid for population labels
    for i = 1:length(u)
        for j = 1:length(v)
            z(i,j) = mapFeature(u(i), v(j),degree)*theta; %Map a polynomial on the grid of degree specified using the values found for theta
        end
    end
    z = z';

    % Plot z = 0
    % Notice you need to specify the range [0, 0]
    contour(u, v, z, 'LineWidth', 2)
    contour(u, v, z, [0, 0], 'LineWidth', 3,'EdgeColor',[0 0 0])
    xlabel('x'); ylabel('y'); title('Stochastic Gradient Descent with Polynomial Boundary of Degree 4'); %Figure information
    legend('Population 0','Population 1','Gradient', 'Final Descision Boundary')

    X = testingPopulationList; label = testingPopulationLabels;
    X = mapFeature(X(:,1), X(:,2), degree);
    decPoint = 1./(1+(exp(-X*theta))); %Using sigmoid find a value for each point
    prediction = zeros(length(X), 1);
    for i = 1:length(X)
        if (decPoint(i) >= 0.5)
            prediction(i) = 1;
        end
    end
    accuracy = round(mean(prediction == label) * 100);
    fprintf('Training: %d %% provided accuracy of: %d %%\n', (trainingSize*100), accuracy)
end