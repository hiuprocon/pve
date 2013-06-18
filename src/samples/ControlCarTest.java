package samples;

/**
 * A test program for controring a car.
 */
public class ControlCarTest {
    public static void main(String args[]) throws Exception {
        // Create a simplified socket for a car.
        //   port:10000 -> Car1
        //   port:20000 -> Car2
        MySocket mySocket = new MySocket(20000);

        // Get the location of the car, and print it.
        String res = mySocket.send("getLoc");
        System.out.println(res);

        // Send a command to control the engine and the steering.
        //   Engine output : 1
        //   Steering      : 3(clockwise direction)
        // Then print the result of the command.
        res = mySocket.send("drive 1 3");
        System.out.println(res);

        // Sleep for 10 seconds.(The car continues to drive.)
        for (int i=0;i<30*10;i++)
            mySocket.send("stepForward");

        // Send a command to stop the car.
        res = mySocket.send("drive 0 0");
        System.out.println(res);

        // Get the location of the car, and print it once more.
        res = mySocket.send("getLoc");
        System.out.println(res);

        // Close the socket.
        mySocket.close();
    }
}
