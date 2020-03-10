package no.ntnu.idi.apollo69framework.network_messages;

public class PlayerInQueue {
    private int position;
    private int queueSize;

    public PlayerInQueue() {
    }

    public PlayerInQueue(int position, int queueSize) {
        this.position = position;
        this.queueSize = queueSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
