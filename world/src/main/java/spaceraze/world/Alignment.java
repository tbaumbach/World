package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Functions;

import javax.persistence.*;

/**
 * Represents one alignment, with a text identifier/name
 * @author wmpabod
 *
 */
@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "ALIGNMENT")
public class Alignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GAME_WORLD")
	private GameWorld gameWorld;

	private String name;
	private String description;

	@JoinTable(name = "CAN_HAVE_VIP_LIST", joinColumns = {
			@JoinColumn(name = "ID", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "CAN_HAVE_VIP_FROM_ALIGNMENT", referencedColumnName = "id", nullable = false)})
	@ManyToMany
	private List<Alignment> canHaveVipList = new ArrayList<>(); // factions from this alignments can have vips from these alignments
	private boolean duelOwnAlignment = true; // duellists with false will never fight another duellist with the same alignment
	@JoinTable(name = "HATE_DUELLISTS_LIST", joinColumns = {
			@JoinColumn(name = "ID", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "CAN_HAVE_VIP_FROM_ALIGNMENT", referencedColumnName = "id", nullable = false)})
	@ManyToMany
	private List<Alignment> hateDuellistsList = new ArrayList<>(); // will fight duellists from these alignments even on same side
	
	@JsonIgnore
	  public String getHTMLTableContent(){
		  StringBuffer sb = new StringBuffer();
		  sb.append("<table border=\"0\" cellspacing=\"4\" cellpadding=\"0\" class=\"sr\">\n");
//		  sb.append("<tr><td>&nbsp;</td></tr>\n");
		  sb.append("<tr>");
		  sb.append("<td>Name</td>");
		  sb.append("<td>" + name + "</td>");
		  sb.append("</tr>\n");
		  sb.append("<tr>");
		  sb.append("<td>Duel own alignment</td>");
		  sb.append("<td>" + Functions.getYesNo(duelOwnAlignment) + "</td>");
		  sb.append("</tr>\n");
		  if (description != null){
			  sb.append("<tr>");
			  sb.append("<td>Description:</td>");
			  sb.append("<td colspan=\"4\">" + description + "</td>");
			  sb.append("</tr>\n");
		  }
		  sb.append("<tr>");
		  sb.append("<td>Can have VIPs from<br>these alignments:</td>");
		  sb.append("<td colspan=\"4\">" + getCanHaveVIPStringList() + "</td>");
		  sb.append("</tr>\n");
		  if (hateDuellistsList.size() > 0){
			  sb.append("<tr>");
			  sb.append("<td>Hates duellists from<br>these alignments:</td>");
			  sb.append("<td colspan=\"4\">" + getHatesDuellistsStringList() + "</td>");
			  sb.append("</tr>\n");
		  }
		  sb.append("</table>");
		  return sb.toString();
	  }
	  
	private String getCanHaveVIPStringList(){
		StringBuffer sb = new StringBuffer();
		for (Alignment alignment : canHaveVipList) {
			if (sb.length() > 0){
				sb.append(", ");
			}
			sb.append(alignment.getName());
		}
		return sb.toString();
	}

	private String getHatesDuellistsStringList(){
		StringBuffer sb = new StringBuffer();
		for (Alignment alignment : hateDuellistsList) {
			if (sb.length() > 0){
				sb.append(", ");
			}
			sb.append(alignment.getName());
		}
		return sb.toString();
	}

	public Alignment(String aName){
		this.name = aName;
		name = name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	public boolean equals(Alignment anotherAlignment){
		return getName().equalsIgnoreCase(anotherAlignment.getName());
	}
	
	public boolean isAlignment(String aName){
		return name.equalsIgnoreCase(aName);
	}
	
	public void addCanHaveVip(Alignment anAlignment){
		canHaveVipList.add(anAlignment);
	}

	public void addHateDuellist(Alignment anAlignment){
		hateDuellistsList.add(anAlignment);
	}

	public boolean canHaveVip(String aVipAlignment){
		return findAlignment(aVipAlignment,canHaveVipList);
	}

	public List<Alignment> getCanHaveVipList(){
		ArrayList<Alignment> alignments = new ArrayList<>(canHaveVipList);
		alignments.add(0, this);
		return alignments;
	}

	public List<Alignment> getHateDuellistList(){
		return hateDuellistsList;
	}

	public boolean hateDuellist(String findAlignment){
		return findAlignment(findAlignment,hateDuellistsList);
	}
	
	/**
	 * Search the listToSearch for a specified alignment
	 * @param findAlignment 
	 * @return
	 */
	private boolean findAlignment(String findAlignment, List<Alignment> listToSearch){
		boolean found = false;
		int index = 0;
		while ((!found) & (index < listToSearch.size())) {
			Alignment anAlignment = listToSearch.get(index);
			//LoggingHandler.finer(anAlignment.getName() + ", alignment=" + findAlignment.getName());
			if (anAlignment.getName().equals(findAlignment)){
				found = true;
			}else{
				index++;
			}
		}
		return found;
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
