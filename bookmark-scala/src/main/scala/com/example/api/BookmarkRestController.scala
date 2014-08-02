package com.example.api

import java.lang.Long

import com.example.domain.Bookmark
import com.example.service.BookmarkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("api/bookmarks"))
class BookmarkRestController @Autowired()(val bookmarkService: BookmarkService) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def getBookmarks = {
    bookmarkService.findAll
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  @ResponseStatus(HttpStatus.CREATED)
  def postBookmarks(@Validated @RequestBody bookmark: Bookmark) = {
    bookmarkService.save(bookmark)
  }

  @RequestMapping(value = Array("{id}"), method = Array(RequestMethod.DELETE))
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteBookmark(@PathVariable("id") id: Long) = {
    bookmarkService.delete(id)
  }
}
