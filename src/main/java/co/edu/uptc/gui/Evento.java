package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona los eventos de la interfaz gráfica.
 * Implementa ActionListener y centraliza el manejo de acciones de usuario.
 */
public class Evento implements ActionListener {
   /**
    * Referencia a la ventana principal de la aplicación.
    */
   private final VentanaPrincipal ventanaPrincipal;

   /**
    * Constructor que recibe la ventana principal para delegar acciones.
    *
    * @param ventanaPrincipal ventana principal de la aplicación
    */
   public Evento (VentanaPrincipal ventanaPrincipal) {
      this.ventanaPrincipal = ventanaPrincipal;
   }

   /**
    * Maneja los eventos de acción generados por la interfaz gráfica.
    *
    * @param e evento de acción
    */
   @Override public void actionPerformed (ActionEvent e) {
      String actionCommand = e.getActionCommand();
      EVENTO nombreEvento  = EVENTO.valueOf(actionCommand);
      switch (nombreEvento) {
         //Eventos para Administradores
         case MOSTRAR_DIALOG_AGREGAR_LIBRO -> ventanaPrincipal.agregarLibro();
         case ACTUALIZAR_LIBRO -> ventanaPrincipal.actualizarLibro();
         case ACTUALIZAR_CLIENTE_ADMIN -> ventanaPrincipal.actualizarDatosClienteAdmin();
         case CREAR_CUENTA -> ventanaPrincipal.crearCuenta();
         case ELIMINAR_LIBRO -> ventanaPrincipal.eliminarLibro();
         case REGISTRAR_LIBRO -> ventanaPrincipal.registrarLibro();
         case VALIDAR_CORREO -> ventanaPrincipal.validarCorreoDisponible();

         //Eventos para Clientes
         case ACTUALIZAR_CLIENTE -> ventanaPrincipal.actualizarDatosCliente();
         case CERRAR_SESION -> ventanaPrincipal.cerrarSesion();
         case INICIAR_SESION -> ventanaPrincipal.validarInicioSesion();
         case LIBRO_AL_CARRITO -> ventanaPrincipal.agregarLibroCarrito();
         case LOGIN_SIGNUP -> ventanaPrincipal.mostrarPanelLoginSignUp();
         case MOSTRAR_DETALLES_COMPRA -> ventanaPrincipal.mostrarDetallesCompra();
         case EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO -> {
            ventanaPrincipal.setMetodoPago(Compra.METODO_PAGO.valueOf(actionCommand));
            ventanaPrincipal.pagar();
         }
         case REGISTRAR_CLIENTE -> ventanaPrincipal.validarRegistro();
      }
   }

   /**
    * Enum que define todos los eventos posibles en la aplicación.
    */
   public enum EVENTO {
      ACTUALIZAR_LIBRO,           // Actualizar información de un libro
      ACTUALIZAR_CLIENTE,         // Actualizar datos de un cliente
      ACTUALIZAR_CLIENTE_ADMIN,   // Actualizar datos de un cliente desde el panel de administrador
      MOSTRAR_DIALOG_AGREGAR_LIBRO, // Mostrar diálogo para agregar libro
      CERRAR_SESION,              // Cerrar sesión de usuario
      CREAR_CUENTA,               // Crear una nueva cuenta
      ELIMINAR_LIBRO,             // Eliminar un libro
      INICIAR_SESION,             // Iniciar sesión de usuario
      LIBRO_AL_CARRITO,           // Agregar libro al carrito
      LOGIN_SIGNUP,               // Mostrar panel de login/registro
      EFECTIVO,                   // Selección de pago en efectivo
      TARJETA_DEBITO,             // Selección de pago con tarjeta débito
      TARJETA_CREDITO,            // Selección de pago con tarjeta crédito
      REGISTRAR_CLIENTE,          // Registrar un nuevo cliente
      REGISTRAR_LIBRO,            // Registrar un nuevo libro
      MOSTRAR_DETALLES_COMPRA,    // Mostrar detalles de una compra
      VALIDAR_CORREO              // Validar disponibilidad de correo
   }
}