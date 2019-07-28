package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import enumerations.Couleurs;
import enumerations.Texte;
import utils.VersionProjet;

/**
 * Classe créant un objet boite de dialogue pour afficher la version en cours
 * et le changelog
 * @author thier
 *
 */
public final class DialogAPropos extends JDialog {
	
	public DialogAPropos(JFrame pParent) {
	    super(pParent, "A propos", true);
	    this.setSize(600, 400);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.initComponent();
	    this.setVisible(true);
	}
	
	private final void initComponent() {
	    //Icône
	    JLabel icon = new JLabel(new ImageIcon(this.getClass().getResource("/MenuAccueil.png")));
	    JPanel panIcon = new JPanel();
	    panIcon.setBackground(Color.DARK_GRAY);
	    panIcon.setPreferredSize(new Dimension(100, 400));
	    panIcon.setLayout(new BorderLayout());
	    panIcon.add(icon);
	    
	    //numero de version
	    JPanel panVersion = new JPanel();
	    panVersion.setBackground(Color.LIGHT_GRAY);
	    panVersion.setMaximumSize(new Dimension(500, 50));
	    JLabel v = new JLabel();
	    v.setText("TriPhoto " + new VersionProjet().getLastVersionNumber());
	    panVersion.add(v);
	    
	    //Detail du changelog
	    JPanel panVersionDetail = new JPanel();
	    panVersionDetail.setBackground(Color.LIGHT_GRAY);
	    panVersionDetail.setMinimumSize(new Dimension(500, 350));
	    JTextPane jtp = new JTextPane();
		jtp.setPreferredSize(new Dimension(500, 350));
		jtp.setMargin(new Insets(5, 10, 5, 5));
		jtp.setFont(Texte.VERSION.getFont());
		jtp.setEditable(false);
		//jtp.setForeground(Texte.VERSION.getCouleur());
		jtp.setText(new VersionProjet().getLastVersionDetail());
	    panVersionDetail.add(jtp);  
	    
	    
	    //Panel tampon pour mis en forme
	    JPanel panTampon = new JPanel();
	    panTampon.setBackground(Color.RED);
	    
	    //Content
	    JPanel content = new JPanel();
	    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS ));
	    //content.setBackground(Color.LIGHT_GRAY);
	    content.add(panVersion);
	   // content.add(panTampon);
	    content.add(panVersionDetail);


	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("OK");
	    okBouton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent arg0) {        
	          DialogAPropos.this.setVisible(false);
	        }
	      });
	    
	    control.add(okBouton);

	    this.getContentPane().add(panIcon, BorderLayout.WEST);
	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	}
}
