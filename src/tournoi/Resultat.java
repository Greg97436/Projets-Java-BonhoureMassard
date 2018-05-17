package tournoi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import connexion.Connexion;

public class Resultat extends Tournoi {
	
	/**
	 * @author SARDOU CONNEMARA
	 * @param date
	 * @param lieu
	 * @param code_tournoi
	 * @param sport
	 * Cette classe contient toutes les méthodes gérant la rentrée et la sortie des résultats
	 */
	
	public Resultat(String date, String lieu, int code_tournoi, String sport) {
		super(date, lieu, code_tournoi, sport);
	}

	public static void annoncer_Resultat() throws SQLException {
		
		/**
		 * Cette méthode permet de récupérer le résultat d'un match du tournoi
		 */
		
		Scanner sc = new Scanner(System.in);
		System.out.println("1.Je veux le résultat d'un match");
		System.out.println("2.Je veux un résultat individuel");
		int a = sc.nextInt();
		if(a==1) {
			System.out.println("Quelle est le nom de la première équipe ?");
			sc.nextLine();
			String e1 = sc.next();
			System.out.println("Quelle est le nom de la deuxième équipe ?");
			sc.nextLine();
			String e2 = sc.next();
			Statement state = Connexion.getInstance().createStatement();
			ResultSet result1 = state.executeQuery("SELECT score_1 FROM match WHERE equipe_1 LIKE '" + e1 + "' AND equipe_2 LIKE '" + e2 + "';");
			int sc1 = 0;
			int sc2 = 0;
			while(result1.next()) {
				sc1 = result1.getInt(1);
			}
			result1.close();
			ResultSet result2 = state.executeQuery("SELECT score_2 FROM match WHERE equipe_1 LIKE '" + e1 + "' AND equipe_2 LIKE '" + e2 + "';");
			while(result2.next()) {
				sc2 = result2.getInt(1);
			}
			result2.close();
			System.out.println("Le score de " + e1 + "-" + e2 + " est " + sc1 + "-" + sc2);
		} else {
			System.out.println("Quelle est le nom de l'équipe ?");
			sc.nextLine();
			String e = sc.next();
			Statement state = Connexion.getInstance().createStatement();
			ResultSet result = state.executeQuery("SELECT score_1 FROM match WHERE equipe_1 = " + e + ";");
			int score = 0;
			while(result.next()) {
				score += result.getInt("score_1");
			}
			System.out.println("Le score de " + e + " est " + score + ";");
			result.close();
		}
		
	}
	
	public static void changement_Statut(ArrayList<String> list_vainqueurs) throws SQLException {
		
		/**
		 * @param list_vainqueurs contenant la liste des vainqueurs des matchs
		 * Cette méthode permet de modifier la BDD en lui indiquant que les équipes perdantes ne sont plus en tournoi
		 */

		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT nom FROM equipe");
		ArrayList<String> equipes = new ArrayList<String>();
		while(result.next()) {
			equipes.add(result.getString("nom"));
		}
		for(int i=0; i< equipes.size(); i++) {
			if(!list_vainqueurs.contains(equipes.get(i))) {
				PreparedStatement preparedState = Connexion.getInstance().prepareStatement("UPDATE equipe SET en_tournoi = 0 WHERE nom = ?;");
				preparedState.setString(1, equipes.get(i));
			}			
		}
	}
}
