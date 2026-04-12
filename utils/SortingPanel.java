package utils;

import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class SortingPanel extends JPanel {

    protected int[] arr;

    long startTime;
    long endTime;
    long runTime;

    JLabel timeLabel;

    public void setTimeLabel(JLabel timeLabel){
        this.timeLabel = timeLabel;
    }

    protected abstract void sort();

    protected void pause() {
        repaint();
        try {
            Thread.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        new Thread(() -> {

            startTime = System.nanoTime();
            sort();
            endTime = System.nanoTime();
            runTime = (endTime - startTime)/1000000;

            onSortingCompleted();

        }).start();
    }

    void onSortingCompleted(){
        if(timeLabel!=null){
            SwingUtilities.invokeLater(()->{
                long minutes = (long) runTime/(1000*60);
                long seconds = (long)(runTime/1000) - (minutes*60);
                timeLabel.setText("Total Time Taken: " + minutes + "mins and " + seconds + "s");
            });
        }
    }

    public SortingPanel(int[] arr) {
        this.arr = arr;
    }

}
