package sync;

import java.util.concurrent.Semaphore;

public class SemaphoreAndSynchronized {
    public static void main(String[] args) {
        final SomeResource resource = new SomeResource(5);
        for (int i = 1; i <= 10; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    resource.useSemaphore();
                }
            });
            t.start();
        }

    }
}

class SomeResource {
    private final Semaphore semaphore;
    private final int maxThread;

    public SomeResource(int maxThread) {
        this.maxThread = maxThread;
        this.semaphore = new Semaphore(maxThread);
    }

    public void useSemaphore() {
        try {
            semaphore.acquire(); // Thread 가 semaphore에게 시작을 알림
            System.out.println("[" + Thread.currentThread().getName() + "]" + (maxThread - semaphore.availablePermits()) + "쓰레드가 점유중"); // semaphore.availablePermits() 사용가능한 Thread의 숫자
            Thread.sleep((long) (Math.random() * 10000));
            semaphore.release(); // Thread 가 semaphore에게 종료를 알림
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void useSynchronized(){
        try {
            System.out.println("[" + Thread.currentThread().getName() + "] : 시작" ); // semaphore.availablePermits() 사용가능한 Thread의 숫자
            Thread.sleep((long) (Math.random() * 10000));
            System.out.println("[" + Thread.currentThread().getName() + "] : 종료" ); // semaphore.availablePermits() 사용가능한 Thread의 숫자

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
