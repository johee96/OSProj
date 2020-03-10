package processScheduling;


import java.util.*;

import static processScheduling.ProcessInfo.HRRNComparator;
import static processScheduling.ProcessInfo.idComparator;

class Scheduling {
    public ArrayList<ProcessInfo> FCFS_Scheduling(ArrayList<ProcessInfo> processInfo) {
    /*
        FCFS : 비선점 스케줄링, 도착시간을 기준으로 먼저 온 것 먼저 처리하는 방식
        따라서 입력받은 순으로 바로 처리한다.
     */

        int runningTime = 0;
        for (int i = 0; i < processInfo.size(); i++) {
            ProcessInfo tmp = processInfo.get(i);
            if (i == 0) {
                tmp.setWaiting_time(0);
                tmp.setTurnaround_time(tmp.getBurst_time());
                tmp.setNormalizedTT(calNTT(tmp));
                runningTime += tmp.getBurst_time();
                continue;
            }
            int waiting_time = tmp.getArrival_time() - runningTime;
            if (waiting_time < 0)
                waiting_time *= -1;
            else //안 기다림
                waiting_time = 0;

            tmp.setWaiting_time(waiting_time);
            tmp.setTurnaround_time(waiting_time + tmp.getBurst_time());
            double nTT = (double) tmp.getTurnaround_time() / tmp.getBurst_time();
            tmp.setNormalizedTT(Math.round(nTT * 10) / 10.0);
            runningTime += tmp.getBurst_time();
        }
        return processInfo;
    }

    public ArrayList<ProcessInfo> RR_Scheduling(ArrayList<ProcessInfo> processInfo, int timeQuantum) {
        /*
        RR : 먼저 도착한 프로세스를 먼저 처리한다. 단, 자원 사용 제한 시간이 있다. -> 독점 방지!!
         */
        Queue<ProcessInfo> ready_queue = new LinkedList<>();
        int runningTime = 0;        //진행한 시간
        int processInfoIdx = 0;     //ready queue에 넣을 process info의 idx
        ready_queue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!ready_queue.isEmpty()) {
            boolean checkContinue = false;
            ProcessInfo tmp = ready_queue.poll();
            int remainingBurstTime = tmp.getRemainingBurst_time() - timeQuantum;          //남은 burst time
            if (remainingBurstTime <= 0) {
                runningTime += tmp.getRemainingBurst_time();
                tmp.setTurnaround_time(runningTime - tmp.getArrival_time());
                tmp.setWaiting_time(tmp.getTurnaround_time() - tmp.getBurst_time());
                tmp.setNormalizedTT(calNTT(tmp));
                checkContinue = true;
            } else {
                runningTime += timeQuantum;
            }

            while (processInfoIdx < processInfo.size()) {
                ProcessInfo nextTmp = processInfo.get(processInfoIdx);
                if (runningTime >= nextTmp.getArrival_time()) {
                    ready_queue.add(nextTmp);
                    processInfoIdx++;
                    continue;
                }
                break;
            }

            if (checkContinue)
                continue;

            tmp.setRemainingBurst_time(remainingBurstTime);
            ready_queue.add(tmp);

        }

        return processInfo;
    }

    public ArrayList<ProcessInfo> SPN_Scheduling(ArrayList<ProcessInfo> processInfo) {
        /*
        Shortest-Process-Next (or SJF) : Burst time􏰰􏱃이 가장 작은 프􏰑􏰈로세스를 먼저 처리한다.
         */
        PriorityQueue<ProcessInfo> ready_queue = new PriorityQueue<>();
        int runningTime = 0;        //진행한 시간
        int processInfoIdx = 0;     //ready queue에 넣을 process info의 idx
        ready_queue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!ready_queue.isEmpty()) {
            ProcessInfo tmp = ready_queue.poll();
            runningTime += tmp.getRemainingBurst_time();

            tmp.setTurnaround_time(runningTime - tmp.getArrival_time());
            tmp.setWaiting_time(tmp.getTurnaround_time() - tmp.getBurst_time());
            tmp.setNormalizedTT(calNTT(tmp));

            while (processInfoIdx < processInfo.size()) {
                ProcessInfo nextTmp = processInfo.get(processInfoIdx);
                if (runningTime >= nextTmp.getArrival_time()) {
                    ready_queue.add(nextTmp);
                    processInfoIdx++;
                    continue;
                }
                break;
            }
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    public ArrayList<ProcessInfo> SRTN_Scheduling(ArrayList<ProcessInfo> processInfo) {

        /*
        Shortest Remaining Time Next : SRTN
        잔여 실행 시간이 더 적은 프로세스가 ready 상태로 되면 먼저 처리
        비현실적..
         */
        PriorityQueue<ProcessInfo> ready_queue = new PriorityQueue<>((o1, o2) -> o1.getRemainingBurst_time() - o2.getRemainingBurst_time());
        int runningTime = processInfo.get(0).getArrival_time();        //진행한 시간
        int processInfoIdx = 0;     //ready queue에 넣을 process info의 idx
        ready_queue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;
        for (int i = 1; i < processInfo.size(); i++) {
            if (runningTime == processInfo.get(i).getArrival_time()) {
                ready_queue.add(processInfo.get(processInfoIdx));
                processInfoIdx++;
            }
        }
        while (true) {
            boolean check = false;
            if (ready_queue.isEmpty())
                break;
            ProcessInfo tmp = ready_queue.poll();
            runningTime++;
            tmp.setRemainingBurst_time(tmp.getRemainingBurst_time() - 1);
            if (tmp.getRemainingBurst_time() == 0) {
                tmp.setTurnaround_time(runningTime - tmp.getArrival_time());
                tmp.setWaiting_time(tmp.getTurnaround_time() - tmp.getBurst_time());
                tmp.setNormalizedTT(calNTT(tmp));
                check = true;
            }
            for (int i = processInfoIdx; i < processInfo.size(); i++) {
                if (runningTime == processInfo.get(i).getArrival_time()) {
                    ready_queue.add(processInfo.get(i));
                }
            }
            if (!check) {
                ready_queue.add(tmp);
            }
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    public ArrayList<ProcessInfo> HRRN_Scheduling(ArrayList<ProcessInfo> processInfo) {
        PriorityQueue<ProcessInfo> ready_queue = new PriorityQueue<>((o1, o2) -> {
            int responseRatio1 = (o1.getWaiting_time() + o1.getRemainingBurst_time()) / o1.getRemainingBurst_time();
            int responseRatio2 = (o2.getWaiting_time() + o2.getRemainingBurst_time()) / o2.getRemainingBurst_time();
            return responseRatio2 - responseRatio1;
        });
        int runningTime = 0;        //진행한 시간
        int processInfoIdx = 0;     //ready queue에 넣을 process info의 idx
        ready_queue.add(processInfo.get(processInfoIdx));
        processInfoIdx++;

        while (!ready_queue.isEmpty()) {
            ProcessInfo tmp = ready_queue.poll();
            runningTime += tmp.getRemainingBurst_time();

            tmp.setTurnaround_time(runningTime - tmp.getArrival_time());
            tmp.setWaiting_time(tmp.getTurnaround_time() - tmp.getBurst_time());
            tmp.setNormalizedTT(calNTT(tmp));

            while (processInfoIdx < processInfo.size()) {
                ProcessInfo nextTmp = processInfo.get(processInfoIdx);
                if (runningTime >= nextTmp.getArrival_time()) {
                    ready_queue.add(nextTmp);
                    processInfoIdx++;
                    continue;
                }
                break;
            }
        }
        processInfo.sort(idComparator);
        return processInfo;
    }

    private static double calNTT(ProcessInfo tmp) {
        double nTT = (double) tmp.getTurnaround_time() / tmp.getBurst_time();
        return Math.round(nTT * 10) / 10.0;
    }
}
