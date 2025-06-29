/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unicorn.service.pantallas;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.model.*;


import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;



/**
 * @author KNOW-RIC
 */
public class I_MYACC extends javax.swing.JFrame {
    private final Account account;
    private final AccountController accountController;
    
    private JTextField txtId, txtUser, txtNombre, txtApellido, txtPhone, txtEmail;
    private JLabel lblRol;
    private JPasswordField txtPassActual, txtPassNueva;
    private JButton btnGuardar;
    
    /**
     * Creates new form DOCENTE
     * @param account
     */
    public I_MYACC(Account account) {
        // Inicialización por defecto para cumplir con los campos final
        this.account = account;
        AccountController tempController = null;

        if (account == null) {
            JOptionPane.showMessageDialog(null, "Error: No se recibió una cuenta válida. Redirigiendo al login.");
            new I_LOGIN().setVisible(true);
            this.accountController = null;
            this.dispose();
            return;
        }

        try {
            // Inicialización correcta del controlador
            tempController = new AccountController(
                new FileHandler<>(new Account()),
                new FileHandler<>(new Substitute()),
                new NewsController(new FileHandler<>(new News()), "admin"),
                new FileHandler<>(new FaQ())
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inicializando controlador: " + e.getMessage());
            this.accountController = null;
            this.dispose();
            return;
        }
        this.accountController = tempController;

        initComponents();
        setTitle("CURSIVE");
        setSize(1360, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Toolkit t = Toolkit.getDefaultToolkit();
            Image icon = t.getImage(getClass().getResource("/unicorn/resources/resources/I_ICON.png"));
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        setupAccountForm();
    }

    private void setupAccountForm() {
        // Fuentes más grandes
        Font labelFont = new Font("Fira Code", Font.BOLD, 22);
        Font fieldFont = new Font("Fira Code", Font.PLAIN, 20);
        Font buttonFont = new Font("Fira Code", Font.BOLD, 24);

        // Panel principal con márgenes más grandes
        JPanel panel = new JPanel(new GridLayout(9, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);

        // Función auxiliar para crear campos consistentes
        BiConsumer<JLabel, JComponent> addField = (label, field) -> {
            label.setFont(labelFont);
            label.setForeground(new Color(10,15,34));
            panel.add(label);
            
            if (field instanceof JTextField) {
                ((JTextField) field).setFont(fieldFont);
                ((JTextField) field).setPreferredSize(new Dimension(300, 40));
            } else if (field instanceof JPasswordField) {
                ((JPasswordField) field).setFont(fieldFont);
                ((JPasswordField) field).setPreferredSize(new Dimension(300, 40));
            }
            panel.add(field);
        };

        // Usuario (no editable)
        txtUser = createNonEditableField(account.getUser(), fieldFont);
        addField.accept(new JLabel("Usuario:"), txtUser);

        // Nombre
        txtNombre = new JTextField(account.getNombre());
        addField.accept(new JLabel("Nombre:"), txtNombre);

        // Apellido
        txtApellido = new JTextField(account.getApellido());
        addField.accept(new JLabel("Apellido:"), txtApellido);

        // Teléfono
        txtPhone = new JTextField(account.getPhone());
        addField.accept(new JLabel("Teléfono:"), txtPhone);

        // Email (no editable)
        txtEmail = createNonEditableField(account.getEmail(), fieldFont);
        addField.accept(new JLabel("Email:"), txtEmail);
        
        // Rol con estilo especial
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setFont(labelFont);
        rolLabel.setForeground(new Color(10,15,34));
        panel.add(rolLabel);
        
        lblRol = new JLabel(account.getTipoCuenta().getDescripcion());
        lblRol.setFont(new Font("Fira Code", Font.BOLD, 20));
        lblRol.setForeground(new Color(10,15,34));
        panel.add(lblRol);

        // Contraseña actual
        txtPassActual = new JPasswordField();
        addField.accept(new JLabel("Contraseña Actual:"), txtPassActual);

        // Nueva contraseña
        txtPassNueva = new JPasswordField();
        addField.accept(new JLabel("Nueva Contraseña:"), txtPassNueva);

        // Botón de guardar con estilo mejorado
        btnGuardar = new JButton("GUARDAR CAMBIOS");
        btnGuardar.setBackground(new Color(32,97,96));
        btnGuardar.setForeground(new Color(10,15,34));
        btnGuardar.setFont(buttonFont);
        btnGuardar.setPreferredSize(new Dimension(300, 50));
        btnGuardar.addActionListener(e -> guardarCambios());

        // Añadir al fondo con posicionamiento absoluto mejorado
        FONDO.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 600, 500));
        FONDO.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 670, 300, 60));
    }

    // Método auxiliar para crear campos no editables
    private JTextField createNonEditableField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setFont(font);
        field.setBackground(new Color(240, 240, 240));
        return field;
    }

    private void guardarCambios() {
        try {
            // 1. Validar campos obligatorios
            if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
                throw new Exception("Nombre y apellido son obligatorios");
            }

            // 2. Actualizar datos básicos
            accountController.updateName(account.getUser(), txtNombre.getText());
            accountController.updateLast(account.getUser(), txtApellido.getText());
            accountController.updatePhone(account.getUser(), txtPhone.getText());

            // 3. Manejo especial para contraseña
            char[] passActual = txtPassActual.getPassword();
            char[] passNueva = txtPassNueva.getPassword();
            
            if (passActual.length > 0 || passNueva.length > 0) {
                if (passActual.length == 0 || passNueva.length == 0) {
                    throw new Exception("Para cambiar contraseña, ambos campos deben estar completos");
                }
                accountController.updatePassword(
                    account.getUser(), 
                    new String(passActual), 
                    new String(passNueva)
                );
            }

            // 4. Mostrar confirmación y refrescar
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente");
            
            // 5. Recargar la ventana con datos actualizados
            Account updatedAccount = accountController.getByUsername(account.getUser());
            new I_MYACC(updatedAccount).setVisible(true);
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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

        FONDO = new javax.swing.JPanel();
        PNL_TOP = new javax.swing.JPanel();
        TITULO = new javax.swing.JLabel();
        BIENVENIDA = new javax.swing.JLabel();
        BTN_LOGOUT = new javax.swing.JButton();
        BTN_MYACC = new javax.swing.JButton();
        BTN_RAIZ = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1360, 800));
        setMinimumSize(new java.awt.Dimension(1360, 800));
        setName("EST"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1360, 800));
        setSize(new java.awt.Dimension(1360, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FONDO.setBackground(new java.awt.Color(77, 199, 183));
        FONDO.setMaximumSize(new java.awt.Dimension(1360, 800));
        FONDO.setMinimumSize(new java.awt.Dimension(1360, 800));
        FONDO.setPreferredSize(new java.awt.Dimension(1360, 800));
        FONDO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PNL_TOP.setBackground(new java.awt.Color(10, 15, 34));

        TITULO.setFont(new java.awt.Font("Fira Code", 1, 48)); // NOI18N
        TITULO.setForeground(new java.awt.Color(255, 255, 255));
        TITULO.setText("CURSIVE");

        BIENVENIDA.setFont(new java.awt.Font("Fira Code", 1, 48)); // NOI18N
        BIENVENIDA.setForeground(new java.awt.Color(255, 255, 255));
        BIENVENIDA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BIENVENIDA.setText("MI CUENTA");
        BIENVENIDA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        BTN_LOGOUT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/LOGOUT.png"))); // NOI18N
        BTN_LOGOUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LOGOUTActionPerformed(evt);
            }
        });

        BTN_MYACC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/I_PERFIL.png"))); // NOI18N
        BTN_MYACC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_MYACCActionPerformed(evt);
            }
        });

        BTN_RAIZ.setBackground(new java.awt.Color(10, 15, 34));
        BTN_RAIZ.setForeground(new java.awt.Color(10, 15, 34));
        BTN_RAIZ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/I_ICON.png"))); // NOI18N
        BTN_RAIZ.setBorder(null);
        BTN_RAIZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_RAIZActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PNL_TOPLayout = new javax.swing.GroupLayout(PNL_TOP);
        PNL_TOP.setLayout(PNL_TOPLayout);
        PNL_TOPLayout.setHorizontalGroup(
            PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PNL_TOPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BTN_RAIZ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TITULO)
                .addGap(83, 83, 83)
                .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        PNL_TOPLayout.setVerticalGroup(
            PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PNL_TOPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTN_RAIZ)
                    .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(PNL_TOPLayout.createSequentialGroup()
                            .addGap(40, 40, 40)
                            .addComponent(TITULO)))
                    .addGroup(PNL_TOPLayout.createSequentialGroup()
                        .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FONDO.add(PNL_TOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 110));

        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_LOGOUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_LOGOUTActionPerformed
        I_LOGIN registerScreen = new I_LOGIN();
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_LOGOUTActionPerformed

    private void BTN_MYACCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_MYACCActionPerformed
        I_MYACC registerScreen = new I_MYACC(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_MYACCActionPerformed

    private void BTN_RAIZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_RAIZActionPerformed
        switch (account.getTipoCuenta()) {
            case ADMIN -> new VI_ADM_BNV(account).setVisible(true);
            case PROFESOR -> new V_PRF_BNV(account).setVisible(true);
            case ESTUDIANTE -> new IV_EST_BNV(account).setVisible(true);
        }
    }//GEN-LAST:event_BTN_RAIZActionPerformed

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
            java.util.logging.Logger.getLogger(I_MYACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(I_MYACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(I_MYACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(I_MYACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new I_MYACC(null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BIENVENIDA;
    private javax.swing.JButton BTN_LOGOUT;
    private javax.swing.JButton BTN_MYACC;
    private javax.swing.JButton BTN_RAIZ;
    private javax.swing.JPanel FONDO;
    private javax.swing.JPanel PNL_TOP;
    private javax.swing.JLabel TITULO;
    // End of variables declaration//GEN-END:variables
}
