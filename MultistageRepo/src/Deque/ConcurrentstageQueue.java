package Deque;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

interface StageQueue<T> {
    void enqueue(T item);
    T dequeue();
    int size();
}

class ConcurrentStageQueue<T> implements StageQueue<T> {
    private Deque<T> queue;

    public ConcurrentStageQueue() {
        this.queue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void enqueue(T item) {
        queue.offer(item);
    }

    @Override
    public T dequeue() {
        return queue.poll();
    }

    @Override
    public int size() {
        return queue.size();
    }
}

