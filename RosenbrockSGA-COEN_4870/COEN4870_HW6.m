maxGen = 20; %Maximum number of generations to generate
popSize = 100; %Number of individuals in the population
mutProb = 10; %Chance out of 100 that a bit will flip during mutation
crossProb = 20; %Chance out of 100 that the genes from the second random parent selected will be used in the offspring
RunStorage=zeros(3,30); %Storage array for each set of 30 runs
FinalStorage=zeros(3,30,27); %Storage array for all 27 sets of 30 runs
popCount = 0; %Counter for population size increase
mutCount = 0; %Counter for mutation rate increase

for a=1:27 %for all 27 parameter variations
    popCount = popCount + 1; %Increase counter
    mutCount = mutCount + 1; %Increase counter
    if (popCount == 10) %Every 9 cycles
        popCount = 1; %Reset the counter
        popSize = popSize*10; %Increase population size by a factor of 10
        mutProb = 10; %Reset the mutation probability
    end
    if (mutCount == 4) %Every 3 cycles
        mutCount = 1; %Reset the counter
        mutProb = mutProb+10; %Increase mutation probability by 10
        crossProb = 20; %Reset the crossover rate
    end
    fprintf('Run: %.0f Population Size: %.0f Mutation Rate: %.0f Crossover Rate: %.0f\n', a, popSize, mutProb, crossProb);
    for i=1:30 %for 30 runs
        [functionValue, fitnessCount, finishtime] = Rosenbrock_sga_modified(maxGen, popSize, mutProb, crossProb);
        RunStorage(1,i) = functionValue;
        RunStorage(2,i) = fitnessCount;
        RunStorage(3,i) = finishtime;
    end
    FinalStorage(:,:,a) = RunStorage; %Save acquired data into the final array
    crossProb = crossProb + 30; %Increase crossover rate by 30
end
FinalStorage(2,:,:) = 1./FinalStorage(2,:,:); %Invert function values to find fitness values
%Perform statistics extraction using the matlab functions min, max, mean, and std
%Ex. min(min(FinalStorage(3,:,:))) finds the minimum finish time in all runs
%Ex 2. min(min(FinalStorage(1,:,1:9))) finds minimum functionValue in first 9 runs
