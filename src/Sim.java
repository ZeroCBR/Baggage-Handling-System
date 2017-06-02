/**
 * The driver of the simulation
 */

public class Sim {
	/**
	 * Create all components and start all of the threads
	 */
	public static void main(String[] args) {

		Animation a = new Animation();
		Belt belt = new Belt(a);
		ScanBelt scannerBelt = new ScanBelt(a);
		Producer producer = new Producer(belt);
		Consumer consumer = new Consumer(belt, scannerBelt);
		BeltMover mover = new BeltMover(belt);
		ScanBeltMover scannerBeltMover = new ScanBeltMover(scannerBelt);
		Scanner scanner = new Scanner(a, scannerBelt);
		Robot robot = new Robot(belt, scanner);
		Sensor sensor = new Sensor(belt, robot);

		consumer.start();
		producer.start();
		mover.start();
		sensor.start();
		scanner.start();
		robot.start();
		scannerBeltMover.start();

		while (consumer.isAlive() && producer.isAlive() && mover.isAlive()
				&& sensor.isAlive() && scanner.isAlive() && robot.isAlive()
				&& scannerBeltMover.isAlive())
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				BaggageHandlingThread.terminate(e);
			}

		// interrupt other threads
		consumer.interrupt();
		producer.interrupt();
		mover.interrupt();
		sensor.interrupt();
		scanner.interrupt();
		robot.interrupt();
		scannerBeltMover.interrupt();
		System.out.println("Sim terminating");
		System.out.println(BaggageHandlingThread.getTerminateException());
		System.exit(0);
	}
}
