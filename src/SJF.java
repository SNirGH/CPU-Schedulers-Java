import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJF extends Scheduler {
    public static void sortReady(ArrayDeque<Process> ready) {
        ArrayList<Process> list = new ArrayList<>(ready);
        list.sort(Comparator.comparingInt(Process::getFirstCpuBurst)
                .thenComparingInt(Process::getPid));
        ready.clear();
        ready.addAll(list);
    }

    public void start() {
        for (Process p : ready) {
            totalTime += p.getCpuBursts().stream().mapToInt(Integer::intValue).sum();
        }

        while(true) {
            printExecution();

            for (Process currentProcess : ready) {
                if (currentProcess.getCpuBursts().isEmpty() && currentProcess.getIoBursts().isEmpty()) {
                    completed.add(currentProcess);
                    ready.remove(currentProcess);
                }
            }

            sortReady(ready);

            int i = 0;
            if(!ready.isEmpty()) {
                while(ready.getFirst().getArrivalTime() > executionTime) {
                    if (i == ready.size()) {
                        List<Process> sortedList = ready.stream().sorted(Comparator.comparingInt(Process::getArrivalTime)).toList();
                        ready.clear();
                        ready.addAll(sortedList);
                        break;
                    }
                    Process notReady = ready.removeFirst();
                    ready.addLast(notReady);
                    i++;
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
                ready.offer(currentProcess);
            }
            else {
                printResult();
                break;
            }
        }
    }
}
