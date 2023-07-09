package doodieman.towerdefense.game.objects.turrets;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameMob;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class WoodTower extends GameTurret {

    long lastShot = 0L;
    long roundTick = 0L;

    public WoodTower(Game game, TurretType turretType, Location location) {
        super(game, turretType, location);
    }

    @Override
    public void update(long roundTick) {
        this.roundTick = roundTick;

        //Shoot
        if (roundTick >= lastShot + ( 20L / getTurretType().getShotsPerSecond() )) {
            this.lastShot = roundTick;
            this.shootClosestMob();
        }

    }

    @Override
    public void roundFinished() {
        this.roundTick = 0L;
        this.lastShot = 0L;
    }

    @Override
    public List<GameMob> detect() {
        return this.getGame().getAliveMobs()
            .stream()
            .filter(mob -> mob.getLocation().distance(this.getLocation()) <= this.getTurretType().getRange())
            .collect(Collectors.toList());
    }

    @Override
    public void shoot(GameMob mob) {
        this.rotateTowardsMob(mob);
        mob.damage(getTurretType().getDamage());
    }



}
