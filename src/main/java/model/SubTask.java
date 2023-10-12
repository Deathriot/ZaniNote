package model;

public class SubTask extends Task{
    // Айди своей суперЗадачи
    private int masterId;
    public SubTask(String title) {
        super(title, TaskType.SUB);
    }

    public SubTask(){
        super(TaskType.SUB);
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    @Override
    public String toString(){
        return super.toString() + "id суперЗадачи: " + masterId + "\n";
    }
}
