package main.connexion;

import javafx.scene.image.Image;
import main.rootpanels.windows.dialog.InformationDialog;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Connexion au serveur de stockage des photos
 */
public class ConnexionFTP {
    private FTPClient conn = new FTPClient();
    private String url = "ftp.cluster029.hosting.ovh.net";
    private String user = "alexlefevw";
    private String password = "aYmxmGAxk4Ecf54r";
    private int port = 21;

    /**
     * Création de la connexion
     */
    public void createConnection(){
        try {
            conn.connect(this.url, this.port);
            conn.login(this.user, this.password);
            conn.enterLocalPassiveMode();
            conn.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la création de la connexion FTP. Veuillez contacter votre administrateur.\n" + e);
        }
    }

    /**
     * Fermeture de la connexion
     */
    public void closeConnection(){
        try {
            conn.disconnect();
        } catch (IOException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la fermeture de la connexion FTP. Veuillez contacter votre administrateur.\n" + e);
        }
    }

    /**
     * Récupération de la photo
     * @param remoteFilePath Chemin de la photo
     * @return Retourne la photo
     */
    public Image retrieveImage(String remoteFilePath) {
        this.createConnection();
        Image image;
        try {
            InputStream in = conn.retrieveFileStream(remoteFilePath);
            byte[] result = in.readAllBytes();
            image = new Image(new ByteArrayInputStream(result));
        }
        catch (IOException e){
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la récupération de l'image. Veuillez contacter votre administrateur.\n" + e);
            image = null;
        }
        this.closeConnection();
        return image;
    }


    /**
     * Upload de la photo
     * @param remoteFilePath Chemin de la photo
     * @param fileImage Photo
     * @return Booléen indiquant le succès ou l'échec de l'upload
     * @throws IOException Gère l'erreur de connexion
     */
    public Boolean sendImage(String remoteFilePath, File fileImage) throws IOException {
        InputStream inputStream = new FileInputStream(fileImage);
        //résultat de l'upload
        boolean res = conn.storeFile(remoteFilePath, inputStream);
        //fermer le flux de lecture
        inputStream.close();
        return res;
    }
}
