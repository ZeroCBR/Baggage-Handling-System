/**
 * A consumer continually tries to take bags from the end of a belt
 */

public class Consumer extends BaggageHandlingThread {
	// the belt from which the consumer takes the bags
	protected Belt belt;
	// the belt from which the consumer takes the clean bags
	protected ScanBelt scannerBelt;
	// get a bag and have rest
	protected final static int REST_TIME = 500;

	/**
	 * Create a new Consumer that consumes from a belt
	 */
	public Consumer(Belt belt, ScanBelt scannerBelt) {
		super();
		this.belt = belt;
		this.scannerBelt = scannerBelt;
	}

	/**
	 * Loop indefinitely trying to get bags from the baggage belt
	 */
	public void run() {
		while (!isInterrupted()) {
			try {
				if (belt.segment[belt.segment.length - 1] != null) {
					Bag bag = belt.getEndBelt();// get the bag from belt
				}
				if (scannerBelt.segment[scannerBelt.segment.length - 1] != null) {
					Bag bag1 = scannerBelt.getEndBelt();// get the bag from
														// scanner
														// belt
				}
				sleep(REST_TIME);
				// let some time pass ...
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}

		System.out.println("Consumer terminated");
	}
}
