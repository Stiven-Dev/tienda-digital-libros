package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;

import javax.swing.*;
import java.awt.*;

public class DialogEliminarLibro extends JDialog {
   private final Evento     evento;
   private final JButton    botonEliminar   = new JButtonRojo("Eliminar");
   private final Font       fuenteLabel     = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final Font       fuenteTextField = new Font("Times New Roman", Font.PLAIN, 20);
   private final Font       fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private       Libro      libro;
   private       JPanel     panelCampos;
   private       JPanel     panelFooter;
   private       JTextField boxISBN;
   private       JTextField boxTitulo;
   private       JTextField boxAutor;
   private       JTextField boxAnioPublicacion;
   private       JTextField boxCategoria;
   private       JTextField boxEditorial;

   public DialogEliminarLibro (Evento evento, Libro libroSeleccionado) {
      super(new JFrame(), "Eliminar Libro", true);
      this.evento = evento;
      this.libro  = libroSeleccionado;
      setLayout(new BorderLayout());
      inicializarPanel();
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      actualizadDatosLibro();
   }

   private void inicializarPanel () {
      inicializarPanelCampos();
      inicializarPanelFooter();
      add(panelCampos, BorderLayout.CENTER);
      add(panelFooter, BorderLayout.SOUTH);
   }

   private void inicializarPanelCampos () {
      panelCampos = new JPanel(new GridBagLayout());
      //Creacion de Labels y centrado de cada uno
      JLabel labelISBN            = new JLabel("*ISBN", SwingConstants.CENTER);
      JLabel labelTitulo          = new JLabel("Titulo", SwingConstants.CENTER);
      JLabel labelAutor           = new JLabel("Autor(es)", SwingConstants.CENTER);
      JLabel labelAnioPublicacion = new JLabel("Año de Publicación", SwingConstants.CENTER);
      JLabel labelCategoria       = new JLabel("Género", SwingConstants.CENTER);
      JLabel labelEditorial       = new JLabel("Editorial", SwingConstants.CENTER);

      //Asignacion de fuente a cada label
      labelISBN.setFont(fuenteLabel);
      labelTitulo.setFont(fuenteLabel);
      labelAutor.setFont(fuenteLabel);
      labelAnioPublicacion.setFont(fuenteLabel);
      labelCategoria.setFont(fuenteLabel);
      labelEditorial.setFont(fuenteLabel);

      //Text Fields
      boxISBN            = new JTextField();
      boxTitulo          = new JTextField();
      boxAutor           = new JTextField();
      boxAnioPublicacion = new JTextField();
      boxCategoria       = new JTextField();
      boxEditorial       = new JTextField();

      //Asignacion de fuente a cada text field
      boxISBN.setFont(fuenteTextField);
      boxTitulo.setFont(fuenteTextField);
      boxAutor.setFont(fuenteTextField);
      boxAnioPublicacion.setFont(fuenteTextField);
      boxCategoria.setFont(fuenteTextField);
      boxEditorial.setFont(fuenteTextField);

      //Se hace que los campos no sean editables a excepcion del ISBN
      boxTitulo.setEditable(false);
      boxAutor.setEditable(false);
      boxAnioPublicacion.setEditable(false);
      boxCategoria.setEditable(false);
      boxEditorial.setEditable(false);

      //Centrado de JTextFields
      boxISBN.setHorizontalAlignment(JTextField.CENTER);
      boxTitulo.setHorizontalAlignment(JTextField.CENTER);
      boxAutor.setHorizontalAlignment(JTextField.CENTER);
      boxAnioPublicacion.setHorizontalAlignment(JTextField.CENTER);
      boxCategoria.setHorizontalAlignment(JTextField.CENTER);
      boxEditorial.setHorizontalAlignment(JTextField.CENTER);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 10, 5);
      gbc.fill   = GridBagConstraints.BOTH;

      //Peso Componente
      gbc.weightx = 0.45f; //Esta asignación hace que todos los layouts tengan este peso, hasta que se cambie

      //Fila 0, Columnas 0 y 1 ⇒ Labels ISBN y Titulo
      gbc.gridy = 0;
      gbc.gridx = 0;
      panelCampos.add(labelISBN, gbc);
      gbc.gridx = 1;
      panelCampos.add(labelTitulo, gbc);

      //Fila 1, Columna 0 y 1 ⇒ Box ISBN y Titulo
      gbc.gridy = 1;
      gbc.gridx = 0;
      panelCampos.add(boxISBN, gbc);
      gbc.gridx = 1;
      panelCampos.add(boxTitulo, gbc);

      //Fila 2, Columna 0 y 1 => Labels Autor y Año de Publicación
      gbc.gridy = 2;
      gbc.gridx = 0;
      panelCampos.add(labelAutor, gbc);
      gbc.gridx = 1;
      panelCampos.add(labelAnioPublicacion, gbc);

      //Fila 3, Columna 0 y 1 => Box Autor y Año de Publicación
      gbc.gridy = 3;
      gbc.gridx = 0;
      panelCampos.add(boxAutor, gbc);
      gbc.gridx = 1;
      panelCampos.add(boxAnioPublicacion, gbc);

      //Fila 4, Columna 0 y 1 => Labels Género y Editorial
      gbc.gridy = 4;
      gbc.gridx = 0;
      panelCampos.add(labelCategoria, gbc);
      gbc.gridx = 1;
      panelCampos.add(labelEditorial, gbc);

      //Fila 5, Columna 0 y 1 => Box Género y Editorial
      gbc.gridy = 5;
      gbc.gridx = 0;
      panelCampos.add(boxCategoria, gbc);
      gbc.gridx = 1;
      panelCampos.add(boxEditorial, gbc);

      add(panelCampos, BorderLayout.CENTER);
   }

   private void inicializarPanelFooter () {
      panelFooter = new JPanel(new GridLayout(1, 1));
      botonEliminar.setActionCommand(Evento.EVENTO.ELIMINAR_LIBRO.name());
      botonEliminar.addActionListener(evento);
      //Asignacion de fuente al boton
      botonEliminar.setFont(fuenteBoton);
      panelFooter.add(botonEliminar);
      add(panelFooter, BorderLayout.SOUTH);
      getRootPane().setDefaultButton(botonEliminar);
   }

   private void actualizadDatosLibro () {
      boxISBN.setText(String.valueOf(libro.getISBN()));
      boxTitulo.setText(libro.getTitulo());
      boxAutor.setText(libro.getAutores());
      boxAnioPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
      boxCategoria.setText(libro.getGenero());
      boxEditorial.setText(libro.getGenero());
      boxISBN.setEditable(false);
      boxTitulo.setEditable(false);
      boxAutor.setEditable(false);
      boxAnioPublicacion.setEditable(false);
      boxCategoria.setEditable(false);
      boxEditorial.setEditable(false);
   }

   public Libro getLibro () {
      return libro;
   }
}