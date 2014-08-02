package com.example.service;

import com.example.domain.Bookmark;
import com.example.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookmarkService {
    @Autowired
    BookmarkRepository bookmarkRepository;

    public List<Bookmark> findAll() {
        return bookmarkRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    public Bookmark save(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    public void delete(Long id) {
        bookmarkRepository.delete(id);
    }
}
