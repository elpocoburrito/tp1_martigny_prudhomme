package tp1_martigny_prudhomme;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * 
 * @author Tommy Prud'homme
 * et Alexandre Martigny
 */
public class FXMLDocumentController implements Initializable {

    private static final int NB_ELEVES = 25;
    private static final int NB_EVALS = 4;

    //Positions statiques des éléments dans le GridPane
    private static final int DA = 0;
    private static final int EXA1 = 1;
    private static final int EXA2 = 2;
    private static final int TP1 = 3;
    private static final int TP2 = 4;
    private static final int TOTAL = 5;

    public static int nbEleves = 0; //Compteur du nombre d'élèves
    private static int[][] tabNotes = new int[NB_ELEVES][NB_EVALS + 2]; //Tableay de notes et de DA
    private static int[] index = new int[NB_ELEVES];

    enum Titre {
        DA, Examen1, Examen2, TP1, TP2, Total
    };

    public String modeToggle = "default";
    private Utilitaires util = new Utilitaires();

    @FXML
    private Label lblNbEleves;
    @FXML
    private Label lblMoyEx1;
    @FXML
    private Label lblMoyEx2;
    @FXML
    private Label lblMoyTP1;
    @FXML
    private Label lblMoyTP2;
    @FXML
    private Label lblMaxEx1;
    @FXML
    private Label lblMaxEx2;
    @FXML
    private Label lblMaxTP1;
    @FXML
    private Label lblMaxTP2;
    @FXML
    private Label lblMinEx1;
    @FXML
    private Label lblMinEx2;
    @FXML
    private Label lblMinTP1;
    @FXML
    private Label lblMinTP2;
    @FXML
    private GridPane gridNotes;
    @FXML
    private ComboBox<String> cmbTris;
    @FXML
    private TextField txfDA;
    @FXML
    private TextField txfExam1;
    @FXML
    private TextField txfExam2;
    @FXML
    private TextField txfTP1;
    @FXML
    private TextField txfTP2;
    @FXML
    private ListView lsvDA;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnAnnuler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTris.getItems().addAll("Tri Ascendant DA", "Tri Descendant DA", "Tri Ascandant Note Finale", "Tri Descendant Note Finale");
        cmbTris.getSelectionModel().select(0);
        lsvDA.getSelectionModel().selectedItemProperty().addListener(e -> {
            actualiserTxf();
        });
        try {
            createClass();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        trierGrid();
        garnirLsv();
        creerGrille();
        garnirGrille();
        remplirStatistiques();
        try {
            sauverClass();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //***   Événements du GUI  ***//
    
    /**
     * Prépare les éléments graphiques pour ajouter des données
     * @param event 
     */
    @FXML
    private void btnAjouterClick(ActionEvent event) {
        modeToggle = "ajouter";
        enableDataCtrls(true);
    }

    /**
     * Supprime l'élément selectionné.
     * @param event 
     */
    @FXML
    private void btnSupprimerClick(ActionEvent event) {
        int indexASupp = 0;
        String DA = lsvDA.getSelectionModel().getSelectedItem().toString();
        lsvDA.getItems().remove(lsvDA.getSelectionModel().getSelectedIndex());

        //Trouve l'index a supprimer dans le GridPane. L'index est sauvegardé quand la boucle tombe sur le DA selectionné dans le GridPane.
        for (int i = 0; i < nbEleves - 1; i++) {
            int var = tabNotes[i][0];
            if (var == Integer.parseInt(DA)) {
                indexASupp = i;
            }
        }
        nbEleves = util.supprimer(tabNotes, indexASupp, nbEleves);
        actualiserGrid();
    }
    
    /**
     * Remplis et déverouille les éléments graphique nécéssaires à la modification
     * @param event 
     */
    @FXML
    private void btnModifierClick(ActionEvent event) {
        int indexASupp = 0;
        modeToggle = "modifier";
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur!");
        alert.setContentText("Veuillez effectuer une sélection dans la liste de DA si-haut.");

        if (lsvDA.getSelectionModel().isEmpty() == true) {
            alert.showAndWait();
        } else {
            enableDataCtrls(true);
            txfDA.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][DA]));
            txfDA.setDisable(true);
            txfExam1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][EXA1]));
            txfExam2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][EXA2]));
            txfTP1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][TP1]));
            txfTP2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][TP2]));
        }
    }
    
    /**
     * Annule la modification/l'ajout
     * @param event 
     */
    @FXML
    private void btnAnnulerClick(ActionEvent event) {
        modeToggle = "default";
        enableDataCtrls(false);
    }

    /**
     * Confirme la modification ou l'ajout lors du clic du bouton OK
     * @param event 
     */
    @FXML
    private void btnOkClick(ActionEvent event) {
        switch (modeToggle) {
            case "ajouter":
                double[] notes = new double[NB_EVALS + 2];
                notes[DA] = Integer.parseInt(txfDA.getText());
                notes[EXA1] = Integer.parseInt(txfExam1.getText());
                notes[EXA2] = Integer.parseInt(txfExam2.getText());
                notes[TP1] = Integer.parseInt(txfTP1.getText());
                notes[TP2] = Integer.parseInt(txfTP2.getText());
                notes[TOTAL] = calculerTotal(notes);
                boolean notesValides = true;
                for (int i = 1; i < notes.length; i++) {
                    if (!(notes[i] <= 100 && notes[i] >= 0)) {
                        notesValides = false;
                    }
                }
                if (notesValides) {
                    nbEleves = util.ajouter(tabNotes, notes, nbEleves);
                    actualiserGrid();
                    enableDataCtrls(false);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur!");
                    alert.setContentText("Entrez des notes entre 0 et 100!");
                    alert.showAndWait();
                }
                break;

            case "modifier":
                double[] note = new double[NB_EVALS + 1];
                note[0] = Integer.parseInt(txfExam1.getText());
                note[1] = Integer.parseInt(txfExam2.getText());
                note[2] = Integer.parseInt(txfTP1.getText());
                note[3] = Integer.parseInt(txfTP2.getText());
                note[4] = calculerTotal(note);
                util.modifier(tabNotes, note, lsvDA.getSelectionModel().getSelectedIndex());
                actualiserGrid();
                enableDataCtrls(false);
                break;

            default:

                break;
        }
    }
    
    /**
     * Confirme la sauvegarde puis quitte
     * @param event
     * @throws IOException 
     */
    @FXML
    private void btnQuitter(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Voulez");
        alert.setContentText("Voulez vous enregistrer?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.YES){
                sauverClass();
                System.exit(0);
            }
            else if(result.get() == ButtonType.NO){
                System.exit(0);
            }
    }
    
    /**
     * changement du tri dans le combobox
     * @param event 
     */
    @FXML
    private void cmbTriChanged(ActionEvent event) {
        trierGrid();
        actualiserGrid();
    }

    /**
     * Met à jour les TextFields lors du clic sur un élément du listview
     * @param event 
     */
    @FXML
    private void lsvDAClick(MouseEvent event) {

        if (lsvDA.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setContentText("Veuillez effectuer une sélection dans la liste de DA si-haut.");

            alert.showAndWait();
        }
    }

    //***   Gestion du DnD sur la poubelle ***//
    @FXML
    private void onDragDetectedLsv(MouseEvent event) {
        Dragboard db = lsvDA.startDragAndDrop(TransferMode.ANY);
        ClipboardContent cbContenu = new ClipboardContent();
        cbContenu.putString(String.valueOf(lsvDA.getSelectionModel().getSelectedIndex()));
        db.setContent(cbContenu);
        System.out.println(lsvDA.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void onDragOverImg(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void onDragDroppedImg(DragEvent event) {
        Dragboard db;
        db = event.getDragboard();
        String contenu = db.getString();
        System.out.println(contenu);
        nbEleves = util.supprimer(tabNotes, Integer.parseInt(contenu), nbEleves);
        lsvDA.getItems().remove(Integer.parseInt(contenu));
        trierGrid();
        actualiserGrid();
    }

    //***   Gestion du GridPane et génération des tabDA et tabNotes ***//
    
    //Supprime puis recrée les colones et lignes du GridPane
    public void creerGrille() {
        gridNotes.getRowConstraints().clear();
        gridNotes.getColumnConstraints().clear();
        gridNotes.getRowConstraints().add(new RowConstraints(30));
        GridPane.setHalignment(gridNotes, HPos.CENTER);
        for (int i = 1; i < nbEleves; i++) {
            RowConstraints eleve = new RowConstraints();
            eleve.setPrefHeight(20);
            gridNotes.getRowConstraints().add(eleve);
        }
        for (int i = 0; i < NB_EVALS + 2; i++) {
            ColumnConstraints eleve = new ColumnConstraints();
            eleve.setPrefWidth(100);
            gridNotes.getColumnConstraints().add(eleve);
        }
    }

    //Vide le texte de tout les éléments Text du GridPane
    public void viderGrille() {
        for (Node noeud : gridNotes.getChildren()) {
            if (noeud instanceof Text) {
                ((Text) noeud).setText("");
            }
        }
    }

    //Ajoute les Titres de colones, les DA et les notes dans le GridPane
    public void garnirGrille() {
        //Ajoute les Titres de colonnes
        int iCol = 0;
        for (Titre titre : Titre.values()) {
            Text text = new Text(String.valueOf(titre));
            gridNotes.add(text, iCol, 0);
            GridPane.setHalignment(text, HPos.CENTER);
            iCol++;
        }

        //Ajoute les DA
        int iLig = 1;

        //Ajoute les notes
        for (int i = 0; i < nbEleves; i++) {
            for (int j = 0; j < tabNotes[i].length; j++) {
                Text texte = new Text(String.valueOf(tabNotes[index[i]][j]));
                if (j == 0) {
                    texte.setFill(Paint.valueOf("#fff"));
                }
                if ((tabNotes[index[i]][TOTAL] < 60 && j == TOTAL && i >= 0) || (tabNotes[index[i]][TOTAL] < 60 && j == DA && i >= 0)) {
                    texte.setFill(Paint.valueOf("#d60000"));
                } else if (j >= 1) {
                    texte.setFill(Paint.valueOf("#efd67a"));
                }
                gridNotes.add(texte, j, i + 1);
                GridPane.setHalignment(texte, HPos.CENTER);
            }
        }
    }

    //Rempli le listview avec les DA d'élèves
    public void garnirLsv() {
        for (int i = 0; i < nbEleves; i++) {
            lsvDA.getItems().add(tabNotes[i][DA]);
        }
    }

    //Lance le tri selectionné dans le ComboBox
    public void trierGrid() {
        int selected = cmbTris.getSelectionModel().getSelectedIndex();
        switch (selected) {
            case 0:
                util.triAscDA(index, tabNotes, nbEleves);
                break;
            case 1:
                util.triDscDA(index, tabNotes, nbEleves);
                break;
            case 2:
                util.triAscNotes(index, tabNotes, TOTAL - 1, nbEleves);
                break;
            case 3:
                util.triDscNotes(index, tabNotes, TOTAL - 1, nbEleves);
                break;
            default:
                break;
        }
    }

    //Boucle sur notes.txt (racine du projet) et lis les entrées, 
    //garnis l'Array de DA tabDA et les notes dans tabNotes. 
    //il met aussi à jour la variable nbEleves
    public void createClass() throws IOException, FileNotFoundException {
        BufferedReader objEntree = new BufferedReader(new FileReader("notes.txt"));
        String ligneLue = objEntree.readLine();
        int i = 0;
        // boucle tant qu'il y a des entrées à lire
        while (ligneLue != null) {
            int j = 0;
            StringTokenizer tok = new StringTokenizer(ligneLue, " ");
            while (tok.countTokens() != 0) {
                int valeur = Integer.parseInt(tok.nextToken());
                tabNotes[i][j] = valeur;
                j++;
            }
            ligneLue = objEntree.readLine();
            i++;
        }
        nbEleves = i;
        objEntree.close();
    }

    /**
     * Sauve les changements dans notes.txt
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void sauverClass() throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter("notes.txt");
        PrintWriter fp = new PrintWriter(fw);
        for (int i = 0; i < tabNotes.length; i++) {
            for (int j = 0; j < tabNotes[i].length; j++) {
                fp.print(tabNotes[i][j] + " ");
            }
            fp.println();
        }
        fp.close();
    }

    //Actualise l'affichage du GridPane
    public void actualiserGrid() {
        viderGrille();
        creerGrille();
        garnirGrille();
        remplirStatistiques();
    }

    //Actualise l'affichage dans les TextFields
    public void actualiserTxf() {
        txfDA.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][DA]));
        txfExam1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][EXA1]));
        txfExam2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][EXA2]));
        txfTP1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][TP1]));
        txfTP2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][TP2]));
    }

    //Active ou désactive les TextFields et Buttons pour l'ajout ou la modification d'entrées
    public void enableDataCtrls(boolean toggle) {
        Object[] objects = new Object[]{txfDA, txfExam1, txfExam2, txfTP1, txfTP2, btnAnnuler, btnOK};
        for (Object obj : objects) {
            if (obj instanceof TextField) {
                ((TextField) obj).setDisable(!toggle);
                ((TextField) obj).setText("");
            } else if (obj instanceof Button) {
                ((Button) obj).setDisable(!toggle);
            }
        }
    }

    //Rempli les statistiques en bas du gridpane
    public void remplirStatistiques() {
        lblNbEleves.setText("Nombre d'élèves: " + String.valueOf(nbEleves));
        lblMoyEx1.setText(util.moyenneEval(tabNotes, EXA1));
        lblMoyEx2.setText(util.moyenneEval(tabNotes, EXA2));
        lblMoyTP1.setText(util.moyenneEval(tabNotes, TP1));
        lblMoyTP2.setText(util.moyenneEval(tabNotes, TP2));
        lblMaxEx1.setText(String.valueOf(util.maxEval(tabNotes, EXA1)));
        lblMaxEx2.setText(String.valueOf(util.maxEval(tabNotes, EXA2)));
        lblMaxTP1.setText(String.valueOf(util.maxEval(tabNotes, TP1)));
        lblMaxTP2.setText(String.valueOf(util.maxEval(tabNotes, TP2)));
        lblMinEx1.setText(String.valueOf(util.minEval(tabNotes, EXA1)));
        lblMinEx2.setText(String.valueOf(util.minEval(tabNotes, EXA2)));
        lblMinTP1.setText(String.valueOf(util.minEval(tabNotes, TP1)));
        lblMinTP2.setText(String.valueOf(util.minEval(tabNotes, TP2)));
    }
    
    //Calcule le total des notes des élèves
    public double calculerTotal(double[] note) {
        return (note[EXA1] * 25 / 100) + (note[EXA2] * 30 / 100) + (note[TP1] * 20 / 100) + (note[TP2] * 25 / 100);
    }
}
