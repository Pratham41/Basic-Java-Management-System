/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import classes.Connect;
import static classes.Connect.pst;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Sakshi
 */
public class PurchaseProducts extends javax.swing.JFrame {

    /**
     * Creates new form PurchaseProducts
     */
    public PurchaseProducts() {
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));
        newprodaddpanel.setVisible(false);
        newdealeraddpanel.setVisible(false);

    }

    //add product method
    public void addProductMethod() {
        try {
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");

            String product = productname.getSelectedItem().toString();
            String hsnNumber = hsnnotxt.getText();

            String product_quantity = quantitytxt.getText();
            float productQuantity = Float.parseFloat(product_quantity);

            String price = productpricetxt.getText();
            float productPrice = Float.parseFloat(price);

            String MRP = extinvoice.getText();
            float MRPPrice = Float.parseFloat(MRP);

            String dealer = dealername.getSelectedItem().toString();

            String SGST = sgsttxt.getText();
            String CGST = cgsttxt.getText();

            //calculation of total mrp.
            float totalProductMrp = MRPPrice * productQuantity;

            //calculation of total price.
            float total_product_price = productQuantity * productPrice;

            //calculation of sgst amount
            float Sgst = Float.parseFloat(SGST);
            float sgst_amt = (total_product_price * Sgst) / 100;

            //calculation of cgst amount
            float Cgst = Float.parseFloat(CGST);
            float cgst_amt = (total_product_price * Cgst) / 100;

            //calculation of total price with sgst and cgst on total product price.
            float productPriceSCGST = total_product_price + sgst_amt + cgst_amt;

            //calculation of total  MRP price with sgst and cgst on total product price.
            float totalMrpWithSCGST = totalProductMrp + sgst_amt + cgst_amt;

            // calculation of total cgst sgst.
            float totalSCGST = sgst_amt + cgst_amt;

            //date time
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            java.sql.Timestamp sqlTime = new java.sql.Timestamp(date.getTime());

            //query for add data into products table
            String query = "insert into products(product_name, hsn_number, quantity, product_price, total_price, mrp, mrp_total_price, destributor, sgst, cgst, sgstamt, cgstamt, total_scgst_amt, total_price_scgst, mrp_total_price_scgst, date, time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //query for add data into available table.
            String query2 = "INSERT INTO available(product_name,quantity) VALUES('" + product + "'," + productQuantity + ") ON "
                    + "DUPLICATE KEY UPDATE product_name='" + product + "',quantity=quantity+" + productQuantity + "";

            PreparedStatement pst = Connect.con.prepareStatement(query);
            Connect.st = Connect.con.createStatement();
            JOptionPane.showMessageDialog(null, "Product added");

            pst.setString(1, product);
            pst.setString(2, hsnNumber);
            pst.setString(3, product_quantity);
            pst.setFloat(4, productPrice);
            pst.setFloat(5, total_product_price);
            pst.setFloat(6, MRPPrice);
            pst.setFloat(7, totalProductMrp);
            pst.setString(8, dealer);
            pst.setFloat(9, Sgst);
            pst.setFloat(10, Cgst);
            pst.setFloat(11, sgst_amt);
            pst.setFloat(12, cgst_amt);
            pst.setFloat(13, totalSCGST);
            pst.setFloat(14, productPriceSCGST);
            pst.setFloat(15, totalMrpWithSCGST);
            pst.setDate(16, sqlDate);
            pst.setTimestamp(17, sqlTime);
            pst.executeUpdate();

            Connect.st.executeUpdate(query2);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //show product on add product panell
    public void showProductsOnAddPanel() {

        try {

            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url, "sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query = "select distinct product_name from products";
            ResultSet rs = Connect.st.executeQuery(query);
            while (rs.next()) {
                String prod = rs.getString("product_name");
                productname.addItem(prod);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    public void shopwHSNOnSellPanel() {
        try {
            String name = productname.getSelectedItem().toString();
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query ="select hsn_number from addproduct where product_name='" + name + "'";
            ResultSet rs = Connect.st.executeQuery(query);
            while (rs.next()) {
                String hsn = rs.getString(1);
                hsnnotxt.setText(hsn);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
    
    
    public int generatebillno(){
        int bill = 0;
        try {
            
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query ="select distinct max(bill_no) from products";
            ResultSet rs = Connect.st.executeQuery(query);
            
            while (rs.next()) {
                bill = rs.getInt(1);
                
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        
        return ++bill;
    }
    
    
    
    
    
    public int generateProductId(){
        int pid = 0;
        try {
            String name = productname.getSelectedItem().toString();
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query ="select distinct Product_id from addproduct where product_name='"+name+"'";
            ResultSet rs = Connect.st.executeQuery(query);
            String query2 ="select distinct max(Product_id) from addproduct";
            if (rs.next()) {
                pid = rs.getInt(1);
                return pid;
            }
            else{
              ResultSet rs2 = Connect.st.executeQuery(query2);  
              while (rs2.next()) {
                pid = rs2.getInt(1);
                
            }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        
        return ++pid;
    }
    
    
    public void insertproductid(){
        try{
            String hsn = "15151990";
            int a =generateProductId();
            String proid = Integer.toString(a);
            String name = productname.getSelectedItem().toString();
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query = "insert into addproduct(Product_id,product_name,hsn_number) "
                    + "values("+proid+",'"+name+"','"+hsn+"')";
            hsnnotxt.setText(hsn);
            Connect.st.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    
    
    
    
    
    
    
    

    //show distributers method on add product panel
    public void showDealersOnAddPAnel() {

        try {

            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query = "select distinct destributor from products";
            ResultSet rs = Connect.st.executeQuery(query);
            while (rs.next()) {
                String dealer = rs.getString("destributor");
                dealername.addItem(dealer);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    
    
    
    
    public void showSGstOnSellPanel() {

        try {
            String name = productname.getSelectedItem().toString();
            Class.forName(Connect.myDriver);
            Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
            Connect.st = Connect.con.createStatement();
            String query = "select sgst,cgst,product_price,mrp from products where product_name='" + name + "'";
            ResultSet rs = Connect.st.executeQuery(query);
            while (rs.next()) {
                String sgst = Float.toString(rs.getFloat(1));
                String cgst = Float.toString(rs.getFloat(2));
                String price = Float.toString(rs.getFloat(3));
                String mrp = Float.toString(rs.getFloat(4));
                cgsttxt.setText(cgst);
                sgsttxt.setText(sgst);
                productpricetxt.setText(price);
                mrptxt2.setText(mrp);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
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

        addproductpanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        quantitytxt = new javax.swing.JTextField();
        productpricelbl = new javax.swing.JLabel();
        productpricetxt = new javax.swing.JTextField();
        mrplbl = new javax.swing.JLabel();
        extinvoice = new javax.swing.JTextField();
        sgstlbl = new javax.swing.JLabel();
        sgsttxt = new javax.swing.JTextField();
        cgstlbl = new javax.swing.JLabel();
        cgsttxt = new javax.swing.JTextField();
        productnamelbl1 = new javax.swing.JLabel();
        hsnnotxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        test = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        quantitylbl2 = new javax.swing.JLabel();
        newdealeraddpanel = new javax.swing.JPanel();
        productnamelbl2 = new javax.swing.JLabel();
        newdealertxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        dealerclrbtn = new javax.swing.JButton();
        newprodaddpanel = new javax.swing.JPanel();
        productnamelbl3 = new javax.swing.JLabel();
        newprodtxt = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        productname = new javax.swing.JComboBox<>();
        dealername = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        mytable = new javax.swing.JTable();
        mrplbl1 = new javax.swing.JLabel();
        mrptxt2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        menupanel = new javax.swing.JPanel();
        addlogolbl = new javax.swing.JLabel();
        addlbl = new javax.swing.JLabel();
        selllogolbl = new javax.swing.JLabel();
        sellblb = new javax.swing.JLabel();
        orderlogo = new javax.swing.JLabel();
        orderlbl = new javax.swing.JLabel();
        showlogo = new javax.swing.JLabel();
        showlbl = new javax.swing.JLabel();
        showlbl1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addproductpanel.setBackground(new java.awt.Color(43, 27, 61));
        addproductpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(150, 150, 165));

        jButton3.setBackground(new java.awt.Color(150, 150, 165));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(1073, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 57, Short.MAX_VALUE))
        );

        addproductpanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 80));

        quantitytxt.setBackground(new java.awt.Color(43, 27, 61));
        quantitytxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        quantitytxt.setForeground(new java.awt.Color(255, 255, 255));
        quantitytxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addproductpanel.add(quantitytxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 220, 30));

        productpricelbl.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        productpricelbl.setForeground(new java.awt.Color(255, 255, 255));
        productpricelbl.setText("Purchase Price");
        addproductpanel.add(productpricelbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, 130, 30));

        productpricetxt.setBackground(new java.awt.Color(43, 27, 61));
        productpricetxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        productpricetxt.setForeground(new java.awt.Color(255, 255, 255));
        productpricetxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addproductpanel.add(productpricetxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 220, 30));

        mrplbl.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mrplbl.setForeground(new java.awt.Color(255, 255, 255));
        mrplbl.setText("External Invoice No");
        addproductpanel.add(mrplbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 380, 160, -1));

        extinvoice.setBackground(new java.awt.Color(43, 27, 61));
        extinvoice.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        extinvoice.setForeground(new java.awt.Color(255, 255, 255));
        extinvoice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addproductpanel.add(extinvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, 230, 30));

        sgstlbl.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        sgstlbl.setForeground(new java.awt.Color(255, 255, 255));
        sgstlbl.setText("SGST");
        addproductpanel.add(sgstlbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 240, 140, 20));

        sgsttxt.setBackground(new java.awt.Color(43, 27, 61));
        sgsttxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        sgsttxt.setForeground(new java.awt.Color(255, 255, 255));
        sgsttxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        sgsttxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sgsttxtActionPerformed(evt);
            }
        });
        addproductpanel.add(sgsttxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, 230, 30));

        cgstlbl.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cgstlbl.setForeground(new java.awt.Color(255, 255, 255));
        cgstlbl.setText("CGST");
        addproductpanel.add(cgstlbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 310, 140, 30));

        cgsttxt.setBackground(new java.awt.Color(43, 27, 61));
        cgsttxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        cgsttxt.setForeground(new java.awt.Color(255, 255, 255));
        cgsttxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cgsttxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cgsttxtActionPerformed(evt);
            }
        });
        addproductpanel.add(cgsttxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 230, 30));

        productnamelbl1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        productnamelbl1.setForeground(new java.awt.Color(255, 255, 255));
        productnamelbl1.setText("Product Name ");
        addproductpanel.add(productnamelbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 120, 30));

        hsnnotxt.setBackground(new java.awt.Color(43, 27, 61));
        hsnnotxt.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        hsnnotxt.setForeground(new java.awt.Color(255, 255, 255));
        hsnnotxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        hsnnotxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hsnnotxtActionPerformed(evt);
            }
        });
        addproductpanel.add(hsnnotxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 220, 30));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plus.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        addproductpanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, 40, 40));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plus.png"))); // NOI18N
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
        });
        addproductpanel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 40, 50));

        test.setBackground(new java.awt.Color(43, 27, 61));
        test.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        test.setForeground(new java.awt.Color(0, 153, 0));
        test.setText("Add");
        test.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        test.setContentAreaFilled(false);
        test.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testActionPerformed(evt);
            }
        });
        addproductpanel.add(test, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 90, 30));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("HSN No");
        addproductpanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 120, 30));

        quantitylbl2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        quantitylbl2.setForeground(new java.awt.Color(255, 255, 255));
        quantitylbl2.setText("Quantity");
        addproductpanel.add(quantitylbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 120, 20));

        newdealeraddpanel.setBackground(new java.awt.Color(43, 27, 61));

        productnamelbl2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        productnamelbl2.setForeground(new java.awt.Color(255, 255, 255));
        productnamelbl2.setText("New Dealer Name ");

        newdealertxt.setBackground(new java.awt.Color(43, 27, 61));
        newdealertxt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        newdealertxt.setForeground(new java.awt.Color(255, 255, 255));
        newdealertxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        newdealertxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newdealertxtActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(43, 27, 61));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 153, 0));
        jButton1.setText("Add ");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        dealerclrbtn.setBackground(new java.awt.Color(43, 27, 61));
        dealerclrbtn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        dealerclrbtn.setForeground(new java.awt.Color(255, 255, 0));
        dealerclrbtn.setText("Clear");
        dealerclrbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0), 2));
        dealerclrbtn.setContentAreaFilled(false);
        dealerclrbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dealerclrbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout newdealeraddpanelLayout = new javax.swing.GroupLayout(newdealeraddpanel);
        newdealeraddpanel.setLayout(newdealeraddpanelLayout);
        newdealeraddpanelLayout.setHorizontalGroup(
            newdealeraddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newdealeraddpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newdealeraddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newdealeraddpanelLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dealerclrbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(newdealertxt)
                    .addGroup(newdealeraddpanelLayout.createSequentialGroup()
                        .addComponent(productnamelbl2)
                        .addGap(0, 75, Short.MAX_VALUE)))
                .addContainerGap())
        );
        newdealeraddpanelLayout.setVerticalGroup(
            newdealeraddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newdealeraddpanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(productnamelbl2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newdealertxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(newdealeraddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dealerclrbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        addproductpanel.add(newdealeraddpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 430, 250, 140));

        newprodaddpanel.setBackground(new java.awt.Color(43, 27, 61));

        productnamelbl3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        productnamelbl3.setForeground(new java.awt.Color(255, 255, 255));
        productnamelbl3.setText("New Product Name ");

        newprodtxt.setBackground(new java.awt.Color(43, 27, 61));
        newprodtxt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        newprodtxt.setForeground(new java.awt.Color(255, 255, 255));
        newprodtxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        newprodtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newprodtxtActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(43, 27, 61));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 153, 0));
        jButton2.setText("Add ");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(43, 27, 61));
        jButton4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 0));
        jButton4.setText("Clear");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0), 2));
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout newprodaddpanelLayout = new javax.swing.GroupLayout(newprodaddpanel);
        newprodaddpanel.setLayout(newprodaddpanelLayout);
        newprodaddpanelLayout.setHorizontalGroup(
            newprodaddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newprodaddpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newprodaddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productnamelbl3)
                    .addGroup(newprodaddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newprodaddpanelLayout.createSequentialGroup()
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(newprodtxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        newprodaddpanelLayout.setVerticalGroup(
            newprodaddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newprodaddpanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(productnamelbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newprodtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newprodaddpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        addproductpanel.add(newprodaddpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 260, 140));

        productname.setBackground(new java.awt.Color(43, 27, 61));
        productname.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        productname.setForeground(new java.awt.Color(255, 255, 255));
        productname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        productname.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productnameItemStateChanged(evt);
            }
        });
        addproductpanel.add(productname, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 220, 30));

        dealername.setBackground(new java.awt.Color(43, 27, 61));
        dealername.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        dealername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addproductpanel.add(dealername, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 480, 220, 30));

        mytable.setBackground(new java.awt.Color(255, 255, 255));
        mytable.setForeground(new java.awt.Color(0, 0, 0));
        mytable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Product id", "HSN", "Quantity", "Purchase Price", "Purchase Total ", "MRP", "MRP Total", "Dealer", "cgst", "sgst", "cgst amt", "sgst amt", "Purchase All", "ProAll", "MRPall"
            }
        ));
        mytable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mytableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(mytable);

        addproductpanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 1090, 120));

        mrplbl1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mrplbl1.setForeground(new java.awt.Color(255, 255, 255));
        mrplbl1.setText("MRP");
        addproductpanel.add(mrplbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 110, -1));

        mrptxt2.setBackground(new java.awt.Color(43, 27, 61));
        mrptxt2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        mrptxt2.setForeground(new java.awt.Color(255, 255, 255));
        mrptxt2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addproductpanel.add(mrptxt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 220, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Dealer Name");
        addproductpanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 450, 110, 30));

        jButton5.setBackground(new java.awt.Color(43, 27, 61));
        jButton5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(240, 148, 222));
        jButton5.setText("Submit");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 148, 222), 2));
        jButton5.setContentAreaFilled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        addproductpanel.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 560, 80, 30));

        jButton6.setBackground(new java.awt.Color(43, 27, 61));
        jButton6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 153, 0));
        jButton6.setText("Update");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        jButton6.setContentAreaFilled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        addproductpanel.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 560, 90, 30));

        jButton7.setBackground(new java.awt.Color(43, 27, 61));
        jButton7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 153, 0));
        jButton7.setText("Delete");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        jButton7.setContentAreaFilled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        addproductpanel.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 560, 80, 30));

        getContentPane().add(addproductpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 0, 1110, 754));

        menupanel.setBackground(new java.awt.Color(150, 150, 165));

        addlogolbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        addlogolbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addlogolblMouseClicked(evt);
            }
        });

        addlbl.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        addlbl.setForeground(new java.awt.Color(0, 0, 0));
        addlbl.setText("   Add");
        addlbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addlblMouseClicked(evt);
            }
        });

        selllogolbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/shopping-cart-of-checkered-design.png"))); // NOI18N
        selllogolbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selllogolblMouseClicked(evt);
            }
        });

        sellblb.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        sellblb.setForeground(new java.awt.Color(0, 0, 0));
        sellblb.setText("    Sell");
        sellblb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sellblbMouseClicked(evt);
            }
        });

        orderlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bag.png"))); // NOI18N
        orderlogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderlogoMouseClicked(evt);
            }
        });

        orderlbl.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        orderlbl.setForeground(new java.awt.Color(0, 0, 0));
        orderlbl.setText(" Order");
        orderlbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderlblMouseClicked(evt);
            }
        });

        showlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/list.png"))); // NOI18N
        showlogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showlogoMouseClicked(evt);
            }
        });

        showlbl.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        showlbl.setForeground(new java.awt.Color(0, 0, 0));
        showlbl.setText("  Shwo");
        showlbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showlblMouseClicked(evt);
            }
        });

        showlbl1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        showlbl1.setForeground(new java.awt.Color(0, 0, 0));
        showlbl1.setText(" Account");
        showlbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showlbl1MouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/money.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout menupanelLayout = new javax.swing.GroupLayout(menupanel);
        menupanel.setLayout(menupanelLayout);
        menupanelLayout.setHorizontalGroup(
            menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menupanelLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(addlogolbl)
                        .addComponent(orderlogo, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                        .addComponent(addlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sellblb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selllogolbl)
                        .addComponent(orderlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(showlogo)
                        .addComponent(showlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addComponent(showlbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        menupanelLayout.setVerticalGroup(
            menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menupanelLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(addlogolbl, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addlbl)
                .addGap(18, 18, 18)
                .addComponent(selllogolbl, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sellblb)
                .addGap(18, 18, 18)
                .addComponent(orderlogo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderlbl)
                .addGap(18, 18, 18)
                .addComponent(showlogo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showlbl)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showlbl1)
                .addGap(53, 53, 53))
        );

        getContentPane().add(menupanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sgsttxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sgsttxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sgsttxtActionPerformed

    private void cgsttxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cgsttxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cgsttxtActionPerformed

    private void hsnnotxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hsnnotxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hsnnotxtActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        // TODO add your handling code here:
        newprodaddpanel.setVisible(true);
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        // TODO add your handling code here:
        newdealeraddpanel.setVisible(true);


    }//GEN-LAST:event_jLabel22MouseClicked

    private void testActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testActionPerformed
       
//        addProductMethod();
        
      



            String product_quantity = quantitytxt.getText();
            float productQuantity = Float.parseFloat(product_quantity);

            String price = productpricetxt.getText();
            float productPrice = Float.parseFloat(price);

            String MRP = mrptxt2.getText();
            float MRPPrice = Float.parseFloat(MRP);

            String dealer = dealername.getSelectedItem().toString();

            String SGST = sgsttxt.getText();
            String CGST = cgsttxt.getText();

            //calculation of total mrp.
            float totalProductMrp =  productQuantity * MRPPrice;
            String tmrpprice = Float.toString(totalProductMrp);
            System.out.println("Total MRP : "+tmrpprice);

            //calculation of total price.
            float total_product_price = productQuantity * productPrice;
            String totalp = Float.toString(total_product_price);//
            //calculation of sgst amount
            float Sgst = Float.parseFloat(SGST);
            float sgst_amt = (total_product_price * Sgst) / 100;
            String samt = Float.toString(sgst_amt);
            //calculation of cgst amount
            float Cgst = Float.parseFloat(CGST);
            float cgst_amt = (total_product_price * Cgst) / 100;
            String camt = Float.toString(cgst_amt);
            
            //calculation of total price with sgst and cgst on total product price.
            float productPriceSCGST = total_product_price + sgst_amt + cgst_amt;
            String ptgst = Float.toString(productPriceSCGST);
            //calculation of total  MRP price with sgst and cgst on total product price.
            float totalMrpWithSCGST = totalProductMrp + sgst_amt + cgst_amt;
            String mrpgst = Float.toString(totalMrpWithSCGST);
            System.out.println("Total MRP with GST : "+totalMrpWithSCGST);
            // calculation of total cgst sgst.
            float totalSCGST = sgst_amt + cgst_amt;
            String tgst = Float.toString(totalSCGST);


    

    int a =generateProductId();
    String proid = Integer.toString(a);
    
    DefaultTableModel dm = (DefaultTableModel)mytable.getModel();
    dm.addRow(new Object[]{productname.getSelectedItem().toString(),proid
            ,hsnnotxt.getText(),quantitytxt.getText(),productpricetxt.getText(),
            totalp,mrptxt2.getText(),tmrpprice,
            dealername.getSelectedItem().toString(),cgsttxt.getText(),sgsttxt.getText(),
            camt,samt,tgst,ptgst,mrpgst
            
    });
    
    
        quantitytxt.setText("");
        productpricetxt.setText("");
        
        sgsttxt.setText("");
        cgsttxt.setText("");
        mrptxt2.setText("");

    }//GEN-LAST:event_testActionPerformed

    private void addlogolblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addlogolblMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_addlogolblMouseClicked

    private void addlblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addlblMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_addlblMouseClicked

    private void selllogolblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selllogolblMouseClicked
        // TODO add your handling code here:
        this.dispose();
        SellProducts sp = new SellProducts();

        sp.setVisible(true);


    }//GEN-LAST:event_selllogolblMouseClicked

    private void sellblbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sellblbMouseClicked
        // TODO add your handling code here:
        this.dispose();
        SellProducts sp = new SellProducts();

        sp.setVisible(true);

    }//GEN-LAST:event_sellblbMouseClicked

    private void orderlogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderlogoMouseClicked
        // TODO add your handling code here:

        this.dispose();
        OrderProduct op = new OrderProduct();
        op.setVisible(true);

    }//GEN-LAST:event_orderlogoMouseClicked

    private void showlogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showlogoMouseClicked
        // TODO add your handling code here:
        this.dispose();
        ShowProducts sp = new ShowProducts();
        sp.setVisible(true);


    }//GEN-LAST:event_showlogoMouseClicked

    private void showlblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showlblMouseClicked
        // TODO add your handling code here:
          this.dispose();
        ShowProducts sp = new ShowProducts();
        sp.setVisible(true);

    }//GEN-LAST:event_showlblMouseClicked

    private void showlbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showlbl1MouseClicked
        // TODO add your handling code here:
        this.dispose();;
        Accounts a = new Accounts ();
        a.setVisible(true);
    }//GEN-LAST:event_showlbl1MouseClicked

    private void newdealertxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newdealertxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newdealertxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String product = newdealertxt.getText();
        dealername.addItem(product);
        dealername.setSelectedItem(product);

        newdealertxt.setText("");

        newdealeraddpanel.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void newprodtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newprodtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newprodtxtActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String product = newprodtxt.getText();
        productname.addItem(product);
        productname.setSelectedItem(product);
        newprodtxt.setText("");
        
        
        
        insertproductid();
        
        
        
        
        newprodaddpanel.setVisible(false);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        newprodtxt.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void dealerclrbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dealerclrbtnActionPerformed
        // TODO add your handling code here:
        newdealertxt.setText("");
    }//GEN-LAST:event_dealerclrbtnActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        dealerclrbtn.setVisible(true);
        showProductsOnAddPanel();
        showDealersOnAddPAnel();
    }//GEN-LAST:event_formWindowOpened

    private void orderlblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderlblMouseClicked
        // TODO add your handling code here:
        this.dispose();
        OrderProduct op = new OrderProduct();
        op.setVisible(true);
    }//GEN-LAST:event_orderlblMouseClicked

    private void productnameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productnameItemStateChanged
        // TODO add your handling code here:
        shopwHSNOnSellPanel();
        showSGstOnSellPanel();
    }//GEN-LAST:event_productnameItemStateChanged

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();;
        Accounts a = new Accounts ();
        a.setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try{

  int rows=mytable.getRowCount();
  int billNo = generatebillno();
  for(int row = 0; row<rows; row++)
  {   Class.forName(Connect.myDriver);
      Connect.con = DriverManager.getConnection(Connect.url,"sql7341636","ByYALuvP9g");
      PreparedStatement pst = null;
    String product = (String)mytable.getValueAt(row, 0);
    String p_id = (String) mytable.getValueAt(row, 1);
    String HSN = (String)mytable.getValueAt(row, 2);
    String quantity = (String)mytable.getValueAt(row, 3);
    String p_price = (String)mytable.getValueAt(row, 4);
    String ptotal = (String)mytable.getValueAt(row, 5);
    String mrp = (String)mytable.getValueAt(row, 6);
    String mrptotal = (String)mytable.getValueAt(row, 7);
    String dealer = (String)mytable.getValueAt(row, 8);
    String cgst = (String)mytable.getValueAt(row, 9);
    String sgst = (String)mytable.getValueAt(row, 10);
    
    String sgstamt = (String)mytable.getValueAt(row, 11);
    String cgstamt = (String)mytable.getValueAt(row, 12);
    String totalgst = (String)mytable.getValueAt(row, 13);
    String pall = (String)mytable.getValueAt(row, 14);
    String mrpall = (String)mytable.getValueAt(row, 15);
    
    java.util.Date date = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    java.sql.Timestamp sqlTime = new java.sql.Timestamp(date.getTime());
    
    
    
    String queryco = "Insert into products(product_name,Product_id,hsn_number,quantity,product_price,total_price,mrp,mrp_total_price,destributor,sgst,cgst,sgstamt,cgstamt,total_scgst_amt,total_price_scgst,mrp_total_price_scgst,date,time,bill_no,External_invoice) "
            + "values ('"+product+"',"+p_id+","+HSN+","+quantity+","+p_price+","+ptotal+","+mrp+","+mrptotal+",'"+dealer+"',"+sgst+","+cgst+","+sgstamt+","+cgstamt+","+totalgst+","+pall+","+mrpall+",'"+sqlDate+"','"+sqlTime+"',"+billNo+","+extinvoice.getText()+")";
     
    
      //query for add data into available table.
            String query2 = "INSERT INTO available(product_name,quantity) VALUES('" + product + "'," + quantity + ") ON "
                    + "DUPLICATE KEY UPDATE product_name='" + product + "',quantity=quantity+" + quantity + "";
    
     System.out.println("p_price"+p_price);
      System.out.println("ptotal"+ptotal);
      System.out.println("mrp"+mrp);
      System.out.println("mrptotal"+mrptotal);
    pst = Connect.con.prepareStatement(queryco);
     Connect.st.executeUpdate(query2);
    pst.execute();     
  }
  JOptionPane.showMessageDialog(null, "Successfully Save");
}
catch(Exception e){
  JOptionPane.showMessageDialog(this,e.getMessage());
}
        
        DefaultTableModel model = (DefaultTableModel) mytable.getModel();
        model.setRowCount(0);
        
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void mytableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mytableMouseClicked
        // TODO add your handling code here:
        
        int selectedRow = mytable.getSelectedRow();
        TableModel model = mytable.getModel();
        
        hsnnotxt.setText(model.getValueAt(selectedRow, 2).toString());
        quantitytxt.setText(model.getValueAt(selectedRow, 3).toString());
        productpricetxt.setText(model.getValueAt(selectedRow, 4).toString());
        mrptxt2.setText(model.getValueAt(selectedRow, 6).toString());
         sgsttxt.setText(model.getValueAt(selectedRow, 10).toString());
         cgsttxt.setText(model.getValueAt(selectedRow, 9).toString());
    }//GEN-LAST:event_mytableMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        
        int i = mytable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)mytable.getModel();
        
        if(i>=0){
            model.setValueAt(hsnnotxt.getText(), i, 2);
            model.setValueAt(quantitytxt.getText(), i, 3);
            model.setValueAt(productpricetxt.getText(), i, 4);
            model.setValueAt(mrptxt2.getText(), i, 10);
             model.setValueAt(sgsttxt.getText(), i, 10);
              model.setValueAt(cgsttxt.getText(), i, 9);
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
       int i = mytable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)mytable.getModel();
        
        if(i>=0){
            model.removeRow(i);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PurchaseProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseProducts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PurchaseProducts().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addlbl;
    private javax.swing.JLabel addlogolbl;
    private javax.swing.JPanel addproductpanel;
    private javax.swing.JLabel cgstlbl;
    private javax.swing.JTextField cgsttxt;
    private javax.swing.JButton dealerclrbtn;
    private javax.swing.JComboBox<String> dealername;
    private javax.swing.JTextField extinvoice;
    private javax.swing.JTextField hsnnotxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel menupanel;
    private javax.swing.JLabel mrplbl;
    private javax.swing.JLabel mrplbl1;
    private javax.swing.JTextField mrptxt2;
    private javax.swing.JTable mytable;
    private javax.swing.JPanel newdealeraddpanel;
    private javax.swing.JTextField newdealertxt;
    private javax.swing.JPanel newprodaddpanel;
    private javax.swing.JTextField newprodtxt;
    private javax.swing.JLabel orderlbl;
    private javax.swing.JLabel orderlogo;
    private javax.swing.JComboBox<String> productname;
    private javax.swing.JLabel productnamelbl1;
    private javax.swing.JLabel productnamelbl2;
    private javax.swing.JLabel productnamelbl3;
    private javax.swing.JLabel productpricelbl;
    private javax.swing.JTextField productpricetxt;
    private javax.swing.JLabel quantitylbl2;
    private javax.swing.JTextField quantitytxt;
    private javax.swing.JLabel sellblb;
    private javax.swing.JLabel selllogolbl;
    private javax.swing.JLabel sgstlbl;
    private javax.swing.JTextField sgsttxt;
    private javax.swing.JLabel showlbl;
    private javax.swing.JLabel showlbl1;
    private javax.swing.JLabel showlogo;
    private javax.swing.JButton test;
    // End of variables declaration//GEN-END:variables
}
