import java.sql.*;
import java.lang.*;
/**
 * @author Eero Sormunen
 * @version 2.0
 * @since 13.5.2019
 **/
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
//tietojen haku funktio
	public static Toimipiste haeToimipiste(Connection connection, int id) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta toimipistettä
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
				throw new Exception("Toimipistetta ei loydy");
			}
		} catch (SQLException se) {
            // SQL virheet
                        throw se;
                } catch (Exception e) {
            // JDBC virheet
                        throw e;
		}
		// käsitellään resultset - laitetaan tiedot tpiste oliolle
		Toimipiste toimipisteOlio = new Toimipiste ();
		
		try {
			if (tulosjoukko.next () == true){
				
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
		// palautetaan toimipisteolio
		
		return toimipisteOlio;
	}
	//toimipisteen lisäysfunktio
     public int lisaaToimipiste (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
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
			if (tulosjoukko.next () == true) { // tpiste loytyi
				throw new Exception("Toimipiste on jo olemassa");
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
	
			if (lkm == 0) {
				throw new Exception("Toimipiste lisaaminen ei onnistu");
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
	//toimipisteen muuttamisfunktio
    public int muutaToimipiste (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
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
			if (tulosjoukko.next () == false) {
				throw new Exception("Toimipistetta ei loydy tietokannasta");
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
				throw new Exception("Toimipisteen muuttaminen ei onnistu");
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
	//poistofunktio
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
				throw new Exception("Toimipisteen poistaminen ei onnistu");
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
