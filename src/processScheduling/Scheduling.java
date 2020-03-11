package processScheduling;


import java.util.*;

import static processScheduling.ProcessInfo.*;

/**
 * Represents process scheduling algorithms.
 *
 * @author cho hee
 * @since 2020-03-11
 */
class Scheduling {
    /**
     * A process scheduling algorithm is FCFS Scheduling.
     * <p> FCFS (First Comes First Service) : Non-preemptive Scheduling.
     * It's a way to deal with what came first(based on arrival time).
     * Therefore, It is processed in the order entered.
     *
     * @param processInfo A processInfo containing process information list.
     * @return A processInfo containing process information list that performed FCFS.
     */
    public ArrayList<ProcessInfo> FCFS_Scheduling(ArrayList<ProcessInfo> processInfo) {
        int runningTime = processInfo.get(0).getArrivalTime();        //진행한 시간
        for (int i = 0; i < processInfo.size(); i++) {
            ProcessInfo tmp = processInfo.get(i);
            runningTime += tmp.getBurstTime();
            setProcessInfo(runningTime, tmp);
        }
        return processInfo;
    }

    /**
     * A process scheduling algorithm is RR Scheduling.
     * <p> RR (Round Robin) : Preemptive Scheduling.
     * It's a way to deal with what came first(based on arrival time).
     * There is a time limit for resource use.
     *
     * @param processInfo A processInfo containing process information list.
     * @param timeQuantum A int containing time quantum that is a time limit for resource use.
     * @return A processInfo containing process information list that performed RR.
     */
    public ArrayList<ProcessInfo> RR_Scheduling(ArrayList<ProcessInfo> processInfo, int timeQuantum) {
        Queue<ProcessInfo> readyQueue = new LinkedList<>();
        int runningTime = processInfo.get(0).getArrivalTime();        //진행한 시간
        int processInfoIdx = 0;                                       //ready queue에 넣을 process info의 idx
        readyQueue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!readyQueue.isEmpty()) {
            boolean isFinished = false;
            ProcessInfo tmp = readyQueue.poll();
            int remainingBurstTime = tmp.getRemainingBurstTime() - timeQuantum;
            if (remainingBurstTime <= 0) {
                runningTime += tmp.getRemainingBurstTime();
                setProcessInfo(runningTime, tmp);
                isFinished = true;
            } else {
                runningTime += timeQuantum;
            }
            //해당 시간에 도착한 프로세스들이 여러개일 수 있으니까 전체적으로 확인
            processInfoIdx = addProcessToReadyQueue(processInfo, readyQueue, processInfoIdx, runningTime);

            if (isFinished)
                continue;
            tmp.setRemainingBurstTime(remainingBurstTime);
            readyQueue.add(tmp);
        }

        return processInfo;
    }

    /**
     * A process scheduling algorithm is SPN Scheduling.
     * <p> SPN (Shortest Process Next) or SJF : Non-preemptive Scheduling.
     * It's a way to deal with what has shortest burst time (based on burst time).
     * @param processInfo A processInfo containing process information list.
     * @return A processInfo containing process information list that performed SPN.
     */
    public ArrayList<ProcessInfo> SPN_Scheduling(ArrayList<ProcessInfo> processInfo) {
        PriorityQueue<ProcessInfo> readyQueue = new PriorityQueue<>();
        int runningTime = processInfo.get(0).getArrivalTime();        //진행한 시간
        int processInfoIdx = 0;                                       //ready queue에 넣을 process info의 idx
        readyQueue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!readyQueue.isEmpty()) {
            ProcessInfo tmp = readyQueue.poll();
            runningTime += tmp.getRemainingBurstTime();
            setProcessInfo(runningTime, tmp);
            processInfoIdx = addProcessToReadyQueue(processInfo, readyQueue, processInfoIdx, runningTime);
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    /**
     * A process scheduling algorithm is SRTN Scheduling.
     * <p> SRTN (Shortest Remaining Time Next) : Preemptive Scheduling.
     * It's a way to deal with what has shortest remaining burst time (based on remaining burst time).
     * @param processInfo A processInfo containing process information list.
     * @return A processInfo containing process information list that performed SRTN.
     */
    public ArrayList<ProcessInfo> SRTN_Scheduling(ArrayList<ProcessInfo> processInfo) {
        PriorityQueue<ProcessInfo> readyQueue = new PriorityQueue<>(SRTNComparator);
        int runningTime = processInfo.get(0).getArrivalTime();        //진행한 시간
        int processInfoIdx = 0;                                       //ready queue에 넣을 process info의 idx
        readyQueue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;
        for (int i = 1; i < processInfo.size(); i++) {
            if (runningTime == processInfo.get(i).getArrivalTime()) {
                readyQueue.add(processInfo.get(processInfoIdx));
                processInfoIdx++;
            }
        }
        while (true) {
            boolean check = false;
            if (readyQueue.isEmpty())
                break;
            ProcessInfo tmp = readyQueue.poll();
            runningTime++;
            tmp.setRemainingBurstTime(tmp.getRemainingBurstTime() - 1);
            if (tmp.getRemainingBurstTime() == 0) {
                setProcessInfo(runningTime, tmp);
                check = true;
            }
            for (int i = processInfoIdx; i < processInfo.size(); i++) {
                if (runningTime == processInfo.get(i).getArrivalTime()) {
                    readyQueue.add(processInfo.get(i));
                }
            }
            if (!check) {
                readyQueue.add(tmp);
            }
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    /**
     * A process scheduling algorithm is HRRN Scheduling.
     * <p> HRRN (High Response Ratio Next) : Non-preemptive Scheduling.
     * It's a way to deal with what has highest response ratio(based on response ratio).
     * response ratio = (WT+BT) / BT.
     * @param processInfo A processInfo containing process information list.
     * @return A processInfo containing process information list that performed HRRN.
     */
    public ArrayList<ProcessInfo> HRRN_Scheduling(ArrayList<ProcessInfo> processInfo) {
        PriorityQueue<ProcessInfo> readyQueue = new PriorityQueue<>(HRRNComparator);
        int runningTime = processInfo.get(0).getArrivalTime();        //진행한 시간
        int processInfoIdx = 0;                                       //ready queue에 넣을 process info의 idx
        readyQueue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!readyQueue.isEmpty()) {
            ProcessInfo tmp = readyQueue.poll();
            runningTime += tmp.getRemainingBurstTime();
            setProcessInfo(runningTime, tmp);
            processInfoIdx = addProcessToReadyQueue(processInfo, readyQueue, processInfoIdx, runningTime);
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    /**
     * Sets process information after processing.
     * @param runningTime It is the sum of the time it takes to work.
     * @param processInfo A processInfo containing process information list.
     */
    private void setProcessInfo(int runningTime, ProcessInfo processInfo) {
        processInfo.setTurnaroundTime(runningTime - processInfo.getArrivalTime());
        processInfo.setWaitingTime(processInfo.getTurnaroundTime() - processInfo.getBurstTime());
        processInfo.setNormalizedTT(calNTT(processInfo));
    }

    /**
     * Adds process information to ready queue.
     * @param processInfo A processInfo containing process information list.
     * @param readyQueue A collection of processes ready to execute.
     * @param processInfoIdx Index of processInfo.
     * @param runningTime Sum of the time it takes to work.
     * @return A int containing processInoIdx
     */
    private int addProcessToReadyQueue(ArrayList<ProcessInfo> processInfo, Queue<ProcessInfo> readyQueue, int processInfoIdx, int runningTime) {
        while (processInfoIdx < processInfo.size()) {
            ProcessInfo nextTmp = processInfo.get(processInfoIdx);
            if (runningTime >= nextTmp.getArrivalTime()) {
                readyQueue.add(nextTmp);
                processInfoIdx++;
                continue;
            }
            break;
        }
        return processInfoIdx;
    }

}