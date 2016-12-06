package com.teamSuperior.guiApp.enums;

/**
 * Created by Domestos on 16.12.06.
 */
public enum Drawable {
    APP_LOGO("silvan_logo_rectangle.png");

    private String path;
    Drawable(String path){
        this.path = "src/com/teamSuperior/drawable/" + path;
    }

    public String getPath(){
        return path;
    }
}
