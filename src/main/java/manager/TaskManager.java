package manager;

import model.Note;
import model.Task;

import java.util.List;

// Интерфейс менеджера требует айдишник задачи, чтобы с ней можно было работать
// По-другому очевидно никак
public interface TaskManager {
    void createEmptyTask(); // Создать задачу с полями по дефолту
    void createTask(String title); // Создать задачу уже как хочешь
    void updateTitle(int taskId, String title); // Редактируем заголовок
    void addTask(int taskId); // Добавляем задачу к суперу или другой простой задачи
    void updateDone(int taskId); // Меняем статус задачи на противоположный
    void delete(int taskId); // Удаляем задачу
    List<Task> getTasks(); // Получаем все задачи
    List<Task> search(String charSequence);
}
