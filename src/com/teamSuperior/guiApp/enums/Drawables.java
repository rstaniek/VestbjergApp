package com.teamSuperior.guiApp.enums;

/**
 * Created by Domestos on 16.12.06.
 * This enum stores filenames of all drawable objects in the project
 * can be used with Drawable class
 */
public enum Drawables {
    TEST_MEME("/i_want_to_believe.jpg"),
    APP_LOGO("/silvan_logo_rectangle.png"),
    TEAM_LOGO("/team_logo.png"),
    X_MAS_IMAGE("/post-8868-Have-a-fucking-weekend-and-don-YeZz.jpeg"),
    SPLASH("/splash_big.png"),
    ICON("/icon_256.png");

    private String path;

    Drawables(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
