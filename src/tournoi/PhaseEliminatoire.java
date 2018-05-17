package tournoi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import connexion.Connexion;
import connexion.UtilitarianClass;

public class PhaseEliminatoire {
	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe permet de gérer des phases éliminatoires
	*/
	public static int retourne_id_sport(String sport) throws SQLException {
		/**
		 * @param sport
		 * @return un int qui sera l'id du sport en entrée
		 */
		
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT s.id FROM sport AS s WHERE s.nom LIKE '"+sport+"' ;");
		int a = 0;
		while (result.next()) {
			a += result.getInt("id");
		}
		return a;
		
	}
    public static String retourne_nom_sport(int id_sport) throws SQLException {
    	/**
    	 * @param id_sport
		 * @return un String qui sera le nom du sport de l'id en entrée
		 */
		
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT s.nom FROM sport AS s WHERE s.id = "+id_sport+" ;");
		String a = new String();
		while (result.next()) {
			a = result.getString("nom");
		}
		System.out.println(a);
		return a;
		
	}

	public static void annoncer_vainqueur_final() throws SQLException {
		/**
		 * Cette méthode affiche le vainqueur du tournoi
		 */
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Pour quel sport voulez-vous le vainqueur?");
		String sport = sc.next();
		System.out.println("Le vainqueur du tournoi est");
		PreparedStatement preparedState1 = Connexion.getInstance()
				.prepareStatement("SELECT e.nom FROM equipe AS e WHERE id_sport = ? AND en_tournoi = true ;");
		preparedState1.setInt(1, retourne_id_sport(sport));
		ResultSet result1 = preparedState1.executeQuery();
		System.out.println(result1.getString(1));
	}
	
	public static ArrayList<String> annoncer_vainqueurs(String sport) throws SQLException {
		ArrayList<String> List_qualif =new ArrayList<String>();
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT equipe_1 FROM match WHERE score_1>score_2 ;");
		while (result.next()) {
			List_qualif.add(result.getString("equipe_1"));
		}
		ResultSet result1 = state.executeQuery("SELECT equipe_2 FROM match WHERE score_2>score_1 ;");
		while (result1.next()) {
			List_qualif.add(result1.getString("equipe_2"));
		}
		return List_qualif;
	}

	public static void generer_tableau_elim(String nom) throws SQLException {
		/**
		 * Cette méthode permet de créer les matchs qui
		 * vont avoir lieu et donc de générer le tableau
		 * initial des phases éliminatoires
		 */

		System.out.println(retourne_id_sport(nom));
		Statement state2 = Connexion.getInstance().createStatement();
		ResultSet result2 = state2
				.executeQuery("SELECT nom FROM equipe WHERE id_sport = " + retourne_id_sport(nom) + " AND en_tournoi = 1 ;");

		ArrayList<String> listEquipes = new ArrayList<String>();
		while (result2.next()) {
			listEquipes.add(result2.getString("nom"));
		}
		state2.close();
		result2.close();
		int id = listEquipes.size();
		System.out.println(listEquipes);
		int n = 0;
		PreparedStatement preparedState1 = Connexion.getInstance().prepareStatement(
				"DELETE FROM match;");
		preparedState1.execute();
		for (int j = 0; j <= id/2; j=j+2) {
				PreparedStatement preparedState2 = Connexion.getInstance().prepareStatement(
						"INSERT INTO match (id , equipe_1 , equipe_2 , score_1 , score_2) VALUES (?,?,?,NULL,NULL)");
				preparedState2.setInt(1, n);
				preparedState2.setObject(2, listEquipes.get(j));
				preparedState2.setObject(3, listEquipes.get(j+1));
				preparedState2.execute(); 
				n += 1;
			
		}

	}
	public static void rentrer_resultat_E() throws SQLException {
		System.out.println("Souhaitez-vous rentrer un score, un temps ou une distance ?");
		String type = UtilitarianClass.sc.next();
		if (type.equals("score")) {
			System.out.println("Quel est le nom de la première équipe?");
			UtilitarianClass.sc.nextLine();
			String equipe1 = UtilitarianClass.sc.nextLine();
			System.out.println("Quel est le nom de la seconde équipe?");
			String equipe2 = UtilitarianClass.sc.nextLine();
			System.out.println("Quel est le score de la première équipe?");
			
			int score1 = UtilitarianClass.sc.nextInt();
			System.out.println("Quel est le score de la seconde équipe?");
			int score2 = UtilitarianClass.sc.nextInt();
			try {
				Statement state = Connexion.getInstance().createStatement();
				ResultSet result = state
						.executeQuery("SELECT id FROM match WHERE equipe_1 LIKE '" + equipe1 + "' AND equipe_2 LIKE '" + equipe2 + "' ;");
				int id = 0;
				while(result.next()){
					id = result.getInt("id");
				}
				result.close();
				PreparedStatement preparedState = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_1=? WHERE id=? ;");
				preparedState.setInt(1,score1);
				preparedState.setInt(2,id);

				PreparedStatement preparedState1 = Connexion.getInstance().prepareStatement("UPDATE match SET score_2=? WHERE id=? ;");
				preparedState1.setInt(1,score2);
				preparedState1.setInt(2,id);
				state.close();
			} catch (SQLException e) {
				Statement state1 = Connexion.getInstance().createStatement();
				ResultSet result1 = state1
						.executeQuery("SELECT id FROM match WHERE equipe_1 LIKE '" + equipe2 + "' and equipe_2 LIKE '" + equipe1 +"' ;");
				int id1 = 0;
				while(result1.next()){
					id1 = result1.getInt("id");
				}
				result1.close();
				
				PreparedStatement preparedState = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_1=? WHERE id=? ;");
				preparedState.setInt(1,score2);
				preparedState.setInt(2,id1);
				
				
				PreparedStatement preparedState1 = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_2=? WHERE id=? ;");
				preparedState1.setInt(1,score1);
				preparedState1.setInt(2,id1);
				state1.close();
			}
		} else if (type.equals("temps")) {
			System.out.println("Quel est le nom de l'équipe ?");
			UtilitarianClass.sc.nextLine();
			String equipe = UtilitarianClass.sc.nextLine();
			System.out.println("Quel est le temps de l'équipe ? (Ecrire sous la forme hr:min:s:cs)");
			UtilitarianClass.sc.nextLine();
			String score =UtilitarianClass.sc.next();
			try {
				Statement state = Connexion.getInstance().createStatement();
				ResultSet result = state.executeQuery("SELECT id FROM match WHERE equipe = " + equipe);
				ResultSet result1 = state
						.executeQuery("UPDATE match SET score=" + String.valueOf(score) + " WHERE id=0)");
				result.close();
				result1.close();
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Quel est le nom de l'équipe ?");
			UtilitarianClass.sc.nextLine();
			String equipe = UtilitarianClass.sc.nextLine();
			System.out.println("Quel est la distance de l'équipe ? (Ecrire sous la forme m:cm)");
			UtilitarianClass.sc.nextLine();
			String score = UtilitarianClass.sc.next();
			try {
				Statement state = Connexion.getInstance().createStatement();
				ResultSet result = state.executeQuery("SELECT id FROM match WHERE equipe = " + equipe);
				ResultSet result1 = state
						.executeQuery("UPDATE match SET score=" + String.valueOf(score) + " WHERE id=0)");
				result.close();
				result1.close();
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	public static void nouveau_tour(String sport) throws SQLException {
		/** 
		 * @param sport
		 * Lors d'un tournoi à phase éliminatoire 
		 * cette méthode permet de 'reset' le tableau 
		 * avec les équipes suivantes*/
		PreparedStatement preparedState = Connexion.getInstance().prepareStatement("DELETE FROM match;");
		preparedState.execute();
		generer_tableau_elim(sport);
	}
		
	
	public static void remise_à_zéro() throws SQLException {
		/**
		 * Nous ne sommes malheureusement capable d'organiser 
		 * qu'un seul tournoi à la fois  
		 * donc il faut remettre les tables à jour 
		 * à la fin de chaque tournoi */
		PreparedStatement preparedstate = Connexion.getInstance().prepareStatement("DELETE FROM joueurs;");
		PreparedStatement preparedstate1 = Connexion.getInstance().prepareStatement("DELETE FROM equipe;");
		PreparedStatement preparedstate2 = Connexion.getInstance().prepareStatement("DELETE FROM tournoi;");
		PreparedStatement preparedstate3 = Connexion.getInstance().prepareStatement("DELETE FROM match;");
		preparedstate1.execute();
		preparedstate.execute();
		preparedstate2.execute();
		preparedstate3.execute();
	}

	public static void verif_score_ok() throws SQLException {
		
		/**
		 *Cette méthode vérifie que tous le scores du tour en cours sont rentrés 
		 */
		
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT score_1 FROM match");
		ArrayList ListScore = new ArrayList();
		while (result.next()) {
			ListScore.add(result.getObject("score_1"));
		}
		for (int i=0; i<ListScore.size();i++) {
			if (ListScore.get(i).equals("NULL")) {
				System.out.println("La saisie des scores n'est pas finie");
				rentrer_resultat_E();
			} else {
				System.out.println("On peut passer au tour suivant");
			}
		}
	}
	
}
