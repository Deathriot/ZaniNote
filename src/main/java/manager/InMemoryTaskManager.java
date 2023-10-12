package manager;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    // Свободный айдишник
    protected int nextId = 1;

    // Храним все типы тасков в мапах по ключу - их айди
    protected final Map<Integer, SimpleTask> simpleTasks = new TreeMap<>();
    protected final Map<Integer, SubTask> subTasks = new TreeMap<>();
    protected final Map<Integer, SuperTask> superTasks = new TreeMap<>();

    @Override
    public void createEmptyTask() {
        SimpleTask task = new SimpleTask("Заголовок");
        task.setId(nextId++);

        simpleTasks.put(task.getId(), task);
    }

    @Override
    public void createTask(String title) {
        if (title == null) {
            title = "";
        }

        SimpleTask task = new SimpleTask(title);
        task.setId(nextId++);

        simpleTasks.put(task.getId(), task);
    }

    @Override
    public void updateTitle(int taskId, String title) {
        if (title == null) {
            title = "";
        }

        Task task = findTask(taskId);
        task.setTitle(title);
    }

    @Override
    public void addTask(int taskId) {
        Task task = findTask(taskId);

        // Создаем новый саб
        SubTask newSub = new SubTask("Новый саб");
        newSub.setId(nextId++);

        switch (task.getType()) {
            case SUPER:
                // Если прикрепляем уже с супер таску - просто добавляем в него задачу
                SuperTask superTask = (SuperTask) task;
                superTask.addSub(newSub);
                superTask.setDone(false); // меняем статус на false тк очевидно новая задача не сделана

                newSub.setMasterId(superTask.getId());

                subTasks.put(newSub.getId(), newSub);
                break;

            case SUB:
                nextId--; // Небольшой костыль, возвращаем обратно свободный айдишник,
                // можно этого не делать, просто так красиво

                // Прикреплять к сабу другой саб наверное и можно в теории, но нахер надо, будет кринж какой-то
                throw new IllegalArgumentException("Не, рекурсии нам тут не нужны");

            case SIMPLE:
                // Если мы прикрепляем саб к обычной задачи начинается тарас-переход
                simpleTasks.remove(taskId); // Удаляем искомую задачу из списка обычных задач

                // Создаем суперЗадачу новую, все по тз (дебильному)
                SuperTask newSuper = new SuperTask("СуперЗадача");
                newSuper.setId(nextId++);

                // А вот и транс переход нашей задачи - превращаем ее в саб, все копируя
                SubTask transTask = new SubTask(task.getTitle());
                transTask.setId(task.getId());
                transTask.setDone(task.isDone());
                transTask.setCreationTime(task.getCreationTime());
                transTask.setMasterId(newSuper.getId());

                newSub.setMasterId(newSuper.getId());

                // Добавляем к нашей новой суперЗадаче новые сабы и пихаем в мапу
                newSuper.addSub(transTask);
                newSuper.addSub(newSub);
                superTasks.put(newSuper.getId(), newSuper);

                // Пихаем новые сабы в свою мапу
                subTasks.put(newSub.getId(), newSub);
                subTasks.put(transTask.getId(), transTask);
                break;
        }
    }

    @Override
    public void updateDone(int taskId) {
        Task task = findTask(taskId);
        task.setDone(!task.isDone()); // просто меняем статус задачи, типа кнопки - тык тык и она туда сюда

        switch (task.getType()) {
            case SIMPLE:
                return; // Это я придумал, просто показываю что если задача обычная - ничего не делаем

            case SUB:
                // Если меняем статус саба - проверяем сразу статус суперЗадачи
                // В тз такого не было, но думаю если все сабы у суперЗадачи сделаны - она тоже должна быть сделана
                SubTask subTask = (SubTask) task;

                SuperTask superTask = superTasks.get(subTask.getMasterId());
                checkSuperTaskStatus(superTask);
                return;

            case SUPER:
                SuperTask superTask1 = (SuperTask) task;
                // Тут уже интереснее - если переключаем статус супера
                // Предлагаю всем сабам ставить true если переключаем что супер сделан
                // Однако если вручную мы выставим false - переключать ничего не будем, так вот пользователь решил типа

                if (superTask1.isDone()) {
                    for (SubTask sub : superTask1.getSubs()) {
                        sub.setDone(true);
                    }
                }
        }
    }

    @Override
    public void delete(int taskId) {
        Task task = findTask(taskId);

        switch(task.getType()){
            case SIMPLE:
                simpleTasks.remove(taskId);
                return;

            case SUB:
                SubTask sub = subTasks.remove(taskId);

                SuperTask superTask = superTasks.get(sub.getMasterId());
                superTask.deleteSub(sub);

                // Если у супера нет сабов - превращаем его обратно в обычную задачу, все по тз
                if (superTask.getSubs().isEmpty()) {
                    SimpleTask transTask = new SimpleTask(superTask.getTitle());
                    transTask.setId(superTask.getId());
                    transTask.setDone(superTask.isDone());
                    transTask.setCreationTime(superTask.getCreationTime());

                    superTasks.remove(superTask.getId());

                    simpleTasks.put(transTask.getId(), transTask);
                    return;
                }

                // После удаления саба смотрим не меняется ли статус супера
                checkSuperTaskStatus(superTask);
                return;

            case SUPER:
                SuperTask superTask1 = (SuperTask) task;

                //Удаляем все сабы у супера, иначе никак (можно сделать чтобы все сабы стали обычными задачами, но это чушь)
                for (SubTask subtask : superTask1.getSubs()) {
                    subTasks.remove(subtask.getId());
                }

                superTasks.remove(superTask1.getId());
                return;
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(simpleTasks.values()); // Для удобства сразу суем простые задачи в список
        tasks.addAll(superTasks.values());

        // Не возвращаем сабы, потому что они и так хранятся в суперах

        return tasks.stream().sorted((task1, task2) -> {
            if (task1.getCreationTime().isAfter(task2.getCreationTime())) {
                return 1;
            }

            if (task1.getCreationTime().isBefore(task2.getCreationTime())) {
                return -1;
            }

            return 0;
        }).collect(Collectors.toList()); // Сортируем по дате создания

    }

    @Override
    public List<Task> search(String charSequence) {
        List<Task> tasks = new ArrayList<>(simpleTasks.values());
        tasks.addAll(superTasks.values());
        tasks.addAll(subTasks.values());

        Set<Task> foundTask = new HashSet<>(); // Используем сет чтобы не париться с повторением задач

        for (Task task : tasks) {
            if (task.getTitle().contains(charSequence)) {

                // если по поиску найден саб - возвращаем его суперзадачу, так круче
                if (task instanceof SubTask) {
                    foundTask.add(superTasks.get(((SubTask) task).getMasterId()));
                } else {
                    foundTask.add(task);
                }
            }
        }

        return new ArrayList<>(foundTask);
    }

    // Можно сказать что костыль, но времени он не жрет, можно оптимизировать, сам предлагай как лучше
    private Task findTask(int id) {
        if (simpleTasks.containsKey(id)) {
            return simpleTasks.get(id);
        }

        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }

        if (superTasks.containsKey(id)) {
            return superTasks.get(id);
        }

        throw new IllegalArgumentException("Такой задачи не существует");
    }

    // Проверяем статус суперЗадачи - если в ней хоть какой-то саб не сделан - суперЗадача еще не сделана и наоборот
    private void checkSuperTaskStatus(SuperTask superTask) {
        for (SubTask sub : superTask.getSubs()) {
            if (!sub.isDone()) {
                superTask.setDone(false);
                return;
            }

            superTask.setDone(true);
        }
    }
}
