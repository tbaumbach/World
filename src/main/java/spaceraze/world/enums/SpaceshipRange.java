package spaceraze.world.enums;

public enum SpaceshipRange {
	NONE("NONE", "-"),
	SHORT("SHORT", "Short"),
	LONG("LONG", "Long");

	private String id;
	private String name;

	SpaceshipRange(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String toString(){
		return this.getName();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String toLowercaseString(){
		return this.getName().toLowerCase();
	}
	
	public boolean greaterThan(SpaceshipRange otherRange){
		return this.compareTo(otherRange) > 0;
	}

	public boolean lesserThan(SpaceshipRange otherRange){
		return this.compareTo(otherRange) < 0;
	}
	
	public boolean canMove(){
		return this != NONE;
	}
	
}
