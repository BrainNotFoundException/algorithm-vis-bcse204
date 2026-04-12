
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import utils.ArrGen;
import utils.BubbleSortPanel;
import utils.MergeSortPanel;
import utils.QuickSortPanel;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setLayout(new GridLayout(1, 3));
        frame.setMinimumSize(new Dimension(1800, 900));

        int[] arr = ArrGen.generateArray(100);
        frame.add(new BubbleSortPanel(arr));
        frame.add(new MergeSortPanel(arr));
        frame.add(new QuickSortPanel(arr));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
