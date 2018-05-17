package tournoi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connexion.Connexion;

public class Point extends Resultat {

	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe permet de calculer le classement d'un sport se déroulant avec des points
	*/
	
	public Point(String date, String lieu, int code_tournoi, String sport) {
		super(date, lieu, code_tournoi, sport);
	}

	public static void calculer_Classement() throws SQLException {

		/**
		 * Cette méthode permet de calculer le classement d'une poule en fonction du vainqueur de chaque match
		 */
		
		Statement state = Connexion.getInstance().createStatement();
		PreparedStatement preparedState = Connexion.getInstance().prepareStatement("UPDATE equipe SET point = 0");
		preparedState.execute();
		// Si equipe 1 gagne
		ResultSet result = state.executeQuery("SELECT equipe_1 FROM match WHERE score_1>score_2;");

		ArrayList<String> listVainqueur1 = new ArrayList<String>();
		while (result.next()) {
			listVainqueur1.add(result.getString("equipe_1"));
		}
		

		state.close();
		result.close();
		for (int i = 0; i < listVainqueur1.size(); i++) {
			System.out.println(listVainqueur1.size());
			PreparedStatement preparedState1 = Connexion.getInstance()
					.prepareStatement("UPDATE equipe SET point = point+3 WHERE nom LIKE ?;");
			preparedState1.setString(1, listVainqueur1.get(i));
			preparedState1.execute();
		}
		
		// Si equipe 2 gagne
		Statement state1 = Connexion.getInstance().createStatement();
		ResultSet result1 = state1.executeQuery("SELECT equipe_2 FROM match WHERE score_2>score_1;");

		ArrayList<String> listVainqueur2 = new ArrayList<String>();
		while (result1.next()) {
			listVainqueur2.add(result1.getString("equipe_2"));
		}
		System.out.println(listVainqueur2);

		state1.close();
		result1.close();
		for (int j = 0; j < listVainqueur2.size(); j++) {
			PreparedStatement preparedState2 = Connexion.getInstance()
					.prepareStatement("UPDATE equipe SET point = point+3 WHERE nom LIKE ? ;");
			preparedState2.setString(1, listVainqueur2.get(j));
			preparedState2.execute();
		}

		// Si match nul
		Statement state4 = Connexion.getInstance().createStatement();
		ResultSet result4 = state4.executeQuery("SELECT equipe_1,equipe_2 FROM match WHERE score_1=score_2;");
		ArrayList<String> listEquipes1 = new ArrayList<String>();
		ArrayList<String> listEquipes2 = new ArrayList<String>();
		ArrayList<String> listEquipes = new ArrayList<String>();
		while (result4.next()) {
			listEquipes1.add(result4.getString("equipe_1"));
			listEquipes2.add(result4.getString("equipe_2"));
		}
		listEquipes.addAll(listEquipes1);
		listEquipes.addAll(listEquipes2);
		state4.close();
		result4.close();
		for (int k = 0; k < listEquipes.size(); k++) {
			PreparedStatement preparedState3 = Connexion.getInstance()
					.prepareStatement("UPDATE equipe SET point = point+1 WHERE nom LIKE ? ;");
			preparedState3.setString(1, listEquipes.get(k));
			preparedState3.execute();
		}
	}
}
