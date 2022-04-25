package main.dao;

import javafx.scene.control.Dialog;
import main.connexion.Connexion;
import main.rootpanels.windows.Match;
import main.rootpanels.windows.dialog.InformationDialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO des matchs
 */
public class MatchsDAO {
    /**
     * Récupère le plus grand ID de tournoi
     * @param dialog Fenêtre de message d'erreur
     * @return Retourne l'ID ou 0 en cas d'erreur
     */
    public int getMaxTournoi(Dialog dialog){
        ResultSet maxNB = Connexion.requeteSelect("SELECT MAX(idtournoi) FROM matchs");
        try {
            maxNB.next();
            return maxNB.getInt(1);
        } catch (SQLException sqle) {
        InformationDialog informationDialog = new InformationDialog();
        informationDialog.makeDialog("Tournoi", "Ajout de tournoi", "Le tournoi n'a pas pu être ajouté.\n" + sqle);
        dialog.close();
            return 0;
        }
    }

    /**
     * Ajoute un nouveau match
     * @param content Valeurs du match
     */
    public void insertMatch(String content){
        int res = Connexion.requeteUpdate("INSERT INTO matchs (idtournoi, idjoueur1, idjoueur2, vainqueur, score, jeux, date) VALUES ('"+content+"')");
    }

    /**
     * Récupère tous les matchs triés par date
     * @return Retourne la liste des matchs
     */
    public List<Match> getMatchs(){
        List<Match> list = new ArrayList<>();
        ResultSet res = Connexion.requeteSelect("SELECT * FROM matchs ORDER BY CONVERT(DATE, date) ASC");
        Match classes;
        int lastIndex=0;
        try {
            while (res.next()) {
                classes = new Match(res.getInt(1), res.getInt(2), res.getInt(3), res.getInt(4), res.getInt(5), res.getString(6), res.getString(7), res.getString(8));
                list.add(classes);
                if(res.getInt(3) > lastIndex){
                    lastIndex=res.getInt(3);
                }
                if(res.getInt(4) > lastIndex){
                    lastIndex=res.getInt(4);
                }
            }
        }
        catch(SQLException throwables){
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la requête vers la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
        return list;
    }
}
