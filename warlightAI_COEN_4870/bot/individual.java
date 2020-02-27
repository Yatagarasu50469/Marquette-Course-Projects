package bot;

public class individual {
    int ID = 0;
    Object info;
    public double aggression; //higher than .5 if currently close to the end of the game or many super regions controlled
                            //lower than .5 if still at the beginning of the game
   boolean [] superRegionControl;
    int roundNum;
    double attackingRatio, removeRatio, reinforceRatio;
    double[] regionPref= new double[42]; //Array of region preferences
    int numParam = 5; //Number of parameters for each individual
    double fitness = 100.0; //Initialize a fitness value
    String status;

    public individual() { //Constructor for an individual

    }

    public int getID() {
        return ID;
    }

    //SETTERS
    public void setFitness(double fit) { //Set the fitness for this individual
        fitness = fit; //Store the fitness
    }

    public void setReinforceRatio(double reinRatio) {
        reinforceRatio = reinRatio;
    }

    public void setAggression(double aggress) {
        aggression = aggress;
    }

    public void setAttackingRatio(double attackRatio) {
        attackingRatio = attackRatio;
    }

    public void setRemoveRatio(double remRatio) {
        removeRatio = remRatio;
    }

    public void setStartingRegions(double[] startRegions) {
        regionPref = startRegions;
    }

    public void setStatus (String s) { status = s; }

    //GETTERS
    public double getFitness() {
        return fitness;
    }

    public double getReinforceRatio() {
        return reinforceRatio;
    }

    public double getAggression() {
        return aggression;
    }

    public double getAttackingRatio() {
        return attackingRatio;
    }

    public double getRemoveRatio() {
        return removeRatio;
    }

    public double[] getStartingRegions() {
        return regionPref;
    }

    public String getStatus() { return status; }

    public void setID(int i) {
        this.ID = i;
    }




    /* TO BE MOVED TO DNA_DRAGON
        //double a, boolean [] src, int r, double [] pr, double at
        //aggression = a;
        //superRegionControl = src;
        //roundNum = r;
        //preferredRegions = pr;
        //attackingRatio = at;
        //runSequence();	//once all set up, figure up how to adjust the aggression,
        				//attackingRatio and preferredRegion values based on the status of the game play
    }
    public void runSequence(){
    	setAggression();
    	setAttackingRatio();
    	
    }


    public void setAggression(){
        int j=0;
        for(int i = 0; i < superRegionControl.length; i++){
            if(superRegionControl[i]){
                j++;
            }else{
                j--;
            }
        }
        j += roundNum;  //means the more rounds played and the more super regions controlled, the
                        //more aggressive the player will play
                        //this method heavily weights the rounds played as the largest factor, meaning
                        //as the game continues, it will become increasingly more aggressive
        aggression = j/100;
        if(aggression > 1){
            aggression = 1;//maxes out at the most aggressive
        }
    }

    public void setAttackingRatio(){
    	aggression -= .5;//this way will still be positive if above .5, or negative if less than .5;
    	aggression += 1;//now will be over 1, therefore increasing attacking ratio if high
    					//or less than 1, therefore decreasing attacking ratio if low
    	attackingRatio = attackingRatio * aggression;
    }
*/

}
