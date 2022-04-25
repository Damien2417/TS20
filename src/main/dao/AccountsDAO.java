package main.dao;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.connexion.Connexion;
import main.rootpanels.windows.dialog.InformationDialog;
import main.variables.Variables;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO des comptes
 */
public class AccountsDAO {
    /**
     * Vérification de l'existence du compte
     * @param userTextField Input du nom de compte
     * @param pwBox Input du mot de passe
     * @return String du message à afficher et du résultat
     */
    public String essaiLogin(TextField userTextField, PasswordField pwBox){
        String username = "";
        String password = "";
        String returnString = "";
        username = userTextField.getText();
        password = returnHash(pwBox.getText());
        ResultSet userData = Connexion.requeteSelect("SELECT name, id, photo FROM accounts WHERE username='"+username+"' AND password='"+password+"'");
        try {
            if (userData.next()) {
                Variables.userName = userData.getString(1);
                Variables.userId = userData.getString(2);
                returnString = "Success";
            }else{
                returnString = "Absence d'identifiant ou mot de passe erroné.";
            }
        } catch (SQLException throwables) {
            returnString = "Impossible de vérifier la présence de l'utilisateur.";
        }
        return returnString;
    }

    /**
     * Cryptage du mot de passe
     * @param password Mot de passe
     * @return Mot de passe crypté
     */
    private String returnHash(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur mot de passe", "Une erreur est survenue lors du hashage", "Une erreur est survenue lors du hashage du mot de passe. Veuillez contacter votre administrateur.\n" + e);
        }
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        return hashtext;
    }
}
