package edu.hitsz.application;

import edu.hitsz.dao.LeaderboardDao;
import edu.hitsz.model.ScoreRecord;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

public class LeaderboardDialog extends JDialog {

    private final LeaderboardDao leaderboardDao;
    private final ScoreRecord currentRecord;
    private final Runnable restartAction;
    private final JTextField nameField = new JTextField(18);
    private final JLabel messageLabel = new JLabel("Current run has not been saved yet.", SwingConstants.CENTER);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Rank", "Difficulty", "Player", "Score", "Time"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JButton saveButton = new JButton("Save Score");

    public LeaderboardDialog(
            Window owner,
            String resultText,
            LeaderboardDao leaderboardDao,
            ScoreRecord currentRecord,
            Runnable restartAction
    ) {
        super(owner, "Leaderboard", ModalityType.APPLICATION_MODAL);
        this.leaderboardDao = leaderboardDao;
        this.currentRecord = currentRecord;
        this.restartAction = restartAction;

        setLayout(new BorderLayout(10, 10));
        add(buildHeader(resultText), BorderLayout.NORTH);
        add(buildTablePane(), BorderLayout.CENTER);
        add(buildControlPane(), BorderLayout.SOUTH);

        refreshTable();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(760, 470);
        setLocationRelativeTo(owner);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                promptForPlayerName();
            }
        });
    }

    private JPanel buildHeader(String resultText) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        JLabel titleLabel = new JLabel(resultText + " - Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        String summaryText = String.format(
                "Current run: %s | score %d | time %s",
                currentRecord.getDifficulty(),
                currentRecord.getScore(),
                currentRecord.formatPlayedAt()
        );
        JLabel summaryLabel = new JLabel(summaryText, SwingConstants.CENTER);
        panel.add(summaryLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane buildTablePane() {
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(table);
    }

    private JPanel buildControlPane() {
        JPanel root = new JPanel(new BorderLayout(6, 6));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        savePanel.add(new JLabel("Player"));
        savePanel.add(nameField);

        saveButton.addActionListener(e -> saveCurrentScore());
        savePanel.add(saveButton);
        root.add(savePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedRecord());
        buttonPanel.add(deleteButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            dispose();
            if (restartAction != null) {
                restartAction.run();
            }
        });
        buttonPanel.add(restartButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        root.add(buttonPanel, BorderLayout.CENTER);
        root.add(messageLabel, BorderLayout.SOUTH);
        return root;
    }

    private void promptForPlayerName() {
        String suggestedName = nameField.getText().trim();
        Object input = JOptionPane.showInputDialog(
                this,
                "Enter player name for the current score:",
                "Save Current Score",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                suggestedName.isEmpty() ? "Player" : suggestedName
        );
        if (input != null) {
            nameField.setText(input.toString().trim());
        }
        nameField.requestFocusInWindow();
    }

    private void saveCurrentScore() {
        String playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a player name before saving.",
                    "Missing Player Name",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        ScoreRecord recordToSave = new ScoreRecord(
                currentRecord.getDifficulty(),
                playerName,
                currentRecord.getScore(),
                currentRecord.getPlayedAt()
        );

        try {
            leaderboardDao.insert(recordToSave);
            refreshTable();
            saveButton.setEnabled(false);
            messageLabel.setText("Saved current score for " + playerName + ".");
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select one leaderboard row before deleting.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the selected leaderboard entry?",
                "Delete Record",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (leaderboardDao.delete(selectedRow)) {
                refreshTable();
                messageLabel.setText("Deleted leaderboard entry #" + (selectedRow + 1) + ".");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The selected entry no longer exists.",
                        "Delete Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTable() {
        List<ScoreRecord> records;
        try {
            records = leaderboardDao.findAll();
        } catch (RuntimeException exception) {
            records = Collections.emptyList();
            messageLabel.setText("Leaderboard load failed: " + exception.getMessage());
        }

        tableModel.setRowCount(0);
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord record = records.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    record.getDifficulty(),
                    record.getPlayerName(),
                    record.getScore(),
                    record.formatPlayedAt()
            });
        }
    }
}
