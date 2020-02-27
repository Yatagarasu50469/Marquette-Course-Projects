// Copyright 2014 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package main;

import bot.individual;
import io.IORobot;

import java.awt.Point;
import java.io.*;
import java.util.*;
import java.lang.InterruptedException;
import java.lang.Thread;
import java.util.zip.*;

import move.AttackTransferMove;
import move.MoveResult;
import move.PlaceArmiesMove;


public class RunGame {


	//Changeable variables
	static int maxPop = 30; //Population size
	static int maxGen = 10; //Number of generations to be run
	static int mutProb = 10; //Probability out of 100 that a parameter will be mutated
	static double mutRate = 0.15; //Percent that a parameter will be changed by, should it be chosen for mutation

	//Game engine unchanged variables
	LinkedList<MoveResult> fullPlayedGame;
	LinkedList<MoveResult> player1PlayedGame;
	LinkedList<MoveResult> player2PlayedGame;
	int gameIndex = 1;
	String playerName1, playerName2;
	final String gameId, bot1Id, bot2Id, bot1Dir, bot2Dir;
	static Engine engine;

	//Bot unchanged variables
	public static individual currentIndividual = new individual(); //Individual being run through simulation; accessed from DNA_DRAGON bot
	public static File swap = new File("swap.txt"); //Create a new file for information to be swapped around between packages
	static FileWriter swapWriter;
	static int numRegions = 42; //Number of regions in the game
	static double fitness = 100.0; //Calculated fitness score
	static int numParam = 5; //Number of bulk parameters that may be mutated
	static double[] regionPref = new double[numRegions]; //Array of region preferences
	static double attackingRatio = 0; //Armies to attack or defend
	static double removeRatio = 0; //Armies to abandon a region
	static double reinforceRatio = 0; //Armies to reinforce a region
	static double aggression = 0; //Aggression of a bot between 0 and 1
	static String status; //String to hold an individual's status
	static double average = 0; //Average fitness for a given generation

	static individual[] oldPop = new individual[maxPop]; //Previous generation array
	static individual[] currPop = new individual[maxPop]; //Current generation array
	static individual parent1 = new individual(); //First parent
	static individual parent2 = new individual(); //Second parent
	static individual child1 = new individual(); //First child
	static individual child2 = new individual(); //Second child
	static individual bestInd = new individual(); //Best individual found so far

	public static void main(String args[]) throws Exception {

		for (int i = 0; i < maxPop; i++) { //For each of the members within the previous and current gen arrays
			currPop[i] = new individual(); //Initialize a new individual in the current generation
			//oldPop[i] = new individual(); //Initialize a new individual in the old generation
		}
		bestInd.setFitness(-100); //Initialize the best individual's fitness level for comparison

		if (swap.createNewFile()) {
			//System.out.println("File is created!"); //Debug line
		} else {
			//System.out.println("File already existed."); //debug line
		}
		swapWriter = new FileWriter(swap); //Create a new file writer for the swap file

		//INITIALIZATION; Run the first round
		RunGame run = new RunGame(args); //Engine setup
		System.out.println("INITIALIZATION COMMENCING");
		for (int i = 0; i < maxPop; i++) { //For each member of the population
			currentIndividual = new individual(); //Reset current individual
			//Set up and save randomized parameters as an individual

			for (int j = 0; j < numRegions; j++) { //For each of the possible regions
				regionPref[j] = Math.random()*100; //Create a random preference for each starting region
			}
			//NOTE ON REGIONS: #attacking armies %60 win rate vs. #defending armies 70% win ratio
			attackingRatio = Math.random()*100; //Create a random ratio for armies to attack or defend
			removeRatio = Math.random()*100; //Create a random ratio for armies to abandon a region
			reinforceRatio =  Math.random()*100; //Create a random ratio for armies to reinforce a region
			aggression = Math.random(); //Create a random starting aggression value
			//Save parameters to the current individual
			currentIndividual.setStartingRegions(regionPref);
			currentIndividual.setAttackingRatio(attackingRatio);
			currentIndividual.setRemoveRatio(removeRatio);
			currentIndividual.setReinforceRatio(reinforceRatio);
			currentIndividual.setAggression(aggression);
			currentIndividual.setID(i);

			writeToSwap(0, i); //Write information to the swap file
			run.go(); //Run an initial game for the individual

			//Assuming DNA_DRAGON is always set as player2, if otherwise, please change
			//Attempting to maximize fitness in population
			if (engine.winningPlayer() == null) { //Maxed out the number of rounds
				fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
				status = "Tie";
			}
			else if (engine.winningPlayer().getName().equals("player2")) { //If the bot won the round
				fitness =  1.0 / (double) engine.getRoundNr(); //Fitness is the inverse of the number of rounds
				status = "Win";
			}
			else { //The bot lost; rounds under 100
				fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
				status = "Loss";
			}
			currentIndividual.setStatus(status); //Set a particular parameter for the current individual
			currentIndividual.setFitness(fitness); //Set a particular parameter for the current individual
			currPop[i] = currentIndividual;
			System.out.println("Generation: 0 Individual: " + i + " Status: " + currPop[i].getStatus() + " Fitness: " + currPop[i].getFitness()); //Print the new fitness for the new population member
			System.out.println("ID in loop: " + currPop[i].getID());
			average = average + currPop[i].getFitness(); //Add up the fitness values for the generation
			if (currPop[i].getFitness() > bestInd.getFitness()) { //if the current individual's fitness is greater then that of the best
				//Set it as the new best individual
				bestInd.setStartingRegions(currPop[i].getStartingRegions());
				bestInd.setReinforceRatio(currPop[i].getReinforceRatio());
				bestInd.setAggression(currPop[i].getAggression());
				bestInd.setAttackingRatio(currPop[i].getAttackingRatio());
				bestInd.setFitness(currPop[i].getFitness());
				bestInd.setStatus(currPop[i].getStatus());
				bestInd.setID(currPop[i].getID());
			}
		}
		average = average / maxPop; //Calculate average fitness for the generation
		System.out.println("Generation 0" + " Average Fitness was " + average + "\n\n");
		average = 0; //Reset average calculation

		//MAIN LOOP; Run all remaining rounds
		System.out.println("Commencing evolutionary cycle");
		for (int j = 1; j <= maxGen; j++) { //For each of the number of games to be played
			System.out.println("Generation: " + j);
			oldPop = currPop; //Transfer the current population to the old
			for (int i = 0; i < maxPop; i++) { //For each member of the population

				//PARENT SELECTION
				parent1 = oldPop[(int)Math.round(Math.random()*(maxPop-1))]; //Select a random parent 1 from the old pop
				parent2 = oldPop[(int)Math.round(Math.random()*(maxPop-1))]; //Select a random parent 2 from the old po

				//RECOMBINATION
				child1 = recombine(child1); //Recombine parents to form a new child
				child2 = recombine(child2); //Recombine parents to form a second child

				//MUTATION
				child1 = mutate(child1); //Perform mutation on the first child
				child2 = mutate(child2); //Perform mutation on the second child

				//FITNESS EVALUATION
				currentIndividual = child1;//Load first child parameters into currentIndividual
				writeToSwap(j, i); //Write information to swap file
				run.go(); //Run the game for the first child

				//Assuming DNA_DRAGON is always set as player2, if otherwise, then change
				//Attempting to maximize fitness in population
				if (engine.winningPlayer() == null) { //Maxed out the number of rounds
					fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
					status = "Tie";
				}
				else if (engine.winningPlayer().getName().equals("player2")) { //If the bot won the round
					fitness =  1.0 / (double) engine.getRoundNr(); //Fitness is the inverse of the number of rounds
					status = "Win";
				}
				else { //The bot lost; rounds under 100
					fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
					status = "Loss";
				}
				currentIndividual.setStatus(status); //Set a particular parameter for the current individual
				currentIndividual.setFitness(fitness); //Set a particular parameter for the current individual
				child1 = currentIndividual; //Append the end performance to the child 1 parameters

				//Append the end performance to the first child's parameters
				currentIndividual = child2; //Load second child parameters into currentIndividual; bot will pull parameters from there
				writeToSwap(j, i); //Write information to swap file
				run.go(); //Run the game for the second child
				//Assuming DNA_DRAGON is always set as player2, if otherwise, please change
				//Attempting to maximize fitness in population
				if (engine.winningPlayer() == null) { //Maxed out the number of rounds
					fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
					status = "Tie";
				}
				else if (engine.winningPlayer().getName().equals("player2")) { //If the bot won the round
					fitness =  1.0 / (double) engine.getRoundNr(); //Fitness is the inverse of the number of rounds
					status = "Win";
				}
				else { //The bot lost; rounds under 100
					fitness = -1.0 / (double) engine.getRoundNr(); //Fitness is the negative inverse of the number of rounds
					status = "Loss";
				}
				currentIndividual.setStatus(status); //Set a particular parameter for the current individual
				currentIndividual.setFitness(fitness); //Set a particular parameter for the current individual
				child2 = currentIndividual; //Append the end performance to the child 2 parameters

				//CHILD SELECTION - SAVE THE FITTEST
				if (child1.getFitness() >= child2.getFitness()) { //if the first child is better than the second
					currPop[i] = child1; //Save the child into the current generation
				}
				else {
					currPop[i] = child2; //Save the child into the current generation
				}
				if (parent1.getFitness() >= currPop[i].getFitness()) { //if the first parent is better than the current
					currPop[i] = parent1; //Save the parent into the current generation
				}
				if (parent2.getFitness() >= currPop[i].getFitness()) { //if the second parent is better than the current
					currPop[i] = parent2; //Save the parent into the current generation
				}
				System.out.println("Generation: " + j + " Individual: " + i + " Status: " + currPop[i].getStatus() + " Fitness: " + currPop[i].getFitness()); //Print the new fitness for the new population member
				average=average+currPop[i].getFitness();
				if (currPop[i].getFitness() > bestInd.getFitness()) { //if the current individual's fitness is greater then that of the best
					//Set it as the new best individual
					bestInd.setStartingRegions(currPop[i].getStartingRegions());
					bestInd.setReinforceRatio(currPop[i].getReinforceRatio());
					bestInd.setAggression(currPop[i].getAggression());
					bestInd.setAttackingRatio(currPop[i].getAttackingRatio());
					bestInd.setFitness(currPop[i].getFitness());
					bestInd.setStatus(currPop[i].getStatus());
					bestInd.setID(currPop[i].getID());
				}
			}
			//Change provided tuning parameters for evolving bot(s) here for later rounds if desired
			average = average / maxPop; //Calculate average fitness for the generation
			System.out.println("Generation " + j + " Average Fitness was " + average + "\n\n");
			average = 0;
		}

		//PRINT END STATISTICS
		System.out.println("The best individual had fitness: " + bestInd.getFitness());
		File bestFile = new File("bestInd.txt"); //Create a new file for the best individual output
		if(bestFile.delete()){ //Delete the existing best individual file, if there is one
			bestFile.createNewFile(); //Create a new best individual file
		}
		FileWriter bestFileWriter = new FileWriter(bestFile); //Initialize the botDebug writer
		bestFileWriter.write("attackingRatio: " + bestInd.getAttackingRatio() + "\n"); //Write parameter to the swap file
		bestFileWriter.write("removeRatio: " + bestInd.getRemoveRatio() + "\n"); //Write parameter to the swap file
		bestFileWriter.write("reinforceRatio: " + bestInd.getReinforceRatio() + "\n"); //Write parameter to the swap file
		bestFileWriter.write("aggression: " + bestInd.getAggression() + "\n"); //Write parameter to the swap file
		bestFileWriter.write("Regions 1-42: ");
		for (int i = 0; i < numRegions; i++) { //For each of the regions on the map
			bestFileWriter.write(regionPref[i] + "\n"); //save each region preference to the swap file
		}
		bestFileWriter.close(); //Close the writer

        System.out.println("THE PROGRAM HAS BEEN COMPLETED");
		System.exit(0); //Exit the program
	}


	private static void writeToSwap(int genNum, int indNum) throws IOException { //Write information to the swap file
		swapWriter.close(); //Close the swapWriter
		if(swap.delete()) { //Delete the existing swap file, if it exists
			swap.createNewFile(); //Create a new swap file
		}
		swapWriter = new FileWriter(swap); //Re-initialize the swapWriter
		attackingRatio = currentIndividual.getAttackingRatio(); //Retrieve parameter from current individual
		removeRatio = currentIndividual.getRemoveRatio(); //Retrieve parameter from current individual
		reinforceRatio = currentIndividual.getReinforceRatio(); //Retrieve parameter from current individual
		aggression = currentIndividual.getAggression(); //Retrieve parameter from current individual
		regionPref = currentIndividual.getStartingRegions(); //Retrieve parameter from current individual
		//Write all of the parameters to the swap file
		swapWriter.write("generation: " + genNum + "\n"); //Write parameter to the swap file
		swapWriter.write("individual: " + indNum + "\n"); //Write parameter to the swap file
		swapWriter.write("attackingRatio: " + attackingRatio + "\n"); //Write parameter to the swap file
		swapWriter.write("removeRatio: " + removeRatio + "\n"); //Write parameter to the swap file
		swapWriter.write("reinforceRatio: " + reinforceRatio + "\n"); //Write parameter to the swap file
		swapWriter.write("aggression: " + aggression + "\n"); //Write parameter to the swap file
		for (int i = 0; i < numRegions; i++) { //For each of the regions on the map
			swapWriter.write("startingregion " + i + ": " + regionPref[i] + "\n"); //save each region preference to the swap file
		}
		swapWriter.close(); //Close the writer
	}

	public static individual recombine(individual child) {
		child.setID(1);
		for (int k = 0; k < numParam; k++) { //For each of the parameters
			if (k == 0) { //For the starting regions
				if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
					regionPref = parent1.getStartingRegions();
					child.setStartingRegions(parent1.getStartingRegions()); //Use parameter from parent 1
				}
				else { //Else
					parent2.getStartingRegions();
					child.setStartingRegions(parent2.getStartingRegions()); //Use parameter from parent 2
				}
			}
			if (k == 1) { //For the attacking ratio
				if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
					child.setAttackingRatio(parent1.getAttackingRatio()); //Use parameter from parent 1
				}
				else { //Else
					child.setAttackingRatio(parent2.getAttackingRatio()); //Use parameter from parent 2
				}
			}
			if (k == 2) { //For the removal ratio
				if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
					if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
						child.setRemoveRatio(parent1.getRemoveRatio()); //Use parameter from parent 1
					}
					else { //Else
						child.setRemoveRatio(parent2.getRemoveRatio()); //Use parameter from parent 2
					}
				}
			}
			if (k == 3) { //For the reinforcement ratio
				if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
					child.setReinforceRatio(parent1.getReinforceRatio()); //Use parameter from parent 1
				}
				else { //Else
					child.setReinforceRatio(parent2.getReinforceRatio()); //Use parameter from parent 2
				}
			}
			if (k == 4) { //For aggression
				if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
					child.setAggression(parent1.getAggression()); //Use parameter from parent 1
				}
				else { //Else
					child.setAggression(parent2.getAggression()); //Use parameter from parent 2
				}
			}
		}
		return child;
	}

	private static individual mutate(individual child) {
		for (int k = 0; k < numParam; k++) { //For each of the parameters
			if (k == 0) { //For the starting regions
				regionPref = child.getStartingRegions(); //Pull the parameter from the child
				for (int l = 0; l < numRegions; l++) { //For each of the regions
					if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
						if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
							regionPref[l] = regionPref[l] + regionPref[l]*mutRate; //Apply positive mutation rate
						}
						else { //Else
							regionPref[l] = regionPref[l] - regionPref[l]*mutRate; //Apply negative mutation rate
						}
					}
				}
				child.setStartingRegions(regionPref); //Set the mutated parameter to the child
			}
			if (k == 1) { //For the attacking ratio
				attackingRatio = child.getAttackingRatio(); //Pull the parameter from the child
				if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
					if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
						attackingRatio = attackingRatio + attackingRatio*mutRate; //Apply positive mutation rate
					}
					else { //Else
						attackingRatio = attackingRatio - attackingRatio*mutRate; //Apply negative mutation rate
					}
				}
				child.setAttackingRatio(attackingRatio); //Set the mutated parameter to the child
			}
			if (k == 2) { //For the removal ratio
				removeRatio = child.getRemoveRatio(); //Pull the parameter from the child
				if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
					if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
						removeRatio = removeRatio + removeRatio*mutRate; //Apply positive mutation rate
					}
					else { //Else
						removeRatio = removeRatio - removeRatio*mutRate; //Apply negative mutation rate
					}
				}
				child.setRemoveRatio(removeRatio); //Set the mutated parameter to the child
			}
			if (k == 3) { //For the reinforcement ratio
				reinforceRatio = child.getReinforceRatio(); //Pull the parameter from the child
				if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
					if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
						reinforceRatio = reinforceRatio + reinforceRatio*mutRate; //Apply positive mutation rate
					}
					else { //Else
						reinforceRatio = reinforceRatio - reinforceRatio*mutRate; //Apply negative mutation rate
					}
				}
				child.setReinforceRatio(reinforceRatio); //Set the mutated parameter to the child
			}
			if (k == 4) { //For aggression
				aggression = child.getAggression(); //Pull the parameter from the child
				if ((Math.random()*100) <= mutProb) { //If a random number 0:100 falls within the mutProb range
					if ((Math.random() > 0.5)) { //If a random value is above 0.5; 50% chance
						aggression = aggression + aggression*mutRate; //Apply positive mutation rate
					}
					else { //Else
						aggression = aggression - aggression*mutRate; //Apply negative mutation rate
					}
				}
				child.setAggression(aggression); //Set the mutated parameter to the child
			}
		}
		return child;
	}

	public RunGame(String args[])
	{
		this.gameId = args[0];
		this.bot1Id = args[1];
		this.bot2Id = args[2];
		this.bot1Dir = args[3];
		this.bot2Dir = args[4];
		this.playerName1 = "player1";
		this.playerName2 = "player2";
	}

	private void go() throws IOException, InterruptedException
	{

		Map initMap, map;
		Player player1, player2;
		IORobot bot1, bot2;
		int startingArmies;

		//setup the bots
		bot1 = new IORobot(bot1Dir);
		bot2 = new IORobot(bot2Dir);

		startingArmies = 5;
		player1 = new Player(playerName1, bot1, startingArmies);
		player2 = new Player(playerName2, bot2, startingArmies);

		//setup the map
		initMap = makeInitMap();
		map = setupMap(initMap);

        //start the engine
		this.engine = new Engine(map, player1, player2);

		//send the bots the info they need to start
		bot1.writeInfo("settings your_bot " + player1.getName());
		bot1.writeInfo("settings opponent_bot " + player2.getName());
		bot2.writeInfo("settings your_bot " + player2.getName());
		bot2.writeInfo("settings opponent_bot " + player1.getName());
		sendSetupMapInfo(player1.getBot(), initMap);
		sendSetupMapInfo(player2.getBot(), initMap);
		this.engine.distributeStartingRegions(); //decide the player's starting regions
		this.engine.recalculateStartingArmies(); //calculate how much armies the players get at the start of the round (depending on owned SuperRegions)
		this.engine.sendAllInfo();

		//play the game
		while(this.engine.winningPlayer() == null && this.engine.getRoundNr() <= 100)
		{
			bot1.addToDump("Round " + this.engine.getRoundNr() + "\n");
			bot2.addToDump("Round " + this.engine.getRoundNr() + "\n");
			this.engine.playRound();
		}
		fullPlayedGame = this.engine.getFullPlayedGame();
		player1PlayedGame = this.engine.getPlayer1PlayedGame();
		player2PlayedGame = this.engine.getPlayer2PlayedGame();
		finish(bot1, bot2);
	}

	//The current game has finished running
	private void finish(IORobot bot1, IORobot bot2) throws InterruptedException
	{
		bot1.finish();
		Thread.sleep(200);
		bot2.finish();
		Thread.sleep(200);

		Thread.sleep(200);
		// write everything
		// String outputFile = this.writeOutputFile(this.gameId, this.engine.winningPlayer());
		this.saveGame(bot1, bot2);
	}

	//Initialize the map
	private Map makeInitMap()
	{
		Map map = new Map();
		SuperRegion northAmerica = new SuperRegion(1, 5);
		SuperRegion southAmerica = new SuperRegion(2, 2);
		SuperRegion europe = new SuperRegion(3, 5);
		SuperRegion afrika = new SuperRegion(4, 3);
		SuperRegion azia = new SuperRegion(5, 7);
		SuperRegion australia = new SuperRegion(6, 2);

		Region region1 = new Region(1, northAmerica);
		Region region2 = new Region(2, northAmerica);
		Region region3 = new Region(3, northAmerica);
		Region region4 = new Region(4, northAmerica);
		Region region5 = new Region(5, northAmerica);
		Region region6 = new Region(6, northAmerica);
		Region region7 = new Region(7, northAmerica);
		Region region8 = new Region(8, northAmerica);
		Region region9 = new Region(9, northAmerica);
		Region region10 = new Region(10, southAmerica);
		Region region11 = new Region(11, southAmerica);
		Region region12 = new Region(12, southAmerica);
		Region region13 = new Region(13, southAmerica);
		Region region14 = new Region(14, europe);
		Region region15 = new Region(15, europe);
		Region region16 = new Region(16, europe);
		Region region17 = new Region(17, europe);
		Region region18 = new Region(18, europe);
		Region region19 = new Region(19, europe);
		Region region20 = new Region(20, europe);
		Region region21 = new Region(21, afrika);
		Region region22 = new Region(22, afrika);
		Region region23 = new Region(23, afrika);
		Region region24 = new Region(24, afrika);
		Region region25 = new Region(25, afrika);
		Region region26 = new Region(26, afrika);
		Region region27 = new Region(27, azia);
		Region region28 = new Region(28, azia);
		Region region29 = new Region(29, azia);
		Region region30 = new Region(30, azia);
		Region region31 = new Region(31, azia);
		Region region32 = new Region(32, azia);
		Region region33 = new Region(33, azia);
		Region region34 = new Region(34, azia);
		Region region35 = new Region(35, azia);
		Region region36 = new Region(36, azia);
		Region region37 = new Region(37, azia);
		Region region38 = new Region(38, azia);
		Region region39 = new Region(39, australia);
		Region region40 = new Region(40, australia);
		Region region41 = new Region(41, australia);
		Region region42 = new Region(42, australia);

		region1.addNeighbor(region2);
		region1.addNeighbor(region4);
		region1.addNeighbor(region30);
		region2.addNeighbor(region4);
		region2.addNeighbor(region3);
		region2.addNeighbor(region5);
		region3.addNeighbor(region5);
		region3.addNeighbor(region6);
		region3.addNeighbor(region14);
		region4.addNeighbor(region5);
		region4.addNeighbor(region7);
		region5.addNeighbor(region6);
		region5.addNeighbor(region7);
		region5.addNeighbor(region8);
		region6.addNeighbor(region8);
		region7.addNeighbor(region8);
		region7.addNeighbor(region9);
		region8.addNeighbor(region9);
		region9.addNeighbor(region10);
		region10.addNeighbor(region11);
		region10.addNeighbor(region12);
		region11.addNeighbor(region12);
		region11.addNeighbor(region13);
		region12.addNeighbor(region13);
		region12.addNeighbor(region21);
		region14.addNeighbor(region15);
		region14.addNeighbor(region16);
		region15.addNeighbor(region16);
		region15.addNeighbor(region18);
		region15.addNeighbor(region19);
		region16.addNeighbor(region17);
		region17.addNeighbor(region19);
		region17.addNeighbor(region20);
		region17.addNeighbor(region27);
		region17.addNeighbor(region32);
		region17.addNeighbor(region36);
		region18.addNeighbor(region19);
		region18.addNeighbor(region20);
		region18.addNeighbor(region21);
		region19.addNeighbor(region20);
		region20.addNeighbor(region21);
		region20.addNeighbor(region22);
		region20.addNeighbor(region36);
		region21.addNeighbor(region22);
		region21.addNeighbor(region23);
		region21.addNeighbor(region24);
		region22.addNeighbor(region23);
		region22.addNeighbor(region36);
		region23.addNeighbor(region24);
		region23.addNeighbor(region25);
		region23.addNeighbor(region26);
		region23.addNeighbor(region36);
		region24.addNeighbor(region25);
		region25.addNeighbor(region26);
		region27.addNeighbor(region28);
		region27.addNeighbor(region32);
		region27.addNeighbor(region33);
		region28.addNeighbor(region29);
		region28.addNeighbor(region31);
		region28.addNeighbor(region33);
		region28.addNeighbor(region34);
		region29.addNeighbor(region30);
		region29.addNeighbor(region31);
		region30.addNeighbor(region31);
		region30.addNeighbor(region34);
		region30.addNeighbor(region35);
		region31.addNeighbor(region34);
		region32.addNeighbor(region33);
		region32.addNeighbor(region36);
		region32.addNeighbor(region37);
		region33.addNeighbor(region34);
		region33.addNeighbor(region37);
		region33.addNeighbor(region38);
		region34.addNeighbor(region35);
		region36.addNeighbor(region37);
		region37.addNeighbor(region38);
		region38.addNeighbor(region39);
		region39.addNeighbor(region40);
		region39.addNeighbor(region41);
		region40.addNeighbor(region41);
		region40.addNeighbor(region42);
		region41.addNeighbor(region42);

		map.add(region1); map.add(region2); map.add(region3);
		map.add(region4); map.add(region5); map.add(region6);
		map.add(region7); map.add(region8); map.add(region9);
		map.add(region10); map.add(region11); map.add(region12);
		map.add(region13); map.add(region14); map.add(region15);
		map.add(region16); map.add(region17); map.add(region18);
		map.add(region19); map.add(region20); map.add(region21);
		map.add(region22); map.add(region23); map.add(region24);
		map.add(region25); map.add(region26); map.add(region27);
		map.add(region28); map.add(region29); map.add(region30);
		map.add(region31); map.add(region32); map.add(region33);
		map.add(region34); map.add(region35); map.add(region36);
		map.add(region37); map.add(region38); map.add(region39);
		map.add(region40); map.add(region41); map.add(region42);
		map.add(northAmerica);
		map.add(southAmerica);
		map.add(europe);
		map.add(afrika);
		map.add(azia);
		map.add(australia);

		return map;
	}

	//Make every region neutral with 2 armies to start with
	private Map setupMap(Map initMap)
	{
		Map map = initMap;
		for(Region region : map.regions)
		{
			region.setPlayerName("neutral");
			region.setArmies(2);
		}
		return map;
	}

	private void sendSetupMapInfo(Robot bot, Map initMap)
	{
		String setupSuperRegionsString, setupRegionsString, setupNeighborsString;
		setupSuperRegionsString = getSuperRegionsString(initMap);
		setupRegionsString = getRegionsString(initMap);
		setupNeighborsString = getNeighborsString(initMap);

		bot.writeInfo(setupSuperRegionsString);
		// System.out.println(setupSuperRegionsString);
		bot.writeInfo(setupRegionsString);
		// System.out.println(setupRegionsString);
		bot.writeInfo(setupNeighborsString);
		// System.out.println(setupNeighborsString);
	}

	private String getSuperRegionsString(Map map)
	{
		String superRegionsString = "setup_map super_regions";
		for(SuperRegion superRegion : map.superRegions)
		{
			int id = superRegion.getId();
			int reward = superRegion.getArmiesReward();
			superRegionsString = superRegionsString.concat(" " + id + " " + reward);
		}
		return superRegionsString;
	}

	private String getRegionsString(Map map)
	{
		String regionsString = "setup_map regions";
		for(Region region : map.regions)
		{
			int id = region.getId();
			int superRegionId = region.getSuperRegion().getId();
			regionsString = regionsString.concat(" " + id + " " + superRegionId);
		}
		return regionsString;
	}

	//beetje inefficiente methode, maar kan niet sneller wss
	private String getNeighborsString(Map map)
	{
		String neighborsString = "setup_map neighbors";
		ArrayList<Point> doneList = new ArrayList<Point>();
		for(Region region : map.regions)
		{
			int id = region.getId();
			String neighbors = "";
			for(Region neighbor : region.getNeighbors())
			{
				if(checkDoneList(doneList, id, neighbor.getId()))
				{
					neighbors = neighbors.concat("," + neighbor.getId());
					doneList.add(new Point(id,neighbor.getId()));
				}
			}
			if(neighbors.length() != 0)
			{
				neighbors = neighbors.replaceFirst(","," ");
				neighborsString = neighborsString.concat(" " + id + neighbors);
			}
		}
		return neighborsString;
	}

	private Boolean checkDoneList(ArrayList<Point> doneList, int regionId, int neighborId)
	{
		for(Point p : doneList)
			if((p.x == regionId && p.y == neighborId) || (p.x == neighborId && p.y == regionId))
				return false;
		return true;
	}

	private String getPlayedGame(Player winner, String gameView)
	{
		StringBuffer out = new StringBuffer();

		LinkedList<MoveResult> playedGame;
		if(gameView.equals("player1"))
			playedGame = player1PlayedGame;
		else if(gameView.equals("player2"))
			playedGame = player2PlayedGame;
		else
			playedGame = fullPlayedGame;

		playedGame.removeLast();
		int roundNr = 2;
		out.append("map " + playedGame.getFirst().getMap().getMapString() + "\n");
		out.append("round 1" + "\n");
		for(MoveResult moveResult : playedGame)
		{
			if(moveResult != null)
			{
				if(moveResult.getMove() != null)
				{
					try {
						PlaceArmiesMove plm = (PlaceArmiesMove) moveResult.getMove();
						out.append(plm.getString() + "\n");
					}
					catch(Exception e) {
						AttackTransferMove atm = (AttackTransferMove) moveResult.getMove();
						out.append(atm.getString() + "\n");
					}
				out.append("map " + moveResult.getMap().getMapString() + "\n");
				}
			}
			else
			{
				out.append("round " + roundNr + "\n");
				roundNr++;
			}
		}

		if(winner != null)
			out.append(winner.getName() + " won\n");
		if (winner.getName() == "Player 2") {
			//
		}
		else
			out.append("Nobody won\n");

		return out.toString();
	}

	private String compressGZip(String data, String outFile)
	{
		try {
			FileOutputStream fos = new FileOutputStream(outFile);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);

			byte[] outBytes = data.getBytes("UTF-8");
			gzos.write(outBytes, 0, outBytes.length);

			gzos.finish();
			gzos.close();

			return outFile;
		}
		catch(IOException e) {
			System.out.println(e);
			return "";
		}
	}


	public void saveGame(IORobot bot1, IORobot bot2) {

		Player winner = this.engine.winningPlayer();
		int score = this.engine.getRoundNr() - 1;
	}
}