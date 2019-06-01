package by.javaweb.multithreading.matrix;

import by.javaweb.multithreading.matrix.processing.MatrixWorker;
import java.util.concurrent.locks.ReentrantLock;

public enum Matrix {
    INSTANCE;

    private int height;
    private int width;
    private final ReentrantLock lock;

    private int[][] storage;

    private Matrix() {
        lock = new ReentrantLock();
        this.height = 0;
        this.width = 0;
        build();
    }

    public Matrix setSize(int height, int width) {
        this.height = height;
        this.width = width;
        build();

        return INSTANCE;
    }

    private void build() {
        storage = new int[getWidth()][getHeight()];
    }

    public void set(Cell cell, int value) {
        storage[cell.getXShift()][cell.getYShift()] = value;
    }

    public void set(int xPos, int yPos, int value) {
        storage[xPos][yPos] = value;
    }

    public int get(Cell cell) {
        return storage[cell.getXShift()][cell.getYShift()];
    }

    public int get(int xPos, int yPos) {
        return storage[xPos][yPos];
    }

    public void fillRandomly() {
        MatrixWorker.fillMatrixRandomly(this);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        try {
            lock.lock();
            StringBuilder collector = new StringBuilder();

            for (int i = 0; i < storage.length; i++) {
                for (int j = 0; j < storage[i].length; j++) {
                    collector.append(String.format("%3d", storage[i][j]));
                }
                collector.append("\n");
            }

            return collector.toString();
        } finally {
            lock.unlock();
        }
    }

}
