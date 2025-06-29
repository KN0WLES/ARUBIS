/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unicorn.service.pantallas;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.model.*;
import unicorn.util.AccountValidation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author HP
 */
public class I_RECOVER extends javax.swing.JFrame {
    private final AccountController accountController;

    /**
     * Creates new form OGIN
     */
    public I_RECOVER(AccountController accountController) {
        this.accountController = accountController;

        // Configuración del icono de la ventana
        Toolkit t = Toolkit.getDefaultToolkit();
        java.net.URL iconUrl = getClass().getResource("/unicorn/resources/resources/ICONO.png");
        if (iconUrl == null) {
            JOptionPane.showMessageDialog(
                null,
                "No se encontró /ICONO.png en el classpath",
                "Error de recursos",
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            setIconImage(t.getImage(iconUrl));
        }

        // Inicialización de componentes gráficos
        initComponents();
        setupPlaceholders();
        setupEnterKeyListener();
        this.setSize(450, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("CURSIVE");
    }

    private void setupPlaceholders() {
        setupTextFieldPlaceholder(CMP_USERNAME, "Ingrese su nombre de usuario");
        setupTextFieldPlaceholder(CMP_PHONE, "Ingrese su telefono");
        setupTextFieldPlaceholder(CMP_EMAIL, "Ingrese su correo electronico");
    }

    private void setupTextFieldPlaceholder(JTextField field, String placeholder) {
        field.setForeground(new Color(102, 102, 102)); // Color gris para placeholder
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE); // Color blanco para texto real
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(102, 102, 102));
                }
            }
        });
        
        // Manejar el caso cuando el usuario borra todo el texto manualmente
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(new Color(102, 102, 102));
                } else if (field.getForeground().equals(new Color(102, 102, 102))) {
                    field.setForeground(Color.WHITE);
                }
            }
        });
    }

    private void setupEnterKeyListener() {
        ActionListener enterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BTN_SOLICITARActionPerformed(null);
            }
        };
        
        CMP_USERNAME.addActionListener(enterListener);
        CMP_PHONE.addActionListener(enterListener);
        CMP_EMAIL.addActionListener(enterListener);
    }

    private List<String> validarCampos() {
        List<String> errores = new ArrayList<>();
        
        // Validar nombre de usuario
        String username = CMP_USERNAME.getText().trim();
        if (username.isEmpty() || username.equals("Ingrese su nombre de usuario")) {
            errores.add("El nombre de usuario es obligatorio");
        }
        
        // Validar teléfono
        String phone = CMP_PHONE.getText().trim();
        if (phone.isEmpty() || phone.equals("Ingrese su telefono")) {
            errores.add("El teléfono es obligatorio");
        } else if (!AccountValidation.validatePhone(phone)) {
            errores.add("El teléfono debe tener entre 7 y 15 dígitos numéricos");
        }
        
        // Validar email
        String email = CMP_EMAIL.getText().trim();
        if (email.isEmpty() || email.equals("Ingrese su correo electronico")) {
            errores.add("El correo electrónico es obligatorio");
        } else if (!AccountValidation.validateEmail(email)) {
            errores.add("El formato del correo electrónico no es válido");
        }
        
        return errores;
    }

    private void mostrarErrores(List<String> errores) {
        if (!errores.isEmpty()) {
            // Restablecer todos los estilos primero
            resetErrorStyles();
            
            // Aplicar estilos de error a los campos problemáticos
            for (String error : errores) {
                if (error.contains("usuario")) {
                    highlightError(CMP_USERNAME, LIN_USERNAME);
                } else if (error.contains("teléfono") || error.contains("telefono")) {
                    highlightError(CMP_PHONE, LIN_PHONE);
                } else if (error.contains("correo") || error.contains("email")) {
                    highlightError(CMP_EMAIL, LIN_EMAIL);
                }
            }
            
            // Mostrar mensaje de errores
            StringBuilder mensaje = new StringBuilder("<html>Se encontraron los siguientes errores:<br><br>");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("<br>");
            }
            mensaje.append("</html>");
            
            JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "Errores de Validación",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void highlightError(JComponent field, JSeparator separator) {
        field.setForeground(Color.RED);
        if (separator != null) {
            separator.setForeground(Color.RED);
        }
    }

    private void resetErrorStyles() {
        // Restablecer campos de texto
        CMP_USERNAME.setForeground(Color.WHITE);
        CMP_PHONE.setForeground(Color.WHITE);
        CMP_EMAIL.setForeground(Color.WHITE);
        
        // Restablecer separadores
        LIN_USERNAME.setForeground(Color.WHITE);
        LIN_PHONE.setForeground(Color.WHITE);
        LIN_EMAIL.setForeground(Color.WHITE);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FONDO = new javax.swing.JPanel();
        LOGO = new javax.swing.JLabel();
        TITULO = new javax.swing.JLabel();
        CMP_USERNAME = new javax.swing.JTextField();
        LIN_USERNAME = new javax.swing.JSeparator();
        CMP_EMAIL = new javax.swing.JTextField();
        LIN_EMAIL = new javax.swing.JSeparator();
        CMP_PHONE = new javax.swing.JTextField();
        LIN_PHONE = new javax.swing.JSeparator();
        BTN_VOLVER = new javax.swing.JButton();
        BTN_SOLICITAR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(450, 600));
        setMinimumSize(new java.awt.Dimension(450, 600));
        setName("I_RECOVER"); // NOI18N
        setSize(new java.awt.Dimension(450, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FONDO.setBackground(new java.awt.Color(10, 15, 34));
        FONDO.setBorder(new javax.swing.border.MatteBorder(null));
        FONDO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LOGO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/LOGO1 (1)_1.jpg"))); // NOI18N
        FONDO.add(LOGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 180));

        TITULO.setBackground(new java.awt.Color(255, 255, 255));
        TITULO.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        TITULO.setForeground(new java.awt.Color(255, 255, 255));
        TITULO.setText("¿PROBLEMAS PARA INICIAR SESION?");
        TITULO.setFocusable(false);
        TITULO.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        TITULO.setMaximumSize(new java.awt.Dimension(90, 16));
        TITULO.setMinimumSize(new java.awt.Dimension(90, 16));
        TITULO.setName(""); // NOI18N
        FONDO.add(TITULO, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 440, 35));

        CMP_USERNAME.setBackground(new java.awt.Color(10, 15, 34));
        CMP_USERNAME.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_USERNAME.setForeground(new java.awt.Color(255, 255, 255));
        CMP_USERNAME.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_USERNAME.setText("Ingrese su nombre de usuario");
        CMP_USERNAME.setToolTipText("");
        CMP_USERNAME.setBorder(null);
        CMP_USERNAME.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_USERNAMEActionPerformed(evt);
            }
        });
        FONDO.add(CMP_USERNAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 260, 30));

        LIN_USERNAME.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_USERNAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 260, 10));

        CMP_EMAIL.setBackground(new java.awt.Color(10, 15, 34));
        CMP_EMAIL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        CMP_EMAIL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_EMAIL.setText("Ingrese su correo electronico");
        CMP_EMAIL.setToolTipText("");
        CMP_EMAIL.setBorder(null);
        CMP_EMAIL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_EMAILActionPerformed(evt);
            }
        });
        FONDO.add(CMP_EMAIL, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 440, 260, 30));

        LIN_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_EMAIL, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 260, 10));

        CMP_PHONE.setBackground(new java.awt.Color(10, 15, 34));
        CMP_PHONE.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_PHONE.setForeground(new java.awt.Color(255, 255, 255));
        CMP_PHONE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_PHONE.setText("Ingrese su telefono");
        CMP_PHONE.setToolTipText("");
        CMP_PHONE.setBorder(null);
        CMP_PHONE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_PHONEActionPerformed(evt);
            }
        });
        FONDO.add(CMP_PHONE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 260, 30));

        LIN_PHONE.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_PHONE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, 260, 10));

        BTN_VOLVER.setBackground(new java.awt.Color(86, 178, 187));
        BTN_VOLVER.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTN_VOLVER.setForeground(new java.awt.Color(255, 255, 255));
        BTN_VOLVER.setText("VOLVER");
        BTN_VOLVER.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_VOLVER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_VOLVERActionPerformed(evt);
            }
        });
        FONDO.add(BTN_VOLVER, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, 173, 39));

        BTN_SOLICITAR.setBackground(new java.awt.Color(86, 178, 187));
        BTN_SOLICITAR.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTN_SOLICITAR.setForeground(new java.awt.Color(255, 255, 255));
        BTN_SOLICITAR.setText("SOLICITAR");
        BTN_SOLICITAR.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_SOLICITAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_SOLICITARActionPerformed(evt);
            }
        });
        FONDO.add(BTN_SOLICITAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 510, 173, 39));

        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_VOLVERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_VOLVERActionPerformed
        I_LOGIN registerScreen = new I_LOGIN();
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_VOLVERActionPerformed

    private void CMP_USERNAMEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_USERNAMEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_USERNAMEActionPerformed

    private void BTN_SOLICITARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_SOLICITARActionPerformed
       // Resetear estilos de error primero
        resetErrorStyles();
        
        // Validación de campos
        List<String> errores = validarCampos();
        
        if (!errores.isEmpty()) {
            mostrarErrores(errores);
            return;
        }

        // Obtener valores ignorando placeholders
        String username = CMP_USERNAME.getText().trim();
        String phone = CMP_PHONE.getText().trim();
        String email = CMP_EMAIL.getText().trim();

        try {
            // Buscar la cuenta por nombre de usuario
            Account account = accountController.getByUsername(username);
            
            if (account == null) {
                throw new AccountException("El usuario no existe");
            }

            // Verificar que los datos coincidan (ignorando mayúsculas/minúsculas en email)
            boolean datosCorrectos = account.getPhone().equals(phone) && 
                                account.getEmail().equalsIgnoreCase(email);
            
            if (datosCorrectos) {
                // Resetear contraseña
                String tempPassword = generateTemporaryPassword();
                account.setRequiereCambioPassword(true);
                accountController.resetUserPassword(username, tempPassword);

                // Mostrar la contraseña temporal en un JTextField para que sea copiable
                JTextField passwordField = new JTextField(tempPassword);
                passwordField.setEditable(false);
                passwordField.setFont(new Font("Dialog", Font.BOLD, 16));
                passwordField.setHorizontalAlignment(JTextField.CENTER);

                JPanel panel = new JPanel(new BorderLayout(0, 10));
                panel.add(new JLabel("<html><div style='text-align: center;'><b>Recuperación Exitosa</b><br><br>Contraseña temporal generada:<br></div></html>", SwingConstants.CENTER), BorderLayout.NORTH);
                panel.add(passwordField, BorderLayout.CENTER);
                panel.add(new JLabel("<html><div style='text-align: center;'><br>Por favor cambie su contraseña después de iniciar sesión.</div></html>", SwingConstants.CENTER), BorderLayout.SOUTH);

                JOptionPane.showMessageDialog(
                    this,
                    panel,
                    "Recuperación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );

                // Redirigir al login
                new I_LOGIN().setVisible(true);
                this.dispose();
            } else {
                throw new AccountException("Los datos no coinciden con la cuenta registrada");
            }
        } catch (AccountException e) {
            // Mostrar mensaje de error con estilo estándar
            JOptionPane.showMessageDialog(
                this,
                "<html><div style='text-align: center;'>" +
                "<b>Error en recuperación</b><br><br>" +
                e.getMessage() +
                "</div></html>",
                "Error en recuperación",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_BTN_SOLICITARActionPerformed

    private String generateTemporaryPassword() {
        // Generar una contraseña temporal aleatoria que cumpla con los requisitos
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        
        String all = upper + lower + numbers;
        StringBuilder tempPassword = new StringBuilder();
        
        // Asegurar al menos una mayúscula, una minúscula y un número
        tempPassword.append(upper.charAt((int)(Math.random() * upper.length())));
        tempPassword.append(lower.charAt((int)(Math.random() * lower.length())));
        tempPassword.append(numbers.charAt((int)(Math.random() * numbers.length())));
        
        // Completar hasta 8 caracteres
        for (int i = 0; i < 5; i++) {
            tempPassword.append(all.charAt((int)(Math.random() * all.length())));
        }
        
        // Mezclar los caracteres
        char[] chars = tempPassword.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = (int)(Math.random() * chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }
        
        return new String(chars);
    }

    private void CMP_EMAILActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_EMAILActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_EMAILActionPerformed

    private void CMP_PHONEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_PHONEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_PHONEActionPerformed

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
            java.util.logging.Logger.getLogger(I_RECOVER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(I_RECOVER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(I_RECOVER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(I_RECOVER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
            AccountController tempController;
            try {
                // Usa el constructor completo si tu sistema lo requiere (ajusta según tus dependencias)
                tempController = new AccountController(
                    new FileHandler<>(new Account()),
                    new FileHandler<>(new Substitute()),
                    new NewsController(new FileHandler<>(new News()), "admin"),
                    new FileHandler<>(new FaQ())
                );
            } catch (AccountException | NewsException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error al inicializar el sistema de cuentas:\n" + e.getMessage(),
                    "Error crítico",
                    JOptionPane.ERROR_MESSAGE
                );
                tempController = null;
                System.exit(1);
            }
            new I_RECOVER(tempController).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_SOLICITAR;
    private javax.swing.JButton BTN_VOLVER;
    private javax.swing.JTextField CMP_EMAIL;
    private javax.swing.JTextField CMP_PHONE;
    private javax.swing.JTextField CMP_USERNAME;
    private javax.swing.JPanel FONDO;
    private javax.swing.JSeparator LIN_EMAIL;
    private javax.swing.JSeparator LIN_PHONE;
    private javax.swing.JSeparator LIN_USERNAME;
    private javax.swing.JLabel LOGO;
    private javax.swing.JLabel TITULO;
    // End of variables declaration//GEN-END:variables
}
