import java.util.ArrayList;
import java.util.Random;

public class TheForest {
	
	
	private static Random random = new Random();
	private static final int randomNumber = random.nextInt(200) + 300;
	//Strings used to create random words
	public static String[] words = {"a", "b", "c", "d"};
	private static ArrayList<String> map = new ArrayList<String>(randomNumber);
	
	


	public static void main(String args[]) {
		//String created with random chars combined and added to the ArrayList Map. 
		for(int i =0; i<= randomNumber; i++) {
			String x = words[random.nextInt(3)] + words[random.nextInt(3)] + words[random.nextInt(3)] + words[random.nextInt(3)];
			map.add(x);
			}		
		}
		
	public static void Forest(Racer racer) {
		System.out.println("Racer-" + racer.getName() + " Enters forest");
		//Sets magicWord to a random word comprised of strings from words. 
		String magicWord = words[random.nextInt(3)] + words[random.nextInt(3)] + words[random.nextInt(3)] + words[random.nextInt(3)];
		System.out.println("Racer-" + racer.getName() + " magic word: " + magicWord);
		//Checks each index if the magicWord is the same.
		for(int j = 0; j <map.size(); j++) {
			if(magicWord.equals(map.get(j))) {
				System.out.println("Racer-" + racer.getName() + " Found magic word at index "+ j + " returning.");
				System.out.println("Racer-" + racer.getName() + " Leaves forest");
				return;
			}
		}
	}

}
