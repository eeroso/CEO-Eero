import java.sql.*;
import java.lang.*;
public class Toimipiste {
	
	// attribuutit, vastaavat tietokantataulun sarakkeita
    private int m_toimipiste_id;
	private String m_nimi;
	private String m_lahiosoite;
	private String m_postitoimipaikka;
	private String m_postinro;
	private String m_email;
	private String m_puhelinnro;

    public Toimipiste(){
 
    }
	// getterit ja setterit
	public int getToimipisteId()
	{
		return m_toimipiste_id;
	}
	public String getNimi() {
		return m_nimi;
	}
	public String getLahiosoite() {
		return m_lahiosoite;
	}
	public String getPostitoimipaikka() {
		return m_postitoimipaikka;
	}
	public String getPostinro() {
		return m_postinro;
	}
	public String getEmail() {
		return m_email;
	}
	public String getPuhelinnro() {
		return m_puhelinnro;
	}
	public void setToimipisteId (int id)
	{
		m_toimipiste_id = id;
	}
	public void setNimi (String ni) {
		m_nimi = ni;
	}
	public void setLahiosoite (String os) {
		m_lahiosoite = os;
	}
	public void setPostitoimipaikka (String ptp) {
		m_postitoimipaikka = ptp;
	}
	public void setPostinro (String pno) {
		m_postinro = pno;
	}
	public void setEmail (String mail) {
		m_email = mail;
	}
	public void setPuhelinnro (String puhveli) {
		m_puhelinnro = puhveli;
	}
    @Override
    public String toString(){
        return (m_toimipiste_id + " " + m_nimi);
    }
	/*
	Haetaan asiakkaan tiedot ja palautetaan asiakasolio kutsujalle.
	Staattinen metodi, ei vaadi fyysisen olion olemassaoloa.
	*/
	public static Toimipiste haeToimipiste(Connection connection, int id) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = id 
		String sql = "SELECT toimipiste_id, nimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro " 
					+ " FROM Toimipiste WHERE toimipiste_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, id); // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko == null) {
				throw new Exception("toimipiste ei loydy");
			}
		} catch (SQLException se) {
            // SQL virheet
                        throw se;
                } catch (Exception e) {
            // JDBC virheet
                        throw e;
		}
		// käsitellään resultset - laitetaan tiedot asiakasoliolle
		Toimipiste toimipisteOlio = new Toimipiste ();
		
		try {
			if (tulosjoukko.next () == true){
				//asiakas_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro
				toimipisteOlio.setToimipisteId (tulosjoukko.getInt("toimipiste_id"));
				toimipisteOlio.setNimi(tulosjoukko.getString("nimi"));
				toimipisteOlio.setLahiosoite (tulosjoukko.getString("lahiosoite"));
				toimipisteOlio.setPostitoimipaikka (tulosjoukko.getString("postitoimipaikka"));
				toimipisteOlio.setPostinro (tulosjoukko.getString("postinro"));
				toimipisteOlio.setEmail (tulosjoukko.getString("email"));
				toimipisteOlio.setPuhelinnro (tulosjoukko.getString("puhelinnro"));
			}
			
		}catch (SQLException e) {
			throw e;
		}
		// palautetaan asiakasolio
		
		return toimipisteOlio;
	}
	/*
	Lisätään asiakkaan tiedot tietokantaan.
	Metodissa "asiakasolio kirjoittaa tietonsa tietokantaan".
	*/
     public int lisaaToimipiste (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id -> ei voi lisätä, jos on jo kannassa
		String sql = "SELECT toimipiste_id" 
					+ " FROM Toimipiste WHERE toimipiste_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null; 
		
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getToimipisteId()); // asetetaan where ehtoon (?) arvo, olion asiakasid
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == true) { // asiakas loytyi
				throw new Exception("toimipiste on jo olemassa");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan INSERT
		sql = "INSERT INTO Toimipiste "
		+ "(toimipiste_id, nimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro) "
		+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		// System.out.println("Lisataan " + sql);
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot INSERTtiin
			lause.setInt( 1, getToimipisteId());
			lause.setString(2, getNimi()); 
			lause.setString(3, getLahiosoite());
			lause.setString(4, getPostitoimipaikka ());
			lause.setString(5, getPostinro ());
			lause.setString(6, getEmail ());
			lause.setString(7, getPuhelinnro ());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
		//	System.out.println("lkm " + lkm);
			if (lkm == 0) {
				throw new Exception("toimipiste lisaaminen ei onnistu");
			}
		} catch (SQLException se) {
            // SQL virheet
            throw se;
        } catch (Exception e) {
            // JDBC ym. virheet
            throw e;
		}
		return 0;
	}
	/*
	Muutetaan asiakkaan tiedot tietokantaan id-tietoa (avain) lukuunottamatta. 
	Metodissa "asiakasolio muuttaa tietonsa tietokantaan".
	*/
    public int muutaToimipiste (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id, virhe, jos ei löydy
		String sql = "SELECT toimipiste_id" 
					+ " FROM Toimipiste WHERE toimipiste_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getToimipisteId()); // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == false) { // asiakasta ei löytynyt
				throw new Exception("Asiakasta ei loydy tietokannasta");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan Update, päiviteään tiedot lukuunottamatta avainta
		sql = "UPDATE  Toimipiste "
		+ "SET nimi = ?, lahiosoite = ?, postitoimipaikka = ?, postinro = ?, email = ?, puhelinnro = ? "
		+ " WHERE toimipiste_id = ?";
		
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			
			// laitetaan olion attribuuttien arvot UPDATEen
			
			lause.setString(1, getNimi()); 
			lause.setString(2, getLahiosoite());
			lause.setString(3, getPostitoimipaikka ());
			lause.setString(4, getPostinro ());
			lause.setString(5, getEmail ());
			lause.setString(6, getPuhelinnro ());
			// where-ehdon arvo
            lause.setInt(7, getToimipisteId());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Asiakkaan muuttaminen ei onnistu");
			}
		} catch (SQLException se) {
            // SQL virheet
                throw se;
        } catch (Exception e) {
            // JDBC ym. virheet
                throw e;
		}
		return 0; // toiminto ok
	}
	/*
	Poistetaan asiakkaan tiedot tietokannasta. 
	Metodissa "asiakasolio poistaa tietonsa tietokannasta".
	*/
	public int poistaToimipiste (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
		// parsitaan DELETE
		String sql = "DELETE FROM  Toimipiste WHERE toimipiste_id = ?";
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot DELETEn WHERE-ehtoon
			lause.setInt( 1, getToimipisteId());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("toimipiste poistaminen ei onnistu");
			}
			} catch (SQLException se) {
            // SQL virheet
                throw se;
             } catch (Exception e) {
            // JDBC virheet
                throw e;
		}
		return 0; // toiminto ok
	}
}