package by.javaweb.multithreading.matrix.processing;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Dispenser<T> {

    List<T> storage;
    ReentrantLock lock;
    int currentElem = 0;

    public Dispenser(List<T> storage) {
        this.storage = storage;
        lock = new ReentrantLock(true);
    }

    public T getNext() {
        try {
            lock.lock();

            if (hasNext()) {
                return storage.get(currentElem++);
            }
        } finally {
            lock.unlock();
        }

        return null;
    }

    // There is no point in synchronizing this method, even though pause between using
    // hasnext() and getNext() will be minimal, some other thread can get inside this gap
    // and sneak an element from dispencer. So we use this only in one thread with methods
    // that call it. E.g. this can't be used in this current task.
    public boolean hasNext() {
        return currentElem < this.storage.size();
    }

}
