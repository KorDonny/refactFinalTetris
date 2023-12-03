package kr.ac.jbnu.se.tetris.entity.numeric;

public enum GameMode {
    NORMAL("싱글 모드"),ITEM("아이템 모드"),SPRINT("스프린트 모드"),
    SURVIVAL("생존 모드"),AI("봇 모드"),MULTI("멀티 모드");
    private final String label;
    GameMode(String label) {
        this.label = label;
    }
    public String label() {return label;}
}
