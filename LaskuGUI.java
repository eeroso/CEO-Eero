import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;  


public class LaskuGUI extends JFrame {
//
    private Lasku m_lasku = new Lasku (); // varausolio, jota tässä pääsääntöisesti käsitellään
	private Connection m_conn; // tietokantayhteys
// käyttöliittymän otsikkokentät
    private JLabel lblLaskuID;
	private JLabel lblVarausID;
	private JLabel lblAsiakasID;
	private JLabel lblNimi;
	private JLabel lblLahiosoite;
    private JLabel lblPostitoimipaikka;
    private JLabel lblPostinro;
    private JLabel lblSumma;
    private JLabel lblAlv;

//käyttöliittymän tekstikentät
    private JTextField txtLaskuID;
	private JTextField txtVarausID;
	private JTextField txtAsiakasID;
	private JTextField txtNimi;
    private JTextField txtLahiosoite;
    private JTextField txtPostitoimipaikka;
    private JTextField txtPostinro;
    private JTextField txtSumma;
    private JTextField txtAlv;
    
 
// käyttöliittymän painikkeet	
    private JButton btnLisaa;
    private JButton btnMuuta;
	private JButton btnHae;
    private JButton btnPoista;
    private JButton btnSlasku;
    private JButton btnPlasku;
	private JButton btnPaluu;
	
    public LaskuGUI() {

        lblLaskuID = new JLabel("Lasku ID");
		lblVarausID = new JLabel("Varaus ID");
		lblAsiakasID = new JLabel("Asiakas ID");
		lblNimi = new JLabel("Nimi");
		lblLahiosoite = new JLabel("Lähiosoite");
		lblPostitoimipaikka = new JLabel("Postitoimipaikka");
        lblPostinro = new JLabel("Postinumero");
        lblSumma = new JLabel("Summa");
        lblAlv = new JLabel("Alv");
        

        txtLaskuID = new JTextField (12);
		txtVarausID = new JTextField (12);
		txtAsiakasID = new JTextField (12);
		txtNimi = new JTextField (36);
		txtLahiosoite = new JTextField (40);
		txtPostitoimipaikka = new JTextField (26);
        txtPostinro = new JTextField (5);
        txtSumma = new JTextField (36);
        txtAlv = new JTextField (36);


        btnHae = new JButton("Hae");
		btnMuuta = new JButton("Muuta");
		btnLisaa = new JButton("Lisaa");
        btnPoista = new JButton("Poista");
        btnSlasku = new JButton("S-postilasku");
        btnPlasku = new JButton("Paperilasku");
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
        btnSlasku.addActionListener(new java.awt.event.ActionListener() { //sahkopostilasku, avaa ruudun johon haluttu spostiosoite kirjoitetaan. Ei toimi todellisuudessa
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String name = JOptionPane.showInputDialog(null,
                        "Anna sahkopostiosoite johon lasku lahetetaan", null);
					}
                });
        btnPlasku.addActionListener(   //paperilasku tapahtumakuuntelija
			new ActionListener () {
				public void actionPerformed(ActionEvent actEvent) {	
						 paperi_lasku(); //ajaa funktion painaessa
					
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
        p2.setLayout(new GridLayout(9, 2));

        p2.add(lblLaskuID);
		p2.add(txtLaskuID);
		
        p2.add(lblVarausID);
        p2.add(txtVarausID);

        p2.add(lblAsiakasID);
		p2.add(txtAsiakasID);
				
		p2.add(lblNimi);
		p2.add(txtNimi);
		
		p2.add(lblLahiosoite);
		p2.add(txtLahiosoite);
		
		p2.add(lblPostitoimipaikka);
		p2.add(txtPostitoimipaikka);
		
		p2.add(lblPostinro);
        p2.add(txtPostinro);
        
        p2.add(lblSumma);
        p2.add(txtSumma);
        
        p2.add(lblAlv);
		p2.add(txtAlv);
		


// oikean reunan paneli, jossa painikkeet
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(5, 1));
		p3.add(btnHae);
		p3.add(btnMuuta);
		p3.add(btnLisaa);
        p3.add(btnPoista);
        p3.add(btnSlasku);
        p3.add(btnPlasku);
		p3.add(btnPaluu); 

        p1.add(p2);
        
        p1.add(p3);
		
        add(p1);
		
		setLocation(100, 100); // Ikkunan paikka 
		setSize(1000, 500);     // Ikkunan koko leveys, korkeus
		setTitle("Lasku");  // yläpalkkiin otsikko
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
    LaskuGUI frmLasku = new LaskuGUI();  // luodaan lomakeluokan olio
	
	
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
			m_conn.close();
		}
		catch (SQLException e) { // tietokantavirhe
			throw e;
		}
		catch (Exception e ) { // muu virhe tapahtui
			throw e;
		}
		
	}
	/*
	Haetaan tietokannasta asiakkaan tiedot näytöllä olebvan varausid:n perusteella ja näytetään tiedot lomakkeella
	*/
	public  void hae_tiedot() {
		// haetaan tietokannasta varausta, jonka varaus_id = txtvarausID 
		m_lasku = null;
		
		try {
			m_lasku = Lasku.haeLasku (m_conn, Integer.parseInt(txtLaskuID.getText()), Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		if (m_lasku.getNimi() == null) {
		// muut virheet
			txtVarausID.setText("");
			txtAsiakasID.setText("");
			txtNimi.setText("");
			txtLahiosoite.setText("");
			txtPostitoimipaikka.setText("");
            txtPostinro.setText("");
            txtSumma.setText("");
			txtAlv.setText("");
			
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// naytetaan tiedot
			txtVarausID.setText(String.valueOf(m_lasku.getVarausId()));
			txtAsiakasID.setText(String.valueOf(m_lasku.getAsiakasId()));
			txtNimi.setText(m_lasku.getNimi());
			txtLahiosoite.setText(m_lasku.getLahiosoite());
			txtPostitoimipaikka.setText(m_lasku.getPostitoimipaikka());
            txtPostinro.setText(m_lasku.getPostinro());
            txtSumma.setText(String.valueOf(m_lasku.getSumma()));
			txtAlv.setText(String.valueOf(m_lasku.getAlv()));
			
		}
		
	}
	/*
	Viedään näytöllä olevat tiedot varausoliolle ja kirjoitetaan ne tietokantaan 
	*/
	public  void lisaa_tiedot() {
		// lisätään tietokantaan varaus
		//System.out.println("Lisataan...");
		boolean lasku_lisatty = true;
		m_lasku = null;
		try {
			m_lasku = Lasku.haeLasku (m_conn, Integer.parseInt(txtLaskuID.getText()), Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			lasku_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			lasku_lisatty = false;
			JOptionPane.showMessageDialog(null, "Tietokantavirhe.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		/*if (String.valueOf(m_lasku.getNimi()) != null) {
		// varaus jo olemassa, näytetään tiedot
			lasku_lisatty = false;
			txtLaskuID.setText(String.valueOf(m_lasku.getLaskuId()));
			txtVarausID.setText(String.valueOf(m_lasku.getVarausId()));
			txtAsiakasID.setText(String.valueOf(m_lasku.getAsiakasId()));
			txtNimi.setText(m_lasku.getNimi());
			txtLahiosoite.setText(m_lasku.getLahiosoite());
            txtPostitoimipaikka.setText(m_lasku.getPostitoimipaikka());
            txtPostinro.setText(m_lasku.getPostinro());
            txtSumma.setText(String.valueOf(m_lasku.getSumma()));
            txtAlv.setText(String.valueOf(m_lasku.getAlv()));
            
			JOptionPane.showMessageDialog(null, "Lasku on jo olemassa.", "Virhe", JOptionPane.ERROR_MESSAGE);
		}*/
		//else
		 //{
            // asetetaan tiedot oliolle
            m_lasku.setLaskuId(Integer.parseInt(txtLaskuID.getText()));
			m_lasku.setVarausId(Integer.parseInt(txtVarausID.getText()));
			m_lasku.setAsiakasId(Integer.parseInt(txtAsiakasID.getText()));			
			m_lasku.setNimi(txtNimi.getText());
			m_lasku.setLahiosoite(txtLahiosoite.getText());
			m_lasku.setPostitoimipaikka(txtPostitoimipaikka.getText());
            m_lasku.setPostinro(txtPostinro.getText());
            m_lasku.setSumma(Double.parseDouble(txtSumma.getText()));
            m_lasku.setAlv(Double.parseDouble(txtSumma.getText()));	

			try {
				// yritetään kirjoittaa kantaan
				m_lasku.lisaaLasku (m_conn);
			} catch (SQLException se) {
			// SQL virheet
				lasku_lisatty = false;
				JOptionPane.showMessageDialog(null, "Laskun lisaaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
			//	 se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				lasku_lisatty = false;
				JOptionPane.showMessageDialog(null, "Laskun lisaaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
			//	 e.printStackTrace();
			}finally {
				if (lasku_lisatty == true)
					JOptionPane.showMessageDialog(null, "Laskun tiedot lisatty tietokantaan.");
			}
		
		//}
		
	}
	/*
	Viedään näytöllä olevat tiedot varausoliolle ja muutetaan ne tietokantaan
	*/
	public  void muuta_tiedot() {
		//System.out.println("Muutetaan...");
			boolean lasku_muutettu = true;
		// asetetaan tiedot oliolle
        m_lasku.setLaskuId(Integer.parseInt(txtLaskuID.getText()));
        m_lasku.setVarausId(Integer.parseInt(txtVarausID.getText()));
        m_lasku.setAsiakasId(Integer.parseInt(txtAsiakasID.getText()));			
        m_lasku.setNimi(txtNimi.getText());
        m_lasku.setLahiosoite(txtLahiosoite.getText());
        m_lasku.setPostitoimipaikka(txtPostitoimipaikka.getText());
        m_lasku.setPostinro(txtPostinro.getText());
        m_lasku.setSumma(m_lasku.getSumma());
        m_lasku.setAlv(m_lasku.getAlv());	
		
			try {
				// yritetään muuttaa (UPDATE) tiedot kantaan
				m_lasku.muutaLasku (m_conn);
			} catch (SQLException se) {
			// SQL virheet
				lasku_muutettu = false;
				JOptionPane.showMessageDialog(null, "Varauksen tietojen muuttaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				 //se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				lasku_muutettu = false;
				JOptionPane.showMessageDialog(null, "Varauksen tietojen muuttaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (lasku_muutettu == true)
					JOptionPane.showMessageDialog(null, "Varauksen tiedot muutettu.");
			}
		
    }
    
    public  void paperi_lasku() { //paperilaskun kirjoittamiseen
        m_lasku = null;
        try {
			m_lasku = Lasku.haeLasku (m_conn, Integer.parseInt(txtLaskuID.getText()), Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (String.valueOf(m_lasku.getVarausId()) == null) {
		// poistettavaa asiakasta ei löydy tietokannasta, tyhjennetään tiedot näytöltä
        txtVarausID.setText("");
        txtAsiakasID.setText("");
        txtNimi.setText("");
        txtLahiosoite.setText("");
        txtPostitoimipaikka.setText("");
        txtPostinro.setText("");
        txtSumma.setText("");
        txtAlv.setText("");

			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
			return; // poistutaan
		}
		else
		{
			// naytetaan poistettavan asiakkaan tiedot
			txtLaskuID.setText(String.valueOf(m_lasku.getLaskuId()));
			txtVarausID.setText(String.valueOf(m_lasku.getVarausId()));
			txtAsiakasID.setText(String.valueOf(m_lasku.getAsiakasId()));
			txtNimi.setText(m_lasku.getNimi());
			txtLahiosoite.setText(m_lasku.getLahiosoite());
            txtPostitoimipaikka.setText(m_lasku.getPostitoimipaikka());
            txtPostinro.setText(m_lasku.getPostinro());
            txtSumma.setText(String.valueOf(m_lasku.getSumma()));
            txtAlv.setText(String.valueOf(m_lasku.getAlv()));
        }
        
        try {
			 m_lasku.paperiLasku (m_conn, Integer.parseInt(txtLaskuID.getText()), Integer.parseInt(txtVarausID.getText()));
			}
			catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				// se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			}

    }
	public  void poista_tiedot() {
		// haetaan tietokannasta asiakasta, jonka asiakas_id = txtAsiakasID 
		m_lasku = null;
		boolean lasku_poistettu = false;
		
		try {
			m_lasku = Lasku.haeLasku (m_conn, Integer.parseInt(txtLaskuID.getText()), Integer.parseInt(txtVarausID.getText()));
		} catch (SQLException se) {
		// SQL virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		// muut virheet
			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
		}
		if (String.valueOf(m_lasku.getVarausId()) == null) {
		// poistettavaa asiakasta ei löydy tietokannasta, tyhjennetään tiedot näytöltä
        txtVarausID.setText("");
        txtAsiakasID.setText("");
        txtNimi.setText("");
        txtLahiosoite.setText("");
        txtPostitoimipaikka.setText("");
        txtPostinro.setText("");
        txtSumma.setText("");
        txtAlv.setText("");

			JOptionPane.showMessageDialog(null, "Laskua ei loydy.", "Virhe", JOptionPane.ERROR_MESSAGE);
			return; // poistutaan
		}
		else
		{
			// naytetaan poistettavan asiakkaan tiedot
			txtLaskuID.setText(String.valueOf(m_lasku.getLaskuId()));
			txtVarausID.setText(String.valueOf(m_lasku.getVarausId()));
			txtAsiakasID.setText(String.valueOf(m_lasku.getAsiakasId()));
			txtNimi.setText(m_lasku.getNimi());
			txtLahiosoite.setText(m_lasku.getLahiosoite());
            txtPostitoimipaikka.setText(m_lasku.getPostitoimipaikka());
            txtPostinro.setText(m_lasku.getPostinro());
            txtSumma.setText(String.valueOf(m_lasku.getSumma()));
            txtAlv.setText(String.valueOf(m_lasku.getAlv()));
		}
		try {
			if (JOptionPane.showConfirmDialog(null, "Haluatko todella poistaa laskun?")==0) {// vahvistus ikkunassa
				m_lasku.poistaLasku (m_conn);
				lasku_poistettu = true;
			}
			} catch (SQLException se) {
			// SQL virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Tietokantavirhe", JOptionPane.ERROR_MESSAGE);
				// se.printStackTrace();
			} catch (Exception e) {
			// muut virheet
				JOptionPane.showMessageDialog(null, "Varauksen tietojen poistaminen ei onnistu.", "Virhe", JOptionPane.ERROR_MESSAGE);
				// e.printStackTrace();
			} finally {
				if (lasku_poistettu == true) { // ainoastaan, jos vahvistettiin ja poisto onnistui
					txtVarausID.setText("");
                    txtAsiakasID.setText("");
                    txtNimi.setText("");
                    txtLahiosoite.setText("");
                    txtPostitoimipaikka.setText("");
                    txtPostinro.setText("");
                    txtSumma.setText("");
                    txtAlv.setText("");

					JOptionPane.showMessageDialog(null, "Varauksen tiedot poistettu tietokannasta.");
					m_lasku = null;
				}
			}
			
		
	}

}
