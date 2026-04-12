
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import themes.Theme;
import utils.ArrGen;
import utils.BubbleSortPanel;
import utils.MergeSortPanel;
import utils.QuickSortPanel;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Sorting Algorithm Visualizer");

        UIManager.put("Panel.background", Theme.BG_Color);
        UIManager.put("Button.background", Theme.Button_Bg);
        UIManager.put("Button.foreground", Theme.Button_Fg);

        frame.setLayout(new BorderLayout());
        // frame.setLayout(new GridLayout(1, 3, 10, 10));
        frame.setMinimumSize(new Dimension(1800, 900));

        JPanel container = new JPanel(new GridLayout(1, 3, 20, 20));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setBackground(Theme.BG_Color);

        int[] arr = ArrGen.generateArray(100);
        BubbleSortPanel bpanel = new BubbleSortPanel(arr.clone());
        MergeSortPanel mpanel = new MergeSortPanel(arr.clone());
        QuickSortPanel qpanel = new QuickSortPanel(arr.clone());

        container.add(bpanel);
        container.add(mpanel);
        container.add(qpanel);

        frame.add(container, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        bpanel.startSorting();
        mpanel.startSorting();
        qpanel.startSorting();
    }

}
