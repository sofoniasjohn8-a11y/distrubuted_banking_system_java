import javax.swing.*;
import java.awt.*;

public class LoginGUI {
    private JFrame frame;
    private BankManager bankManager;
    private DatabaseManager dbManager;

    private static final Color PRIMARY     = new Color(30, 60, 114);
    private static final Color PRIMARY_END = new Color(42, 82, 152);

    public LoginGUI(BankManager bankManager) {
        this.bankManager = bankManager;
        this.dbManager   = DatabaseManager.getInstance();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Bank Management System - Login");
        frame.setSize(500, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel main = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PRIMARY, 0, getHeight(), PRIMARY_END));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Bank Portal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(title);

        main.add(Box.createVerticalStrut(8));

        JLabel subtitle = new JLabel("Secure Banking at Your Fingertips");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(200, 215, 240));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(subtitle);

        main.add(Box.createVerticalStrut(35));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabs.setBackground(new Color(240, 240, 240));
        tabs.setForeground(PRIMARY);
        tabs.addTab("Login",    createLoginPanel());
        tabs.addTab("Register", createRegisterPanel());
        main.add(tabs);

        frame.setContentPane(main);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(245, 245, 245));

        JTextField usernameField = styledField();
        JPasswordField passwordField = new JPasswordField();
        stylePasswordField(passwordField);

        panel.add(fieldLabel("Username"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(fieldLabel("Password"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(25));

        JButton loginBtn = styledButton("LOGIN", new Color(30, 150, 200), new Color(20, 130, 180));
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.");
                return;
            }
            if (dbManager.authenticateUser(username, password)) {
                String fullname = dbManager.getFullname(username);
                frame.dispose();
                SwingUtilities.invokeLater(() -> new BankManagerGUI(bankManager, username, fullname));
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        panel.add(loginBtn);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(245, 245, 245));

        JTextField fullnameField = styledField();
        JTextField usernameField = styledField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField  = new JPasswordField();
        stylePasswordField(passwordField);
        stylePasswordField(confirmField);

        panel.add(fieldLabel("Full Name"));
        panel.add(fullnameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(fieldLabel("Username"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(fieldLabel("Password"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(fieldLabel("Confirm Password"));
        panel.add(confirmField);
        panel.add(Box.createVerticalStrut(25));

        JButton registerBtn = styledButton("REGISTER", new Color(100, 180, 80), new Color(80, 160, 60));
        registerBtn.addActionListener(e -> {
            String fullname  = fullnameField.getText().trim();
            String username  = usernameField.getText().trim();
            String password  = new String(passwordField.getPassword());
            String confirm   = new String(confirmField.getPassword());

            if (fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(frame, "Password must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                dbManager.registerUser(username, fullname, password);
                JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
                fullnameField.setText(""); usernameField.setText("");
                passwordField.setText(""); confirmField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(registerBtn);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        f.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        return f;
    }

    private void stylePasswordField(JPasswordField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        f.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }

    private JButton styledButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    public JFrame getFrame() { return frame; }
}
