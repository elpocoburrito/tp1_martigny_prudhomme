package tp1_martigny_prudhomme;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Utilitaires {

    public void supprimer(int[][] tab, int ligne) {
        //Supprime une ligne de notes
        for (int i = 0; i < tab[ligne].length; i++) {
            tab[ligne][i] = 0;                  //Delete Notes
        }

        //Réorganise le tableau une ligne plus haut
        for (int i = ligne; i < tab.length - 1; i++) {
            permuterInt(tab, i, 1 + i);       //Sort Notes
        }
        FXMLDocumentController.nbEleves--;
    }

    public void ajouter(int[][] tab, double[] notes) {
        int ligne = FXMLDocumentController.nbEleves;
        Arrays.toString(notes);
        for (int i = 0; i < notes.length; i++) {
            tab[ligne][i] = (int) notes[i];
        }
        FXMLDocumentController.nbEleves++;
    }

    public void modifier(int[][] tab, double[] note, int ind) {
        for (int i = 0; i < 5; i++) {
            tab[ind][i] = (int) note[i];
        }
    }

    private void permuterInt(int[][] tab, int ind1, int ind2) {
        int[] transit = tab[ind1]; //du type des indices à trier
        tab[ind1] = tab[ind2];
        tab[ind2] = transit;
    }

    private void permuterIntID(int[] tab, int ind1, int ind2) {
        int transit = tab[ind1]; //du type des indices à trier
        tab[ind1] = tab[ind2];
        tab[ind2] = transit;
    }

    public void triAscNotes(int[] index, int[][] tab, int colone, int nbEleves) {
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }

        for (int i = 0; i < nbEleves - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < nbEleves; j++) {
                if (tab[index[iMin]][colone] > tab[index[j]][colone]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }

    public void triDscNotes(int[] index, int[][] tab, int colone, int nbEleves) {
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }
        for (int i = 0; i < tab.length - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < tab.length; j++) {
                if (tab[index[iMin]][colone] < tab[index[j]][colone]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }

    public void triAscDA(int[] index, int[][] tab, int nbEleves) {
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }

        for (int i = 0; i < nbEleves - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < nbEleves; j++) {
                if (tab[index[iMin]][0] > tab[index[j]][0]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }

    public void triDscDA(int[] index, int[][] tab, int nbEleves) {
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }

        for (int i = 0; i < nbEleves - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < nbEleves; j++) {
                if (tab[index[iMin]][0] < tab[index[j]][0]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }

    public String moyenneEval(int[][] tab, int col) {
        double temp = 0;
        double total = 0;
        
        DecimalFormat format = new DecimalFormat("##.00");

        for (int i = 0; i < FXMLDocumentController.nbEleves; i++) {
            total += tab[i][col];
            temp++;
        }

        return format.format(total/temp);
    }

    public int minEval(int[][] tab, int col) {
        int min = 101;
        for (int i = 0; i < FXMLDocumentController.nbEleves; i++) {
            if (tab[i][col] < min) {
                min = tab[i][col];
            }
        }
        return min;
    }

    public int maxEval(int[][] tab, int col) {
        int max = 0;

        for (int i = 0; i < FXMLDocumentController.nbEleves; i++) {
            if (tab[i][col] > max) {
                max = tab[i][col];
            }
        }
        return max;
    }
}
