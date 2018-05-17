package tournoi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import connexion.Connexion;
import connexion.UtilitarianClass;

public class PhasePoule {

	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe permet de gérer des poules
	*/
	
	public static void donner_classement() throws SQLException {
		/**
		 * Cette méthode permet de donner le classement du championnat en cours
		 */
		
		System.out.println("Pour quel sport voulez-vous voir le classement ?");
		String sport = UtilitarianClass.sc.next();
		UtilitarianClass.sc.nextLine();
		System.out.println("Dans quelle poule (si existante) voulez-vous voir le classement ? (int attendu Sinon 0)");
		int poule = UtilitarianClass.sc.nextInt();
		System.out.println("Le classement du championnat est");
		Statement state6 = Connexion.getInstance().createStatement();
		ResultSet result6 = state6.executeQuery("SELECT e.nom FROM equipe AS e WHERE poule = " + poule
				+ " AND id_sport = " + PhaseEliminatoire.retourne_id_sport(sport) + " ORDER BY point DESC;");
		int place = 1;
		ArrayList<String> List_Classement = new ArrayList<String>();
		while (result6.next()) {
			List_Classement.add(result6.getString("nom"));
			System.out.println(place + ". " + result6.getString("nom"));
			place += 1;
		}
	}

	public static ArrayList<String> annoncer_vainqueur_poule(String sport) throws SQLException {

		/**
		 * @param sport
		 * @return une liste contenant le nom des équipes qualifiées
		 */
		
		ArrayList<String> List_qualif = new ArrayList<String>();
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery(
				"SELECT e.nom , COUNT(*) AS point FROM equipe AS e, point AS p ORDER BY point DESC LIMIT 2 WHERE id_sport ="
						+ PhaseEliminatoire.retourne_id_sport(sport) + ";");
		while (result.next()) {
			List_qualif.add(result.getString("nom"));
		}
		return List_qualif;
	}

	public static ArrayList<String> retourne_list_qualifie(String sport, int Nb_poules) throws SQLException {
		ArrayList<String> ListQualif = new ArrayList<String>();
		for (int i = 0; i <= Nb_poules; i++) {
			ListQualif.addAll(annoncer_vainqueur_poule(sport));
		}
		return ListQualif;

	}
}
