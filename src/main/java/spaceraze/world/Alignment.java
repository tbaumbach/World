package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import jakarta.persistence.*;

/**
 * Represents one alignment, with a text identifier/name
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "ALIGNMENT")
public class Alignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String uuid;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_GAME_WORLD")
	private GameWorld gameWorld;

	private String name;
	private String description;

	@ElementCollection
	@CollectionTable(name = "CAN_HAVE_VIP_FROM_ALIGNMENT")
	private List<String> canHaveVips = new ArrayList<>(); // factions from this alignments can have vips from these alignments

	private boolean duelOwnAlignment = true; // duellists with false will never fight another duellist with the same alignment

	@ElementCollection
	@CollectionTable(name = "HATE_DUELLISTS")
	private List<String> hateDuellists = new ArrayList<>(); // will fight duellists from these alignments even on same side


	public Alignment(String aName, GameWorld gameWorld){
		this.uuid = UUID.randomUUID().toString();
		this.gameWorld = gameWorld;
		this.name = aName;
		name = name.substring(0,1).toUpperCase() + name.substring(1);
		this.canHaveVips = new ArrayList<>();
		this.canHaveVips.add(this.uuid);
	}
	
	public boolean equals(Alignment anotherAlignment){
		return getUuid().equalsIgnoreCase(anotherAlignment.getUuid());
	}
	
	public boolean isAlignment(String aName){
		return name.equalsIgnoreCase(aName);
	}
	
	public void addCanHaveVip(Alignment anAlignment){
		canHaveVips.add(anAlignment.getUuid());
	}

	public void addHateDuellist(Alignment anAlignment){
		hateDuellists.add(anAlignment.getUuid());
	}

	public boolean isDuelOwnAlignment() {
		return duelOwnAlignment;
	}

	public void setDuelOwnAlignment(boolean duelOwnAlignment) {
		this.duelOwnAlignment = duelOwnAlignment;
	}

	@Override
	public String toString(){
		return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName(){
		return name;
	}


}
