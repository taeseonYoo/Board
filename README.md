# 🖥️Board

<img width="1684" alt="Image" src="https://github.com/user-attachments/assets/f4f9c4c1-2363-4c01-b780-72fecc0ed538" />

## 🔨기능
- 게시글 등록, 조회, 수정, 삭제, 페이지 네이션
- 댓글 등록, 조회, 수정, 삭제
- 회원 : springsecurity 회원 가입, 로그인, 로그아웃


## ⚙️기술 스택

### 🎯Backend
<div> <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"> <img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"> </div>

### 🗄️Database
<div> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/H2Database-blue?style=for-the-badge&logo=H2&logoColor=white"> </div>

### 🎨Frontend
<div> <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white"> <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=Bootstrap&logoColor=white"> </div>

### ☁️Environment & Infrastructure
<div> <img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"> <img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"> <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"> </div>

## 📷실행 화면

<details>
  <summary>게시글 주요 실행 화면</summary>
  페이지 네이션
  <img width="1600" src="https://github.com/user-attachments/assets/38359f7c-221b-46ca-9f73-02b00d3301b5"/>
  게시글 작성
  <img width="1600" alt="Image" src="https://github.com/user-attachments/assets/161d1c1e-dcc8-49ad-a613-a29bd236fb36" />
  게시글 상세보기(본인이 작성한 게시글인 경우에만 수정, 삭제)
  <img width="1600" alt="Image" src="https://github.com/user-attachments/assets/80a35493-d636-4d3e-b896-f4f4f465f162" />
</details>
<details>
  <summary>회원 주요 실행 화면</summary>
  마이페이지
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/8e020b63-1de3-40b9-b1c6-61ba8ac87b6f" />
회원가입
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/fb6bddca-4eda-44ba-ba0a-52230b33d56c" />
로그인
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/ca823987-97f7-4b04-9088-f6db3ab46479" />
</details>
<details>
  <summary>댓글 주요 실행 화면</summary>
  댓글 작성
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/c76280a7-782b-4341-8e30-c5fea36394dd" />
댓글 작성 완료
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/61140644-d56b-42f9-acbd-a51f47d6020e" />
댓글 드롭다운 버튼(수정, 삭제)
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/740b7d16-210f-48e9-855c-7b97ee41b328" />
댓글 수정
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/6a8589f7-02c3-427a-b499-86c28a575355" />
댓글 삭제
<img width="1600" alt="Image" src="https://github.com/user-attachments/assets/f4e848f1-12bf-4d2e-89fc-ced3dd485650" />
</details>


## ⚒️구조 및 설계

### 1. DB설계
<img width="750" alt="all" src="https://github.com/user-attachments/assets/65d3958d-2f17-47d2-9c18-ab468b78d265" />
<img width="750" alt="member" src="https://github.com/user-attachments/assets/1ac35aed-42a6-424d-aab6-8295306b6b27" />
<img width="750" alt="post" src="https://github.com/user-attachments/assets/9d5c5dc9-0068-47e8-b737-ad55467c8111" />
<img width="750" alt="comment" src="https://github.com/user-attachments/assets/d0d73149-0339-4685-bb7f-3b08205233ba" />

### 2. API설계
<img width="750" alt="memberAPI" src="https://github.com/user-attachments/assets/9321abc1-1771-4f46-a50c-c27da025dc8c" />
<img width="750" alt="postAPI" src="https://github.com/user-attachments/assets/78debd21-f011-4628-8327-d512a343f595" />
<img width="750" alt="commentsAPI" src="https://github.com/user-attachments/assets/0f5e0c2b-e3b4-44c8-b908-a8cb7f7c91d6" />

## ✏️개발 과정
<a href="https://taetae99.tistory.com/27">프로젝트 명세서 작성</a><br>
<a href="https://taetae99.tistory.com/28">도메인 설계 및 회원 기능 개발 및 테스트</a><br>
<a href="https://taetae99.tistory.com/30">게시글, 댓글 기능 개발 및 테스트</a><br>
<a href="https://taetae99.tistory.com/31">로그인 및 게시판 메인화면 구현, Spring Security를 사용한 로그인</a><br>
<a href="https://taetae99.tistory.com/35">마이 페이지 구현 및 Dto와 Form 설계</a><br>
<a href="https://taetae99.tistory.com/36">MemberController MockMvc를 사용하여 테스트하기</a><br>
<a href="https://taetae99.tistory.com/38">게시글 생성 기능 구현</a><br>
<a href="https://taetae99.tistory.com/39">게시글 수정, 삭제 기능 개발</a><br>
<a href="https://taetae99.tistory.com/40">게시판 페이징 구현</a><br>
<a href="https://taetae99.tistory.com/44">비동기 없이 댓글 수정 및 댓글 생성, 삭제 구현</a><br>
<a href="https://taetae99.tistory.com/46">마이그레이션 : H2에서 MySQL</a><br>

