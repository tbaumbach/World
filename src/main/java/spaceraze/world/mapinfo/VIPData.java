package spaceraze.world.mapinfo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data om alla vippar fr�n en spelare
 * 
 * @author developer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "VIP_DATA")
public class VIPData implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String playerName;

	@ElementCollection
	@CollectionTable(name = "VIP_SHORT_NAME")
	@Builder.Default
	private List<String> vipShortNames = new ArrayList<>();

	@Transient
	public void addVipShortName(String vipShortName) {
		vipShortNames.add(vipShortName);
	}

	@Transient
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("vipShortNames, playerName: " + playerName);
		sb.append("\n");
		for (String str : vipShortNames) {
			sb.append("\tvipShortName: " + str);
			sb.append("\n");
		}
		return sb.toString();
	}

}
