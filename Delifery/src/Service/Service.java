package Service;

import Donnees.*;
import Util.Calculs;
import Util.Coordonnees;
import Util.FileSystemXML;
import Vue.Carte;
import Vue.DetailsTour;
import Vue.VueApplication;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Service {

    private static volatile Service instance;
    private DonneesCarte donneesCarte;
    private CatalogueTours catalogueTours;

    private VueApplication vueApplication;

    private Carte carte;

    private String nomFichierCarte;


    public Service() {
        this.catalogueTours = new CatalogueTours();
    }

    /**
     * Renvoie une instance unique de Service en utilisant le modèle Singleton.
     * Si aucune instance n'existe, elle en crée une. Cette méthode garantit
     * qu'une seule instance de Service est utilisée dans l'application.
     *
     * @return L'instance unique de Service.
     */
    public static Service getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new Service();
                }
            }
        }
        return instance;
    }

    /**
     * Crée et initialise les données de carte à partir d'un fichier XML.
     * Charge les informations de carte à partir du fichier spécifié, crée une carte avec
     * des intersections, des sections, un entrepôt de départ et des informations d'échelle.
     *
     * @param nomFichier Le nom du fichier XML contenant les données de la carte.
     * @return Les données de la carte créées à partir du fichier XML.
     */
    public DonneesCarte creerDonneesCarte(String nomFichier) {
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();

        String nomFichierCarte;
        // Chemin d'accès fixe (à modifier selon vos besoins)
        String cheminFixe = System.getProperty("user.dir") + "/Delifery/fichiersXML2022/";

        String cheminComplet = cheminFixe + nomFichier;

        //System.out.println("Chemin Fixe (Methode creerDonneesCarte dans Service) : "+cheminComplet);

        Object[] objects = fsxml.lireXML(nomFichier);

        if (objects != null && objects[0] instanceof CatalogueTours) {
            this.catalogueTours = (CatalogueTours) objects[0];
            System.out.println("Print de la méthode creerDonnerCartes de la class Service qui print le catalgue Tour dans le cas où le fichier xml correspond à la restitution d'un catalogueTout : \n" + this.getCatalogueTours().toString());
            nomFichierCarte = this.catalogueTours.getMapName();
            cheminComplet = cheminFixe + nomFichierCarte;
            this.nomFichierCarte = nomFichierCarte;
            objects = fsxml.lireXML(nomFichierCarte);
        } else {
            this.nomFichierCarte = nomFichier;
        }

        if (objects == null) {
            objects = fsxml.lireXML(cheminComplet);
        }

        Intersection[] entrepot = (Intersection[]) objects[0];


        Intersection[] intersections = (Intersection[]) objects[1];
        Section[] sections = (Section[]) objects[2];
        float minLong = (float) objects[3];
        float maxLong = (float) objects[4];
        float minLat = (float) objects[5];
        float maxLat = (float) objects[6];
        float[] origin = {minLat, minLong};
        float longDiff = maxLong - minLong;
        float latDiff = maxLat - minLat;
        float echelleX = 1 / latDiff;
        float echelleY = 1 / longDiff;


        Intersection entrepotDepart = entrepot[0];
        Map<Intersection, Map<Intersection, Float>> carte = new HashMap<>();

        for (Section s : sections) {
            Intersection origine = s.getOrigine();
            Intersection destination = s.getDestination();
            Float taille = s.getTaille();

            // Vérifiez si la clé origine existe dans la carte
            if (!carte.containsKey(origine)) {
                carte.put(origine, new HashMap<>());
            }
            carte.get(origine).put(destination, taille);
        }

        DonneesCarte carteCourante = new DonneesCarte(nomFichier, entrepotDepart, carte, echelleX, echelleY, origin);
        this.donneesCarte = carteCourante;
        return carteCourante;
    }

    public Tour creerTour(ArrayList<Livraison> livraisons, ArrayList<Intersection> intersections, Livreur livreur) {

        Tour tour = new Tour(livraisons, intersections, livreur);

        return tour;
    }

    /**
     * Crée une livraison à partir des caractéristiques entrées par l'utilisateur
     * Tente d'ajouter une livraison à un tour existant pour un livreur donné
     *
     * @param adresse L'intersection à laquelle la livraison est associée.
     * @param creneau Le créneau de temps de la livraison.
     * @param livreur Le livreur choisi par l'utilisateur.
     * @return true si l'ajout de la livraison est réussi, false sinon.
     */
    public Boolean essaieAjoutLivraisonAuTour(Intersection adresse, Creneau creneau, Livreur livreur) {
        Livraison l = new Livraison((long) (Math.random() * 1000), adresse, creneau);
        // chercher le tour auquel correspond le livreur
        CatalogueTours catalogueTours = this.catalogueTours;
        Tour tour = catalogueTours.getTourByLivreur(livreur);
        if (tour == null) {
            ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
            livraisons.add(l);
            tour = new Tour(livraisons, livreur);
            catalogueTours.ajouterTour(tour);
        } else {
            tour.addLivraisonsTour((l));
        }
        DonneesCarte dc = this.donneesCarte;

        tour = calculerTour(tour, 15.0, dc, dc.getEntrepot());
        if (!tour.toutesLivraisonsAcceptees()) {
            return false;
        }
        // essayer de calculer le tour (modifier dans la fonction qui calcule le tour pour renvoyer l'état de l'opération)
        // si ok
        updateCarte();
        updatePanel();
        return true;
    }

    /**
     * Crée une instance de Livraison avec les détails spécifiés.
     *
     * @param id         L'identifiant de la livraison.
     * @param adresse    L'intersection associée à la livraison.
     * @param creneau    Le créneau de temps de la livraison.
     * @param heureDebut L'heure de début de la livraison.
     * @param heureFin   L'heure de fin de la livraison.
     * @return Une nouvelle instance de Livraison avec les détails donnés.
     */
    public Livraison creerLivraison(Long id, Intersection adresse, Creneau creneau, LocalTime heureDebut, LocalTime heureFin) {
        Livraison l = new Livraison(id, adresse, creneau, heureDebut, heureFin);
        return l;
    }

    /**
     * Crée une instance d'Intersection avec l'identifiant et les coordonnées spécifiés.
     *
     * @param id          L'identifiant de l'intersection.
     * @param coordonnees Les coordonnées de l'intersection.
     * @return Une nouvelle instance d'Intersection avec l'identifiant et les coordonnées donnés.
     */
    public Intersection creerIntersection(BigInteger id, Coordonnees coordonnees) {
        Intersection i = new Intersection(id, coordonnees);
        return i;
    }

    /**
     * Crée une liste d'intersections pour des simulations de livraison.
     * Utilisé à des fins de test.
     *
     * @return Une liste d'intersections préparées pour des simulations de livraison.
     */
    public ArrayList<Intersection> creerIntersectionsPourLivrer() {
        Intersection destination3 = new Intersection(new BigInteger("25321456"), new Coordonnees(45.749214, 4.875591));
        Intersection destination4 = new Intersection(new BigInteger("25321433"), new Coordonnees(45.74969, 4.873468));
        Intersection destination5 = new Intersection(new BigInteger("25321422"), new Coordonnees(45.749027, 4.873145));
        Intersection destination6 = new Intersection(new BigInteger("975886496"), new Coordonnees(45.756874, 4.8574047));
        ArrayList<Intersection> list = new ArrayList<Intersection>();
        list.add((destination5));
        list.add(destination3);
        list.add(destination4);
        list.add((destination6));
        return list;
    }

    /**
     * Crée une liste de livraisons pour des intersections spécifiées, avec un identifiant et un créneau donnés.
     * Utilisée à des fins de test.
     *
     * @param listeIntersections La liste des intersections pour les livraisons.
     * @param id                 L'identifiant à attribuer à chaque livraison.
     * @param c                  Le créneau de temps pour toutes les livraisons.
     * @return Une liste de livraisons associées aux intersections fournies, avec l'identifiant et le créneau donnés.
     */
    public ArrayList<Livraison> creerListeLivraisons(ArrayList<Intersection> listeIntersections, long id, Creneau c) {
        ArrayList<Livraison> listeLivraison = new ArrayList<Livraison>();
        for (Intersection i : listeIntersections) {
            Livraison l = new Livraison((long) id, i, c);
            listeLivraison.add(l);
        }
        return listeLivraison;
    }

    /**
     * Calcule un itinéraire de livraison pour un tour donné, en respectant les créneaux de livraison.
     *
     * @param tour     Le tour pour lequel l'itinéraire de livraison est calculé.
     * @param vitesse  La vitesse de déplacement pour le calcul de l'itinéraire.
     * @param carte    Les données de la carte utilisées pour le calcul de l'itinéraire.
     * @param entrepot L'intersection représentant l'entrepôt de départ du tour.
     * @return Le tour avec un itinéraire de livraison calculé respectant les créneaux.
     */
    public Tour calculerTour(Tour tour, Double vitesse, DonneesCarte carte, Intersection entrepot) {
        tour.reinitialiserTrajet();
        final int creneau8 = 8;
        final int creneau9 = 9;
        final int creneau10 = 10;
        final int creneau11 = 11;

        ArrayList<Livraison> livraisons8 = new ArrayList<>();
        ArrayList<Livraison> livraisons9 = new ArrayList<>();
        ArrayList<Livraison> livraisons10 = new ArrayList<>();
        ArrayList<Livraison> livraisons11 = new ArrayList<>();

        List<Intersection> chemin8 = new ArrayList<>();
        List<Intersection> chemin9 = new ArrayList<>();
        List<Intersection> chemin10 = new ArrayList<>();
        List<Intersection> chemin11 = new ArrayList<>();

        ArrayList<Livraison> livraisons = tour.getLivraisons();

        for (Livraison l : livraisons) {
            if (l.getCreneau().getValeur() == creneau8) {
                livraisons8.add(l);
            } else if (l.getCreneau().getValeur() == creneau9) {
                livraisons9.add(l);
            } else if (l.getCreneau().getValeur() == creneau10) {
                livraisons10.add(l);
            } else if (l.getCreneau().getValeur() == creneau11) {
                livraisons11.add(l);
            }
        }

        //On trie les listes de livraison
        Intersection interCourante = entrepot;
        tour.ajouterIntersection(entrepot);
        LocalTime tempsCourant = LocalTime.of(8, 0, 0);
        double distance;


        livraisons8 = Calculs.trierLivraisons(livraisons8, interCourante);
        for (Livraison l : livraisons8) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante, l.getAdresse(), carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant, distance, vitesse);
            if (l.getHeureArrivee().getHour() < (l.getCreneau().getValeur() + 1) && chemin != null) {
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant = l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons9 = Calculs.trierLivraisons(livraisons9, interCourante);
        for (Livraison l : livraisons9) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante, l.getAdresse(), carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant, distance, vitesse);
            if (l.getHeureArrivee().getHour() < (l.getCreneau().getValeur() + 1) && chemin != null) {
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant = l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons10 = Calculs.trierLivraisons(livraisons10, interCourante);

        for (Livraison l : livraisons10) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante, l.getAdresse(), carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant, distance, vitesse);
            if (l.getHeureArrivee().getHour() < (l.getCreneau().getValeur() + 1) && chemin != null) {
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant = l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        livraisons11 = Calculs.trierLivraisons(livraisons11, interCourante);
        for (Livraison l : livraisons11) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante, l.getAdresse(), carte.getCarte());
            distance = Calculs.getDistanceChemin(chemin);
            l.mettreAJourHeure(tempsCourant, distance, vitesse);
            if (l.getHeureArrivee().getHour() < (l.getCreneau().getValeur() + 1) && chemin != null) {
                tour.ajouterListeAuTrajet(chemin);
                interCourante = l.getAdresse();
                tempsCourant = l.getHeureDepart();
            } else {
                l.livraisonNonLivree();
            }
        }

        if (interCourante != entrepot) {
            List<Intersection> chemin = Calculs.dijkstra(interCourante, entrepot, carte.getCarte());
            tour.ajouterListeAuTrajet(chemin);
            interCourante = entrepot;
        }


        return tour;
    }

    /**
     * Ouvre les détails d'un tour spécifique en créant une fenêtre dédiée pour afficher les informations.
     *
     * @param chemin Le chemin spécifique lié aux détails à afficher.
     * @param id     L'identifiant du tour pour lequel les détails sont ouverts.
     */
    public void ouvrirDetails(String chemin, Long id) {
        Tour t = catalogueTours.getTourById(id);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth() * 0.8;
        double height = 1020.0 / 1080.0 * screenBounds.getHeight();
        DetailsTour detailsTour = new DetailsTour(chemin, t, (int) (width), (int) (height));
        Stage stage = new Stage();
        try {
            detailsTour.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param Ctour
     * @param cheminSauvegarde
     */
    public void sauvegarderCatalogueTourXML(CatalogueTours Ctour, String cheminSauvegarde) {
        System.out.println("Catalogue a sauvegardé : " + Ctour.toString());
        System.out.println("Chemin où enregistrer :" + cheminSauvegarde);
        FileSystemXML.EcrireCatalogueXML(Ctour, cheminSauvegarde);

    }

    /**
     * Initialise un livreur avec un nom spécifique.
     *
     * @param name Le nom du livreur à initialiser.
     * @return Une instance de livreur initialisée avec le nom spécifié.
     */
    public Livreur initialisationLivreur(String name) {
        Livreur l = new Livreur(name);
        return l;
    }

    /**
     * Récupère toutes les intersections de la carte sous forme de liste.
     *
     * @return Une liste contenant toutes les intersections extraites de la carte.
     */
    public ArrayList<Intersection> getAllIntersections() {
        // On crée une liste vide pour stocker les intersections
        ArrayList<Intersection> intersections = new ArrayList<>();
        // On parcourt l'ensemble des clés de la carte
        for (Intersection key : donneesCarte.getCarte().keySet()) {
            // On ajoute chaque clé à la liste
            intersections.add(key);
        }
        // On renvoie la liste
        return intersections;
    }

    /**
     * Récupère une liste de noms des livreurs associés aux tours dans le catalogue.
     *
     * @return Une liste contenant les noms des livreurs présents dans le catalogue de tours.
     */
    public ArrayList<String> getListLivreur() {
        ArrayList<String> livreurs = new ArrayList<>();
        for (Tour t : catalogueTours.catalogue) {
            livreurs.add(t.getLivreur().getNom());
        }
        return livreurs;
    }

    /**
     * Récupère une liste de tours du le catalogue courant.
     *
     * @return Une liste contenant les tours présents dans le catalogue de tours.
     */
    public ArrayList<Tour> getTours() {
        return catalogueTours.getCatalogue();
    }


    /**
     * Met à jour la carte dans l'interface graphique de l'application.
     * Appelle la méthode d'actualisation de la carte dans l'interface graphique.
     */
    public void updateCarte() {
        vueApplication.updateCarte();
    }

    /**
     * Met à jour le panneau dans l'interface graphique de l'application.
     * Appelle la méthode d'actualisation du panneau dans l'interface graphique.
     */
    public void updatePanel() {
        vueApplication.updatePanel();
    }

    /**
     * Modifie le nombre de livreurs disponibles dans le catalogue de tours.
     * Ajoute de nouveaux livreurs ou en supprime en fonction du nombre spécifié.
     *
     * @param nombre Le nombre de livreurs désiré dans le catalogue de tours.
     */
    public void setNbLivreur(int nombre) {

        if (nombre > catalogueTours.getListeLivreurs().size()) {
            for (int i = catalogueTours.getListeLivreurs().size(); i < nombre; i++) {
                catalogueTours.ajouterTour(new Tour(new Livreur(i)));
            }
        } else {
            for (int i = catalogueTours.getListeLivreurs().size(); i > nombre; i--) {
                catalogueTours.supprimerTour();
            }

        }
    }

    /**
     * Restitue un catalogue de tours à partir d'un fichier XML spécifié.
     *
     * @param cheminfihcierCatalogueTour Le chemin du fichier XML du catalogue de tours à restituer.
     * @return true si la restitution du catalogue est réussie, false sinon.
     */
    public boolean restituerTour(String cheminfihcierCatalogueTour) {
        boolean b = false;
        FileSystemXML fsxml;
        fsxml = new FileSystemXML();
        Object[] objects = fsxml.lireXML(cheminfihcierCatalogueTour);
        if (objects[0] != null ){
             if(objects[0] instanceof CatalogueTours) {
                 CatalogueTours Ctour = (CatalogueTours) objects[0];
                 if (this.nomFichierCarte.contains(Ctour.getMapName())) { //On vérifie la cohérence des cartes
                     setCatalogueTours((CatalogueTours) objects[0]);
                     b = true;
                     updateCarte();
                     updatePanel();
                 } else {
                     System.out.println("Impossible d'importer le Catalogue car le CatalogueTour ne correspond pas avec la carte affichée");
                 }
             }
        }
        return b;
    }


    /**
     * Renvoie les données de la carte.
     *
     * @return Les données de la carte.
     */
    public DonneesCarte getDonneesCarte() {
        return donneesCarte;
    }

    /**
     * Renvoie le catalogue de tours.
     *
     * @return Le catalogue de tours.
     */
    public CatalogueTours getCatalogueTours() {
        return catalogueTours;
    }

    /**
     * Définit le catalogue de tours avec celui spécifié.
     *
     * @param catalogueTours Le catalogue de tours à définir.
     */
    public void setCatalogueTours(CatalogueTours catalogueTours) {
        this.catalogueTours = catalogueTours;
    }

    /**
     * Définit l'instance de l'interface graphique de l'application.
     *
     * @param vueApplication L'instance de l'interface graphique de l'application à définir.
     */
    public void setVueApplication(VueApplication vueApplication) {
        this.vueApplication = vueApplication;
    }

    /**
     * Renvoie l'objet Carte.
     *
     * @return L'objet Carte.
     */
    public Carte getCarte() {
        return this.carte;
    }

    /**
     * Définit l'objet Carte avec celui spécifié.
     *
     * @param carte L'objet Carte à définir.
     */
    public void setCarte(Carte carte) {
        this.carte = carte;
    }

}
