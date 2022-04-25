package main.connexion;

import main.rootpanels.windows.dialog.InformationDialog;

import java.sql.*;


/**
 * Gestion des requêtes SQL vers la base de données.
 */
public class Connexion {
    private static Connection conn = null;
    private static String url = "jdbc:mysql://mysql-ts20.alwaysdata.net:3306/ts20_1";
    private static String user = "ts20";
    private static String password = "Q>u:39J9.6b*[9ZcGcvH";

    /**
     * Récupère l'URL de la base.
     * @return Retourne l'URL de la base.
     */
    public static String getUrl() {
        return url;
    }


    /**
     * Definit la valeur de l'URL de la base.
     * @param url URL de la base.
     */
    public static void setUrl(String url) {
        Connexion.url = url;
    }

    /**
     * Récupère le nom d'utilisateur pour la connexion à la base
     * @return Retourne le nom d'utilisateur pour la connexion à la base
     */
    public static String getUser() {
        return user;
    }

    /**
     * Definit le nom d'utilisateur de la base.
     * @param user nom d'utilisateur
     */
    public static void setUser(String user) {
        Connexion.user = user;
    }

    /**
     * Récupère le mot de passe de la base.
     * @return Retourne le mot de passe de la base.
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Definit le mot de passe de la base.
     * @param password mot de passe de la base.
     */
    public static void setPassword(String password) {
        Connexion.password = password;
    }

    /**
     * Chargement du driver nécessaire pour la connexion à la base.
     */
    public static void loadDriver(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la tentative de connexion dû au driver. Veuillez contacter votre administrateur.\n" + e);
        }
    }

    /**
     * Crée une instance de connexion à la base.
     */
    public static void createConnection(){
        try {
            conn= DriverManager.getConnection(url,user, password);
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la création de la connexion. Veuillez contacter votre administrateur.\n" + e);
        }
    }

    /**
     * Ferme l'instance de connexion à la base.
     */
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur de connexion", "Une erreur est survenue lors de la connexion", "Une erreur est survenue lors de la fermeture de la connexion. Veuillez contacter votre administrateur.\n" + e);
        }
    }

    /**
     * Exécute une requête de type select vers la base.
     * @param req Requête à exécuter.
     * @return Retourne le nombre de ligne qui ont été modifiées
     */
    public static ResultSet requeteSelect(String req){
        try {
            checkTimeOut();
        } catch (SQLException throwables) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur lors de la reconnexion", "Une erreur est survenue lors de la reconnexion", "Une erreur est survenue lors de la reconnexion à la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
        ResultSet res = null;
        try {
            Statement stmt = conn.createStatement();
            res = stmt.executeQuery(req);
        } catch (SQLException throwables) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur lors de la requête", "Une erreur est survenue lors de la requête", "Une erreur est survenue lors de la requête à la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
        return res;
    }

    /**
     * Exécute une requête de type update vers la base.
     * @param req Requête à exécuter.
     * @return Retourne le nombre de ligne qui ont été modifiées
     */
    public static int requeteUpdate(String req){
        try {
            checkTimeOut();
        } catch (SQLException throwables) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur lors de la reconnexion", "Une erreur est survenue lors de la reconnexion", "Une erreur est survenue lors de la reconnexion à la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
        Statement stmt = null;
        int ligneModifiees = 0;
        try {
            stmt = conn.createStatement();
            ligneModifiees = stmt.executeUpdate(req);
        } catch (SQLException throwables) {
            InformationDialog informationDialog = new InformationDialog();
            informationDialog.makeDialog("Erreur lors de la requête", "Une erreur est survenue lors de la requête", "Une erreur est survenue lors de la requête à la base. Veuillez contacter votre administrateur.\n" + throwables);
        }
        return ligneModifiees;
    }

    /**
     * Vérifie si la connexion à la base est expirée, et la recrée si nécéssaire.
     * @throws SQLException Gère l'erreur de connexion
     */
    private static void checkTimeOut() throws SQLException {
        if(!conn.isValid(3)){
            conn=null;
            createConnection();
        }
    }
}
