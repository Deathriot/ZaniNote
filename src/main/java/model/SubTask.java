package model;

public class SubTask extends Task{
    // Айди своей суперЗадачи
    private int masterId;
    public SubTask(String title, String text) {
        super(title, text);
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    @Override
    public String toString(){
        return super.toString() + "\n" + "id суперЗадачи: " + masterId + "\n";
    }
}
