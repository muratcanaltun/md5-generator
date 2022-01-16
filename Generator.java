import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class Generator {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("MD5 Generator");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setMinimumSize(new Dimension(500, 500));
        jFrame.setResizable(false);
        jFrame.setLayout(new GridLayout(0,1));

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(1, 2));
        JTextField filepath = new JTextField("Please choose your file path.");
        JButton browse = new JButton("Browse");
        browse.addActionListener(e -> {
            selectFile(jFrame, filepath);
        });
        browse.setMaximumSize(new Dimension(150, 100));

        filePanel.add(filepath);
        filePanel.add(browse);

        JPanel generatePanel = new JPanel();
        JButton generate = new JButton("Generate");
        generate.setMaximumSize(new Dimension(150, 100));


        generatePanel.add(generate);

        JTextField hash = new JTextField("Your MD5 hash:");

        generate.addActionListener(e -> {
            try {
                generateHash(filepath, hash);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        jFrame.add(filePanel);
        jFrame.add(generate);
        jFrame.add(hash);

        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.validate();
    }

    public static void selectFile(JFrame jFrame, JTextField path) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            path.setText(f.getAbsolutePath());
        }
    }

    public static void generateHash(JTextField pathField, JTextField hashField) throws Exception {
        hashField.setText("Your MD5 hash: " + MD5Checksum.getMD5Checksum(pathField.getText()));
    }


    public static class MD5Checksum {

        public static byte[] createChecksum(String filename) throws Exception {
            InputStream fis =  new FileInputStream(filename);

            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            return complete.digest();
        }

        // see this How-to for a faster way to convert
        // a byte array to a HEX string
        public static String getMD5Checksum(String filename) throws Exception {
            byte[] b = createChecksum(filename);
            String result = "";

            for (int i=0; i < b.length; i++) {
                result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return result;
        }
    }
}


