import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;


public class VarausGUI extends JFrame {
//
    private Varaus m_varaus = new Varaus (); // varausolio, jota tässä pääsääntöisesti käsitellään
	private Connection m_conn; // tietokantayhteys
// käyttöliittymän otsikkokentät
	private JLabel lblVarausID;
	private JLabel lblAsiakasID;
    private JLabel lblToimipisteID;
	private JLabel lblVarattu_pvm;
	private JLabel lblVahvistus_pvm;
    private JLabel lblVarattu_alkupvm;
	private JLabel lblVarattu_loppupvm;
	private JLabel lblPvm_alku;
	private JLabel lblPvm_loppu;

//käyttöliittymän tekstikentät
	private JTextField txtVarausID;
	private JTextField txtAsiakasID;
    private JTextField txtToimipisteID;
	private JTextField txtVarattu_pvm;
	private JTextField txtVahvistus_pvm;
    private JTextField txtVarattu_alkupvm;
	private JTextField txtVarattu_loppupvm;
	private JTextField txtPvm_alku;
	private JTextField txtPvm_loppu;
 
// käyttöliittymän painikkeet	
    private JButton btnLisaa;
    private JButton btnMuuta;
	private JButton btnHae;
	private JButton btnPoista;
	private JButton btnHaeraportti;
	private JButton btnPaluu;
	
    public VarausGUI() {

		lblVarausID = new JLabel("Varaus id");
		lblAsiakasID = new JLabel("Asiakas id");
		lblToimipisteID = new JLabel("Toimipiste id");
		lblVarattu_pvm = new JLabel("Varattu_pvm");
		lblVahvistus_pvm = new JLabel("Vahvistus_pvm");
		lblVarattu_alkupvm = new JLabel("Varattu_alkupvm");
		lblVarattu_loppupvm = new JLabel("Varattu_loppupvm");
		lblPvm_alku = new JLabel("Hae valilta, alku pvm");
		lblPvm_loppu = new JLabel("Hae valilta, loppu pvm");

		
		txtVarausID = new JTextField (6);
		txtAsiakasID = new JTextField (12);
		txtToimipisteID = new JTextField (26);
		txtVarattu_pvm = new JTextField (36);
		txtVahvistus_pvm = new JTextField (5);
		txtVarattu_alkupvm = new JTextField (26);
		txtVarattu_loppupvm = new JTextField (36);
		txtPvm_alku = new JTextField (26);
		txtPvm_loppu = new JTextField (36);


        btnHae = new JButton("Hae");
		btnMuuta = new JButton("Muuta");
		btnLisaa = new JButton("Lisaa");
		btnPoista = new JButton("Poista");
		btnHaeraportti = new JButton("Hae valilta");
		btnPaluu = new JButton("Lopeta");

		// lisätään hakupainikkeelle tapahtumakuuntelija
		btnHae.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
						hae_tiedot ();
					
				}
			}
		);
		// lisätään lisayspainikkeelle tapahtumakuuntelija
		btnLisaa.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
						lisaa_tiedot ();
					
				}
			}
		);
		// lisätään muuta-painikkeelle tapahtumakuuntelija
		btnMuuta.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
						muuta_tiedot ();
					
				}
			}
		);
		// lisätään muuta-painikkeelle tapahtumakuuntelija
		btnPoista.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
						poista_tiedot ();
					
				}
			}
		);
		// lisätään lopetuspainikkeelle tapahtumakuuntelija
		btnPaluu.addActionListener(   
			new ActionListener () {
				public void actionPerformed(ActionEvent actEvent) {
					try {
						sulje_kanta ();
					} catch (SQLException se) {
					// SQL virheet
						JOptionPane.showMessageDialog(null, "Tapahtui tietokantavirhe tietokantaa suljettaessa.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
					} catch (Exception e) {
					// muut virheet
						JOptionPane.showMessageDialog(null, "Tapahtui virhe tietokantaa suljettaessa.", "Virhe", JOptionPane.ERROR_MESSAGE);
					} finally {
						System.exit(0);
					}
						
        }
      }
	);
	btnHaeraportti.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
						hae_raportti ();
						JOptionPane.showMessageDialog(null, "Raportti kirjoitettu haetuilta aikavaleilta");
					
				}
			}
		);
		// näytön paneli, jossa vasen ja oikea puoli
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 2));
		// vasemman reunan paneli, jossa kenttien otsikot ja tietokentät
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(9, 2));
		
        p2.add(lblVarausID);
        p2.add(txtVarausID);

        p2.add(lblAsiakasID);
		p2.add(txtAsiakasID);
		
		p2.add(lblToimipisteID);
		p2.add(txtToimipisteID);
		
		p2.add(lblVarattu_pvm);
		p2.add(txtVarattu_pvm);
		
		p2.add(lblVahvistus_pvm);
		p2.add(txtVahvistus_pvm);
		
		p2.add(lblVarattu_alkupvm);
		p2.add(txtVarattu_alkupvm);
		
		p2.add(lblVarattu_loppupvm);
		p2.add(txtVarattu_loppupvm);

		p2.add(lblPvm_alku);
		p2.add(txtPvm_alku);

		p2.add(lblPvm_loppu);
		p2.add(txtPvm_loppu);
		


// oikean reunan paneli, jossa painikkeet
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(5, 1));
		p3.add(btnHae);
		p3.add(btnMuuta);
		p3.add(btnLisaa);
		p3.add(btnPoista); 
		p3.add(btnHaeraportti);
		p3.add(btnPaluu); 

        p1.add(p2);
        
        p1.add(p3);
		
        add(p1);
		
		setLocation(100, 100); // Ikkunan paikka 
		setSize(800, 400);     // Ikkunan koko leveys, korkeus
		setTitle("Varaus");  // yläpalkkiin otsikko
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //ei sammuta master guita
		setVisible(true); // lomake näkyviin
	
		// avataan tietokanta
		try {
			yhdista ();
		 } catch (SQLException se) {
            // SQL virheet
			JOptionPane.showMessageDialog(null, "Tapahtui virhe tietokantaa avattaessa.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // JDBC virheet
            JOptionPane.showMessageDialog(null, "Tapahtui JDBCvirhe tietokantaa avattaessa.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		
    }
	
	public static void main(String args[]) {
    VarausGUI frVaraus = new VarausGUI();  // luodaan lomakeluokan olio
	  
	
	}
	/*
	Avataan tietokantayhteys
	*/
	public  void yhdista() throws SQLException, Exception {
		m_conn = null;
		String url = "jdbc:mariadb://localhost:3306/villagepeople"; // palvelin = localhost, :portti annettu asennettaessa, tietokannan nimi
		try {
			m_conn=DriverManager.getConnection(url,"root","qjwax2ic");
		}
		catch (SQLException e) { // tietokantaan ei saada yhteyttä
			m_conn = null;
			throw e;
		}
		catch (Exception e ) { // JDBC ajuria ei löydy
			throw e;
		}
		
	}
	/*
	Suljetaan tietokantayhteys
	*/
	public  void sulje_kanta() throws SQLException, Exception {
		// suljetaan		
		try {
			// sulje yhteys kantaan
			m_conn.close ();
		}
		catch (SQLException e) { // tietokantavirhe
			throw e;
		}
		catch (Exception e ) { // muu virhe tapahtui
			throw e;
		}
		
	}
	/*
	Haetaan tietokannasta varauksen tiedot näytöllä olebvan varausid:n perusteella ja näytetään tiedot lomakkeella
	*/
	public  void hae_tiedot() {
		// haetaan tietokannasta varausta, jonka varaus_id = txtvarausID 
		m_varaus = null;
		
		try {
			m_varaus = Varaus.haeVaraus (m_conn, Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_varaus.getVarattu_alkupvm() == null) {
		// muut virheet
			txtAsiakasID.setText("");
			txtToimipisteID.setText("");
			txtVarattu_pvm.setText("");
			txtVahvistus_pvm.setText("");
			txtVarattu_alkupvm.setText("");
			txtVarattu_loppupvm.setText("");
			
			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// naytetaan tiedot
			txtAsiakasID.setText(String.valueOf(m_varaus.getAsiakasId()));
			txtToimipisteID.setText(String.valueOf(m_varaus.getToimipisteId()));
			txtVarattu_pvm.setText(m_varaus.getVarattu_pvm());
			txtVahvistus_pvm.setText(m_varaus.getVahvistus_pvm());
			txtVarattu_alkupvm.setText(m_varaus.getVarattu_alkupvm());
			txtVarattu_loppupvm.setText(m_varaus.getVarattu_loppupvm());
			
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot varausoliolle ja kirjoitetaan ne tietokantaan 
	*/
	public  void lisaa_tiedot() {
		// lisätään tietokantaan varaus
		boolean varaus_lisatty = true;
		m_varaus = null;
		try {
			m_varaus = Varaus.haeVaraus (m_conn, Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			varaus_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			varaus_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
			// asetetaan tiedot oliolle
			m_varaus.setVarausId(Integer.parseInt(txtVarausID.getText()));
			m_varaus.setAsiakasId(Integer.parseInt(txtAsiakasID.getText()));
			m_varaus.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
			m_varaus.setVarattu_pvm(txtVarattu_pvm.getText());
			m_varaus.setVahvistus_pvm(txtVahvistus_pvm.getText());
			m_varaus.setVarattu_alkupvm(txtVarattu_alkupvm.getText());
			m_varaus.setVarattu_loppupvm(txtVarattu_loppupvm.getText());

			try {
				// yritetään kirjoittaa kantaan
				m_varaus.lisaaVaraus (m_conn);
			} catch (SQLException se) {
			// SQL virheet
				varaus_lisatty = false;
				JOptionPane.showMessageDialog(null, "Varauksen lisaaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
			// muut virheet
				varaus_lisatty = false;
				JOptionPane.showMessageDialog(null, "Varauksen lisaaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
			}finally {
				if (varaus_lisatty == true)
					JOptionPane.showMessageDialog(null, "Varauksen tiedot lisatty tietokantaan.");
			}
		
	
		
	}
	/*
	Viedään näytöllä olevat tiedot varausoliolle ja muutetaan ne tietokantaan
	*/
	public  void muuta_tiedot() {
			boolean varaus_muutettu = true;
		// asetetaan tiedot oliolle
				m_varaus.setAsiakasId(Integer.parseInt(txtAsiakasID.getText()));
				m_varaus.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
				m_varaus.setVarattu_pvm(txtVarattu_pvm.getText());
				m_varaus.setVahvistus_pvm(txtVahvistus_pvm.getText());
				m_varaus.setVarattu_alkupvm(txtVarattu_alkupvm.getText());
				m_varaus.setVarattu_loppupvm(txtVarattu_loppupvm.getText());
			
			try {
				// yritetään muuttaa (UPDATE) tiedot kantaan
				m_varaus.muutaVaraus (m_conn);
			} catch (SQLException se) {
			// SQL virheet
				varaus_muutettu = false;
				JOptionPane.showMessageDialog(null, "Varauksen tietojen muuttaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);

			} catch (Exception e) {
			// muut virheet
				varaus_muutettu = false;
				JOptionPane.showMessageDialog(null, "Varauksen tietojen muuttaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);

			} finally {
				if (varaus_muutettu == true)
					JOptionPane.showMessageDialog(null, "Varauksen tiedot muutettu.");
			}
		
	}
	public  void hae_raportti() { //tietokannasta haku halutulla aikavälillä
	
		m_varaus.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
		m_varaus.setPvmalku(txtPvm_alku.getText());
		m_varaus.setPvmloppu(txtPvm_loppu.getText());
		
			try {
				m_varaus.haeRaportti (m_conn);
			} catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Hakuehdoilla ei loytynyt varauksia.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Haku ei onnistunut", "Virhe", JOptionPane.ERROR_MESSAGE);
			}
		
	}
	public  void poista_tiedot() {
		m_varaus = null;
		boolean varaus_poistettu = false;
		
		try {
			m_varaus = Varaus.haeVaraus (m_conn, Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (String.valueOf(m_varaus.getVarausId()) == null) {
		// poistettavaa varausta ei löydy tietokannasta, tyhjennetään tiedot näytöltä
			txtAsiakasID.setText("");
			txtToimipisteID.setText("");
			txtVarattu_pvm.setText("");
			txtVahvistus_pvm.setText("");
			txtVarattu_alkupvm.setText("");
			txtVarattu_loppupvm.setText("");

			JOptionPane.showMessageDialog(null, "Varausta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
			return; // poistutaan
		}
		else
		{
			// naytetaan poistettavan varauksen tiedot
			txtAsiakasID.setText(String.valueOf(m_varaus.getAsiakasId()));
			txtToimipisteID.setText(String.valueOf(m_varaus.getToimipisteId()));
			txtVarattu_pvm.setText(m_varaus.getVarattu_pvm());
			txtVahvistus_pvm.setText(m_varaus.getVahvistus_pvm());
			txtVarattu_alkupvm.setText(m_varaus.getVarattu_alkupvm());
			txtVarattu_loppupvm.setText(m_varaus.getVarattu_loppupvm());
		}
		try {
			if (JOptionPane.showConfirmDialog(null, "Haluatko todella poistaa varauksen?")==0) {// vahvistus ikkunassa
				m_varaus.poistaVaraus (m_conn);
				varaus_poistettu = true;
			}
			} catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (varaus_poistettu == true) { // ainoastaan, jos vahvistettiin ja poisto onnistui
					txtVarausID.setText("");
					txtAsiakasID.setText("");
					txtToimipisteID.setText("");
					txtVarattu_pvm.setText("");
					txtVahvistus_pvm.setText("");
					txtVarattu_alkupvm.setText("");
					txtVarattu_loppupvm.setText("");

					JOptionPane.showMessageDialog(null, "Varauksen tiedot poistettu tietokannasta.");
					m_varaus = null;
				}
			}
			
		
	}

}
