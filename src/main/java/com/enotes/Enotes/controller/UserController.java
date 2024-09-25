package com.enotes.Enotes.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enotes.Enotes.entity.Notes;
import com.enotes.Enotes.entity.User;
import com.enotes.Enotes.repository.UserRepository;
import com.enotes.Enotes.service.NotesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private NotesService notesService;

    @ModelAttribute
    public User getUser(Principal p, Model m) {
        String email = p.getName();
        User user = userRepo.findByEmail(email);
        m.addAttribute("user", user);
        return user;
    }

    @GetMapping("/addNotes")
    public String addNotes() {
        return "add_notes"; // View for adding notes
    }

    @GetMapping("/viewNotes")
    public String viewNotes(Model m, Principal p, @RequestParam(defaultValue = "0") Integer pageNo) {
        User user = getUser(p, m);
        Page<Notes> notes = notesService.getNotesByUser(user, pageNo);
        m.addAttribute("currentPage", pageNo);
        m.addAttribute("totalElements", notes.getTotalElements());
        m.addAttribute("totalPages", notes.getTotalPages());
        m.addAttribute("notesList", notes.getContent());
        return "view_notes"; // View for viewing notes
    }

    @GetMapping("/editNotes/{id}")
    public String editNotes(@PathVariable int id, Model m, HttpSession session) {
        Notes notes = notesService.getNotesById(id);
        if (notes == null) {
            session.setAttribute("msg", "Note not found");
            return "redirect:/user/viewNotes"; // Redirect if the note does not exist
        }
        m.addAttribute("notes", notes); // Add the note object to the model for editing
        return "edit_notes"; // View for editing notes
    }

    @PostMapping("/saveNotes")
    public String saveNotes(@ModelAttribute Notes notes, HttpSession session, Principal p, Model m) {
        notes.setDate(LocalDate.now());

        User user = getUser(p, m);
        notes.setUser(user);

        try {
            notesService.saveNotes(notes);
            session.setAttribute("msg", "Notes saved successfully");
        } catch (Exception e) {
            logger.error("Error saving notes", e);
            session.setAttribute("msg", "Something went wrong: " + e.getMessage());
        }

        return "redirect:/user/addNotes"; // Redirect to add notes page
    }

    @PostMapping("/updateNotes")
    public String updateNotes(@ModelAttribute Notes notes, HttpSession session, Principal p, Model m) {
        notes.setDate(LocalDate.now());

        User user = getUser(p, m);
        notes.setUser(user);

        try {
            notesService.saveNotes(notes); // Ensure this method can handle both saving and updating
            session.setAttribute("msg", "Notes updated successfully");
        } catch (Exception e) {
            logger.error("Error updating notes", e);
            session.setAttribute("msg", "Something went wrong: " + e.getMessage());
        }

        return "redirect:/user/viewNotes"; // Redirect to view notes page
    }

    @GetMapping("/deleteNotes/{id}")
    public String deleteNotes(@PathVariable int id, HttpSession session) {
        boolean f = notesService.deleteNotes(id);
        if (f) {
            session.setAttribute("msg", "Notes deleted successfully");
        } else {
            session.setAttribute("msg", "Somethingit dg went wrong");
        }
        return "redirect:/user/viewNotes"; // Redirect to view notes page
    }
}
