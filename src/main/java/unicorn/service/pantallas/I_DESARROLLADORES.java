/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unicorn.service.pantallas;

import unicorn.model.*;

import javax.swing.*;
import java.awt.*;
import static unicorn.util.TipoCuenta.ADMIN;
import static unicorn.util.TipoCuenta.ESTUDIANTE;
import static unicorn.util.TipoCuenta.PROFESOR;

/**
 * @author KNOW-RIC
 */
public class I_DESARROLLADORES extends javax.swing.JFrame {
    private final Account account;
    private JScrollPane scrollPanelEquipo;
    private JPanel panelEquipoDesarrollo;

    /**
     * Creates new form DOCENTE
     * @param account
     */
    public I_DESARROLLADORES(Account account) {
        this.account = account;
        setTitle("CURSIVE");
        setSize(1100, 800);
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

        // Inicializar componentes
        initComponents();

        // Esperar a que la ventana esté visible para agregar el panel de equipo
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        setupTeamPanel();
                        panelEquipoDesarrollo.setOpaque(false);
                        scrollPanelEquipo.setOpaque(false);
                        scrollPanelEquipo.getViewport().setOpaque(false);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(I_DESARROLLADORES.this,
                            "Error al configurar el panel de equipo: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    I_DESARROLLADORES.this.removeComponentListener(this);
                });
            }
        });
    }

    private void setupTeamPanel() {
        // Panel principal para contener la información del equipo
        panelEquipoDesarrollo = new JPanel();
        panelEquipoDesarrollo.setLayout(new BoxLayout(panelEquipoDesarrollo, BoxLayout.Y_AXIS));
        panelEquipoDesarrollo.setBackground(new Color(77, 199, 183));
        
        // Crear panel deslizable
        scrollPanelEquipo = new JScrollPane(panelEquipoDesarrollo);
        scrollPanelEquipo.setBorder(null);
        scrollPanelEquipo.setBounds(100, 150, 900, 600);
        
        // Agregar información general del equipo
        JPanel panelInfoEquipo = new JPanel();
        panelInfoEquipo.setLayout(new BorderLayout(10, 10));
        panelInfoEquipo.setBackground(new Color(32, 97, 96));
        panelInfoEquipo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(255, 255, 255), 2)
        ));
        panelInfoEquipo.setMaximumSize(new Dimension(900, 100));
        panelInfoEquipo.setPreferredSize(new Dimension(900, 100));
    
        JTextArea infoGeneral = new JTextArea(
            """
            MATERIA: METODOS Y TALLER DE PROGRAMACION
            DOCENTE: VALENTIN LAIME ZAPATA
            GRUPO: UNICORN""");
        infoGeneral.setLineWrap(true);
        infoGeneral.setWrapStyleWord(true);
        infoGeneral.setEditable(false);
        infoGeneral.setBackground(new Color(32, 97, 96));
        infoGeneral.setFont(new Font("Fira Code", Font.BOLD, 16));
        infoGeneral.setForeground(new Color(255, 255, 255));
    
        panelInfoEquipo.add(infoGeneral, BorderLayout.CENTER);
        panelEquipoDesarrollo.add(panelInfoEquipo);
    
        // Agregar espacio entre secciones
        panelEquipoDesarrollo.add(Box.createRigidArea(new Dimension(0, 20)));
    
       // Agregar miembros del equipo con manejo de errores
        try {
            agregarMiembroEquipo("FIGUEREDO MANCILLA JOSE ARMANDO", 
                "/unicorn/resources/resources/jose.jpg", 
                "Desarrollador principal del proyecto. Encargado de la arquitectura general del sistema y de la implementación de la lógica de proyecto. " +
                "Supervisó la integración entre el frontend y backend. Colaboró en decisiones clave de diseño, asegurando una estructura escalable y funcional.");
        
            agregarMiembroEquipo("ORELLANA CASTRO KEYLA YHAJAIRA", 
                "/unicorn/resources/resources/Keyla.jpg", 
                "Especialista en interfaces gráficas. Responsable del diseño y desarrollo de las pantallas de la aplicación y de la experiencia de usuario. " +
                "Además, participó en el testing y documentación del sistema, realizando pruebas exhaustivas y contribuyendo a la elaboración de la documentación técnica.");
        
            agregarMiembroEquipo("PUMA URIBE WENDY", 
                "/unicorn/resources/resources/wendy.jpg", 
                "Desarrolladora de UI/UX. Diseñó los componentes visuales y contribuyó con la implementación de la interfaz gráfica del sistema. " +
                "También asumió responsabilidades en testing y documentación, realizando pruebas exhaustivas del sistema y elaborando la documentación técnica del proyecto.");
        
            agregarMiembroEquipo("COLQUE ORELLANA JHAIR HENRY", 
                "/unicorn/resources/resources/Henry.jpg", 
                "Especialista en interfaces gráficas. Responsable del diseño y desarrollo de las pantallas de la aplicación y de la experiencia de usuario. " +
                "Además, participó en el testing y documentación del sistema, realizando pruebas exhaustivas y contribuyendo a la elaboración de la documentación técnica.");
        
            agregarMiembroEquipo("ARISPE LEON JUAN DAVID", 
                "/unicorn/resources/resources/David.jpg", 
                "Especialista en interfaces gráficas. Responsable del diseño y desarrollo de las pantallas de la aplicación y de la experiencia de usuario. " +
                "Además, participó en el testing y documentación del sistema, realizando pruebas exhaustivas y contribuyendo a la elaboración de la documentación técnica.");
        } catch (Exception e) {
            System.err.println("Error al agregar miembros del equipo: " + e.getMessage());
        }

        FONDO.add(scrollPanelEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 950, 590));
        FONDO.revalidate();
        FONDO.repaint();
    }

    private void agregarMiembroEquipo(String nombre, String rutaImagen, String descripcion) {
        // Panel para cada miembro del equipo
        JPanel panelMiembro = new JPanel();
        panelMiembro.setLayout(new BorderLayout(10, 10));
        panelMiembro.setBackground(new Color(32, 97, 96));
        panelMiembro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(255, 255, 255), 2)
        ));
        panelMiembro.setMaximumSize(new Dimension(900, 150));
        panelMiembro.setPreferredSize(new Dimension(900, 150));

        // Panel para la imagen (circular)
        JPanel panelImagen = new JPanel(new GridBagLayout());
        panelImagen.setBackground(new Color(32, 97, 96));
        panelImagen.setPreferredSize(new Dimension(80, 100));

        JLabel labelImagen = new JLabel();
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagen.setPreferredSize(new Dimension(60, 60));

        boolean imagenCargada = false;

        // Intentamos cargar la imagen si se proporcionó una ruta
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                java.net.URL recurso = getClass().getResource(rutaImagen);
                if (recurso != null) {
                    ImageIcon iconoOriginal = new ImageIcon(recurso);
                    Image img = iconoOriginal.getImage();
                    Image imagenRedimensionada = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    labelImagen.setIcon(new ImageIcon(imagenRedimensionada));
                    imagenCargada = true;
                } else {
                    System.err.println("No se encontró la imagen: " + rutaImagen);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar imagen de " + nombre + ": " + e.getMessage());
            }
        }

        // Si no se pudo cargar la imagen, usamos iniciales
        if (!imagenCargada) {
            String iniciales = obtenerIniciales(nombre);
            labelImagen.setText(iniciales);
            labelImagen.setFont(new Font("Fira Code", Font.BOLD, 24));
            labelImagen.setForeground(Color.WHITE);
            labelImagen.setBackground(new Color(77, 199, 183));
            labelImagen.setOpaque(true);
        }

        // Hacer la imagen circular
        JPanel panelCircular = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(77, 199, 183));
                g2.fillOval(0, 0, 60, 60);
                g2.dispose();
            }
        };
        panelCircular.setLayout(new GridBagLayout());
        panelCircular.setPreferredSize(new Dimension(60, 60));
        panelCircular.setOpaque(false);
        panelCircular.add(labelImagen);

        panelImagen.add(panelCircular);

        // Etiqueta para el nombre
        JLabel labelNombre = new JLabel(nombre);
        labelNombre.setFont(new Font("Fira Code", Font.BOLD, 16));
        labelNombre.setForeground(Color.WHITE);

        // Área de texto para la descripción
        JTextArea areaDescripcion = new JTextArea(descripcion);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setEditable(false);
        areaDescripcion.setBackground(new Color(32, 97, 96));
        areaDescripcion.setFont(new Font("Fira Code", Font.PLAIN, 14));
        areaDescripcion.setForeground(Color.WHITE);
        areaDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel para contener nombre y descripción
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BorderLayout(5, 5));
        panelInfo.setBackground(new Color(32, 97, 96));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelInfo.add(labelNombre, BorderLayout.NORTH);
        panelInfo.add(areaDescripcion, BorderLayout.CENTER);

        // Agregar componentes al panel del miembro
        panelMiembro.add(panelImagen, BorderLayout.WEST);
        panelMiembro.add(panelInfo, BorderLayout.CENTER);

        // Agregar espacio entre miembros
        panelEquipoDesarrollo.add(panelMiembro);
        panelEquipoDesarrollo.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private String obtenerIniciales(String nombreCompleto) {
        StringBuilder iniciales = new StringBuilder();
        String[] palabras = nombreCompleto.split(" ");
        
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                iniciales.append(palabra.charAt(0));
                if (iniciales.length() >= 2) break; // Máximo 2 iniciales
            }
        }
        
        return iniciales.toString();
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
        BTN_RAIZ = new javax.swing.JButton();
        BIENVENIDA = new javax.swing.JLabel();
        BTN_MYACC = new javax.swing.JButton();
        BTN_LOGOUT = new javax.swing.JButton();
        BTN_VOLVER = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1360, 800));
        setMinimumSize(new java.awt.Dimension(1360, 800));
        setName("EST"); // NOI18N
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

        BTN_RAIZ.setBackground(new java.awt.Color(10, 15, 34));
        BTN_RAIZ.setForeground(new java.awt.Color(10, 15, 34));
        BTN_RAIZ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unicorn/resources/resources/I_ICON.png"))); // NOI18N
        BTN_RAIZ.setBorder(null);
        BTN_RAIZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_RAIZActionPerformed(evt);
            }
        });

        BIENVENIDA.setFont(new java.awt.Font("Fira Code", 1, 48)); // NOI18N
        BIENVENIDA.setForeground(new java.awt.Color(255, 255, 255));
        BIENVENIDA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BIENVENIDA.setText("¡QUIENES SOMOS!");
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

        javax.swing.GroupLayout PNL_TOPLayout = new javax.swing.GroupLayout(PNL_TOP);
        PNL_TOP.setLayout(PNL_TOPLayout);
        PNL_TOPLayout.setHorizontalGroup(
            PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PNL_TOPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BTN_RAIZ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TITULO)
                .addGap(30, 30, 30)
                .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PNL_TOPLayout.setVerticalGroup(
            PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PNL_TOPLayout.createSequentialGroup()
                .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTN_RAIZ)
                    .addComponent(BTN_LOGOUT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTN_MYACC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PNL_TOPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PNL_TOPLayout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(TITULO))
                        .addGroup(PNL_TOPLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(BIENVENIDA, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FONDO.add(PNL_TOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 110));

        BTN_VOLVER.setBackground(new java.awt.Color(32, 97, 96));
        BTN_VOLVER.setFont(new java.awt.Font("Fira Code", 1, 24)); // NOI18N
        BTN_VOLVER.setForeground(new java.awt.Color(10, 15, 34));
        BTN_VOLVER.setText("VOLVER");
        BTN_VOLVER.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BTN_VOLVER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_VOLVERActionPerformed(evt);
            }
        });
        FONDO.add(BTN_VOLVER, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 750, 250, 40));

        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 800));
        FONDO.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_MYACCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_MYACCActionPerformed
        I_MYACC registerScreen = new I_MYACC(account);
        registerScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTN_MYACCActionPerformed

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
        this.dispose();
    }//GEN-LAST:event_BTN_RAIZActionPerformed

    private void BTN_VOLVERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_VOLVERActionPerformed
        this.dispose();
        switch (account.getTipoCuenta()) {
            case ADMIN -> new VI_ADM_FAQ(account).setVisible(true);
            case PROFESOR -> new V_PRF_FAQ(account).setVisible(true);
            case ESTUDIANTE -> new IV_EST_FAQ(account).setVisible(true);
        }
    }//GEN-LAST:event_BTN_VOLVERActionPerformed

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
            java.util.logging.Logger.getLogger(I_DESARROLLADORES.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            new I_DESARROLLADORES(null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BIENVENIDA;
    private javax.swing.JButton BTN_LOGOUT;
    private javax.swing.JButton BTN_MYACC;
    private javax.swing.JButton BTN_RAIZ;
    private javax.swing.JButton BTN_VOLVER;
    private javax.swing.JPanel FONDO;
    private javax.swing.JPanel PNL_TOP;
    private javax.swing.JLabel TITULO;
    // End of variables declaration//GEN-END:variables
}
