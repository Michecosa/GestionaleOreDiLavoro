package esercizio.main;

import java.awt.Color;

/**
 * Palette colori centralizzata dell'applicazione.
 * Tutti i colori sono definiti qui per garantire coerenza visiva.
 */
public final class AppColors {

    private AppColors() {} // Classe non istanziabile

    public static final Color BG        = new Color(0xF4, 0xF7, 0xF6); // Sfondo molto chiaro, quasi bianco
    public static final Color PANEL_BG  = Color.WHITE;                  // Pannelli bianchi per stacco
    public static final Color CARD_BG   = Color.WHITE;                  // Schede bianche
    public static final Color ACCENT    = new Color(0x00, 0x56, 0xD2); // Blu scuro e saturo (accessibile)
    public static final Color ACCENT2   = new Color(0x62, 0x00, 0xEA); // Viola scuro (accessibile)
    public static final Color TEXT_MAIN = new Color(0x1A, 0x1D, 0x21); // Testo quasi nero
    public static final Color TEXT_SUB  = new Color(0x5F, 0x63, 0x68); // Grigio medio-scuro per sottotitoli
    public static final Color DANGER    = new Color(0xD3, 0x2F, 0x2F); // Rosso scuro
    public static final Color SUCCESS   = new Color(0x1B, 0x5E, 0x20); // Verde scuro
    public static final Color ROW_ALT   = new Color(0xF8, 0xF9, 0xFA); // Grigio chiarissimo per righe alterne
    public static final Color BORDER    = new Color(0xDA, 0xDF, 0xE4); // Colore bordi standard
}