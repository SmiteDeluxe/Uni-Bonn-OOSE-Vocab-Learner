package application;

import javafx.scene.control.CheckBox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * gets the Vocabulary from the server and creates VocabList objects
 * @author ***
 */
public class VocabList {
    int number;
    String language1;
    String language2;
    String userTranslation;
    int errors;
    int phase;
    private boolean select;

    /**
     * Creates a new VocabList object
     *
     * @param number Vocab number
     * @param language1 first language
     * @param language2 second language
     * @param phase phase of the vocabulary
     * @param select state of checkbox
     */
    public VocabList() {
    }

    public VocabList (int number, String language1, String language2, int phase, boolean select){
        this.number = number;
        this.language1 = language1;
        this.language2 = language2;
        this.phase = phase;
        this.select = select;
    }

    public VocabList (int number, String language1, String language2, int phase){
        this.number = number;
        this.language1 = language1;
        this.language2 = language2;
        this.phase = phase;
    }

    /**
     * Only for ControllerLearning
     * used in showAllVocables()
     * @param number
     * @param language1
     * @param language2
     * @param userTranslation
     * @param phase
     */
    public VocabList (int number, String language1, String language2, String userTranslation, int errors, int phase){
        this.number = number;
        this.language1 = language1;
        this.language2 = language2;
        this.userTranslation = userTranslation;
        this.errors = errors;
        this.phase = phase;
    }

    //Get data
    public int getNumber() {
        return number;
    }

    public int getPhase() {
        return phase;
    }

    public String getLanguage1() {
        return language1;
    }

    public String getLanguage2() {
        return language2;
    }

    public String getUserTranslation(){
        return userTranslation;
    }

    public int getErrors() {
        return errors;
    }

    public boolean getSelect(){ return select;}

    //Set data
    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setSelect (boolean select){
        this.select = select;
    }
    // Liste
    public static ArrayList<VocabList> list = new ArrayList<VocabList>();


}
