package com.enotes.Enotes.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.enotes.Enotes.entity.Notes;
import com.enotes.Enotes.entity.User;

public interface NotesService {
    public Notes saveNotes(Notes notes);
    public Notes getNotesById(int id);
    public Page<Notes> getNotesByUser(User user,int pageNo);
    public Notes updateNotes(Notes notes);
    public boolean deleteNotes(int id);

}