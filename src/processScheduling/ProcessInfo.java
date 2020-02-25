package processScheduling;

import java.util.Comparator;

public class ProcessInfo implements Comparable<ProcessInfo> {
    private int pId, arrival_time, burst_time, waiting_time, turnaround_time;
    private double NormalizedTT;
    private int remainingBurst_time;

    public ProcessInfo(int pId, int arrival_time, int burst_time) {
        this.arrival_time = arrival_time;
        this.pId = pId;
        this.burst_time = burst_time;
        this.remainingBurst_time = burst_time;
    }


    public int getpId() {
        return pId;
    }

    public int getRemainingBurst_time() {
        return remainingBurst_time;
    }

    public int getArrival_time() {
        return arrival_time;
    }

    public int getBurst_time() {
        return burst_time;
    }

    public void setRemainingBurst_time(int tmpBurst_time) {
        this.remainingBurst_time = tmpBurst_time;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public int getTurnaround_time() {
        return turnaround_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public void setTurnaround_time(int turnaround_time) {
        this.turnaround_time = turnaround_time;
    }

    public void setNormalizedTT(double normalizedTT) {
        NormalizedTT = normalizedTT;
    }

    public double getNormalizedTT() {
        return NormalizedTT;
    }

    @Override
    public int compareTo(ProcessInfo o) {
        //SPN_Scheduling를 위해 burst time 으로 정렬
        if (this.burst_time < o.getBurst_time()) return -1;
        else if (this.burst_time < o.getBurst_time()) return 1;
        return 0;
    }

    static Comparator<ProcessInfo> idComparator = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo a, ProcessInfo b) {
            return  a.getpId()-b.getpId();
        }
    };
}
