# TimeTogether: 공유캘린더

<hr />
<h1>📚 STACKS</h1>
<div align=center> 
  <p>Front-End</p>
  <img src="https://img.shields.io/badge/Flutter-02569B?style=for-the-badge&logo=Flutter&logoColor=white" alt="Flutter"> 
  
  <p>Back-End</p>
  <img src="https://img.shields.io/badge/Java%2017-007396?style=for-the-badge&logo=Java&logoColor=white" alt="Java 17"> 
  <img src="https://img.shields.io/badge/Spring%20Boot%203.4-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white" alt="Spring Boot 3.4">
  <img src="https://img.shields.io/badge/MariaDB%2015.1-003545?style=for-the-badge&logo=MariaDB&logoColor=white" alt="MariaDB 15.1">

  <p>Server</p>
  <img src="https://img.shields.io/badge/Raspberry%20Pi-A22846?style=for-the-badge&logo=RaspberryPi&logoColor=white" alt="Raspberry Pi 4b">
</div>

<hr />
<h1>📝 API 명세서</h1>
<div>
  JSON을 이용하여 데이터를 요청, 반환합니다.
</div>
<a href="https://sincere-mass-b32.notion.site/API-15cdd9371a1080e29613d6cee5107ac2?pvs=74">API 명세서(노션)</a>


<hr />
<h1>👉 소개</h1>
<div>
  공유 캘린더를 공유 기간 범위를 정하여 생성하고, 초대를 통해 서로의 일정을 공유할 수 있는 캘린더입니다.
</div>


<hr />
<h1>📅 핵심 기능</h1>
<div>
  <h3>1. 공유 캘린더 생성</h3>
  공유 캘린더의 이름, 시작 기간 및 종료 기간을 설정하여 생성합니다. 공유 캘린더를 생성한 사람이 관리자가 됩니다. <br />
  관리자는 공유 캘린더 생성과 동시에 공유 캘린더에 자동으로 참여하게 됩니다. <br />
  공유 캘린더의 시작 및 종료 기간을 수정할 수 있습니다.
</div>
<div>
  <h3>2. 초대 링크 방식을 통한 사용자 초대</h3>
  공유 캘린더 참여 기준: 공유 캘린더 참여 정보(유저 식별자와 공유식별자 저장)를 저장하는 "SharedUser" 테이블에 유저 A의 식별자와 공유 캘린더 S의 식별자가 묶어서 저장되면 참여된 것으로 간주합니다. <br />
  따라서 공유 캘린더에 참여하기 위해서는 해당 공유 캘린더의 식별자 정보가 필요합니다. 이를 토큰화하여 초대링크를 생성합니다. <br />
  토큰 생성 시점과 캘린더 식별자 정보를 문자열로 합치고, Base64 방식으로 인코딩하여 토큰을 생성합니다.
  <br />
  초대링크를 클릭하게 되면 토큰을 디코딩하여 공유 캘린더의 식별자 정보를 HttpSession에 저장합니다. <br />
  이후 사용자가 로그인을 성공하였을 때 세션에 공유 캘린더 식별자 정보가 존재한다면 사용자와 공유 캘린더의 두 식별자를 묶어 저장하여 초대를 완료합니다. <br />
  사용이 완료된 세션은 제거합니다. 혹은 오랜 시간 사용하지 않으면 자동으로 제거됩니다.
</div>
<div>
  <h3>3. 공유 캘린더의 공유 기간 범위에 맞게 개인 캘린더를 공유 캘린더에 '동기화'</h3>
  공유 캘린더에 참여한 사용자는 자신의 "개인 캘린더" 정보를 공유 캘린더에 동기화할 수 있습니다. 동기화 API가 호출되면, 공유 캘린더의 기간 범위에 맞는 일정을 일괄적으로 동기화합니다.
</div>
<div>
  <h3>4. 공유 캘린더에 새로운 일정을 추가</h3>
  공유 캘린더의 기간 범위에 맞게 기간을 설정하여 새로운 일정을 추가할 수 있습니다. <br />
  공유 캘린더에 존재하는 모든 일정 정보는 참여자들이 확인할 수 있습니다.
</div>


<hr />
<h1>🔨 데이터 관리</h1>
<div>
  1. 공유 캘린더를 제거하면 해당 공유 캘린더와 관련된 모든 사용자 참여 정보 및 내부 이벤트가 데이터베이스에서 같이 제거됩니다.
  <br />
  2. 유저 정보가 제거되면 해당 유저가 생성한 공유 캘린더도 같이 제거됩니다.
</div>

<hr />
<h1>ERD</h1>
![image](https://github.com/user-attachments/assets/becd7af7-cd97-43fd-8d30-989d0c9d88fa)
