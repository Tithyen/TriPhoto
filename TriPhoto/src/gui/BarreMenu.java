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
	
	/**
	 * constructeur	
	 */
	public BarreMenu() {
		super();
		this.menu1.add(item11);
		this.add(menu1);
		
		this.menu1.setMnemonic(KeyEvent.VK_F);
		this.item11.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if (source == item11) {
			System.exit(0);
		}
	}
}
