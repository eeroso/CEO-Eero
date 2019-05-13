

/**
 *
 * @author Eero Sormunen
 * @version 2.0
 * @since 13.5.2019 
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.*;
import java.lang.*;
public class Lasku{
	
	// attribuutit, vastaavat tietokantataulun sarakkeita
	private int m_lasku_id;
	private int m_varaus_id;
	private int m_asiakas_id;
	private String m_nimi;
	private String m_lahiosoite;
	private String m_postitoimipaikka;
	private String m_postinro;
	private Double m_summa;
	private Double m_alv;

    public Lasku(){
 
    }
	// getterit ja setterit
	public int getLaskuId()
	{
		return m_lasku_id;
	}
	public int getVarausId() {
		return m_varaus_id;
	}
	public int getAsiakasId() {
		return m_asiakas_id;
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
	public Double getSumma() {
		return m_summa;
	}
	public Double getAlv() {
		return m_alv;
	}


	public void setLaskuId (int lid)
	{
		m_lasku_id = lid;
	}
	public void setVarausId (int vid) {
		m_varaus_id = vid;
	}
	public void setAsiakasId (int aid) {
		m_asiakas_id = aid;
	}
	public void setNimi (String ni) {
		m_nimi = ni;
	}
	public void setLahiosoite (String lah) {
		m_lahiosoite = lah;
	}
	public void setPostitoimipaikka (String pos) {
		m_postitoimipaikka = pos;
	}
	public void setPostinro (String pnro) {
		m_postinro = pnro;
	}
	public void setSumma (Double su) {
		m_summa = su;
	}
	public void setAlv (Double al) {
		m_alv = al;
	}


    @Override
    public String toString(){
        return (m_lasku_id + " " + m_asiakas_id + " " + m_nimi);
    }
	/*
	Haetaan asiakkaan tiedot ja palautetaan asiakasolio kutsujalle.
	Staattinen metodi, ei vaadi fyysisen olion olemassaoloa.
	*/
	public static Lasku haeLasku (Connection connection, int lid, int vid) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = id 
		String sql = "SELECT lasku_id, varaus_id, asiakas_id, nimi, lahiosoite, postitoimipaikka, postinro, summa, alv" 
					+ " FROM lasku WHERE lasku_id = ? AND varaus_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, lid);
			lause.setInt( 2, vid);
		 // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko == null) {
				throw new Exception("laskua ei loydy");
			}
		} catch (SQLException se) {
            // SQL virheet
                        throw se;
                } catch (Exception e) {
            // JDBC virheet
                        throw e;
		}
		// käsitellään resultset - laitetaan tiedot laskuoliolle
		Lasku laskuOlio = new Lasku ();
		
		try {
			if (tulosjoukko.next () == true){
				//lasku_id, asiakas_id, toimipiste_id, nimi, lahiosoite, postitoimipaikka, postinro
				laskuOlio.setLaskuId (tulosjoukko.getInt("lasku_id"));
				laskuOlio.setVarausId(tulosjoukko.getInt("varaus_id"));
				laskuOlio.setAsiakasId (tulosjoukko.getInt("asiakas_id"));
				laskuOlio.setNimi (tulosjoukko.getString("nimi"));
				laskuOlio.setLahiosoite (tulosjoukko.getString("lahiosoite"));
				laskuOlio.setPostitoimipaikka (tulosjoukko.getString("postitoimipaikka"));
				laskuOlio.setPostinro (tulosjoukko.getString("postinro"));
				laskuOlio.setSumma(tulosjoukko.getDouble("summa"));
				laskuOlio.setAlv(tulosjoukko.getDouble("alv"));


			}
			
		}catch (SQLException e) {
			throw e;
		}
		// palautetaan asiakasolio
		
		return laskuOlio;
	}
	/*
	Lisätään varauksen tiedot tietokantaan.
	Metodissa "laskuolio kirjoittaa tietonsa tietokantaan".
	*/
     public int lisaaLasku (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id -> ei voi lisätä, jos on jo kannassa
		String sql = "SELECT lasku_id" 
					+ " FROM lasku WHERE lasku_id = ? AND varaus_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null; 
		
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getLaskuId()); // asetetaan where ehtoon (?) arvo, olion asiakasid
			lause.setInt( 2, getVarausId());

			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == true) { // asiakas loytyi
				throw new Exception("lasku on jo olemassa");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan INSERT
		sql = "INSERT INTO lasku "
		+ "(lasku_id, varaus_id, asiakas_id, nimi, lahiosoite, postitoimipaikka, postinro, summa, alv) "
		+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		// System.out.println("Lisataan " + sql);
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot INSERTtiin
			lause.setInt(1, getLaskuId());
			lause.setInt(2, getVarausId()); 
			lause.setInt(3, getAsiakasId()); 
			lause.setString(4, getNimi());
			lause.setString(5, getLahiosoite ());
			lause.setString(6, getPostitoimipaikka ());
			lause.setString(7, getPostinro ());
			lause.setDouble(8, getSumma());
			lause.setDouble(9, getAlv());
		
			// suorita sql-lause
			int lkm = lause.executeUpdate();	
		//	System.out.println("lkm " + lkm);
			if (lkm == 0) {
				throw new Exception("Laskun lisaaminen ei onnistu");
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
    
    public void paperiLasku (Connection connection, int lid, int vid) throws SQLException, Exception { //paperilaskun kirjoitusfunktio
        String sql = "SELECT * FROM lasku WHERE lasku_id = ? AND varaus_id = ? "; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
        PreparedStatement lause = null; 
        FileWriter writer = new FileWriter("lasku.txt"); //tiedosto johon paperilasku tulee
		BufferedWriter data = new BufferedWriter(writer);
		
		try {

            lause = connection.prepareStatement(sql);
            lause.setInt( 1, lid);
            lause.setInt( 2, vid);  // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();
 
            while ( tulosjoukko.next() ) {
                int lasku = tulosjoukko.getInt("lasku_id"); //kannasta tiedot
                
                int varaus = tulosjoukko.getInt("varaus_id");
               
                int asiakas = tulosjoukko.getInt("asiakas_id");
               
                String nimi = tulosjoukko.getString("nimi");
              
                String lahios = tulosjoukko.getString("lahiosoite");
               
                String postitoim = tulosjoukko.getString("postitoimipaikka");
                
                String pnro = tulosjoukko.getString("postinro");
                
                Double summa = tulosjoukko.getDouble("summa");
                
                Double alv = tulosjoukko.getDouble("alv");
                
			
			data.write("Lasku ID: " + lasku); //kirjoitetaan juuri haetut tiedot lasku-tekstitiedostoon
			data.newLine();
			data.write("Varaus ID: " + varaus);
			data.newLine();
			data.write("Asiakas ID: " + asiakas);
			data.newLine();
			data.write("Nimi: " + nimi);
			data.newLine();
			data.write("Lähiosoite: " + lahios);
			data.newLine();
			data.write("Postitoimipaikka: " + postitoim);
			data.newLine();
			data.write("Postinumero: " + pnro);
			data.newLine();
			data.write("Summa: " + summa);
			data.newLine();
			data.write("Alv: " + alv);
			data.newLine();




            data.close();//suljetaan kirjoittaja
            }
            
        } catch (SQLException se) { //virheet
            System.err.println("Virhe");
        } catch (Exception e) {
			e.printStackTrace();
		}
}




    
	/*
	Muutetaan asiakkaan tiedot tietokantaan id-tietoa (avain) lukuunottamatta. 
	Metodissa "asiakasolio muuttaa tietonsa tietokantaan".
	*/
    public int muutaLasku (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		// haetaan tietokannasta asiakasta, jonka asiakas_id = olion id, virhe, jos ei löydy
		String sql = "SELECT lasku_id" 
					+ " FROM lasku WHERE lasku_id = ? AND varaus_id = ?"; // ehdon arvo asetetaan jäljempänä
		ResultSet tulosjoukko = null;
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			lause.setInt( 1, getLaskuId());
			lause.setInt( 2, getVarausId()); // asetetaan where ehtoon (?) arvo
			// suorita sql-lause
			tulosjoukko = lause.executeQuery();	
			if (tulosjoukko.next () == false) { // asiakasta ei löytynyt
				throw new Exception("laskua ei loydy tietokannasta");
			}
		} catch (SQLException se) {
            // SQL virheet
                    throw se;
                } catch (Exception e) {
            // JDBC virheet
                    throw e;
		}
		// parsitaan Update, päiviteään tiedot lukuunottamatta avainta
		sql = "UPDATE  lasku "
		+ "SET asiakas_id = ?, nimi = ?, lahiosoite = ?, postitoimipaikka = ?, postinro = ?, summa = ?, alv = ?"
		+ " WHERE lasku_id = ? AND varaus_id = ?";
		
		lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			
			// laitetaan olion attribuuttien arvot UPDATEen
			
			lause.setInt(1, getAsiakasId()); 
			lause.setString(2, getNimi());
			lause.setString(3, getLahiosoite ());
			lause.setString(4, getPostitoimipaikka ());
			lause.setString(5, getPostinro ());
			lause.setDouble(6, getSumma());
			lause.setDouble(7, getAlv());

			// where-ehdon arvo
			lause.setInt(8, getLaskuId());
			lause.setInt(9, getVarausId());
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
	public int poistaLasku (Connection connection) throws SQLException, Exception { // tietokantayhteys välitetään parametrina
		
		// parsitaan DELETE
		String sql = "DELETE FROM lasku WHERE lasku_id = ? AND varaus_id = ?";
		PreparedStatement lause = null;
		try {
			// luo PreparedStatement-olio sql-lauseelle
			lause = connection.prepareStatement(sql);
			// laitetaan arvot DELETEn WHERE-ehtoon
			lause.setInt( 1, getLaskuId());
			lause.setInt( 2, getVarausId());
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
