import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import themes.Theme;
import utils.*;

public class Main {

    static BubbleSortPanel bpanel;
    static MergeSortPanel mpanel;
    static QuickSortPanel qpanel;
    static JButton pauseBtn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int arrSize = showStartupDialog(null);
            if (arrSize < 1) System.exit(0);
            buildMainWindow(arrSize);
        });
    }

    static int showStartupDialog(JFrame owner) {
        JDialog dialog = new JDialog(owner, "Array Size", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        JLabel title = new JLabel("Configure Visualizer");
        title.setForeground(Theme.BUTTON_FG);
        title.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose an array size between 1 and 1500");
        subtitle.setForeground(Theme.TIME_COLOR);
        subtitle.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(1, 1500, 300);
        slider.setBackground(Theme.BG_COLOR);
        slider.setForeground(Theme.BUTTON_FG);
        slider.setMajorTickSpacing(500);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JTextField field = new JTextField("300", 8);
        field.setBackground(Theme.FIELD_BG);
        field.setForeground(Theme.BUTTON_FG);
        field.setCaretColor(Theme.BUTTON_FG);
        field.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setMaximumSize(new Dimension(120, 32));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Theme.ERROR_FG);
        errorLabel.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 11));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        slider.addChangeListener(e ->
            field.setText(String.valueOf(slider.getValue()))
        );

        field.getDocument().addDocumentListener(new DocumentListener() {
            void sync() {
                try {
                    int v = Integer.parseInt(field.getText().trim());
                    if (v >= 1 && v <= 1500) {
                        slider.setValue(v);
                        errorLabel.setText(" ");
                    } else {
                        errorLabel.setText("Must be between 1 and 1500");
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Enter a valid integer");
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) { sync(); }
            @Override
            public void removeUpdate(DocumentEvent e) { sync(); }
            @Override
            public void changedUpdate(DocumentEvent e) { sync(); }
        });

        int[] result = {-1};

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Theme.BUTTON_BG);
        cancelBtn.setForeground(Theme.BUTTON_FG);
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton goBtn = new JButton("Visualize!");
        goBtn.setBackground(Theme.BUTTON_BG);
        goBtn.setForeground(Theme.BUTTON_FG);
        goBtn.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 13));
        goBtn.addActionListener(e -> {
            try {
                int v = Integer.parseInt(field.getText().trim());
                if (v >= 1 && v <= 1500) {
                    result[0] = v;
                    dialog.dispose();
                } else {
                    errorLabel.setText("Must be between 1 and 1500");
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Enter a valid integer");
            }
        });

        dialog.getRootPane().setDefaultButton(goBtn);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setBackground(Theme.BG_COLOR);
        btnRow.add(cancelBtn);
        btnRow.add(goBtn);

        panel.add(title);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(18));
        panel.add(slider);
        panel.add(Box.createVerticalStrut(10));
        panel.add(field);
        panel.add(Box.createVerticalStrut(4));
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnRow);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(1800, 900));
        dialog.setVisible(true);

        return result[0];
    }

    static void buildMainWindow(int arrSize) {
        UIManager.put("Panel.background", Theme.BG_COLOR);
        UIManager.put("Button.background", Theme.BUTTON_BG);
        UIManager.put("Button.foreground", Theme.BUTTON_FG);

        JFrame frame = new JFrame("Sorting Algorithm Visualizer");
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(1800, 900));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        toolbar.setBackground(Theme.TOOLBAR_BG);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));

        JButton restartBtn = new JButton("Restart");
        JButton newArrayBtn = new JButton("New Array");
        pauseBtn = new JButton("Pause");

        for (JButton b : new JButton[]{restartBtn, newArrayBtn, pauseBtn}) {
            b.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 13));
            toolbar.add(b);
        }

        toolbar.add(Box.createHorizontalStrut(16));
        JLabel speedLabel = new JLabel("Swap Delay:");
        speedLabel.setForeground(Theme.BUTTON_FG);
        speedLabel.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 12));
        toolbar.add(speedLabel);

        JSlider speedSlider = new JSlider(0, 50, 5);
        speedSlider.setBackground(Theme.TOOLBAR_BG);
        speedSlider.setPreferredSize(new Dimension(160, 28));
        toolbar.add(speedSlider);

        JLabel speedVal = new JLabel("5 ms");
        speedVal.setForeground(Theme.TIME_COLOR);
        speedVal.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 12));
        toolbar.add(speedVal);

        int[] arr = ArrGen.generateArray(arrSize);
        bpanel = new BubbleSortPanel(arr.clone());
        mpanel = new MergeSortPanel(arr.clone());
        qpanel = new QuickSortPanel(arr.clone());

        JPanel container = new JPanel(new GridLayout(1, 3, 20, 20));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setBackground(Theme.BG_COLOR);
        container.add(createCard("Bubble Sort", bpanel));
        container.add(createCard("Merge Sort", mpanel));
        container.add(createCard("Quick Sort", qpanel));

        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(container, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        bpanel.startSorting();
        mpanel.startSorting();
        qpanel.startSorting();

        restartBtn.addActionListener(e -> {
            pauseBtn.setText("Pause");
            bpanel.restart(); mpanel.restart(); qpanel.restart();
            bpanel.startSorting(); mpanel.startSorting(); qpanel.startSorting();
        });

        newArrayBtn.addActionListener(e -> {
            int size = showStartupDialog(frame);
            if (size < 1) return;
            int[] fresh = ArrGen.generateArray(size);
            pauseBtn.setText("Pause");
            bpanel.resetAndStart(fresh.clone());
            mpanel.resetAndStart(fresh.clone());
            qpanel.resetAndStart(fresh.clone());
        });

        pauseBtn.addActionListener(e -> {
            boolean nowPaused = !bpanel.isPaused();
            bpanel.setPaused(nowPaused);
            mpanel.setPaused(nowPaused);
            qpanel.setPaused(nowPaused);
            pauseBtn.setText(nowPaused ? "Resume" : "Pause");
        });

        speedSlider.addChangeListener(e -> {
            int delay = speedSlider.getValue();
            speedVal.setText(delay + " ms");
            bpanel.setDelay(delay);
            mpanel.setDelay(delay);
            qpanel.setDelay(delay);
        });
    }

    public static JPanel createCard(String title, SortingPanel panel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Theme.BUTTON_FG);
        label.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 16));

        JLabel timeLabel = new JLabel("Time: Running...", SwingConstants.CENTER);
        timeLabel.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 16));
        timeLabel.setForeground(Theme.TIME_COLOR);

        panel.setTimeLabel(timeLabel);

        card.add(label, BorderLayout.NORTH);
        card.add(panel, BorderLayout.CENTER);
        card.add(timeLabel, BorderLayout.SOUTH);
        return card;
    }
}