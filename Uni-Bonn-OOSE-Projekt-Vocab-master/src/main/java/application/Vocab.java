package application;

import com.google.gson.annotations.Expose;


import java.util.ArrayList;
import java.util.List;

/**
 * represents a JSON vocabulary entry from the server
 * @author ***
 */
public class Vocab {

    int id;
    @Expose
    String answer;
    @Expose
    String question;
    @Expose
    String language;
    int phase;
    boolean select;

    /**
     * Creates new Vocab entry
     */
    public Vocab (){

    }

    /**
     * Creates new Vocab entry
     *
     * @param id Vocab id in list
     * @param answer
     * @param question
     * @param language
     * @param phase
     */
    public Vocab(int id, String answer, String question, String language, int phase){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }
    public Vocab(String answer, String question, String language){
        this.answer = answer;
        this.question = question;
        this.language = language;
    }
    //Transfered VocabList Constructors to Vocab
    public Vocab (int id, String answer, String question,String language, int phase, boolean select){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.select = select;
    }

    public Vocab (String answer, String question,String language, int phase){
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }

    //Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    //Getter methods
    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getLanguage() {
        return language;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isSelect() {
        return select;
    }

    public boolean isSelected() {
        return select;
    }

    /**
     * inits Vocab entry from Gson
     * @param gson Gson as String
     * @return
     */
    public Vocab fromGson(String gson){
        // TODO implement
        return new Vocab();
    }

    /**
     * pares entire Vocab List from Gson. Can be used the retract the date from the API.
     * @return
     */
    public static List<Vocab> parseVocabListFromGson(String gson){
        return new ArrayList<Vocab>();
    }

    public String toString(){
        return ("ID# " + this.id + ": " + this.question + " : " + this.answer + " in " + this.language +
                ", phase " + this.phase);
    }
}
