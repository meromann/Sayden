package rltut;

public class Wound {

	protected int duration;
	public int duration() { return duration; }
	
	protected int originalDuration;
	public void resetDuration() { this.duration = this.originalDuration; }
	
	public boolean isHealed() { return duration < 0 ? false : duration < 1; }
	
	private int severity;
	public int severity() { return severity; }
	
	private DamageType type;
	public DamageType type() { return type; }
	
	private BodyPart bodyPart;
	public BodyPart bodyPart() { return bodyPart; }
	public Wound setBodyPart(BodyPart position) { this.bodyPart = position; return this; }
	
	private Wound reference;
	
	public Wound(int duration, int severity, DamageType type, BodyPart bodyPart){
		this.duration = duration;
		this.originalDuration = duration;
		this.severity = severity;
		this.type = type;
		this.bodyPart = bodyPart;
	}
	
	public Wound(Wound other){
		this.duration = other.duration();
		this.originalDuration = other.duration();
		this.type = other.type();
		this.severity = other.severity();
		this.bodyPart = other.bodyPart();
		this.reference = other;
	}
	
	public void update(Creature creature){
		if(duration > 0)
			duration--;
		
		if(reference != null)
			reference.update(creature);
	}
	
	public void onApply(Creature creature, Creature applier){
		if(reference != null)
			reference.onApply(creature, applier);
	}
	
	public void onFinish(Creature creature){
		if(reference != null)
			reference.onFinish(creature);
	}
}
