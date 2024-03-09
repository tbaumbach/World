package spaceraze.world.orders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Handle an order to change the notes text for a planet
 *
 * @author Paul Bodin
 */
@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLANET_NOTE_CHANGE")
public class PlanetNotesChange implements Serializable {
    static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String planetName;
	private String notesText;

    public String getNotesText() {
        return notesText;
    }

    public PlanetNotesChange(String planetName, String notesText) {
        this.planetName = planetName;
        this.notesText = notesText;
    }

    public String getText() {
        String text = null;
        if ("".equals(notesText)) {
            text = "Empty notes for planet " + planetName + ".";
        } else {
            text = "Set notes for planet " + planetName + " to \"" + notesText + "\".";
        }
        return text;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
    }

}
