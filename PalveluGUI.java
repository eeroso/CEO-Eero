import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;


public class PalveluGUI extends JFrame {
//
    private Palvelu m_palvelu = new Palvelu (); 
	private Connection m_conn;
// käyttöliittymän otsikkokentät
	private JLabel lblPalveluID;
	private JLabel lblToimipisteID;
	private JLabel lblNimi;
    private JLabel lblTyyppi;
	private JLabel lblKuvaus;
    private JLabel lblHinta;
    private JLabel lblAlv;
//käyttöliittymän tekstikentät
    private JTextField txtPalveluID;
	private JTextField txtToimipisteID;
	private JTextField txtNimi;
	private JTextField txtTyyppi;
    private JTextField txtKuvaus;
	private JTextField txtHinta;
    private JTextField txtAlv;
// käyttöliittymän painikkeet	
    private JButton btnLisaa;
    private JButton btnMuuta;
	private JButton btnHae;
	private JButton btnPoista;
	private JButton btnPaluu;
	
    public PalveluGUI() {

        lblPalveluID = new JLabel("Palvelu ID");
		lblToimipisteID = new JLabel("Toimipiste id");
		lblNimi = new JLabel("Nimi");
		lblTyyppi = new JLabel("Tyyppi");
		lblKuvaus = new JLabel("Kuvaus");
		lblHinta = new JLabel("Hinta");
		lblAlv = new JLabel("Alv");
		
		txtPalveluID = new JTextField (6);
		txtToimipisteID = new JTextField (12);
		txtNimi = new JTextField (36);
		txtTyyppi = new JTextField (20);
		txtKuvaus = new JTextField (40);
		txtHinta = new JTextField (15);
		txtAlv = new JTextField (5);

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
        
        p2.add(lblPalveluID);
		p2.add(txtPalveluID);

        p2.add(lblToimipisteID);
        p2.add(txtToimipisteID);

        p2.add(lblNimi);
		p2.add(txtNimi);
			
		p2.add(lblTyyppi);
		p2.add(txtTyyppi);
		
		p2.add(lblKuvaus);
		p2.add(txtKuvaus);
		
		p2.add(lblHinta);
		p2.add(txtHinta);
		
		p2.add(lblAlv);
		p2.add(txtAlv);
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
		setTitle("Palvelut");  // yläpalkkiin otsikko
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // osaa loppua
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
    PalveluGUI frmPalvelu = new PalveluGUI();  // luodaan lomakeluokan olio
	  
	
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
		
		m_palvelu= null;
		
		try {
			m_palvelu = Palvelu.haePalvelu (m_conn, Integer.parseInt(txtPalveluID.getText()), Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_palvelu.getNimi() == null) {
		// muut virheet
			txtToimipisteID.setText("");
			txtNimi.setText("");
			txtTyyppi.setText("");
			txtKuvaus.setText("");
			txtHinta.setText("");
			txtAlv.setText("");
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// naytetaan tiedot
			txtToimipisteID.setText(String.valueOf(m_palvelu.getToimipisteId()));
			txtNimi.setText(m_palvelu.getNimi());
			txtTyyppi.setText(String.valueOf(m_palvelu.getTyyppi()));
			txtKuvaus.setText(m_palvelu.getKuvaus());
			txtHinta.setText(String.valueOf(m_palvelu.getHinta()));
			txtAlv.setText(String.valueOf(m_palvelu.getAlv()));
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot asiakasoliolle ja kirjoitetaan ne tietokantaan
	*/
	public  void lisaa_tiedot() {
		// lisätään tietokantaan asiakas
		//System.out.println("Lisataan...");
		boolean palvelu_lisatty = true;
		m_palvelu = null;
		try {
			m_palvelu  = Palvelu.haePalvelu (m_conn, Integer.parseInt(txtPalveluID.getText()), Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
        palvelu_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
        palvelu_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_palvelu.getNimi() != null) {
		// asiakas jo olemassa, näytetään tiedot
        palvelu_lisatty = false;
			txtToimipisteID.setText(String.valueOf(m_palvelu.getToimipisteId()));
			txtNimi.setText(m_palvelu.getNimi());
			txtTyyppi.setText(String.valueOf(m_palvelu.getTyyppi()));
			txtKuvaus.setText(m_palvelu.getKuvaus());
			txtHinta.setText(String.valueOf(m_palvelu.getHinta()));
            txtAlv.setText(String.valueOf(m_palvelu.getAlv()));
			JOptionPane.showMessageDialog(null, "Palvelu on jo olemassa.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// asetetaan tiedot oliolle
            m_palvelu.setPalveluId(Integer.parseInt(txtPalveluID.getText()));
            m_palvelu.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
			m_palvelu.setNimi(txtNimi.getText());
			m_palvelu.setTyyppi(Integer.parseInt(txtTyyppi.getText()));
			m_palvelu.setKuvaus(txtKuvaus.getText());
			m_palvelu.setHinta(Double.parseDouble(txtHinta.getText()));
			m_palvelu.setAlv(Double.parseDouble(txtAlv.getText()));

			try {
				// yritetään kirjoittaa kantaan
                m_palvelu.lisaaPalvelu (m_conn);
			} catch (SQLException se) {
			// SQL virheet
            palvelu_lisatty = false;
				JOptionPane.showMessageDialog(null, "Palvelun lisaaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			//	 se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
            palvelu_lisatty = false;
				JOptionPane.showMessageDialog(null, "Palvelun lisaaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
			//	 e.printStackTrace();
			}finally {
				if (palvelu_lisatty == true)
					JOptionPane.showMessageDialog(null, "Palvelu lisatty tietokantaan.");
			}
		
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot Toimipisteoliolle ja muutetaan ne tietokantaan
	*/
	public  void muuta_tiedot() {
		//System.out.println("Muutetaan...");
			boolean palvelu_muutettu = true;
		// asetetaan tiedot oliolle
			//m_palvelu.setPalveluId(Integer.parseInt(txtToimipisteID.getText()));
            m_palvelu.setToimipisteId(Integer.parseInt(txtToimipisteID.getText()));
			m_palvelu.setNimi(txtNimi.getText());
			m_palvelu.setTyyppi(Integer.parseInt(txtTyyppi.getText()));
			m_palvelu.setKuvaus(txtKuvaus.getText());
			m_palvelu.setHinta(m_palvelu.getHinta());
			m_palvelu.setAlv(m_palvelu.getAlv());
			
			try {
				// yritetään muuttaa (UPDATE) tiedot kantaan
				m_palvelu.muutaPalvelu (m_conn);
			} catch (SQLException se) {
			// SQL virheet
            palvelu_muutettu = false;
				JOptionPane.showMessageDialog(null, "Palvelun tietojen muuttaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				 //se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
            palvelu_muutettu = false;
				JOptionPane.showMessageDialog(null, "Palvelun tietojen muuttaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (palvelu_muutettu == true)
					JOptionPane.showMessageDialog(null, "Palvelun tiedot muutettu.");
			}
		
	}
	public  void poista_tiedot() {
		// haetaan tietokannasta toimipisteta, jonka toimipiste_id = txttoimipisteID 
		m_palvelu = null;
		boolean palvelu_poistettu = false;
		
		try {
			m_palvelu = Palvelu.haePalvelu (m_conn, Integer.parseInt(txtPalveluID.getText()), Integer.parseInt(txtToimipisteID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_palvelu.getNimi() == null) {
		// poistettavaa asiakasta ei löydy tietokannasta, tyhjennetään tiedot näytöltä
			txtToimipisteID.setText("");
			txtNimi.setText("");
			txtTyyppi.setText("");
			txtKuvaus.setText("");
			txtHinta.setText("");
			txtAlv.setText("");
			JOptionPane.showMessageDialog(null, "Palvelua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
			return; // poistutaan
		}
		else
		{
            // naytetaan poistettavan asiakkaan tiedot
            txtToimipisteID.setText(String.valueOf(m_palvelu.getToimipisteId()));
			txtNimi.setText(m_palvelu.getNimi());
			txtTyyppi.setText(String.valueOf(m_palvelu.getTyyppi()));                                                       
			txtKuvaus.setText(m_palvelu.getKuvaus());
			txtHinta.setText(String.valueOf(m_palvelu.getHinta()));
			txtAlv.setText(String.valueOf(m_palvelu.getAlv()));
		}
		try {
			if (JOptionPane.showConfirmDialog(null, "Haluatko todella poistaa palvelun?")==0) {// vahvistus ikkunassa
				m_palvelu.poistaPalvelu (m_conn);
				palvelu_poistettu = true;
			}
			} catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Palvelun poistaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				// se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Palvelun poistaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (palvelu_poistettu == true) { // ainoastaan, jos vahvistettiin ja poisto onnistui
                    txtPalveluID.setText("");
                    txtToimipisteID.setText("");
					txtNimi.setText("");
					txtTyyppi.setText("");
					txtKuvaus.setText("");
					txtHinta.setText("");
					txtAlv.setText("");
					JOptionPane.showMessageDialog(null, "Palvelu  poistettu tietokannasta.");
					m_palvelu = null;
				}
			}
			
		
	}

}
