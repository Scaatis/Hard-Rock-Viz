package scaatis.rrr;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;

import org.json.JSONObject;

public class SpriteViz extends JFrame {
    private static final long               serialVersionUID = 1L;
    private static int                      width            = 1024;
    private static int                      height           = 768;
    public static final int                 scale            = 2;
    public static final int                 fps              = 30;
    public static boolean                   fullscreen       = false;

    private boolean                         running;
    private String                          host;
    private Scene                           scene;
    private SortedSet<KeyEvent>             pressedKeys;
    private ConcurrentLinkedQueue<KeyEvent> eventQueue;

    public SpriteViz(String host) {
        super("Hard Rock Racing");
        if (fullscreen) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            width = gd.getDisplayMode().getWidth();
            height = gd.getDisplayMode().getHeight();
        }
        running = false;
        this.host = host;
        scene = new WaitingScene();
        pressedKeys = Collections.synchronizedSortedSet(new TreeSet<>(new Comparator<KeyEvent>() {
            @Override
            public int compare(KeyEvent o1, KeyEvent o2) {
                return Integer.compare(o1.getKeyCode(), o2.getKeyCode());
            }
        }));
        eventQueue = new ConcurrentLinkedQueue<>();

        setIgnoreRepaint(true);
        setUndecorated(fullscreen);
        setBounds(0, 0, width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        createBufferStrategy(2);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (pressedKeys.contains(e)) {
                    eventQueue.add(e);
                    pressedKeys.remove(e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!pressedKeys.contains(e)) {
                    eventQueue.add(e);
                    pressedKeys.add(e);
                }
            }
        });
    }

    public void stop() {
        running = false;
    }

    public void start() {
        running = true;
        Socket socket;
        PrintWriter writer;
        BufferedReader reader;
        try {
            socket = new Socket(host, 1993);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            stop();
            return;
        }
        sendHandshake(writer);
        long timeA = System.nanoTime();
        long timeB = timeA;
        while (running) {
            timeB = System.nanoTime();
            double delta = (timeB - timeA) / 1e9;
            setTitle("FPS " + (1 / delta));
            timeA = System.nanoTime();
            while (!eventQueue.isEmpty()) {
                scene.receiveInput(eventQueue.poll());
            }
            scene.update(delta);
            try {
                while (reader.ready()) {
                    parseInput(reader.readLine());
                }
            } catch (IOException e) {
                stop();
                break;
            }
            // repaint();
            redraw();
            timeB = System.nanoTime();
        }
        close(socket);
    }

    private void close(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redraw() {
        BufferStrategy strategy = getBufferStrategy();
        Graphics g = strategy.getDrawGraphics();
        g.setClip(new Rectangle(0, 0, width, height));
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.scale(scale, scale);
        scene.draw(graphics2d);
        g.dispose();
        strategy.show();
        Toolkit.getDefaultToolkit().sync();
    }

    private void sendHandshake(PrintWriter out) {
        JSONObject handshake = new JSONObject();
        handshake.put("message", "connect");
        handshake.put("type", "observer");
        handshake.put("tracktiled", true);
        out.println(handshake.toString());
    }

    private void parseInput(String line) {
        JSONObject object = new JSONObject(line);
        String message = object.getString("message");
        if (message.equals("gamestart")) {
            scene.cleanup();
            scene = new RaceScene(object);
        } else if (message.equals("raceover")) {
            scene.cleanup();
            if (object.getJSONArray("placement").length() == 0) {
                scene = new WaitingScene();
            } else {
                scene = new PostRaceScene(object);
            }
        } else {
            scene.receiveJSON(object);
        }
    }

    public static void main(String[] args) {
        String host = null;
        for (String s : args) {
            if (args.equals("-f")) {
                fullscreen = true;
            } else {
                host = s;
            }
        }
        SpriteViz viz = new SpriteViz(host == null ? "localhost" : host);
        viz.start();
    }

    public static Point getScreenCenter() {
        return new Point(width / scale / 2, height / scale / 2);
    }

    public static int getScreenWidth() {
        return width / scale;
    }

    public static int getScreenHeight() {
        return height / scale;
    }
}
