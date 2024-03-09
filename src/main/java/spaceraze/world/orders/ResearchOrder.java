package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Player;
import spaceraze.world.enums.HighlightType;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "RESEARCH_ORDER")
public class ResearchOrder implements Serializable {
	 static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "researchOrder")
	private Expense expense;

	@ManyToOne
	@JoinColumn(name = "FK_ORDERS")
	private Orders orders;
	 
	 private String advantageName;
	 private int cost;

	 public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public ResearchOrder(String name, int cost){
		 setAdvantageName(name);
		 setCost(cost);
	 }

	 public String getAdvantageName() {
		return advantageName;
	}

	public void setAdvantageName(String advantageName) {
		this.advantageName = advantageName;
	}
	
	public String getText(){
	    String returnString = "";
	    returnString = "Research on " + getAdvantageName();
	    return returnString;
	  }
	
	public void addToHighlights(Player p, HighlightType type){
		p.addToHighlights(getAdvantageName(), type);
	}
	
}
