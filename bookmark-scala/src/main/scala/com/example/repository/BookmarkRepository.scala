package com.example.repository

import java.lang.Long

import com.example.domain.Bookmark
import org.springframework.data.jpa.repository.JpaRepository

trait BookmarkRepository extends JpaRepository[Bookmark, Long] {

}
