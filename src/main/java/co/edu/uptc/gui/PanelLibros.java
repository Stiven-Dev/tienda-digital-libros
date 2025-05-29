package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;
import co.edu.uptc.gui.Evento.EVENTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelLibros extends JPanel {
   private final  Evento            evento;
   private final  Font              fuenteCabecera = new Font("Arial", Font.BOLD, 15);
   private final  Font              fuenteCelda    = new Font("Lucida Sans Unicode", Font.PLAIN, 12);
   private final  JPanel            panelBotones   = new JPanel(new GridBagLayout());
   private        DefaultTableModel model;
   private        JTable            tableLibros;
   private static Libro             libroSeleccionado;

   public PanelLibros (Evento evento) {
      this.evento = evento;
      inicializarPanelLibros();
   }

   private DefaultTableModel getDefaultTableModel () {
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return column == 10;
         }

         @Override public Class<?> getColumnClass (int columnIndex) {
            return switch (columnIndex) {
               case 0 -> long.class;
               case 4, 9 -> int.class;
               case 8 -> double.class;
               case 10 -> Boolean.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasDoubleFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 8) {
               value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   private void inicializarPanelLibros () {
      setLayout(new BorderLayout());
      model       = getDefaultTableModel();
      tableLibros = new JTable(model) {
         @Override public Component prepareRenderer (TableCellRenderer renderer, int row, int column) {
            Component component = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
               component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220));
            } else {
               component.setBackground(Color.ORANGE);
            }
            return component;
         }
      };
      tableLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      tableLibros.setRowHeight(30);
      tableLibros.getTableHeader().setFont(fuenteCabecera);
      tableLibros.setFont(fuenteCelda);
      formatearColumnas(tableLibros);
      JScrollPane scrollPane = new JScrollPane(tableLibros);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      add(scrollPane, BorderLayout.CENTER);
      modificacionesLibros();
   }

   private void formatearColumnas (JTable tableLibros) {
      tableLibros.setDefaultRenderer(Object.class, celdasDoubleFormateadas());
   }

   void refrescarLista (VentanaPrincipal ventanaPrincipal) {
      model.setDataVector(ventanaPrincipal.obtenerVectorLibros(), NOMBRE_COLUMNAS.getCabecera());
   }

   private void modificacionesLibros () {
      model.addTableModelListener(event -> {
         int fila    = event.getFirstRow();
         int columna = event.getColumn();
         if (columna == 10) {
            agregarAlCarrito(fila);
         }
      });
   }

   private void agregarAlCarrito (int fila) {
      final int columnaAgregar = NOMBRE_COLUMNAS.AGREGAR.getIndex();
      if (!(boolean) model.getValueAt(fila, columnaAgregar)) return;
      libroSeleccionado = getLibroFila(fila);
      model.setValueAt(false, fila, columnaAgregar);
      evento.actionPerformed(new ActionEvent(this, 0, EVENTO.LIBRO_AL_CARRITO.name()));
   }

   protected void reasignarFuncionalidadAdmin () {
      int              columnaAgregar = NOMBRE_COLUMNAS.AGREGAR.getIndex();
      TableColumnModel columnModel    = tableLibros.getColumnModel();
      columnModel.removeColumn(columnModel.getColumn(columnaAgregar));
      tableLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      crearPanelBotones();
   }

   private void crearPanelBotones () {
      //Configuración de GridBagConstraints
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets  = new Insets(5, 5, 5, 5);
      gbc.anchor  = GridBagConstraints.CENTER;
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.weightx = 0.25;
      gbc.gridy   = 0;

      //Creación de botones
      JButton botonAgregar    = new JButton("Agregar Libro");
      JButton botonActualizar = new JButton("Actualizar Libro");
      JButton botonEliminar   = new JButton("Eliminar Libro");

      //Boton Actualizar
      gbc.gridx = 0;
      panelBotones.add(botonAgregar, gbc);
      //Boton Actualizar
      gbc.gridx = 1;
      panelBotones.add(botonActualizar, gbc);
      //Boton Eliminar
      gbc.gridx = 2;
      panelBotones.add(botonEliminar, gbc);

      //Asignacion de funciones a los botones
      agregarFuncionalidadBotones(botonAgregar, botonActualizar, botonEliminar);
      this.add(panelBotones, BorderLayout.SOUTH);
   }

   private void agregarFuncionalidadBotones (JButton botonAgregar, JButton botonActualizar, JButton botonEliminar) {
      //Boton Agregar
      botonAgregar.setActionCommand(EVENTO.AGREGAR_LIBRO.name());
      botonAgregar.addActionListener(evento);

      //Boton Actualizar
      botonActualizar.setActionCommand(EVENTO.ACTUALIZAR_LIBRO.name());
      botonActualizar.addActionListener(_ -> {
         int filaSeleccionada = tableLibros.getSelectedRow();
         if (filaSeleccionada >= 0) {
            libroSeleccionado = getLibroFila(filaSeleccionada);
            botonActualizar.removeActionListener(evento);
            botonActualizar.addActionListener(evento);
         }
      });

      //Boton Eliminar
      botonEliminar.setActionCommand(EVENTO.ELIMINAR_LIBRO.name());
      botonEliminar.addActionListener(_ -> {
         int filaSeleccionada = tableLibros.getSelectedRow();
         if (filaSeleccionada >= 0) {
            libroSeleccionado = getLibroFila(filaSeleccionada);
            botonEliminar.removeActionListener(evento);
            botonEliminar.addActionListener(evento);
         }
      });
   }

   private Libro getLibroFila (int filaSeleccionada) {
      Libro libro = new Libro();
      libro.setISBN((long) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.ISBN.getIndex()));
      libro.setTitulo((String) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.TITULO.getIndex()));
      libro.setAutores((String) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.AUTORES.getIndex()));
      libro.setGenero((String) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.GENERO.getIndex()));
      libro.setNumeroPaginas((int) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.PAGINAS.getIndex()));
      libro.setEditorial((String) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.EDITORIAL.getIndex()));
      libro.setAnioPublicacion((int) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.ANIO.getIndex()));
      libro.setFORMATO(Libro.FORMATOS.valueOf(String.valueOf(model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.FORMATO.getIndex()))));
      libro.setPrecioVenta((double) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.PRECIO.getIndex()));
      libro.setCantidadDisponible((int) model.getValueAt(filaSeleccionada, NOMBRE_COLUMNAS.CANTIDAD.getIndex()));
      return libro;
   }

   public static Libro getLibroSeleccionado () {
      return libroSeleccionado;
   }

   public enum NOMBRE_COLUMNAS {
      ISBN(0, "ISBN"),
      TITULO(1, "Título"),
      AUTORES(2, "Autores"),
      GENERO(3, "Género"),
      PAGINAS(4, "# Páginas"),
      EDITORIAL(5, "Editorial"),
      ANIO(6, "Año Publicación"),
      FORMATO(7, "Formato"),
      PRECIO(8, "Precio"),
      CANTIDAD(9, "Cantidad"),
      AGREGAR(10, "Agregar");
      private final int    index;
      private final String name;

      NOMBRE_COLUMNAS (int index, String name) {
         this.index = index;
         this.name  = name;
      }

      public int getIndex () {
         return index;
      }

      String getName () {
         return name;
      }

      static String[] getCabecera () {
         String[] cabecera = new String[NOMBRE_COLUMNAS.values().length];
         for (NOMBRE_COLUMNAS columna : NOMBRE_COLUMNAS.values()) {
            cabecera[columna.getIndex()] = columna.getName();
         }
         return cabecera;
      }
   }
}