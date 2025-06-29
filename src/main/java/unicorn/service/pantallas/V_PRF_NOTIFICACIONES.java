/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unicorn.service.pantallas;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.IFile;
import unicorn.model.*;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author KNOW-RIC
 */
public class V_PRF_NOTIFICACIONES extends javax.swing.JFrame {
    private final Account account;
    private final NewsController newsController;

    /**
     * Creates new form DOCENTE
     * @param account
     */
    public V_PRF_NOTIFICACIONES(Account account) {
        this.account = account;
        try {
            IFile<News> newsFileHandler = new FileHandler<>(new News());
            this.newsController = new NewsController(newsFileHandler, account.getId());
        } catch (NewsException e) {
            throw new RuntimeException("Error al inicializar el controlador de notificaciones", e);
        }
        initComponents();
        setTitle("CURSIVE - Panel de Profesores");
        setSize(1360, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
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
        
        // Configurar la tabla
        configureTable();
        Timer timer = new Timer(30000, e -> loadNotificationsToTable()); // Actualizar cada 30 segundos
        timer.start();
    }

    private void configureTable() {
        // Configurar modelo de tabla con columnas adecuadas
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"MENSAJE", "TIPO", "FECHA", "ESTADO"}) {
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        DTO_NOTIFICACIONES.setModel(model);
        
        // Configurar renderizador para resaltar notificaciones no leídas
        DTO_NOTIFICACIONES.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Obtener el estado de la notificación
                String estado = (String) table.getModel().getValueAt(row, 3);
                
                if ("NO LEÍDO".equals(estado)) {
                    c.setBackground(new Color(255, 255, 200)); // Amarillo claro para no leídas
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(table.getBackground());
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                
                if (isSelected) {
                    c.setBackground(new Color(77, 199, 183)); // Color de selección
                }
                
                return c;
            }
        });
        
        // Configurar tamaño de columnas
        DTO_NOTIFICACIONES.getColumnModel().getColumn(0).setPreferredWidth(400); // Mensaje
        DTO_NOTIFICACIONES.getColumnModel().getColumn(1).setPreferredWidth(150); // Tipo
        DTO_NOTIFICACIONES.getColumnModel().getColumn(2).setPreferredWidth(150); // Fecha
        DTO_NOTIFICACIONES.getColumnModel().getColumn(3).setPreferredWidth(100); // Estado
    }

    private void loadNotificationsToTable() {
        DefaultTableModel model = (DefaultTableModel) DTO_NOTIFICACIONES.getModel();
        model.setRowCount(0); // Limpiar la tabla
        
        try {
            // Obtener todas las notificaciones del usuario
            List<News> notifications = newsController.getAllNewsByUser(account.getId());
            
            // Formateador de fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (News news : notifications) {
                // Agregar cada notificación como una fila en la tabla
                model.addRow(new Object[]{
                    news.getMensaje(),
                    news.getTipoNotificacion().getDescripcion(),
                    news.getFecha().format(formatter),
                    news.isLeida() ? "LEÍDO" : "NO LEÍDO"
                });
            }
            
            // Mostrar mensaje si no hay notificaciones
            if (notifications.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No tienes notificaciones", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar notificaciones: " + e.getMessage(), 
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
        PNL_OPTION = new javax.swing.JPanel();
        BTN_MIHORARIO = new javax.swing.JButton();
        BTN_NOTIFICACIONES = new javax.swing.JButton();
        BTN_FaQ = new javax.swing.JButton();
        PNL_TOP = new javax.swing.JPanel();
        TITULO = new javax.swing.JLabel();
        BIENVENIDA = new javax.swing.JLabel();
        BTN_MYACC = new javax.swing.JButton();
        BTN_LOGOUT = new javax.swing.JButton();
        BTN_RAIZ = new javax.swing.JButton();
        TBL_NTF = new javax.swing.JScrollPane();
        DTO_NOTIFICACIONES = new javax.swing.JTable();
        BTN_COM_MATERIA = new javax.swing.JButton();
        BTN_LEER2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1360, 800));
        setName("EST"); // NOI18N
        setSize(new java.awt.Dimension(1360, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FONDO.setBackground(new java.awt.Color(77, 199, 183));
        FONDO.setMaximumSize(new java.awt.Dimension(1100, 800));
        FONDO.setMinimumSize(new java.awt.Dimension(1100, 800));
        FONDO.setPreferredSize(new java.awt.Dimension(1100, 800));
        FONDO.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PNL_OPTION.setBackground(new java.awt.Color(10, 15, 34));
        PNL_OPTION.setPreferredSize(new java.awt.Dimension(260, 800));
        PNL_OPTION.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BTN_MIHORARIO.setBackground(new java.awt.Color(32, 97, 96));
        BTN_MIHORARIO.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_MIHORARIO.setForeground(new java.awt.Color(255, 255, 255));
        BTN_MIHORARIO.setText("MI HORARIO");
        BTN_MIHORARIO.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_MIHORARIO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_MIHORARIOActionPerformed(evt);
            }
        });
        PNL_OPTION.add(BTN_MIHORARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 240, 60));

        BTN_NOTIFICACIONES.setBackground(new java.awt.Color(77, 199, 183));
        BTN_NOTIFICACIONES.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_NOTIFICACIONES.setForeground(new java.awt.Color(10, 15, 34));
        BTN_NOTIFICACIONES.setText("NOTIFICACION");
        BTN_NOTIFICACIONES.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_NOTIFICACIONES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_NOTIFICACIONESActionPerformed(evt);
            }
        });
        PNL_OPTION.add(BTN_NOTIFICACIONES, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 240, 60));

        BTN_FaQ.setBackground(new java.awt.Color(32, 97, 96));
        BTN_FaQ.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_FaQ.setForeground(new java.awt.Color(255, 255, 255));
        BTN_FaQ.setText("FAQ");
        BTN_FaQ.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BTN_FaQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_FaQActionPerformed(evt);
            }
        });
        PNL_OPTION.add(BTN_FaQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 240, 50));

        FONDO.add(PNL_OPTION, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 320, 690));

        PNL_TOP.setBackground(new java.awt.Color(10, 15, 34));

        TITULO.setFont(new java.awt.Font("Fira Code", 1, 48)); // NOI18N
        TITULO.setForeground(new java.awt.Color(255, 255, 255));
        TITULO.setText("CURSIVE");

        BIENVENIDA.setFont(new java.awt.Font("Fira Code", 1, 48)); // NOI18N
        BIENVENIDA.setForeground(new java.awt.Color(255, 255, 255));
        BIENVENIDA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BIENVENIDA.setText("NOTIFICACIONES");
        BIENVENIDA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        BTN_MYACC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/I_PERFIL.png"))); // NOI18N
        BTN_MYACC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_MYACCActionPerformed(evt);
            }
        });

        BTN_LOGOUT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/LOGOUT.png"))); // NOI18N
        BTN_LOGOUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LOGOUTActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PNL_TOPLayout.setVerticalGroup(
            PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PNL_TOPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TITULO)
                    .addComponent(BTN_RAIZ)
                    .addGroup(PNL_TOPLayout.createSequentialGroup()
                        .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FONDO.add(PNL_TOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 110));

        DTO_NOTIFICACIONES.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MENSAJE", "TIPO", "FECHA", "ESTADO"
            }
        ));
        TBL_NTF.setViewportView(DTO_NOTIFICACIONES);

        FONDO.add(TBL_NTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 830, 440));

        BTN_COM_MATERIA.setBackground(new java.awt.Color(32, 97, 96));
        BTN_COM_MATERIA.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_COM_MATERIA.setForeground(new java.awt.Color(10, 15, 34));
        BTN_COM_MATERIA.setText("COMINICADOS POR MATERIA ");
        BTN_COM_MATERIA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BTN_COM_MATERIA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_COM_MATERIAActionPerformed(evt);
            }
        });
        FONDO.add(BTN_COM_MATERIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 730, 350, 40));

        BTN_LEER2.setBackground(new java.awt.Color(32, 97, 96));
        BTN_LEER2.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_LEER2.setForeground(new java.awt.Color(10, 15, 34));
        BTN_LEER2.setText("MARCAR COMO LEIDO");
        BTN_LEER2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BTN_LEER2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LEER2ActionPerformed(evt);
            }
        });
        FONDO.add(BTN_LEER2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 730, 280, 40));

        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_MIHORARIOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_MIHORARIOActionPerformed
        IV_EST_HORARIO registerScreen = new IV_EST_HORARIO(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_MIHORARIOActionPerformed

    private void BTN_NOTIFICACIONESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_NOTIFICACIONESActionPerformed
        V_PRF_NOTIFICACIONES registerScreen = new V_PRF_NOTIFICACIONES(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_NOTIFICACIONESActionPerformed

    private void BTN_MYACCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_MYACCActionPerformed
        I_MYACC registerScreen = new I_MYACC(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_MYACCActionPerformed

    private void BTN_FaQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_FaQActionPerformed
        IV_EST_FAQ registerScreen = new IV_EST_FAQ(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_FaQActionPerformed

    private void BTN_LOGOUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_LOGOUTActionPerformed
        I_LOGIN registerScreen = new I_LOGIN();
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_LOGOUTActionPerformed

    private void BTN_RAIZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_RAIZActionPerformed
        switch (account.getTipoCuenta()) {
            case ADMIN -> new VI_ADM_BNV(account).setVisible(true);
            case PROFESOR -> new V_PRF_BNV(account).setVisible(true);
            case ESTUDIANTE -> new IV_EST_BNV(account).setVisible(true);
        }
    }//GEN-LAST:event_BTN_RAIZActionPerformed

    private void BTN_COM_MATERIAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_COM_MATERIAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_COM_MATERIAActionPerformed

    private void BTN_LEER2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_LEER2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_LEER2ActionPerformed

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
            java.util.logging.Logger.getLogger(V_PRF_NOTIFICACIONES.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_PRF_NOTIFICACIONES.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_PRF_NOTIFICACIONES.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_PRF_NOTIFICACIONES.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            new V_PRF_NOTIFICACIONES(null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BIENVENIDA;
    private javax.swing.JButton BTN_COM_MATERIA;
    private javax.swing.JButton BTN_FaQ;
    private javax.swing.JButton BTN_LEER2;
    private javax.swing.JButton BTN_LOGOUT;
    private javax.swing.JButton BTN_MIHORARIO;
    private javax.swing.JButton BTN_MYACC;
    private javax.swing.JButton BTN_NOTIFICACIONES;
    private javax.swing.JButton BTN_RAIZ;
    private javax.swing.JTable DTO_NOTIFICACIONES;
    private javax.swing.JPanel FONDO;
    private javax.swing.JPanel PNL_OPTION;
    private javax.swing.JPanel PNL_TOP;
    private javax.swing.JScrollPane TBL_NTF;
    private javax.swing.JLabel TITULO;
    // End of variables declaration//GEN-END:variables
}
