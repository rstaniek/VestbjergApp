package com.teamSuperior.guiApp.GUI;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class Window {
    public static void inflate(window wnd){
        //TODO: inflate window
    }
}

enum window{
    EMP_STATS("../layout/empStatistics.fxml", "Statistics", false),
    SETTINGS("../layout/settingsWindow.fxml", "Settings", false);


    private String layoutPath, wndTitle;
    private boolean resizable;
    window(String layoutPath, String wndTitle, boolean resizable){
        this.layoutPath = layoutPath;
        this.wndTitle = wndTitle;
        this.resizable = resizable;
    }

    public String getLayoutPath() {
        return layoutPath;
    }

    public String getWndTitle() {
        return wndTitle;
    }

    public boolean isResizable() {
        return resizable;
    }
}
