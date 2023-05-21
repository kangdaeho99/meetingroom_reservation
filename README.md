<!-- # room_reservation
Spring Boot를 통해 회의실 예약 사이트입니다. -->

## **🤝 회의실 예약 프로그램( Room-Reservation Service )   — (진행중)**
> Spring Boot를 통해 개발하고 있는 회의실 예약 사이트입니다.

- **링크 :** [http://ec2-15-164-137-161.ap-northeast-2.compute.amazonaws.com:8080/room/list](http://ec2-15-164-137-161.ap-northeast-2.compute.amazonaws.com:8080/room/list)  ****
- **깃허브 README & 소스코드 :**    
    [https://github.com/kangdaeho99/room_reservation](https://github.com/kangdaeho99/room_reservation)
    
![Untitled](https://user-images.githubusercontent.com/48047377/239265484-59eeabf4-087f-4de4-b50d-474633f56086.PNG)



- 프로젝트 설명 : 회의실 예약 사이트입니다.
- 백엔드 기술스택 : Spring Boot ( JPA & QueryDSL)
- 개발목표 : Spring Boot, Spring Security, Spring Batch(Batch), JPA & Query(ORM), Redis (Cache), Rabbit MQ(Message Broker), AWS(Infra), Docker(Infra), Jenkins & CodeDeploy(CI/CD) 에 대해 알아보는것

<br/>
<br/>

## 📒DB구조
> DB 제 1NF, 2NF, 3NF 정규화 규칙을 지켰습니다. 현재 프로젝트 진행중이며, 미완성상태입니다. 
> 이후에 회의실 예약 관련 테이블을 추가할 계획입니다.

![DB ERD](https://user-images.githubusercontent.com/48047377/239389773-24ae798e-cba5-4a1f-a4b4-0e55688afea4.PNG)


<br/>
<br/>

##  1.메인페이지

> 회의실 목록을 볼 수 있고, 검색이 가능합니다. <br/>

![Untitled](https://user-images.githubusercontent.com/48047377/239265484-59eeabf4-087f-4de4-b50d-474633f56086.PNG)
![Untitled](https://user-images.githubusercontent.com/48047377/239265493-19cfaf18-7843-48de-8885-287b485a70a9.PNG)
![Untitled](https://user-images.githubusercontent.com/48047377/239265501-a8183f9b-15e7-4c84-9fb4-7461a2332350.PNG)

##  2.회의실 상세화면

> 회의실 상세화면입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265565-4d83950e-062b-486b-8b80-4652b7c53c0e.PNG)


##  3.회의실 등록화면

> 회의실 등록화면입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265587-520b27ce-4718-489d-9762-6b6ff83b94ed.PNG)

##  4.회의실 수정화면

> 회의실 수정화면입니다. 삭제와 수정 둘다 할 수 있습니다.

![Untitled](https://user-images.githubusercontent.com/48047377/239271775-d50e8f8d-2617-474d-a804-f287f58bea67.PNG)

##  5.회의실 댓글목록화면

> 회의실 댓글목록화면입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265632-b953bcaa-3728-40f5-9a4b-906cfc46056c.PNG)


##  6.회의실 댓글등록모달

> 회의실 댓글목록모달입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265638-7d7556ed-2fad-437f-a66b-927f05d8ee46.PNG)


##  7.회의실 댓글삭제모달

> 회의실 댓글삭제모달입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265647-bf061d55-5f76-4b4d-8f27-b9aecef0f788.PNG)


## 8.회의실 로그인화면

> 회의실 로그인화면입니다. 

![Untitled](https://user-images.githubusercontent.com/48047377/239265662-a306d373-da8d-46ef-a8e3-004107429408.PNG)



