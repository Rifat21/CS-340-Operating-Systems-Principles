import java.util.Random;
import java.util.concurrent.Semaphore;

public class TheMountain {
	
	/*Semaphore mountain is a Mutex used to provide mutual exclusion to the mountain. 
	 * A racer thread cannot enter the mountain unless it has access to the mountain semaphore.
	 * A racer thread that gets access first goes into mountain and only releases when its done.*/
	private static Semaphore mountain = new Semaphore(1);
    private static Random mountainWait = new Random();

	public static void main(Racer racer) {
		try {
			mountain.acquire();             //Wait for mutex(mountain)
			System.out.println("Racer-" + racer.getName() + " Enters Mountain");
			Thread.sleep(mountainWait.nextInt(5000) + 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Racer-" + racer.getName() + " leaves Mountain");
		mountain.release();                //Finished with mountain and signal mutex(mountain).

	}

}
