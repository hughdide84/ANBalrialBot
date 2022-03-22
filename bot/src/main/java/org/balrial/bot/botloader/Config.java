package org.balrial.bot.botloader;

import com.google.gson.Gson;

import java.io.File;

//Todo mover esto a java properties (que lo haga el diego de mañana)
public class Config {

    Gson gson = new Gson();
    File configFile = new File("src/main/resources/bot_config.json");



    public static String getToken() {
        return null;
    }


    public static String getBotName() {
        return null;
    }

}
