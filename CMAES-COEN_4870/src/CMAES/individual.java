package CMAES;

public class individual {

	double x,y; //Coordinates 
	double fitness = 1000; //Initial fitness
	int bestParent; //Which parent was more fit
	//Initial constructor
	public individual(double x2, double y2) {
		this.x = x2;
		this.y = y2;
	}
}
