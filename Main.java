
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;
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

    static JLabel[] statusLabels = new JLabel[3];
    static JLabel[] timeLabels = new JLabel[3];
    static JPanel[] rowPanels = new JPanel[3];

    static AtomicInteger finishCounter = new AtomicInteger(0);
    static boolean[] finished = new boolean[3];

    static Timer liveTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int arrSize = showStartupDialog(null);
            if (arrSize < 1) {
                System.exit(0);
            }
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

        JLabel subtitle = new JLabel("Choose an array size between 1 and 1,000,000");
        subtitle.setForeground(Theme.TIME_COLOR);
        subtitle.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(1, 1000000, 300);
        slider.setBackground(Theme.BG_COLOR);
        slider.setForeground(Theme.BUTTON_FG);
        slider.setMajorTickSpacing(100000);
        slider.setMinorTickSpacing(10000);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JTextField field = new JTextField("300", 8);
        field.setBackground(Theme.FIELD_BG);
        field.setForeground(Theme.BUTTON_FG);
        field.setCaretColor(Theme.BUTTON_FG);
        field.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setMaximumSize(new Dimension(120, 32));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Theme.ERROR_FG);
        errorLabel.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 11));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        slider.addChangeListener(e -> field.setText(String.valueOf(slider.getValue())));

        field.getDocument().addDocumentListener(new DocumentListener() {
            void sync() {
                try {
                    int v = Integer.parseInt(field.getText().trim());
                    if (v >= 1 && v <= 1000000) {
                        slider.setValue(v);
                        errorLabel.setText(" ");
                    } else {
                        errorLabel.setText("Must be between 1 and 1,000,000");
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Enter a valid integer");
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                sync();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sync();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                sync();
            }
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
                if (v >= 1 && v <= 1000000) {
                    result[0] = v;
                    dialog.dispose();
                } else {
                    errorLabel.setText("Must be between 1 and 1,000,000");
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
        dialog.setLocationRelativeTo(owner);
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

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(Theme.TOOLBAR_BG);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));

        JButton restartBtn = makeToolbarBtn("Restart");
        JButton newArrayBtn = makeToolbarBtn("New Array");
        pauseBtn = makeToolbarBtn("Pause");

        toolbar.add(restartBtn);
        toolbar.add(newArrayBtn);
        toolbar.add(pauseBtn);

        int[] arr = ArrGen.generateArray(arrSize);
        bpanel = new BubbleSortPanel(arr.clone());
        mpanel = new MergeSortPanel(arr.clone());
        qpanel = new QuickSortPanel(arr.clone());
        bpanel.setVisible(false);
        mpanel.setVisible(false);
        qpanel.setVisible(false);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(Theme.BG_COLOR);

        JPanel tableCard = buildTableCard();
        centerWrap.add(tableCard);

        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(centerWrap, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        resetFinishState();
        wireCallbacks();
        startAllSorting();
        startLiveTimer();

        restartBtn.addActionListener(e -> {
            pauseBtn.setText("Pause");
            stopLiveTimer();
            resetFinishState();
            resetRowStyles();
            clearTimeLabelTexts();
            bpanel.restart();
            mpanel.restart();
            qpanel.restart();
            wireCallbacks();
            startAllSorting();
            startLiveTimer();
        });

        newArrayBtn.addActionListener(e -> {
            int size = showStartupDialog(frame);
            if (size < 1) {
                return;
            }
            int[] fresh = ArrGen.generateArray(size);
            pauseBtn.setText("Pause");
            stopLiveTimer();
            resetFinishState();
            resetRowStyles();
            clearTimeLabelTexts();
            bpanel.resetAndStart(fresh.clone());
            mpanel.resetAndStart(fresh.clone());
            qpanel.resetAndStart(fresh.clone());
            wireCallbacks();
            startLiveTimer();
        });

        pauseBtn.addActionListener(e -> {
            boolean nowPaused = !bpanel.isPaused();
            bpanel.setPaused(nowPaused);
            mpanel.setPaused(nowPaused);
            qpanel.setPaused(nowPaused);
            pauseBtn.setText(nowPaused ? "Resume" : "Pause");
        });
    }

    static JPanel buildTableCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));
        card.setPreferredSize(new Dimension(1100, 520));

        JLabel heading = new JLabel("Sorting Algorithm Race");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 26));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(heading);
        card.add(Box.createVerticalStrut(6));

        JLabel subheading = new JLabel("Live runtime comparison");
        subheading.setForeground(new Color(120, 120, 120));
        subheading.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 12));
        subheading.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subheading);
        card.add(Box.createVerticalStrut(20));

        card.add(buildHeaderRow());
        card.add(Box.createVerticalStrut(8));
        card.add(makeDivider());
        card.add(Box.createVerticalStrut(8));

        String[][] rows = {
            {"Bubble Sort", "O(n²)", "Comparison-based, in-place"},
            {"Merge Sort", "O(n log n)", "Divide & conquer, stable"},
            {"Quick Sort", "O(n log n)", "Pivot-based, in-place"},};

        for (int i = 0; i < 3; i++) {
            JPanel row = buildAlgoRow(i, rows[i][0], rows[i][1], rows[i][2]);
            rowPanels[i] = row;
            card.add(row);
            if (i < 2) {
                card.add(Box.createVerticalStrut(6));
            }
        }

        card.add(Box.createVerticalStrut(16));
        card.add(makeDivider());
        card.add(Box.createVerticalStrut(12));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        legend.setBackground(Theme.CARD_BG);
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);
        legend.add(legendDot(Theme.TIME_COLOR, "Running"));
        legend.add(legendDot(Theme.WINNER_COLOR, "Winner"));
        legend.add(legendDot(new Color(160, 160, 160), "Completed"));
        card.add(legend);

        return card;
    }

    static JPanel buildHeaderRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
        row.setBackground(Theme.CARD_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] cols = {"Algorithm", "Complexity", "Description", "Live Time"};
        for (String col : cols) {
            JLabel lbl = new JLabel(col);
            lbl.setForeground(new Color(100, 100, 100));
            lbl.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 11));
            row.add(lbl);
        }
        return row;
    }

    static JPanel buildAlgoRow(int idx, String name, String complexity, String desc) {
        JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
        row.setBackground(Theme.CARD_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 45), 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        namePanel.setBackground(row.getBackground());
        JLabel dot = new JLabel("*");
        dot.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 10));
        dot.setForeground(Theme.TIME_COLOR);
        statusLabels[idx] = dot;

        JLabel nameLbl = new JLabel(name);
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 18));
        namePanel.add(dot);
        namePanel.add(nameLbl);

        JLabel complexityLbl = new JLabel(complexity);
        complexityLbl.setForeground(new Color(180, 140, 255));
        complexityLbl.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 16));

        JLabel descLbl = new JLabel(desc);
        descLbl.setForeground(new Color(130, 130, 130));
        descLbl.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 14));

        JLabel timeLbl = new JLabel("—");
        timeLbl.setForeground(Theme.TIME_COLOR);
        timeLbl.setFont(new Font("JetBrainsMono NFP", Font.BOLD, 18));
        timeLabels[idx] = timeLbl;

        row.add(namePanel);
        row.add(complexityLbl);
        row.add(descLbl);
        row.add(timeLbl);
        return row;
    }

    static JSeparator makeDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    static JPanel legendDot(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Theme.CARD_BG);
        JLabel dot = new JLabel("*");
        dot.setForeground(color);
        dot.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 10));
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(130, 130, 130));
        lbl.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 11));
        p.add(dot);
        p.add(lbl);
        return p;
    }

    static void startLiveTimer() {
        liveTimer = new Timer(50, (ActionEvent e) -> {
            SortingPanel[] panels = {bpanel, mpanel, qpanel};
            for (int i = 0; i < 3; i++) {
                if (!finished[i]) {
                    long ms = panels[i].getElapsedMs();
                    timeLabels[i].setText(ms + " ms");
                }
            }
        });
        liveTimer.start();
    }

    static void stopLiveTimer() {
        if (liveTimer != null) {
            liveTimer.stop();
        }
    }

    static void resetFinishState() {
        finishCounter.set(0);
        finished = new boolean[3];
    }

    static void wireCallbacks() {
        SortingPanel[] panels = {bpanel, mpanel, qpanel};
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            panels[i].setOnCompleteCallback(() -> onAlgoFinished(idx, panels[idx]));
        }
    }

    static void onAlgoFinished(int idx, SortingPanel panel) {
        long ms = panel.getFinalRuntime();
        finished[idx] = true;

        timeLabels[idx].setText(ms + " ms");
        timeLabels[idx].setForeground(new Color(160, 160, 160));
        statusLabels[idx].setForeground(new Color(160, 160, 160));
        statusLabels[idx].setText("done");

        int count = finishCounter.incrementAndGet();
        if (count == 1) {
            markWinner(idx, ms);
        }
        if (count == 3) {
            stopLiveTimer();
        }
    }

    static void markWinner(int idx, long ms) {
        rowPanels[idx].setBackground(Theme.WINNER_BG);
        for (Component c : rowPanels[idx].getComponents()) {
            c.setBackground(Theme.WINNER_BG);
            if (c instanceof JPanel jPanel) {
                for (Component cc : jPanel.getComponents()) {
                    cc.setBackground(Theme.WINNER_BG);
                }
            }
        }
        rowPanels[idx].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.WINNER_COLOR, 2),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        statusLabels[idx].setForeground(Theme.WINNER_COLOR);
        statusLabels[idx].setText("WIN");
        timeLabels[idx].setForeground(Theme.WINNER_COLOR);
        timeLabels[idx].setText(ms + " ms");
        timeLabels[idx].setFont(new Font("JetBrainsMono NFP", Font.BOLD, 18));
    }

    static void resetRowStyles() {
        for (int i = 0; i < 3; i++) {
            if (rowPanels[i] == null) {
                continue;
            }
            rowPanels[i].setBackground(Theme.CARD_BG);
            for (Component c : rowPanels[i].getComponents()) {
                c.setBackground(Theme.CARD_BG);
                if (c instanceof JPanel jPanel) {
                    for (Component cc : jPanel.getComponents()) {
                        cc.setBackground(Theme.CARD_BG);
                    }
                }
            }
            rowPanels[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(45, 45, 45), 1),
                    BorderFactory.createEmptyBorder(12, 14, 12, 14)));
            statusLabels[i].setText("*");
            statusLabels[i].setForeground(Theme.TIME_COLOR);
            timeLabels[i].setForeground(Theme.TIME_COLOR);
            timeLabels[i].setFont(new Font("JetBrainsMono NFP", Font.BOLD, 18));
        }
    }

    static void clearTimeLabelTexts() {
        for (JLabel lbl : timeLabels) {
            if (lbl != null) {
                lbl.setText("—");
            }
        }
    }

    static void startAllSorting() {
        bpanel.startSorting();
        mpanel.startSorting();
        qpanel.startSorting();
    }

    static JButton makeToolbarBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("JetBrainsMono NFP", Font.PLAIN, 13));
        btn.setBackground(Theme.BUTTON_BG);
        btn.setForeground(Theme.BUTTON_FG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        return btn;
    }
}
