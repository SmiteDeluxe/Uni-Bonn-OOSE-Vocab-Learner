package application;


import java.util.List;

/**
 * Contains global Variables to pass between Classes
 */
public class Variables {
	private static String mail, server;
	private static List<Vocab> usersVocab, selectedVocab;
	private static double x,y;
	private static ControllerVocList c;

	public static String getServer() { return server; }
	public static void setServer(String server) {
		Variables.server = server;
	}

	public static List<Vocab>  getUsersVocab() {
		return usersVocab;
	}
	public static void setUsersVocab(List<Vocab>  usersVocab) {
		Variables.usersVocab = usersVocab;
	}

	public static List<Vocab> getSelectedVocab() { return selectedVocab; }
	public static void setSelectedVocab(List<Vocab> selectedVocab) { Variables.selectedVocab = selectedVocab; }

	public static String getMail() {
		return mail;
	}
	public static void setMail(String mail) {
		Variables.mail = mail;
	}

	public static double getX() { return x; }
	public static void setX(double x) { Variables.x = x; }

	public static double getY() { return y; }
	public static void setY(double y) { Variables.y = y; }

	public static ControllerVocList getC() {
		return c;
	}

	public static void setC(ControllerVocList c) {
		Variables.c = c;
	}
}
