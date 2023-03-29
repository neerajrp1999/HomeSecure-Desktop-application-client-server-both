package server.main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound {
    private static float SAMPLE_RATE = 44100;
    private static int MILLSECONDS = 500;
    private static int VOLUME = 20;
    private void beep() {
        //Toolkit.getDefaultToolkit().beep();
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        SourceDataLine sdl = null;
        try {
            sdl = AudioSystem.getSourceDataLine(af);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            sdl.open(af);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        sdl.start();

        byte[] buf = new byte[1];

        int analogFrequency = 523;
        double normalizedFrequency = (analogFrequency / SAMPLE_RATE) * 2.0 * Math.PI;

        /*
         * samples per second / 1000 = samples per milliseconds
         *
         * numberOfSamples = sample per milliseconds * milliseconds
         */
        float numberOfSamples = (SAMPLE_RATE / 1000) * MILLSECONDS;

        for (int i = 0; i < numberOfSamples; i++) {
            double angleForThisSample = i * normalizedFrequency;
            buf[0] = (byte) (Math.sin(angleForThisSample) * VOLUME);
            sdl.write(buf, 0, 1);
        }

        sdl.drain();
        //sdl.stop();
        //sdl.close();
        //sdl.stop();
    }
    private static int OCTAVE = 5;

    private static SourceDataLine sdl;

    public void main1() {

        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);

        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
//            generateMusicalNotes(E, E,E, E,E, E,E, E,E, E,E, E,E, E);
           generateMusicalNotes(MusicNote.E, MusicNote.E, MusicNote.E);
            sdl.stop();
            sdl.close();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void generateMusicalNotes(MusicNote... notes) throws Exception {
        for (MusicNote note : notes) {
            if (MusicNote.PAUSE.equals(note)) {
                Thread.sleep(500);
            } else {
                generateMusicalNote(note.analogFrequency(OCTAVE));
            }
        }
    }

    public static void generateMusicalNote(int analogFrequency) throws LineUnavailableException {

        byte[] buf = new byte[1];

        double normalizedFrequency = (analogFrequency / SAMPLE_RATE) * 2.0 * Math.PI;

        /*
         * samples per second / 1000 = samples per milliseconds
         *
         * numberOfSamples = sample per milliseconds * milliseconds
         */
        float numberOfSamples = (SAMPLE_RATE / 1000) * MILLSECONDS;

        for (int i = 0; i < numberOfSamples; i++) {
            double angleForThisSample = i * normalizedFrequency;
            buf[0] = (byte) (Math.sin(angleForThisSample) * VOLUME);

            sdl.write(buf, 0, 1);
        }
        sdl.drain();
    }

}

enum MusicNote {
    C(new int[] { 16, 32, 65, 130, 261, 523, 1046, 2093, 4186, 8372 }),
    D(new int[] { 18, 36, 73, 146, 293, 587, 1174, 2349, 4698, 9397 }),
    E(new int[] { 20, 41, 82, 164, 329, 659, 1318, 2637, 5274, 10548 }),
    F(new int[] { 21, 43, 87, 174, 349, 698, 1396, 2793, 5587, 11175 }),
    G(new int[] { 24, 48, 97, 195, 391, 783, 1567, 3135, 6271, 12543 }),
    A(new int[] { 27, 55, 110, 220, 440, 880, 1760, 3520, 7040, 14080 }),
    B(new int[] { 30, 61, 123, 246, 493, 987, 1975, 3951, 7902, 15804 }), PAUSE(new int[] {});

    private int[] analogFrequencies;

    private MusicNote(int[] frequencies) {
        this.analogFrequencies = frequencies;
    }

    public int analogFrequency(int octave) {
        return analogFrequencies[octave];
    }
}