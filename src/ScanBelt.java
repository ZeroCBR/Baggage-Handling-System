/**
 * The baggage scanner belt
 */
public class ScanBelt {

	// the items in the belt segments
	volatile protected Bag[] segment;

	// the length of this belt
	protected int beltLength = 2;

	// the animation to keep up to date
	protected Animation a;

	// the belt type (for including in exception messages)
	// protected String type;

	final private static String indentation = "                 ";

	/**
	 * Create a new, empty belt, initialised as empty
	 */
	public ScanBelt(Animation a) {
		segment = new Bag[beltLength];
		for (int i = 0; i < segment.length; i++) {
			segment[i] = null;
		}
		this.a = a;
	}

	/**
	 * Put a bag on the scanner belt.
	 * 
	 * @param bag
	 *            the bag to put onto the scanner belt.
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
		System.out.println(bag.getId() + " arrived scanner belt");

		// notify any waiting threads that the scanner belt has changed
		notifyAll();
	}

	/**
	 * Take a bag off the end of the scanner belt
	 * 
	 * @return the removed bag
	 * @throws InterruptedException
	 *             if the thread executing is interrupted
	 */
	public synchronized Bag getEndBelt() throws InterruptedException {
		Bag bag;
		if (segment[segment.length - 1] != null) {
			bag = segment[segment.length - 1];
			segment[segment.length - 1] = null;

			// make a note of the event
			System.out.print(indentation);
			System.out.println(bag.getId() + " departed on scanner belt");
			// notify any waiting threads that the scanner belt has changed
			notifyAll();
			a.animateMove(this);
			return bag;
		}
		notifyAll();
		return null;
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
		// do not move the belt

		while (isEmpty() || segment[segment.length - 1] != null) {
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
		// notify any waiting threads that the scanner belt has changed
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
