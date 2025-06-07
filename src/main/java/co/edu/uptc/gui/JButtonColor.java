package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Superclase para botones personalizados de color en la interfaz gráfica.
 * Permite definir el color por defecto y el color al presionar el botón.
 */
public class JButtonColor extends JButton {
    /**
     * Color por defecto del botón.
     */
    protected final Color defaultColor;
    /**
     * Color del botón cuando está presionado.
     */
    protected final Color pressedColor;
    /**
     * Texto mostrado en el botón.
     */
    protected final String text;

    /**
     * Constructor que inicializa el botón con los colores y el texto dados.
     * @param text texto a mostrar en el botón
     * @param defaultColor color por defecto
     * @param pressedColor color al presionar
     */
    public JButtonColor(String text, Color defaultColor, Color pressedColor) {
        super(text);
        this.text = text;
        this.defaultColor = defaultColor;
        this.pressedColor = pressedColor;
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Lucida Sans Unicode", Font.BOLD, 20));
    }

    /**
     * Dibuja el botón con el color y estilo personalizado.
     * @param g contexto gráfico
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fillColor = getModel().isPressed() ? pressedColor : defaultColor;
        g2.setColor(fillColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        setText(text);
        super.paintComponent(g);
    }
}