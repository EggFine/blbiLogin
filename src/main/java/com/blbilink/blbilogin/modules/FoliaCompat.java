package com.blbilink.blbilogin.modules;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FoliaCompat {
    private static boolean folia;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
    }
    
    public static boolean isFolia() {
        return folia;
    }
    
    public static void runTaskAsync(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
            return;
        }
        Executors.defaultThreadFactory().newThread(run).start();
    }
    
    public static void runTaskTimerAsync(Plugin plugin, Consumer<Object> run, long delay, long period) {
        if (!folia) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> run.accept(null), delay, period);
            return;
        }
        try {
            Method getSchedulerMethod = Server.class.getDeclaredMethod("getGlobalRegionScheduler");
            getSchedulerMethod.setAccessible(true);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());
            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);
            executeMethod.setAccessible(true);
            executeMethod.invoke(globalRegionScheduler, plugin, run, delay, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void runTask(Plugin plugin, Consumer<Object> run) {
        if (!folia) {
            Bukkit.getScheduler().runTask(plugin, () -> run.accept(null));
            return;
        }
        try {
            Method getSchedulerMethod = Server.class.getDeclaredMethod("getGlobalRegionScheduler");
            getSchedulerMethod.setAccessible(true);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());
            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getDeclaredMethod("run", Plugin.class, Consumer.class);
            executeMethod.setAccessible(true);
            executeMethod.invoke(globalRegionScheduler, plugin, run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable run, Runnable retired, long delay) {
    	if (!folia) {
    		Bukkit.getScheduler().runTaskLater(plugin, run, delay);
    		return;
    	} 
    	try {
    		Method getSchedulerMethod = Server.class.getDeclaredMethod("getScheduler");
    		Object entityScheduler = getSchedulerMethod.invoke(entity, new Object[0]);
    		Class<?> schedulerClass = entityScheduler.getClass();
    		Method executeMethod = schedulerClass.getMethod("execute", new Class[] { Plugin.class, Runnable.class, Runnable.class, long.class });
    		executeMethod.invoke(entityScheduler, new Object[] { plugin, run, retired, Long.valueOf(delay) });
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    }
	
	public static void runTaskLater(Plugin plugin, Runnable task, long delay) {
		if (!folia) {
			Bukkit.getScheduler().runTaskLater(plugin, task, delay);
		} else {
			try {
				Method getSchedulerMethod = Server.class.getDeclaredMethod("getGlobalRegionScheduler");
				getSchedulerMethod.setAccessible(true);
				Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());
				Class<?> schedulerClass = globalRegionScheduler.getClass();
				Method executeMethod = schedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Runnable.class, long.class);
				executeMethod.setAccessible(true);
				executeMethod.invoke(globalRegionScheduler, plugin, task, delay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}