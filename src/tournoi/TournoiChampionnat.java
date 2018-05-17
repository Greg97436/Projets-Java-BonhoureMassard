package tournoi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Scanner;

import connexion.Connexion;
import connexion.UtilitarianClass;

public class TournoiChampionnat extends Tournoi {
	
	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe gère un tournoi de type championnat
	*/

	public TournoiChampionnat(String date, String lieu, int code_tournoi, String sport) { /**On récupère les éléments de la classe mère*/
		super( date, lieu, code_tournoi, sport);
	}

	public static void generer_match_champ(String sport) throws SQLException {

		/**
		 * @param sport qui est le sport pratiqué dans le championnat
		 * Cette méthode génère les matchs du championnat en créant une nouvelle table
		 * et en y plaçant tous les matchs entre toutes les équipes inscrites dans ce sport
		 */
		
		PreparedStatement preparedState2 = Connexion.getInstance().prepareStatement("SELECT e.nom FROM equipe AS e WHERE e.id_sport = ? ;");
		preparedState2.setInt(1, PhaseEliminatoire.retourne_id_sport(sport));
		ResultSet result2 = preparedState2.executeQuery();

		ArrayList<Object> listEquipes = new ArrayList<Object>();
		while (result2.next()) {
			listEquipes.add(result2.getString("nom"));
		}
		preparedState2.close();
		result2.close();
		System.out.println(listEquipes);
		int id = listEquipes.size();
		int n = retourne_id_max("match");
		for (int j = 0; j <= id - 1; j++) {
			for (int i = j+1 ; i <= id - 1; i++) {

				PreparedStatement preparedState3 = Connexion.getInstance().prepareStatement(
						"INSERT INTO match (id , equipe_1 , equipe_2 , score_1 , score_2) VALUES (?,?,?,0,0)");
				preparedState3.setInt(1, n);
				preparedState3.setObject(2, listEquipes.get(j));
				preparedState3.setObject(3, listEquipes.get(i));
				preparedState3.execute(); 
				n ++;
			}
		}
	}

	public static int retourne_id_max(String table) throws SQLException {
		int id_max = 0;
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT MAX(id) FROM "+table+" ;");
		while (result.next()) {
			id_max=result.getInt(1);
		}
		return id_max;
	}
	
	/**
	 * La fonction n'est jamais utilisée
	 */
	
//	public static void optimiser_nb_poule() throws SQLException {
//		
//		/**
//		 * Permet de faire des poules optimales en fonction du nombre d'équipes participantes
//		 */
//		
//		Scanner sc = new Scanner(System.in);
//		Statement state2 = Connexion.getInstance().createStatement();
//		String sql = "SELECT e.nom FROM equipe AS e WHERE e.id_sport = " + 3 + ";";
//		ResultSet result2 = state2.executeQuery(sql);
//		ArrayList<String> listEquipes = new ArrayList<String>();
//
//		while (result2.next()) {
//			listEquipes.add(result2.getString("nom"));
//		}
//
//		state2.close();
//		result2.close();
//		int n = listEquipes.size();
//		System.out.println(n);
//		System.out.println("Combien de poules voulez-vous faire?");
//		int NbPoules = sc.nextInt();
//		System.out.println("Combien d'équipe par poule ?");
//		int Nb_equi_ppoule = sc.nextInt();
//		for (int i = 0; i <= n; i++) {
//			Statement state3 = Connexion.getInstance().createStatement();
//			ResultSet result3 = state3
//					.executeQuery("UPDATE equipe SET poule =" + String.valueOf(i % NbPoules) + " WHERE nom LIKE '" + listEquipes.get(i) + "' ;"); // revoir le calcul
//			state3.close();
//			result3.close();
//		}
//		sc.close();
//	}

	public static void main(String[] args) throws SQLException {
		retourne_id_max("joueurs");
	}

	public static void creeTournoi(String date, String lieu, int code_tournoi, String sport) throws SQLException {
		/**
		 * @param date
		 * @param lieu
		 * @param code_tournoi
		 * @param sport
		 * Remplie la table
		 */
		
		PreparedStatement prepareState1 = Connexion.getInstance().prepareStatement("INSERT INTO tournoi (code_tournoi, lieu,date, sport ) VALUES (?,?,?,?) ;");
		prepareState1.setInt(1, code_tournoi);
		prepareState1.setString(2, lieu);
		prepareState1.setString(3, date);
		prepareState1.setString(4, sport);
		prepareState1.execute();
		
	}

	public static int Code_tournoi() throws SQLException {
		
		/**
		 * @return code_tournoi
		 * Retourne le code tournoi du tournoi créé
		 */
		
		Statement state4 = Connexion.getInstance().createStatement();
		ResultSet result4 = state4.executeQuery("SELECT code_tournoi FROM tournoi;");
		return result4.getInt(1);
	}

}
