package za.co.healthfirstpims;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.UIManager;

public final class Theme {

    // HealthFirst Pharmacy dark-premium colour palette
    public static final Color BACKGROUND = new Color(18, 20, 24);
    public static final Color SURFACE = new Color(30, 33, 39);
    public static final Color SURFACE_LIGHT = new Color(43, 47, 54);

    public static final Color EMERALD = new Color(24, 160, 112);
    public static final Color EMERALD_DARK = new Color(15, 112, 78);
    public static final Color GOLD = new Color(212, 175, 55);

    public static final Color TEXT_PRIMARY =
            new Color(245, 242, 232);

    public static final Color TEXT_SECONDARY =
            new Color(171, 177, 187);

    public static final Color DANGER =
            new Color(220, 75, 75);

    public static final Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 28);

    public static final Font HEADING_FONT =
            new Font("Segoe UI", Font.BOLD, 18);

    public static final Font BODY_FONT =
            new Font("Segoe UI", Font.PLAIN, 14);

    private Theme() {
        // Prevents Theme objects from being created
    }

    public static void applyGlobalStyle() {
        // Only change the text cursor colours globally.
        // This keeps JOptionPane messages readable.
        UIManager.put("TextField.caretForeground", GOLD);
        UIManager.put("PasswordField.caretForeground", GOLD);
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(EMERALD);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}