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

package bot;

import main.Region;
import move.AttackTransferMove;
import move.PlaceArmiesMove;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import main.RunGame;

public class DNA_DRAGON implements Bot
{
	static double attackingRatio = 0; //Armies to attack or defend
	static double removeRatio = 0; //Armies to abandon a region
	static double reinforceRatio = 0; //Armies to reinforce a region
    static double aggression = 0; //Aggression of a bot between 0 and 1
	static String swapLine; //String to hold parameters read from swap file
	static File swap = new File("swap.txt"); //Create a new file for information to be swapped around between packages
	static File botDebug;
	static FileWriter botDebugWriter;
	static String idString; //Individual number
	static String genString; //Generation number
	static double[] regionPref= new double[42]; //Array of region preferences
	static double[] cloneRegionPref; //Clone of region preferences for starting region search
	boolean aggFlag1 = false; //Has aggression increased check
	boolean aggFlag2 = false; //Has aggression increased check
	boolean aggFlag3 = false; //Has aggression increased check
	boolean aggFlag4 = false; //Has aggression increased check
	boolean aggFlag5 = false; //Has aggression increased check

	@Override
	/**
	 * A method used at the start of the game to decide which player start with what Regions. 6 Regions are required to be returned.
	 * Regions are selected based on provided preferred regions
	 * @return : a list of m (m=6) Regions starting with the most preferred Region and ending with the least preferred Region to start with 
	 */
	public ArrayList<Region> getPreferredStartingRegions(BotState state, Long timeOut) {
		int highRegionPref = 0; //Highest region preference found
		int highRegionInd = -1; //Highest region preference index found
		cloneRegionPref = regionPref; //Clone the region preferences
		ArrayList<Region> preferredStartingRegions = new ArrayList<Region>();

		for(int i=0; i<6; i++) { //For the number of regions that need to be chosen as preferences
			for (int j = 0; j < 42; j++) { //For all of the regions that need to be considered
				if (cloneRegionPref[j] > highRegionPref) { //If a more preferential region has been found
					highRegionInd = j; //Save the region's index
				}
			}

			Region region = state.getFullMap().getRegion(highRegionInd+1); //Retrieve the region selected
			preferredStartingRegions.add(region); //Add the region to the array of top 6 preferred regions
			try {
				botDebugWriter.write("REGION: " + highRegionInd+1 + " has the value " + regionPref[highRegionInd] + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			cloneRegionPref[highRegionInd] = 0; //Remove the selected region from contention
			highRegionPref = 0; //Reset the highest region preference found
		}

		return preferredStartingRegions; //Return the top 6 preferences to the main game engine
	}

	@Override
	/**
	 * This method is called for at first part of each round.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut) throws IOException {

		botDebugWriter = new FileWriter(botDebug,true); //Initialize the botDebug writer, append to existing
		if (state.getRoundNumber() == 1) {
			botDebugWriter.write("Player Name: " + state.getMyPlayerName() + "\n");
		}
		botDebugWriter.write("Round #: " + state.getRoundNumber() + "\n");
		botDebugWriter.write("First Stage\n");

		ArrayList<PlaceArmiesMove> placeArmiesMoves = new ArrayList<PlaceArmiesMove>();
		String myName = state.getMyPlayerName();

		int armies = state.getStartingArmies(); //Number of armies to move this turn
		int armiesLeft = 2;
		LinkedList<Region> visibleRegions = state.getVisibleMap().getRegions();

		while(armiesLeft > 0) //While this bot still has armies remaining
		{
			double rand = Math.random();
			int r = (int) (rand*visibleRegions.size()); //Choose a random region
			Region region = visibleRegions.get(r); //Select the region
			
			if(region.ownedByPlayer(myName)) //If the visible regions are owned by this bot
			{
				placeArmiesMoves.add(new PlaceArmiesMove(myName, region, armies)); //
				armiesLeft -= armies; //Decrement count of available movable armies
			}
		}
		botDebugWriter.close(); //Close the botDebug writer
		return placeArmiesMoves;
	}

	@Override
	/**
	 * This method is called for at the second part of each round.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public ArrayList<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut) throws IOException {

		botDebugWriter = new FileWriter(botDebug,true); //Initialize the botDebug writer, append to existing
		botDebugWriter.write("Second Stage\n");

		if ((state.getRoundNumber() > 20) && !aggFlag1) { //If the round number has passed 20 and this has not yet been performed
			aggression = aggression + 0.5; //Increase the aggression level
			aggFlag1 = true; //Signal not to perform this check next turn
		}
		if ((state.getRoundNumber() > 40) && !aggFlag2) { //If the round number has passed 20 and this has not yet been performed
			aggression = aggression + 0.5; //Increase the aggression level
			aggFlag2 = true; //Signal not to perform this check next turn
		}
		if ((state.getRoundNumber() > 60) && !aggFlag3) { //If the round number has passed 20 and this has not yet been performed
			aggression = aggression + 0.5; //Increase the aggression level
			aggFlag3 = true; //Signal not to perform this check next turn
		}
		if ((state.getRoundNumber() > 80) && !aggFlag4) { //If the round number has passed 20 and this has not yet been performed
			aggression = aggression + 0.5; //Increase the aggression level
			aggFlag4 = true; //Signal not to perform this check next turn
		}
		if ((state.getRoundNumber() > 90) && !aggFlag5) { //If the round number has passed 20 and this has not yet been performed
			aggression = aggression + 0.5; //Increase the aggression level
			aggFlag5 = true; //Signal not to perform this check next turn
		}

		if(aggression > .6) {
			botDebugWriter.write("Playing Extra Risky\n");
		}
		else if(aggression > .5 && aggression < .6) {
			botDebugWriter.write("Playing Risky\n");
		}
		else if(aggression > .45 && aggression < .5) {
			botDebugWriter.write("Playing Safe\n");
		}
		else if(aggression > .4 && aggression < .45) {
			botDebugWriter.write("Playing Extra Safe\n");
		}
		else {
			botDebugWriter.write("Playing Neutral\n");
		}


		ArrayList<AttackTransferMove> attackTransferMoves = new ArrayList<AttackTransferMove>();
		String myName = state.getMyPlayerName();
		int armies = 5;

		for (Region fromRegion : state.getVisibleMap().getRegions()) //For each of the regions that is visible to the player
		{
			if (fromRegion.ownedByPlayer(myName)) //if
			{
				ArrayList<Region> possibleToRegions = new ArrayList<Region>();
				possibleToRegions.addAll(fromRegion.getNeighbors());

				while (!possibleToRegions.isEmpty())  //while there are still regions that I can attack
				{
					double rand = Math.random();
					int r = (int) (rand * possibleToRegions.size());
					Region toRegion = possibleToRegions.get(r);
                    //There are five possible modes: extra risky, risky, neutral, safe, and extra safe
                    //extra risky, translation: high aggression, remove, reinforce and attacking ratio
                    if(aggression > .6){
                        if (!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 2) //if there is a region that is not mine that i can move to with a certain number of armies
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies)); //Then attack that region with the avaliable armies.
                            break;
                        } else if (toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
                            break;
                        } else
                            possibleToRegions.remove(toRegion);
                        
                    }//end of extra risky
                    //risky: moderate aggression, remove, reinforce and attacking ratio
                    else if(aggression > .5 && aggression < .6){
                        if (!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 4) //if there is a region that is not mine that i can move to with a certain number of armies
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies)); //Then attack that region with the avaliable armies.
                            break;
                        } else if (toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
                            break;
                        } else
                            possibleToRegions.remove(toRegion);
                    
                    }
                    else if(aggression > .45 && aggression < .5){
                        if (!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 8) //if there is a region that is not mine that i can move to with a certain number of armies
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies)); //Then attack that region with the avaliable armies.
                            break;
                        } else if (toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
                            break;
                        } else
                            possibleToRegions.remove(toRegion);
                        
                    }
                    else if(aggression > .4 && aggression < .45){
                        if (!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 10) //if there is a region that is not mine that i can move to with a certain number of armies
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies)); //Then attack that region with the avaliable armies.
                            break;
                        } else if (toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
                            break;
                        } else
                            possibleToRegions.remove(toRegion);
                        
                    }
                    //neutral
                    else{
                        if (!toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 6) //if there is a region that is not mine that i can move to with a certain number of armies
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies)); //Then attack that region with the avaliable armies.
                            break;
                        } else if (toRegion.getPlayerName().equals(myName) && fromRegion.getArmies() > 1) //do a transfer
                        {
                            attackTransferMoves.add(new AttackTransferMove(myName, fromRegion, toRegion, armies));
                            break;
                        } else
                            possibleToRegions.remove(toRegion);
                        
                    }
                    
                }
				}
			}
		
		botDebugWriter.close(); //Close the botDebug writer
		return attackTransferMoves;
	}

	public static void main(String[] args) throws IOException {
		int regionCounter = 0; //Counter for the region array index
		Scanner swapScanner = new Scanner(swap); //Create a new file scanner

		while (swapScanner.hasNext()) { //For the length of the file
			swapLine = swapScanner.next(); //Read in the next line
			if (swapLine.equals("generation:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				genString = swapLine; //Store the id for creating a debug file
			}
			if (swapLine.equals("individual:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				idString = swapLine; //Store the id for creating a debug file
				botDebug = new File("botDebugGen" + genString + "Ind" + idString + ".txt"); //Create a new file for debug information storage
				if(botDebug.delete()){ //Delete the existing botDebug file, if there is one
					botDebug.createNewFile(); //Create a new botDebug file
				}
				botDebugWriter = new FileWriter(botDebug); //Initialize the botDebug writer
				botDebugWriter.write("DNA_DRAGON DEBUG INITIALIZED\n");
			}
			if (swapLine.equals("attackingRatio:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				attackingRatio = Double.parseDouble(swapLine); //Save the parameter locally
				botDebugWriter.write("ATTACKING RATIO: " + attackingRatio + "\n");
			}
			if (swapLine.equals("removeRatio:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				removeRatio = Double.parseDouble(swapLine); //Save the parameter locally
				botDebugWriter.write("REMOVE RATIO: " + removeRatio + "\n");
			}
			if (swapLine.equals("reinforceRatio:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				reinforceRatio = Double.parseDouble(swapLine); //Save the parameter locally
				botDebugWriter.write("REINFORCE RATIO: " + reinforceRatio + "\n");
			}
			if (swapLine.equals("aggression:")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				aggression = Double.parseDouble(swapLine); //Save the parameter locally
				botDebugWriter.write("AGGRESSION: " + aggression + "\n");

			}
			if (swapLine.equals("startingregion")) { //Upon finding a particular parameter
				swapLine = swapScanner.next(); //Read in the result
				swapLine = swapScanner.next(); //Read in the result
				regionPref[regionCounter] = Double.parseDouble(swapLine); //Parse the result into the parameter array
				//botDebugWriter.write("REGION: " + regionCounter + " has the value " + regionPref[regionCounter] + "\n");
				regionCounter++; //increment the array index for starting regions
			}
		}
		swapScanner.close(); //Close the scanner
		botDebugWriter.write("PARAMETER READ SUCCESSFUL\n");

		botDebugWriter.close();

		BotParser parser = new BotParser(new DNA_DRAGON());
		parser.run();
	}

}
