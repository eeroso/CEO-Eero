/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MonkkonenE
 */
import java.sql.*;
import java.lang.*;
public class Varaus {
	
	// attribuutit, vastaavat tietokantataulun sarakkeita
    private int m_varaus_id;
	private int m_asiakas_id;
	private int m_toimipiste_id;
	private String m_varattu_pvm;
	private String m_vahvistus_pvm;
	private String m_varattu_alkupvm;
	private String m_varattu_loppupvm;

    public Varaus(){
 
    }
	// getterit ja setterit
	public int getVarausId()
	{
		return m_varaus_id;
	}
	public int getAsiakasId() {
		return m_asiakas_id;
	}
	public int getToimipisteId() {
		return m_toimipiste_id;
	}
	public String getVarattu_pvm() {
		return m_varattu_pvm;
	}
	public String getVahvistus_pvm() {
		return m_vahvistus_pvm;
	}
	public String getVarattu_alkupvm() {
		return m_varattu_alkupvm;
	}
	public String getVarattu_loppupvm() {
		return m_varattu_loppupvm;
	}


	public void setVarausId (int id)
	{
		m_varaus_id = id;
	}
	public void setAsiakasId (int asid) {
		m_asiakas_id = asid;
	}
	public void setToimipisteId (int toim) {
		m_toimipiste_id = toim;
	}
	public void setVarattu_pvm (String varat) {
		m_varattu_pvm = varat;
	}
	public void setVahvistus_pvm (String vahv) {
		m_vahvistus_pvm = vahv;
	}
	public void setVarattu_alkupvm (String alkpvm) {
		m_varattu_alkupvm = alkpvm;
	}
	public void setVarattu_loppupvm (String loppvm) {
		m_varattu_loppupvm = loppvm;
	}


    @Override
    public String toString(){
        return (m_varaus_id + " " + m_asiakas_id + " " + m_toimipiste_id);
    }
	/*
	Haetaan asiakkaan tiedot ja palautetaan asiakasolio kutsujalle.
	Staattinen metodi, ei vaadi fyysisen olion olemassaoloa.
	*/
	public static Varaus haeVaraus (Connection connection, int id) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = id 
		String sql = "SELECT varaus_id, asiakas_id, toimipiste_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm" 
					+ " FROM varaus WHERE varaus_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, id); // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko == null) {
				throw new Exception("Varausta ei loydy");
			}
		} catch (SQLException se) {
            // SQL virheet
                        throw se;
                } catch (Exception e) {
            // JDBC virheet
                        throw e;
		}
		// käsitellään resultset - laitetaan tiedot varausoliolle
		Varaus varausOlio = new Varaus ();
		
		try {
			if (tulosjoukko.next () == true){
				//varaus_id, asiakas_id, toimipiste_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm
				varausOlio.setVarausId (tulosjoukko.getInt("varaus_id"));
				varausOlio.setAsiakasId (tulosjoukko.getInt("asiakas_id"));
				varausOlio.setToimipisteId(tulosjoukko.getInt("toimipiste_id"));
				varausOlio.setVarattu_pvm (tulosjoukko.getString("varattu_pvm"));
				varausOlio.setVahvistus_pvm (tulosjoukko.getString("vahvistus_pvm"));
				varausOlio.setVarattu_alkupvm (tulosjoukko.getString("varattu_alkupvm"));
				varausOlio.setVarattu_loppupvm (tulosjoukko.getString("varattu_loppupvm"));

			}
			
		}catch (SQLException e) {
			throw e;
		}
		// palautetaan asiakasolio
		
		return varausOlio;
	}
	/*
	Lisätään varauksen tiedot tietokantaan.
	Metodissa "varausolio kirjoittaa tietonsa tietokantaan".
	*/
     public int lisaaVaraus (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id -> ei voi lisätä, jos on jo kannassa
		String sql = "SELECT varaus_id" 
					+ " FROM varaus WHERE varaus_id = ? AND asiakas_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null; 
		
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getVarausId()); // asetetaan where ehtoon (?) arvo, olion asiakasid
			lause.setInt( 2, getAsiakasId());

			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == true) { // asiakas loytyi
				throw new Exception("Varaus on jo olemassa");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan INSERT
		sql = "INSERT INTO Varaus "
		+ "(varaus_id, asiakas_id, toimipiste_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) "
		+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		// System.out.println("Lisataan " + sql);
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot INSERTtiin
			lause.setInt( 1, getVarausId());
			lause.setInt(2, getAsiakasId()); 
			lause.setInt(3, getToimipisteId()); 
			lause.setString(4, getVarattu_pvm());
			lause.setString(5, getVahvistus_pvm ());
			lause.setString(6, getVarattu_alkupvm ());
			lause.setString(7, getVarattu_loppupvm ());
		
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
		//	System.out.println("lkm " + lkm);
			if (lkm == 0) {
				throw new Exception("Varauksen lisaaminen ei onnistu");
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
    public int muutaVaraus (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id, virhe, jos ei löydy
		String sql = "SELECT varaus_id" 
					+ " FROM varaus WHERE varaus_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getVarausId()); // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == false) { // asiakasta ei löytynyt
				throw new Exception("Varausta ei loydy tietokannasta");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan Update, päiviteään tiedot lukuunottamatta avainta
		sql = "UPDATE  Varaus "
		+ "SET asiakas_id = ?, toimipiste_id = ?, varattu_pvm = ?, vahvistus_pvm = ?, varattu_alkupvm = ?, varattu_loppupvm = ? "
		+ " WHERE varaus_id = ?";
		
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			
			// laitetaan olion attribuuttien arvot UPDATEen
			
			lause.setInt(1, getAsiakasId()); 
			lause.setInt(2, getToimipisteId()); 
			lause.setString(3, getVarattu_pvm());
			lause.setString(4, getVahvistus_pvm ());
			lause.setString(5, getVarattu_alkupvm ());
			lause.setString(6, getVarattu_loppupvm ());

			// where-ehdon arvo
            lause.setInt(7, getVarausId());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Varauksen muuttaminen ei onnistu");
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
	public int poistaVaraus (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
		// parsitaan DELETE
		String sql = "DELETE FROM  Varaus WHERE varaus_id = ?";
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot DELETEn WHERE-ehtoon
			lause.setInt( 1, getVarausId());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Varauksen poistaminen ei onnistu");
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
