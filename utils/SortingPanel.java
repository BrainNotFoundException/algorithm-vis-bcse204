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
    private volatile int delayMs = 5;
    private volatile boolean stopRequested = false;
    private Thread sortThread;

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public void setDelay(int ms) {
        this.delayMs = ms;
    }

    void updateTimeLabel() {
        if (timeLabel != null) {

            long timeSpentPaused = pausedTime;
            if (paused && pauseStart != 0) {
                timeSpentPaused += System.nanoTime() - pauseStart;
            }
            long updateTime = (System.nanoTime() - startTime - timeSpentPaused) / 1000000;

            SwingUtilities.invokeLater(() -> {
                timeLabel.setText("Time: " + updateTime + "ms");
            });
        }
    }

    protected abstract void sort();

    protected void pause() {
        updateTimeLabel();
        repaint();
        long target = System.currentTimeMillis() + delayMs;
        try {
            while (System.currentTimeMillis() < target || paused) {
                if (stopRequested) {
                    return;
                }
                Thread.sleep(paused ? 50 : 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected boolean isStopped() {
        return stopRequested;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = Math.max(getWidth() / arr.length, 1);
        for (int i = 0; i < arr.length; i++) {
            g.fillRect(i * width, getHeight() - arr[i], width, arr[i]);
        }
    }

    public void startSorting() {
        stopRequested = false;
        paused = false;
        sortThread = new Thread(() -> {
            startTime = System.nanoTime();
            sort();
            if (!stopRequested) {
                endTime = System.nanoTime();
                runTime = (endTime - startTime) / 1000000;
                onSortingCompleted();
            }
        });
        sortThread.setDaemon(true);
        sortThread.start();
    }

    public void restart() {
        stopRequested = true;
        paused = false;
        if (sortThread != null) {
            sortThread.interrupt();
            try {
                sortThread.join(200);
            } catch (InterruptedException ignored) {
            }
        }
        arr = originalArr.clone();
        if (timeLabel != null) {
            SwingUtilities.invokeLater(() -> timeLabel.setText("Time Taken"));
        }
        repaint();
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
        if (timeLabel != null) {
            SwingUtilities.invokeLater(()
                    -> {
                timeLabel.setText("Total Time: " + (runTime - pausedTime/1000000) + "ms");
            });
        }
    }

    public SortingPanel(int[] arr) {
        this.arr = arr;
        this.originalArr = arr.clone();

        this.pausedTime = 0;
    }

    public void resetAndStart(int[] newArr) {
        restart();
        this.arr = newArr.clone();
        this.originalArr = newArr.clone();
        repaint();
        startSorting();
    }
}
