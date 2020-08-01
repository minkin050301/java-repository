import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FinalEncryptor {
	
    public static void main(String[] args) {
        String mode = "enc";
        String alg = "shift";
        String data = "";
        String in = null;
        String out = null;
        int key = 0;

        for (int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i])) {
                mode = args[i + 1];
            } else if ("-key".equals(args[i])) {
                key = Integer.parseInt(args[i + 1]);
            } else if ("-data".equals(args[i])) {
                data = args[i + 1];
            } else if ("-in".equals(args[i])) {
                in = args[i + 1];
            } else if ("-out".equals(args[i])) {
                out = args[i + 1];
            } else if ("-alg".equals(args[i])) {
                alg = args[i + 1];
            }
        }

        String message;
        EncryptorDecryptor encryptorDecryptor = new EncryptorDecryptor(alg, mode);

        if (!"".equals(data) || in == null) {
            message = data;
        } else {
            message = readFromFile(in);
        }
        
        String processedMessage = encryptorDecryptor.processMessage(message, key);
        if (out != null) {
            writeToFile(out, processedMessage);
        } else {
            System.out.println(processedMessage);
        }
    }

    public static String readFromFile(String fileName) {
        File inputFile = new File(fileName);
        try (Scanner scanner = new Scanner(inputFile)) {
            String message = "";
            while (scanner.hasNextLine()) {
                message += scanner.nextLine();
            }
            return message;
        } catch (FileNotFoundException e) {
            System.out.println("Error. " + e.getMessage());
            return "";
        }
    }

    public static void writeToFile(String fileName, String data) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(data);
        } catch (FileNotFoundException e) {
            System.out.println("Error. " + e.getMessage());
        }
    }
}

class EncryptorDecryptor {

    private EncryptionAlgorithm encryptionAlgorithm;
    private boolean encryptionMode;

    public EncryptorDecryptor(EncryptionAlgorithm encryptionAlgorithm, boolean encryptionMode) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.encryptionMode = encryptionMode;
    }

    public EncryptorDecryptor(EncryptionAlgorithm encryptionAlgorithm) {
        this(encryptionAlgorithm, true);
    }

    public EncryptorDecryptor(String algorithm, String mode) {
        if ("unicode".equals(algorithm)) {
            this.encryptionAlgorithm = new UnicodeAlgorithm();
        } else {
            this.encryptionAlgorithm = new ShiftAlgorithm();
        }
        if ("dec".equals(mode)) {
            this.encryptionMode = false;
        } else {
            this.encryptionMode = true;
        }
    }

    public String processMessage(String message, int key) {
        if (encryptionMode) {
            return encryptionAlgorithm.encrypt(message, key);
        } else {
            return encryptionAlgorithm.decrypt(message, key);
        }
    }
}

interface EncryptionAlgorithm {

    String encrypt(String message, int key);

    String decrypt(String message, int key);
}

class UnicodeAlgorithm implements EncryptionAlgorithm {

    @Override
    public String encrypt(String message, int key) {
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] += key;
        }
        return String.valueOf(chars);
    }

    @Override
    public String decrypt(String message, int key) {
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] -= key;
        }
        return String.valueOf(chars);
    }
}

class ShiftAlgorithm implements EncryptionAlgorithm {

    @Override
    public String encrypt(String message, int key) {
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = (char) ('a' + (chars[i] - 'a' + key) % 26);
            } else if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char) ('A' + (chars[i] - 'A' + key) % 26);
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public String decrypt(String message, int key) {
        String decryptedMessage = "";
        for (int i = 0; i < message.length(); ++i) {
            char ch = message.charAt(i);
            
            if (ch >= 'a' && ch <= 'z') {
                ch = (char)(ch - key);
                
                if (ch < 'a') {
                    ch = (char)(ch + 'z' - 'a' + 1);
                }
                
                decryptedMessage += ch;
            }
            else if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch - key);
                
                if (ch < 'A') {
                    ch = (char) (ch + 'Z' - 'A' + 1);
                }
                
                decryptedMessage += ch;
            }
            else {
                decryptedMessage += ch;
            }
        }
        return decryptedMessage;
    }
}