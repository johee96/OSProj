package sync;

import java.util.concurrent.Semaphore;

public class DiningPhilosopherProblem {
    public static void main(String[] args) {
        Semaphore[] sticks = new Semaphore[5];
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            sticks[i] = new Semaphore(1);
        }
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i, sticks[i], sticks[(i + 1) % 5]);
        }
        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }


    }

    static class Philosopher extends Thread {
        int id;
        Semaphore lStick, rStick;

        Philosopher(int id, Semaphore lStick, Semaphore rStick) {
            this.id = id;
            this.lStick = lStick;
            this.rStick = rStick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if(id % 2==0){
                        lStick.acquire();
                        rStick.acquire();
                    }else{
                        rStick.acquire();
                        lStick.acquire();
                    }
                    eating();
                    lStick.release();
                    rStick.release();
                    thinking();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void eating() {
            System.out.println(id + "가 먹고있는 중..");
        }

        void thinking() {
            System.out.println(id + "가 생각하고 있는 중..");
        }

    }
}
