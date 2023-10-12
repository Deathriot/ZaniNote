package org.example;

import model.*;
import manager.*;
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new FileTaskManager();

        manager.createEmptyTask(); // 1 - future sub
        manager.createEmptyTask(); // 2 - future sub
        manager.createEmptyTask(); // 3 - simple

        manager.addTask(1);  // 4 - sub   5 - super
        manager.addTask(5);  // 6 - sub
        manager.addTask(5);  // 7 - sub
        manager.addTask(2);  // 8 - sub  9 - super

        TaskManager manager2 = new FileTaskManager();
        System.out.println(manager2.getTasks());
    }
}