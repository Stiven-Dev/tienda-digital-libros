package co.edu.uptc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Evento implements ActionListener {
   private final VentanaPrincipal ventana;

   public Evento (VentanaPrincipal ventana) {
      this.ventana = ventana;
   }

   @Override public void actionPerformed (ActionEvent e) {
      String actionCommand = e.getActionCommand();
      EVENTO nombreEvento  = EVENTO.valueOf(actionCommand);
      switch (nombreEvento) {
         //Eventos para Administradores
         case MOSTRAR_DIALOG_AGREGAR_LIBRO -> ventana.dialogAgregarLibro();
         case ACTUALIZAR_LIBRO -> ventana.actualizarLibro();
         case CREAR_CUENTA -> ventana.crearCuenta();
         case ELIMINAR_LIBRO -> ventana.eliminarLibro();
         case REGISTRAR_LIBRO -> ventana.registrarLibro();
         case VALIDAR_CORREO -> ventana.validarCorreoDisponible();

         //Eventos para Clientes
         case ACTUALIZAR_CLIENTE -> ventana.actualizarDatosCliente();
         case CERRAR_SESION -> ventana.cerrarSesion();
         case INICIAR_SESION -> ventana.validarInicioSesion();
         case LIBRO_AL_CARRITO -> ventana.agregarLibroCarrito();
         case LOGIN_SIGNUP -> ventana.mostrarPanelLoginSignUp();
         case MOSTRAR_DETALLES_COMPRA -> ventana.mostrarDetallesCompra();
         case PAGAR_EFECTIVO -> ventana.pagarEfectivo();
         case PAGAR_TARJETA -> ventana.pagarTarjeta();
         case REGISTRAR_CLIENTE -> ventana.validarRegistro();
      }
   }

   public enum EVENTO {
      ACTUALIZAR_LIBRO,
      ACTUALIZAR_CLIENTE,
      MOSTRAR_DIALOG_AGREGAR_LIBRO,
      CERRAR_SESION,
      CREAR_CUENTA,
      ELIMINAR_LIBRO,
      INICIAR_SESION,
      LIBRO_AL_CARRITO,
      LOGIN_SIGNUP,
      PAGAR_EFECTIVO,
      PAGAR_TARJETA,
      REGISTRAR_CLIENTE,
      REGISTRAR_LIBRO,
      MOSTRAR_DETALLES_COMPRA,
      VALIDAR_CORREO
   }
}