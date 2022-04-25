package main.rootpanels.windows;

/**
 * Objet match
 */
public class Match {
    private int id;
    private int idtournoi;
    private int idjoueur1;
    private int idjoueur2;
    private int vainqueur;
    private String score;
    private String jeux;
    private String date;

    /**
     * Constructeur basique
     */
    public Match() {
    }

    /**
     * Constructeur complet
     * @param id ID du match
     * @param idtournoi ID du tournoi
     * @param idjoueur1 ID du premier joueur
     * @param idjoueur2 ID du second joueur
     * @param vainqueur Vainqueur du match
     * @param score Score du match
     * @param jeux Jeux du match
     * @param date Date du match
     */
    public Match(int id, int idtournoi, int idjoueur1, int idjoueur2, int vainqueur, String score, String jeux, String date) {
        this.id = id;
        this.idtournoi = idtournoi;
        this.idjoueur1 = idjoueur1;
        this.idjoueur2 = idjoueur2;
        this.vainqueur = vainqueur;
        this.score = score;
        this.jeux = jeux;
        this.date = date;

    }

    /**
     * Récupère l'ID
     * @return Retourne l'ID
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'ID
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Récupère l'ID du tournoi
     * @return Retourne l'ID du tournoi
     */
    public int getIdtournoi() {
        return idtournoi;
    }

    /**
     * Définit l'ID du tournoi
     * @param idtournoi ID du tournoi
     */
    public void setIdtournoi(int idtournoi) {
        this.idtournoi = idtournoi;
    }

    /**
     * Récupère l'ID du premier joueur
     * @return Retourne l'ID du premier joueur
     */
    public int getIdjoueur1() {
        return idjoueur1;
    }

    /**
     * Définit l'ID du premier joueur
     * @param idjoueur1 ID du premier joueur
     */
    public void setIdjoueur1(int idjoueur1) {
        this.idjoueur1 = idjoueur1;
    }

    /**
     * Récupère l'ID du second joueur
     * @return Retourne l'ID du second joueur
     */
    public int getIdjoueur2() {
        return idjoueur2;
    }

    /**
     * Définit l'ID du second joueur
     * @param idjoueur2 ID du second joueur
     */
    public void setIdjoueur2(int idjoueur2) {
        this.idjoueur2 = idjoueur2;
    }

    /**
     * Récupère l'ID du vainqueur
     * @return Retourne l'ID du vainqueur
     */
    public int getVainqueur() {
        return vainqueur;
    }

    /**
     * Définit l'ID du vainqueur
     * @param vainqueur ID du vainqueur
     */
    public void setVainqueur(int vainqueur) {
        this.vainqueur = vainqueur;
    }

    /**
     * Récupère le score
     * @return Retourne le score
     */
    public String getScore() {
        return score;
    }

    /**
     * Récupère le score
     * @param score Score
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * Récupère les jeux
     * @return Retourne les jeux
     */
    public String getJeux() {
        return jeux;
    }

    /**
     * Définit les jeux
     * @param jeux Jeux
     */
    public void setJeux(String jeux) {
        this.jeux = jeux;
    }

    /**
     * Récupère la date
     * @return Retourne la date
     */
    public String getDate() {
        return date;
    }

    /**
     * Définit la date
     * @param date Date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
