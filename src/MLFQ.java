import java.util.Comparator;
import java.util.List;

public class MLFQ extends Scheduler {
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

            List<Process> sortedList = ready.stream().sorted(Comparator.comparingInt(Process::getQueue)).toList();
            ready.clear();
            ready.addAll(sortedList);

            int i = 0;
            if(!ready.isEmpty()) {
                while(ready.getFirst().getArrivalTime() > executionTime) {
                    if (i == ready.size()) {
                        List<Process> sortArrivalTimes = ready.stream().sorted(Comparator.comparingInt(Process::getArrivalTime)).toList();
                        ready.clear();
                        ready.addAll(sortArrivalTimes);
                        break;
                    }
                    Process notReady = ready.removeFirst();
                    ready.addLast(notReady);
                    i++;
                }
            }

            Process currentProcess = ready.poll();
            if(currentProcess != null) {
                switch(currentProcess.getQueue()) {
                    case 1:
                        round_robin(currentProcess);
                        break;
                    case 2:
                        round_robin(currentProcess);
                        break;
                    case 3:
                        fcfs(currentProcess);
                        break;
                    default:
                        break;
                }
            }
            else {
                printResult();
                break;
            }
        }
    }

    private void fcfs(Process currentProcess) {
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

    private void round_robin(Process currentProcess) {
        if (currentProcess.isFirstRun()) {
            currentProcess.setResponseTime(executionTime);
            currentProcess.setFirstRun(false);
        }

        int tq = switch (currentProcess.getQueue()) {
            case 1 -> 5;
            case 2 -> 10;
            default -> 0;
        };

        if (currentProcess.getArrivalTime() <= executionTime) {
            currentProcess.setWaitTimes(currentProcess.getWaitTimes() + (executionTime - currentProcess.getArrivalTime()));
        }

        int cpuBurst = currentProcess.getCpuBursts().getFirst();
        int ioBurst = 0;
        if (!currentProcess.getIoBursts().isEmpty()) {
            ioBurst = currentProcess.getIoBursts().getFirst();
        }

        int checkNegative = currentProcess.getFirstCpuBurst() - tq;
        currentProcess.setFirstCpuBurst(Math.max(checkNegative, 0));

        if ((currentProcess.getQueue() == 1 || currentProcess.getQueue() == 2) && currentProcess.getFirstCpuBurst() == 0) {
            executionTime += cpuBurst;
            currentProcess.setArrivalTime(executionTime + ioBurst);
            currentProcess.getCpuBursts().removeFirst();
            if (!currentProcess.getIoBursts().isEmpty())
                currentProcess.getIoBursts().removeFirst();
        }
        else if ((currentProcess.getQueue() == 1 || currentProcess.getQueue() == 2) && currentProcess.getFirstCpuBurst() > 0) {
            executionTime += tq;
            currentProcess.setQueue(currentProcess.getQueue() + 1);
        }

        ready.addLast(currentProcess);
    }
}
