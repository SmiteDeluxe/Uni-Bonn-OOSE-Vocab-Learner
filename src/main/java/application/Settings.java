package application;

/**
 * Class to construct Settings Json
 */
public class Settings {
    private String lang;

    public Settings(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
