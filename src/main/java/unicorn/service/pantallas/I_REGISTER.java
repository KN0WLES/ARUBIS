/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unicorn.service.pantallas;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.model.*;
import unicorn.util.AccountValidation;
import unicorn.util.TipoCuenta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author KNOW-RIC
 */
public class I_REGISTER extends javax.swing.JFrame {
    private final AccountController accountController;

    /**
     * Creates new form OGIN
     */
    public I_REGISTER(AccountController accountController) {
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
        setupTextFieldPlaceholder(CMP_NAME, "Nombre");
        setupTextFieldPlaceholder(CMP_LASTNAME, "Apellidos");
        setupTextFieldPlaceholder(CMP_PHONE, "Telefono");
        setupTextFieldPlaceholder(CMP_EMAIL, "Correo Electronico");
        setupTextFieldPlaceholder(CMP_USER, "Usuario");
        setupPasswordFieldPlaceholder(CMP_PASSWORD, "Ingrese su contraseña");
    }

    private void setupTextFieldPlaceholder(JTextField field, String placeholder) {
        field.setForeground(new Color(102, 102, 102)); // Color gris para el placeholder
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE); // Color blanco para el texto real
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
    }

    private void setupPasswordFieldPlaceholder(JPasswordField field, String placeholder) {
        field.setForeground(new Color(102, 102, 102)); // Color gris para el placeholder
        field.setEchoChar((char)0); // Mostrar texto normal
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE); // Color blanco para el texto real
                    field.setEchoChar('•'); // Mostrar caracteres de contraseña
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char)0);
                    field.setText(placeholder);
                    field.setForeground(new Color(102, 102, 102));
                }
            }
        });
    }

    private void setupEnterKeyListener() {
        // Crear un ActionListener común para todos los campos
        ActionListener enterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BTN_REGISTRARSEActionPerformed(null); // Simular clic en el botón de registro
            }
        };
        
        // Asignar el listener a todos los campos
        CMP_NAME.addActionListener(enterListener);
        CMP_LASTNAME.addActionListener(enterListener);
        CMP_PHONE.addActionListener(enterListener);
        CMP_EMAIL.addActionListener(enterListener);
        CMP_USER.addActionListener(enterListener);
        CMP_PASSWORD.addActionListener(enterListener);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        campo_pass = new javax.swing.JPasswordField();
        campo_pass1 = new javax.swing.JPasswordField();
        pass_separator1 = new javax.swing.JSeparator();
        FONDO = new javax.swing.JPanel();
        TITULO = new javax.swing.JLabel();
        LOGO = new javax.swing.JLabel();
        CMP_NAME = new javax.swing.JTextField();
        LIN_NAME = new javax.swing.JSeparator();
        CMP_LASTNAME = new javax.swing.JTextField();
        LIN_LASTNAME = new javax.swing.JSeparator();
        CMP_PHONE = new javax.swing.JTextField();
        LIN_PHONE = new javax.swing.JSeparator();
        CMP_EMAIL = new javax.swing.JTextField();
        LIN_EMAIL = new javax.swing.JSeparator();
        CMP_USER = new javax.swing.JTextField();
        LIN_USER = new javax.swing.JSeparator();
        CMP_PASSWORD = new javax.swing.JPasswordField();
        LIN_PASSWORD = new javax.swing.JSeparator();
        BTN_VOLVER = new javax.swing.JButton();
        BTN_REGISTRARSE = new javax.swing.JButton();

        campo_pass.setBackground(new java.awt.Color(10, 15, 34));
        campo_pass.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        campo_pass.setForeground(new java.awt.Color(255, 255, 255));
        campo_pass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campo_pass.setText("Ingrese su contraseña");
        campo_pass.setToolTipText("");
        campo_pass.setActionCommand("<Not Set>");
        campo_pass.setBorder(null);
        campo_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campo_passActionPerformed(evt);
            }
        });

        campo_pass1.setBackground(new java.awt.Color(10, 15, 34));
        campo_pass1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        campo_pass1.setForeground(new java.awt.Color(255, 255, 255));
        campo_pass1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        campo_pass1.setText("Ingrese su contraseña");
        campo_pass1.setToolTipText("");
        campo_pass1.setActionCommand("<Not Set>");
        campo_pass1.setBorder(null);
        campo_pass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campo_pass1ActionPerformed(evt);
            }
        });

        pass_separator1.setForeground(new java.awt.Color(0, 0, 0));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(450, 600));
        setMinimumSize(new java.awt.Dimension(450, 600));
        setName("I_REGISTER"); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FONDO.setBackground(new java.awt.Color(10, 15, 34));
        FONDO.setBorder(new javax.swing.border.MatteBorder(null));
        FONDO.setMaximumSize(new java.awt.Dimension(450, 600));
        FONDO.setMinimumSize(new java.awt.Dimension(450, 600));
        FONDO.setName("I_REGISTER"); // NOI18N
        FONDO.setPreferredSize(new java.awt.Dimension(450, 600));
        FONDO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TITULO.setBackground(new java.awt.Color(255, 255, 255));
        TITULO.setFont(new java.awt.Font("Dialog", 1, 28)); // NOI18N
        TITULO.setForeground(new java.awt.Color(255, 255, 255));
        TITULO.setText("CREAR CUENTA");
        TITULO.setFocusable(false);
        TITULO.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        TITULO.setMaximumSize(new java.awt.Dimension(90, 16));
        TITULO.setMinimumSize(new java.awt.Dimension(90, 16));
        TITULO.setName(""); // NOI18N
        FONDO.add(TITULO, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 230, 30));

        LOGO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/LOGO1 (1)_1.jpg"))); // NOI18N
        FONDO.add(LOGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 200, 180));

        CMP_NAME.setBackground(new java.awt.Color(10, 15, 34));
        CMP_NAME.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_NAME.setForeground(new java.awt.Color(255, 255, 255));
        CMP_NAME.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_NAME.setText("Nombre");
        CMP_NAME.setToolTipText("");
        CMP_NAME.setBorder(null);
        CMP_NAME.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_NAMEActionPerformed(evt);
            }
        });
        FONDO.add(CMP_NAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 170, 30));

        LIN_NAME.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_NAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 170, 10));

        CMP_LASTNAME.setBackground(new java.awt.Color(10, 15, 34));
        CMP_LASTNAME.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_LASTNAME.setForeground(new java.awt.Color(255, 255, 255));
        CMP_LASTNAME.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_LASTNAME.setText("Apellidos");
        CMP_LASTNAME.setToolTipText("");
        CMP_LASTNAME.setBorder(null);
        CMP_LASTNAME.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_LASTNAMEActionPerformed(evt);
            }
        });
        FONDO.add(CMP_LASTNAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 170, 30));

        LIN_LASTNAME.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_LASTNAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 170, 10));

        CMP_PHONE.setBackground(new java.awt.Color(10, 15, 34));
        CMP_PHONE.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_PHONE.setForeground(new java.awt.Color(255, 255, 255));
        CMP_PHONE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_PHONE.setText("Telefono");
        CMP_PHONE.setToolTipText("");
        CMP_PHONE.setBorder(null);
        CMP_PHONE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_PHONEActionPerformed(evt);
            }
        });
        FONDO.add(CMP_PHONE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, 260, 30));

        LIN_PHONE.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_PHONE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 260, 10));

        CMP_EMAIL.setBackground(new java.awt.Color(10, 15, 34));
        CMP_EMAIL.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        CMP_EMAIL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_EMAIL.setText("Correo Electronico");
        CMP_EMAIL.setToolTipText("");
        CMP_EMAIL.setBorder(null);
        CMP_EMAIL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_EMAILActionPerformed(evt);
            }
        });
        FONDO.add(CMP_EMAIL, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 260, 30));

        LIN_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_EMAIL, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 260, 10));

        CMP_USER.setBackground(new java.awt.Color(10, 15, 34));
        CMP_USER.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        CMP_USER.setForeground(new java.awt.Color(255, 255, 255));
        CMP_USER.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_USER.setText("Usuario");
        CMP_USER.setToolTipText("");
        CMP_USER.setBorder(null);
        CMP_USER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_USERActionPerformed(evt);
            }
        });
        FONDO.add(CMP_USER, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, 260, 30));

        LIN_USER.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_USER, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, 260, 10));

        CMP_PASSWORD.setBackground(new java.awt.Color(10, 15, 34));
        CMP_PASSWORD.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        CMP_PASSWORD.setForeground(new java.awt.Color(255, 255, 255));
        CMP_PASSWORD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CMP_PASSWORD.setText("Ingrese su contraseña");
        CMP_PASSWORD.setToolTipText("");
        CMP_PASSWORD.setActionCommand("<Not Set>");
        CMP_PASSWORD.setBorder(null);
        CMP_PASSWORD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMP_PASSWORDActionPerformed(evt);
            }
        });
        FONDO.add(CMP_PASSWORD, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 470, 260, 30));

        LIN_PASSWORD.setForeground(new java.awt.Color(255, 255, 255));
        FONDO.add(LIN_PASSWORD, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 500, 260, 10));

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
        FONDO.add(BTN_VOLVER, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, 173, 39));

        BTN_REGISTRARSE.setBackground(new java.awt.Color(86, 178, 187));
        BTN_REGISTRARSE.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        BTN_REGISTRARSE.setForeground(new java.awt.Color(255, 255, 255));
        BTN_REGISTRARSE.setText("REGISTRARSE");
        BTN_REGISTRARSE.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_REGISTRARSE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_REGISTRARSEActionPerformed(evt);
            }
        });
        FONDO.add(BTN_REGISTRARSE, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 520, 173, 39));

        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campo_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campo_passActionPerformed

    private void campo_pass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo_pass1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campo_pass1ActionPerformed

    private void CMP_NAMEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_NAMEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_NAMEActionPerformed

    private void CMP_PASSWORDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_PASSWORDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_PASSWORDActionPerformed

    private void CMP_LASTNAMEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_LASTNAMEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_LASTNAMEActionPerformed

    private void BTN_VOLVERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_VOLVERActionPerformed
        I_LOGIN registerScreen = new I_LOGIN();
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_VOLVERActionPerformed

    private void BTN_REGISTRARSEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_REGISTRARSEActionPerformed
        // Resetear estilos de error primero
        resetErrorStyles();
        
        // Validación de campos
        List<String> errores = validarCampos();
        
        if (!errores.isEmpty()) {
            mostrarErrores(errores);
            return;
        }

        try {
            // Crear objeto Account con los datos del formulario
            Account nuevaCuenta = new Account(
                CMP_NAME.getText().trim(),
                CMP_LASTNAME.getText().trim(),
                CMP_PHONE.getText().trim(),
                CMP_EMAIL.getText().trim(),
                CMP_USER.getText().trim(),
                new String(CMP_PASSWORD.getPassword()),
                TipoCuenta.ESTUDIANTE // Asumimos que es estudiante por defecto
            );
            
            // Registrar la cuenta
            accountController.registerAccount(nuevaCuenta);
            
            // Mensaje de éxito
            JOptionPane.showMessageDialog(
                this,
                "¡Registro exitoso! Bienvenido a CURSIVE. Tu cuenta está pendiente de aprobación.",
                "Registro completado",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Volver al login
            BTN_VOLVERActionPerformed(evt);
            
        } catch (AccountException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al registrar: " + ex.getMessage(),
                "Error en registro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_BTN_REGISTRARSEActionPerformed

    private List<String> validarCampos() {
        List<String> errores = new ArrayList<>();
        
        // Validar nombre
        if (CMP_NAME.getText().trim().isEmpty() || CMP_NAME.getText().equals("Nombre")) {
            errores.add("El nombre es obligatorio");
        }
        
        // Validar apellido
        if (CMP_LASTNAME.getText().trim().isEmpty() || CMP_LASTNAME.getText().equals("Apellidos")) {
            errores.add("El apellido es obligatorio");
        }
        
        // Validar teléfono
        String phone = CMP_PHONE.getText().trim();
        if (phone.isEmpty() || phone.equals("Telefono")) {
            errores.add("El teléfono es obligatorio");
        } else if (!AccountValidation.validatePhone(phone)) {
            errores.add("El teléfono debe tener entre 7 y 15 dígitos numéricos");
        }
        
        // Validar email
        String email = CMP_EMAIL.getText().trim();
        if (email.isEmpty() || email.equals("Correo Electronico")) {
            errores.add("El correo electrónico es obligatorio");
        } else if (!AccountValidation.validateEmail(email)) {
            errores.add("El formato del correo electrónico no es válido");
        } else if (!email.endsWith("@est.umss.edu")) {
            errores.add("El correo debe ser del dominio @est.umss.edu para estudiantes");
        }
        
        // Validar usuario
        String username = CMP_USER.getText().trim();
        if (username.isEmpty() || username.equals("Usuario")) {
            errores.add("El usuario es obligatorio");
        } else if (!AccountValidation.validateUsername(username)) {
            errores.add("El usuario debe tener al menos 4 caracteres alfanuméricos");
        }
        
        // Validar contraseña
        String password = new String(CMP_PASSWORD.getPassword()).trim();
        if (password.isEmpty() || password.equals("Ingrese su contraseña")) {
            errores.add("La contraseña es obligatoria");
        } else if (!AccountValidation.validatePasswordStrength(password)) {
            errores.add("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
        }
        
        return errores;
    }

    private void mostrarErrores(List<String> errores) {
        if (!errores.isEmpty()) {
            // Restablecer todos los estilos primero
            resetErrorStyles();
            
            // Aplicar estilos de error a los campos problemáticos
            for (String error : errores) {
                if (error.contains("nombre")) {
                    highlightError(CMP_NAME, LIN_NAME);
                } else if (error.contains("apellido")) {
                    highlightError(CMP_LASTNAME, LIN_LASTNAME);
                } else if (error.contains("teléfono") || error.contains("telefono")) {
                    highlightError(CMP_PHONE, LIN_PHONE);
                } else if (error.contains("correo") || error.contains("email")) {
                    highlightError(CMP_EMAIL, LIN_EMAIL);
                } else if (error.contains("usuario")) {
                    highlightError(CMP_USER, LIN_USER);
                } else if (error.contains("contraseña") || error.contains("contrasena")) {
                    highlightError(CMP_PASSWORD, LIN_PASSWORD);
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
        CMP_NAME.setForeground(Color.WHITE);
        CMP_LASTNAME.setForeground(Color.WHITE);
        CMP_PHONE.setForeground(Color.WHITE);
        CMP_EMAIL.setForeground(Color.WHITE);
        CMP_USER.setForeground(Color.WHITE);
        CMP_PASSWORD.setForeground(Color.WHITE);
        
        // Restablecer separadores
        LIN_NAME.setForeground(Color.WHITE);
        LIN_LASTNAME.setForeground(Color.WHITE);
        LIN_PHONE.setForeground(Color.WHITE);
        LIN_EMAIL.setForeground(Color.WHITE);
        LIN_USER.setForeground(Color.WHITE);
        LIN_PASSWORD.setForeground(Color.WHITE);
    }

    private void CMP_USERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMP_USERActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CMP_USERActionPerformed

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
            java.util.logging.Logger.getLogger(I_REGISTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(I_REGISTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(I_REGISTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(I_REGISTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            new I_REGISTER(tempController).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_REGISTRARSE;
    private javax.swing.JButton BTN_VOLVER;
    private javax.swing.JTextField CMP_EMAIL;
    private javax.swing.JTextField CMP_LASTNAME;
    private javax.swing.JTextField CMP_NAME;
    private javax.swing.JPasswordField CMP_PASSWORD;
    private javax.swing.JTextField CMP_PHONE;
    private javax.swing.JTextField CMP_USER;
    private javax.swing.JPanel FONDO;
    private javax.swing.JSeparator LIN_EMAIL;
    private javax.swing.JSeparator LIN_LASTNAME;
    private javax.swing.JSeparator LIN_NAME;
    private javax.swing.JSeparator LIN_PASSWORD;
    private javax.swing.JSeparator LIN_PHONE;
    private javax.swing.JSeparator LIN_USER;
    private javax.swing.JLabel LOGO;
    private javax.swing.JLabel TITULO;
    private javax.swing.JPasswordField campo_pass;
    private javax.swing.JPasswordField campo_pass1;
    private javax.swing.JSeparator pass_separator1;
    // End of variables declaration//GEN-END:variables
}
