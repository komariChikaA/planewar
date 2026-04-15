package edu.hitsz.application;

import edu.hitsz.model.ScoreRecord;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.List;

public class LeaderboardDialog extends JDialog {

    public LeaderboardDialog(Window owner, String resultText, List<ScoreRecord> records, Runnable restartAction) {
        super(owner, "总排行榜", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel(resultText + "，总排行榜已更新", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"名次", "难度", "玩家名", "得分", "时间"};
        Object[][] rowData = new Object[records.size()][columnNames.length];
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord record = records.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = record.getDifficulty();
            rowData[i][2] = record.getPlayerName();
            rowData[i][3] = record.getScore();
            rowData[i][4] = record.formatPlayedAt();
        }

        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        JButton restartButton = new JButton("重新开始");
        restartButton.addActionListener(e -> {
            dispose();
            if (restartAction != null) {
                restartAction.run();
            }
        });

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(restartButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 420);
        setLocationRelativeTo(owner);
    }
}
