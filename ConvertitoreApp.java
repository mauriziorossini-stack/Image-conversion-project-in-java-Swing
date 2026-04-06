import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ConvertitoreApp extends JFrame {
    private JComboBox<String> comboOutput;
    private JLabel lblAnteprima;

    public ConvertitoreApp() {
        setTitle("Image Converter - Layout Semplice");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        //Imposto il layout principale della finestra come BorderLayou
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 45, 45));

        // 1. PARTE SUPERIORE (NORTH)
        JPanel pnlNord = new JPanel();
        pnlNord.setLayout(new BoxLayout(pnlNord, BoxLayout.Y_AXIS));
        pnlNord.setOpaque(false);
        pnlNord.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        //Header
        JLabel lblHeader = new JLabel("seleziona il tipo di output e inserisci un immagine valida");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra l'header
        lblHeader.setFont(new Font("Arial", Font.PLAIN, 14));

        //Riga Comandi (Label + Combo + Bottone)
        JPanel pnlComandi = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlComandi.setOpaque(false);

        JLabel lblConverti = new JLabel("converti in:");
        lblConverti.setForeground(Color.WHITE);

        String[] opzioni = {"jpg", "jpeg", "png", "bmp", "gif"};
        comboOutput = new JComboBox<>(opzioni);

        JButton btnScegli = new JButton("seleziona immagine");
        btnScegli.setBackground(Color.WHITE);

        pnlComandi.add(lblConverti);
        pnlComandi.add(comboOutput);
        pnlComandi.add(btnScegli);

        //pezzi al pannello Nord
        pnlNord.add(lblHeader);
        pnlNord.add(Box.createVerticalStrut(15)); // Spazio tra header e comandi
        pnlNord.add(pnlComandi);

        // 2. PARTE CENTRALE (CENTER)
        // Qui mettiamo l'anteprima. In BorderLayout, il CENTER prende tutto lo spazio rimasto
        lblAnteprima = new JLabel("anteprima immagine", SwingConstants.CENTER);
        lblAnteprima.setForeground(Color.WHITE);
        lblAnteprima.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20), // Margine esterno
                BorderFactory.createLineBorder(Color.BLACK, 3)  // Bordo nero
        ));

        //AGGIUNTA AL FRAME
        add(pnlNord, BorderLayout.NORTH);
        add(lblAnteprima, BorderLayout.CENTER);

        //Evento click
        btnScegli.addActionListener(e -> avviaProcesso());

        setVisible(true);
    }

    private void avviaProcesso() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String formato = (String) comboOutput.getSelectedItem();

            aggiornaAnteprima(file);

            try {
                ImageProcessor.converti(file, formato);
                JOptionPane.showMessageDialog(this, "File salvato correttamente!");
            } catch (Exception ex) {
                if (formato.equals("pdf") || formato.equals("webp")) {
                    JOptionPane.showMessageDialog(this, "Formato non supportato senza librerie esterne.", "Errore", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                }
            }
        }
    }

    private void aggiornaAnteprima(File file) {
        try {
            BufferedImage img = ImageIO.read(file);

            int w = lblAnteprima.getWidth() - 40;
            int h = lblAnteprima.getHeight() - 40;
            if(w <= 0) w = 300; if(h <= 0) h = 200;

            Image scalata = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            lblAnteprima.setIcon(new ImageIcon(scalata));
            lblAnteprima.setText("");
        } catch (Exception e) {
            lblAnteprima.setText("Errore anteprima");
            lblAnteprima.setIcon(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConvertitoreApp::new);
    }
}