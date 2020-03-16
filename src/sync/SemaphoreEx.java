package sync;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore를 이용한 한정된 자원 사용
 */
public class SemaphoreEx {
    public static void main(String[] args) {
        System.out.println("Starting...");
        BoundedResource resource = new BoundedResource(5);
        //10개의 스레드가 생성되어있지만, 동시에 리소스를 사용할 수 있는 스레드는 3개임
        for(int i=0;i<20;i++){
            new UserThread(resource).start();
        }
    }

}

class UserThread extends Thread {
    private final static Random random = new Random(1000);
    private final BoundedResource resource;

    public UserThread(BoundedResource resource) {
        this.resource = resource;
    }
    public void run(){
        try {
            while(true){
                resource.use();
                Thread.sleep(random.nextInt(3000));
            }
        }catch(InterruptedException e){
            Log.show("InterruptedException : "+e);
        }
    }
}

class BoundedResource {
    private final Semaphore semaphore;
    private final int nPermits;
    private final static Random random = new Random(1000);

    public BoundedResource(int cnt) {
        this.semaphore = new Semaphore(cnt);
        this.nPermits = cnt;
    }

    public void use() throws InterruptedException {
        semaphore.acquire(); // 세마포어 리소스 확보
        try {
            doUse();
        } finally {
            semaphore.release(); //세마포어 리소스 해제
        }
    }

    protected void doUse() throws InterruptedException {
        /*
        nPermits - semaphore.availablePermits()
        = 최대 리소스 개수 - 세마포어에서 이용가능한 리소스 개수
        = 현재 사용중인 리소스 개수
         */
        Log.show("Begin : 현재 사용중인 리소스 개수 = " + (nPermits - semaphore.availablePermits()));
        Thread.sleep(random.nextInt(500));
        Log.show("End : 현재 사용중인 리소스 개수 = " + (nPermits - semaphore.availablePermits()));
    }

}

class Log {
    public static void show(String msg) {
        System.out.println(Thread.currentThread().getName() + ":" + msg);
    }
}
