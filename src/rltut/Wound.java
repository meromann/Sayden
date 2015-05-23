package rltut;

public class Wound {
	protected int duration;
	
	public boolean isHealed() { return duration < 1; }
	
	private int severity;
	public int severity() { return severity; }
	
	private String name;
	public String name() { return name; }
	
	public Wound(int duration, int severity, String name){
		this.duration = duration;
		this.severity = severity;
		this.name = name;
	}
	
	public Wound(Wound other){
		this.duration = other.duration; 
	}
	
	public void update(Creature creature){
		duration--;
	}
	
	public void onApply(Creature creature){	}
	
	public void onFinish(Creature creature){ }
}
