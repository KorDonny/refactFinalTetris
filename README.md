# refactFinalTetris
#작업 방법
개인 레포에서

{
1. checkout remote/origin/master
2. new branch로 브렌치명 기입, 작업
3. 작업후 commit & push
4. 이후 github에서 팀원이 pull request 신청하면 master계정에서 확인후 승인.
@ ( vcs, github, create pull request, master <- 해당브렌치, merge pull request, master에서 승인 ) 단, PullRequest할때 conflict 발생하면 pull해서 conflict 해결하고 다시 PullRequest 
6. master 계정은 지속해서 origin/master를 remote/해당브렌치명과 merge 작업 수행. ( fetch )
7. 이후 다시 remote/origin/master에 push.
8. 이전 작업자들은 지속해서 github에서 master branch를 sync해줘야함. 
9. pull remote/origin/master
}

#conflict 해결 -> master에서 수행하는 것이 바람직. 코드 구문의 필요성과 의의를 파악.
최대한 대면 작업이 필요함.

#reset방법
1. reset mixed -> 로컬 작업부분 그대로, git에는 초기이후 작업부분이 unstaged, git log 없음
2. reset soft -> 로컬 작업 부분 그대로, git에는 초기이후 작업부분이 staged, git log 없음
3. reset hard -> 지정 헤더 이후 작업 모두 삭제, git에는 작업부분 삭제로 커밋할것 없다 뜸, git log 없음

#구현 목표
1. 타이머 정리 및 병합 -> 메인 타이머를 Tetris 클래스에서 수행. 이것을 각 클래스마다 매개변수로 참조.
2. 쓰레드 최적화 - 불필요 쓰레드 삭제, 분산된 timer 병합
3. 키입력 최적화
4. UI구성 필요 - { 로그인창, 메인창, 스코어창, 게임창 }
5. 로그인 및 회원가입, 회원 정보 로직 구성
6. 기능 추가. ( 게임모드 리펙토링, 게임 모드내 세부구성 추가, 난이도 조절 }
7. 소켓 통신? - {TCP, UDP등등}
