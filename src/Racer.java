import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class Racer extends Thread{
	
	//Random object used create random sleep time. 
	private static Random wait = new Random();                              
	//Semaphore that is used to signal the judge that all the racers have finished their race. 
	public static Semaphore raceDone = new Semaphore(0);         
	public long raceStart,raceEnd,startTime,endTime;
	public long forestTime;
	public long mountainTime;
	public long riverTime;
	public long raceDuration;
	public static int numRacers, numLines;
	public static List<Racer> raceResults = new ArrayList<Racer>();          //ArrayList of Racers to be stored with their times.
	public static List<Semaphore> signalHome = new ArrayList<Semaphore>();   //ArrayList of semaphores used to signal the  

	
	public void run() {
		raceStart = System.currentTimeMillis();
		
		try {

			Thread.sleep(wait.nextInt(5000) + 1000); 		//Sleep for a random time of 1-6 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startTime = System.nanoTime();
		TheForest.Forest(this);									//Enters forest
		endTime = System.nanoTime();
		forestTime = (endTime-startTime);

		
		try {
			Thread.sleep(wait.nextInt(5000) + 1000);			//Sleep for a random time of 1-6 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startTime = System.currentTimeMillis();
		TheMountain.main(this);									//Enters mountain
		endTime = System.currentTimeMillis();
		mountainTime = (endTime-startTime);
		
		try {
			Thread.sleep(wait.nextInt(5000) + 1000); 		    //Sleep for a random time of 1-6 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startTime = System.currentTimeMillis();
		try {
			TheRiver.main(this);                               //Enters River
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		riverTime = (endTime-startTime);
		raceEnd = System.currentTimeMillis();                          //Finishes Race
		raceDuration = (raceEnd-raceStart);
		raceResults.add(this);
		raceDone.release();                                           //Signals judge that thread finished race.
		try {
			TheJudge.reports.acquire();                      // Waits for Judge to release the reports before proceeding and going home.
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*If its the first racer meaning the last race to leave: Wait for signal from the second racer. Tell Judge by signaling
		semaphore judgeCanGoHome that all the racers have gone home and the Judge can go home now.*/
		if(Integer.parseInt(this.getName()) == 1) {
			try {
				signalHome.get(Integer.parseInt(this.getName()) -1).acquire();  //Waits for signal from racer 2
			} catch (NumberFormatException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Racer-" + this.getName() + " goes home.");     
			TheJudge.judgeCanGoHome.release();                                  //Signal judge it's okay to go home and go home. 
		}else {

			try {
				signalHome.get(Integer.parseInt(this.getName()) -1).acquire();      //Wait for signal from the racer in front.
			} catch (NumberFormatException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Racer-" + this.getName() + " goes home.");          //Signal the next racer that its okay to go home and go home. 
			signalHome.get(Integer.parseInt(this.getName()) -2).release();
		}
			
	}
	


	
	public static void main(String args[]){

		TheForest.main(args);                        //Initialize the ArrayList of random words with random size.
		TheJudge judge = new TheJudge();             //Instantiate the judge
		Scanner input = new Scanner(System.in);
		System.out.println("How many racers in this race: ");
		numRacers = input.nextInt();
		System.out.println("How many racers can cross the river at once(numLines): ");
		numLines = input.nextInt();
		//JudgeCheck decides how many times the Judge checks to see if group is formed. 
		if(numRacers % numLines == 0) {
			TheJudge.judgeCheck = numRacers/numLines;
		}else {
			TheJudge.judgeCheck = (numRacers/numLines + 1);
		}
		judge.start();                                              //Start the Judge thread.
		
		for(int i = 0; i<numRacers; i++) {
			Semaphore signal = new Semaphore(0);                    //Create semaphores that will be used to signal when to go home.
			signalHome.add(signal);                                 //Add the semaphores to the ArrayList for ease of signaling. 
			Racer temp = new Racer();                               //Create Racers change their name and start each racer thread. 
			temp.setName("" +(i+1));
			temp.start();
		}
		
	}



}
