package rltut;

import javax.swing.JFrame;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import rltut.screens.Screen;
import rltut.screens.StartScreen;

public class ApplicationMain extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	public ApplicationMain(){
		super("Sayden");
		//Instanciamos el panel de texto
		terminal = new AsciiPanel(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		add(terminal);
		pack();
		//La primer pantalla es la StartScreen
		screen = new StartScreen();
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
		repaint();
	}
	
	@Override
	public void repaint(){
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }
	
	public static void main(String[] args) {
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(false);
		app.setSize(Constants.SCREEN_WIDTH * 9 + 1, Constants.SCREEN_HEIGHT * 16 + 29);
		//app.setBackground(AsciiPanel.black);
	}
}
