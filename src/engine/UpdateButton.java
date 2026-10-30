package engine;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class UpdateButton extends Button {

    // Inside your event listener for the updateButton:
    public UpdateButton(String s) {
        super(s);
        setOnAction(event -> {
            String downloadUrl = "https://8-Bit-Weather.com/8BitWeather-Setup-latest.msi";

            // Save to user's temp directory
            String tempDir = System.getProperty("java.io.tmpdir");
            File tempMsiFile = new File(tempDir, "8BitWeather_Update.msi");

            // Your exact certificate thumbprint (uppercase, no spaces)
            String expectedThumbprint = "6BD476DA1FA3E39E0069556621AB9EB9CF8AA111";

            // Disable button to prevent multiple clicks
            setDisable(true);

            // Run in a background thread so your app UI doesn't freeze during download
            new Thread(() -> {
                try {
                    // 1. Download the MSI via HTTPS
                    try (BufferedInputStream in = new BufferedInputStream(new URI(downloadUrl).toURL().openStream());
                            FileOutputStream fileOutputStream = new FileOutputStream(tempMsiFile)) {
                        byte[] dataBuffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                    }

                    // 2. Extract and verify the certificate thumbprint
                    String actualThumbprint = getMsiThumbprint(tempMsiFile);

                    if (expectedThumbprint.equalsIgnoreCase(actualThumbprint)) {
                        // 3. THUMBPRINT MATCHES: Run the installer safely
                        ProcessBuilder pb = new ProcessBuilder("msiexec.exe", "/i", tempMsiFile.getAbsolutePath());
                        pb.start();

                        // Exit your Java app so the installer can update the files
                        System.exit(0);
                    } else {
                        tempMsiFile.delete();
                        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR,
                                "Signature mismatch. Expected " + expectedThumbprint + " but got " + actualThumbprint)
                                .show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Update failed: " + e).show());
                } finally {
                    Platform.runLater(() -> setDisable(false));
                }
            }).start();
        });
    }

    private String getMsiThumbprint(File file) throws Exception {

        String path = file.getAbsolutePath().replace("'", "''");

        Process p = new ProcessBuilder("powershell", "-NoProfile", "-Command",
                "$s = Get-AuthenticodeSignature -LiteralPath '" + path + "'; " +
                        "$s.Status; $s.SignerCertificate.Thumbprint")
                .redirectErrorStream(true).start();

        String[] lines = new String(p.getInputStream().readAllBytes()).trim().split("\\R");

        p.waitFor();

        String status = lines[0].trim();
        if (status.equals("NotSigned") || status.equals("HashMismatch"))
            throw new IOException("Bad signature: " + status);
        if (lines.length < 2 || lines[1].isBlank())
            throw new IOException("No signer certificate found.");

        return lines[1].trim();
    }
}
