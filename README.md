# FADE - Fashion Democracy

투표를 통해 알아가는 패션 트렌드 서비스 FADE의 백엔드 Repository입니다.  
투표, 사진 업로드, 피드, 구독, 북마크, 알림 기능이 포함된 SNS 플랫폼에서 사용되는 기본적인 API를 경험할 수 있도록 만들어졌습니다.  
매일 가장 많은 투표를 받은 게시글은 패션왕으로 선정되어 월 캘린더에 아카이빙됩니다.

## FADE 서비스 링크
https://fade.swygbro.com/

## 개발 환경
* <img src="https://img.shields.io/badge/Java-3766AB?style=flat-square&logo=Java&logoColor=white"/> version 17
* <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> version 3.3.1
* <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySql&logoColor=white"/> version 8.3.0
* <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/> version 24.0.6

## 사용 기술
* 소셜 로그인 : KAKAO REST API
* 기본 인증 및 인가 : Json Web Token(JWT)
* DB 엑세스 및 ORM : JPA
* 이미지 업로드 방식 : S3 Presigned URL
* 매일 투표 집계 : Spring Scheduler
* 예외 처리 : @RestControllerAdvice를 통한 Custom Exception

## 인프라 구성도
![인프라](https://github.com/user-attachments/assets/4fc86050-0e6f-49ff-a993-7243f1076fee)

## ERD
![ERD](https://github.com/user-attachments/assets/c4c69e53-b5e5-423d-8bfa-29f0c7ae70f0)