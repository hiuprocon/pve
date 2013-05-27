package prototype;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.github.hiuprocon.pve.ui.Client;

public class CarController extends JFrame implements KeyListener, Runnable, ActionListener {
    private static final long serialVersionUID = 1L;
    String name;
    int port;
    Client client;
    boolean keyUp;
    boolean keyDown;
    boolean keyRight;
    boolean keyLeft;
    JTextField commandTF;
    JTextArea textArea;

    public CarController(String name,int port,int x,int y,int w,int h) {
        super(name+":" + port);
        this.name = name;
        this.port = port;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        commandTF = new JTextField();
        add(commandTF,BorderLayout.NORTH);
        commandTF.addActionListener(this);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setPreferredSize(new Dimension(500,150));
        add(sp,BorderLayout.CENTER);

        addKeyListener(this);
        commandTF.addKeyListener(this);
        textArea.addKeyListener(this);
        
        client = new Client(port);
        //setSize(300, 50);
        //pack();
        setBounds(x,y,w,h);
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==commandTF) {
            String c = commandTF.getText();
            textArea.append("IN : "+c+"\n");
            textArea.append("OUT: "+client.post(c)+"\n");
            commandTF.setText("");
        } else {
            textArea.append("???\n");
        }
        
    }
    @Override
    public void run() {
        while (true) {
            double engine = 0.0;
            double steering = 0.0;

            if (keyUp)
                engine += 1.0;
            if (keyDown)
                engine -= 1.0;

            if (keyRight)
                steering += 3.0;
            if (keyLeft)
                steering -= 3.0;

            // System.out.println(client.post("drive "+engine+" "+steering));
            client.post("drive " + engine + " " + steering);
            client.post("stepForward");
            /*
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                ;
            }
            */
        }
    }

    public static void main(String args[]) {
        int port = Integer.parseInt(args[1]);
        new CarController(args[0],port,0,0,500,200);
    }
}
