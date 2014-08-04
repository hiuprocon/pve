all: Sample1 Sample2 ControlCarTest
Sample1: Vec3d.o MySocket.o SampleBase.o Sample1.o
	c++ -o Sample1 Vec3d.o MySocket.o SampleBase.o Sample1.o
Sample2: Vec3d.o MySocket.o SampleBase.o Sample2.o
	c++ -o Sample2 Vec3d.o MySocket.o SampleBase.o Sample2.o
Sample1.o: Sample1.cpp
	c++ -c Sample1.cpp
Sample2.o: Sample2.cpp
	c++ -c Sample2.cpp
SampleBase.o: SampleBase.h SampleBase.cpp
	c++ -c SampleBase.cpp
ControlCarTest: MySocket.o ControlCarTest.o
	c++ -o ControlCarTest MySocket.o ControlCarTest.o
ControlCarTest.o: ControlCarTest.cpp
	c++ -c ControlCarTest.cpp
MySocket.o: MySocket.h MySocket.cpp
	c++ -c MySocket.cpp
Vec3d.o: Vec3d.h Vec3d.cpp
	c++ -c Vec3d.cpp
clean:
	rm -f *.o Sample1 Sample2 ControlCarTest
