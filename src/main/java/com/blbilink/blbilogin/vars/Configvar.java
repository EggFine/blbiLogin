package com.blbilink.blbilogin.vars;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configvar {

    public static Map<String, Location> originalLocation = new HashMap<>();
    public static List<String> noLoginPlayerList = new ArrayList<>();
    public static List<String> canFlyingPlayerList = new ArrayList<>();
    public static FileConfiguration config;
    public static File configFile;

}
