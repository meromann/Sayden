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
	
	public static final int SCREEN_WIDTH = 80;
	public static final int SCREEN_HEIGHT = 30;
	
	public static final int WORLD_WIDTH = 80;
	public static final int WORLD_HEIGHT = 24;
	
	public static final int MENU_OFFSET = (int)(SCREEN_HEIGHT * .95f);
	
	public static final String STARTING_MAP = "Pueblo";
	
	public ApplicationMain(){
		super();
		terminal = new AsciiPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
		add(terminal);
		pack();
		screen = new StartScreen();
		addKeyListener(this);
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
	}
}
