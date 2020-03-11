package processScheduling;

import java.util.Comparator;

/**
 * Represents process information.
 *
 * @author cho hee
 * @since 2020-03-11
 */
public class ProcessInfo implements Comparable<ProcessInfo> {
    /**
     * Represents process id.
     */
    private int pId;
    /**
     * Represents process arrival time.
     */
    private int arrivalTime;
    /**
     * Represents process burst time entered.
     */
    private int burstTime;
    /**
     * Represents process waiting time.
     */
    private int waitingTime;
    /**
     * The time it takes to start and complete the process.
     */
    private int turnaroundTime;
    /**
     * Represents normalized turnaround time.
     */
    private double normalizedTT;
    /**
     * Represents the remaining process burst time.
     */
    private int remainingBurstTime;

    /**
     * Create an process information.
     *
     * @param pId         the process id
     * @param arrivalTime the process arrival time
     * @param burstTime   the process burst time
     */
    public ProcessInfo(int pId, int arrivalTime, int burstTime) {
        this.arrivalTime = arrivalTime;
        this.pId = pId;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
    }

    /**
     * Gets the process id.
     *
     * @return A int representing the process id.
     */
    public int getpId() {
        return this.pId;
    }

    /**
     * Gets the remaining process burst time.
     *
     * @return A int representing the remaining process burst time.
     */
    public int getRemainingBurstTime() {
        return this.remainingBurstTime;
    }

    /**
     * Gets the process arrival time.
     *
     * @return int representing the process arrival time.
     */
    public int getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Gets the process burst time.
     *
     * @return int representing the process burst time.
     */
    public int getBurstTime() {
        return this.burstTime;
    }

    /**
     * Gets the process waiting time.
     *
     * @return A int representing the process waiting time.
     */
    public int getWaitingTime() {
        return this.waitingTime;
    }

    /**
     * Gets the process turnaround time.
     *
     * @return A int representing the process turnaround time.
     */
    public int getTurnaroundTime() {
        return this.turnaroundTime;
    }

    /**
     * Gets the process normalized turnaround time.
     *
     * @return A double representing the process normalized turnaround time.
     */
    public double getNormalizedTT() {
        return this.normalizedTT;
    }

    /**
     * Sets the process burst time.
     *
     * @param burstTime A int containing the process burst time.
     */
    public void setRemainingBurstTime(int burstTime) {
        this.remainingBurstTime = burstTime;
    }

    /**
     * Sets the process waiting time.
     *
     * @param waitingTime A int containing the process waiting time.
     */
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    /**
     * Sets the process turnaround time.
     *
     * @param turnaroundTime A int containing the process turnaround time.
     */
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    /**
     * Sets the process normalized turnaround time.
     *
     * @param normalizedTT A double containing the process turnaround time.
     */
    public void setNormalizedTT(double normalizedTT) {
        this.normalizedTT = normalizedTT;
    }

    /**
     * Sort by burst time for SPN Scheduling
     *
     * @param processInfo A ProcessInfo containing the Process information.
     * @return A int containing comparative result
     */
    @Override
    public int compareTo(ProcessInfo processInfo) {
        //SPN_Scheduling를 위해 burst time 으로 정렬
        if (this.burstTime < processInfo.getBurstTime()) return -1;
        else if (this.burstTime > processInfo.getBurstTime()) return 1;
        return 0;
    }

    /**
     * Sort by process id
     */
    static Comparator<ProcessInfo> idComparator = (a, b) -> a.getpId() - b.getpId();
    /**
     * Sort by response ratio in HRRN algorithm.
     */
    static Comparator<ProcessInfo> HRRNComparator = (o1, o2) -> {
        int responseRatio1 = (o1.getWaitingTime() + o1.getRemainingBurstTime()) / o1.getRemainingBurstTime();
        int responseRatio2 = (o2.getWaitingTime() + o2.getRemainingBurstTime()) / o2.getRemainingBurstTime();
        return responseRatio2 - responseRatio1;
    };
    /**
     * Sort by remaining process burst time in SRTN algorithm.
     */
    static Comparator<ProcessInfo> SRTNComparator = (o1, o2) -> o1.getRemainingBurstTime() - o2.getRemainingBurstTime();
    /**
     * Calculate normalized turnaround time.
     *
     * @param processInfo A processInfo containing the process information.
     * @return A double containing calculated result
     */
    static double calNTT(ProcessInfo processInfo) {
        double nTT = (double) processInfo.getTurnaroundTime() / processInfo.getBurstTime();
        return Math.round(nTT * 10) / 10.0;
    }
}
