package com.example.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.example.App;
import com.example.domain.Bookmark;
import com.example.repository.BookmarkRepository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({ "server.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE" })
public class BookmarkRestControllerIntegrationTest {
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Value("${local.server.port}")
    int port;
    String apiEndpoint;
    RestTemplate restTemplate = new TestRestTemplate();
    Bookmark springIO;
    Bookmark springBoot;

    @Before
    public void setUp() {
        bookmarkRepository.deleteAll();
        springIO = new Bookmark();
        springIO.setName("Spring IO");
        springIO.setUrl("http://spring.io");
        springBoot = new Bookmark();
        springBoot.setName("Spring Boot");
        springBoot.setUrl("http://projects.spring.io/spring-boot");

        bookmarkRepository.save(Arrays.asList(springIO, springBoot));
        apiEndpoint = "http://localhost:" + port + "/api/bookmarks";
    }

    @Test
    public void testGetBookmarks() throws Exception {
        ResponseEntity<List<Bookmark>> response = restTemplate.exchange(
                apiEndpoint, HttpMethod.GET, null /* body,header */,
                new ParameterizedTypeReference<List<Bookmark>>() {
                });
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().size(), is(2));

        Bookmark bookmark1 = response.getBody().get(0);
        assertThat(bookmark1.getId(), is(springIO.getId()));
        assertThat(bookmark1.getName(), is(springIO.getName()));
        assertThat(bookmark1.getUrl(), is(springIO.getUrl()));

        Bookmark bookmark2 = response.getBody().get(1);
        assertThat(bookmark2.getId(), is(springBoot.getId()));
        assertThat(bookmark2.getName(), is(springBoot.getName()));
        assertThat(bookmark2.getUrl(), is(springBoot.getUrl()));
    }

    @Test
    public void testPostBookmarks() throws Exception {
        Bookmark google = new Bookmark();
        google.setName("Google");
        google.setUrl("http://google.com");

        ResponseEntity<Bookmark> response = restTemplate.exchange(apiEndpoint,
                HttpMethod.POST, new HttpEntity<>(google), Bookmark.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Bookmark bookmark = response.getBody();
        assertThat(bookmark.getId(), is(notNullValue()));
        assertThat(bookmark.getName(), is(google.getName()));
        assertThat(bookmark.getUrl(), is(google.getUrl()));

        assertThat(
                restTemplate
                        .exchange(
                                apiEndpoint,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Bookmark>>() {
                                }).getBody().size(), is(3));
    }

    @Test
    public void testDeleteBookmarks() throws Exception {
        ResponseEntity<Void> response = restTemplate.exchange(apiEndpoint
                + "/{id}", HttpMethod.DELETE, null /* body,header */,
                Void.class, Collections.singletonMap("id", springIO.getId()));
        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));

        assertThat(
                restTemplate
                        .exchange(
                                apiEndpoint,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Bookmark>>() {
                                }).getBody().size(), is(1));
    }
}
