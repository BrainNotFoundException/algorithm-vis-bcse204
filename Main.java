
import java.awt.*;
import javax.swing.*;
import themes.Theme;
import utils.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Sorting Algorithm Visualizer");

        int arrSize;
        while (true) {
            String input = JOptionPane.showInputDialog("Enter array size between 1 to 1500: ");
            if (input == null) {
                System.exit(0);
            }

            try {
                arrSize = Integer.parseInt(input);
                if (arrSize <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a positive integer.");
                } else if (arrSize > 1500) {
                    JOptionPane.showMessageDialog(null, "The value is too big, enter a smaller value.");
                } else {
                    break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer.");
            }
        }

        UIManager.put("Panel.background", Theme.BG_COLOR);
        UIManager.put("Button.background", Theme.BUTTON_BG);
        UIManager.put("Button.foreground", Theme.BUTTON_FG);

        frame.setLayout(new BorderLayout());
        // frame.setLayout(new GridLayout(1, 3, 10, 10));
        frame.setMinimumSize(new Dimension(1800, 900));

        JPanel container = new JPanel(new GridLayout(1, 3, 20, 20));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setBackground(Theme.BG_COLOR);

        int[] arr = ArrGen.generateArray(arrSize);
        BubbleSortPanel bpanel = new BubbleSortPanel(arr.clone());
        MergeSortPanel mpanel = new MergeSortPanel(arr.clone());
        QuickSortPanel qpanel = new QuickSortPanel(arr.clone());

        container.add(createCard("Bubble Sort", bpanel));
        container.add(createCard("Merge Sort", mpanel));
        container.add(createCard("Quick Sort", qpanel));

        frame.add(container, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        bpanel.startSorting();
        mpanel.startSorting();
        qpanel.startSorting();
    }

    public static JPanel createCard(String title, SortingPanel panel) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(30, 30, 30));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Theme.BUTTON_FG);
        label.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 16));

        JLabel timeLabel = new JLabel("Time Taken", SwingConstants.CENTER);
        timeLabel.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 16));
        timeLabel.setForeground(Theme.TIME_COLOR);

        panel.setTimeLabel(timeLabel);

        card.add(label, BorderLayout.NORTH);
        card.add(panel, BorderLayout.CENTER);
        card.add(timeLabel, BorderLayout.SOUTH);

        return card;
    }

}
