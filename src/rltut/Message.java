package rltut;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public class Message {
	private String message;
	public String message() { return message; }
	
	private Color color;
	public Color color() { return color; }
	
	public Message (String message){
		this.message = message;
		this.color = AsciiPanel.white;
	}
	
	public Message (String message, Color color) {
		this.message = message;
		this.color = color;
	}
}
