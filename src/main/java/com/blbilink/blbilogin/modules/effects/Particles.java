package com.blbilink.blbilogin.modules.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Particles {
    public void createFallingParticlesAroundPlayer(Player player) {
        Location playerLocation = player.getLocation();
        double radius = 1.5; // 粒子圈的半径
        int particleCount = 10; // 每圈粒子数量

        for (int i = 0; i < particleCount; i++) {
            // 计算粒子位置
            double angle = 2 * Math.PI * i / particleCount;
            double x = playerLocation.getX() + radius * Math.cos(angle);
            double y = playerLocation.getY() + 1;
            double z = playerLocation.getZ() + radius * Math.sin(angle);

            // 设置粒子位置
            Location particleLocation = new Location(playerLocation.getWorld(), x, y, z);


            // 发送粒子效果
            Particle particle = Particle.valueOf("FIREWORKS_SPARK");
            playerLocation.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0.1); // 使用Firework粒子效果
        }

    }
}
