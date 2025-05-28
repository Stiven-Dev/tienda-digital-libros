package co.edu.uptc.gui;

import javax.swing.*;
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
         case AGREGAR_LIBRO -> ventana.agregarLibro();
         case ACTUALIZAR_LIBRO -> ventana.actualizarLibro();
         case BUSCAR_LIBRO -> buscarLibro(e);
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
         case PAGAR_EFECTIVO -> ventana.pagarEfectivo();
         case PAGAR_TARJETA -> ventana.pagarTarjeta();
         case REGISTRAR -> ventana.validarRegistro();
      }
   }

   private void buscarLibro (ActionEvent e) {
      JButton boton = (JButton) e.getSource();
      JPanel  panel = (JPanel) boton.getClientProperty("Panel");
      ventana.buscarLibroParaPanelModificaciones(panel);
   }

   public enum EVENTO {
      ACTUALIZAR_LIBRO,
      ACTUALIZAR_CLIENTE,
      AGREGAR_LIBRO,
      BUSCAR_LIBRO,
      CERRAR_SESION,
      CREAR_CUENTA,
      ELIMINAR_LIBRO,
      INICIAR_SESION,
      LIBRO_AL_CARRITO,
      LOGIN_SIGNUP,
      PAGAR_EFECTIVO,
      PAGAR_TARJETA,
      REGISTRAR,
      REGISTRAR_LIBRO,
      VALIDAR_CORREO
   }
}