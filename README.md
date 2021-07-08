카페를 예약하는 홈페이지입니다
springboot/gradle을 사용했습니다

설명
DB=mysql

USER
구현기능
회원가입
로그인
마이페이지
비밀번호변경
이메일인증
비밀번호찾기

RESERVATION
당일 예약 시스템
아임포트를 이용한 결제 시스템
(아임포트 api호출)
예약내역 페이지
예약변경/취소가능

BOARD
글쓰기
글검색
글수정
글삭제
조회수
댓글쓰기
댓글삭제
댓글수정

사용언어 

프론트
html/바닐라js/j쿼리/css
-백엔드 위주로 공부하고 싶어서
 자세히는 알지 못하나 최대한 바닐라js로 구성하려했습니다
 하지만 예약하는 부분에서 좀 복잡해서 j쿼리의 도움을 받았습니다
 아이디 중복검사에서 비동기 검사를 보고 너무 신기해서
 대부분 비동기 통신으로 구성했습니다

백엔드
자바/스프링/jpa
-MultipartFile을 이용해서 파일형식을 받을 수 있다는 걸 공부 했습니다
그래서 프론트의 사진을 받았습니다
RestTemplate/HttpHeaders/HttpEntity 를 이용해서 아임포트api를 호출합니다
자바 서버에서 외부 api를 호출하는 방법을 익혔습니다
(물론 회사마다 방법이 약간씩 다른거 같습니다)
@Transactional(rollbackFor = {Exception.class}) 이용해 롤백을 구현했습니다
@RestControllerAdvice를이용해서 예외처리를 했습니다


글작성시 사진은 
로컬 과 db에 http://local~식/로컬경로 이렇게 
두가지 로 저장했습니다
프론트에게 던저줄때
img태그에 넣을 수 있도록 db에는 링크식으로 저장했습니다
public List<boardimagevo> insertImageLocal
public void insertImageToDb

글변경에서 사진은 
일단 기존 사진들의 변화를 감지해야 했습니다
예를들어 처음 사용자가 글을 쓰때 a/b/c라는 사진을 넣었다고 하고
글 수정중 b를 제거한 상황이 발생했습니다
이럴때는 프론트에서 a/c남은 사진만 서버로 주면 
서버에서 글에 해당하는 사진 db로가서 모두가져 온다음
a/b/c중 빠진 사진이 있다면 이경우에는 b가 되겠죠
b를 list에 넣어준다음 db에서 삭제 해주는 형식으로 했습니다
이런방식을 쓰면 모든 사진을 삭제하고 수정하거나
모든사진을 삭제하고 새사진을 넣을때나
기존사진에 변경이 생겼을때 잘 컨트롤 할 수있을 거라고 생각 되어서 만들었습니다
private List<boardimagevo> selectDeleteImage(List<Integer>alreadyimages,int bid) 
그리고 그다음 새사진이 있다면
사진을 글씨기 처럼 추가해주게 만들었습니다
즉 기존사진변화처리->새사진이있다면등록
기존사진/새사진을 따로따로 생각하고 처리했습니다

글 형식은 varchar가아닌 blob사용 해서 
mysql조회시 글내용이 아니라 용량으로 나오게 하고싶은데
아직 구현하지 못했습니다

예외는
@RestControllerAdvice 사용했습니다
restcontroller전역에서 예외를 잡아서 한번에
던저줄 수 있는 기능을 활용했습니다

롤백은 
@Transactional(rollbackFor = {Exception.class}) 사용하였습니다
예를들어
글 작성/수정/삭제중 글은 삭제됐는데 사진 삭제중 오류가 나면 
어떡하지? 이런 생각이 들어서 실험하던중 예외가 발생하면
Transactional 잡아서 롤백 시킨다는 사실을 알았고
적용했습니다 save()에도 적용이 되는걸 발견해서 
자주 사용하게 될거같다라는 생각이 들었습니다

그밖의 자세한 내용은
https://cordingmonster.tistory.com/category/Spring%20boot%20%ED%98%BC%EC%9E%90%20%EB%A7%9B%EB%B3%B4%EA%B8%B0
에서 좀더 자세히 보실 수 있습니다

MYSQL 입니다
| blogboard          |
| blogboardimage     |
| blogcomment        |
| bloghistory        |
| blogreservation    |
| blogseatinfor      |
| blogusers   

회원테이블=blogusers
+------------+--------------+------+-----+---------+----------------+
| Field      | Type         | Null | Key | Default | Extra          |
+------------+--------------+------+-----+---------+----------------+
| id         | int          | NO   | PRI | NULL    | auto_increment |
| created    | datetime     | YES  |     | NULL    |                |
| email      | varchar(30)  | NO   | UNI | NULL    |                |
| emailcheck | varchar(255) | YES  |     | NULL    |                |
| name       | varchar(20)  | NO   |     | NULL    |                |
| provider   | varchar(255) | YES  |     | NULL    |                |
| providerid | varchar(255) | YES  |     | NULL    |                |
| pwd        | varchar(100) | NO   |     | NULL    |                |
| randnum    | varchar(255) | YES  |     | NULL    |                |
| role       | varchar(255) | NO   |     | NULL    |                |
+------------+--------------+------+-----+---------+----------------+

게시글에 대한 테이블=blogboard
+---------+-------------+------+-----+---------+----------------+
| Field   | Type        | Null | Key | Default | Extra          |
+---------+-------------+------+-----+---------+----------------+
| bid     | int         | NO   | PRI | NULL    | auto_increment |
| content | longtext    | NO   |     | NULL    |                |
| created | datetime    | YES  |     | NULL    |                |
| email   | varchar(50) | NO   |     | NULL    |                |
| hit     | int         | NO   |     | 0       |                |
| title   | varchar(30) | NO   |     | NULL    |                |
+---------+-------------+------+-----+---------+----------------+

댓글테이블=blogcomment
+---------+-------------+------+-----+---------+----------------+
| Field   | Type        | Null | Key | Default | Extra          |
+---------+-------------+------+-----+---------+----------------+
| cid     | int         | NO   | PRI | NULL    | auto_increment |
| bid     | int         | NO   |     | NULL    |                |
| comment | varchar(50) | NO   |     | NULL    |                |
| created | datetime    | YES  |     | NULL    |                |
| email   | varchar(50) | NO   |     | NULL    |                |
+---------+-------------+------+-----+---------+----------------+

예약에 관련된 테이블=blogreservation/bloghistory

blogreservation
+---------------------+--------------+------+-----+---------+----------------+
| Field               | Type         | Null | Key | Default | Extra          |
+---------------------+--------------+------+-----+---------+----------------+
| id                  | int          | NO   | PRI | NULL    | auto_increment |
| created             | datetime     | YES  |     | NULL    |                |
| remail              | varchar(255) | NO   |     | NULL    |                |
| rname               | varchar(255) | NO   |     | NULL    |                |
| requesthour         | int          | NO   |     | NULL    |                |
| reservationdatetime | datetime     | NO   |     | NULL    |                |
| seat                | varchar(255) | NO   |     | NULL    |                |
| imp_uid             | varchar(255) | NO   |     | NULL    |                |
+---------------------+--------------+------+-----+---------+----------------+
bloghistory

blogseatinfor
+---------+--------------+------+-----+---------+----------------+
| Field   | Type         | Null | Key | Default | Extra          |
+---------+--------------+------+-----+---------+----------------+
| id      | int          | NO   | PRI | NULL    | auto_increment |
| limited | int          | NO   |     | NULL    |                |
| price   | int          | NO   |     | NULL    |                |
| seat    | varchar(255) | NO   |     | NULL    |                |
+---------+--------------+------+-----+---------+----------------+
예약할때 자리별 인원수/시간별요금을 넣어 놨습니다






