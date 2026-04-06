import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    //Formati supportati nativamente da Java SE
    public static final String[] FORMATI_NATIVI = {"jpg", "jpeg", "png", "gif", "bmp"};

    public static void converti(File fileInput, String formatoOutput) throws Exception {
        //Caricamento immagine
        BufferedImage immagine = ImageIO.read(fileInput);
        if (immagine == null) throw new IOException("File non supportato o corrotto.");

        //Generazione nuovo percorso
        String path = fileInput.getAbsolutePath();
        int lastDot = path.lastIndexOf('.');
        String nuovoPath = (lastDot == -1) ? path + "." + formatoOutput : path.substring(0, lastDot) + "." + formatoOutput;

        File fileOutput = new File(nuovoPath);

        //Scrittura (ImageIO gestisce jpeg, jpg, png, bmp, gif nativamente)
        boolean successo = ImageIO.write(immagine, formatoOutput, fileOutput);

        if (!successo) {
            throw new Exception("Il formato " + formatoOutput + " non è supportato nativamente da Java.");
        }
    }
}