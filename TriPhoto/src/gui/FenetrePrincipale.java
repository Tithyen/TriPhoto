package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import enumerations.Couleurs;
import enumerations.Texte;
import tri.Message;
import tri.Parametres;
import tri.Log;
import tri.Process;
import utils.VersionProjet;

/**
 * Classe définissant la fenêtre principale de l'IHM
 * @author thierryG
 *
 */
@SuppressWarnings("serial")
public class FenetrePrincipale extends JFrame  {
	
	//private JFileChooser fc;
	private JPanel panelWest, panelCentre;
	private CardLayout cardLayout = new CardLayout();
	private Memo memo = new Memo();
	private JProgressBar progressBar;
	private Bouton ba, bt;
	private Log log ;
	
	/**
	 * constructeur 
	 */
	public FenetrePrincipale() {
		
		log = new Log();
		this.setTitle("Tri photos et videos");
		this.setSize(1100, 900);
		this.setMinimumSize(new Dimension(800,600));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//On ajouter un événement fermeture de la fenetre
		//pour enregistrer les propriétés dans le fichier Tri.properties
		//Voir la classe Parametres
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}						
		});
			
		///////////////////// BARRE DE MENU
		this.setJMenuBar(new BarreMenu());
		
		///////////////////// PANEL WEST
		//ce panel sera composé d'un panel ayant un Boxlayout 
		//pour avoir des boutons alignés verticalement		
		panelWest = new JPanel();
		panelWest.setBackground(Color.DARK_GRAY);
		//panelWest.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Couleurs.PANEL_FONCE.getCouleur()));
		panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS ));

		//Création des BoutonMenu
		String[] menu = {"Tri", "Settings"};
		List<Bouton> listeBoutonMenu = new ArrayList<Bouton>();
		for (String s : menu) {
			URL urlImage = this.getClass().getResource("/Menu" + s + ".png");
			ImageIcon icon = new ImageIcon(urlImage);
			Bouton bouton = new Bouton(s, icon, 200, 200);
			listeBoutonMenu.add(bouton);
			bouton.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Couleurs.MENU_BORDURE.getCouleur()));
			bouton.setCouleurFond(Couleurs.MENU_DEFAUT.getCouleur());
			bouton.setCouleurFondSurvole(Couleurs.MENU_SURVOLE.getCouleur());
			bouton.setName(s);
			bouton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					evenementSourisBoutonMenu(bouton);
				}
			});
			panelWest.add(bouton);	
		}
		
		//Affichafe de la VERSION
		//creation du textarea intermediaire
		JTextArea tampon = new JTextArea();
		tampon.setBackground(Couleurs.MENU_DEFAUT.getCouleur());
		panelWest.add(tampon);
		//creation du textpane version
		JTextPane jtp = new JTextPane();
		jtp.setMaximumSize(new Dimension(200, 20));
		jtp.setBackground(Couleurs.MENU_DEFAUT.getCouleur());
		jtp.setFont(Texte.VERSION.getFont());
		jtp.setForeground(Texte.VERSION.getCouleur());
		
		VersionProjet version = new VersionProjet();
		jtp.setText(version.getVersion());
	    panelWest.add(jtp);
		
				
		////////////////////// PANEL CENTER
		/* dans ce panel on disposera un Jpanel muni d'un CardLayout
		 * ce layout permettra de modifier le panel visible en fonction
		 * du choix qui sera fait depuis les boutons menu
		 */
		panelCentre = new JPanel();
		panelCentre.setLayout(cardLayout);
		
		/*on définit les JPanel qui seront chacun associés à un layout
		 * et rendus visibles après appui sur un bouton menu
		 */	
		//accueil : panel que l'on verra à l'ouverture de l'application
		JPanel panelAccueil = new JPanel();
		panelAccueil.setBackground(Couleurs.PANEL_CLAIR.getCouleur());
		
		//tri
		//on affecte à ce panel tri un layout de type border layout pour y placer 
		//les différents éléments voulus
		JPanel panelTri = new JPanel();
		panelTri.setLayout(new BorderLayout());
		
		JPanel panelTriNorth = new JPanel();
		JLabel labelTriNorth = new JLabel("Lancement du Tri des fichiers");
		labelTriNorth.setForeground(Texte.TITRE2.getCouleur());
		labelTriNorth.setFont(Texte.TITRE2.getFont());
		panelTriNorth.add(labelTriNorth);		
		
		//on créé un JScrollPane dans lequel on place un objet Memo 
		JScrollPane areaScrollPane = new JScrollPane(memo);
		areaScrollPane.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		areaScrollPane.setBackground(Couleurs.PANEL_CLAIR.getCouleur());	
		
		//on définit un Jpanel que l'on placera en position sud
		JPanel panelTriSud = new JPanel();
		//on définit un GridBagLayout pour ce panel
		panelTriSud.setLayout(new GridBagLayout());
		panelTriSud.setBackground(Couleurs.PANEL_FONCE.getCouleur());
		
		//on définit deux boutons dans ce panel
		bt = new Bouton("Lancer le Tri", 150, 50);
		bt.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (bt.isEnabled()) {
					// on teste si les parametres sont définis correctement
					//sinon on ne permet pas le lancement du tri et on affiche
					//le panel settings avec les erreurs identifiées
					if (!Parametres.ListerCleErreur().isEmpty()) {
						//on informe du problème par une boite de dialogue
					   // ImageIcon img = new ImageIcon("images/MenuAccueil.png");
					    JOptionPane.showMessageDialog(null, "Impossible de poursuivre le tri.\n "
					    		+ "Des paramètres doivent être complétés avant de poursuivre.",
					    		"Actions requises de votre part", JOptionPane.WARNING_MESSAGE); 
					    // on affiche le panel settings
					    cardLayout.show(panelCentre, menu[1]);
					    for (Bouton b : listeBoutonMenu) {
							if (b.getLegende() == menu[1]) {
								b.setSelectionne(true);
							} else {
								b.setSelectionne(false);
							}
						}
					} else {
						LancementTri();
					}
				}
			}
		});
		
		ba = new Bouton("Annuler", 150, 50);
		ba.setEnabled(false);
		ba.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				
			}
		});

		//on définit une JProgressBar
		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		progressBar.setFont(Texte.TITRE1.getFont());
		progressBar.setVisible(true);
		
		//on place ces 3 éléments dans le panel en jouant sur les 
		//propriétés du gbc
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.;
		gbc.insets = new Insets(1, 0, 1, 0);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		panelTriSud.add(bt, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.;
		//gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(1, 1, 1, 1);
		//gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		panelTriSud.add(progressBar, gbc);
		gbc.gridx = 2;
		gbc.weightx = 0.;
		gbc.insets = new Insets(1, 0, 1, 0);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		panelTriSud.add(ba, gbc);	
		
		panelTri.add(panelTriNorth, BorderLayout.NORTH);
		panelTri.add(areaScrollPane, BorderLayout.CENTER);
		panelTri.add(panelTriSud, BorderLayout.SOUTH);
		
		//settings
		//on affecte à ce panel un layout de type border layout pour y placer 
		//les différents éléments voulus
		JPanel panelSettings = new JPanel();
		panelSettings.setBackground(Couleurs.PANEL_DEFAUT.getCouleur());
		panelSettings.setLayout(new BorderLayout());
		
		//on cree un panel north
		JPanel panelSettingsNorth = new JPanel();
		panelSettingsNorth.setBackground(Couleurs.PANEL_CLAIR.getCouleur());
		//on ajoute un label en position north
		JLabel labelSettingsNorth = new JLabel("Définition des Paramètres");
		labelSettingsNorth.setFont(Texte.TITRE2.getFont());
		panelSettingsNorth.add(labelSettingsNorth);
		
		//on créé un panelCenter muni d'un GridBagLayout
		JPanel panelSettingsCenter = new JPanel();
		panelSettingsCenter.setLayout(new BorderLayout());
		panelSettingsCenter.add(new PanelProprietes(), BorderLayout.CENTER);
		
		panelSettings.add(panelSettingsNorth, BorderLayout.NORTH);
		panelSettings.add(panelSettingsCenter, BorderLayout.CENTER);
		
		//panelCentre.add(panelAccueil, menu[0]);
		panelCentre.add(panelTri, menu[0]);
		panelCentre.add(panelSettings, menu[1]);
		
		//par defaut on affiche le panel Accueil
		//on sélectionne le bouton menu correspondant
		cardLayout.show(panelCentre, menu[0]);
		for (Bouton b : listeBoutonMenu) {
			if (b.getLegende() == menu[0]) {
				b.setSelectionne(true);
			} else {
				b.setSelectionne(false);
			}
		}

        this.getContentPane().add(panelWest, BorderLayout.WEST);
        this.getContentPane().add(panelCentre, BorderLayout.CENTER);
        
        this.setVisible(true);
	}

	
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FenetrePrincipale.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    ////////////////////////////////// EVENEMENTS SUR SOURIS ////////////////////////////////////
    
    private void evenementSourisBoutonMenu(Bouton argBouton) {
    	//on change le fond du bouton qui a été selectionné et on
		// fixe la couleurs des autres boutons à leur couleur par défaut
		argBouton.setSelectionne(true);
		for (Component c : panelWest.getComponents()) {
			if (c instanceof Bouton && c.getName() != argBouton.getName()) {
				((Bouton)c).setNonSelectionne();
			}
		}
		//on affiche le panel qui va bien dans le cardlayout
		cardLayout.show(panelCentre, argBouton.getLegende());
    }

	private void LancementTri() {
		/*on implemente un SwingWorker qui permet d'effectuer la tâche
		 * de tri en tâche de fond ;
		 * La méthode 'process' gère le retours de données pendant le traitement
		 * et la méthode 'done' les retours de fin de traitement
		 */
		new SwingWorker<Void, Message>(){
			@Override
            public Void doInBackground() {
            	bt.setEnabled(false);
				ba.setEnabled(true);
				progressBar.setStringPainted(true);
				progressBar.setString("");
				progressBar.setValue(progressBar.getMinimum());
            	//on instancie un objet log
				log = new Log();
				//on instancie la classe process avec un ecouteur status 
				//qui permettra de recuperer l'information dans la methode process
            	new Process((message)->publish(message));
                return null;
            }
			
            @Override
            protected void done() {
            	progressBar.setString("Terminé");
                bt.setEnabled(true);
				ba.setEnabled(false);
				//Affichage du log si option correspodante est a true
				Boolean b = false;
				if (Parametres.getValeur("log.choix.affiche").contains("true")) {
					b = true;
				}
				try {
					log.clore(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            @Override
            protected void process(List<Message> argMessage) {
                for(Message m : argMessage) {
                	switch (m.getType()) {
                		// Il s'agit d'un texte donc à afficher dans le memo et le log
                		case Memo: 	memo.ajouter((String)m.getValeur(), m.getLevel());
                					if (m.getType() != null) { //on enregistre aussi dans le log
                						log.ajouter((String)m.getValeur(), m.getLevel());
                					}
                					break;
                		// il s'agit d'un etat d'avancement de la progressBar	
                		case Progression : 	if (progressBar.isVisible()) {
                								progressBar.setValue((Integer)m.getValeur());
                							}
                							break;
                		// il s'agit de la valeur max de la progressBar
                		case Max : 	progressBar.setMaximum((Integer)m.getValeur());
                    				progressBar.setValue(0);
                    				progressBar.setStringPainted(true);
                    				progressBar.setVisible(true);  
                    				progressBar.setString("En cours...");
                    				break;
                	}
                }
            }
		}.execute();
	}		
	
}
