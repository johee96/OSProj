package processScheduling;

import java.util.ArrayList;

public class TestMain {
    public static void main(String[] args) {
        Scheduling scheduling = new Scheduling();
        ArrayList<ProcessInfo> FCFSSchedulingResult = scheduling.FCFS_Scheduling(getDummyProcess());
        printProcessInfo(FCFSSchedulingResult);
        ArrayList<ProcessInfo> RRSchedulingResult = scheduling.RR_Scheduling(getDummyProcess(), 4);
        printProcessInfo(RRSchedulingResult);
        ArrayList<ProcessInfo> SPNSchedulingResult = scheduling.SPN_Scheduling(getDummyProcess());
        printProcessInfo(SPNSchedulingResult);
        ArrayList<ProcessInfo> SRTNSchedulingResult = scheduling.SRTN_Scheduling(getDummyProcess());
        printProcessInfo(SRTNSchedulingResult);
        ArrayList<ProcessInfo> HRRNSchedulingResult = scheduling.HRRN_Scheduling(getDummyProcess());
        printProcessInfo(HRRNSchedulingResult);
    }

    private static ArrayList<ProcessInfo> getDummyProcess() {
        ArrayList<ProcessInfo> dummyProcessInfo = new ArrayList<>();
        dummyProcessInfo.add(new ProcessInfo(1, 0, 3));
        dummyProcessInfo.add(new ProcessInfo(2, 1, 7));
        dummyProcessInfo.add(new ProcessInfo(3, 3, 2));
        dummyProcessInfo.add(new ProcessInfo(4, 5, 5));
        dummyProcessInfo.add(new ProcessInfo(5, 6, 3));
        return dummyProcessInfo;
    }

    private static void printProcessInfo(ArrayList<ProcessInfo> processInfo) {
        System.out.println("---------------------------------");

        double TTSum = 0;
        for (ProcessInfo tmp : processInfo) {
            System.out.println("Pid: " + tmp.getpId() + " Arrival Time: " + tmp.getArrival_time()
                    + " BurstTime: " + tmp.getBurst_time() + " WaitingTime: " + tmp.getWaiting_time() + " TurnaroundTime: " + tmp.getTurnaround_time()
                    + " NormalizedTT: " + tmp.getNormalizedTT());
            TTSum += tmp.getTurnaround_time();
        }
        System.out.println("Average response time : " + TTSum / processInfo.size());

    }
}
