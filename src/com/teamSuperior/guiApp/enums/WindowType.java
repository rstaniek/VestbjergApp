package com.teamSuperior.guiApp.enums;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public enum WindowType {
    EMP_STATS("/com/teamSuperior/guiApp/layout/empStatistics.fxml", "Statistics", true),
    EMP_MANAGEMENT("/com/teamSuperior/guiApp/layout/empManagement.fxml", "Manage Employees", false),
    SETTINGS("/com/teamSuperior/guiApp/layout/settingsWindow.fxml", "Settings", false);


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
