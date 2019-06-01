package by.javaweb.multithreading.matrix.processing;

import by.javaweb.multithreading.matrix.Cell;
import java.util.concurrent.TimeUnit;

public class CellProcessor extends Thread {

    int id;
    Dispenser<Cell> source;
    Cell target;
    ReportCollector reportCollector;

    public CellProcessor(int id, Dispenser<Cell> source, ReportCollector reportCollector) {
        this.id = id;
        this.source = source;
        this.reportCollector = reportCollector;
    }

    private void process() {
        while ((target = source.getNext()) != null) {
            boolean successfullyLocked = false;
            try {
                if (successfullyLocked = target.tryLock() && !target.isChanged()) {
                    target.setValue(id);
                    reportSuccess();
                    TimeUnit.MILLISECONDS.sleep(1);
                } else {
                    reportFail();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                if (successfullyLocked) {
                    target.unlock();
                }
            }
        }
    }

    private void reportFail() {
        reportCollector.reportFail(id, target);
    }

    private void reportSuccess() {
        reportCollector.reportSuccess(id, target);
    }

    @Override
    public void run() {
        process();
    }

}
