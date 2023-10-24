# refactFinalTetris
개인 레포에서 작업후 push, 이후 github에서 request pull 신청하면 master계정에서 확인후 승인.
master 계정은 지속해서 origin/master를 remote/해당브렌치명과 merge 작업 수행.
이후 다시 remote/origin/master에 push.
이전 작업자들은 지속해서 github에서 master branch를 sync해줘야함.

#구현 목표
1. 타이머 정리 및 병합 -> 메인 타이머를 Tetris 클래스에서 수행. 이것을 각 클래스마다 매개변수로 참조.
2. 쓰레드 최적화 - 불필요 쓰레드 삭제, 분산된 timer 병합
3. 키입력 최적화
4. UI구성 필요 - { 로그인창, 메인창, 스코어창, 게임창 }
5. 로그인 및 회원가입, 회원 정보 로직 구성
6. 기능 추가. ( 게임모드 리펙토링, 게임 모드내 세부구성 추가, 난이도 조절 }
7. 소켓 통신? - {TCP, UDP등등}
