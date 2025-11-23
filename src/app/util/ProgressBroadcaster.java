package app.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProgressBroadcaster {
    private static final ProgressBroadcaster instance = new ProgressBroadcaster();
    private final ConcurrentHashMap<Integer, List<PrintWriter>> listeners = new ConcurrentHashMap<>();

    private ProgressBroadcaster() {}

    public static ProgressBroadcaster getInstance() {
        return instance;
    }

    public void addListener(int jobId, PrintWriter writer) {
        listeners.computeIfAbsent(jobId, k -> new CopyOnWriteArrayList<>()).add(writer);
    }

    public void removeListener(int jobId, PrintWriter writer) {
        List<PrintWriter> list = listeners.get(jobId);
        if (list != null) {
            list.remove(writer);
        }
    }

    public void publish(int jobId, String data) {
        List<PrintWriter> list = listeners.get(jobId);
        if (list == null || list.isEmpty()) return;
        for (PrintWriter w : list) {
            try {
                w.write("data: " + data + "\n\n");
                w.flush();
            } catch (Exception e) {
                // Remove broken listeners
                removeListener(jobId, w);
            }
        }
    }
}
