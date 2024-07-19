package com.blbilink.blbilogin.vars;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configvar {
    public static String language;
    public static File configFile;
    public static FileConfiguration config;
    public static String prefix;

    public static boolean noLoginPlayerCantMove;
    public static boolean noLoginPlayerCantUseCommand;
    public static List noLoginPlayerAllowUseCommand;
    public static boolean noLoginPlayerCantBreak;
    public static boolean noLoginPlayerCantHurt;
    public static boolean noLoginPlayerParticle;
    public static boolean playerJoinAutoTeleportToSavedLocation;
    public static boolean playerJoinAutoTeleportToSavedLocation_AutoBack;
    public static boolean noLoginPlayerSendMessage;
    public static boolean noLoginPlayerSendTitle;
    public static boolean noLoginPlayerSendSubTitle;
    public static boolean noLoginPlayerSendActionBar;
    public static boolean noRegisterPlayerSendMessage;
    public static boolean noRegisterPlayerSendTitle;
    public static boolean noRegisterPlayerSendSubTitle;
    public static boolean noRegisterPlayerSendActionBar;

    public static boolean successLoginSendTitle;
    public static boolean successLoginSendSubTitle;

    public static boolean isFolia = false;

    public static List<String> noLoginPlayerList = new ArrayList<>();

    public static String location_world;
    public static double location_x;
    public static double location_y;
    public static double location_z;
    public static float location_yaw;
    public static float location_pitch;
}
