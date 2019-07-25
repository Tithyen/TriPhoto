package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class BarreMenu extends JMenuBar implements ActionListener {
	
	private JMenu menu1 = new JMenu("Fichier");
	private JMenuItem item11 = new JMenuItem("Quitter");
	private JMenu menu2 = new JMenu("Help");
	private JMenuItem item22 = new JMenuItem("A propos");
	
	/**
	 * constructeur	
	 */
	public BarreMenu() {
		super();
		/// MENU 1
		this.menu1.add(item11);
		this.add(menu1);		
		this.menu1.setMnemonic(KeyEvent.VK_F);
		this.item11.addActionListener(this);
		/// MENU2
		this.menu2.add(item22);
		this.add(menu2);
		this.menu2.setMnemonic(KeyEvent.VK_H);
		this.item22.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if (source == item11) {
			System.exit(0);
		} else if (source == item22) {
			//on créé et on ouvre un boite de dialogue A propos
			new DialogAPropos(null);
		}
	}
}
