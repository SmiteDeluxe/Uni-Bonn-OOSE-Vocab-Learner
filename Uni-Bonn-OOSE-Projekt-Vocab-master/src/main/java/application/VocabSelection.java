package application;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

/**
 * Class to represent the Vocab entries in a list with an additional checkbox.
 * NOTE: not compatible with the json parsed from the API.
 * @author ***
 */
public class VocabSelection extends Vocab {

    private CheckBox selectVocab;
    private Button deleteButton;
    private Button editButton;

    public VocabSelection(){
    }


    public VocabSelection(Vocab v, boolean checkedState){
        this.selectVocab = new CheckBox();
        super.answer = v.answer;
        super.id = v.id;
        super.language = v.language;
        super.phase = v.phase;
        super.question = v.question;
        this.getSelectVocab().setSelected(checkedState);
    }

    public VocabSelection(int id, String answer, String question, String language, int phase){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.selectVocab = new CheckBox();
    }

    public VocabSelection(int id, String answer, String question, String language, int phase, boolean checkedState){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.selectVocab = new CheckBox();
        this.getSelectVocab().setSelected(checkedState);
    }

    /**
     * @return state of checkbox (true = checked)
     */
    public CheckBox getSelectVocab(){
        return selectVocab;
    }

    /**
     * Allows to check the checkbox of the object
     * @param selectVocab checkbox is set if true
     */
    public void setSelectVocab(CheckBox selectVocab) {
        this.selectVocab = selectVocab;
    }
}
