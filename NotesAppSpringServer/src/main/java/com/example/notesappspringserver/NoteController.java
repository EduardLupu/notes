package com.example.notesappspringserver;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class NoteController {

    private NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/notes")
    public List<Note> retrieveAllNotes() {
        System.out.println("GET ALL");
        return noteRepository.findAll();
    }

    @GetMapping("/notes/{id}")
    public Note retrieveNoteById(@PathVariable Integer id) {
        System.out.println("GET NOTE");
        Optional<Note> noteOptional = noteRepository.findById(id);
        return noteOptional.orElse(null);
    }

    @PostMapping("/notes")
    public Note createNote(@RequestBody Note note) {
        System.out.println("POST NOTE" + note.getId());
        return noteRepository.save(note);
    }

    @DeleteMapping("/notes/{id}")
    public Note deleteNote(@PathVariable Integer id) {
        System.out.println("DELETE");
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            return null;
        }

        noteRepository.deleteById(id);
        return noteOptional.get();
    }

    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable Integer id, @RequestBody Note note) {
        System.out.println("PUT");
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isEmpty()) {
            return null;
        }
        Note noteToUpdate = noteOptional.get();
        noteToUpdate.setId(note.getId());
        noteToUpdate.setTitle(note.getTitle());
        noteToUpdate.setContent(note.getContent());
        noteToUpdate.setUpdatedAt(note.getUpdatedAt());
        noteToUpdate.setCreatedAt(note.getCreatedAt());
        return noteRepository.save(noteToUpdate);
    }
}
