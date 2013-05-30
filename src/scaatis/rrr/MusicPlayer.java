package scaatis.rrr;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

public class MusicPlayer extends UpdatableObject {

    public static final ArrayList<Sequence> songs = new ArrayList<>();
    static {
        songs.add(load("res/songs/badtothebone.mid"));
        songs.add(load("res/songs/borntobewild.mid"));
        songs.add(load("res/songs/highwaystar.mid"));
        songs.add(load("res/songs/paranoid.mid"));
        songs.add(load("res/songs/petergun.mid"));
        songs.add(load("res/songs/radarlove.mid"));
    }

    private Sequencer                       sequencer;
    private Receiver                        receiver;
    private int                             currentIndex;
    private Random                          random;
    private boolean                         playing;

    public MusicPlayer(int id) {
        super(id, new Point(), new DummyResource());
        try {
            sequencer = MidiSystem.getSequencer(false);
            receiver = MidiSystem.getReceiver();
            sequencer.getTransmitter().setReceiver(receiver);
            sequencer.open();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        playing = false;
        random = new Random();
        currentIndex = -1;
        nextSong();
    }

    public void nextSong() {
        int nextIndex;
        do {
            nextIndex = random.nextInt(songs.size());
        } while (nextIndex == currentIndex);
        currentIndex = nextIndex;
        try {
            sequencer.setSequence(songs.get(currentIndex));
            ShortMessage volMessage = new ShortMessage();
            for (int i = 0; i < 16; i++) {
                try {
                    volMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, 2);
                } catch (InvalidMidiDataException e) {
                }
                receiver.send(volMessage, -1);
            }
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void play() {
        if (playing) {
            return;
        }
        playing = true;
        sequencer.setLoopCount(0);
        sequencer.start();
    }

    public void stop() {
        if (!playing) {
            return;
        }
        playing = false;
        sequencer.stop();
    }

    @Override
    public void update(double delta) {
        if (!sequencer.isRunning() && playing) {
            stop();
            nextSong();
            play();
        }
    }

    public static Sequence load(String path) {
        try {
            return MidiSystem.getSequence(new File(path));
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
