# jpashop

* 인프런 강의를 듣고 kotlin으로 변경
* 환경
    * db는 postgres13 사용
        * docker-compose up -d
    * gradle 7.4
    * java 17
    * springboot 2.6.4

## 추가사항

* ExceptionHandler를 이용한 공통 에러처리 추가
* spring-boot-starter-validation를 이용한 에러메세지 처리
* 확장메소드 활용（ex:IOrderRepository.findOrder）
* setter는 protected setter로 변경해서 사용
* ktlint 설정
* domain, api로 나눠서 Gradle Multi Module Project로 변경

## 학습인강

* [실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94/dashboard)
* [실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84/dashboard)
* [실전! Querydsl](https://www.inflearn.com/course/Querydsl-%EC%8B%A4%EC%A0%84/dashboard)


