package utils;

import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class SortingPanel extends JPanel {

    protected int[] arr;

    protected abstract void sort();

    protected void pause() {
        repaint();
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth() / arr.length;

        for (int i = 0; i < arr.length; i++) {
            g.fillRect(i * width, getHeight() - arr[i], width, arr[i]);
        }
    }

    public void startSorting() {
        new Thread(() -> sort()).start();
    }

    public SortingPanel(int[] arr) {
        this.arr = arr;
    }

}
