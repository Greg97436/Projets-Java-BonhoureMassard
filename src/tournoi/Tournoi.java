package tournoi;

public abstract class Tournoi {
	/**
	 * @author BONHOURE/MASSARD
	 * C'est la classe mère possédant les attributs ne figurant pas forcément dans la BDD
	*/
	
	private String date;
	private String lieu;
	private int code_tournoi;
	private String sport;
	
	public Tournoi(String date, String lieu, int code_tournoi, String sport) {
		super();
		this.date = date;
		this.lieu = lieu;
		this.code_tournoi = code_tournoi;
		this.sport = sport;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLieu() {
		return lieu;
	}
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	public int getCode_tournoi() {
		return code_tournoi;
	}
	public void setCode_tournoi(int code_tournoi) {
		this.code_tournoi = code_tournoi;
	}
}
