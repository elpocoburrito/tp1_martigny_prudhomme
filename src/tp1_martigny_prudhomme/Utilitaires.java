package tp1_martigny_prudhomme;

import java.util.Arrays;

public class Utilitaires {

    public void supprimer(int[] tabID, int[][] tab, int ligne) {
        tabID[ligne] = 0;

        //Supprime une ligne de notes
        for (int i = 0; i < tab[ligne].length; i++) {
            tab[ligne][i] = 0;                  //Delete Notes
        }

        //Réorganise le tableau une ligne plus haut
        for (int i = ligne; i < tab.length - 1; i++) {
            permuterIntID(tabID, i, 1 + i);  //Sort DA
            permuterInt(tab, i, 1 + i);       //Sort Notes
        }
        FXMLDocumentController.nbEleves--;
    }

    public void ajouter(int[] tabID, int[][] tab, double[] notes) {
        int ligne = FXMLDocumentController.nbEleves;
        Arrays.toString(notes);
        tabID[ligne] = (int) notes[0];
        for (int i = 1; i < notes.length; i++) {
            tab[ligne][i - 1] = (int) notes[i];
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

    
    public void triAscNotes(int[] index, int[][] tab, int colone, int nbEleves){     
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
    
    public void triDscNotes(int[] index, int[][] tab, int colone, int nbEleves){     
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
    
    public void triAscDA(int[] index, int[] tab, int nbEleves){     
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }
        
        for (int i = 0; i < nbEleves - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < nbEleves; j++) {
                if (tab[index[iMin]] > tab[index[j]]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }
    
    public void triDscDA(int[] index, int[] tab, int nbEleves){     
        int iMin;
        //initialiser tableau tabDA     
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }
        
        for (int i = 0; i < nbEleves - 1; i++) {
            iMin = i;
            for (int j = i + 1; j < nbEleves; j++) {
                if (tab[index[iMin]] < tab[index[j]]) {
                    iMin = j;
                }
            }
            permuterIntID(index, iMin, i);
        }
    }
    public boolean examValide(double tab[]){
        
    }
    
    public double calculerTotal(double note1, double note2, double note3, double note4) {
        return (note1 * 25 / 100) + (note2 * 30 / 100) + (note3 * 20 / 100) + (note4 * 25 / 100);
    }

}
