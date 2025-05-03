package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;

import javax.swing.*;
import java.awt.*;

public class PanelEliminarLibro extends JPanel implements PanelLibroModificable {
   private final Evento     evento;
   private final JButton    botonEliminar   = new JButton("Eliminar");
   private final Font       fuenteLabel     = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   private final Font       fuenteTextField = new Font("Times New Roman", Font.PLAIN, 20);
   private final Font       fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private final JLabel     mensajeDeError  = new JLabel();
   private       Libro      libro           = null;
   private       JPanel     panelCampos;
   private       JPanel     panelFooter;
   private       JTextField boxISBN;
   private       JTextField boxTitulo;
   private       JTextField boxAutor;
   private       JTextField boxAnioPublicacion;
   private       JTextField boxCategoria;
   private       JTextField boxEditorial;

   public PanelEliminarLibro (Evento evento) {
      this.evento = evento;
      setLayout(new BorderLayout());
      inicializarPanel();
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
      panelFooter = new JPanel(new GridLayout(2, 1));
      JButton botonBuscar = new JButton("Buscar");
      botonBuscar.setActionCommand(Evento.EVENTO.BUSCAR_LIBRO.name());
      botonBuscar.putClientProperty("Panel", this);
      botonBuscar.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         if (mensajeDeError.getText().isBlank()) {
            botonBuscar.removeActionListener(evento);
            botonBuscar.addActionListener(evento);
         }
      });
      botonBuscar.addActionListener(evento);

      JButton botonEliminar = new JButton("Eliminar");
      botonEliminar.setActionCommand(Evento.EVENTO.ELIMINAR_LIBRO.name());
      botonEliminar.addActionListener(_ -> {
         mensajeDeError.setText(obtenerMensajeDeError());
         if (mensajeDeError.getText().isBlank()) {
            botonEliminar.removeActionListener(evento);
            botonEliminar.addActionListener(evento);
         }
      });

      //Asignacion de fuente al boton
      botonBuscar.setFont(fuenteBoton);
      botonEliminar.setFont(fuenteBoton);

      panelFooter.add(botonBuscar);
      panelFooter.add(botonEliminar);

      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setFont(new Font("Arial", Font.BOLD, 20));
      mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
      panelFooter.add(mensajeDeError);

      add(panelFooter, BorderLayout.SOUTH);
   }

   private String obtenerMensajeDeError () {
      //Validacion de Campo Vacio del ISBN
      if (boxISBN.getText().isEmpty()) {
         return "Debe rellenar el campo ISBN";
      }

      //Validacion de ISBN
      if (!boxISBN.getText().matches("^[0-9]{10,13}$")) {
         return "El campo ISBN debe tener de 10 a 13 caracteres numéricos";
      }

      return "";
   }

   long getISBN () {
      if (boxISBN.getText().isEmpty()) {
         return -1;
      }
      return Long.parseLong(boxISBN.getText());
   }

   @Override public void setDatosLibro (Libro libro) {
      this.libro = libro;
      actualizadDatosLibro();
   }

   @Override public void setMensajeError () {
      mensajeDeError.setForeground(Color.RED);
      mensajeDeError.setText("Libro no encontrado / no apto para eliminar");
   }

   @Override public void setMensajeConfirmacion (String mensajeConfirmacion) {
      mensajeDeError.setForeground(Color.GREEN);
      mensajeDeError.setText(mensajeConfirmacion);
   }

   private void actualizadDatosLibro () {
      mensajeDeError.setForeground(Color.GREEN);
      mensajeDeError.setText("Libro encontrado");

      boxTitulo.setText(libro.getTitulo());
      boxAutor.setText(libro.getAutores());
      boxAnioPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
      boxCategoria.setText(libro.getCategoria());
      boxEditorial.setText(libro.getGenero());
   }
}