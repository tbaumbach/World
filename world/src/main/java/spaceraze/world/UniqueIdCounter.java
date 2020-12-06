package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "UniqueIdCounter")
@Table(name ="UNIQUE_ID_COUNTER")
public class UniqueIdCounter implements Serializable{
  static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK_GALAXY")
  private Galaxy galaxy;

  private int uniqueIdCounter = 0;
  private CounterType counterType;

  public UniqueIdCounter(CounterType counterType){
    this.counterType = counterType;
  }

  public int getUniqueId(){
    uniqueIdCounter++;
    return uniqueIdCounter;
  }


}
