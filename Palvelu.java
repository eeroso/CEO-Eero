import java.sql.*;
import java.lang.*;
/**
 * @author Eero Sormunen
 * @version 2.0
 * @since 13.5.2019
 **/ 
public class Palvelu {
	
    // attribuutit, vastaavat tietokantataulun sarakkeita
    private int m_palvelu_id;
    private int m_toimipiste_id;
	private String m_nimi;
	private int m_tyyppi;
	private String m_kuvaus;
	private Double m_hinta;
	private Double m_alv;

    public Palvelu(){
 
    }
	// getterit ja setterit
	public int getPalveluId()
	{
        return m_palvelu_id;
    }
    public int getToimipisteId() {
		return m_toimipiste_id;
	}
	public String getNimi() {
		return m_nimi;
	}
	public int getTyyppi() {
		return m_tyyppi;
	}
	public String getKuvaus() {
		return m_kuvaus;
	}
	public Double getHinta() {
		return m_hinta;
	}
	public Double getAlv() {
		return m_alv;
    }
    public void setPalveluId(int pid)
	{
        m_palvelu_id = pid;
    }
	public void setToimipisteId (int tid)
	{
		m_toimipiste_id = tid;
	}
	public void setNimi (String ni) {
		m_nimi = ni;
	}
	public void setTyyppi (int ty) {
		m_tyyppi = ty;
	}
	public void setKuvaus (String kuv) {
		m_kuvaus = kuv;
    }
	public void setHinta (Double hi) {
		m_hinta = hi;
	}
	public void setAlv (Double al) {
		m_alv = al;
	}
	
    @Override
    public String toString(){
        return (m_palvelu_id + " " + m_toimipiste_id);
    }
	//haetaan palvelun tiedot
	public static Palvelu haePalvelu(Connection connection, int pid, int tid) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta palvelua palvelu_id:n ja viiteavaimen perusteella
		String sql = "SELECT palvelu_id, toimipiste_id, nimi, tyyppi, kuvaus, hinta, alv " 
					+ " FROM Palvelu WHERE palvelu_id = ? AND toimipiste_id = ?"; // ehdon arvot asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, pid);
			lause.setInt( 2, tid); // asetetaan where ehtoon (?) arvot
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko == null) {
				throw new Exception("Palvelua ei loydy");
			}
		} catch (SQLException se) {
            // SQL virheet
                        throw se;
                } catch (Exception e) {
            // JDBC virheet
                        throw e;
		}
		// käsitellään resultset
		Palvelu palveluOlio = new Palvelu ();
		
		try {
			if (tulosjoukko.next () == true){
                palveluOlio.setPalveluId(tulosjoukko.getInt("palvelu_id"));
                palveluOlio.setToimipisteId (tulosjoukko.getInt("toimipiste_id"));
				palveluOlio.setNimi(tulosjoukko.getString("nimi"));
				palveluOlio.setTyyppi (tulosjoukko.getInt("tyyppi"));
				palveluOlio.setKuvaus (tulosjoukko.getString("kuvaus"));
				palveluOlio.setHinta (tulosjoukko.getDouble("hinta"));
				palveluOlio.setAlv (tulosjoukko.getDouble("alv"));
			}
			
		}catch (SQLException e) {
			throw e;
		}
		// palautetaan palveluolio
		
		return palveluOlio;
	}
	//Lisätään palvelu
     public int lisaaPalvelu (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		String sql = "SELECT palvelu_id" 
					+ " FROM Palvelu WHERE palvelu_id = ? AND toimipiste_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null; 
		
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt(1, getPalveluId());
			lause.setInt(2, getToimipisteId()); // asetetaan where ehtoon (?) arvot
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == true) { // asiakas loytyi
				throw new Exception("Palvelu on jo olemassa");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan INSERT
		sql = "INSERT INTO Palvelu "
		+ "(palvelu_id, toimipiste_id, nimi, tyyppi, kuvaus, hinta, alv) "
		+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
            // laitetaan arvot INSERTtiin
            lause.setInt( 1, getPalveluId());
			lause.setInt(2, getToimipisteId());
			lause.setString(3, getNimi()); 
			lause.setInt(4, getTyyppi());
			lause.setString(5, getKuvaus());
			lause.setDouble(6, getHinta());
			lause.setDouble(7, getAlv());
			
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Palvelun lisaaminen ei onnistu");
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
	//palvleuiden muuttaminen
    public int muutaPalvelu (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		String sql = "SELECT palvelu_id" 
					+ " FROM Palvelu WHERE palvelu_id = ? AND toimipiste_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getPalveluId()); // asetetaan where ehtoon (?) arvot
			lause.setInt( 2, getToimipisteId());
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == false) { //palvelua ei löytynyt
				throw new Exception("Palvelua ei loydy tietokannasta");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan Update, päiviteään tiedot lukuunottamatta avaimia
		sql = "UPDATE Palvelu SET nimi = ?, tyyppi = ?, kuvaus = ?, hinta = ?, alv = ? WHERE palvelu_id = ? AND toimipiste_id = ?";
		
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			
			// laitetaan olion attribuuttien arvot UPDATEen
            

			lause.setString(1, getNimi()); 
			lause.setInt(2, getTyyppi());
			lause.setString(3, getKuvaus());
			lause.setDouble(4, getHinta());
			lause.setDouble(5, getAlv());
			// where-ehdon arvo
			lause.setInt(6, getPalveluId());
			lause.setInt(7, getToimipisteId());
			
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Palvelun muuttaminen ei onnistu");
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
	//palveluiden poistaminen
	public int poistaPalvelu (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
		// parsitaan DELETE
		String sql = "DELETE FROM Palvelu WHERE palvelu_id = ? AND toimipiste_id = ?";
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot DELETEn WHERE-ehtoon
			lause.setInt( 1, getPalveluId());
			lause.setInt( 2, getToimipisteId());
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
			if (lkm == 0) {
				throw new Exception("Palvelun poistaminen ei onnistu");
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
