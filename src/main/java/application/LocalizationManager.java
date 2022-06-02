package application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * handles internationalization
 */
public class LocalizationManager extends ControllerMainScene {
    private static SupportedLanguage currentLanguage = SupportedLanguage.ENGLISH;//defaults to english
    private static final Map<SupportedLanguage, Map<String, String>> translations = new HashMap<>();
    private static Settings u;

    public static void Init() {
        //Initialize the strings.

        Map<String, String> english = new HashMap<>();
        Map<String, String> german = new HashMap<>();

        english.put("vocab", "Vocabulary");
        english.put("learn", "Learn");
        english.put("goals", "Goals");
        english.put("reg", "Register");
        english.put("login", "Login");
        english.put("wrongLogText", "Wrong eMail and/or Password");
        english.put("serverText", "Server error");
        english.put("validMail", "Not a valid eMail");
        english.put("wrongRegText", "EMail already registered");
        english.put("pwLabel", "Password:");
        english.put("remember", "Remember me");
        english.put("listSetts1", "Language:");
        english.put("apply", "Apply");
        english.put("setting", "Settings");

        //English text for VocList
        //TODO Check translations
        english.put("searchField","Enter search");
        english.put("search","Search");
        english.put("add","Add");
        english.put("delete","Delete");
        english.put("switch","Enable");
        english.put("enable","Enable Edit-Mode");
        english.put("disable","Disable Edit-Mode");
        english.put("select","Select");
        //English text for VocAdd
        english.put("answerAdd","Enter Answer here");
        english.put("questionAdd","Enter Question here");
        english.put("languageAdd","Enter Language here");
        english.put("confirm","Confirm");
        english.put("cancel","Cancel");
        english.put("addVoc","Add Vocabulary");
        english.put("addLabel","Add Vocabulary");

        //English: Learning
        english.put("SelectedVocable", "Vocable");
        english.put("UserErrors", "Mistakes");
        english.put("UserTranslation", "Translation?");
        english.put("UserScored", "Score");
        english.put("SelectedVocableTranslation", "Answer");
        english.put("Phase", "New phase");
        english.put("UserScore", "Score");
        english.put("manualCorrectionButton", "Manual correction");
        english.put("manualCorrectButton", "Correct");
        english.put("manualPartlyCorrectButton", "Partly correct");
        english.put("manualWrongButton", "Wrong");
        english.put("ErrorCorrection", "Mistakes");
        english.put("submitErrorsButton", "Submit");
        english.put("solveButton", "Solve");
        english.put("nextButton", "Next");
        english.put("TestedVocables", "Vocable tested:");
        english.put("CorrectVocables", "completely correct:");
        english.put("AverageScored", "Average Points Scored:");
        english.put("WrongVocables", "completely wrong:");
        english.put("ScoreFinal", "Score:");
        english.put("PartCorrectVocables", "partly correct");
        english.put("showAllVocablesButton", "Show All Vocables");
        english.put("restartLearningButton", "Start learning again");
        english.put("hideAllVocablesButton", "Hide All Vocables");
        english.put("ErrorNoVocables", "No Vocables Selected");
        english.put("changeToVocabulary", "Go to Vocabulary");
        english.put("changeToGoals", "Go to Goals");
        english.put("numberColumn", "No");
        english.put("questionColumn", "1. Language");
        english.put("answerColumn", "2. Language");
        english.put("userTranslationColumn", "Your Answer");
        english.put("errorColumn", "Mistakes");
        english.put("newPhaseColumn", "new Phase");


        // Goals
        english.put("filterLang", "Filter Language");
        english.put("selectAll", "Select All");
        english.put("startLearning", "Start Lerning");
        english.put("startLearningRand", "Start Learning in Random Order");
        english.put("answer", "Answer");
        english.put("question", "Question");
        english.put("phase", "Phase");
        english.put("id", "ID");
        english.put("language", "Language");
        english.put("selectToLearn", "Learn");
        english.put("allLangLabel", "All");
        english.put("deselectAll", "Deselect All");

        german.put("vocab", "Vokabeln");
        german.put("learn", "Lernen");
        german.put("goals", "Ziele");
        german.put("reg", "Registrieren");
        german.put("login", "Login");
        german.put("wrongLogText", "Falsche Email und/oder Passwort");
        german.put("serverText", "Server Error");
        german.put("validMail", "Keine g\u00FCltige eMail");
        german.put("wrongRegText", "EMail bereits registriert");
        german.put("pwLabel", "Passwort:");
        german.put("remember", "Anmeldung merken");
        german.put("listSetts1", "Sprache:");
        german.put("apply", "Anwenden");
        german.put("setting", "Einstellungen");

        //German text for VocList
        //TODO Check translations
        german.put("searchField","Suche hier ein eingeben");
        german.put("search","Suchen");
        german.put("add","Hinzuf\u00FCgen");
        german.put("delete","L\u00F6schen");
        german.put("switch","Ein");
        german.put("enable","Bearbeitungs-Modus Aus");
        german.put("disable","Bearbeitungs-Modus An");
        german.put("select","Ausw\u00E4hlen");
        //German text for VocAdd
        german.put("answerAdd","Antwort hier eingeben");
        german.put("questionAdd","Frage hier eingeben");
        german.put("languageAdd","Sprache hier eingeben");
        german.put("confirm","Best\u00E4tigen");
        german.put("cancel","Abbrechen");
        german.put("addVoc","Vokabel hinzuf\u00FCgen");
        german.put("addLabel","Vokabeln hinzuf\u00FCgen");

        // Goals
        german.put("filterLang", "Nach Sprache Filtern");
        german.put("selectAll", "Alle Ausw\u00E4hlen");
        german.put("startLearning", "Lernen Starten");
        german.put("startLearningRand", "Lernen in zuf\u00E4lliger Reihenfolge");
        german.put("answer", "Antwort");
        german.put("question", "Frage");
        german.put("phase", "Phase");
        german.put("language", "Sprache");
        german.put("id", "ID");
        german.put("selectToLearn", "Lernen");
        german.put("allLangLabel", "Alle");
        german.put("deselectAll", "Keine Ausw\u00E4hlen");

        //German: Learning
        german.put("SelectedVocable", "Vokabel");
        german.put("UserErrors", "Fehler");
        german.put("UserTranslation", "\u00dcbersetzung?");
        german.put("UserScored", "Punkte");
        german.put("SelectedVocableTranslation", "L\u00f6sung");
        german.put("Phase", "Neue Phase");
        german.put("UserScore", "Punkte");
        german.put("manualCorrectionButton", "Manuelle Korrektur");
        german.put("manualCorrectButton", "Korrekt");
        german.put("manualPartlyCorrectButton", "Teilweise korrekt");
        german.put("manualWrongButton", "Falsch");
        german.put("ErrorCorrection", "Fehler");
        german.put("submitErrorsButton", "Best\u00E4tigen");
        german.put("solveButton", "Aufl\u00f6sen");
        german.put("nextButton", "Weiter");
        german.put("TestedVocables", "Vokabeln getestet");
        german.put("CorrectVocables", "Korrekte Antworten:");
        german.put("AverageScored", "Durschnittliche Punkte:");
        german.put("WrongVocables", "Falsche Antworten:");
        german.put("ScoreFinal", "Punkte:");
        german.put("PartCorrectVocables", "Zum Teil richtige Antworten:");
        german.put("showAllVocablesButton", "Alle Vokabeln anzeigen");
        german.put("restartLearningButton", "Nochmal lernen");
        german.put("hideAllVocablesButton", "Alle Vokablen verstecken");
        german.put("ErrorNoVocables", "Keine Vokabeln ausgew\u00E4hlt");
        german.put("changeToVocabulary", "Gehe zu Vokabeln");
        german.put("changeToGoals", "Gehe zu Ziele");
        german.put("numberColumn", "Nr.");
        german.put("questionColumn", "1. Sprache");
        german.put("answerColumn", "2. Sprache");
        german.put("userTranslationColumn", "Antworten");
        german.put("errorColumn", "Fehler");
        german.put("newPhaseColumn", "neue Phase");




        translations.put(SupportedLanguage.ENGLISH, english);
        translations.put(SupportedLanguage.GERMAN, german);

        Gson g = new Gson();
        try {
            u = g.fromJson(new FileReader("src/main/resources/Settings.json"), Settings.class);
            currentLanguage = SupportedLanguage.valueOf(u.getLang());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String get(String key) {
        return translations.get(currentLanguage).get(key);
    }

    public static void setLanguage(SupportedLanguage language) throws IOException {
        currentLanguage = language;
        Gson g = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        u.setLang(language.toString());
        String s = g.toJson(u);
        FileWriter file = new FileWriter("src/main/resources/Settings.json");
        file.write(s);
        file.close();
    }

    public enum SupportedLanguage {
        ENGLISH, GERMAN
    }
}
