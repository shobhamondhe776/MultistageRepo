package Deque;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiStageProcessing {

    private StageQueue<Item>[] stages;
    private ExecutorService executor;

    @SuppressWarnings("unchecked")
    public MultiStageProcessing(int numStages, int numThreads) {
        stages = new ConcurrentStageQueue[numStages];
        for (int i = 0; i < numStages; i++) {
            stages[i] = new ConcurrentStageQueue<>();
        }
        executor = Executors.newFixedThreadPool(numThreads);
    }

    public void addItemToStage(Item item, int stage) {
        stages[stage].enqueue(item);
    }

    public void startProcessing() {
        for (int i = 0; i < stages.length; i++) {
            final int stageIndex = i;
            executor.submit(() -> processStage(stageIndex));
        }
    }

    private void processStage(int stageIndex) {
        StageQueue<Item> stageQueue = stages[stageIndex];
        while (true) {
            Item item = stageQueue.dequeue();
            if (item != null) {
                System.out.println("Processing item " + item.getItem() + " at stage " + stageIndex);
                item.setStage(stageIndex + 1);
                if (stageIndex + 1 < stages.length) {
                    stages[stageIndex + 1].enqueue(item);
                }
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static void main(String[] args) {
        MultiStageProcessing processing = new MultiStageProcessing(3, 3);

        // Assuming Item class and its constructor exist
        processing.addItemToStage(new Item("item1", 0, 1, "description1"), 0);
        processing.addItemToStage(new Item("item2", 0, 2, "description2"), 0);
        processing.addItemToStage(new Item("item3", 0, 1, "description3"), 0);

        processing.startProcessing();
    }
}
