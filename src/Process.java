import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;

public class Process {
    private final int pId;
    private int waitTimes;
    private int arrivalTime;
    private int responseTime;
    private int turnaroundTime;
    private int queue;
    private boolean firstRun;
    private final List<Integer> data;
    private final List<Integer> cpuBursts;
    private final ArrayDeque<Integer> ioBursts;

    public Process(int pId, List<Integer> data) {
        this.pId = pId;
        this.data = data;
        this.waitTimes = 0;
        this.arrivalTime = 0;
        this.responseTime = 0;
        this.turnaroundTime = 0;
        this.queue = 1;
        this.firstRun = true;
        this.cpuBursts = new ArrayList<>();
        this.ioBursts = new ArrayDeque<>();
    }

    public void parse() {
        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                cpuBursts.add(data.get(i));
            } else {
                ioBursts.offer(data.get(i));
            }
        }
    }

    public int getPid() {
        return pId;
    }

    public int getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(int waitTimes) {
        this.waitTimes = waitTimes;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public List<Integer> getCpuBursts() {
        return cpuBursts;
    }

    public int getFirstCpuBurst() { return cpuBursts.getFirst(); }

    public void setFirstCpuBurst(int x) {
        cpuBursts.set(0, x);
    }

    public ArrayDeque<Integer> getIoBursts() {
        return ioBursts;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }
}