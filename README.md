﻿# blogkimscafe

kimscafe gradle 이후로 작업중이다
원래 완성하고 블로그에올리려고 했는데 
그러면 너무 오래걸릴거같아서 이다


목표

User
아이디 중복검사
회원가입
로그인
비밀번호 변경
비밀번호찾기
oauth2로그인

board
글쓰기
글수정
글삭제
댓글등록
댓글삭제
댓글수정

reservation
예약 등록/수정/취소
예약페이지
이전 예약내역 출력
시간별예약
날짜별예약

인증
이메일 인증
전화인증
jwt토큰
csrf토큰

프론트
j쿼리를 최대한 안쓰고 바닐라 js로 구현중
thymeleaf사용중 
예약 시스템에는 j쿼리 사용예정
백엔드에 집중하고 싶어서 막히는부분은
j쿼리로 풀고 나가려고 계획

백엔드
구현
User
아이디 중복검사
-xhlhttprequest이용해 비동기 검사
회원가입
-@vaild를 이용해 유효성검사
로그인
-시큐리티를 이용해 로그인
비밀번호 변경
비밀번호찾기
-이메일로 전송해주는형식
oauth2로그인

시큐리티를 이용해 최대한 구현중
dto/vo를 일부로 같이 사용하면서 
개념을 잡고있다 

board
글쓰기
글수정

-json을 익숙해 지기위해
최대한 json형식으로 주고 받는중
재밌는건 map/jsonobject둘다 json으로
보낼수있다

comment
댓글등록
댓글삭제
댓글수정

reservation

인증
이메일인증
-JavaMailSender 이용해 전송




