package rps.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import rps.client.UIController;

/**
 * creates the menu of the game
 */
public class Menu {

	private final JFrame frame;
	private final UIController controller;

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu menuGame = new JMenu("Spiel");
	private final JMenuItem menuGameNew = new JMenuItem("Neues Spiel");
	private final JMenuItem menuGameSurrender = new JMenuItem("Aufgeben");
	private final JMenuItem menuGameExit = new JMenuItem("Beenden");
	private final JMenu menuHelp = new JMenu("Hilfe");
	private final JMenuItem menuHelpRules = new JMenuItem("Spielregeln");
	private final JMenuItem menuHelpAbout = new JMenuItem("Über Stein, Papier, Schere");
	private final JMenuItem menuHelpCredits = new JMenuItem("Credits");

	public Menu(JFrame frame, UIController controller) {

		this.frame = frame;
		this.frame.setTitle("Stein, Papier, Schere");
		this.controller = controller;

		buildMenuStructure();
		bindMenuActions();
	}

	private void buildMenuStructure() {
		menuGame.setMnemonic(KeyEvent.VK_G);

		menuBar.add(menuGame);
		menuGame.add(menuGameNew);
		menuGame.add(menuGameSurrender);
		menuGame.addSeparator();
		menuGame.add(menuGameExit);
		menuBar.add(menuHelp);
		menuHelp.add(menuHelpRules);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpCredits);
		frame.setJMenuBar(menuBar);

		menuGameSurrender.setEnabled(false);
		menuGameNew.setEnabled(false);
	}

	/**
	 * associates the actions of the menu to dialogs, new games, etc.
	 */
	private void bindMenuActions() {
		menuGameSurrender.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleSurrender();

			}
		});
		menuGameNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleNewGame();

			}
		});
		menuGameExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleExit();
			}
		});
		menuHelpRules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new RulesDialog(null);
			}
		});
		menuHelpAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg1) {
				new AboutDialog(null);
			}
		});
		menuHelpCredits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg2) {
				new CreditDialog(null);
			}
		});
	}

	public void gameStarted() {
		menuGameNew.setEnabled(true);
		menuGameSurrender.setEnabled(true);
	}

	public void gameEnded() {
		menuGameNew.setEnabled(true);
		menuGameSurrender.setEnabled(false);
	}

	public void reset() {
		menuGameNew.setEnabled(false);
		menuGameSurrender.setEnabled(false);
	}
}