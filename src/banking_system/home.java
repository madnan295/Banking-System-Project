/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package banking_system;

import dataBase.connection;
import java.awt.Color;
import static java.awt.image.ImageObserver.HEIGHT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mohammad Adnan
 */
public class home extends javax.swing.JFrame {

    /**
     * Creates new form Main
     *
     * @param accNo
     */
    String accNO;

    public home(String accNo) {
        initComponents();
        saveButton2.setVisible(false);
        recieverPanel1.setVisible(false);
        accNO = accNo;
        if (home.getUserData(accNO)) {

                setTableData(userTransactionData);

                edditButton2.setEnabled(true);
                depositButton2.setEnabled(true);
                withdrawButton3.setEnabled(true);
                nextButton4.setEnabled(true);
                nextButton4.setVisible(true);
                //profile
                nameTextField3.setText(userData.get("name"));
                genderTextField1.setText(userData.get("gender"));
                nationalityTextField3.setText(userData.get("nationality"));
                dateOfBirthTextField4.setText(userData.get("date_of_birth"));
                accTypeTextField4.setText(userData.get("account_type"));
                accNoTextField5.setText(userData.get("account_no"));
                phNoTextField5.setText(userData.get("phone_no"));
                addressTextField3.setText(userData.get("address"));
                //deposit
                nameTextField5.setText(userData.get("name"));
                accNoTextField4.setText(userData.get("account_no"));
                if (userData.get("balance") == null || "".equals(userData.get("balance"))) {
                    availableBalanceTextField6.setText("0");
                } else {
                    availableBalanceTextField6.setText(userData.get("balance"));
                }
                //withdraw
                nameTextField6.setText(userData.get("name"));
                accNoTextField7.setText(userData.get("account_no"));
                if (userData.get("balance") == null || "".equals(userData.get("balance"))) {
                    availableBalanceTextField8.setText("0");
                } else {
                    availableBalanceTextField8.setText(userData.get("balance"));
                }
                //transfer
                nameTextField9.setText(userData.get("name"));
                accNoTextField10.setText(userData.get("account_no"));
                if (userData.get("balance") == null || "".equals(userData.get("balance"))) {
                    availableBalanceTextField11.setText("0");
                } else {
                    availableBalanceTextField11.setText(userData.get("balance"));
                }
                //available balance
                nameTextField8.setText(userData.get("name"));
                accNoTextField11.setText(userData.get("account_no"));
                if (userData.get("balance") == null || "".equals(userData.get("balance"))) {
                    accNoTextField12.setText("0");
                } else {
                    accNoTextField12.setText(userData.get("balance"));
                }
            }
    }

    static Map<String, String> userData = new HashMap();

    private static boolean getUserData(String account_no) {
        try {
            ResultSet rs;
            Connection con = connection.connect();
            PreparedStatement pst;
            String sqlQuery = "SELECT * from users WHERE account_no =? ";
            pst = con.prepareStatement(sqlQuery);
            pst.setString(1, account_no);
            rs = pst.executeQuery();

            if (rs.next()) {

               

                    userData.put("id", rs.getString("id"));
                    userData.put("name", rs.getString("name"));
                    userData.put("account_no", rs.getString("account_no"));
                    userData.put("password", rs.getString("password"));
                    userData.put("phone_no", rs.getString("phone_no"));
                    userData.put("account_type", rs.getString("account_type"));
                    userData.put("gender", rs.getString("gender"));
                    userData.put("address", rs.getString("address"));
                    userData.put("nationality", rs.getString("nationality"));
                    userData.put("date_of_birth", rs.getString("date_of_birth"));
                    userData.put("balance", rs.getString("balance"));
                    getUserTransactionsData(userData.get("account_no"));
                    return true;

                

            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    static ArrayList<Map<String, String>> userTransactionData = new ArrayList<>();

    private static void getUserTransactionsData(String account_no) {
        userTransactionData.clear();
        try {
            ResultSet rs;
            Connection con = connection.connect();
            PreparedStatement pst;
            String sqlQuery = "SELECT * from users_transactions WHERE `user_id` = ? ";
            pst = con.prepareStatement(sqlQuery);
            pst.setString(1, account_no);
            rs = pst.executeQuery();

            while (rs.next()) {
                Map<String, String> transaction = new HashMap<>();

                transaction.put("id", rs.getString("id"));
                transaction.put("user_id", rs.getString("user_id"));
                transaction.put("to_name", rs.getString("to_name"));
                transaction.put("to_ac", rs.getString("to_ac"));
                transaction.put("from_name", rs.getString("from_name"));
                transaction.put("from_ac", rs.getString("from_ac"));
                transaction.put("payment_type", rs.getString("payment_type"));
                transaction.put("date", rs.getString("date"));
                transaction.put("ammount", rs.getString("ammount"));

                userTransactionData.add(transaction);
            }

        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setTableData(ArrayList<Map<String, String>> transactions) {

        DefaultTableModel model = (DefaultTableModel) transactionTable2.getModel();
        System.out.println("Table Data : " + transactionTable2.getRowCount());
        System.out.println("new transactions : " + transactions.size());
        for (int i = transactionTable2.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (Map<String, String> transaction : transactions) {
            model.addRow(new Object[]{
                transaction.get("payment_type"),
                transaction.get("from_ac"),
                transaction.get("from_name"),
                transaction.get("to_ac"),
                transaction.get("to_name"),
                transaction.get("ammount"),
                transaction.get("date"),});
        }

    }

    public void deposit(String ammount) {
        double balance = 0.0;

        if (userData.get("balance") != null) {
            balance = Double.parseDouble(userData.get("balance"));
        }
        double depositAmmount = Double.parseDouble(ammount) + balance;

        try {
            try ( Connection con = connection.connect()) {
                PreparedStatement pst;
                String updateQuery = "UPDATE users SET balance = ? WHERE account_no = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, String.valueOf(depositAmmount));
                pst.setString(2, String.valueOf(accNO));

                pst.executeUpdate();

            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String formattedDate = formatter.format(LocalDateTime.now());
            storeTransactions(accNO, "System", "System", userData.get("name"), userData.get("account_no"), ammount, formattedDate, "Deposit");
            JOptionPane.showMessageDialog(rootPane, "Ammount deposited\nsuccessfully", "Deposit Ammount", JOptionPane.OK_OPTION);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went worng!, Deposit Unsuccessfull", "Ammount Deposit", HEIGHT);
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void withdraw(String ammount) {
        double balance = 0.0;

        if (userData.get("balance") != null) {
            balance = Double.parseDouble(userData.get("balance"));
        }
        double withdrawAmmount = balance - Double.parseDouble(ammount);

        try {
            try ( Connection con = connection.connect()) {
                PreparedStatement pst;
                String updateQuery = "UPDATE users SET balance = ? WHERE account_no = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, String.valueOf(withdrawAmmount));
                pst.setString(2, String.valueOf(accNO));

                pst.executeUpdate();

            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String formattedDate = formatter.format(LocalDateTime.now());
            storeTransactions(accNO, userData.get("account_no"), userData.get("name"), "", "", ammount, formattedDate, "Withdraw");
            JOptionPane.showMessageDialog(rootPane, "Ammount Withdrawn\nsuccessfully", "Ammount Withdraw", JOptionPane.OK_OPTION);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went worng!, Withdrawn Unsuccessfull", "Ammount Withdraw", HEIGHT);
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transfer(String ammount, String toAccNo) {
        double OurBalance = 0.0;
        double RecieverBalance = 0.0;
        String toName = "";

        if (userData.get("balance") != null) {
            OurBalance = Double.parseDouble(userData.get("balance"));
        }

        double OurBalanceRemaing = OurBalance - Double.parseDouble(ammount);
        if (OurBalanceRemaing <= 0) {
            JOptionPane.showMessageDialog(rootPane, "Insufficient amount!, Transffer Unsuccessfull", "Ammount Transfer", HEIGHT);
        }
        try {
            try ( Connection con = connection.connect()) {

                ResultSet rs;
                PreparedStatement pst;

                String getRecieverQuery = "SELECT name,balance from users WHERE account_no = ? ";
                pst = con.prepareStatement(getRecieverQuery);
                pst.setString(1, String.valueOf(toAccNo));
                rs = pst.executeQuery();

                if (rs.next()) {
                    toName = rs.getString("name");
                    RecieverBalance = Double.parseDouble(rs.getString("balance"));
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Something went worng!, Transfer Unsuccessfull", "Ammount Transfer", HEIGHT);
                    return;
                }

                String updateQuery = "UPDATE users SET balance = ? WHERE account_no = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, String.valueOf(OurBalanceRemaing));
                pst.setString(2, String.valueOf(accNO));
                pst.executeUpdate();

                String updateRecieverQuery = "UPDATE users SET balance = ? WHERE account_no = ?";
                pst = con.prepareStatement(updateRecieverQuery);
                pst.setString(1, String.valueOf(RecieverBalance + Double.parseDouble(ammount)));
                pst.setString(2, String.valueOf(toAccNo));
                pst.executeUpdate();

            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String formattedDate = formatter.format(LocalDateTime.now());
            storeTransactions(accNO, accNO, userData.get("name"), toName, toAccNo, ammount, formattedDate, "Transfer");
            storeTransactions(toAccNo, accNO, userData.get("name"), toName, toAccNo, ammount, formattedDate, "Received");
            JOptionPane.showMessageDialog(rootPane, "Ammount Transfered\nsuccessfully", "Ammount Transfer", JOptionPane.OK_OPTION);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went worng!, Transfer Unsuccessfull", "Ammount Transfer", HEIGHT);
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void storeTransactions(String user_id, String from_ac, String from_name, String to_name, String to_ac, String ammount, String date, String payment_type) {
        try {

            try ( Connection con = connection.connect()) {
                PreparedStatement pst;

                String insertQuery = "INSERT INTO users_transactions (user_id, to_name, to_ac, from_ac, from_name,ammount, payment_type, date) VALUES "
                        + "("
                        + "'" + user_id + "',"
                        + "'" + to_name + "',"
                        + "'" + to_ac + "',"
                        + "'" + from_ac + "',"
                        + "'" + from_name + "',"
                        + "'" + ammount + "',"
                        + "'" + payment_type + "',"
                        + "'" + date + "'"
                        + ")";
                pst = con.prepareStatement(insertQuery);
                pst.execute();
                con.close();
                if(!"Received".equals(payment_type)){
                    getUserTransactionsData(user_id);
                    setTableData(userTransactionData);
                }
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went worng!", "Store transaction", HEIGHT);
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void edditProfile(String name, String gender, String nationality, String date_of_birth, String acc_tpe, String phNo, String address) {

        try {
            try ( Connection con = connection.connect()) {
                PreparedStatement pst;
                String updateQuery = "UPDATE users SET name = ?,gender = ?,nationality = ?,date_of_birth = ?,account_type = ?,phone_no = ?,address = ? WHERE account_no = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, String.valueOf(name));
                pst.setString(2, String.valueOf(gender));
                pst.setString(3, String.valueOf(nationality));
                pst.setString(4, String.valueOf(date_of_birth));
                pst.setString(5, String.valueOf(acc_tpe));
                pst.setString(6, String.valueOf(phNo));
                pst.setString(7, String.valueOf(address));
                pst.setString(8, String.valueOf(accNO));

                pst.executeUpdate();

            }
       edditButton2.setVisible(true);
        saveButton2.setVisible(false);
        nameTextField3.setEditable(false);
        genderTextField1.setEditable(false);
        nationalityTextField3.setEditable(false);
        dateOfBirthTextField4.setEditable(false);
        accTypeTextField4.setEditable(false);
        phNoTextField5.setEditable(false);
        addressTextField3.setEditable(false);

        nameTextField3.setBackground(new Color(241, 241, 250));
        nameTextField3.setForeground(new Color( 153, 153, 153));

        genderTextField1.setBackground(new Color(241, 241, 250));
        genderTextField1.setForeground(new Color( 153, 153, 153));

        nationalityTextField3.setBackground(new Color(241, 241, 250));
        nationalityTextField3.setForeground(new Color( 153, 153, 153));

        dateOfBirthTextField4.setBackground(new Color(241, 241, 250));
        dateOfBirthTextField4.setForeground(new Color( 153, 153, 153));

        accTypeTextField4.setBackground(new Color(241, 241, 250));
        accTypeTextField4.setForeground(new Color( 153, 153, 153));

        phNoTextField5.setBackground(new Color(241, 241, 250));
        phNoTextField5.setForeground(new Color( 153, 153, 153));

        addressTextField3.setBackground(new Color(241, 241, 250));
        addressTextField3.setForeground(new Color( 153, 153, 153));
        
                       
                        JOptionPane.showMessageDialog(rootPane, "Profile updated\nsuccessfully", "Eddit Profile", JOptionPane.OK_OPTION);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Something went worng!, Profile can't be updated", "Eddit Profile", HEIGHT);
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        MainDesign = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        profilePanel1 = new javax.swing.JPanel();
        nameText1 = new javax.swing.JLabel();
        nameTextField3 = new javax.swing.JTextField();
        accNoText = new javax.swing.JLabel();
        accTypeTextField4 = new javax.swing.JTextField();
        genderTextField1 = new javax.swing.JTextField();
        phNoText = new javax.swing.JLabel();
        phNoTextField5 = new javax.swing.JTextField();
        accTypeText = new javax.swing.JLabel();
        genderText = new javax.swing.JLabel();
        addresstext1 = new javax.swing.JLabel();
        addressTextField3 = new javax.swing.JTextField();
        edditButton2 = new javax.swing.JButton();
        saveButton2 = new javax.swing.JButton();
        accNoTextField5 = new javax.swing.JTextField();
        genderText1 = new javax.swing.JLabel();
        passwordTextField2 = new javax.swing.JTextField();
        nationalityText2 = new javax.swing.JLabel();
        nationalityTextField3 = new javax.swing.JTextField();
        dateOfBirthTextField4 = new javax.swing.JTextField();
        dateOfBirthText3 = new javax.swing.JLabel();
        depositPanel2 = new javax.swing.JPanel();
        accNoTextField4 = new javax.swing.JTextField();
        accNoText2 = new javax.swing.JLabel();
        nameText3 = new javax.swing.JLabel();
        nameTextField5 = new javax.swing.JTextField();
        availableBalanceText3 = new javax.swing.JLabel();
        availableBalanceTextField6 = new javax.swing.JTextField();
        depositAmmountText4 = new javax.swing.JLabel();
        depositAmmountTextField7 = new javax.swing.JTextField();
        totalAmmountText5 = new javax.swing.JLabel();
        totalAmmountTextField8 = new javax.swing.JTextField();
        depositButton2 = new javax.swing.JButton();
        withdrawPanel3 = new javax.swing.JPanel();
        nameTextField6 = new javax.swing.JTextField();
        nameText4 = new javax.swing.JLabel();
        accNoText3 = new javax.swing.JLabel();
        accNoTextField7 = new javax.swing.JTextField();
        availableBalanceText4 = new javax.swing.JLabel();
        availableBalanceTextField8 = new javax.swing.JTextField();
        depositAmmountText5 = new javax.swing.JLabel();
        withdrawlAmmountTextField8 = new javax.swing.JTextField();
        remainningBalanceText6 = new javax.swing.JLabel();
        remainingBalanceTextField9 = new javax.swing.JTextField();
        withdrawButton3 = new javax.swing.JButton();
        transferPanel4 = new javax.swing.JPanel();
        senderPanel2 = new javax.swing.JPanel();
        transferAmmountText7 = new javax.swing.JLabel();
        availableBalanceText7 = new javax.swing.JLabel();
        accNoText6 = new javax.swing.JLabel();
        nameText7 = new javax.swing.JLabel();
        nameTextField9 = new javax.swing.JTextField();
        accNoTextField10 = new javax.swing.JTextField();
        availableBalanceTextField11 = new javax.swing.JTextField();
        transferAmmountTextField10 = new javax.swing.JTextField();
        remainningBalanceText8 = new javax.swing.JLabel();
        remainingBalanceTextField12 = new javax.swing.JTextField();
        nextButton4 = new javax.swing.JButton();
        recieverPanel1 = new javax.swing.JPanel();
        transferAmmountText6 = new javax.swing.JLabel();
        recieverAccNoText4 = new javax.swing.JLabel();
        recieverText5 = new javax.swing.JLabel();
        recieverNameTextField7 = new javax.swing.JTextField();
        recieverAccNoTextField9 = new javax.swing.JTextField();
        transferAmmountTextField9 = new javax.swing.JTextField();
        transferButton6 = new javax.swing.JButton();
        availableBalancePanel5 = new javax.swing.JPanel();
        nameText6 = new javax.swing.JLabel();
        nameTextField8 = new javax.swing.JTextField();
        accNoText5 = new javax.swing.JLabel();
        accNoTextField11 = new javax.swing.JTextField();
        availableBalanceText6 = new javax.swing.JLabel();
        accNoTextField12 = new javax.swing.JTextField();
        transactionPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        transactionTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Banking Management System");
        setResizable(false);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(934, 594));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MainDesign.setBackground(new java.awt.Color(255, 255, 255));
        MainDesign.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 51, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        profilePanel1.setBackground(new java.awt.Color(255, 204, 204));
        profilePanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        profilePanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameText1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nameText1.setText("Name: ");
        profilePanel1.add(nameText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, -1, 27));

        nameTextField3.setEditable(false);
        nameTextField3.setBackground(new java.awt.Color(241, 241, 250));
        nameTextField3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nameTextField3.setForeground(new java.awt.Color(153, 153, 153));
        nameTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        nameTextField3.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        nameTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextField3FocusLost(evt);
            }
        });
        nameTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextField3ActionPerformed(evt);
            }
        });
        profilePanel1.add(nameTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 190, -1));

        accNoText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        accNoText.setText("Accout  NO:");
        profilePanel1.add(accNoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, -1, 27));

        accTypeTextField4.setEditable(false);
        accTypeTextField4.setBackground(new java.awt.Color(241, 241, 250));
        accTypeTextField4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        accTypeTextField4.setForeground(new java.awt.Color(153, 153, 153));
        accTypeTextField4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accTypeTextField4.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        profilePanel1.add(accTypeTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, 190, -1));

        genderTextField1.setEditable(false);
        genderTextField1.setBackground(new java.awt.Color(241, 241, 250));
        genderTextField1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        genderTextField1.setForeground(new java.awt.Color(153, 153, 153));
        genderTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        genderTextField1.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        profilePanel1.add(genderTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, -1));

        phNoText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        phNoText.setText("Phone No:");
        profilePanel1.add(phNoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, 27));

        phNoTextField5.setEditable(false);
        phNoTextField5.setBackground(new java.awt.Color(241, 241, 250));
        phNoTextField5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        phNoTextField5.setForeground(new java.awt.Color(153, 153, 153));
        phNoTextField5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        phNoTextField5.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        phNoTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                phNoTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                phNoTextField5FocusLost(evt);
            }
        });
        phNoTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phNoTextField5ActionPerformed(evt);
            }
        });
        profilePanel1.add(phNoTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, 190, -1));

        accTypeText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        accTypeText.setText("Account Type:");
        profilePanel1.add(accTypeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 70, -1, 27));

        genderText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        genderText.setText("Gender:");
        profilePanel1.add(genderText, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 27));

        addresstext1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        addresstext1.setText("Address:");
        profilePanel1.add(addresstext1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 280, -1, 27));

        addressTextField3.setEditable(false);
        addressTextField3.setBackground(new java.awt.Color(241, 241, 250));
        addressTextField3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        addressTextField3.setForeground(new java.awt.Color(153, 153, 153));
        addressTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addressTextField3.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        addressTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                addressTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                addressTextField3FocusLost(evt);
            }
        });
        profilePanel1.add(addressTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 280, 190, -1));

        edditButton2.setBackground(new java.awt.Color(255, 51, 255));
        edditButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        edditButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/edit.png"))); // NOI18N
        edditButton2.setText("Edit");
        edditButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edditButton2.setEnabled(false);
        edditButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edditButton2ActionPerformed(evt);
            }
        });
        profilePanel1.add(edditButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 190, 30));

        saveButton2.setBackground(new java.awt.Color(255, 51, 255));
        saveButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        saveButton2.setForeground(new java.awt.Color(255, 255, 255));
        saveButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/save.png"))); // NOI18N
        saveButton2.setText("Save");
        saveButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveButton2.setEnabled(false);
        saveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButton2ActionPerformed(evt);
            }
        });
        profilePanel1.add(saveButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 190, 30));

        accNoTextField5.setEditable(false);
        accNoTextField5.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        accNoTextField5.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField5.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        profilePanel1.add(accNoTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 190, -1));

        genderText1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        genderText1.setText("Gender:");
        profilePanel1.add(genderText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, 27));

        passwordTextField2.setEditable(false);
        passwordTextField2.setBackground(new java.awt.Color(241, 241, 250));
        passwordTextField2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        passwordTextField2.setForeground(new java.awt.Color(204, 204, 204));
        passwordTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        profilePanel1.add(passwordTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, -1));

        nationalityText2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nationalityText2.setText("Nationality");
        profilePanel1.add(nationalityText2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 27));

        nationalityTextField3.setEditable(false);
        nationalityTextField3.setBackground(new java.awt.Color(241, 241, 250));
        nationalityTextField3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nationalityTextField3.setForeground(new java.awt.Color(153, 153, 153));
        nationalityTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        nationalityTextField3.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        profilePanel1.add(nationalityTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 190, -1));

        dateOfBirthTextField4.setEditable(false);
        dateOfBirthTextField4.setBackground(new java.awt.Color(241, 241, 250));
        dateOfBirthTextField4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        dateOfBirthTextField4.setForeground(new java.awt.Color(153, 153, 153));
        dateOfBirthTextField4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dateOfBirthTextField4.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        profilePanel1.add(dateOfBirthTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 190, -1));

        dateOfBirthText3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        dateOfBirthText3.setText("Date of Birth");
        profilePanel1.add(dateOfBirthText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, -1, 27));

        jTabbedPane1.addTab("Profile", profilePanel1);

        depositPanel2.setBackground(new java.awt.Color(255, 204, 204));
        depositPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        accNoTextField4.setEditable(false);
        accNoTextField4.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        accNoTextField4.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accNoTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                accNoTextField4FocusLost(evt);
            }
        });
        accNoTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accNoTextField4ActionPerformed(evt);
            }
        });
        depositPanel2.add(accNoTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 190, -1));

        accNoText2.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        accNoText2.setText("Account NO:");
        depositPanel2.add(accNoText2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, -1, 27));

        nameText3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        nameText3.setText("Name: ");
        depositPanel2.add(nameText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, -1, 27));

        nameTextField5.setEditable(false);
        nameTextField5.setBackground(new java.awt.Color(241, 241, 250));
        nameTextField5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        nameTextField5.setForeground(new java.awt.Color(153, 153, 153));
        nameTextField5.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nameTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextField5FocusLost(evt);
            }
        });
        nameTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextField5ActionPerformed(evt);
            }
        });
        depositPanel2.add(nameTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 190, -1));

        availableBalanceText3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        availableBalanceText3.setText("Available Balance:");
        depositPanel2.add(availableBalanceText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, -1, 27));

        availableBalanceTextField6.setEditable(false);
        availableBalanceTextField6.setBackground(new java.awt.Color(241, 241, 250));
        availableBalanceTextField6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        availableBalanceTextField6.setForeground(new java.awt.Color(153, 153, 153));
        availableBalanceTextField6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        availableBalanceTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                availableBalanceTextField6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                availableBalanceTextField6FocusLost(evt);
            }
        });
        availableBalanceTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBalanceTextField6ActionPerformed(evt);
            }
        });
        depositPanel2.add(availableBalanceTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, 190, -1));

        depositAmmountText4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        depositAmmountText4.setText("Deposit Ammount");
        depositPanel2.add(depositAmmountText4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, -1, 27));

        depositAmmountTextField7.setBackground(new java.awt.Color(241, 241, 250));
        depositAmmountTextField7.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        depositAmmountTextField7.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        depositAmmountTextField7.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                depositAmmountTextField7CaretUpdate(evt);
            }
        });
        depositAmmountTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                depositAmmountTextField7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                depositAmmountTextField7FocusLost(evt);
            }
        });
        depositAmmountTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depositAmmountTextField7ActionPerformed(evt);
            }
        });
        depositPanel2.add(depositAmmountTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 190, -1));

        totalAmmountText5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        totalAmmountText5.setText("Total Ammount");
        depositPanel2.add(totalAmmountText5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, -1, 27));

        totalAmmountTextField8.setEditable(false);
        totalAmmountTextField8.setBackground(new java.awt.Color(241, 241, 250));
        totalAmmountTextField8.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        totalAmmountTextField8.setForeground(new java.awt.Color(153, 153, 153));
        totalAmmountTextField8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        totalAmmountTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                totalAmmountTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                totalAmmountTextField8FocusLost(evt);
            }
        });
        totalAmmountTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalAmmountTextField8ActionPerformed(evt);
            }
        });
        depositPanel2.add(totalAmmountTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 190, -1));

        depositButton2.setBackground(new java.awt.Color(255, 51, 255));
        depositButton2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        depositButton2.setForeground(new java.awt.Color(255, 255, 255));
        depositButton2.setText("Deposit");
        depositButton2.setEnabled(false);
        depositButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depositButton2ActionPerformed(evt);
            }
        });
        depositPanel2.add(depositButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 190, 30));

        jTabbedPane1.addTab("Deposit", depositPanel2);

        withdrawPanel3.setBackground(new java.awt.Color(255, 204, 204));
        withdrawPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameTextField6.setEditable(false);
        nameTextField6.setBackground(new java.awt.Color(241, 241, 250));
        nameTextField6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        nameTextField6.setForeground(new java.awt.Color(153, 153, 153));
        nameTextField6.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nameTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextField6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextField6FocusLost(evt);
            }
        });
        nameTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextField6ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(nameTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 190, -1));

        nameText4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        nameText4.setText("Name: ");
        withdrawPanel3.add(nameText4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, 27));

        accNoText3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        accNoText3.setText("Account NO:");
        withdrawPanel3.add(accNoText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, 27));

        accNoTextField7.setEditable(false);
        accNoTextField7.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField7.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        accNoTextField7.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accNoTextField7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                accNoTextField7FocusLost(evt);
            }
        });
        accNoTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accNoTextField7ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(accNoTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 190, -1));

        availableBalanceText4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        availableBalanceText4.setText("Available Balance:");
        withdrawPanel3.add(availableBalanceText4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, 27));

        availableBalanceTextField8.setEditable(false);
        availableBalanceTextField8.setBackground(new java.awt.Color(241, 241, 250));
        availableBalanceTextField8.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        availableBalanceTextField8.setForeground(new java.awt.Color(153, 153, 153));
        availableBalanceTextField8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        availableBalanceTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                availableBalanceTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                availableBalanceTextField8FocusLost(evt);
            }
        });
        availableBalanceTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBalanceTextField8ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(availableBalanceTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, 190, -1));

        depositAmmountText5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        depositAmmountText5.setText("Withdrawl Ammount:");
        withdrawPanel3.add(depositAmmountText5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, -1, 27));

        withdrawlAmmountTextField8.setBackground(new java.awt.Color(241, 241, 250));
        withdrawlAmmountTextField8.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        withdrawlAmmountTextField8.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        withdrawlAmmountTextField8.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                withdrawlAmmountTextField8CaretUpdate(evt);
            }
        });
        withdrawlAmmountTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                withdrawlAmmountTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                withdrawlAmmountTextField8FocusLost(evt);
            }
        });
        withdrawlAmmountTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withdrawlAmmountTextField8ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(withdrawlAmmountTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 190, -1));

        remainningBalanceText6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        remainningBalanceText6.setText("Remaining Balance:");
        withdrawPanel3.add(remainningBalanceText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, -1, 27));

        remainingBalanceTextField9.setEditable(false);
        remainingBalanceTextField9.setBackground(new java.awt.Color(241, 241, 250));
        remainingBalanceTextField9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        remainingBalanceTextField9.setForeground(new java.awt.Color(153, 153, 153));
        remainingBalanceTextField9.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        remainingBalanceTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remainingBalanceTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                remainingBalanceTextField9FocusLost(evt);
            }
        });
        remainingBalanceTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remainingBalanceTextField9ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(remainingBalanceTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 190, -1));

        withdrawButton3.setBackground(new java.awt.Color(255, 51, 255));
        withdrawButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        withdrawButton3.setForeground(new java.awt.Color(255, 255, 255));
        withdrawButton3.setText("Withdraw");
        withdrawButton3.setEnabled(false);
        withdrawButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withdrawButton3ActionPerformed(evt);
            }
        });
        withdrawPanel3.add(withdrawButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 190, 30));

        jTabbedPane1.addTab("Withdraw", withdrawPanel3);

        senderPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        transferAmmountText7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        transferAmmountText7.setText("Transfer Ammount:");
        senderPanel2.add(transferAmmountText7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, -1, 27));

        availableBalanceText7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        availableBalanceText7.setText("Available Balance:");
        senderPanel2.add(availableBalanceText7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, 27));

        accNoText6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        accNoText6.setText("Account NO:");
        senderPanel2.add(accNoText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, 27));

        nameText7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        nameText7.setText("Name: ");
        senderPanel2.add(nameText7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, 27));

        nameTextField9.setEditable(false);
        nameTextField9.setBackground(new java.awt.Color(241, 241, 250));
        nameTextField9.setForeground(new java.awt.Color(153, 153, 153));
        nameTextField9.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nameTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextField9FocusLost(evt);
            }
        });
        nameTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextField9ActionPerformed(evt);
            }
        });
        senderPanel2.add(nameTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 190, -1));

        accNoTextField10.setEditable(false);
        accNoTextField10.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField10.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accNoTextField10FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                accNoTextField10FocusLost(evt);
            }
        });
        accNoTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accNoTextField10ActionPerformed(evt);
            }
        });
        senderPanel2.add(accNoTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 190, -1));

        availableBalanceTextField11.setEditable(false);
        availableBalanceTextField11.setBackground(new java.awt.Color(241, 241, 250));
        availableBalanceTextField11.setForeground(new java.awt.Color(153, 153, 153));
        availableBalanceTextField11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        availableBalanceTextField11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                availableBalanceTextField11FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                availableBalanceTextField11FocusLost(evt);
            }
        });
        availableBalanceTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBalanceTextField11ActionPerformed(evt);
            }
        });
        senderPanel2.add(availableBalanceTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, 190, -1));

        transferAmmountTextField10.setBackground(new java.awt.Color(241, 241, 250));
        transferAmmountTextField10.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        transferAmmountTextField10.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                transferAmmountTextField10CaretUpdate(evt);
            }
        });
        transferAmmountTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                transferAmmountTextField10FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                transferAmmountTextField10FocusLost(evt);
            }
        });
        transferAmmountTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferAmmountTextField10ActionPerformed(evt);
            }
        });
        senderPanel2.add(transferAmmountTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 190, -1));

        remainningBalanceText8.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        remainningBalanceText8.setText("Remaining Balance:");
        senderPanel2.add(remainningBalanceText8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, -1, 27));

        remainingBalanceTextField12.setEditable(false);
        remainingBalanceTextField12.setBackground(new java.awt.Color(241, 241, 250));
        remainingBalanceTextField12.setForeground(new java.awt.Color(153, 153, 153));
        remainingBalanceTextField12.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        remainingBalanceTextField12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remainingBalanceTextField12FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                remainingBalanceTextField12FocusLost(evt);
            }
        });
        remainingBalanceTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remainingBalanceTextField12ActionPerformed(evt);
            }
        });
        senderPanel2.add(remainingBalanceTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 190, -1));

        nextButton4.setBackground(new java.awt.Color(255, 51, 255));
        nextButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        nextButton4.setForeground(new java.awt.Color(255, 255, 255));
        nextButton4.setText("Next");
        nextButton4.setEnabled(false);
        nextButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButton4ActionPerformed(evt);
            }
        });
        senderPanel2.add(nextButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 340, 190, 30));

        recieverPanel1.setBackground(new java.awt.Color(255, 204, 204));
        recieverPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        transferAmmountText6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        transferAmmountText6.setText("Transfer Ammount:");
        recieverPanel1.add(transferAmmountText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, -1, 27));

        recieverAccNoText4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        recieverAccNoText4.setText("Reciever's Account NO:");
        recieverPanel1.add(recieverAccNoText4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, -1, 27));

        recieverText5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        recieverText5.setText("Reciever's Name: ");
        recieverPanel1.add(recieverText5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, -1, 27));

        recieverNameTextField7.setEditable(false);
        recieverNameTextField7.setBackground(new java.awt.Color(241, 241, 250));
        recieverNameTextField7.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        recieverNameTextField7.setForeground(new java.awt.Color(153, 153, 153));
        recieverNameTextField7.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        recieverNameTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                recieverNameTextField7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                recieverNameTextField7FocusLost(evt);
            }
        });
        recieverNameTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recieverNameTextField7ActionPerformed(evt);
            }
        });
        recieverPanel1.add(recieverNameTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 190, -1));

        recieverAccNoTextField9.setBackground(new java.awt.Color(241, 241, 250));
        recieverAccNoTextField9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        recieverAccNoTextField9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        recieverAccNoTextField9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                recieverAccNoTextField9CaretUpdate(evt);
            }
        });
        recieverAccNoTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                recieverAccNoTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                recieverAccNoTextField9FocusLost(evt);
            }
        });
        recieverAccNoTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recieverAccNoTextField9ActionPerformed(evt);
            }
        });
        recieverPanel1.add(recieverAccNoTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 190, -1));

        transferAmmountTextField9.setEditable(false);
        transferAmmountTextField9.setBackground(new java.awt.Color(241, 241, 250));
        transferAmmountTextField9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        transferAmmountTextField9.setForeground(new java.awt.Color(153, 153, 153));
        transferAmmountTextField9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        transferAmmountTextField9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                transferAmmountTextField9CaretUpdate(evt);
            }
        });
        transferAmmountTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                transferAmmountTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                transferAmmountTextField9FocusLost(evt);
            }
        });
        transferAmmountTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferAmmountTextField9ActionPerformed(evt);
            }
        });
        recieverPanel1.add(transferAmmountTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 190, 190, -1));

        transferButton6.setBackground(new java.awt.Color(255, 51, 255));
        transferButton6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        transferButton6.setForeground(new java.awt.Color(255, 255, 255));
        transferButton6.setText("Transfer");
        transferButton6.setEnabled(false);
        transferButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferButton6ActionPerformed(evt);
            }
        });
        recieverPanel1.add(transferButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, 190, 30));

        javax.swing.GroupLayout transferPanel4Layout = new javax.swing.GroupLayout(transferPanel4);
        transferPanel4.setLayout(transferPanel4Layout);
        transferPanel4Layout.setHorizontalGroup(
            transferPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(recieverPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
            .addGroup(transferPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(senderPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE))
        );
        transferPanel4Layout.setVerticalGroup(
            transferPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(recieverPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
            .addGroup(transferPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(senderPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transfer", transferPanel4);

        availableBalancePanel5.setBackground(new java.awt.Color(255, 204, 204));
        availableBalancePanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameText6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nameText6.setText("Name: ");
        availableBalancePanel5.add(nameText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, -1, 27));

        nameTextField8.setEditable(false);
        nameTextField8.setBackground(new java.awt.Color(241, 241, 250));
        nameTextField8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nameTextField8.setForeground(new java.awt.Color(153, 153, 153));
        nameTextField8.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nameTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextField8FocusLost(evt);
            }
        });
        nameTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextField8ActionPerformed(evt);
            }
        });
        availableBalancePanel5.add(nameTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 190, -1));

        accNoText5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        accNoText5.setText("Account NO:");
        availableBalancePanel5.add(accNoText5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, -1, 27));

        accNoTextField11.setEditable(false);
        accNoTextField11.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        accNoTextField11.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accNoTextField11FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                accNoTextField11FocusLost(evt);
            }
        });
        accNoTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accNoTextField11ActionPerformed(evt);
            }
        });
        availableBalancePanel5.add(accNoTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 190, -1));

        availableBalanceText6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        availableBalanceText6.setText("Available Balance:");
        availableBalancePanel5.add(availableBalanceText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, -1, 27));

        accNoTextField12.setEditable(false);
        accNoTextField12.setBackground(new java.awt.Color(241, 241, 250));
        accNoTextField12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        accNoTextField12.setForeground(new java.awt.Color(153, 153, 153));
        accNoTextField12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        accNoTextField12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accNoTextField12FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                accNoTextField12FocusLost(evt);
            }
        });
        accNoTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accNoTextField12ActionPerformed(evt);
            }
        });
        availableBalancePanel5.add(accNoTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 190, 190, -1));

        jTabbedPane1.addTab("Available Balance", availableBalancePanel5);

        transactionTable2.setBackground(new java.awt.Color(255, 204, 204));
        transactionTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Name", "Account No.", "Reciever's \nAccount No.", "Reciever's Account Name", "Ammount ", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transactionTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        transactionTable2.setEnabled(false);
        transactionTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(transactionTable2);
        if (transactionTable2.getColumnModel().getColumnCount() > 0) {
            transactionTable2.getColumnModel().getColumn(2).setPreferredWidth(30);
            transactionTable2.getColumnModel().getColumn(3).setPreferredWidth(30);
            transactionTable2.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        javax.swing.GroupLayout transactionPanel6Layout = new javax.swing.GroupLayout(transactionPanel6);
        transactionPanel6.setLayout(transactionPanel6Layout);
        transactionPanel6Layout.setHorizontalGroup(
            transactionPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 992, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        transactionPanel6Layout.setVerticalGroup(
            transactionPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("Transaction", transactionPanel6);

        MainDesign.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 30, 900, 520));

        jButton1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 51));
        jButton1.setText("LOG OUT");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        MainDesign.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, -10, -1, 40));

        jPanel10.add(MainDesign, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 896, 560));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        new login().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void accNoTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accNoTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField12ActionPerformed

    private void accNoTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField12FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField12FocusLost

    private void accNoTextField12FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField12FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField12FocusGained

    private void accNoTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accNoTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField11ActionPerformed

    private void accNoTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField11FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField11FocusLost

    private void accNoTextField11FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField11FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField11FocusGained

    private void nameTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField8ActionPerformed

    private void nameTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField8FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField8FocusLost

    private void nameTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField8FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField8FocusGained

    private void transferButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferButton6ActionPerformed
        if (!"".equals(recieverAccNoTextField9.getText())) {

            transfer(transferAmmountTextField9.getText(), recieverAccNoTextField9.getText());
            recieverNameTextField7.setText("");
            recieverAccNoTextField9.setText("");
            transferAmmountTextField9.setText("");
            recieverPanel1.setVisible(false);

            availableBalanceTextField11.setText(remainingBalanceTextField12.getText());
            availableBalanceTextField8.setText(remainingBalanceTextField12.getText());
            availableBalanceTextField6.setText(remainingBalanceTextField12.getText());
            accNoTextField12.setText(remainingBalanceTextField12.getText());
            transferAmmountTextField10.setText("");
            remainingBalanceTextField12.setText("");
            senderPanel2.setVisible(true);
            transferButton6.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter\nreciever's account number", "Transfer Ammount", JOptionPane.YES_OPTION);
        }
    }//GEN-LAST:event_transferButton6ActionPerformed

    private void transferAmmountTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferAmmountTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField9ActionPerformed

    private void transferAmmountTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transferAmmountTextField9FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField9FocusLost

    private void transferAmmountTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transferAmmountTextField9FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField9FocusGained

    private void transferAmmountTextField9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_transferAmmountTextField9CaretUpdate

    }//GEN-LAST:event_transferAmmountTextField9CaretUpdate

    private void recieverAccNoTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recieverAccNoTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recieverAccNoTextField9ActionPerformed

    private void recieverAccNoTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recieverAccNoTextField9FocusLost
        if (!"".equals(recieverAccNoTextField9.getText())) {
            if (recieverAccNoTextField9.getText().equals(accNO)) {
                transferButton6.setEnabled(false);
                recieverNameTextField7.setText("");
                JOptionPane.showMessageDialog(rootPane, "You can't transffer to\n your own Bank Account", "Transfer Ammount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                ResultSet rs;
                Connection con = connection.connect();
                PreparedStatement pst;
                String sqlQuery = "SELECT * from users WHERE account_no =? ";
                pst = con.prepareStatement(sqlQuery);
                pst.setString(1, recieverAccNoTextField9.getText());
                rs = pst.executeQuery();

                if (rs.next()) {
                    recieverNameTextField7.setText(rs.getString("name"));
                    transferButton6.setEnabled(true);
                } else {
                    transferButton6.setEnabled(false);
                    recieverNameTextField7.setText("");
                    JOptionPane.showMessageDialog(rootPane, "Please enter a\nvalid account number", "Transfer Ammount", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
//                    if (!recieverAccNoTextField9.getText().equals(accNoTextField10.getText())) {
//                        if (words[1].equals(recieverAccNoTextField9.getText())) {
//                            recieverNameTextField7.setText(words[0]);
//                            checker = false;
//                            transferButton6.setEnabled(true);
//                            break;
//                        }
//                    } else {
//                        checker = false;
//                        transferButton6.setEnabled(false);
//                        recieverNameTextField7.setText("");
//                        JOptionPane.showMessageDialog(rootPane, "You can't transffer to\n your own Bank Account", "Transfer Ammount", JOptionPane.ERROR_MESSAGE);
//                        break;
//                    }
//
//
//               
//                if (checker == true) {
//

//
//                }
        } else {
            recieverNameTextField7.setText("");
            transferButton6.setEnabled(false);
        }
    }//GEN-LAST:event_recieverAccNoTextField9FocusLost

    private void recieverAccNoTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recieverAccNoTextField9FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_recieverAccNoTextField9FocusGained

    private void recieverAccNoTextField9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_recieverAccNoTextField9CaretUpdate

    }//GEN-LAST:event_recieverAccNoTextField9CaretUpdate

    private void recieverNameTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recieverNameTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recieverNameTextField7ActionPerformed

    private void recieverNameTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recieverNameTextField7FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_recieverNameTextField7FocusLost

    private void recieverNameTextField7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recieverNameTextField7FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_recieverNameTextField7FocusGained

    private void nextButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButton4ActionPerformed
        senderPanel2.setVisible(false);
        recieverPanel1.setVisible(true);
        transferAmmountTextField9.setText(transferAmmountTextField10.getText());
    }//GEN-LAST:event_nextButton4ActionPerformed

    private void remainingBalanceTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remainingBalanceTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField12ActionPerformed

    private void remainingBalanceTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remainingBalanceTextField12FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField12FocusLost

    private void remainingBalanceTextField12FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remainingBalanceTextField12FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField12FocusGained

    private void transferAmmountTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferAmmountTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField10ActionPerformed

    private void transferAmmountTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transferAmmountTextField10FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField10FocusLost

    private void transferAmmountTextField10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transferAmmountTextField10FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_transferAmmountTextField10FocusGained

    private void transferAmmountTextField10CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_transferAmmountTextField10CaretUpdate
        if (!"".equals(transferAmmountTextField10.getText())) {
            if (Double.parseDouble((transferAmmountTextField10.getText())) <= (Double.parseDouble(availableBalanceTextField11.getText()))) {
                Double availableBalance, transferAmmount, remainingBalance;
                availableBalance = Double.parseDouble(availableBalanceTextField11.getText());
                transferAmmount = Double.parseDouble(transferAmmountTextField10.getText());
                remainingBalance = availableBalance - transferAmmount;
                remainingBalanceTextField12.setText(String.valueOf(remainingBalance));
            } else {
                transferAmmountTextField10.setText("");
                JOptionPane.showMessageDialog(rootPane, "You don't have\nEnough Balance", "Transfer Ammount", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_transferAmmountTextField10CaretUpdate

    private void availableBalanceTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBalanceTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField11ActionPerformed

    private void availableBalanceTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField11FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField11FocusLost

    private void availableBalanceTextField11FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField11FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField11FocusGained

    private void accNoTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accNoTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField10ActionPerformed

    private void accNoTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField10FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField10FocusLost

    private void accNoTextField10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField10FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField10FocusGained

    private void nameTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField9ActionPerformed

    private void nameTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField9FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField9FocusLost

    private void nameTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField9FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField9FocusGained

    private void withdrawButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withdrawButton3ActionPerformed
        if (!"".equals(withdrawlAmmountTextField8.getText())) {
            withdraw(withdrawlAmmountTextField8.getText());
            availableBalanceTextField8.setText(remainingBalanceTextField9.getText());
            availableBalanceTextField6.setText(remainingBalanceTextField9.getText());
            availableBalanceTextField11.setText(remainingBalanceTextField9.getText());
            accNoTextField12.setText(remainingBalanceTextField9.getText());
            remainingBalanceTextField9.setText("");
            withdrawlAmmountTextField8.setText("");

        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter Ammount\nfor withdrawl", "Withdraw Ammount", JOptionPane.YES_OPTION);
        }
    }//GEN-LAST:event_withdrawButton3ActionPerformed

    private void remainingBalanceTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remainingBalanceTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField9ActionPerformed

    private void remainingBalanceTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remainingBalanceTextField9FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField9FocusLost

    private void remainingBalanceTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remainingBalanceTextField9FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_remainingBalanceTextField9FocusGained

    private void withdrawlAmmountTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withdrawlAmmountTextField8ActionPerformed

    }//GEN-LAST:event_withdrawlAmmountTextField8ActionPerformed

    private void withdrawlAmmountTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_withdrawlAmmountTextField8FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_withdrawlAmmountTextField8FocusLost

    private void withdrawlAmmountTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_withdrawlAmmountTextField8FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_withdrawlAmmountTextField8FocusGained

    private void withdrawlAmmountTextField8CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_withdrawlAmmountTextField8CaretUpdate
        if (!"".equals(withdrawlAmmountTextField8.getText())) {
            if (Double.parseDouble((withdrawlAmmountTextField8.getText())) <= (Double.parseDouble(availableBalanceTextField8.getText()))) {
                Double availableBalance, withdrawlAmmount, remainingBalance;
                availableBalance = Double.parseDouble(availableBalanceTextField8.getText());
                withdrawlAmmount = Double.parseDouble(withdrawlAmmountTextField8.getText());
                remainingBalance = availableBalance - withdrawlAmmount;
                remainingBalanceTextField9.setText(String.valueOf(remainingBalance));
            } else {
                remainingBalanceTextField9.setText("");
                withdrawlAmmountTextField8.setText("");
                JOptionPane.showMessageDialog(rootPane, "You don't have\nEnough Balance", "Withdraw Ammount", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_withdrawlAmmountTextField8CaretUpdate

    private void availableBalanceTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBalanceTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField8ActionPerformed

    private void availableBalanceTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField8FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField8FocusLost

    private void availableBalanceTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField8FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField8FocusGained

    private void accNoTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accNoTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField7ActionPerformed

    private void accNoTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField7FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField7FocusLost

    private void accNoTextField7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField7FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField7FocusGained

    private void nameTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField6ActionPerformed

    private void nameTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField6FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField6FocusLost

    private void nameTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField6FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField6FocusGained

    private void depositButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depositButton2ActionPerformed
        if (!"".equals(depositAmmountTextField7.getText())) {
            deposit(depositAmmountTextField7.getText());

            availableBalanceTextField6.setText(totalAmmountTextField8.getText());
            availableBalanceTextField8.setText(totalAmmountTextField8.getText());
            availableBalanceTextField11.setText(totalAmmountTextField8.getText());
            accNoTextField12.setText(totalAmmountTextField8.getText());
            totalAmmountTextField8.setText("");
            depositAmmountTextField7.setText("");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter Ammount\nto deposit", "Deposit Ammount", JOptionPane.YES_OPTION);
        }
    }//GEN-LAST:event_depositButton2ActionPerformed

    private void totalAmmountTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalAmmountTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalAmmountTextField8ActionPerformed

    private void totalAmmountTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalAmmountTextField8FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_totalAmmountTextField8FocusLost

    private void totalAmmountTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalAmmountTextField8FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_totalAmmountTextField8FocusGained

    private void depositAmmountTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depositAmmountTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depositAmmountTextField7ActionPerformed

    private void depositAmmountTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_depositAmmountTextField7FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_depositAmmountTextField7FocusLost

    private void depositAmmountTextField7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_depositAmmountTextField7FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_depositAmmountTextField7FocusGained

    private void depositAmmountTextField7CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_depositAmmountTextField7CaretUpdate
        if (!"".equals(depositAmmountTextField7.getText())) {
            double availableBalance, depositAmmount, totalAmmount;
            availableBalance = Double.parseDouble(availableBalanceTextField6.getText());
            depositAmmount = Double.parseDouble(depositAmmountTextField7.getText());
            totalAmmount = availableBalance + depositAmmount;
            totalAmmountTextField8.setText(String.valueOf(totalAmmount));
        }
    }//GEN-LAST:event_depositAmmountTextField7CaretUpdate

    private void availableBalanceTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBalanceTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField6ActionPerformed

    private void availableBalanceTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField6FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField6FocusLost

    private void availableBalanceTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_availableBalanceTextField6FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBalanceTextField6FocusGained

    private void nameTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField5ActionPerformed

    private void nameTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField5FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField5FocusLost

    private void nameTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField5FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextField5FocusGained

    private void accNoTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accNoTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField4ActionPerformed

    private void accNoTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField4FocusLost

    private void accNoTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accNoTextField4FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_accNoTextField4FocusGained

    private void saveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButton2ActionPerformed

        edditProfile(
                nameTextField3.getText(),
                genderTextField1.getText(),
                nationalityTextField3.getText(),
                dateOfBirthTextField4.getText(),
                accTypeTextField4.getText(),
                phNoTextField5.getText(),
                addressTextField3.getText()
        );

    }//GEN-LAST:event_saveButton2ActionPerformed

    private void edditButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edditButton2ActionPerformed
        saveButton2.setEnabled(true);
        saveButton2.setVisible(true);
        edditButton2.setVisible(false);

        nameTextField3.setEditable(true);
        genderTextField1.setEditable(true);
        nationalityTextField3.setEditable(true);
        dateOfBirthTextField4.setEditable(true);
        accTypeTextField4.setEditable(true);
        phNoTextField5.setEditable(true);
        addressTextField3.setEditable(true);

        nameTextField3.setBackground(new Color(255, 255, 255));
        nameTextField3.setForeground(Color.black);

        genderTextField1.setBackground(new Color(255, 255, 255));
        genderTextField1.setForeground(Color.black);

        nationalityTextField3.setBackground(new Color(255, 255, 255));
        nationalityTextField3.setForeground(Color.black);

        dateOfBirthTextField4.setBackground(new Color(255, 255, 255));
        dateOfBirthTextField4.setForeground(Color.black);

        accTypeTextField4.setBackground(new Color(255, 255, 255));
        accTypeTextField4.setForeground(Color.black);

        phNoTextField5.setBackground(new Color(255, 255, 255));
        phNoTextField5.setForeground(Color.black);

        addressTextField3.setBackground(new Color(255, 255, 255));
        addressTextField3.setForeground(Color.black);

    }//GEN-LAST:event_edditButton2ActionPerformed

    private void addressTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addressTextField3FocusLost

    }//GEN-LAST:event_addressTextField3FocusLost

    private void addressTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addressTextField3FocusGained

    }//GEN-LAST:event_addressTextField3FocusGained

    private void phNoTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phNoTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phNoTextField5ActionPerformed

    private void phNoTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phNoTextField5FocusLost

    }//GEN-LAST:event_phNoTextField5FocusLost

    private void phNoTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phNoTextField5FocusGained

    }//GEN-LAST:event_phNoTextField5FocusGained

    private void nameTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextField3ActionPerformed

    }//GEN-LAST:event_nameTextField3ActionPerformed

    private void nameTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField3FocusLost

    }//GEN-LAST:event_nameTextField3FocusLost

    private void nameTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextField3FocusGained

    }//GEN-LAST:event_nameTextField3FocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new home("").setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainDesign;
    private javax.swing.JLabel accNoText;
    private javax.swing.JLabel accNoText2;
    private javax.swing.JLabel accNoText3;
    private javax.swing.JLabel accNoText5;
    private javax.swing.JLabel accNoText6;
    private javax.swing.JTextField accNoTextField10;
    private javax.swing.JTextField accNoTextField11;
    private javax.swing.JTextField accNoTextField12;
    private javax.swing.JTextField accNoTextField4;
    private javax.swing.JTextField accNoTextField5;
    private javax.swing.JTextField accNoTextField7;
    private javax.swing.JLabel accTypeText;
    private javax.swing.JTextField accTypeTextField4;
    private javax.swing.JTextField addressTextField3;
    private javax.swing.JLabel addresstext1;
    private javax.swing.JPanel availableBalancePanel5;
    private javax.swing.JLabel availableBalanceText3;
    private javax.swing.JLabel availableBalanceText4;
    private javax.swing.JLabel availableBalanceText6;
    private javax.swing.JLabel availableBalanceText7;
    private javax.swing.JTextField availableBalanceTextField11;
    private javax.swing.JTextField availableBalanceTextField6;
    private javax.swing.JTextField availableBalanceTextField8;
    private javax.swing.JLabel dateOfBirthText3;
    private javax.swing.JTextField dateOfBirthTextField4;
    private javax.swing.JLabel depositAmmountText4;
    private javax.swing.JLabel depositAmmountText5;
    private javax.swing.JTextField depositAmmountTextField7;
    private javax.swing.JButton depositButton2;
    private javax.swing.JPanel depositPanel2;
    private javax.swing.JButton edditButton2;
    private javax.swing.JLabel genderText;
    private javax.swing.JLabel genderText1;
    private javax.swing.JTextField genderTextField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel nameText1;
    private javax.swing.JLabel nameText3;
    private javax.swing.JLabel nameText4;
    private javax.swing.JLabel nameText6;
    private javax.swing.JLabel nameText7;
    private javax.swing.JTextField nameTextField3;
    private javax.swing.JTextField nameTextField5;
    private javax.swing.JTextField nameTextField6;
    private javax.swing.JTextField nameTextField8;
    private javax.swing.JTextField nameTextField9;
    private javax.swing.JLabel nationalityText2;
    private javax.swing.JTextField nationalityTextField3;
    private javax.swing.JButton nextButton4;
    private javax.swing.JTextField passwordTextField2;
    private javax.swing.JLabel phNoText;
    private javax.swing.JTextField phNoTextField5;
    private javax.swing.JPanel profilePanel1;
    private javax.swing.JLabel recieverAccNoText4;
    private javax.swing.JTextField recieverAccNoTextField9;
    private javax.swing.JTextField recieverNameTextField7;
    private javax.swing.JPanel recieverPanel1;
    private javax.swing.JLabel recieverText5;
    private javax.swing.JTextField remainingBalanceTextField12;
    private javax.swing.JTextField remainingBalanceTextField9;
    private javax.swing.JLabel remainningBalanceText6;
    private javax.swing.JLabel remainningBalanceText8;
    private javax.swing.JButton saveButton2;
    private javax.swing.JPanel senderPanel2;
    private javax.swing.JLabel totalAmmountText5;
    private javax.swing.JTextField totalAmmountTextField8;
    private javax.swing.JPanel transactionPanel6;
    private javax.swing.JTable transactionTable2;
    private javax.swing.JLabel transferAmmountText6;
    private javax.swing.JLabel transferAmmountText7;
    private javax.swing.JTextField transferAmmountTextField10;
    private javax.swing.JTextField transferAmmountTextField9;
    private javax.swing.JButton transferButton6;
    private javax.swing.JPanel transferPanel4;
    private javax.swing.JButton withdrawButton3;
    private javax.swing.JPanel withdrawPanel3;
    private javax.swing.JTextField withdrawlAmmountTextField8;
    // End of variables declaration//GEN-END:variables

    private void print(double depositAmmount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
