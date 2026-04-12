import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import utils.ArrGen;

public class Main{

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setLayout(new GridLayout(1, 3));
        frame.setMinimumSize(new Dimension(600, 300));

        int[] baseArray = ArrGen.generateArray(100);
        for(int i=0; i<100; i++){
            System.out.print(baseArray[i] + " ");
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}