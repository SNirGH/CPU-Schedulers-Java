import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FCFS extends Scheduler {
    public void start() {
        for (Process p : ready) {
            totalTime += p.getCpuBursts().stream().mapToInt(Integer::intValue).sum();
        }

        while(true) {
            printExecution();

            List<Process> sortedList = ready.stream().sorted(Comparator.comparingInt(Process::getArrivalTime)).toList();
            ready.clear();
            ready.addAll(sortedList);
            assert ready.peek() != null;
            if (ready.peek().getCpuBursts().isEmpty()) {
                assert ready.peek() != null;
                if (ready.peek().getIoBursts().isEmpty()) {
                    completed.add(ready.poll());
                }
            }

            Process currentProcess = ready.poll();
            if(currentProcess != null) {
                if (currentProcess.isFirstRun()) {
                    currentProcess.setResponseTime(executionTime);
                    currentProcess.setFirstRun(false);
                }
                if (currentProcess.getArrivalTime() <= executionTime) {
                    currentProcess.setWaitTimes(currentProcess.getWaitTimes() + (executionTime - currentProcess.getArrivalTime()));
                }

                int cpuBurst = currentProcess.getCpuBursts().removeFirst();
                int ioBurst = currentProcess.getIoBursts().isEmpty() ? 0 : currentProcess.getIoBursts().removeFirst();
                if (currentProcess.getArrivalTime() > executionTime) {
                    executionTime = currentProcess.getArrivalTime();
                    printExecution();
                }
                executionTime += cpuBurst;

                currentProcess.setArrivalTime(executionTime + ioBurst);
                System.out.println(currentProcess);
                ready.offer(currentProcess);
            }
            else {
                printResult();
                break;
            }
        }
    }
}
