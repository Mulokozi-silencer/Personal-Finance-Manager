import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Transaction Class
class Transaction {
    private String type;
    private String category;
    private double amount;

    public Transaction(String type, String category, double amount) {
        this.type = type;
        this.category = category;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return type + " | " + category + " | " + amount;
    }
}

// Main App Class
public class FinanceManager {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JLabel totalIncomeLabel, totalExpenseLabel, balanceLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceManager::new);
    }

    public FinanceManager() {
        JFrame frame = new JFrame("Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel for Input
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
        JButton addButton = new JButton("Add Transaction");

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeCombo);

        // Transaction List Panel
        JPanel listPanel = new JPanel(new BorderLayout());
        JList<String> transactionList = new JList<>(listModel);
        transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(transactionList);

        JButton removeButton = new JButton("Remove Selected");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        listPanel.add(new JLabel("Transactions:"), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);
        listPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpenseLabel = new JLabel("Total Expense: $0.00");
        balanceLabel = new JLabel("Balance: $0.00");

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(balanceLabel);

        // Print Button
        JButton printButton = new JButton("Print Summary");
        JPanel printPanel = new JPanel(new FlowLayout());
        printPanel.add(printButton);

        // Add Panels to Frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listPanel, BorderLayout.CENTER);
        frame.add(summaryPanel, BorderLayout.WEST);
        frame.add(printPanel, BorderLayout.SOUTH);

        // Button Action: Add Transaction
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String type = typeCombo.getSelectedItem().toString();
                    String category = categoryField.getText();
                    double amount = Double.parseDouble(amountField.getText());

                    if (category.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Category cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Transaction transaction = new Transaction(type, category, amount);
                    transactions.add(transaction);
                    listModel.addElement(transaction.toString());

                    updateSummary();

                    // Clear Input Fields
                    amountField.setText("");
                    categoryField.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Button Action: Remove Transaction
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = transactionList.getSelectedIndex();
                if (selectedIndex != -1) {
                    transactions.remove(selectedIndex);  // Remove from the ArrayList
                    listModel.remove(selectedIndex);    // Remove from the UI list
                    updateSummary();
                } else {
                    JOptionPane.showMessageDialog(frame, "No transaction selected!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Button Action: Print Summary
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String summary = "Total Income: " + totalIncomeLabel.getText() + "\n" +
                        "Total Expense: " + totalExpenseLabel.getText() + "\n" +
                        "Balance: " + balanceLabel.getText();
                System.out.println(summary);
                JOptionPane.showMessageDialog(frame, summary, "Transaction Summary", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private void updateSummary() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("Income")) {
                totalIncome += transaction.getAmount();
            } else {
                totalExpense += transaction.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        totalIncomeLabel.setText("Total Income: $" + totalIncome);
        totalExpenseLabel.setText("Total Expense: $" + totalExpense);
        balanceLabel.setText("Balance: $" + balance);
    }
}
