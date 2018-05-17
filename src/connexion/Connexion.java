package connexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
	
	/**
	 * @author BONHOURE/MASSARD
	 * Cette classe est utilisée uniquement pour créer l'objet connection
	 * et permettre la connexion avec la base de données
	 */
	
        private static Connection connection;  

        private Connexion() { 
                try {
                        Class.forName("org.sqlite.JDBC");
                        connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/gregb/OneDrive/Bureau/SQlite/sqlite-tools-win32-x86-3230100/tournoi");
                }

                catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public static Connection getInstance(){
                if(connection == null){
                        new Connexion();
                }
                return connection;
        }
}