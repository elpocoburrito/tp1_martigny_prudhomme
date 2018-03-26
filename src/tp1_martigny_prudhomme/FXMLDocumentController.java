package tp1_martigny_prudhomme;

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
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import tp1_martigny_prudhomme.Utilitaires.*;

public class FXMLDocumentController implements Initializable {

    @FXML
    private GridPane gridNotes;
    @FXML
    private ComboBox<String> cmbTris;

    private static final int NB_ELEVES = 25;
    private static final int NB_EVALS = 4;

    private static final int DA = 0;
    private static final int EXA1 = 1;
    private static final int EXA2 = 2;
    private static final int TP1 = 3;
    private static final int TP2 = 4;
    private static final int TOTAL = 5;

    public static int nbEleves = 0; //Compteur du nombre d'élèves
    private static int[][] tabNotes = new int[NB_ELEVES][NB_EVALS + 1];
    private static int[] index = new int[NB_ELEVES];

    public String modeToggle = "default";
    private Utilitaires util = new Utilitaires();

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

    enum Titre {
        DA, Examen1, Examen2, TP1, TP2, Total
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTris.getItems().addAll("Tri Ascendant DA", "Tri Descendant DA", "Tri Ascandant Note Finale", "Tri Descendant Note Finale");
        try {
            createClass();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        garnirLsv();
        creerGrille();
        garnirGrille();
    }

    @FXML
    private void btnAjouterClick(ActionEvent event) {
        modeToggle = "ajouter";
        enableDataCtrls(true);
    }

    @FXML
    private void btnSupprimerClick(ActionEvent event) {
        modeToggle = "supprimer";
        int indexASupp = 0;
        String DA = lsvDA.getSelectionModel().getSelectedItem().toString();
        lsvDA.getItems().remove(lsvDA.getSelectionModel().getSelectedIndex());

        //Trouve l'index a supprimer dans le GridPane. L'index est sauvegardé quand la boucle tombe sur le DA selectionné dans le GridPane.
        for (int i = 0; i < nbEleves - 1; i++) {
            int var = index[i];
            if (var == Integer.parseInt(DA)) {
                indexASupp = i;
            }
        }

        util.supprimer(index, tabNotes, indexASupp);
        viderGrille();
        creerGrille();
        garnirGrille();
    }

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
            txfDA.setText(lsvDA.getSelectionModel().getSelectedItem().toString());
            txfDA.setDisable(true);

            txfExam1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][0]));
            txfExam2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][1]));
            txfTP1.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][2]));
            txfTP2.setText(String.valueOf(tabNotes[lsvDA.getSelectionModel().getSelectedIndex()][3]));
        }
    }

    @FXML
    private void btnAnnulerClick(ActionEvent event) {
        modeToggle = "default";
        enableDataCtrls(false);
    }

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
                notes[TOTAL] = util.calculerTotal(notes[EXA1],notes[EXA2],notes[TP1],notes[TP2]);
                util.ajouter(index, tabNotes, notes);
                viderGrille();
                creerGrille();
                garnirGrille();
                enableDataCtrls(false);
                break;

            case "supprimer":

                break;

            case "modifier":
                double[] note = new double[NB_EVALS+1];
                note[0] = Integer.parseInt(txfExam1.getText());
                note[1] = Integer.parseInt(txfExam2.getText());
                note[2] = Integer.parseInt(txfTP1.getText());
                note[3] = Integer.parseInt(txfTP2.getText());
                note[4] = util.calculerTotal(note[0],note[1],note[2],note[3]);
                util.modifier(tabNotes, note, lsvDA.getSelectionModel().getSelectedIndex());
                viderGrille();
                creerGrille();
                garnirGrille();
                enableDataCtrls(false);
                break;

            default:

                break;
        }
    }

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

    public void creerGrille() {
        gridNotes.getRowConstraints().clear();
        gridNotes.getColumnConstraints().clear();
        gridNotes.getRowConstraints().add(new RowConstraints(30));
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

    public void viderGrille() {
        for (Node noeud : gridNotes.getChildren()) {
            if (noeud instanceof Text) {
                ((Text) noeud).setText("");
            }
        }
    }

    public void garnirGrille() {
        int iCol = 0;
        for (Titre titre : Titre.values()) {
            gridNotes.add(new Text(titre + ""), iCol, 0);
            iCol++;
        }
        int iLig = 1;
        for (int i = 0; i < nbEleves; i++) {
            gridNotes.add(new Text(index[i] + ""), 0, iLig);
            iLig++;
        }
        for (int i = 0; i < nbEleves; i++) {
            for (int j = 0; j < tabNotes[i].length; j++) {
                gridNotes.add(new Text(tabNotes[i][j] + ""), j + 1, i + 1);
            }
        }
    }

    public void garnirLsv() {
        for (int i = 0; i < nbEleves; i++) {
            lsvDA.getItems().add(index[i]);
        }
    }

    public void createClass() throws FileNotFoundException, IOException {
        BufferedReader objEntree = new BufferedReader(new FileReader("notes.txt"));
        String ligneLue = objEntree.readLine();
        int i = 0;
        while (ligneLue != null) {
            int j = 0;
            StringTokenizer tok = new StringTokenizer(ligneLue, " ");
            index[i] = Integer.parseInt(tok.nextToken());
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
}
