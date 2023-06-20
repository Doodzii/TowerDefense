package doodieman.towerdefense.game.objects.turrets;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameMob;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class WoodTower extends GameTurret {

    private double range;

    public WoodTower(Game game, TurretType turretType, Location location) {
        super(game, turretType, location);
        this.range = 5;
    }

    @Override
    public void update(long roundTick) {

        if (roundTick % 60 == 0)
            this.shootClosestMob();
    }

    @Override
    public List<GameMob> detect() {
        return this.getGame().getAliveMobs()
            .stream()
            .filter(mob -> mob.getLocation().distance(this.getLocation()) <= range)
            .collect(Collectors.toList());
    }

    @Override
    public void shoot(GameMob mob) {
        this.rotateTowardsMob(mob);
        mob.kill();
    }



}