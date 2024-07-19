package com.blbilink.blbilogin.modules;

import java.util.ArrayList;
import java.util.List;

public class Configvar {
    public static String language;
    public static String prefix;

    public static boolean noLoginPlayerCantMove;
    public static boolean noLoginPlayerCantUseCommand;
    public static List noLoginPlayerAllowUseCommand;
    public static boolean noLoginPlayerCantBreak;
    public static boolean noLoginPlayerCantHurt;
    public static boolean noLoginPlayerParticle;

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
}
