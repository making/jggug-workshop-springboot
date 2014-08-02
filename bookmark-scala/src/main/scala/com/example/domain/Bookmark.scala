package com.example.domain

import java.lang.Long
import javax.persistence.{Entity, GeneratedValue, Id}
import javax.validation.constraints.{NotNull, Size}

import org.hibernate.validator.constraints.URL

import scala.beans.BeanProperty

@Entity
class Bookmark {
  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  @NotNull
  @Size(min = 1, max = 255)
  var name: String = _

  @BeanProperty
  @NotNull
  @Size(min = 1, max = 255)
  @URL
  var url: String = _
}
