package model;
//References:
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
class TestDemo extends Thread {
	ASData asData;

	public TestDemo(ASData asData) {
		this.asData = asData;		
	}
	
	public void run() {
		try {
			System.out.println("Thread " + Thread.currentThread().getId() + " is running");
			System.out.println("Thread " + Thread.currentThread().getId() + ", Method : playerSignIn() , " + asData.playerSignIn("testuser1", "testuser1", "182.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + ", Method : createPlayerAccount() , " + asData.createPlayerAccount("test1", "user1", "24", "testuser1", "testuser1", "182.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + ", Method : playerSignIn() , " + asData.playerSignIn("testuser1", "testuser1", "182.123.123.123"));
		} catch (Exception e) {
			System.out.println("Exception is caught");
		}
	}
}

public class Testing {
	private static ASData asData;

	public static void main(String[] args) {
		asData = new ASData();
//		asData.createPlayerAccount("test1", "user1", "24", "testuser1", "testuser1", "182.123.123.123");
		
		for (int i = 0; i < 3; i++) {
			TestDemo td = new TestDemo(asData);
			td.start();
			asData = td.asData;
		}

	}
}