package main.dao;

import javafx.scene.control.Label;
import main.connexion.Connexion;
import main.connexion.ConnexionFTP;
import main.rootpanels.windows.dialog.InformationDialog;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * DAO des joueurs
 */
public class JoueursDAO {

    /**
     * Ajout d'un nouveau joueur
     * @param nom Nom du joueur
     * @param prenom Prénom du joueur
     * @param club Club du joueur
     * @param errorMessage Label affichant le message d'erreur si nécessaire
     * @param extension Format de la photo
     * @param imageSelected True si une image a été ajoutée
     * @param image Photo du joueur
     * @return Booléen indiquant si le joueur a été ajouté
     */
    public boolean sendJoueur(String nom, String prenom, String club, Label errorMessage, String extension, boolean imageSelected, File image) {
        //Joueur déjà existant
        try {
            errorMessage.setVisible(false);
            ResultSet userData = Connexion.requeteSelect("SELECT id FROM joueurs WHERE nom='" + nom + "' AND prenom='" + prenom + "' AND club='" + club + "'");
            if (userData.next()) {
                errorMessage.setText("Joueur déjà existant");
                errorMessage.setVisible(true);
                return false;
            }else{
                //Insert du joueur
                try {
                    Connexion.requeteUpdate("INSERT INTO joueurs (nom, prenom, club, photo) VALUES ('" + nom + "','" + prenom + "','" + club + "', '" + extension + "')");

                    userData = Connexion.requeteSelect("SELECT id FROM joueurs WHERE nom='" + nom + "' AND prenom='" + prenom + "' AND club='" + club + "'");
                    userData.next();
                    String id = userData.getString(1);
                    if (imageSelected == true) {
                        ConnexionFTP connexion = new ConnexionFTP();
                        connexion.createConnection();
                        connexion.sendImage("/www/imagesJoueursProjetJava/" + id + "." + extension, image);
                        connexion.closeConnection();
                    }
                }
                catch (SQLException | IOException throwables){
                    InformationDialog informationDialog = new InformationDialog();
                    informationDialog.makeDialog("Erreur ajout de joueur", "Une erreur est survenue lors de l'ajout du joueur", "Une erreur est survenue lors de l'ajout du joueur. Veuillez contacter votre administrateur.");
                }
                return true;
            }
        }
        catch (SQLException e){
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur lors de la récupération de la base", "Une erreur est survenue lors de l'ajout du joueur", "Une erreur est survenue lors de la récuperation de la base. Veuillez contacter votre administrateur.\n" + e);
        }
        return false;
    }


    /**
     * Récupération de tous les joueurs
     * @param listJoueurs Liste des joueurs
     * @return Retourne la liste
     */
    public ArrayList<String> getJoueurs(ArrayList<String> listJoueurs){
        ResultSet res = Connexion.requeteSelect("SELECT nom, prenom, id FROM joueurs");
        try {
            while (res.next()) {
                String nom = res.getString(1);
                String prenom = res.getString(2);
                String id = "%" + res.getString(3);

                listJoueurs.add(nom + " " + prenom + id);
            }
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la requête vers la base. Veuillez contacter votre administrateur.\n" + e);
        }
        return listJoueurs;
    }

    /**
     * Récupère le plus grand ID
     * @return Retourne l'ID
     */
    public int getMaxId(){
        ResultSet countResultSet = Connexion.requeteSelect("SELECT MAX(id) FROM joueurs");
        int result=0;
        try {
            countResultSet.next();
            result = countResultSet.getInt(1);
            return result;
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la connexion à la base. Veuillez contacter votre administrateur.\n" + e);
        }
        return result;
    }

    /**
     * Récupération des joueurs du tournoi
     * @param idArray Premier joueurs
     * @param sqlId Autres joueurs
     * @param mapNomJoueurs Map des joueurs
     */
    public void fillMapNomJoueurs(int idArray, String sqlId, Map mapNomJoueurs){
        ResultSet joueursInfo = Connexion.requeteSelect("SELECT id,nom,prenom from joueurs WHERE id="+idArray+sqlId);

        try {
            while (joueursInfo.next()) {
                mapNomJoueurs.put(joueursInfo.getInt(1), joueursInfo.getString(2) + " " + joueursInfo.getString(3));
            }
        }
        catch (SQLException throwables) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la requête vers la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
    }

    /**
     * Récupération du nombre de joueurs
     * @return Retourne le nombre de joueurs
     */
    public double getNbJoueur(){
        ResultSet countResultSet = Connexion.requeteSelect("SELECT COUNT(id) FROM joueurs");
        int result=0;
        try {
            countResultSet.next();
            result = countResultSet.getInt(1);
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la connexion à la base. Veuillez contacter votre administrateur.\n" + e);
        }
        return result;
    }

    /**
     * Récupération du nombre de joueurs correspondant à la recherche
     * @param search Recherche de l'utilisateur
     * @return Retourne le nombre de joueurs correspondant à la recherche
     */
    public double getNbJoueurSearch(String search){
        ResultSet userDataCount = Connexion.requeteSelect("SELECT COUNT(id) FROM joueurs"+search);
        try {
            userDataCount.next();
            return ((Double.parseDouble(userDataCount.getString(1))/8)+ 0.875);
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la connexion à la base. Veuillez contacter votre administrateur.\n" + e);
            return 0.00;
        }
    }

    /**
     * @param request Joueurs devant être récupérés
     * @return Tableau des joueurs
     */
    public String[][] getJoueursPage(String request){
        String[][] array = new String[8][4];
        ResultSet userData = Connexion.requeteSelect(request);
        int i=0;
        try {
            while(userData.next()){
                array[i][0] = userData.getString(1);
                array[i][1] = userData.getString(2);
                array[i][2] = userData.getString(3);
                array[i][3] = userData.getString(4);
                i++;
            }
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la connexion à la base. Veuillez contacter votre administrateur.\n" + e);
        }

        return array;
    }

}
