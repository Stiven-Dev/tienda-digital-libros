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
         case BUSCAR_LIBRO -> {
            JButton boton = (JButton) e.getSource();
            JPanel  panel = (JPanel) boton.getClientProperty("Panel");
            ventana.buscarLibro(panel);
         }
         case GUARDAR_LIBRO_AL_CARRITO -> ventana.agregarLibroCarrito();
         case GUARDAR_LIBRO_AL_ARCHIVO -> ventana.agregarLibroArchivo();
         //case ACTUALIZAR_CLIENTE -> ventana.actualizarDatosCliente();
         case ACTUALIZAR_LIBRO -> ventana.actualizarLibro();
         case CREAR_CUENTA -> ventana.crearCuenta();
         case ELIMINAR_LIBRO -> ventana.eliminarLibro();
         case INICIAR_SESION -> ventana.validarInicioSesion();
         case LOGIN_SIGNUP -> ventana.mostrarPanelLoginSignUp();
         case PAGAR_EFECTIVO -> ventana.pagarEfectivo();
         case PAGAR_TARJETA -> ventana.pagarTarjeta();
         case REGISTRAR -> ventana.validarRegistro();
         case VALIDAR_CORREO -> ventana.validarCorreoDisponible();
      }
   }

   public enum EVENTO {
      ACTUALIZAR_LIBRO,
      ACTUALIZAR_CLIENTE,
      GUARDAR_LIBRO_AL_ARCHIVO,
      GUARDAR_LIBRO_AL_CARRITO,
      BUSCAR_LIBRO,
      CREAR_CUENTA,
      ELIMINAR_LIBRO,
      INICIAR_SESION,
      LOGIN_SIGNUP,
      PAGAR_EFECTIVO,
      PAGAR_TARJETA,
      REGISTRAR,
      VALIDAR_CORREO
   }
}