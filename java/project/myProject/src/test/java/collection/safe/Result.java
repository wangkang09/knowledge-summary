package collection.safe;

import java.util.List;
import java.util.Queue;

public class Result {
    private long timestamp;
    private Queue<String> sequences;
    private Status status;

    public Result() {

    }

    public Result(long timestamp, Queue<String> sequences, Status status) {
        this.timestamp = timestamp;
        this.sequences = sequences;
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Queue<String> getSequences() {
        return sequences;
    }

    public void setSequences(Queue<String> sequences) {
        this.sequences = sequences;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
