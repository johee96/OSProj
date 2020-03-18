package sync;

public class ProducerConsumerProblemSync {
    public static void main(String[] args) {
        Buffer buff = new Buffer(100);
        Producer pro = new Producer(buff, 10000);
        Consumer con = new Consumer(buff, 10000);

        pro.start();
        con.start();
        try {
            pro.join();
            con.join();
        } catch (InterruptedException e) {
        }
    }

    static class Buffer {
        int size;
        int[] buffer;
        int count, in, out;

        Buffer(int size) {
            this.size = size;
            buffer = new int[this.size];
            count = in = out = 0;
        }

        synchronized void putIn(int item) {
            if (count == size) { //기다려
                try {
                    notifyAll();
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                count++;
                buffer[in] = item;
                System.out.println(buffer[in] + "을 생산했습니다.");
                in = (in + 1) % size;
                notifyAll();
            }
        }

        synchronized int takeOut() {
            if (count == 0) {
                try {
                    notifyAll();
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                count--;
                int output = buffer[out];
                System.out.println(output + "을 소비했습니다.");
                out = (out + 1) % size;
                notifyAll();
                return output;
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
