public class ScanBeltMover extends BaggageHandlingThread {

    // the scanner belt to be handled
    protected ScanBelt scannerBelt;

    // the amount of time it takes to move the scanner belt
    protected final static int MOVE_TIME = 900;

    /**
     * Create a new ScannerBeltMover with a scanner belt to move
     */
    public ScanBeltMover(ScanBelt scannerBelt) {
        super();
        this.scannerBelt = scannerBelt;
    }

    /**
     * Move the scanner belt as often as possible, but only if there 
     * is a bag on the belt which is not in the last position.
     */
    public void run() {
        while (!isInterrupted()) {
            try {
                scannerBelt.move();
                // spend MOVE_TIME milliseconds moving the belt            	
                Thread.sleep(MOVE_TIME);
            } catch (OverloadException e) {
                terminate(e);
            } catch (InterruptedException e) {
                this.interrupt();
            }        	
        }

        System.out.println("ScannerBeltMover terminated");
    }
}
