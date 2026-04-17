package utils;

import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class SortingPanel extends JPanel {

    protected int[] arr;
    private int[] originalArr;

    long startTime, endTime, runTime, pauseStart, pausedTime;
    JLabel timeLabel;

    private volatile boolean paused = false;
    // private volatile int delayMs = 5;
    //private volatile boolean stopRequested = false;
    private Thread sortThread;

    private Runnable onCompleteCallback;

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    // public void setDelay(int ms) {
    //     this.delayMs = ms;
    // }
    public void setOnCompleteCallback(Runnable callback) {
        this.onCompleteCallback = callback;
    }

    public long getElapsedMs() {
        if (startTime == 0) {
            return 0;
        }
        long timeSpentPaused = pausedTime;
        if (paused && pauseStart != 0) {
            timeSpentPaused += System.nanoTime() - pauseStart;
        }
        return (System.nanoTime() - startTime - timeSpentPaused) / 1_000_000;
    }

    public long getFinalRuntime() {
        return runTime - pausedTime / 1_000_000;
    }

    // void updateTimeLabel() {
    //     if (timeLabel != null) {
    //         long elapsed = getElapsedMs();
    //         SwingUtilities.invokeLater(() -> timeLabel.setText("Run Time: " + elapsed + " ms"));
    //     }
    // }
    protected abstract void sort();

    protected void pause() {
        //updateTimeLabel();
        repaint();
        // long target = System.currentTimeMillis() + delayMs;
        // try {
        //     while (System.currentTimeMillis() < target || paused) {
        //         if (stopRequested) return;
        //         Thread.sleep(paused ? 50 : 1);
        //     }
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }
        try {
            while (paused) {
                if (stopped) {
                    return;
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    volatile boolean stopped = false;

    protected boolean isStopped() {
        return stopped;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void startSorting() {
        stopped = false;
        paused = false;
        pausedTime = 0;
        pauseStart = 0;
        sortThread = new Thread(() -> {
            startTime = System.nanoTime();
            sort();
            if (!stopped) {
                endTime = System.nanoTime();
                runTime = (endTime - startTime) / 1_000_000;
                onSortingCompleted();
            }
        });
        sortThread.setDaemon(true);
        sortThread.start();
    }

    public void restart() {
        stopped = true;
        paused = false;
        arr = originalArr.clone();
        startTime = 0;
        runTime = 0;
        pausedTime = 0;
        pauseStart = 0;
        repaint();
        startSorting();
    }

    public void setPaused(boolean p) {
        if (p && !paused) {
            pauseStart = System.nanoTime();
        }
        if (!p && paused) {
            pausedTime += System.nanoTime() - pauseStart;
        }
        this.paused = p;
    }

    public boolean isPaused() {
        return paused;
    }

    void onSortingCompleted() {
        long finalMs = getFinalRuntime();
        if (timeLabel != null) {
            SwingUtilities.invokeLater(() -> timeLabel.setText("Final Time: " + finalMs + " ms"));
        }
        if (onCompleteCallback != null) {
            SwingUtilities.invokeLater(onCompleteCallback);
        }
    }

    public SortingPanel(int[] arr) {
        this.arr = arr;
        this.originalArr = arr.clone();
        this.pausedTime = 0;
    }

    public void resetAndStart(int[] newArr) {
        stopped = true;
        paused = false;
        arr = newArr.clone();
        originalArr = newArr.clone();
        startTime = 0;
        runTime = 0;
        pausedTime = 0;
        pauseStart = 0;
        repaint();
        startSorting();
    }
}
