package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.StatsController;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuStatistics {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    StatsController statsController;
    public MenuStatistics(){ statsController = new StatsController(); }

    public void printStatisticsMenu() {
        int choice;
        while (isRunning) {
            System.out.println("Statistics Menu");
            statsController.generateStats();
            System.out.println("1. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Error, please try again");
                    break;
            }

        }

    }
}
