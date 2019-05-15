import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * @author Eero Sormunen
 * @version 2.0
 * @since 13.5.2019
 **/

public class GUI extends JPanel {

    //napit
    private JButton btnAsiakas;
    private JButton btnToimipiste;
    private JButton btnPalvelu;
    private JButton btnLasku;
    private JButton btnVaraus;

    public GUI() {
        //komponentit
        btnAsiakas = new JButton ("Asiakas");
        btnToimipiste = new JButton ("Toimipiste");
        btnPalvelu = new JButton ("Palvelu");
        btnLasku = new JButton ("Lasku");
        btnVaraus = new JButton ("Varaus");

        //koon säätö
        setPreferredSize (new Dimension (944, 574));
        setLayout (null);

        //komponentit
        add (btnAsiakas);
        add (btnToimipiste);
        add (btnPalvelu);
        add (btnLasku);
        add (btnVaraus);

        //nappien sijainnit
        btnAsiakas.setBounds (400, 40, 117, 50);
        btnToimipiste.setBounds (400, 120, 117, 50);
        btnPalvelu.setBounds (400, 200, 117, 50);
        btnLasku.setBounds (400, 280, 117, 50);
        btnVaraus.setBounds (400, 360, 117, 50);
     
        //laitetaan kukin nappi avaamaan sille tarkoitetun toisen GUI:n
        btnAsiakas.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
                    AsiakasGUI asiakasgui = new AsiakasGUI();
                    asiakasgui.setVisible(true); //näkyviin
					
				}
			}
        );
        btnToimipiste.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
                    ToimipisteGUI toimipistegui = new ToimipisteGUI();
                    toimipistegui.setVisible(true);
					
				}
			}
        );
        btnPalvelu.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
                    PalveluGUI palvelugui = new PalveluGUI();
                    palvelugui.setVisible(true);
					
				}
			}
        );
        btnLasku.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
                    LaskuGUI laskugui = new LaskuGUI();
                    laskugui.setVisible(true);
					
				}
			}
        );
        btnVaraus.addActionListener(   // toteutetaan  käyttämällä Javan ns. nimettömiä sisäluokkia
			new ActionListener () {// parametrina luotavan "rajapintaluokan ilmentymä": new ActionListener()
				public void actionPerformed(ActionEvent actEvent) {	
                    VarausGUI varausgui = new VarausGUI();
                    varausgui.setVisible(true);
					
				}
			}
		);
   
   
   
   
    }
    
    public static void main (String[] args) {
        JFrame frame = new JFrame ("MasterGUI");

        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); //sammuu kokonaan
        frame.getContentPane().add (new GUI());
        frame.pack();
        frame.setVisible (true);
    }
}