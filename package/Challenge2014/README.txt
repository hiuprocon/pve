1. Overview

This package consists of a simulation environment and its samples.

This computer simulation environment is used for International
Programming Contest.  Samples are A) Server/Client sample and
B) simple client implementation and its usage example
C) a complete sample program to clear the game. There is a brief
description of these samples a bit later.

This simulation environment and samples work on Windows (32bit, 64bit),
Mac OS X, and Linux (64bit).
NOTICE:
    * When you use MS-Windows, you will need to install JRE (Java6 or
      higher) in advance.
    * When you use Mac OS X and use Java installed by default, you
      should delete following files (or move them into some other
      place) in /System/Library/Java/Extensions:
        j3daudio.jar, j3dcore.jar, j3dutils.jar, vecmath.jar, libJ3D.jnilib,
        libJ3DAudio.jnilib, libJ3DUtils.jnilib

2. Rules of the Challenge

* Participants develop programs for simulated cars
  which bring jewels to the goals by collecting all of them 
  which scattered in a virtual environment.
* There are two cars in the virtual environment. And
  two cars must cooperate to clear the challenge.
* The winner is determined by time to clear the challenge.
* Jewels are placed by random number. So simulations are
  executed at 10 times, and the average time is used to
  determine the winner.
* The time limit of the challenge is 5,000 sec.
  If the program could not complete the challenge in time limit,
  5,000 sec is used to calculate the average time.
* To bring jewels to the goals, cars have to use the elevator.
  The elevator can carry 5 jewels. But 6 or more jewels can
  not be carried at one time.

3. A) Server/Client sample program

3.1 What does this sample do?

We propose a challenge as the subject for our programming contest
(both international and local), which is to bring jewels to the goal by
collecting all of them which scattered in a playing field by
manipulating multiple cars.

This sample shows a picture of that challenge. It is also for tesing a
simulation environment.

This server program provides a playing field.


Client program runs two clients. These are for controlling a red-colored
car and a blued-colored car which carries jewels to the gols.


Each client connects to the server via an internet socket interface.
A detailed communications protocol is described later on
"Communication protocol".


3.2 How to start programs and how to control cars and a view of a
field

Step one is to run a server program and the second is to run a client
program.
    MS-Windows: execute Server.bat and Client.bat.
    Max OS X: execute Server.command and Client.command.
    Linux: execute Server.sh and Client.sh.

After client program is loaded, two windows will appear. Each window
corresponds to each car. Click corresponding window and use arrow keys
to control a car. You can also input a command (available commands are
shown later) into a text field which is located on top of the window.
When you input a command and press the enter key, that command is sent
to the server and the result which the server returns will be shown on
the window.


4. B) Simple sample of client implementation and its usage example

In the samples/Java/simple folder, we provide a simple socket communication
sample.

MySocket is a simple client for one-line text messaging.
ControlCarTest sends "getLoc" "drive" and "stepForward" commands
using MySocket class.

To execute this sample, At first, run the server. Then check
"only one car" button. Finally, run CoontrolCarTest class.

5. C) a complete sample program to clear the game

In the samples/Java/complete folder, we provide a complete sample program
to clear the game.

This sample program is separated to many classes. Executable classes
are Sample1 and Sample2. And Sample1 and Sample2 extends SampleBase
which is the super class of them. The concept of this sample is explained
in comments in SampleBase.java. 

To execute this sample, At first, run the server. Then run Sample1
and Sample2.

6. Communcations protocol

The server and the clients use a TCP/IP connection.

A car connects to the server on port 10000 or 20000.
In this sample, a red car on the left side connects on port 10000, the
other on the right side connects on port 20000.

Available commands are as follows:
    * drive - controlling a car
    * getLoc - getting its location
    * getRev - getting its rotaion angle
    * seachJewels - getting jewels informations
    * stepForward - steping the simulation process forward
    * sendMessage - sending a message to another car
    * receiveMessages - receiving all messages in own mailbox
    * setWaitTime - controling simulation speed
Please refer to the next section for more details of those commands.

All commands are case-sensitive. When you send a wrong command (for
example, when you send "getloc" instead of "getLoc"), you will just
get "ERROR" response from the server.

6.1 Controlling a car

Request: drive power steering
Response: OK

    Parameters:
        power - the engine power of this car
        steering - the power of rotation.
                If it has a positive value, the direction is the
                clockwise direction. If it has a negative number, the
                direction is the counterclockwise.
        Both power and steering are recognized as real number by the
        server if you will set the number as integer.

Example:
    Request: drive 2.0 1.0
    Response: OK

    Request: drive 1 3
    Response: OK

    In the second example, the power 1 is recognized as 1.0 and the
    steering 3 is recongized as 3.0 by the server.

6.2 Obtaining the location of this car

Request: getLoc
Response: x y z

    Request has no parameters.
    Response Parameters:
        x - the x-coordinate of this car
        y - the y-coordinate of this car
        z - the z-coordinate of this car
        All of them are real number.

Example:
    Request: getLoc
    Response: -80.0 0.07752883434295654 -1.541969395475462E-4

6.3 Obtaining its rotation angle

Request: getRev
Response: x y z

    Request has no parameters.
    Response Parameters:
        x - the rotation angle of x of this car
        y - the rotation angle of y of this car
        z - the rotation angle of z of this car

Example:
    Request: getRev
    Response: -1.4273451865348383 90.01178963772256 -0.0056981111728151

6.4 Obtaining the locations of all jewels in the playing field

Request: searchJewels
Response: n id_1 x_1 y_1 z_1 id_2 x_2 y_2 z_2 ...

    Request has no parameters.
    Response Parameters:
        n - the number of jewels
        id_1 - id of the jewel #1
        x_1 - the x-coordinate of the jewel #1
        y_1 - the y-coordinate of the jewel #1
        z_1 - the z-coordinate of the jewel #1
        id_2 - id of the jewel #2
        x_2 - the x-coordinate of the jewel #2
        y_2 - the y-coordinate of the jewel #2
        z_2 - the z-coordinate of the jewel #2
        ...
    This response returns the location of all jewels. So, we get 40
    items with a single-line in the problem setting we provided in
    this package! The example of the response is below (it's a
    single-line).

-- From here: the example of the response --
20 jA1.0 -109.26143646240234 0.24994425475597382 -30.579011917114258 jA1.1 -105.56793212890625 0.2499610185623169 18.476598739624023 jA1.2 -124.10114288330078 0.2490706741809845 -42.476505279541016 jA1.3 -63.30953598022461 0.24992847442626953 -46.274654388427734 jA1.4 -128.35411071777344 -11536.73046875 12.186260223388672 jA1.5 -105.72850036621094 0.24945469200611115 7.561228275299072 jA1.6 -121.95637512207031 0.2499908059835434 -19.04789924621582 jA1.7 -102.19898986816406 0.2497095763683319 11.39200496673584 jA1.8 -90.5438232421875 0.2498069703578949 -24.62664794921875 jA1.9 -87.7422103881836 0.2499949336051941 8.926119804382324 jA2.0 47.195858001708984 0.24997757375240326 -13.833354949951172 jA2.1 63.87803268432617 0.24998214840888977 -36.5544548034668 jA2.2 90.43672943115234 0.2492053210735321 10.645959854125977 jA2.3 54.83708572387695 0.24988259375095367 -11.222868919372559 jA2.4 99.43578338623047 0.24934367835521698 2.1986961364746094 jA2.5 90.72463989257812 0.24999012053012848 -16.9257755279541 jA2.6 31.021682739257812 0.24997389316558838 17.490110397338867 jA2.7 40.18168640136719 0.24996960163116455 -47.197574615478516 jA2.8 84.7614517211914 0.24932542443275452 -49.65036392211914 jA2.9 57.94783401489258 0.24973884224891663 -16.77199363708496
-- up to here --

6.5 Put a clock forward in the simulation environment

(2013,05/28) This command is added for a warranty of same conditions
on different computers. 

Request: stepForward
Response: OK

    Request and Response have no parameters.

The time of the simulation environment is advanced, when all activated
cars send this "stepForward" command. And responses are blocked until
the server complete simulation process.

6.6 Send a message to another car

A car can send any string message to another car.

Request: sendMessage message-string
Response: OK

    Parameter:
        message - a message send to another car.

        The parameter "message" can contains any character.
        But comma "," is used for a message separator in next
        "receiveMessages" command. So, you shoud not use ","
        in messages.

Example:
    Request: sendMessage Hello World
    Response: OK

.....

6.7 Receive messages from own mailbox

A car can read all messages in own mailbox.

Request: receiveMessages
Response: messages: message-string1,message-string2,...

    Parameter:
        message-stringN - Messages stored in the mailbox.

Example:
    Request: receiveMessages
    Response: messages: Hello World1,Hello World2,...

6.8 Control simulation speed

A car can control simulation speed by setting waiting time
which is inserted between each simulation steps. This command
is useful if you observe simulation steps at appropriate timing.

Request: setWaitTime time(millisecond)
Response: OK

    Parameter:
        time(millisecond) - waiting time

Example:
    Request: setWaitTime 0
    Response: OK

