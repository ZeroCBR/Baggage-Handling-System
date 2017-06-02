/**
 * A scanner scan the suspicious bag on segment 2 of belt
 */
public class Scanner extends Thread {
	// time of scanning a bag
	protected final static int SCAN_TIME = 3000;
	// the belt to which the producer puts the bags
	protected Bag bag;
	// the status of scanner
	protected Boolean busy;
	// the animation to keep up to date
	protected Animation a;
	// the scanner belt
	protected ScanBelt scannerBelt;

	/**
	 * Create a new scanner
	 */
	Scanner(Animation a, ScanBelt scannerBelt) {
		this.bag = null;
		this.busy = false;
		this.a = a;
		this.scannerBelt = scannerBelt;
	}

	/**
	 * The thread's main method. Continually tries to scan the suspicious bag
	 */
	public void run() {
		while (!isInterrupted()) {
			try {
				scan();
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
		System.out.println("Scanner terminated");
	}

	/**
	 * scan the suspicious bag in the scanner
	 */
	public synchronized void scan() throws InterruptedException {
		// while there is another bag in the scanner, block this thread
		while (bag == null || busy) {
			wait();
		}
		int id = bag.id;
		Animation.DrawSuspicious.showSuspicious(String.valueOf(id));// draw a
																	// suspicious
																	// bag in
																	// scanner
		busy = true;
		sleep(SCAN_TIME);// scan the bag
		bag.clean();
		System.out.println(bag.id + " has been cleaned");
		scannerBelt.put((Bag) bag.clone(), 0);// pass the bag to scanner belt
		bag = null;
		busy = false;
		Animation.DrawSuspicious.removeSuspicious();// remove the suspicious bag
													// in scanner

	}
}