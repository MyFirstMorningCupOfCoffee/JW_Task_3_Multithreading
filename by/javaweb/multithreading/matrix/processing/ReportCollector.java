package by.javaweb.multithreading.matrix.processing;

import by.javaweb.multithreading.matrix.Cell;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

public class ReportCollector {

    private final ArrayList<Unit> positiveResults;
    private final ArrayList<Unit> negativeResults;
    private final ReentrantLock lock;

    public ReportCollector() {
        positiveResults = new ArrayList<>();
        negativeResults = new ArrayList<>();
        lock = new ReentrantLock();
    }

    protected class Unit {

        int processorId;
        Cell target;

        public Unit(int processorId, Cell target) {
            this.processorId = processorId;
            this.target = target;
        }

    }

    // There is no real point in making report methods synchronized,
    // but I'd like to keep report units in the order they evoke these methods
    public void reportFail(int id, Cell target) {
        try {
            lock.lock();
            Unit unit = new Unit(id, target);
            negativeResults.add(unit);
        } finally {
            lock.unlock();
        }
    }

    public void reportSuccess(int id, Cell target) {
        try {
            lock.lock();
            Unit unit = new Unit(id, target);
            positiveResults.add(unit);
        } finally {
            lock.unlock();
        }
    }

    public String results() {
        Map<Integer, Integer> succMap = new TreeMap<>();
        Map<Integer, Integer> failMap = new TreeMap<>();

        for (Unit u : positiveResults) {
            Integer count;
            if ((count = succMap.get(u.processorId)) != null) {
                succMap.put(u.processorId, count + 1);
            } else {
                succMap.put(u.processorId, 1);
            }
        }

        for (Unit u : negativeResults) {
            Integer count;
            if ((count = failMap.get(u.processorId)) != null) {
                failMap.put(u.processorId, count + 1);
            }
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, Integer> entry : succMap.entrySet()) {
            sb.append("processor [").append(entry.getKey()).append("]: ");
            sb.append(entry.getValue()).append(" cells rewritten\n");
        }

        sb.append("\n");

        for (Map.Entry<Integer, Integer> entry : failMap.entrySet()) {
            sb.append("processor [").append(entry.getKey()).append("]: ");
            sb.append(entry.getValue()).append(" cells were failed to rewrite\n");
        }

        return sb.toString();
    }
}
