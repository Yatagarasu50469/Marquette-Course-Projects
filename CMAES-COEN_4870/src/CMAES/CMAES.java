package CMAES;
/*
 * Covariance Matrix Adaptation Evolutionary Strategy
 * Author: David Helminiak
 * Description: ES as applied to finding the minimizer to a particular function
 * 
 * Date Created: 23 October 2017
 * Date Last Modified: 26 October 2017
 * 
 * Representation: Real-valued vectors
 * Parent Selection: Uniform random
 * Recombination: Intermediate
 * Mutation: CMA
 * Survivor Selection: Deterministic elitist
 * 
*/

import java.util.Random;

public class CMAES {
	//Alterable variables
	static boolean Rosenbrock = true; //Should the Rosenbrock function be used
	static boolean Bukin = false; //Should the Bukin-N.6 function be used
	static int popSize = 100; //Population size
	static int maxGen = 100; //Maximum number of generations 
	static double sigma = 0.3; //Mutation step size
	static double minMaxX = 50; //Max and minimum (-minMaxX) allowable x coordinate
	static double minMaxY = 50; //Max and minimum (-minMaxY) allowable y coordinate
	static double maxMinXYmesh = 2; //Give an arbitrary symmetric region of interest; convergence progress; 2 for Rosenbrock; 15 for Bukin
	//static double fitBound = 1/function(0.000000001,0.000000001); //acceptable convergence level
	
	//Unchanged variables
	static double x,y = 0; //Declare initial coordinates
	static int genCount = 0; //Generation counter
	static double functionValue = 0; //Calculated value of the function for an x,y
	static double bestFit = -1; //Initialize a best fit
	static double bestX = 0; //Initialize a best x
	static double bestY = 0; //Initialize a best y
	static double currBestFit = -1; //Initialize a current best fit
	static double currBestX = 0; //Initialize a current best x
	static double currBestY = 0; //Initialize a current best y	
	static boolean found = false; //Flag for if the minimizer is found
	static double inArea = 0; //Number of individuals found in the region of interest specified; shows convergence progress
	static double fitSum = 0; //Sum of fitness values
	static double xSum = 0; //Sum of the x values
	static double ySum = 0; //Sum of the y values
	static double meanFit = 0; //Mean of the fitness values
	static double meanX = 0; //Mean of the x values
	static double meanY = 0; //Mean of the y values
	static double lastMeanFit = 0; //Last mean of the fitness values
	static double stepX = 0; //Step size for x
	static double stepY = 0; //Step size for y
	static int par1, par2; //Parent indices
	static Random numGen = new Random(); //Random number generation unit for gaussian distribution

	
	//Main method
	public static void main(String[] args) {
		System.out.println("function: " + function(1,1));
		long startTime = System.currentTimeMillis(); //Start a timer
		//Initialize random population
		individual[] population = new individual[popSize]; //Initialize a current population
		individual[] oldPopulation = new individual[popSize]; //Initialize an old population
		individual[] newPopulation = new individual[popSize]; //Initialize a new population
		individual[] survivalPool = new individual[popSize*2]; //Initialize a pool from which only the strongest will rise
		for (int i = 0; i<popSize; i++) { //For each of the individuals within the current population
			//Setup positions
			x = Math.random()* (-minMaxX-(minMaxX)) + (minMaxX); //Generate a random x position
			y = Math.random()* (-minMaxY-(minMaxY)) + (minMaxY); //Generate a random y position
			population[i] = new individual(x,y); //Save the positions as a new individual within the population
			
			//Check # individuals in region of interest
			if ((x<=maxMinXYmesh) && (x>(-maxMinXYmesh)) && (y<=maxMinXYmesh) && (y>=(-maxMinXYmesh))) {
				inArea++; //Increment the relevant count
		    }
			
			//Determine initial fitness
		    functionValue = function(x,y); //Determine the value of the function for the given coordinates
		    if (functionValue == 0) { //If the minimizer is found
		    	functionValue=0.00000000001; //Set it to a very low non-zero number
		    }
			functionValue = 1/functionValue; //Inverse the function value to find fitness, higher is better
			population[i].fitness = functionValue; //Set the fitness of the individual
			//Check for new best individual
			if (population[i].fitness > currBestFit) {//If the found fitness is better than the best individual in the current generation
				currBestFit = population[i].fitness; //Save its fitness as the best in this generation
				currBestX = population[i].x; //Save its x as the best in this generation
				currBestY = population[i].y; //Save its y as the best in this generation
			}
			if (population[i].fitness > bestFit) { //If the found fitness was an improvement
				bestFit = population[i].fitness; //Save its fitness as the best
				bestX = population[i].x; //Save its x as the best
				bestY = population[i].y; //Save its y as the best
			}
			x=population[i].x; //Retrieve x
			y=population[i].y; //Retrieve y
			if ((1/functionValue) == 0) { //if the minimizer was found
		    	found = true; //Indicate that it was found
				break; //exit the loop
			}
	        fitSum+=functionValue; //Add fitness to the sum
	        xSum+=x; //Add x to the sum
	        ySum+=y; //Add y to the sum
		}
		lastMeanFit = fitSum/popSize; //Calculate the average fitness of the population
		meanX = xSum/popSize; //Calculate the average x value of the population
		meanY = ySum/popSize; //Calculate the average y value of the population
		
		//Print information on the generation
		System.out.println("Generation: " + genCount); //Print the current generation number
		System.out.println("The best minimizer in this generation was found at x: " + currBestX + " y: " + currBestY + " with a fitness of " + currBestFit); //Print out termination notice
		System.out.println("Individuals in the region of interest was: " + ((inArea/(double)popSize) * 100.0)+ "%\n");
		System.out.println("The average fitness was: " + lastMeanFit + "\n");

		while (true) {  //Loop until termination criteria is met
			if (found || (genCount >= maxGen)) { //If the minimizer has been found, or maxGen is reached
				break; //Exit the loop
			}
			genCount++; //Increment the generation count
			
			//Reset information for the next generation
			inArea = 0;
			currBestFit = -1; 
			currBestX = 0;
			currBestY = 0;
			fitSum = 0;
			meanFit = 0;
			meanX = 0;
			meanY = 0;
			oldPopulation = population; //Transfer the current population to the old
			
			for (int i = 0; i < popSize; i++) { //For all of the individuals within the population
				
				//Parent Selection: Uniform Random
				par1 = ((int)(Math.random()*popSize)); //Randomly select an individual from the old population
				par2 = ((int)(Math.random()*popSize)); //Randomly select a second individual from the old population
				individual parent1 = oldPopulation[par1]; //Retrieve individual information
				individual parent2 = oldPopulation[par2]; //Retrieve individual information

				//Recombination: Intermediate 
				x = (parent1.x+parent2.x)/2; //Average parent x coordinates
				y = (parent1.y+parent2.y)/2; //Average parent y coordinates
				population[i] = new individual(x,y); //Create a child in the current population
				if (parent1.fitness >= parent2.fitness) {  //If the first parent's fitness is larger or equal to the second
					population[i].bestParent = par1; //Indicate that the first parent had better fitness
				}
				else { 
					population[i].bestParent = par2; //Indicate that the second parent had better fitness
				}
				
				//Determine fitness
			    functionValue = function(x,y); //Determine the value of the function for the given coordinates
			    if (functionValue == 0) { //If the minimizer is found
			    	functionValue=0.00000000001; //Set it to a very low non-zero number
			    }
				functionValue = 1/functionValue; //Inverse the function value to find fitness, higher is better
				population[i].fitness = functionValue; //Set the fitness of the individual
				//Check for new best individual
				if (population[i].fitness > currBestFit) {//If the found fitness is better than the best individual in the current generation
					currBestFit = population[i].fitness; //Save its fitness as the best in this generation
					currBestX = population[i].x; //Save its x as the best in this generation
					currBestY = population[i].y; //Save its y as the best in this generation
				}
				if (population[i].fitness > bestFit) { //If the found fitness was an improvement
					bestFit = population[i].fitness; //Save its fitness as the best
					bestX = population[i].x; //Save its x as the best
					bestY = population[i].y; //Save its y as the best
				}

				if ((1/functionValue) == 0) { //if the minimizer was found
					found = true; //Indicate that it was found
					break; //exit the loop
				}

		        fitSum+=functionValue; //Add fitness to the sum	
		        xSum+=x; //Add x to the sum
		        ySum+=y; //Add y to the sum
			}
			if (found) { //If the minimizer has been found at this stage exit the outer loop
				break; //Exit the loop
			}
			meanFit = fitSum/popSize; //Calculate the average fitness of the population
			meanX = xSum/popSize; //Calculate the average x value of the population
			meanY = ySum/popSize; //Calculate the average y value of the population
			
			for (int i = 0; i < popSize; i++) { //For each of the individuals within the new generation
				//Sample from distribution an x and y term to mutate the child with
				stepX= (numGen.nextGaussian()*sigma);
				stepY= (numGen.nextGaussian()*sigma);
				population[i].x=population[i].x+stepX; //Mutate the individual's x
				population[i].y=population[i].y+stepY; //Mutate the individual's y
				
				//Re-determine current population fitness
				x = population[i].x; //Retrieve x
				y = population[i].y; //Retrieve y
			    functionValue = function(x,y); //Determine the value of the function for the given coordinates
			    if (functionValue == 0) { //If the minimizer is found
			    	functionValue=0.00000000001; //Set it to a very low non-zero number
			    }
				functionValue = 1/functionValue; //Inverse the function value to find fitness, higher is better
				population[i].fitness = functionValue; //Set the fitness of the individual
				//Check for new best individual
				if (population[i].fitness > currBestFit) {//If the found fitness is better than the best individual in the current generation
					currBestFit = population[i].fitness; //Save its fitness as the best in this generation
					currBestX = population[i].x; //Save its x as the best in this generation
					currBestY = population[i].y; //Save its y as the best in this generation
				}
				if (population[i].fitness > bestFit) { //If the found fitness was an improvement
					bestFit = population[i].fitness; //Save its fitness as the best
					bestX = population[i].x; //Save its x as the best
					bestY = population[i].y; //Save its y as the best
				}
				x=population[i].x; //Retrieve x
				y=population[i].y; //Retrieve y
				if ((1/functionValue) == 0) { //if the minimizer was found
			    	found = true; //Indicate that it was found
					break; //exit the loop
				}

				//Survivor Selection: Deterministic Elitist
				if (population[i].fitness < oldPopulation[population[i].bestParent].fitness) { //If the fitness is not an improvement on the best parent
					population[i] = oldPopulation[population[i].bestParent]; //Replace the child with the parent
				}
				else { //There was an improvement
					//System.out.println("difference: " + (population[i].fitness-oldPopulation[population[i].bestParent].fitness));
				}
				x = population[i].x; //set x for area check
				y = population[i].y; //set x for area check
				if ((x<=maxMinXYmesh) && (x>(-maxMinXYmesh)) && (y<=maxMinXYmesh) && (y>=(-maxMinXYmesh))) {
					inArea++; //Increment the relevant count
				}

			}
			
			//Print statistics
			System.out.println("Generation: " + genCount); //Print the current generation number
			System.out.println("The best minimizer in this generation was found at x: " + currBestX + " y: " + currBestY + " with a fitness of " + currBestFit); //Print out termination notice
			System.out.println("Individuals in the region of interest was: " + ((inArea/(double)popSize) * 100.0)+ "%\n");
			}
		
		if (found) { //If the minimizer was found
			System.out.println("The minimizer was found at x: " + bestX + " y: " + bestY + " with fitness: " + bestFit + " in " + genCount + " generation(s)."); //Print out termination notice
		}
		if (!found) { //If the maximum number of generations is exceeded 
			System.out.println("The best minimizer was found at x: " + bestX + " y: " + bestY + " with a fitness of " + bestFit + " after all generations ran"); //Print out termination notice
		}	
		System.out.println("It took: " + (((double)System.currentTimeMillis() - (double)startTime)/1000) +  " seconds to run"); //Print time it took to run
		
	}
	
	//Calculate the function value for a provided x and y value
	private static double function(double x2, double y2) {
		double functionValue = 0; //Initialize a function value		
		if (Rosenbrock) { //Should the user wish to use the Rosenbrock function
			double a = 1; //Initialize the a variable
			double b = 100; //Initialize the b variable
			
			functionValue = Math.pow((a-x2),2)+b*(Math.pow((y2-(Math.pow((x2),2))),2)); //Calculate the actual function value
		}
		if (Bukin) { //Should the user wish to use the Bukin-N.6 function
			functionValue = 100.0*Math.sqrt(Math.abs(y2-0.01*(Math.pow(x2,2))))+0.01*(x2+10.0); //Calculate the actual function value
		}		
			return functionValue; //Return the result
	}
}