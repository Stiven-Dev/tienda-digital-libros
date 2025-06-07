package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;
import co.edu.uptc.entity.Libro;
import co.edu.uptc.entity.Usuario;
import co.edu.uptc.gui.Evento.EVENTO;
import co.edu.uptc.model.Tienda;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;

public class PanelCarrito extends JPanel {
   private final Evento                 evento;
   private final VentanaPrincipal       ventanaPrincipal;
   private final Font                   fuenteCabecera         = new Font("Arial", Font.BOLD, 15);
   private final Font                   fuenteCelda            = new Font("Lucida Sans Unicode", Font.PLAIN, 15);
   private final Font                   fuenteBoton            = new Font("Lucida Sans Unicode", Font.BOLD, 20);
   private       int                    cantidadLibros         = 0;
   private       double                 valorTotalImpuesto     = 0;
   private       double                 subTotal               = 0;
   private       double                 total                  = 0;
   private final int                    cantidadMaximaTotal    = 20;
   private final int                    cantidadMaximaPorLibro = 10;
   private final JLabel                 labelCantidad          = new JLabel(String.format("Cantidad de libros: %d", cantidadLibros));
   private final JLabel                 labelImpuesto          = new JLabel(String.format("Impuesto: $%,.2f", valorTotalImpuesto));
   private final JLabel                 labelSubTotal          = new JLabel(String.format("Subtotal: $%,.2f", subTotal));
   private final JLabel                 labelTotal             = new JLabel(String.format("$%f", total));
   private       GridBagConstraints     gbc;
   public        DefaultTableModel      model;
   private       HashMap<Long, Integer> carritoDeCompras;

   public PanelCarrito (VentanaPrincipal ventanaPrincipal, Evento evento) {
      this.evento           = evento;
      this.ventanaPrincipal = ventanaPrincipal;
      setLayout(new GridBagLayout());
      inicializarPanelCarrito();
      carritoDeCompras = ventanaPrincipal.getCarritoActual();
      inicializarPanelFooter();
   }

   public DefaultTableModel getDefaultTableModel () {
      // La última columna es de tipo Boolean para mostrar un JCheckBox
      return new DefaultTableModel(NOMBRE_COLUMNAS.getCabecera(), 0) {
         @Override public boolean isCellEditable (int row, int column) {
            return column > 7 && column < 11;
         }

         @Override public Class<?> getColumnClass (int columna) {
            return switch (columna) {
               case 0 -> long.class;
               case 3, 4, 5, 7 -> double.class;
               case 6 -> int.class;
               case 8, 9, 10 -> Boolean.class;
               default -> String.class;
            };
         }
      };
   }

   private DefaultTableCellRenderer celdasFormateadas () {
      return new DefaultTableCellRenderer() {
         @Override public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            switch (column) {
               case 3, 4, 5, 7 -> value = String.format("$%,.2f", (double) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      };
   }

   private void inicializarPanelCarrito () {
      gbc         = new GridBagConstraints();
      gbc.insets  = new Insets(0, 0, 0, 0);
      gbc.fill    = GridBagConstraints.BOTH;
      gbc.gridx   = 0;
      gbc.gridy   = 0;
      gbc.weightx = 1;
      gbc.weighty = 0.7f;

      model = getDefaultTableModel();
      modificacionesCarrito();
      JTable tableCarrito = new JTable(model);
      tableCarrito.getTableHeader().setFont(fuenteCabecera);
      tableCarrito.setFont(fuenteCelda);
      tableCarrito.setRowHeight(30);
      formatearColumnas(tableCarrito);
      JScrollPane scrollPane = new JScrollPane(tableCarrito);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(scrollPane, gbc);
   }

   private void formatearColumnas (JTable tableCarrito) {
      tableCarrito.getColumnModel().getColumn(3).setCellRenderer(celdasFormateadas());
      tableCarrito.getColumnModel().getColumn(4).setCellRenderer(celdasFormateadas());
      tableCarrito.getColumnModel().getColumn(5).setCellRenderer(celdasFormateadas());
      tableCarrito.getColumnModel().getColumn(7).setCellRenderer(celdasFormateadas());
   }

   private void inicializarPanelFooter () {
      JPanel panelDescripcionCompra = new JPanel(new GridLayout(3, 1));
      panelDescripcionCompra.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Descripcion de la compra", TitledBorder.RIGHT, TitledBorder.TOP, fuenteCabecera));
      //Centrado de lables
      labelCantidad.setHorizontalAlignment(JLabel.RIGHT);
      labelImpuesto.setHorizontalAlignment(JLabel.RIGHT);
      labelSubTotal.setHorizontalAlignment(JLabel.RIGHT);
      //Asignacion de fuente a cada label
      labelCantidad.setFont(fuenteCelda);
      labelImpuesto.setFont(fuenteCelda);
      labelSubTotal.setFont(fuenteCelda);
      //Se añaden los labels al panel
      panelDescripcionCompra.add(labelCantidad);
      panelDescripcionCompra.add(labelImpuesto);
      panelDescripcionCompra.add(labelSubTotal);

      gbc.gridy   = 1;
      gbc.weighty = 0.15f;
      add(panelDescripcionCompra, gbc);

      //Footer
      JPanel             panelFooter = new JPanel(new GridBagLayout());
      GridBagConstraints gbcFooter   = new GridBagConstraints();
      gbcFooter.insets  = new Insets(5, 5, 5, 5);
      gbcFooter.fill    = GridBagConstraints.BOTH;
      gbcFooter.weightx = 0.5f;
      gbcFooter.weighty = 1;
      gbcFooter.gridy   = 0;

      //Panel de valor total de la compra
      JPanel panelValorTotal = new JPanel(new BorderLayout());
      panelValorTotal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Valor Total de la Compra", TitledBorder.CENTER, TitledBorder.TOP, fuenteCabecera));
      labelTotal.setHorizontalAlignment(JLabel.CENTER);
      labelTotal.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
      panelValorTotal.add(labelTotal, BorderLayout.CENTER);
      panelFooter.add(panelValorTotal, gbc);

      //Panel de botones de pago
      JPanel panelMediosDePago = new JPanel(new GridLayout(1, 3, 10, 10));
      panelMediosDePago.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Medios de Pago", TitledBorder.CENTER, TitledBorder.TOP, fuenteCabecera));
      JButton botonPagarEfectivo       = new JButtonAzul(Compra.METODO_PAGO.EFECTIVO.getName());
      JButton botonPagarTarjetaDebito  = new JButtonAzul(Compra.METODO_PAGO.TARJETA_DEBITO.getName());
      JButton botonPagarTarjetaCredito = new JButtonAzul(Compra.METODO_PAGO.TARJETA_CREDITO.getName());
      botonPagarEfectivo.setActionCommand(EVENTO.PAGAR_EFECTIVO.name());
      botonPagarTarjetaDebito.setActionCommand(EVENTO.PAGAR_TARJETA_DEBITO.name());
      botonPagarTarjetaCredito.setActionCommand(EVENTO.PAGAR_TARJETA_CREDITO.name());
      botonPagarEfectivo.addActionListener(evento);
      botonPagarTarjetaDebito.addActionListener(evento);
      botonPagarTarjetaCredito.addActionListener(evento);
      panelMediosDePago.add(botonPagarEfectivo);
      panelMediosDePago.add(botonPagarTarjetaDebito);
      panelMediosDePago.add(botonPagarTarjetaCredito);

      gbcFooter.gridx = 0;
      panelFooter.add(panelValorTotal, gbcFooter);
      gbcFooter.gridx++;
      panelFooter.add(panelMediosDePago, gbcFooter);
      //Asignacion de fuente a cada boton
      botonPagarEfectivo.setFont(fuenteBoton);
      botonPagarTarjetaDebito.setFont(fuenteBoton);
      botonPagarTarjetaCredito.setFont(fuenteBoton);

      gbc.weightx = 0.1f;
      gbc.gridy   = 2;
      gbc.gridx   = 0;

      add(panelFooter, gbc);

      actualizarLabelsCompra();
   }

   private void modificacionesCarrito () {
      model.addTableModelListener(event -> {
         int fila    = event.getFirstRow();
         int columna = event.getColumn();
         switch (columna) {
            case 8 -> sumarAlCarrito(fila);
            case 9 -> quitarAlCarrito(fila);
            case 10 -> descartarDelCarrito(fila);
            default -> {
               actualizarDatosCompra();
               return;
            }
         }
         actualizarDatosCompra();
      });
   }

   private void sumarAlCarrito (int fila) {
      final int columnaAgregar = NOMBRE_COLUMNAS.SUMAR.getIndex();
      if (!((boolean) model.getValueAt(fila, columnaAgregar))) {
         return;
      }
      if (cantidadLibros >= cantidadMaximaTotal) {
         String mensaje = String.format("No se pueden agregar mas de %d libros al carrito", cantidadMaximaTotal);
         JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
         model.setValueAt(false, fila, columnaAgregar);
         return;
      }

      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      try {
         long  ISBN     = (long) model.getValueAt(fila, columnaISBN);
         int   cantidad = (int) model.getValueAt(fila, columnaCantidad);
         Libro libro    = obtenerLibroModel(fila);
         if (carritoDeCompras.get(ISBN) >= cantidadMaximaPorLibro) {
            String mensaje = String.format("No se pueden agregar mas de %d unidades del mismo libro", cantidadMaximaPorLibro);
            JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
            return;
         }
         if (libro.getCantidadDisponible() > cantidad) {
            cantidad += 1;
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, fila, columnaCantidad);
            actualizarPrecioVenta(ISBN, fila);
            return;
         }
         String mensaje = String.format("No quedan mas unidades de \"%s\"", libro.getTitulo());
         JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
      } catch (NullPointerException e) {
         Tienda.agregarLog(e.getMessage());
      } finally {
         model.setValueAt(false, fila, columnaAgregar);
      }
   }

   private Libro obtenerLibroModel (int fila) {
      int  columnaISBN = NOMBRE_COLUMNAS.ISBN.getIndex();
      long ISBN        = (long) model.getValueAt(fila, columnaISBN);
      return ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
   }

   private void quitarAlCarrito (int fila) {
      final int columnaQuitar = NOMBRE_COLUMNAS.RESTAR.getIndex();
      if (!(boolean) model.getValueAt(fila, columnaQuitar)) {
         return;
      }
      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      try {
         int  cantidad = (int) model.getValueAt(fila, columnaCantidad);
         long ISBN     = (long) model.getValueAt(fila, columnaISBN);
         if (cantidad > 1) {
            cantidad -= 1;
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, fila, columnaCantidad);
            actualizarPrecioVenta(ISBN, fila);
            model.setValueAt(false, fila, columnaQuitar);
            return;
         }
         model.removeRow(fila);
         carritoDeCompras.remove(ISBN);
      } catch (NullPointerException e) {
         System.err.println(e.getMessage());
      }
   }

   private void descartarDelCarrito (int fila) {
      final int columnaISBN = NOMBRE_COLUMNAS.ISBN.getIndex();
      long      ISBN        = (long) model.getValueAt(fila, columnaISBN);
      carritoDeCompras.remove(ISBN);
      model.removeRow(fila);
   }

   void agregarArticulo (long ISBN) {
      actualizarDatosCompra();
      if (cantidadLibros >= cantidadMaximaTotal) {
         String mensaje = String.format("No se pueden agregar mas de %d libros al carrito", cantidadMaximaTotal);
         JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      Libro libro = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
      if (libro == null) {
         return;
      }
      if (carritoDeCompras.get(ISBN) == null) {
         carritoDeCompras.put(ISBN, 1);
         int cantidad = 1;
         rellenarDatosFilaLibro(libro, cantidad);
         actualizarDatosCompra();
         CartBooksButton.setCount(cantidadLibros);
         return;
      }
      aumentarCantidad(ISBN);
   }

   private void rellenarDatosFilaLibro (Libro libro, int cantidad) {
      Object[] datosFila = new Object[NOMBRE_COLUMNAS.values().length];
      long     ISBN      = libro.getISBN();
      datosFila[NOMBRE_COLUMNAS.ISBN.getIndex()]    = ISBN;
      datosFila[NOMBRE_COLUMNAS.TITULO.getIndex()]  = libro.getTitulo();
      datosFila[NOMBRE_COLUMNAS.AUTORES.getIndex()] = libro.getAutores();
      double valorUnitario = libro.getPrecioVenta();
      double valorImpuesto = obtenerValorImpuesto(ISBN);
      datosFila[NOMBRE_COLUMNAS.VALOR_SIN_IVA.getIndex()]      = valorUnitario - valorImpuesto;
      datosFila[NOMBRE_COLUMNAS.VALOR_IMPUESTO.getIndex()]     = valorImpuesto;
      datosFila[NOMBRE_COLUMNAS.VALOR_IVA_INCLUIDO.getIndex()] = valorUnitario;
      datosFila[NOMBRE_COLUMNAS.CANTIDAD.getIndex()]           = cantidad;
      datosFila[NOMBRE_COLUMNAS.VALOR_TOTAL.getIndex()]        = valorUnitario;
      datosFila[NOMBRE_COLUMNAS.SUMAR.getIndex()]              = false;
      datosFila[NOMBRE_COLUMNAS.RESTAR.getIndex()]             = false;
      datosFila[NOMBRE_COLUMNAS.DESCARTAR.getIndex()]          = false;
      model.addRow(datosFila);
   }

   void aumentarCantidad (long ISBN) {
      final int columnaISBN     = NOMBRE_COLUMNAS.ISBN.getIndex();
      final int columnaCantidad = NOMBRE_COLUMNAS.CANTIDAD.getIndex();
      int       cantidad        = carritoDeCompras.get(ISBN);
      Libro     libro           = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
      if (libro.getCantidadDisponible() <= cantidad) {
         String mensaje = String.format("No quedan mas unidades de \"%s\"", libro.getTitulo());
         JOptionPane.showMessageDialog(null, String.format(mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE));
         return;
      }
      if (cantidad >= cantidadMaximaPorLibro) {
         String mensaje = String.format("No se pueden agregar mas de %d unidades del mismo libro", cantidadMaximaPorLibro);
         JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      if (ventanaPrincipal.obtenerLibroMedianteISBN(ISBN).getCantidadDisponible() <= cantidad) {
         return;
      }
      cantidad += 1;
      for (int i = 0; i < model.getRowCount(); i++) {
         if ((long) model.getValueAt(i, columnaISBN) == ISBN) {
            carritoDeCompras.replace(ISBN, cantidad);
            model.setValueAt(cantidad, i, columnaCantidad);
            actualizarPrecioVenta(ISBN, i);
            return;
         }
      }
   }

   private void actualizarPrecioVenta (long ISBN, int filaModel) {
      final int columnaPrecioVenta = NOMBRE_COLUMNAS.VALOR_TOTAL.getIndex();
      double    valorUnitario      = obtenerLibroModel(filaModel).getPrecioVenta();
      int       cantidad           = carritoDeCompras.get(ISBN);
      double    precioVenta        = ventanaPrincipal.obtenerPrecioTotalProducto(valorUnitario, cantidad);
      model.setValueAt(precioVenta, filaModel, columnaPrecioVenta);
   }

   private double obtenerValorImpuesto (long ISBN) {
      double valorUnitario = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN).getPrecioVenta();
      return ventanaPrincipal.obtenerPrecioImpuesto(valorUnitario);
   }

   private void actualizarDatosCompra () {
      cantidadLibros     = ventanaPrincipal.obtenerCantidadLibros();
      valorTotalImpuesto = ventanaPrincipal.obtenerValorTotalImpuesto();
      subTotal           = ventanaPrincipal.obtenerSubTotalVenta();
      if (ventanaPrincipal.usuarioRegistrado()) {
         total = ventanaPrincipal.obtenerTotalVenta();
      } else {
         total = subTotal;
      }
      actualizarLabelsCompra();
   }

   private void actualizarLabelsCompra () {
      labelCantidad.setText(String.format("Cantidad de libros: %d", cantidadLibros));
      labelImpuesto.setText(String.format("Impuesto: $%,.2f", valorTotalImpuesto));
      labelSubTotal.setText(String.format("Subtotal: $%,.2f", subTotal));
      labelTotal.setText(String.format("$%,.2f", total));
   }

   public void refrescarLista (Usuario usuario) {
      if (usuario == null) {
         return;
      }
      if (usuario.getID() < 1) {
         return;
      }
      model.setRowCount(0);
      carritoDeCompras = ventanaPrincipal.getCarritoActual();
      for (HashMap.Entry<Long, Integer> entry : carritoDeCompras.entrySet()) {
         long ISBN                = entry.getKey();
         int  cantidadEnCarrito   = entry.getValue();
         int  unidadesDisponibles = ventanaPrincipal.unidadesDisponibles(ISBN);
         if (unidadesDisponibles < 1) {
            carritoDeCompras.remove(ISBN);
            ventanaPrincipal.eliminarLibroDelCarrito(ISBN, usuario.getID());
            String mensaje = String.format("El Libro con ISBN %d ya no cuenta con unidades", ISBN);
            JOptionPane.showMessageDialog(null, mensaje, "Alerta", JOptionPane.ERROR_MESSAGE);
            continue;
         }
         if (unidadesDisponibles < cantidadEnCarrito) {
            carritoDeCompras.replace(entry.getKey(), unidadesDisponibles);
            String mensaje = String.format("El libro con ISBN %d solo cuenta con %d unidades disponibles", ISBN, unidadesDisponibles);
            JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
            continue;
         }
         agregarLibroAlCarrito(ISBN);
      }
   }

   private void agregarLibroAlCarrito (long ISBN) {
      Libro libro = ventanaPrincipal.obtenerLibroMedianteISBN(ISBN);
      if (libro == null) {
         return;
      }
      int cantidad = carritoDeCompras.get(ISBN);
      rellenarDatosFilaLibro(libro, cantidad);
      CartBooksButton.setCount(cantidadLibros);
   }

   public int getCantidadLibros () {
      return cantidadLibros;
   }

   public void vaciarCarrito () {
      model.setRowCount(0);
      carritoDeCompras.clear();
      cantidadLibros     = 0;
      valorTotalImpuesto = 0;
      subTotal           = 0;
      total              = 0;
      actualizarLabelsCompra();
      CartBooksButton.setCount(cantidadLibros);
   }

   public enum NOMBRE_COLUMNAS {
      ISBN(0, "ISBN"),
      TITULO(1, "Título"),
      AUTORES(2, "Autores"),
      VALOR_SIN_IVA(3, "Vlr"),
      VALOR_IMPUESTO(4, "Vlr Impuesto"),
      VALOR_IVA_INCLUIDO(5, "Vlr +IVA"),
      CANTIDAD(6, "Cantidad"),
      VALOR_TOTAL(7, "Vlr Total"),
      SUMAR(8, "+"),
      RESTAR(9, "-"),
      DESCARTAR(10, "x");
      private final int    index;
      private final String name;

      NOMBRE_COLUMNAS (int index, String name) {
         this.index = index;
         this.name  = name;
      }

      public int getIndex () {
         return index;
      }

      static String[] getCabecera () {
         String[] cabecera = new String[NOMBRE_COLUMNAS.values().length];
         for (NOMBRE_COLUMNAS columna : NOMBRE_COLUMNAS.values()) {
            cabecera[columna.getIndex()] = columna.name;
         }
         return cabecera;
      }
   }
}