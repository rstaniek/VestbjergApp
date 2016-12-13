package com.teamSuperior.guiApp.enums;

/**
 * Created by Domestos on 16.12.06.
 * This enum stores filenames of all drawable objects in the project
 * can be used with Drawable class
 */
public enum Drawables {
    TEST_MEME("/i_want_to_believe.jpg"),
    TEAM_LOGO("/team_logo.png"),
    APP_LOGO("/silvan_logo_rectangle.png");

    private String path;

    Drawables(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
