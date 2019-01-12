import java.util.concurrent.Semaphore;

public class TheJudge extends Thread {
	//JudgeSemaphore is used to block the judge until it is signaled when a group is formed.
	public static Semaphore judgeSemaphore = new Semaphore(0);
	//JudgeCanGoHome is used to block the judge from going home and is Signaled by Racer-1 once all the racers have gone home. 
	public static Semaphore judgeCanGoHome = new Semaphore(0);

	public static int judgeCheck;
	public static Semaphore reports = new Semaphore(0);
	
	
	public void run() {
		while(judgeCheck > 0) {
			try {
				//Once a judge is signaled, the judge signals all the racers that were waiting to be grouped. 
				judgeSemaphore.acquire();
				for(int i = 0; i < Racer.numLines-1; i++) {
					TheRiver.group.release();
					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			judgeCheck--;
		}
		//Judge waits for all racers to finish the race
		for(int i = 0; i< Racer.numRacers; i++) {
			try {
				Racer.raceDone.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Judge prints Report 1
		System.out.println("");
		System.out.println("-------------------------------------------------------");
		System.out.println("Report 1: Total duration of entire race for each racer |");
		System.out.println("-------------------------------------------------------");
		System.out.println("");
		for(int i=0; i<Racer.raceResults.size(); i++) {
			System.out.println("Racer-" + Racer.raceResults.get(i).getName() + " came in position "+ (i+1) + " and took " + Racer.raceResults.get(i).raceDuration + " milliseconds to complete the entire race.");
			System.out.println("");
		}
		
		//Judge prints Report 2
		System.out.println("------------------------------------");
		System.out.println("Report 2: Each racers obstacle time |");
		System.out.println("------------------------------------ ");
		for(int i=0; i<Racer.raceResults.size(); i++) {
			System.out.println("Racer-" + Racer.raceResults.get(i).getName() + " took " + Racer.raceResults.get(i).forestTime + " nanoseconds to complete forest.");
			System.out.println("Racer-" + Racer.raceResults.get(i).getName() + " took " + Racer.raceResults.get(i).mountainTime + " milliseconds to complete mountain.");
			System.out.println("Racer-" + Racer.raceResults.get(i).getName() + " took " + Racer.raceResults.get(i).riverTime + " milliseconds to complete river.");
			System.out.println("");
		}
		//Judge signals all the racers that reports have been posted.
		for(int i = 0; i< Racer.numRacers; i++) {
			reports.release();
		}
		
		//Signals the last racer that he can go home. 
		
		Racer.signalHome.get(Racer.numRacers-1).release();
		
		//Blocked and waits for signal from Racer-1 that it is okay to go home
		try {
			judgeCanGoHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("The judge goes home.");
	}

	public static void main(String args[]) {
		
	}

}
