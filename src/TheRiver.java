import java.util.Random;
import java.util.concurrent.Semaphore;

public class TheRiver {

	private static int racersInRiver = 0;
	
	/*Semaphore mutex is used to increment the variable racersInRiver to prevent concurrency problems.
	 * The semaphore group is used for the racer to wait for signal from judge that a full group is formed and they are ready to proceed.
	 * */
	private static Semaphore mutex = new Semaphore(1);
	public static Semaphore group = new Semaphore(0);
	private static Random riverWait = new Random();
	
	
	public static void main(Racer racer) throws InterruptedException {
		mutex.acquire();                        //Wait for mutex
		racersInRiver++;	                    //Increment river
		System.out.println("Racer-" + racer.getName() + " is inside the river.");
		//Just like story problem done in class, if not enough racers to form group or not the last number of racer to enter, Wait for group signal.
		if(racersInRiver % Racer.numLines != 0 && racersInRiver != Racer.numRacers) {
			System.out.println("Racer-" + racer.getName() + " waiting to be grouped.");
			mutex.release();
			group.acquire();
			Thread.sleep(riverWait.nextInt(5000) + 1000);
			System.out.println("Racer-" + racer.getName() + " leaves the river.");
		}else {
			//Group number has been met, signal judge that a group is ready
			mutex.release();
			TheJudge.judgeSemaphore.release();
			Thread.sleep(riverWait.nextInt(5000) + 1000);
			System.out.println("Racer-" + racer.getName() + " leaves the river.");
		}
	}

}
