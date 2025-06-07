package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para confirmar la eliminación de un libro del sistema.
 * Muestra los datos del libro y permite al usuario confirmar o cancelar la eliminación.
 */
public class DialogEliminarLibro extends JDialog {
   /**
    * Referencia al manejador de eventos de la aplicación.
    */
   private final Evento     evento;
   /**
    * Botón para confirmar la eliminación del libro.
    */
   private final JButton    botonEliminar   = new JButtonRojo("Eliminar");
   /**
    * Fuente utilizada para los labels.
    */
   private final Font       fuenteLabel     = new Font("Lucida Sans Unicode", Font.PLAIN, 20);
   /**
    * Fuente utilizada para los campos de texto.
    */
   private final Font       fuenteTextField = new Font("Times New Roman", Font.PLAIN, 20);
   /**
    * Fuente utilizada para el botón de eliminar.
    */
   private final Font       fuenteBoton     = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   /**
    * Libro que se va a eliminar.
    */
   private       Libro      libro;
   /**
    * Panel que contiene los campos de información del libro.
    */
   private       JPanel     panelCampos;
   /**
    * Panel que contiene el botón de eliminar.
    */
   private       JPanel     panelFooter;
   /**
    * Campo de texto para mostrar el ISBN del libro.
    */
   private       JTextField boxISBN;
   /**
    * Campo de texto para mostrar el título del libro.
    */
   private       JTextField boxTitulo;
   /**
    * Campo de texto para mostrar el/los autor(es) del libro.
    */
   private       JTextField boxAutor;
   /**
    * Campo de texto para mostrar el año de publicación del libro.
    */
   private       JTextField boxAnioPublicacion;
   /**
    * Campo de texto para mostrar el género del libro.
    */
   private       JTextField boxCategoria;
   /**
    * Campo de texto para mostrar la editorial del libro.
    */
   private       JTextField boxEditorial;

   /**
    * Constructor del diálogo de eliminación de libro.
    *
    * @param evento            manejador de eventos
    * @param libroSeleccionado libro a eliminar
    */
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

   /**
    * Inicializa los paneles principales del diálogo.
    */
   private void inicializarPanel () {
      inicializarPanelCampos();
      inicializarPanelFooter();
      add(panelCampos, BorderLayout.CENTER);
      add(panelFooter, BorderLayout.SOUTH);
   }

   /**
    * Inicializa el panel de campos de texto con los datos del libro.
    */
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

   /**
    * Inicializa el panel inferior con el botón de eliminar.
    */
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

   /**
    * Actualiza los campos del formulario con los datos del libro seleccionado.
    */
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

   /**
    * Retorna el libro que se va a eliminar.
    *
    * @return libro a eliminar
    */
   public Libro getLibroAEliminar () {
      return libro;
   }
}