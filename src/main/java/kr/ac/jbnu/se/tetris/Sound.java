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
        File bgmFile = new File("music/bgm.wav");
        try {
            bgmClip = AudioSystem.getClip();
            bgmClip.open(AudioSystem.getAudioInputStream(bgmFile));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setDropSound() {
        File dropSoundFile = new File("music/drop.wav");
        try {
            dropSoundClip = AudioSystem.getClip();
            dropSoundClip.open(AudioSystem.getAudioInputStream(dropSoundFile));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setRemoveSound() {
        File removeSoundFile = new File("music/remove.wav");
        try {
            removeSoundClip = AudioSystem.getClip();
            removeSoundClip.open(AudioSystem.getAudioInputStream(removeSoundFile));
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