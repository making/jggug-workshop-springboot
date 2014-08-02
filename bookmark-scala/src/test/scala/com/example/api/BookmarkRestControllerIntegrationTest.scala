package com.example.api

import java.util.{Arrays, Collections, List}

import com.example.AppConfig
import com.example.domain.Bookmark
import com.example.repository.BookmarkRepository
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.boot.test.{IntegrationTest, SpringApplicationConfiguration, TestRestTemplate}
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.{HttpEntity, HttpMethod, HttpStatus, ResponseEntity}
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate

@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(classes = Array(classOf[AppConfig]))
@WebAppConfiguration
@IntegrationTest(Array("server.port:0", "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE"))
class BookmarkRestControllerIntegrationTest {
  @Autowired
  var bookmarkRepository: BookmarkRepository = _
  @Value("${local.server.port}")
  var port: Int = _
  var apiEndpoint: String = null
  var restTemplate: RestTemplate = new TestRestTemplate
  var springIO: Bookmark = _
  var springBoot: Bookmark = _

  @Before
  def setUp: Unit = {
    bookmarkRepository.deleteAll
    springIO = new Bookmark
    springIO.setName("Spring IO")
    springIO.setUrl("http://spring.io")
    springBoot = new Bookmark
    springBoot.setName("Spring Boot")
    springBoot.setUrl("http://projects.spring.io/spring-boot")
    bookmarkRepository.save(Arrays.asList(springIO, springBoot))
    apiEndpoint = "http://localhost:" + port + "/api/bookmarks"
  }

  @Test
  def testGetBookmarks: Unit = {
    val response: ResponseEntity[List[Bookmark]] = restTemplate.exchange(apiEndpoint, HttpMethod.GET, null
      , new ParameterizedTypeReference[List[Bookmark]] {
      })
    assertThat(response.getStatusCode, is(HttpStatus.OK))
    assertThat(response.getBody.size, is(2))
    val bookmark1: Bookmark = response.getBody.get(0)
    assertThat(bookmark1.getId, is(springIO.getId))
    assertThat(bookmark1.getName, is(springIO.getName))
    assertThat(bookmark1.getUrl, is(springIO.getUrl))
    val bookmark2: Bookmark = response.getBody.get(1)
    assertThat(bookmark2.getId, is(springBoot.getId))
    assertThat(bookmark2.getName, is(springBoot.getName))
    assertThat(bookmark2.getUrl, is(springBoot.getUrl))
  }

  @Test
  def testPostBookmarks: Unit = {
    val google: Bookmark = new Bookmark
    google.setName("Google")
    google.setUrl("http://google.com")
    val response: ResponseEntity[Bookmark] = restTemplate.exchange(apiEndpoint, HttpMethod.POST
      , new HttpEntity[Bookmark](google), classOf[Bookmark])
    assertThat(response.getStatusCode, is(HttpStatus.CREATED))
    val bookmark: Bookmark = response.getBody
    assertThat(bookmark.getId, is(notNullValue))
    assertThat(bookmark.getName, is(google.getName))
    assertThat(bookmark.getUrl, is(google.getUrl))
    assertThat(restTemplate.exchange(apiEndpoint, HttpMethod.GET, null,
      new ParameterizedTypeReference[List[Bookmark]] {
      }).getBody.size, is(3))
  }

  @Test
  def testDeleteBookmark: Unit = {
    val response: ResponseEntity[Void] = restTemplate.exchange(apiEndpoint + "/{id}", HttpMethod.DELETE, null
      , classOf[Void], Collections.singletonMap("id", springIO.getId))
    assertThat(response.getStatusCode, is(HttpStatus.NO_CONTENT))
    assertThat(restTemplate.exchange(apiEndpoint, HttpMethod.GET, null, new ParameterizedTypeReference[List[Bookmark]] {
    }).getBody.size, is(1))
  }

}
