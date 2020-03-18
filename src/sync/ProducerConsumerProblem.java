package sync;

import java.util.concurrent.Semaphore;

public class ProducerConsumerProblem {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(100);
        Producer producer = new Producer(buffer, 10000);
        Consumer consumer = new Consumer(buffer, 10000);
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
        }
    }

    static class Buffer {
        int size;
        int in;
        int out;
        int[] buffer;
        int count;
        Semaphore mutex, pro, con;

        Buffer(int size) {
            this.size = size;
            buffer = new int[this.size];
            in = out = count = 0;
            mutex = new Semaphore(1);
            pro = new Semaphore(size);  // 생산자를 위한 버퍼
            con = new Semaphore(0); // 소비자를 위한 버퍼
        }

        void putIn(int item) {
            while (count == size) ;
            try {
                pro.acquire(); // 버퍼의 비어있는 공간을 1 감소시킨다.(비어있는 공간이 없으면 block)
                mutex.acquire();
                count++;
                buffer[in] = item;
                System.out.println(buffer[in] + "을 생산했습니다.");
                in = (in + 1) % size;
                mutex.release();
                con.release(); // 버퍼에 찬 공간을 1 증가시킨다.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int takeOut() {
            while (count == 0) ;
            int output = 0;
            try {
                con.acquire(); // 버퍼의 찬 공간을 1 감소시킨다.(버퍼가 모두 비어있으면 block)
                mutex.acquire();
                count--;
                output = buffer[out];
                System.out.println(output + "을 소비했습니다.");
                out = (out + 1) % size;
                mutex.release();
                pro.release(); // 버퍼의 비어있는 공간을 1 증가 시킨다.
                return output;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    static class Producer extends Thread {
        Buffer b;
        int cnt;

        Producer(Buffer b, int cnt) {
            this.b = b;
            this.cnt = cnt;
        }

        public void run() {
            for (int i = 0; i < cnt; i++) {
                b.putIn(i);
            }
        }
    }

    static class Consumer extends Thread {
        Buffer b;
        int cnt;

        Consumer(Buffer b, int cnt) {
            this.b = b;
            this.cnt = cnt;
        }

        public void run() {
            for (int i = 0; i < cnt; i++) {
                b.takeOut();
            }
        }
    }
}
