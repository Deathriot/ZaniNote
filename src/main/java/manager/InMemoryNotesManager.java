package manager;

import model.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class InMemoryNotesManager implements NotesManager{
    private final Map<Integer, Note> notes = new TreeMap<>();
    private int nextId = 1;
    @Override
    public void createNote() {
        Note note = new Note();
        note.setId(nextId++);

        notes.put(note.getId(), note);
    }
    // Большая проблема, но наверное решим как разберемся: Как менять текст и заголовок, не удаляя прошлое его значение?
    // Да, можно запоминать прошлое значение и складывать их, но как удалять частично? как редактировать нормально? хз
    @Override
    public void updateTitle(int noteId, String title) {
        Note note = findNote(noteId);

        if(title == null){
            title = "";
        }

        note.setTitle(title);
        note.setChangeTime(LocalDateTime.now()); // Обновляем дату редактирования
    }

    @Override
    public void updateText(int noteId, String text) {
        Note note = findNote(noteId);

        if(text == null){
            text = "";
        }

        note.setText(text);
        note.setSymbCount(text.length());
        note.setChangeTime(LocalDateTime.now()); // Обновляем дату редактирования
    }

    @Override
    public void delete(int noteId) {
        notes.remove(noteId);
    }

    @Override
    public List<Note> getNotes() {
        // Возвращаем список отсортированный по дате редактирования
        return notes.values().stream().sorted((note1 , note2) ->{
            if(note1.getChangeTime().isAfter(note2.getChangeTime())){
                return 1;
            }

            if(note1.getChangeTime().isBefore(note2.getChangeTime())){
                return -1;
            }

            return 0;
        }).collect(Collectors.toList());
    }


    // Не ебу как ты собрался искать нормально, но держи вот
    @Override
    public List<Note> search(String charSequence) {
        List<Note> findNotes = new ArrayList<>();

        for(Note note : notes.values()){
            if(note.getTitle().contains(charSequence) || note.getText().contains(charSequence)){
                findNotes.add(note);
            }
        }

        return findNotes;
    }

    private Note findNote(int id){
        if(!notes.containsKey(id)){
            throw new IllegalArgumentException("Такой заметки не существует");
        }

        return notes.get(id);
    }
}
