package com.example.service

import java.lang.Long

import com.example.domain.Bookmark
import com.example.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookmarkService @Autowired()(val bookmarkRepository: BookmarkRepository) {

  def findAll = {
    bookmarkRepository.findAll(new Sort(Direction.ASC, "id"))
  }

  def save(bookmark: Bookmark) = {
    bookmarkRepository.save(bookmark)
  }

  def delete(id: Long) = {
    bookmarkRepository.delete(id)
  }
}
