import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;

public class Scheduler {
    ArrayDeque<Process> ready = new ArrayDeque<>();
    ArrayList<Process> completed = new ArrayList<>();
    int executionTime = 0;
    int totalTime = 0;

    public void add(Process process) {
        ready.add(process);
    }

    public void printExecution() {
        System.out.printf("[Execution Time: %d]\n", executionTime);
        System.out.println("\n\n");
    }

    public void printResult() {
        completed.sort(Comparator.comparing(Process::getPid));

        for (Process p : completed) {
            p.setTurnaroundTime(p.getArrivalTime());
            System.out.println("\nProcess: " + p.getPid());
            System.out.println("Wait Time: " + p.getWaitTimes());
            System.out.println("Response Time: " + p.getResponseTime());
            System.out.println("Turnaround Time: " + p.getTurnaroundTime());
        }

        double waitTimeAvg = 0.0, turnaroundTimeAvg = 0.0, responseTimeAvg = 0.0;
        for (Process p : completed) {
            waitTimeAvg += p.getWaitTimes() / 8.0;
            turnaroundTimeAvg += p.getTurnaroundTime() / 8.0;
            responseTimeAvg += p.getResponseTime() / 8.0;
        }

        System.out.println("\nWait Time Average: " + String.format("%.2f", waitTimeAvg));
        System.out.println("Turnaround Time Average: " + String.format("%.2f", turnaroundTimeAvg));
        System.out.println("Response Time Average: " + String.format("%.2f", responseTimeAvg));
        System.out.println("\nCPU Utilization: " + String.format("%.2f", ((double) totalTime / executionTime) * 100.0) + "%");
    }
}
