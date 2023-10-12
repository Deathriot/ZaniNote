package manager;

import com.google.gson.*;
import model.SimpleTask;
import model.SubTask;
import model.SuperTask;
import model.Task;

import java.io.*;
import java.util.List;

// Здесь реализована логика сохранения и загрузки, для удобства делаем менеджер, наследник обычного
public class FileTaskManager extends InMemoryTaskManager {

    private final Gson gson;
    private final File file; // Храним в поле файл с сохранением
    private final static  String filePath = "src\\save.txt"; // относительный путь к файлу сохранения

    public FileTaskManager() {
        this.gson = new GsonBuilder().serializeNulls().create(); // Красиво печатать нельзя при текущей логики
        this.file = new File(filePath);
        load(); // при создании менеджера сразу все загружаем
    }

    @Override
    public void createEmptyTask() {
        super.createEmptyTask();
        save();
    }

    @Override
    public void createTask(String title) {
        super.createTask(title);
        save();
    }

    @Override
    public void updateTitle(int taskId, String title) {
        super.updateTitle(taskId, title);
        save();
    }

    @Override
    public void addTask(int taskId) {
        super.addTask(taskId);
        save();
    }

    @Override
    public void updateDone(int taskId) {
        super.updateDone(taskId);
        save();
    }

    @Override
    public void delete(int taskId) {
        super.delete(taskId);
        save();
    }

    // Это прототип сохранения, он каждый раз все перезаписывает. На небольших дистанциях это незаметно, но
    // В идеале надо придумать как нормально сохранять
    private void save(){
        try(FileWriter writer = new FileWriter(file)){
            writer.write(gson.toJson(simpleTasks.values()));
            writer.write("\n");
            writer.write(gson.toJson(subTasks.values()));
            writer.write("\n");
            writer.write(gson.toJson(superTasks.values()));

        } catch (IOException ex){
            throw new RuntimeException("Файл сохранения не найден");
        }
    }

    // Загрузку тоже можно оптимизировать
    private void load(){
        int currentNextId = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {

            while(br.ready()){
                String inputArray = br.readLine(); // Читаем все строчку - это массив задач в джсон

                if(inputArray == null || inputArray.isEmpty()){ // Проверяем не пустой ли файл, тогда ничего не читаем
                    return;
                }

                JsonArray jsonTasks = gson.fromJson(inputArray, JsonArray.class);

                for(JsonElement jsonTask: jsonTasks){ // Перебираем этот массив
                    JsonObject parsedTask = jsonTask.getAsJsonObject(); // Получаем элемент массива как объект джсон
                    String taskType = parsedTask.get("type").getAsString(); // Получаем поле типа

                    // Исходя из типа десериализуем и суем в соответствующую мапу
                    switch(taskType){
                        case "SIMPLE":
                            SimpleTask simpleTask = gson.fromJson(parsedTask, SimpleTask.class);
                            simpleTasks.put(simpleTask.getId(), simpleTask);

                            //Так нужно потому что задачи могут быть удалены, а свободный айдишник инкриментируется
                            //Поэтому ищем постоянно максимальное значение
                            currentNextId = Math.max(currentNextId, simpleTask.getId());
                            break;

                        case "SUB":
                            SubTask subTask = gson.fromJson(parsedTask, SubTask.class);
                            subTasks.put(subTask.getId(),subTask);

                            currentNextId = Math.max(currentNextId, subTask.getId());
                            break;

                        case "SUPER":
                            SuperTask superTask = gson.fromJson(parsedTask, SuperTask.class);
                            superTasks.put(superTask.getId(), superTask);

                            currentNextId = Math.max(currentNextId, superTask.getId());
                            break;
                    }
                }
            }
            nextId = ++currentNextId; // Восстанавливаем соответствующий свободный айдишник

        } catch(IOException ex){
            throw new RuntimeException("При чтении файла произошла ошибка");
        }
    }
}
