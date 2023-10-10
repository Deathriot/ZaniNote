package model;

import java.util.List;

// СуперЗадача - хранит в себе еще задачи
public class SuperTask extends Task{
    // Список айдишников саб задач
    private List<Integer> subId;
    // И сами саб задачи, будем хранить и то и другое, чтоб было проще искать
    private List<SubTask> subs;
    public SuperTask(String title, String text) {
        super(title, text);
    }

    // Добавить задачу в список
    public void addSub(SubTask sub){
        subs.add(sub);
        subId.add(sub.getId());
    }

    public void deleteSub(SubTask sub){
        subs.remove(sub);
        subId.remove(sub.getId());
    }
    public List<Integer> getSubId() {
        return subId;
    }

    public List<SubTask> getSubs() {
        return subs;
    }

    public String toString(){
        return super.toString() + "\n" + "Список сабов: " + "\n" + subs.toString() + "\n";
    }
}
