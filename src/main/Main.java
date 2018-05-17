package main;

import java.sql.SQLException;
import java.util.Scanner;
import connexion.UtilitarianClass;
import tournoi.PhaseEliminatoire;
import tournoi.PhasePoule;
import tournoi.Point;
import tournoi.Resultat;
import tournoi.TournoiChampionnat;

public class Main {

	/**
	 * @author BONHOURE/MASSARD Cette classe est dédiée à l'exécution de
	 *         l'application Elle ne contient qu'une seule méthode main C'est elle
	 *         qui va faire appel aux méthodes de toutes les autres classes et va
	 *         permettre les interactions
	 */

	public static void main(String[] args) throws SQLException {
		Scanner sc = UtilitarianClass.sc;
		System.out.println(
				"Bonjour et bienvenue pour créer votre propre tournoi"); /**
																			 * Lorsque le main est lancé on crée
																			 * automatiquement un nouveau tournoi
																			 */

		int choix1 = 0;
		/**
		 * L'utilisateur peut cependant choisir quel type de tournoi il souhaite lancer
		 */
		do {

			System.out.println(
					"Saisir 0 pour créer un tournoi à phase éliminatoire / Saisir 1 pour créer un Championnat / Saisir 2 si vous voulez renseigner un résultat / Saisir 3 pour consulter des résultats");
			int choix = sc.nextInt();

			if (choix == 0) { /** Différentes actions ont lieu en fonction du choix de l'utilisateur */
				PhaseEliminatoire.remise_à_zéro();
				System.out.println("Veuillez renseigner la date du tournoi : ");
				String date = sc.next();
				System.out.println("Veuiller renseigner le lieu du tournoi : ");
				String lieu = sc.next();
				System.out.println("Veuiller renseigner un code tournoi : ");
				int code_tournoi = sc.nextInt();
				System.out.println(
						"Veuiller renseigner le sport pratiqué lors du tournoi ainsi que la catégorie (Ex: Rugby Honneur) : ");

				String sport = UtilitarianClass.ajouterSport();
				TournoiChampionnat.creeTournoi(date, lieu, code_tournoi, sport);

				System.out.println("Combien d'équipe y aura-t-il dans le tournoi ? (Puissance de 2)");
				int Nb_equipe = sc.nextInt();
				System.out.println(Nb_equipe);
				if (!isPowerof2(Nb_equipe)) {
					System.out.println("Le nombre n'est pas une puissance de 2");
				} else {
					for (int i = 1; i <= Nb_equipe; i++) {
						UtilitarianClass.ajouterEquipe(sport, i);
					}

					UtilitarianClass.ajouterJoueur(sport);
					int i =0;
					int d = Nb_equipe;
					while(d!=1) {
						d=d/2;
						i++;
					}
					
					for (int j =0 ; j<i; j++) {
						PhaseEliminatoire.generer_tableau_elim(sport);
						for (int k = 0; k < Nb_equipe / 2; k++) {
							Nb_equipe /= 2;
							PhaseEliminatoire.rentrer_resultat_E();
						}
						Resultat.changement_Statut(PhaseEliminatoire.annoncer_vainqueurs(sport));
					}
				}
			} else if (choix == 1) {
				PhaseEliminatoire.remise_à_zéro();
				System.out.println("Veuillez renseigner la date du tournoi : ");

				String date = sc.next();
				System.out.println("Veuiller renseigner le lieu du tournoi : ");
				String lieu = sc.next();
				System.out.println("Veuiller renseigner un code tournoi : ");

				int code_tournoi = sc.nextInt();
				System.out.println(
						"Veuiller renseigner le sport pratiqué lors du tournoi ainsi que la catégorie (Ex: Rugby Honneur) : ");

				String sport = UtilitarianClass.ajouterSport();
				TournoiChampionnat.creeTournoi(date, lieu, code_tournoi, sport);

				System.out.println("Combien d'équipe y aura-t-il dans le tournoi ?");
				int Nb_equipe1 = sc.nextInt();
				for (int i = 1; i <= Nb_equipe1; i++) {

					UtilitarianClass.ajouterEquipe(sport, i);
				}

				UtilitarianClass.ajouterJoueur(sport);

				TournoiChampionnat.generer_match_champ(sport);
				
				System.out.println("1. Fermer l'appli 2.Retourner au menu");
				sc.nextLine();
				choix1 = sc.nextInt();
				
			} else if (choix == 2) {
				System.out.println("Code tournoi");
				int code = sc.nextInt();
				if (code == TournoiChampionnat.Code_tournoi()) {

					int boucle = 1;
					while (boucle == 1) {
						UtilitarianClass.rentrer_resultat();
						System.out.println("Quit : 0 /nAutre résultat : 1 ");
						boucle = sc.nextInt();
					}

				} else {
					System.out.println("Code tournoi inconnu");
				}
			} else {
				System.out.println("Code tournoi");
				int code = sc.nextInt();
				if (code == TournoiChampionnat.Code_tournoi()) {

					Point.calculer_Classement();
					System.out.println("1.Afficher le classement");
					System.out.println("2.Voir le résultat d'un match");
					sc.nextLine();
					int b = sc.nextInt();
					if (b == 1) {
						PhasePoule.donner_classement();
					} else {
						Resultat.annoncer_Resultat();
					}
				} else {
					System.out.println("Code tournoi inconnu");
				}
				System.out.println("1. Fermer l'appli 2.Retourner au menu");
				sc.nextLine();
				choix1 = sc.nextInt();
			}
		} while (choix1 == 2);
	}

	public static boolean isPowerof2(int i) {
		return i > 0 && (i & (i - 1)) == 0;
	}

}
