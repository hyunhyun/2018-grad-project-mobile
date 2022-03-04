# 2018-grad-project-mobile
## Safe house - 웹 방범 서비스
---
<pre>
1인 가구들이 카메라 모듈이 달린 장치를 집앞에 부착하여 위험인물 또는 지인 등 방문자의 얼굴을 외출하여서도 조회하고 방문자 등록시 알림 및 안심 귀가 할 수 있도록 하는 서비스입니다.
</pre>

해당 리파지토리는 client인 안드로이드의 소스입니다.

1. API 설계 : [API]([서버 API 설계 · hyunhyun/2018-grad-project-mobile Wiki · GitHub](https://github.com/hyunhyun/2018-grad-project-mobile/wiki/%EC%84%9C%EB%B2%84-API-%EC%84%A4%EA%B3%84)
2. Client 설명: [Andriod]([안드로이드Client · hyunhyun/2018-grad-project-mobile Wiki · GitHub](https://github.com/hyunhyun/2018-grad-project-mobile/wiki/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CClient)
### 기본기능
* 방문자  실시간으로 외부에서도 사진으로 조회
* 지인 얼굴 등록후 방문시 알림
* 타인이 등록한 위험 블랙리스트 공유 가능
* 블랙리스트 등록가능
* 	귀가시 위치 정보 일정 간격으로 등록 
* 위험시 보호자에게 지도로 사용자 위치 볼 수 있는 서비스 제공(서버에서 문자로)

### 개발환경  
* 웹 서버 :Node.js, Mongo DB
	* Cloud 개발 환경 : Digital Ocean Cloud Web server, Amazon EC2, Amazon lambda, Amazon S3
* *클라이언트* : Android ** -> 해당 소스
	* 외부라이브러리 : SQLite, Okhttp, Firebase Authentication, Firebase Cloud 메시징, AWS S3
* Hardware 개발환경 : Raspberry pi, Raspbian OS (Linux)
* 협업도구 : Git
