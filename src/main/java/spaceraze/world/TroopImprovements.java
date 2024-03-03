package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class TroopImprovements implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeUuid;
    private int attackInfantry;
    private int attackArmored;
    private int attackArtillery;
    private int damageCapacity;
    private boolean changeSpaceshipTravel;
    private boolean spaceshipTravel;
    private boolean changeVisible;
    private boolean visible;
    private int costBuild;
    private int costSupport;

    public TroopImprovements(String typeUuid){
        this.typeUuid = typeUuid;
    }

    public String getTypeUuid(){
        return typeUuid;
    }

    public int getAttackInfantry() {
        return attackInfantry;
    }

    public void setAttackInfantry(int attackInfantry) {
        this.attackInfantry = attackInfantry;
    }

    public int getAttackArmored() {
        return attackArmored;
    }

    public void setAttackArmored(int attackArmored) {
        this.attackArmored = attackArmored;
    }

    public int getAttackArtillery() {
        return attackArtillery;
    }

    public void setAttackArtillery(int attackArtillery) {
        this.attackArtillery = attackArtillery;
    }

    public int getDamageCapacity() {
        return damageCapacity;
    }

    public void setDamageCapacity(int damageCapacity) {
        this.damageCapacity = damageCapacity;
    }

    public boolean isChangeSpaceshipTravel() {
        return changeSpaceshipTravel;
    }

    public void setChangeSpaceshipTravel(boolean changeSpaceshipTravel) {
        this.changeSpaceshipTravel = changeSpaceshipTravel;
    }

    public boolean isSpaceshipTravel() {
        return spaceshipTravel;
    }

    public void setSpaceshipTravel(boolean spaceshipTravel) {
        this.spaceshipTravel = spaceshipTravel;
    }

    public boolean isChangeVisible() {
        return changeVisible;
    }

    public void setChangeVisible(boolean changeVisible) {
        this.changeVisible = changeVisible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCostBuild() {
        return costBuild;
    }

    public void setCostBuild(int costBuild) {
        this.costBuild = costBuild;
    }

    public int getCostSupport() {
        return costSupport;
    }

    public void setCostSupport(int costSupport) {
        this.costSupport = costSupport;
    }
}
