package tou;

public class tournament {
	public static void main(String[] args)throws InterruptedException{
		
		tournament main = new tournament();
		System.out.println("Start Tournament...");
		long[] tournament=main.tournamentResults(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		
		for(int i=0; i< tournament.length; i++)
			System.out.format("%,13d%n", tournament[i]); 	
		System.out.println("END");
	}
	
	long[] tournamentResults(int startNo, int endNo){
		long timeResults[] = new long[endNo-startNo];
		for(int j=0; j <endNo-startNo ; j++){
			int n=startNo+j;
			tournamentThread ts=new tournamentThread(n);
			Thread[] t=new Thread[n];
			for(int i=0;i<n;i++){
				t[i]=new Thread(ts.tasks[i]);
				t[i].start();
			}
			for(int i=0;i<n;i++){
				try {
					t[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("The count result compares to thread number "+(n)+"*10 : "+(ts.countResult()==n*10));
			System.out.println("The average lock time in each increment: "+(ts.timeResult()/(n*10)));
			timeResults[j]=ts.timeResult()/(n*10);
		}
		return timeResults;
	}
}