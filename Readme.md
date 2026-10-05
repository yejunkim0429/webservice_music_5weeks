학번:22400132
이름:김예준

노래 관리 REST API

프로젝트 소개

노래 정보를 등록, 조회, 수정, 삭제하는 프로젝트이다. 관리하는 데이터는 id, 제목 title, 가수 artist, 앨범 album, 장르 genre, 발매 연도 releaseYear이다. id는 등록할 때 자동으로 생성된다.

songcontroller는 요청을 받고, songservice는 데이터를 처리한다. songrepository는 저장 기능을 정의하는 인터페이스이며, memoryrepository는 LinkedHashMap에 노래를 저장한다. Song은 노래 데이터이고, request와 songresponse는 요청과 응답에 사용하는 DTO이다.

로컬에서는 IntelliJ에서 MusicApiApplication의 main 메서드를 실행한다. 터미널에서 ./gradlew bootRun으로 실행할 수도 있다. 기본 포트는 8080이다.

POST /api/songs는 노래 등록이다.
GET /api/songs는 전체 조회이다.
GET /api/songs/{id}는 단건 조회이다.
PUT /api/songs/{id}는 수정이다.
DELETE /api/songs/{id}는 삭제이다.
GET /api/songs?title=검색어는 제목 검색이다.

등록 요청 예시는 다음과 같다.

{"title":"Render Song","artist":"Student Band","album":"Deployment Test","genre":"POP","releaseYear":2026}

등록 응답 예시는 다음과 같다.

{"id":1,"title":"Render Song","artist":"Student Band","album":"Deployment Test","genre":"POP","releaseYear":2026}

개인 GitHub 저장소는(https://github.com/yejunkim0429/webservice_music_5weeks)이다.

배포 주소는 (https://webservice-music-5weeks.onrender.com)이다.

Organization 저장소 주소는 업로드 후 추가할 예정이다.

개발환경과 Dependency

IDE는 IntelliJ IDEA이다. JDK는 Eclipse Temurin 17.0.20.1이며, Spring Boot는 4.1.1, Gradle은 9.7.1이다. 데이터 저장에는 LinkedHashMap을 사용하였다. 배포 환경은 Docker를 사용하는 Render Free Web Service이다.

spring-boot-starter-webmvc는 REST API를 만들고 JSON 요청과 응답을 처리하기 위해 사용하였다. 프로젝트 생성 시 포함된 spring-boot-starter-test와 junit-platform-launcher는 자동화 테스트를 위한 Dependency이다. 이번 기능 확인은 curl로 진행하였다.

Solution 분석

질문 1. 등록 요청은 어떤 순서로 처리되는가?

BookController.create가 요청을 받고 BookService.create를 호출한다. Service는 BookRepository.save를 호출하고, 실제 저장은 MemoryBookRepository.save가 담당한다.

질문 2. BookRequest, Book, BookResponse의 역할은 무엇인가?

BookRequest는 등록과 수정 요청을 받는 DTO이다. Book은 저장하고 수정하는 Domain 객체이다. BookResponse는 응답 DTO이다. BookController.create에서 요청을 받고, BookService.create에서 Book을 만든 뒤 toResponse로 응답을 변환한다.

질문 3. 새 데이터의 id는 어디에서 만들어지는가?

MemoryBookRepository.save에서 sequence를 증가시키고 Book.setId로 id를 지정한다. 이후 Map에 저장한다.

질문 4. 존재하지 않는 id는 어떻게 404가 되는가?

BookService.findBook에서 BookRepository.findById의 결과가 없으면 ResponseStatusException을 발생시킨다. 상태는 NOT_FOUND이다. findById, update, delete가 findBook을 사용한다.

질문 5. Domain 객체는 어떻게 Response DTO로 변환되는가?

BookService.toResponse에서 Book의 getId, getTitle, getAuthor, getPrice로 값을 가져와 BookResponse를 생성한다.

개발 과정

첫째, IntelliJ에서 Java 17과 Gradle을 사용하는 Spring Boot 프로젝트를 생성하고 Spring Web을 선택하였다. MusicApiApplication을 실행하여 서버가 시작되는 것을 확인하고 GitHub에 연결하였다.

둘째, Song 클래스에 필드, 생성자, getter와 setter를 작성하였다. request와 songresponse를 record로 작성하여 요청에는 id가 없고 응답에는 id가 포함되도록 하였다.

셋째, songrepository 인터페이스와 memoryrepository를 작성하였다. save에서 id를 생성하고, findAll, findById, update, deleteById에서 Collection을 사용하도록 하였다.

넷째, songservice와 songcontroller에 CRUD 기능을 작성하였다. curl로 등록, 전체 조회, 단건 조회, 수정, 수정 결과 조회, 삭제를 확인하였다. 삭제한 id의 조회, 수정, 삭제에서는 모두 404를 확인하였다.

다섯째, 입력 검증과 제목 검색을 추가하고 테스트하였다. 이후 Dockerfile을 작성하고 로컬 컨테이너 실행과 Render 배포를 진행하였다.

기능 수정과 확장

잘못된 데이터가 저장되는 것을 막기 위해 songservice.validate를 작성하였다. create와 update에서 검증하도록 하였다. 제목과 가수가 null이거나 공백이면 400을 반환한다. 발매 연도가 null이거나 0 이하인 경우에도 400을 반환한다.

정상 데이터 등록은 201을 반환하였다. 제목을 공백으로 바꾼 등록 요청은 예상대로 400을 반환하였다. 발매 연도를 0으로 바꾼 수정 요청도 400을 반환하였고, 다시 조회하여 기존 값이 유지되는 것을 확인하였다.

원하는 노래를 찾기 위해 제목 검색을 추가하였다. songcontroller.findAll에서 title을 받고, songservice.searchByTitle에서 제목에 검색어가 포함된 노래를 찾아 songresponse로 반환한다.

Campus Song과 Night Drive를 등록한 뒤 title=Campus로 조회하였다. 예상대로 200과 Campus Song만 포함된 배열이 반환되었다. title=XYZ로 조회한 결과는 200과 빈 배열 []이었다.

배포 과정

Dockerfile에서 Gradle로 JAR를 빌드하고 Java 17로 실행하도록 작성하였다. application.properties에는 server.port=${PORT:8080}을 추가하여 Render의 포트를 사용하도록 하였다.

docker build -t music-api . 명령어로 로컬 빌드를 확인하고 변경 내용을 GitHub에 Push하였다. Render에서 GitHub 저장소를 연결하여 Web Service로 배포하였다. 상태가 Live로 변경되었으며, 별도의 배포 오류는 발생하지 않았다.

배포 주소에서 POST /api/songs로 Render Song을 등록하였다. 실제 응답은 201이며 id가 1로 생성되었다. GET /api/songs는 200과 등록한 노래가 포함된 배열을 반환하였다.

GET /api/songs?title=Render는 200과 Render Song을 반환하였다. GET /api/songs?title=XYZ는 200과 빈 배열 []을 반환하였다.

데이터는 메모리에 저장하므로 서버가 재시작되면 사라진다.

Weekly Report

Key Learning

Controller, Service, Repository가 맡는 역할과 요청 처리 순서를 이해하였다. Request DTO와 Response DTO를 나누는 이유를 알게 되었다. 입력값과 데이터 존재 여부에 따라 400과 404를 반환하는 방법을 배웠다.

Problem & Solution

Controller를 작성하고 컴파일했지만 전체 조회에서 404가 발생하였다. 수정 전 서버가 계속 실행 중이었기 때문이다. 기존 프로세스를 종료하고 다시 실행한 뒤 200과 빈 배열이 반환되는 것을 확인하였다.

Code Review

songservice.update는 findSong으로 노래를 찾고 validate로 요청값을 검사한다. 검증이 끝난 뒤 필드를 변경하고 Repository에 반영한다. 마지막으로 toResponse를 통해 응답 DTO를 반환한다. 검증을 먼저 하므로 잘못된 수정 요청이 기존 데이터를 변경하지 않는다.

AI Usage

AI에 계층별 역할, DTO 작성, CRUD 구현, 오류 원인, Docker와 Render 배포 방법을 질문하였다. 제안받은 코드를 참고하여 직접 입력하였고, for문과 if문을 사용하는 방식으로 구현하였다. curl로 CRUD, 400, 404, 검색 결과와 배포 후 응답을 확인하였다.

Reflection

현재는 서버를 재시작하면 데이터가 사라진다. 앞으로 Database에 데이터를 저장하는 방법과 자동화 테스트 작성 방법을 공부하고 싶다.

건의사항

Docker 이미지 생성과 외부 서버 배포의 차이를 예시와 함께 설명해 주면 배포 과정을 이해하는 데 도움이 될 것 같다.