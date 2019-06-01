package by.javaweb.multithreading.matrix;

import java.util.concurrent.locks.ReentrantLock;

public class Cell {

    private final int xShift;
    private final int yShift;

    private final Matrix matrix;
    private final ReentrantLock lock;

    private boolean changed;

    public Cell(Matrix matrix, int xShift, int yShift) {
        this.matrix = matrix;
        this.xShift = xShift;
        this.yShift = yShift;

        lock = new ReentrantLock();
    }

    public int getXShift() {
        return xShift;
    }

    public int getYShift() {
        return yShift;
    }

    public void setValue(int value) {
        try {
            lock.lock();

            matrix.set(this, value);
            changed = true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void lock() {
        lock.lock();
    }

    public boolean tryLock() {
        return lock.tryLock();
    }

    public void unlock() {
        lock.unlock();
    }

    public int getValue() {
        return matrix.get(this);
    }

}
