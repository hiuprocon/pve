package prototype;

import javax.swing.*;
import java.awt.event.*;
import com.github.hiuprocon.pve.ui.Client;

public class CarAController extends JFrame implements KeyListener, Runnable {
    private static final long serialVersionUID = 1L;
    Client client;
    boolean keyUp;
    boolean keyDown;
    boolean keyRight;
    boolean keyLeft;

    public CarAController(int port) {
        super("CarA:" + port);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        client = new Client(port);
        setSize(300, 50);
        setVisible(true);
        new Thread(this).start();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
        case KeyEvent.VK_UP:
            keyUp = true;
            break;
        case KeyEvent.VK_DOWN:
            keyDown = true;
            break;
        case KeyEvent.VK_RIGHT:
            keyRight = true;
            break;
        case KeyEvent.VK_LEFT:
            keyLeft = true;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
        case KeyEvent.VK_UP:
            keyUp = false;
            break;
        case KeyEvent.VK_DOWN:
            keyDown = false;
            break;
        case KeyEvent.VK_RIGHT:
            keyRight = false;
            break;
        case KeyEvent.VK_LEFT:
            keyLeft = false;
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        ;
    }

    @Override
    public void run() {
        while (true) {
            double engine = 0.0;
            double steering = 0.0;

            if (keyUp)
                engine += 500.0;
            if (keyDown)
                engine -= 500.0;

            if (keyRight)
                steering += 3.0;
            if (keyLeft)
                steering -= 3.0;

            // System.out.println(client.post("drive "+engine+" "+steering));
            client.post("drive " + engine + " " + steering);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                ;
            }
        }
    }

    public static void main(String args[]) {
        int port = Integer.parseInt(args[0]);
        new CarAController(port);
    }
}
