package model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Note {
    // Главная залупа
    private int id;
    // Заголовок
    private String title;
    // Содержание
    private String text;
    // Последнее изменение
    private LocalDateTime changeTime;
    // Количество символов
    private int symbCount;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - hh:mm");

    public Note(){
        this.title = "Заголовок";
        this.text = "Текст";
        this.changeTime = LocalDateTime.now();
        this.symbCount = text.length();
    }

    public Note(int id, String title, String text, LocalDateTime changeTime, int symbCount) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.changeTime = changeTime;
        this.symbCount = symbCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }

    public void setSymbCount(int symbCount) {
        this.symbCount = symbCount;
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

    public String getText() {
        return text;
    }

    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public int getSymbCount() {
        return symbCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return this.id == note.id; // Иначе нахер нам нужен айди?
    }

    // Хешкод пусть будет
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (changeTime != null ? changeTime.hashCode() : 0);
        result = 31 * result + symbCount;
        return result;
    }

    @Override
    public String toString() {
        return "Заметка номер " + id + "\n" +
                "Заголовок: " + title + "\n" +
                "Текст: " + text + "\n" +
                "Дата Изменения: " + changeTime.format(formatter) + "\n" +
                "Количество символов: " + symbCount + "\n";
    }
}
