package tou;

public class tournamentThread {
	
	private static int tournamentSize;
	private static int power2size;
	private static int absentNodes;
	private volatile int count;
	public final Runnable[] tasks;
	private volatile static boolean[][] want;
	private volatile static int[] priority;
	private long lockTimeSum;
	
	public int countResult(){
		return count;
	}
	public long timeResult(){
		return lockTimeSum;
	}

	tournamentThread(int n){
		tournamentSize=n;
		power2size=(int)Math.ceil(Math.log(n)/Math.log(2));
		absentNodes=(int)(Math.pow(2,power2size)-n);
		int nodesNumber=(int)Math.pow(2, power2size);
		want = new boolean[nodesNumber][2];
		priority = new int[nodesNumber];
		for(int i=0; i<nodesNumber; i++){
			for(int side=0;side<2;side++){
				want[i][side]=false;
			}
			priority[i]=0;
		}
		count=0;
		lockTimeSum=0;
		tasks = new Runnable[tournamentSize];
		for(int i=0; i<tournamentSize; i++){
			tasks[i]=new Runnable(){
				public void run(){
					int v = (int)Thread.currentThread().getId() % tournamentSize;
					int executingNode = (int)Math.pow(2, power2size-1)+(int)v/2;
					for(int j=0;j<10;j++){
						long time1 = System.nanoTime();
						this.node(executingNode,v%2);
						long time2 = System.nanoTime();
						lockTimeSum+=time2-time1;
						count++;
						//System.out.println("Thread "+v+" increment count to: "+count);
						try {		
							Thread.sleep(10);//sleep in minisecond
						}catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.pathExit(executingNode,v%2);
					}
				}
				private void pathExit(int v,int side){
					if(v==1){
						exit(v,side);
					}
					else{
						pathExit((int)v/2,v%2);
						exit(v,side);
					}
				}
				private void node(int v, int side){
					//absentNode(absentNodes,0);
					want[v][side]=false;
					while(want[v][1-side]==true && priority[v]==1-side){
						
					}
					want[v][side]=true;
					if(priority[v]==1-side && want[v][1-side]==true){
						node(v,side);
					}
					else{
						while(want[v][1-side]==true){}
						if(v==1){
							return;
						}
						else{
							node((int)v/2,v%2);
							return;
						}
					}
				}
				private void absentNode(int m, int layer){
					if(((int)m/2) >=1 ){
						absentNode(((int)m/2),layer+1);
					}
					for(int i=0; i<m; i++){
						int v= (int)(Math.pow(2, power2size-layer+1)-1-m);
						int side=(m+1)%2;
						want[v][side]=false;
						priority[v]=1-side;
					}
					
				}
				private void exit(int v,int side){
					want[v][side]=false;
					priority[v]=1-side;
				}
			};
		}
	}
}