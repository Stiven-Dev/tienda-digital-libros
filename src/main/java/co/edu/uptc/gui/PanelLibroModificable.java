package co.edu.uptc.gui;

import co.edu.uptc.entity.Libro;

public interface PanelLibroModificable {
   void setDatosLibro (Libro libro);

   void setMensajeError ();

   void setMensajeConfirmacion (String mensajeConfirmacion);
}