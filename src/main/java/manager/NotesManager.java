package manager;

import model.Note;

import java.util.List;

// Менеджер заметок так же должен оперировать айдишникками
public interface NotesManager {
    void createNote(); // Создаем пустую заметку
    void updateTitle(int noteId, String title); // Добавить и обновить заголовок - это одно и то же, так что метод один
    void updateText(int noteId, String text); // То же самое с текстом
    void delete(int noteId); // Удаляем заметку
    List<Note> getNotes(); // Получаем все заметки
    List<Note> search(String charSequence);

}
