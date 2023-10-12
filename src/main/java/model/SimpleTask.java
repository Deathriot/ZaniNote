package model;

// Обычная задача - ничего интересного
public class SimpleTask extends Task{
    public SimpleTask(String title) {
        super(title, TaskType.SIMPLE);
    }

    public SimpleTask(){
        super(TaskType.SIMPLE);
    }

    @Override
    public String toString(){
        return super.toString() + "\n";
    }
}
