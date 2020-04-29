package spaceraze.world;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "UniqueIdCounter")
public class UniqueIdCounter implements Serializable{
  static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private int uniqueIdCounter = 0;

  public UniqueIdCounter(){}

  public UniqueIdCounter(String name){
    this.name = name;
  }

  public int getUniqueId(){
    uniqueIdCounter++;
    return uniqueIdCounter;
  }

  public String getName(){
    return name;
  }

}
