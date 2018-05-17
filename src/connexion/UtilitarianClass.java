package connexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import tournoi.PhaseEliminatoire;
import tournoi.TournoiChampionnat;

public class UtilitarianClass {

	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe est dédiée à la création du tournoi par l'utilisateur
	 * Elle est donc remplie de méthodes et de scanners afin de communiquer avec l'utilisateur pour qu'il complète les informations manquantes
	*/
	
	public static Scanner sc = new Scanner(System.in);

	public static String ajouterSport() throws SQLException {
		/**
		 * @return un String qui sera le nom du sport du tournoi créé
		 * Cette classe permet donc de préciser le sport du tournoi et même
		 * de l'ajouter à la BDD s'il n'est pas déjà dedans
		 */

		Statement state = Connexion.getInstance().createStatement();
		ResultSet result = state.executeQuery("SELECT * FROM sport;");
		ArrayList<String> listSports = new ArrayList<String>();
		while (result.next()) {
			listSports.add(result.getString("nom"));
		}
		int id = listSports.size();
		result.close();
		state.close();
		System.out.println("Quel sport souhaitez-vous faire ?");
		UtilitarianClass.sc.nextLine();
		String nom = UtilitarianClass.sc.nextLine();
		if (!listSports.contains(nom)) {
			try {
				PreparedStatement preparedState = Connexion.getInstance().prepareStatement(
						"INSERT INTO sport(id,nom,nb_joueurs_min,nb_remplacant_max) VALUES (?,?,?,?);");
				preparedState.setInt(1, id);
				preparedState.setString(2, nom);
				System.out.println("Quel est le nombre de joueurs minimum ?");
				int nb_joueur_min = UtilitarianClass.sc.nextInt();
				preparedState.setInt(3, nb_joueur_min);
				System.out.println("Quel est le nombre de remplaçants maximum ?");
				UtilitarianClass.sc.nextLine();
				int nb_remplacant_max = UtilitarianClass.sc.nextInt();
				preparedState.setInt(4, nb_remplacant_max);

				preparedState.executeUpdate();

				preparedState.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Ok Le sport existe ");
		}
		return nom;
	}

	public static void ajouterEquipe(String sport, int NumeroEquipe) {

		/**
		 * @param sport qui est le nom du sport retourné par la méthode précédente
		 * @param le numéro de l'équipe donné dans la méthode main
		 * Cette classe permet d'ajouter les équipes du tournoi dans la BDD
		 */
		System.out.println("Vous allez rentrer l'équipe" + NumeroEquipe);
		try {
			PreparedStatement preparedState = Connexion.getInstance().prepareStatement(
					"INSERT INTO equipe(nom,id_sport,nb_joueurs,capitaine,poule,point,en_tournoi) VALUES (?,?,?,?,?,?,?);");
			System.out.println("Quel est le nom de l'équipe ?");
			UtilitarianClass.sc.nextLine();
			String nom = UtilitarianClass.sc.next();
			preparedState.setString(1, nom);
			preparedState.setInt(2, PhaseEliminatoire.retourne_id_sport(sport));
			System.out.println("Combien y-a-t-il de joueurs dans l'équipe ?");
			UtilitarianClass.sc.nextLine();
			int nb_joueurs = UtilitarianClass.sc.nextInt();
			preparedState.setInt(3, nb_joueurs);
			System.out.println("Qui est le capitaine ?");
			UtilitarianClass.sc.nextLine();
			String capitaine = UtilitarianClass.sc.next();
			preparedState.setString(4, capitaine);
			preparedState.setInt(5, 0);
			preparedState.setInt(6, 0);
			preparedState.setBoolean(7, true);
			preparedState.executeUpdate();

			preparedState.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void ajouterJoueur(String sport) throws SQLException {
		/**
		 * @param sport qui est le nom du sport retourné par la première méthode
		 * Cette classe permet d'ajouter les joueurs du tournoi dans la BDD
		 */
		Statement state = Connexion.getInstance().createStatement();
		ResultSet result2 = state.executeQuery("SELECT nb_joueurs_min FROM sport WHERE nom LIKE '" + sport + "';");
		int joueurs_min = 0;
		int remplacant_max = 0;
		while (result2.next()) {
			joueurs_min = result2.getInt(1);
		}
		result2.close();
		ResultSet result3 = state.executeQuery("SELECT nb_remplacant_max FROM sport WHERE nom LIKE '" + sport + "';");
		while (result3.next()) {
			remplacant_max = result3.getInt(1);
		}
		result3.close();
		state.close();
		int tot = joueurs_min + remplacant_max;

		System.out.println("Vous devez ajouter entre " + joueurs_min + " et " + tot + " joueurs");
		System.out.println("Souhaitez-vous ajouter un joueur ?");

		String encore = UtilitarianClass.sc.next();

		int i = TournoiChampionnat.retourne_id_max("joueurs");
		try {
			while (encore.equals("oui")) {
				PreparedStatement preparedState = Connexion.getInstance()
						.prepareStatement("INSERT INTO joueurs(id,nom,equipe,capitaine) VALUES (?,?,?,?)");
				preparedState.setInt(1, i);
				System.out.println("Quel est le nom du joueur ?");
				UtilitarianClass.sc.nextLine();
				String nom = UtilitarianClass.sc.next();
				preparedState.setString(2, nom);
				System.out.println("Quelle est son équipe ?");
				UtilitarianClass.sc.nextLine();
				String equipe = UtilitarianClass.sc.next();
				preparedState.setString(3, equipe);
				System.out.println("Est-il capitaine ? (true/false)");
				UtilitarianClass.sc.nextLine();
				boolean capitaine = UtilitarianClass.sc.nextBoolean();
				preparedState.setBoolean(4, capitaine);

				preparedState.executeUpdate();

				preparedState.close();
				i++;

				System.out.println("Souhaitez-vous ajoutez un autre joueur ?");
				UtilitarianClass.sc.nextLine();
				encore = UtilitarianClass.sc.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rentrer_resultat() throws SQLException {
		/**
		 * Cette classe permet d'ajouter les résultats des matchs ou les performances des athlètes dans la BDD
		 * en fonction du type de résultat qu'ils auront (distance, temps, point)
		 */
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

			Statement state = Connexion.getInstance().createStatement();
			int id = 0;
			ResultSet result = state.executeQuery(
					"SELECT id FROM match WHERE equipe_1 LIKE '" + equipe1 + "' AND equipe_2 LIKE '" + equipe2 + "' ;");

			if (result.next()) { 
									
				id = result.getInt("id");

				result.close();
				PreparedStatement preparedState = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_1=? WHERE id=? ;");
				preparedState.setInt(1, score1);
				preparedState.setInt(2, id);
				preparedState.execute();

				System.out.println(score2);
				PreparedStatement preparedState1 = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_2=? WHERE id=? ;");
				preparedState1.setInt(1, score2);
				preparedState1.setInt(2, id);
				preparedState1.execute();
				state.close();
			} else {

				Statement state1 = Connexion.getInstance().createStatement();
				ResultSet result1 = state1.executeQuery("SELECT id FROM match WHERE equipe_1 LIKE '" + equipe2
						+ "' and equipe_2 LIKE '" + equipe1 + "' ;");
				int id1 = 0;
				while (result1.next()) {
					id1 = result1.getInt("id");
				}
				result1.close();

				PreparedStatement preparedState2 = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_1=? WHERE id=? ;");
				preparedState2.setInt(1, score2);
				preparedState2.setInt(2, id1);
				preparedState2.execute();

				PreparedStatement preparedState3 = Connexion.getInstance()
						.prepareStatement("UPDATE match SET score_2=? WHERE id=? ;");
				preparedState3.setInt(1, score1);
				preparedState3.setInt(2, id1);
				preparedState3.execute();
				state1.close();
			}

		} else if (type.equals("temps")) {
			System.out.println("Quel est le nom de l'équipe ?");
			UtilitarianClass.sc.nextLine();
			String equipe = UtilitarianClass.sc.nextLine();
			System.out.println("Quel est le temps de l'équipe ? (Ecrire sous la forme hr:min:s:cs)");
			UtilitarianClass.sc.nextLine();
			String score = UtilitarianClass.sc.next();
			try {
				Statement state = Connexion.getInstance().createStatement();
				ResultSet result = state.executeQuery("SELECT id FROM match WHERE equipe = " + equipe);
				ResultSet result1 = state
						.executeQuery("UPDATE match SET score_1=" + String.valueOf(score) + " WHERE id=0)");
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

	public static void main(String[] args) throws SQLException {
		rentrer_resultat();
	}
}