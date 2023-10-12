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
    //Тип задачи, нужен чтоб проще было определять собсна тип задачи, а также необходим для сериализации
    private final TaskType type;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - hh:mm");

    public Task(String title, TaskType type) {
        this.type = type;
        this.title = title;
        done = false; // Нахер нам писать уже сделанную задачу?
        creationTime = LocalDateTime.now(); // Ну вот создали прям щас
    }

    public Task(TaskType type){
        this.type = type;
        this.done = false;
        this.creationTime = LocalDateTime.now();
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

    public TaskType getType() {
        return type;
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
        return "\n" + getTaskName(type) + " " + + id + "\n" +
                "Заголовок: " + title + "\n" +
                "Сделана ли: " + done + "\n" +
                "Дата создания: " + creationTime.format(formatter) + "\n";
    }

    private String getTaskName(TaskType type){
        switch (type){
            case SIMPLE:
                return "Обычная задача";
            case SUB:
                return "Саб задача";
            case SUPER:
                return "Супер задача";
        }

        throw new IllegalArgumentException("Это ваще как эту ошибку можно получить, поехал совсем? " +
                "Такой задачи физически не может быть");
    }
}
