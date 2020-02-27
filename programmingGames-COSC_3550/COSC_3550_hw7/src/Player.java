import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class Player {
	// A simple class to keep track of each player
	// (this class is used by Server and Client)
	int x, y;
	int dir;
	Color color;
	int grab; //Is the player attempting to grab; 1 true; 0 false
	int blast; //Is the player attempting to blase; 1 true; 0 false
	int score; //What is the player's current score
	int sticks = 4; //How many explosives does the player have remaining
	
	// Directions
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	Player(int x1, int y1, int d, Color c, int grab1, int blast1) {
		x = x1;
		y = y1;
		dir = d;
		color = c;
		grab = grab1;
		blast = blast1;
	}

	public void turnLeft() {
		dir--;
		if (dir < UP)
			dir = LEFT;
	}

	public void turnRight() {
		dir++;
		if (dir > LEFT)
			dir = UP;
	}
	
	public void blast() {
		System.out.println("BLAST METHOD CALLED");
	}
	public void grab() {
		System.out.println("GRAB METHOD CALLED");
		
	}

	public void render(GraphicsContext gc) {
		int px = Grab.CELLSIZE * x;
		int py = Grab.CELLSIZE * y;
		gc.setFill(color);
		gc.fillOval(px, py, Grab.CELLSIZE - 1, Grab.CELLSIZE - 1);
		gc.setFill(Color.BLACK);
		switch (dir) {
		case UP:
			gc.fillOval(px + Grab.CELLSIZE / 4, py, Grab.CELLSIZE / 2, Grab.CELLSIZE / 2);
			break;
		case RIGHT:
			gc.fillOval(px + Grab.CELLSIZE / 2, py + Grab.CELLSIZE / 4, Grab.CELLSIZE / 2, Grab.CELLSIZE / 2);
			break;
		case DOWN:
			gc.fillOval(px + Grab.CELLSIZE / 4, py + Grab.CELLSIZE / 2, Grab.CELLSIZE / 2, Grab.CELLSIZE / 2);
			break;
		case LEFT:
			gc.fillOval(px, py + Grab.CELLSIZE / 4, Grab.CELLSIZE / 2, Grab.CELLSIZE / 2);
			break;
		}
	}
}
