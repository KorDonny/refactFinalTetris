package kr.ac.jbnu.se.tetris;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip bgmClip;
    private Clip dropSoundClip;
    private Clip removeSoundClip;

    public Sound() {
        setBgm();
        setDropSound();
        setRemoveSound();
    }

    private synchronized void setBgm() {
        File bgmFile = new File("./src/main/java/kr/ac/jbnu/se/tetris/Resource/music/bgm.wav");
        try {
            bgmClip = AudioSystem.getClip();
            bgmClip.open(AudioSystem.getAudioInputStream(bgmFile));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setDropSound() {
        File dropSoundFile = new File("./src/main/java/kr/ac/jbnu/se/tetris/Resource/music/drop.wav");

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(dropSoundFile)) {
            // 명시적으로 원하는 오디오 포맷을 설정
            AudioFormat desiredFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, // 인코딩 타입
                    44100,                           // 샘플 레이트
                    16,                              // 비트 해상도
                    2,                               // 채널 수 (1: 모노, 2: 스테레오)
                    4,                               // 프레임 사이즈
                    44100,                           // 프레임 레이트
                    false                            // 빅 엔디안 여부
            );

            try (AudioInputStream formattedAudioInputStream = AudioSystem.getAudioInputStream(desiredFormat, audioInputStream)) {
                dropSoundClip = AudioSystem.getClip();
                dropSoundClip.open(formattedAudioInputStream);
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setRemoveSound() {
        File removeSoundFile = new File("./src/main/java/kr/ac/jbnu/se/tetris/Resource/music/remove.wav");
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(removeSoundFile)) {
            // 명시적으로 원하는 오디오 포맷을 설정
            AudioFormat desiredFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, // 인코딩 타입
                    44100,                           // 샘플 레이트
                    16,                              // 비트 해상도
                    2,                               // 채널 수 (1: 모노, 2: 스테레오)
                    4,                               // 프레임 사이즈
                    44100,                           // 프레임 레이트
                    false                            // 빅 엔디안 여부
            );

            try (AudioInputStream formattedAudioInputStream = AudioSystem.getAudioInputStream(desiredFormat, audioInputStream)) {
                removeSoundClip = AudioSystem.getClip();
                removeSoundClip.open(formattedAudioInputStream);
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void stopBgm() {
        bgmClip.stop();
    }

    public void startBgm() {
        bgmClip.start();
        bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void playDropSound() {
        dropSoundClip.setFramePosition(0);
        dropSoundClip.start();
    }

    public void playRemoveSound() {
        removeSoundClip.setFramePosition(0);
        removeSoundClip.start();
    }
}