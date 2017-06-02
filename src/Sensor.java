/**
 * a sensor sense the suspicious bag on segment 2 of belt
 */
public class Sensor extends Thread{
	
	protected final static int MOVE_TIME = 900;

	// the belt to which the producer puts the bags
	protected Belt belt;
	// the robot object
	protected Robot robot;

	/**
	 * Create a new producer to feed a given belt
	 */
	Sensor(Belt belt, Robot robot) {
		super();
		this.belt = belt;
		this.robot = robot;
	}
	
	/**
	 * The thread's main method. Continually tries to sense the suspicious bag
	 */
	public void run() {
		while (!isInterrupted()) {
			try {
				belt.sense(robot);
				sleep(MOVE_TIME);
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}

		 System.out.println("Sensor terminated");
	}


}