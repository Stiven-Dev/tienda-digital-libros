package co.edu.uptc.gui;

import co.edu.uptc.entity.Compra;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Evento implements ActionListener {
   private final VentanaPrincipal ventanaPrincipal;

   public Evento (VentanaPrincipal ventanaPrincipal) {
      this.ventanaPrincipal = ventanaPrincipal;
   }

   @Override public void actionPerformed (ActionEvent e) {
      String actionCommand = e.getActionCommand();
      EVENTO nombreEvento  = EVENTO.valueOf(actionCommand);
      switch (nombreEvento) {
         //Eventos para Administradores
         case MOSTRAR_DIALOG_AGREGAR_LIBRO -> ventanaPrincipal.dialogAgregarLibro();
         case ACTUALIZAR_LIBRO -> ventanaPrincipal.actualizarLibro();
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
         case PAGAR_EFECTIVO, PAGAR_TARJETA_CREDITO, PAGAR_TARJETA_DEBITO -> {
            ventanaPrincipal.setMetodoPago(Compra.METODO_PAGO.valueOf(actionCommand));
            ventanaPrincipal.pagar();
         }
         case REGISTRAR_CLIENTE -> ventanaPrincipal.validarRegistro();
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
      PAGAR_TARJETA_DEBITO,
      PAGAR_TARJETA_CREDITO,
      REGISTRAR_CLIENTE,
      REGISTRAR_LIBRO,
      MOSTRAR_DETALLES_COMPRA,
      VALIDAR_CORREO
   }
}