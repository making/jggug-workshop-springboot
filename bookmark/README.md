# Spring Boot Handson App

## Run application

    $ mvn spring-boot:run

## REST API
### Create bookmarks

    $ curl http://localhost:8080/api/bookmarks -v -X POST -H 'Content-Type:application/json' -d '{"name":"Google", "url":"http://google.com"}'
    * Adding handle: conn: 0x7fa3f4004000
    * Adding handle: send: 0
    * Adding handle: recv: 0
    * Curl_addHandleToPipeline: length: 1
    * - Conn 0 (0x7fa3f4004000) send_pipe: 1, recv_pipe: 0
    * About to connect() to localhost port 8080 (#0)
    *   Trying ::1...
    * Connected to localhost (::1) port 8080 (#0)
    > POST /api/bookmarks HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    > Content-Type:application/json
    > Content-Length: 44
    >
    * upload completely sent off: 44 out of 44 bytes
    < HTTP/1.1 201 Created
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Type: application/json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Sat, 12 Jul 2014 06:44:08 GMT
    <
    * Connection #0 to host localhost left intact
    {"id":1,"name":"Google","url":"http://google.com"}
    
### List bookmarks

    $ curl http://localhost:8080/api/bookmarks -v -X GET
    * Adding handle: conn: 0x7fbfd200aa00
    * Adding handle: send: 0
    * Adding handle: recv: 0
    * Curl_addHandleToPipeline: length: 1
    * - Conn 0 (0x7fbfd200aa00) send_pipe: 1, recv_pipe: 0
    * About to connect() to localhost port 8080 (#0)
    *   Trying ::1...
    * Connected to localhost (::1) port 8080 (#0)
    > GET /api/bookmarks HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    >
    < HTTP/1.1 200 OK
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Type: application/json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Sat, 12 Jul 2014 06:47:55 GMT
    <
    * Connection #0 to host localhost left intact
    [{"id":1,"name":"Google","url":"http://google.com"}]

### Delete a bookmark

    $ curl http://localhost:8080/api/bookmarks/1 -v -X DELETE
    * Adding handle: conn: 0x7fbb3980ca00
    * Adding handle: send: 0
    * Adding handle: recv: 0
    * Curl_addHandleToPipeline: length: 1
    * - Conn 0 (0x7fbb3980ca00) send_pipe: 1, recv_pipe: 0
    * About to connect() to localhost port 8080 (#0)
    *   Trying ::1...
    * Connected to localhost (::1) port 8080 (#0)
    > DELETE /api/bookmarks/1 HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    >
    < HTTP/1.1 204 No Content
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Date: Sat, 12 Jul 2014 06:48:24 GMT
    <
    * Connection #0 to host localhost left intact
    
### Sample Client App for REST API
    
Go to [sample app on JSFIDDLE](http://jsfiddle.net/Ca2g2/).

## Traditinal Web App

Access [http://localhost:8080/bookmark/list](http://localhost:8080/bookmark/list).

## Enable spring security

    $ mvn spring-boot:run -Drun.arguments="--bookmark.security.enabled=true"