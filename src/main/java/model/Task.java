package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Абстрактная задача - основа для всех задач
public abstract class Task {
    // Без айди никуда
    private int id;
    // Заголовок
    private String title;
    // Сделано чи не
    private boolean done;
    // Дата создания, пусть будет
    private LocalDateTime creationTime;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - hh:mm");

    public Task(String title) {
        this.title = title;
        done = false; // Нахер нам писать уже сделанную задачу?
        creationTime = LocalDateTime.now(); // Ну вот создали прям щас
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return this.id == task.id; // Айди уникальные хуле
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (done ? 1 : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        return result;
    }

    // Чтоб красиво выглядело, в теории так можно преобразовывать в Json
    @Override
    public String toString() {
        return "Задача номер " + id + "\n" +
                "Заголовок: " + title + "\n" +
                "Сделана ли: " + done + "\n" +
                "Дата создания: " + creationTime.format(formatter) + "\n";
    }
}
