/**
 * The baggage belt
 */
public class Belt {

	// the items in the belt segments
	protected Bag[] segment;

	// the length of this belt
	protected int beltLength = 5;

	// the animation to keep up to date
	protected Animation a;

	// the status of belt
	public static boolean run = true;

	// the belt type (for including in exception messages)
	// protected String type;

	final private static String indentation = "                 ";

	/**
	 * Create a new, empty belt, initialised as empty
	 */
	public Belt(Animation a) {
		segment = new Bag[beltLength];
		for (int i = 0; i < segment.length; i++) {
			segment[i] = null;
		}
		this.a = a;
	}

	/**
	 * Put a bag on the belt.
	 * 
	 * @param bag
	 *            the bag to put onto the belt.
	 * @param index
	 *            the place to put the bag
	 * @throws InterruptedException
	 *             if the thread executing is interrupted.
	 */
	public synchronized void put(Bag bag, int index)
			throws InterruptedException {
		// while there is another bag in the way, block this thread
		while (segment[index] != null) {
			wait();
		}

		// insert the element at the specified location
		segment[index] = bag;

		// make a note of the event
		if (bag.isSuspicious()) {
			System.out.println(bag.getId() + " arrived (sus)");
		} else {
			System.out.println(bag.getId() + " arrived");
		}

		// notify any waiting threads that the belt has changed
		notifyAll();

	}

	/**
	 * Take a bag off the end of the belt
	 * 
	 * @return the removed bag
	 * @throws InterruptedException
	 *             if the thread executing is interrupted
	 */
	public synchronized Bag getEndBelt() throws InterruptedException {
		Bag bag;
		// get the next item
		bag = segment[segment.length - 1];
		segment[segment.length - 1] = null;

		// make a note of the event
		System.out.print(indentation);
		if (!bag.isClean()) {
			System.out.println(bag.getId() + " departed -- unclean!!!");
		} else {
			System.out.println(bag.getId() + " departed");
		}

		// notify any waiting threads that the belt has changed
		notifyAll();
		return bag;
	}

	/**
	 * Move the belt along one segment
	 * 
	 * @throws OverloadException
	 *             if there is a bag at position beltLength.
	 * @throws InterruptedException
	 *             if the thread executing is interrupted.
	 */
	public synchronized void move() throws InterruptedException,
			OverloadException {
		// if there is something at the end of the belt, or the belt is empty,
		// or something needs to be picked up for a scan, do not move the belt

		while (isEmpty() || segment[segment.length - 1] != null || !run) {
			wait();
		}

		// double check that a bag cannot fall of the end
		if (segment[segment.length - 1] != null) {
			String message = "Bag fell off end of " + " belt";
			throw new OverloadException(message);
		}

		// move the elements along, making position 0 null
		for (int i = segment.length - 1; i > 0; i--) {
			segment[i] = segment[i - 1];
		}
		segment[0] = null;
		a.animateMove(this);

		// notify any waiting threads that the belt has changed
		notifyAll();

	}

	/**
	 * sense the suspicious bag in the belt
	 * 
	 * @throws InterruptedException
	 *             if the thread executing is interrupted.
	 */
	public synchronized void sense(Robot robot) throws InterruptedException {
		// if there is nothing at the middle of the belt, or the bag is not
		// suspicious, or the bag is clean, do not run the sensor
		while (segment[2] == null
				|| (segment[2] != null && segment[2].suspicious == false)
				|| segment[2].clean) {
			wait();
		}
		System.out.println("susBag " + segment[2].id + " sense");
		// check whether the robot is busy
		if (!robot.busy) {
			robot.bag = (Bag) segment[2].clone();// robot takes the suspicious
													// bag
			segment[2] = null;// remove the suspicious bag on belt
			run = true;
		} else {
			run = false;
		}
		// notify any waiting threads that the belt has changed
		notifyAll();
	}

	/**
	 * @return the maximum size of this belt
	 */
	public int length() {
		return beltLength;
	}

	/**
	 * Peek at what is at a specified segment
	 * 
	 * @param index
	 *            the index at which to peek
	 * @return the bag in the segment (or null if the segment is empty)
	 */
	public Bag peek(int index) {
		Bag result = null;
		if (index >= 0 && index < beltLength) {
			result = segment[index];
		}
		return result;
	}

	// check whether the belt is currently empty
	private boolean isEmpty() {
		for (int i = 0; i < segment.length; i++) {
			if (segment[i] != null) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return java.util.Arrays.toString(segment);
	}

	/*
	 * @return the final position on the belt
	 */
	public int getEndPos() {
		return beltLength - 1;
	}
}
