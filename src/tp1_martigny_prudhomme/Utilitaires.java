package tp1_martigny_prudhomme;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Utilitaires {

    /**
     * 
     * @param tab Tableau de valeurs à modifier
     * @param ligne ligne à supprimer
     * @param nbEleves nombre d'élèves restant
     * @return nombre d'élève restant
     */
    public int supprimer(int[][] tab, int ligne, int nbEleves) {
        //Supprime une ligne de notes
        for (int i = 0; i < tab[ligne].length; i++) {
            tab[ligne][i] = 0;                  //Delete Notes
        }

        //Réorganise le tableau une ligne plus haut
        for (int i = ligne; i < tab.length - 1; i++) {
            permuterInt(tab, i, 1 + i);       //Sort Notes
        }
        nbEleves--;
        return nbEleves;
    }

    /**
     * 
     * @param tab tableau de valeurs à modifier
     * @param notes array des notes à ajouter
     * @param nbEleves nombre d'élève restant
     * @return nombre d'élève restant
     */
    public int ajouter(int[][] tab, double[] notes, int nbEleves) {
        int ligne = FXMLDocumentController.nbEleves;
        Arrays.toString(notes);
        for (int i = 0; i < notes.length; i++) {
            tab[ligne][i] = (int) notes[i];
        }
        nbEleves++;
        return nbEleves;
    }

    /**
     * 
     * @param tab Tableau de valeurs à modifier
     * @param note tableau de notes à modifier
     * @param ind ligne à modifier
     */
    public void modifier(int[][] tab, double[] note, int ind) {
        for (int i = 1; i < tab[ind].length; i++) {
            tab[ind][i] = (int) note[i-1];
        }
    }

    private void permuterInt(int[][] tab, int ind1, int ind2) {
        int[] transit = tab[ind1]; //du type des indices à trier
        tab[ind1] = tab[ind2];
        tab[ind2] = transit;
    }
    
    /**
     * permute 2 valeurs
     * @param tab
     * @param ind1
     * @param ind2 
     */
    private void permuterIntID(int[] tab, int ind1, int ind2) {
        int transit = tab[ind1]; //du type des indices à trier
        tab[ind1] = tab[ind2];
        tab[ind2] = transit;
    }

    /**
     * Tri les valeurs de façon ascendante selon Notes
     * @param index tableau trié par indirection
     * @param tab  tableau de valeurs à trier
     * @param colone colonne selon laquelle on trie
     * @param nbEleves nombre d'élève restant de la classe
     */
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

        /**
     * Tri les valeurs de façon descendante selon Notes
     * @param index tableau trié par indirection
     * @param tab  tableau de valeurs à trier
     * @param colone colonne selon laquelle on trie
     * @param nbEleves nombre d'élève restant de la classe
     */
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

        /**
     * Tri les valeurs de façon ascendante selon DA
     * @param index tableau trié par indirection
     * @param tab  tableau de valeurs à trier
     * @param colone colonne selon laquelle on trie
     * @param nbEleves nombre d'élève restant de la classe
     */
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

        /**
     * Tri les valeurs de façon descendante selon DA
     * @param index tableau trié par indirection
     * @param tab  tableau de valeurs à trier
     * @param colone colonne selon laquelle on trie
     * @param nbEleves nombre d'élève restant de la classe
     */
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

    /**
     * Calcule la moyenne des évaluations
     * @param tab tableau de notes
     * @param col colone à calculer
     * @return la moyenne
     */
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
    
    /**
     * L'évaluation la plus basse
     * @param tab tableau de valeurs
     * @param col colone à évaluer
     * @return l'évaluation la plus basse
     */
    public int minEval(int[][] tab, int col) {
        int min = 101;
        for (int i = 0; i < FXMLDocumentController.nbEleves; i++) {
            if (tab[i][col] < min) {
                min = tab[i][col];
            }
        }
        return min;
    }

    /**
     *  retourne la plus grande note
     * @param tab tableau de valeurs
     * @param col la colone à évaluer
     * @return la plus grande note
     */
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
