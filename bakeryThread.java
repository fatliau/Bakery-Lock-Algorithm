package bak;

public class bakeryThread {

	private static int backerySize;
	private volatile int count;
	public final Runnable[] tasks;
	private volatile static boolean[] flag;
	private volatile static int[] label;
	private long lockTimeSum;
	
	public int countResult(){
		return count;
	}
	public long timeResult(){
		return lockTimeSum;
	}
	
	bakeryThread(int n){
		backerySize=n;
		flag = new boolean[n];
		label = new int[n];
		for(int i=0; i<n; i++){
			flag[i]=false;
			label[i]=0;
		}
		tasks = new Runnable[n];
		count=0;
		lockTimeSum=0;
		for(int i=0; i<n; i++){
			flag[i]=false;
			label[i]=0;
			tasks[i]=new Runnable(){
				public void run(){
					int v = (int) Thread.currentThread().getId() % backerySize;
					for(int j=0;j<10;j++){
						long time1 = System.nanoTime();
						this.lock();
						long time2 = System.nanoTime();
						lockTimeSum+=time2-time1;
						count++;
						//System.out.println("Thread "+v+" increment count to: "+count);
						try {		
							Thread.sleep(10);//sleep in minisecond
						}catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.unlock();
					}
				}
				public void lock(){
					int i = (int) Thread.currentThread().getId() % backerySize;
					flag[i] = true;
					int temp=0;
					for(int a=0; a<backerySize ;a++){
						temp=(temp<label[a])?label[a]:temp;
					}
					label[i]=temp+1;
					while(waitInWhile(i)){		

					}
				}
				public boolean waitInWhile(int id){
					boolean result=false;
					outerloop:
						for(int b=0; b<backerySize; b++){
							if(flag[b]==true){
								if(label[b]<label[id]){
									result=true;
									break outerloop;
								}
								else if(label[b]==label[id]){
									if(b<id){
										result=true;
										break outerloop;
									}
								}
							}
						}
					return result;
				}
				public void unlock(){
					int i = (int) Thread.currentThread().getId() % backerySize;
					flag[i] = false;
				}
			};
		}
	}
}
