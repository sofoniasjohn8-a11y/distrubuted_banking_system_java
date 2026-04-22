import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BankManagerGUI {

    private JFrame frame;
    private JTextField txtAccountId;
    private JTextField txtAmount;
    private BankManager bankManager;
    private String username;
    private String fullname;

    private static final Color PRIMARY     = new Color(30, 60, 114);
    private static final Color PRIMARY_END = new Color(42, 82, 152);
    private static final Color CARD_BG     = new Color(245, 248, 255);
    private static final Font  UI_FONT     = new Font("Segoe UI", Font.PLAIN, 13);

    private DatabaseManager dbManager = DatabaseManager.getInstance();

    public BankManagerGUI(BankManager bankManager, String username, String fullname) {
        this.bankManager = bankManager;
        this.username    = username;
        this.fullname    = fullname;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Bank Management System");
        frame.setSize(640, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel root = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PRIMARY, 0, getHeight(), PRIMARY_END));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setLayout(new BorderLayout(0, 20));
        root.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Bank Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome, " + fullname, SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcome.setForeground(new Color(200, 215, 240));

        // Logout button top-right
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.setBackground(new Color(200, 60, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(80, 30));
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { logoutBtn.setBackground(new Color(170, 40, 40)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { logoutBtn.setBackground(new Color(200, 60, 60)); }
        });
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new LoginGUI(bankManager));
            }
        });

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.CENTER);
        topRow.add(logoutBtn, BorderLayout.EAST);

        header.add(topRow, BorderLayout.CENTER);
        header.add(welcome, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // ── Card ─────────────────────────────────────────────────────────────
        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(210, 220, 240), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        // Input grid
        JPanel inputs = new JPanel(new GridLayout(2, 2, 15, 10));
        inputs.setOpaque(false);
        inputs.add(styledLabel("Account ID"));
        inputs.add(styledLabel("Amount ($)"));
        txtAccountId = styledField();
        txtAmount    = styledField();
        inputs.add(txtAccountId);
        inputs.add(txtAmount);
        card.add(inputs, BorderLayout.NORTH);

        // Check if user already has an account
        boolean hasAccount = dbManager.userHasAccount(username);

        // If user has account, pre-fill account ID field
        if (hasAccount) {
            txtAccountId.setText(dbManager.getAccountIdByUsername(username));
            txtAccountId.setEditable(false);
        }

        // Buttons — hide Create if user already has an account
        int cols = hasAccount ? 4 : 5;
        JPanel buttons = new JPanel(new GridLayout(1, cols, 10, 0));
        buttons.setOpaque(false);
        if (!hasAccount)
            buttons.add(actionButton("Create",   new Color(30, 150, 200), new Color(20, 130, 180), this::createAccount));
        buttons.add(actionButton("Deposit",  new Color(60, 179, 113), new Color(40, 159, 93),  this::deposit));
        buttons.add(actionButton("Withdraw", new Color(220, 80, 80),  new Color(190, 60, 60),  this::withdraw));
        buttons.add(actionButton("Transfer", new Color(230, 140, 30), new Color(200, 120, 20), this::transfer));
        buttons.add(actionButton("Balance",  new Color(120, 80, 200), new Color(100, 60, 180), this::checkBalance));
        card.add(buttons, BorderLayout.CENTER);

        // User info footer inside card
        String accountInfo = hasAccount ? "Account: " + dbManager.getAccountIdByUsername(username) : "No account yet";
        JLabel userInfo = new JLabel("Logged in as: " + username + "  |  " + accountInfo, SwingConstants.CENTER);
        userInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        userInfo.setForeground(new Color(120, 130, 160));
        card.add(userInfo, BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 80, 120));
        return lbl;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(UI_FONT);
        f.setPreferredSize(new Dimension(0, 36));
        f.setBorder(new CompoundBorder(
            new LineBorder(new Color(190, 205, 230), 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return f;
    }

    private JButton actionButton(String text, Color bg, Color hover, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 42));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        btn.addActionListener(action);
        return btn;
    }

    private String cleanError(Exception ex) {
        String msg = ex.getMessage();
        if (msg == null) return "An unexpected error occurred.";
        // Strip RMI wrapper prefix
        int idx = msg.lastIndexOf(": ");
        return (idx >= 0 && idx < msg.length() - 2) ? msg.substring(idx + 2) : msg;
    }

    private void createAccount(ActionEvent e) {
        String id = txtAccountId.getText().trim();
        String amtText = txtAmount.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an Account ID.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        if (amtText.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an initial balance amount.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        try {
            double amount = Double.parseDouble(amtText);
            if (amount < 0) { JOptionPane.showMessageDialog(frame, "Initial balance cannot be negative.", "Invalid Amount", JOptionPane.WARNING_MESSAGE); return; }
            bankManager.creerCompte(id, amount, username);
            txtAccountId.setText(id);
            txtAccountId.setEditable(false);
            JOptionPane.showMessageDialog(frame, "Account '" + id + "' created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Rebuild GUI to hide Create button
            frame.dispose();
            SwingUtilities.invokeLater(() -> new BankManagerGUI(bankManager, username, fullname));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, cleanError(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deposit(ActionEvent e) {
        String id = txtAccountId.getText().trim();
        String amtText = txtAmount.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an Account ID.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        if (amtText.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an amount to deposit.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        try {
            bankManager.ajouter(id, Double.parseDouble(amtText));
            JOptionPane.showMessageDialog(frame, "Amount deposited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, cleanError(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdraw(ActionEvent e) {
        String id = txtAccountId.getText().trim();
        String amtText = txtAmount.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an Account ID.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        if (amtText.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an amount to withdraw.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        try {
            bankManager.retirer(id, Double.parseDouble(amtText));
            JOptionPane.showMessageDialog(frame, "Amount withdrawn successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, cleanError(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transfer(ActionEvent e) {
        String amtText = txtAmount.getText().trim();
        if (amtText.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an amount to transfer.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        String src  = JOptionPane.showInputDialog(frame, "Enter source account ID:");
        if (src == null) return;
        String dest = JOptionPane.showInputDialog(frame, "Enter destination account ID:");
        if (dest == null) return;
        try {
            bankManager.transfererSolde(src.trim(), dest.trim(), Double.parseDouble(amtText));
            JOptionPane.showMessageDialog(frame, "Transfer completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, cleanError(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkBalance(ActionEvent e) {
        String id = txtAccountId.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter an Account ID.", "Missing Input", JOptionPane.WARNING_MESSAGE); return; }
        try {
            double balance = bankManager.consulterSolde(id);
            JOptionPane.showMessageDialog(frame,
                String.format("Account: %s%nBalance: $%.2f", id, balance),
                "Balance", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, cleanError(ex), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JFrame getFrame() { return frame; }
}
