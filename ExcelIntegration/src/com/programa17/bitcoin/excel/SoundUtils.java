package com.programa17.bitcoin.excel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/8/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SoundUtils {

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new FileInputStream("./ExcelIntegration/res/beep-01.wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        beep();
    }

    public static void beep() {
        playSound("beep-01.wav");
    }
}
