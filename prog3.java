import java.util.Scanner;

/*
 * Authors:
 * Cal Mezzell
 * Shane McDermott
 * Jason Jacobs
 */

public class prog3 {
	
	private MemoryManager memoryManager;
	
	public prog3 () {
		readInput();
	}
	
	public void readFirstLine () {
		Scanner in = new Scanner(System.in);
		memoryManager = new MemoryManager(in.nextInt(), in.nextInt());
		in.nextLine();
	}
	
	public void readInput () {
		Scanner in = new Scanner(System.in);
		memoryManager = new MemoryManager(in.nextInt(), in.nextInt());
		
		while (in.hasNext()) {
			int id = in.nextInt();
			char action = in.next().charAt(0);
			
			if (action == '+') {
				int size = in.nextInt();
				memoryManager.allocate(id, size);
			}
			else if (action == '-') {
				memoryManager.deallocate(id);
			}
			in.nextLine();
		}
	}
	
	public static void main (String[] args) {
		new prog3();
	}
}
