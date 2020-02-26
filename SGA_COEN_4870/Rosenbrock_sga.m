%Name: SGA (Simple Genetic Algorithm)
%Author: David Helminiak
%Description: This program is based on Pseudocode by Goldberg (Publication
%unknown) and is intended to find the minimizer of a given function using a
%simple genetic algorithm. Plots for each generation are stored in the
%MATLAB directory. Set testNum variable appropriatley. 
%
%Date Created: 8 October 2017
%Date Modified: 9 Ocbober 2017

function sga

clc; clear all; %Clean the workspace
set(0,'DefaultFigureVisible','off') %Set all figures off
progStart = tic; %Start  timer for the total program (with figures if enabled) run time
figureFlag = 0; %Should figure generation be enabled
%INITIALIZATION OF PARAMETERS AND INITIAL POPULATION
%-------------------------------------------------------------------------------------------------
funcName = 'Rosenbrock'; %Name of the function being used
testNum = 0; %Test number; For figure development; Must manually increment
a = 1; b = 100; %Input constant variables
func = @(x,y) ((a-x).^2)+b*((y-(x.^2)).^2); %Input function
maxGen = 20; %Maximum number of generations to generate
popSize = 100; %Number of individuals in the population
mutProb = 10; %Chance out of 100 that a bit will flip during mutation
maxMinXY= 50; %Limits for function x,y bounds; minimum is negative of this value; maximum is positive version
maxMinXYmesh = 2; %Mesh x,y limits for plot; min is negative

finished = 0; %Flag for if the program has completed its objective before maxGen occurs
genCount = 0; %Current generation iteration
[X,Y]=meshgrid(-maxMinXYmesh:.1:maxMinXYmesh); %Meshgrid for the function
f=func(X,Y); %Function results for the basic meshgrid
zMax = max(max(f)); %Maximum Z visible  on plot
numCoordCol = 2*(length(de2bi(maxMinXY))+1); %Number of columns needed for the coordinates in binary with neg.
maxFunc = func(maxMinXY, maxMinXY); %Maximum allowable function value for the boundary conditions provided
minFunc = 0.0000000000000001; %Minimum allowable function value
maxFit = 1/minFunc; %Maximum allowable fitness value
minFit = 1/maxFunc; 
bestFunctionValue = maxFunc+1; %Lowest function value found for the Function
bestIndividual = zeros(1, numCoordCol+2); %Array for the best individual found
inArea = 0; %Count of how many individuals are within the min to max X and Y
totFigTime = 0; %Time accumulation for figure generation time

mkdir (sprintf('%s_Test_%.0f', funcName, testNum)); %Make a folder to save generated images

%Setup present and past population arrays
oldPop = zeros(popSize, numCoordCol+2); %column 1: index; column 2: fitness; column 3-etc.: binary traits
pop = zeros(popSize, numCoordCol+2); %column 1: index; column 2: fitness; column 3-etc.: binary traits
for i=1:1:popSize %For each of the individuals in the population
	pop(i,1) = i; %Index them appropriately 
	x = round((maxMinXY-(-maxMinXY)).*rand() + (-maxMinXY)); %Create a random x value in the bounds given
	y = round((maxMinXY-(-maxMinXY)).*rand() + (-maxMinXY)); %Create a random y value in the bounds given
	pop(i,4:((numCoordCol/2)+2)) = de2bi(abs(x),((numCoordCol/2)-1)); %Convert/save x as binary with a standard # of columns
	pop(i,((numCoordCol/2)+4):(numCoordCol+2)) = de2bi(abs(y),((numCoordCol/2)-1)); %Convert/save y as binary with a standard # of columns
	if (x < 0) %If x is negative
		pop(i,3) = 1; %Set flag for negative value
	end	
	if (y < 0) %If y is negative
		pop(i,((numCoordCol/2)+3)) = 1; %Set flag for negative value
	end
end

%Determine the initial fitness for all of the present population's individuals
for i=1:1:popSize %For each of the individuals in the population
	x = bi2de(pop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
	y = bi2de(pop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
	if (pop(i,3) == 1) %If the negative flag x is set
		x = -x; %Set x as negative
    end %End if
	if (pop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
		y = -y; %Set y as negative
	end	%End if
    if ((x<=maxMinXYmesh) && (x>(-maxMinXYmesh)) && (y<=maxMinXYmesh) && (y>=(-maxMinXYmesh))) %if the coordinate is within the expected area
            inArea = inArea + 1; %Increase a count of how many individuals have reasonable coordinates
    end %End if
    functionValue = func(x,y);
    if (functionValue == 0) %Should the function value be zero
        functionValue = minFunc; %make it nonzero, but very small
        finished = 1; %Set a flag to indicate that the program should terminate
        bestX = x; bestY = y; %Store the minimal values found
    end %End if

    if (functionValue <= bestFunctionValue)
        bestIndividual = pop(i,:);
        bestFunctionValue = functionValue;
    end %End if
    functionValue = 1/functionValue; %Trying to minimize function so take the inverse
    functionValue = ((functionValue-minFit)/(maxFit-minFit))*100; %Normalize values
	pop(i,2) = functionValue; %Initial fitness of the individual: the negative function result
end %End for
for i=1:1:popSize %For each of the individuals in the population
    pop(i,2) = pop(i,2) - min(pop(:,2)); %Subract the lowest fitness value from each of the fitness values, such that they ware all positive

end %End for

%Print initial conditions
fprintf('Generation %.0f \n',genCount) %Generation counter
fprintf('The best fitness of: %e was found at coordinates x: %.0f y: %.0f \n',max(pop(:,2)), x, y);
fprintf('The average fitness of the generation was %e \n',mean(pop(:,2)));
fprintf('There were %f percent of the population in the -%.0f <= x <= %.0f by -%.0f <= y <= %.0f area \n', ((inArea/popSize)*100),maxMinXYmesh,maxMinXYmesh,maxMinXYmesh,maxMinXYmesh);
fprintf('\n');

%-------------------------------------------------------------------------------------------------

%INITIAL POPULATION FIGURE
%-------------------------------------------------------------------------------------------------
figTime = tic; %Start a timer for time spent on figure generation here

    if (figureFlag == 1)
        genPlot1 = figure;
        hold on;
        surf(X,Y,f);
        for i=1:1:popSize %For each of the individuals in the population
        	x = bi2de(pop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
        	y = bi2de(pop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
        	if (pop(i,3) == 1) %If the negative flag x is set
        		x = -x; %Set x as negative
            end %End if
        	if (pop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
			y = -y; %Set y as negative
            end	%End if
            scatter3(x,y,func(x,y), 'filled', 'w');
        end %End for
        hold off;
    
        genPlot2 = figure;
        hold on;
        for i=1:1:popSize %For each of the individuals in the population
            x = bi2de(pop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
            y = bi2de(pop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
            if (pop(i,3) == 1) %If the negative flag x is set
            	x = -x; %Set x as negative
            end %End if
            if (pop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
            	y = -y; %Set y as negative
            end	%End if
            plot(x,y,'.','MarkerSize',40,'color','k');
        end %End for
        hold off;
        
        %Transfer plots to a combined figure
        genPlot = figure;
        h1 = subplot(1,2,1);
        h2 = subplot(1,2,2);
        transfer1 = findobj('Parent',genPlot1,'Type','axes');
        transfer2 = findobj('Parent',genPlot2,'Type','axes');
        copyobj(get(transfer1,'Children'),h1);
        copyobj(get(transfer2,'Children'),h2);
        
        %Set subplot 1 parameters
        xlim(h1, [-2 2]);
        ylim(h1, [-2 2]);
        zlim(h1, [0 zMax]);
        xlabel(h1, 'x');
        ylabel(h1, 'y');
        zlabel(h1, 'f');
        view(h1, -160, 50)
        grid(h1, 'on');
        
        %Set subplot 2 parameters
        xlim(h2, [-maxMinXY maxMinXY]);
        ylim(h2, [-maxMinXY maxMinXY]);
        xlabel(h2, 'x');
        ylabel(h2, 'y');
        grid(h2, 'on');
        
        %Set Main Figure parameters and save
        suptitle(sprintf('%s Function, Generation: %.0f, %.1f Percent of Population in Region',funcName,genCount,((inArea/popSize)*100))); 
        %set(genPlot, 'Position', [100, 100, 1200, 500]); %Resize the figure window
        genPlot.PaperUnits = 'inches';
        genPlot.PaperPosition = [0 0 20 10];
        print(genPlot, sprintf('%s_Test_%.0f/Generation %.0f.png',funcName,testNum,genCount),'-dpng');
        close(genPlot);
    end %End if
    totFigTime = totFigTime + toc(figTime); %Add figure generation time to amount to be removed
%-------------------------------------------------------------------------------------------------


%MAIN LOOP
%-------------------------------------------------------------------------------------------------
while (genCount < maxGen) && (finished == 0) %While the current generation iteration is less than the maximum
%Alternative while loop that shows convergence without stopping when min is found
%while (genCount < maxGen) %While the current generation iteration is less than the maximum
    fprintf('Generation %.0f \n',genCount+1) %Generation counter

	%Setup the future population
	newPop = zeros(popSize, numCoordCol+2); %column 1: index; column 2: fitness; column 3-etc.: binary traits
	for i=1:1:popSize %For each of the individuals in the population
		newPop(i,1) = i; %Index the individuals for the future
    end %End for
	genCount = genCount + 1; %Increment the current generation iteration count
	
	%PERFORM GENERATION
	%*******************************************************************************
	%Select random mates from the population
	j = 1; %Index counter for newPop
	j2 = (popSize/2)+1; %Second index counter for newPop
	if mod(popSize,2) == 0 %Check if the population size is even
        sumFitness = sum(pop(:,2)); %Find the sum of all fitness values within the population
		while (j <= popSize/2) %For half of the population size
            randomFitness = sumFitness*rand(); %Choose a random fitness between 0 and the sumFitness value
            i = 1; %Index counter for mate selection
            partialSum = 0; %Running sum of fitness values
            
            while ((partialSum <= randomFitness) && (i < popSize)) %While the partial sum is less than the number selected and the population size has not been reached
                i = i + 1; %increase the index counter for the mate selection
                partialSum = partialSum + pop(i,2); %Increase the running sum by that of the individual being examined
            end %End while
            mate1 = i; %Set the selected mate index
            
            randomFitness = sumFitness*rand(); %Choose another random fitness between 0 and the sumFitness value
            i = 1; %Reset index counter for mate selection
            partialSum = 0; %Running sum of fitness values
            while ((partialSum <= randomFitness) && (i < popSize)) %While the partial sum is less than the number selected and the population size has not been reached
                i = i + 1; %increase the index counter for the mate selection
                partialSum = partialSum + pop(i,2); %Increase the running sum by that of the individual being examined
            end %End while
            mate2 = i; %Set the selected mate index
            
            %Prepare for uniform crossover
            child1x = round(rand() + 1); %Should child 1 gain x from mate 1 or mate 2 randomly
            child1y = round(rand() + 1); %Should child 1 gain y from mate 1 or mate 2 randomly
            child2x = round(rand() + 1); %Should child 2 gain x from mate 1 or mate 2 randomly
            child2y = round(rand() + 1); %Should child 2 gain y from mate 1 or mate 2 randomly  
            
            %Child 1 crossover
            if (child1x == 1) %If mate 1 for x selected
                newPop(j,3:((numCoordCol/2)+2)) = pop(mate1,3:((numCoordCol/2)+2)); %First child takes x from mate1
            else %Mate 2 for x selected
                newPop(j,3:((numCoordCol/2)+2)) = pop(mate2,3:((numCoordCol/2)+2)); %First child takes x from mate2
            end %End if/else       
            if (child1y == 1) %If mate 1 for y selected
                newPop(j,((numCoordCol/2)+3):(numCoordCol+2)) = pop(mate1,((numCoordCol/2)+3):(numCoordCol+2)); %First child takes y from mate1
            else %Mate 2 for y selected
                newPop(j,((numCoordCol/2)+3):(numCoordCol+2)) = pop(mate2,((numCoordCol/2)+3):(numCoordCol+2)); %First child takes y from mate2
            end %End if/else
            
            %Child 2 crossover
            if (child2x == 1) %If mate 1 for x selected
                newPop(j2,3:((numCoordCol/2)+2)) = pop(mate1,3:((numCoordCol/2)+2)); %Second child takes x from mate1
            else %Mate 2 for x selected
                newPop(j2,3:((numCoordCol/2)+2)) = pop(mate2,3:((numCoordCol/2)+2)); %Second child takes x from mate2
            end %End if/else
            if (child2y == 1) %If mate 1 for y selected
                newPop(j2,((numCoordCol/2)+3):(numCoordCol+2)) = pop(mate1,((numCoordCol/2)+3):(numCoordCol+2)); %First child takes y from mate1
            else %Mate 2 for y selected
                newPop(j2,((numCoordCol/2)+3):(numCoordCol+2)) = pop(mate2,((numCoordCol/2)+3):(numCoordCol+2)); %First child takes y from mate2
            end %End if/else            
            
			j = j + 1; %Advance index for child 1
			j2 = j2 + 1; %Advance index for child 2
        end %End while
	else %If the population size is odd
		Error = 'The population size is odd, please use even values' %Print out an error message
    end %End if/else
	%*******************************************************************************
	

	%PERFORM MUTATION
	%*******************************************************************************
    for i = 1:1:popSize %For each of the individuals in the population
        for j = 3:1:((numCoordCol/2)+2) %For the x coordinate bits
            if (round((100).*rand()) < mutProb) %if a randomly generated number is less than the mutProb
                %Flip the designated bit
                if (newPop(i,j) == 1) %If the bit is 1
                    newPop(i,j) = 0; %Set the bit to 0
                else %The bit is 0
                    newPop(i,j) = 1; %Set the bit to 1
                end %End if
            end %End if
        end %End for
        for j = ((numCoordCol/2)+3):1:(numCoordCol+2) %For the y coordinate bits
            if (round((100).*rand()) < mutProb) %if a randomly generated number is less than the mutProb
                %Flip the designated bit
                if (newPop(i,j) == 1) %If the bit is 1
                    newPop(i,j) = 0; %Set the bit to 0
                else %The bit is 0
                    newPop(i,j) = 1; %Set the bit to 1
                end %End if
            end %End if
        end %End for
    end %End for
	
	%*******************************************************************************
	

	%CALCULATE GENERATION STATISTICS
	%*******************************************************************************
	
	%Determine the fitness for all of the new population's individuals
    inArea = 0; %Reset count of how many individuals are within the min to max X and Y
	for i=1:1:popSize %For each of the individuals in the population
		x = bi2de(newPop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
		y = bi2de(newPop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
		if (newPop(i,3) == 1) %If the negative flag x is set
			x = -x; %Set x as negative
        end %End if
		if (newPop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
			y = -y; %Set y as negative
		end	%End if
        functionValue = func(x,y);
        if (functionValue == 0) %Should the function value be zero
            functionValue = minFunc; %make it nonzero, but very small
            finished = 1; %Set a flag to indicate that the program should terminate
            bestX = x; bestY = y; %Store the minimal values found
        end %End if
        if (functionValue < bestFunctionValue)
            bestIndividual = newPop(i,:);
            bestFunctionValue = functionValue;
        end %End if
        functionValue = 1/functionValue; %Trying to minimize function so take the inverse
        functionValue = ((functionValue-minFit)/(maxFit-minFit))*100; %Normalize values
		newPop(i,2) =  functionValue; %Initial fitness of the individual: the negative function result
        if ((x<=maxMinXYmesh) && (x>(-maxMinXYmesh)) && (y<=maxMinXYmesh) && (y>=(-maxMinXYmesh))) %if the coordinate is within the expected area
            inArea = inArea + 1; %Increase a count of how many individuals have reasonable coordinates
        end %End if
    end	%End for
for i=1:1:popSize %For each of the individuals in the population
    newPop(i,2) = newPop(i,2) - min(newPop(:,2)); %Subract the lowest fitness value from each of the fitness values, such that they ware all positive
end %End for
    
	[~,bestFitIndex] = max(newPop(:,2));
    x = bi2de(newPop(bestFitIndex,4:((numCoordCol/2)+2))); %Retrieve |x|
	y = bi2de(newPop(bestFitIndex,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
    if (newPop(bestFitIndex,3) == 1) %If the negative flag x is set
		x = -x; %Set x as negative
	end
	if (newPop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
		y = -y; %Set y as negative
    end	
    
    fprintf('The best fitness of: %e was found at coordinates x: %.0f y: %.0f \n',max(newPop(:,2)), x, y);
    fprintf('The average fitness of the generation was %e \n',mean(newPop(:,2)));
    fprintf('There were %f percent of the population in the -%.0f <= x <= %.0f by -%.0f <= y <= %.0f area \n', ((inArea/popSize)*100),maxMinXYmesh,maxMinXYmesh,maxMinXYmesh,maxMinXYmesh);
    fprintf('\n');

    %Create and save figures for each generation
    	figTime = tic; %Start a timer for time spent on figure generation here

    if (figureFlag == 1)
        genPlot1 = figure;
        hold on;
        surf(X,Y,f);
        for i=1:1:popSize %For each of the individuals in the population
        	x = bi2de(newPop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
        	y = bi2de(newPop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
        	if (newPop(i,3) == 1) %If the negative flag x is set
        		x = -x; %Set x as negative
            end %End if
        	if (newPop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
			y = -y; %Set y as negative
            end	%End if
            scatter3(x,y,func(x,y), 'filled', 'w');
        end %End for
        hold off;
    
        genPlot2 = figure;
        hold on;
        for i=1:1:popSize %For each of the individuals in the population
            x = bi2de(newPop(i,4:((numCoordCol/2)+2))); %Retrieve |x|
            y = bi2de(newPop(i,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
            if (newPop(i,3) == 1) %If the negative flag x is set
            	x = -x; %Set x as negative
            end %End if
            if (newPop(i,((numCoordCol/2)+3)) == 1) %If the negative flag y is set
            	y = -y; %Set y as negative
            end	%End if
            plot(x,y,'.','MarkerSize',40,'color','k');
        end %End for
        hold off;
        
        %Transfer plots to a combined figure
        genPlot = figure;
        h1 = subplot(1,2,1);
        h2 = subplot(1,2,2);
        transfer1 = findobj('Parent',genPlot1,'Type','axes');
        transfer2 = findobj('Parent',genPlot2,'Type','axes');
        copyobj(get(transfer1,'Children'),h1);
        copyobj(get(transfer2,'Children'),h2);
        
        %Set subplot 1 parameters
        xlim(h1, [-2 2]);
        ylim(h1, [-2 2]);
        zlim(h1, [0 zMax]);
        xlabel(h1, 'x');
        ylabel(h1, 'y');
        zlabel(h1, 'f');
        view(h1, -160, 50)
        grid(h1, 'on');
        
        %Set subplot 2 parameters
        xlim(h2, [-maxMinXY maxMinXY]);
        ylim(h2, [-maxMinXY maxMinXY]);
        xlabel(h2, 'x');
        ylabel(h2, 'y');
        grid(h2, 'on');
        
        %Set Main Figure parameters and save
        suptitle(sprintf('%s Function, Generation: %.0f, %.1f Percent of Population in Region',funcName,genCount,((inArea/popSize)*100))); 
        %set(genPlot, 'Position', [100, 100, 1200, 500]); %Resize the figure window
        genPlot.PaperUnits = 'inches';
        genPlot.PaperPosition = [0 0 20 10];
        print(genPlot, sprintf('%s_Test_%.0f/Generation %.0f.png',funcName,testNum,genCount),'-dpng');
        close(genPlot);
    end %End if
    totFigTime = totFigTime + toc(figTime); %Add figure generation time to amount to be removed
    %*******************************************************************************

    
	% ADVANCE GENERATIONS
	%*******************************X************************************************
	
	oldPop = pop; %Advance the present to past generation
	pop = newPop; %Advance the future to present generation
	
	%*******************************************************************************

    
end %End while

fprintf('The program finished running in: %f seconds. \n',toc(progStart)-totFigTime') %Stop timer for the program run time
x = bi2de(bestIndividual(1,4:((numCoordCol/2)+2))); %Retrieve |x|
y = bi2de(bestIndividual(1,((numCoordCol/2)+4):(numCoordCol+2))); %Retrieve |y|
functionValue = func(x,y);
if (finished == 0)
    fprintf('The best individual was at x: %.0f y: %.0f with a function value of %f\n',x,y,functionValue); %Best individual found
else
    functionValue = func(bestX,bestY);
    fprintf('The best individual was at x: %.0f y: %.0f with a function value of %f\n',bestX,bestY,functionValue); %Best individual found
end %End script
end %End script