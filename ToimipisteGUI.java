import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;


public class ToimipisteGUI extends JFrame {
//
    private Toimipiste m_toimipiste = new Toimipiste (); 
	private Connection m_conn;
// käyttöliittymän otsikkokentät
	private JLabel lblToimipisteID;
	private JLabel lblNimi;
	private JLabel lblLahiosoite;
	private JLabel lblPostinro;
    private JLabel lblPostitoimipaikka;
	private JLabel lblEmail;
    private JLabel lblPuhelinnro;
//käyttöliittymän tekstikentät
	private JTextField txtToimipisteID;
	private JTextField txtNimi;
	private JTextField txtLahiosoite;
	private JTextField txtPostinro;
    private JTextField txtPostitoimipaikka;
	private JTextField txtEmail;
    private JTextField txtPuhelinnro;
// käyttöliittymän painikkeet	
    private JButton btnLisaa;
    private JButton btnMuuta;
	private JButton btnHae;
	private JButton btnPoista;
	private JButton btnPaluu;
	
    public ToimipisteGUI() {

		lblToimipisteID = new JLabel("Toimipiste id");
		lblNimi = new JLabel("Nimi");
		lblLahiosoite = new JLabel("Lahiosoite");
		lblPostinro = new JLabel("Postinro");
		lblPostitoimipaikka = new JLabel("Postitoimipaikka");
		lblEmail = new JLabel("Email");
		lblPuhelinnro = new JLabel("Puhelin");
		
		txtToimipisteID = new JTextField (6);
		txtNimi = new JTextField (12);
		txtLahiosoite = new JTextField (36);
		txtPostinro = new JTextField (5);
		txtPostitoimipaikka = new JTextField (26);
		txtEmail = new JTextField (36);
		txtPuhelinnro = new JTextField (26);

        btnHae = new JButton("Hae");
		btnMuuta = new JButton("Muuta");
		btnLisaa = new JButton("Lisaa");
		btnPoista = new JButton("Poista");
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
		// näytön paneli, jossa vasen ja oikea puoli
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 2));
		// vasemman reunan paneli, jossa kenttien otsikot ja tietokentät
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(8, 2));
		
        p2.add(lblToimipisteID);
        p2.add(txtToimipisteID);

        p2.add(lblNimi);
		p2.add(txtNimi);
			
		p2.add(lblLahiosoite);
		p2.add(txtLahiosoite);
		
		p2.add(lblPostinro);
		p2.add(txtPostinro);
		
		p2.add(lblPostitoimipaikka);
		p2.add(txtPostitoimipaikka);
		
		p2.add(lblEmail);
		p2.add(txtEmail);
		
		p2.add(lblPuhelinnro);
		p2.add(txtPuhelinnro);
// oikean reunan paneli, jossa painikkeet
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(5, 1));
		p3.add(btnHae);
		p3.add(btnMuuta);
		p3.add(btnLisaa);
		p3.add(btnPoista); 
		p3.add(btnPaluu); 

        p1.add(p2);
        
        p1.add(p3);
		
        add(p1);
		
		setLocation(100, 100); // Ikkunan paikka 
		setSize(800, 400);     // Ikkunan koko leveys, korkeus
		setTitle("Toimipiste");  // yläpalkkiin otsikko
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // osaa loppua
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
    ToimipisteGUI frmToimipiste = new ToimipisteGUI();  // luodaan lomakeluokan olio
	  
	
	}
	/*
	Avataan tietokantayhteys
	*/
	public  void yhdista() throws SQLException, Exception {
		m_conn = null;
		String url = "jdbc:mariadb://localhost:3306/villagepeople"; // palvelin = localhost, :portti annettu asennettaessa, tietokannan nimi
		try {
			// ota yhteys kantaan, kayttaja = root, salasana = root
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
	
	public  void hae_tiedot() {
		
		m_toimipiste = null;
		
		try {
			m_toimipiste = Toimipiste.haeToimipiste (m_conn, Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Toimipistetta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Toimipistetta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_toimipiste.getNimi() == null) {
		// muut virheet
			txtNimi.setText("");
			txtLahiosoite.setText("");
			txtPostinro.setText("");
			txtPostitoimipaikka.setText("");
			txtEmail.setText("");
			txtPuhelinnro.setText("");
			JOptionPane.showMessageDialog(null, "Toimipistetta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// naytetaan tiedot
			txtNimi.setText(m_toimipiste.getNimi());
			txtLahiosoite.setText(m_toimipiste.getLahiosoite());
			txtPostinro.setText(m_toimipiste.getPostinro());
			txtPostitoimipaikka.setText(m_toimipiste.getPostitoimipaikka());
			txtEmail.setText(m_toimipiste.getEmail());
			txtPuhelinnro.setText(m_toimipiste.getPuhelinnro());
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot asiakasoliolle ja kirjoitetaan ne tietokantaan
	*/
	public  void lisaa_tiedot() {
		// lisätään tietokantaan asiakas
		//System.out.println("Lisataan...");
		boolean toimipiste_lisatty = true;
		m_toimipiste = null;
		try {
			m_toimipiste = Toimipiste.haeToimipiste (m_conn, Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
        toimipiste_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
        toimipiste_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_toimipiste.getNimi() != null) {
		// asiakas jo olemassa, näytetään tiedot
        toimipiste_lisatty = false;
			txtNimi.setText(m_toimipiste.getNimi());
			txtLahiosoite.setText(m_toimipiste.getLahiosoite());
			txtPostinro.setText(m_toimipiste.getPostinro());
			txtPostitoimipaikka.setText(m_toimipiste.getPostitoimipaikka());
			txtEmail.setText(m_toimipiste.getEmail());
			txtPuhelinnro.setText(m_toimipiste.getPuhelinnro());
			JOptionPane.showMessageDialog(null, "Toimipiste on jo olemassa.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// asetetaan tiedot oliolle
			m_toimipiste.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
			m_toimipiste.setNimi(txtNimi.getText());
			m_toimipiste.setLahiosoite(txtLahiosoite.getText());
			m_toimipiste.setPostinro(txtPostinro.getText());
			m_toimipiste.setPostitoimipaikka(txtPostitoimipaikka.getText());
			m_toimipiste.setEmail(txtEmail.getText());
			m_toimipiste.setPuhelinnro(txtPuhelinnro.getText());
			try {
				// yritetään kirjoittaa kantaan
				m_toimipiste.lisaaToimipiste (m_conn);
			} catch (SQLException se) {
			// SQL virheet
            toimipiste_lisatty = false;
				JOptionPane.showMessageDialog(null, "toimipiste lisaaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			//	 se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
            toimipiste_lisatty = false;
				JOptionPane.showMessageDialog(null, "toimipiste lisaaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
			//	 e.printStackTrace();
			}finally {
				if (toimipiste_lisatty == true)
					JOptionPane.showMessageDialog(null, "toimipistetiedot lisatty tietokantaan.");
			}
		
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot Toimipisteoliolle ja muutetaan ne tietokantaan
	*/
	public  void muuta_tiedot() {
		//System.out.println("Muutetaan...");
			boolean toimipiste_muutettu = true;
		// asetetaan tiedot oliolle
			m_toimipiste.setNimi(txtNimi.getText());
			m_toimipiste.setLahiosoite(txtLahiosoite.getText());
			m_toimipiste.setPostinro(txtPostinro.getText());
			m_toimipiste.setPostitoimipaikka(txtPostitoimipaikka.getText());
			m_toimipiste.setEmail(txtEmail.getText());
			m_toimipiste.setPuhelinnro(txtPuhelinnro.getText());
			
			try {
				// yritetään muuttaa (UPDATE) tiedot kantaan
				m_toimipiste.muutaToimipiste (m_conn);
			} catch (SQLException se) {
			// SQL virheet
            toimipiste_muutettu = false;
				JOptionPane.showMessageDialog(null, "Asiakkaan tietojen muuttaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				 //se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				toimipiste_muutettu = false;
				JOptionPane.showMessageDialog(null, "Asiakkaan tietojen muuttaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (toimipiste_muutettu == true)
					JOptionPane.showMessageDialog(null, "Asiakkaan tiedot muutettu.");
			}
		
	}
	public  void poista_tiedot() {
		// haetaan tietokannasta toimipisteta, jonka toimipiste_id = txttoimipisteID 
		m_toimipiste = null;
		boolean toimipiste_poistettu = false;
		
		try {
			m_toimipiste = Toimipiste.haeToimipiste (m_conn, Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Asiakasta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Asiakasta ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_toimipiste.getNimi() == null) {
		// poistettavaa asiakasta ei löydy tietokannasta, tyhjennetään tiedot näytöltä
			txtNimi.setText("");
			txtLahiosoite.setText("");
			txtPostinro.setText("");
			txtPostitoimipaikka.setText("");
			txtEmail.setText("");
			txtPuhelinnro.setText("");
			JOptionPane.showMessageDialog(null, "Asiakasta ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
			return; // poistutaan
		}
		else
		{
			// naytetaan poistettavan asiakkaan tiedot
			txtNimi.setText(m_toimipiste.getNimi());
			txtLahiosoite.setText(m_toimipiste.getLahiosoite());
			txtPostinro.setText(m_toimipiste.getPostinro());
			txtPostitoimipaikka.setText(m_toimipiste.getPostitoimipaikka());
			txtEmail.setText(m_toimipiste.getEmail());
			txtPuhelinnro.setText(m_toimipiste.getPuhelinnro());
		}
		try {
			if (JOptionPane.showConfirmDialog(null, "Haluatko todella poistaa toimipiste?")==0) {// vahvistus ikkunassa
				m_toimipiste.poistaToimipiste (m_conn);
				toimipiste_poistettu = true;
			}
			} catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Asiakkaan tietojen poistaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				// se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Asiakkaan tietojen poistaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (toimipiste_poistettu == true) { // ainoastaan, jos vahvistettiin ja poisto onnistui
					txtToimipisteID.setText("");
					txtNimi.setText("");
					txtLahiosoite.setText("");
					txtPostinro.setText("");
					txtPostitoimipaikka.setText("");
					txtEmail.setText("");
					txtPuhelinnro.setText("");
					JOptionPane.showMessageDialog(null, "Asiakkaan tiedot poistettu tietokannasta.");
					m_toimipiste = null;
				}
			}
			
		
	}

}
