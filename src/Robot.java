/**
 * A robot carries bag from belt to scanner
 */
public class Robot extends Thread {

	// the belt to which the producer puts the bags
	protected Bag bag;
	// the status of robot
	protected boolean busy;
	// the belt object
	protected Belt belt;
	// the scanner object
	protected Scanner scanner;
	// robot moving time
	protected final static int MOVE_TIME = 450;

	/**
	 * Create a new robot
	 */
	Robot(Belt belt, Scanner scanner) {
		this.busy = false;
		this.bag = null;
		this.belt = belt;
		this.scanner = scanner;
	}

	/**
	 * take a suspicious bag on the segment 2 of belt and move to scanner
	 */
	public synchronized void takeBagFromBelt() throws InterruptedException {
		busy = true;
		System.out.println(bag.id + " has been taken from belt");
		sleep(MOVE_TIME);// the time that moves to scanner
	}

	/**
	 * put a suspicious bag in the scanner
	 */
	public synchronized void putBagInScanner() throws InterruptedException {
		// while there is no bag for robot, the bag is clean, scanner is busy
		// with a bag, block this thread
		while (bag == null || bag.clean == true || scanner.bag != null
				|| scanner.busy) {
			wait();
		}
		scanner.bag = (Bag) bag.clone();// put bag into scanner
		bag = null;// release robot's bag
		sleep(MOVE_TIME);// move back to belt
		System.out.println(scanner.bag.id + " has been put in scanner");
		scanner.scan();// let the scanner scan bag
		busy = false;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				// robot monitor its bag, while its bag is not clean then put it
				// in scanner
				if (bag != null && bag.clean != true) {
					takeBagFromBelt();
					putBagInScanner();
				}
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}

		System.out.println("Robot terminated");
	}
}
