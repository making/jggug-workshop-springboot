package com.example.web;

import com.example.domain.Bookmark;
import com.example.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("bookmark")
public class BookmarkController {
    @Autowired
    BookmarkService bookmarkService;

    @ModelAttribute
    Bookmark setUp() {
        Bookmark bookmark = new Bookmark();
        return bookmark;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    String list(Model model) {
        List<Bookmark> bookmarks = bookmarkService.findAll();
        model.addAttribute("bookmarks", bookmarks);
        return "bookmark/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated Bookmark bookmark, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return list(model);
        }
        bookmarkService.save(bookmark);
        return "redirect:/bookmark/list";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    String delete(@RequestParam("id") Long id) {
        bookmarkService.delete(id);
        return "redirect:/bookmark/list";
    }
}
