package com.teamSuperior.guiApp.enums;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public enum WindowType {
    EMP_STATS("../layout/empStatistics.fxml", "Statistics", true),
    EMP_MANAGEMENT("../layout/empManagement.fxml", "Manage Employees", true),
    SETTINGS("../layout/settingsWindow.fxml", "Settings", false);


    private String layoutPath, wndTitle;
    private boolean resizable;

    WindowType(String layoutPath, String wndTitle, boolean resizable) {
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
