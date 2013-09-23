all: sample1 sample2 control_car_test
sample1: vec3d.o my_socket.o sample_base.o sample1.o
	gcc -o sample1 vec3d.o my_socket.o sample_base.o sample1.o -lm
sample2: vec3d.o my_socket.o sample_base.o sample2.o
	gcc -o sample2 vec3d.o my_socket.o sample_base.o sample2.o -lm
sample1.o: abstract.h sample1.c
	gcc -c sample1.c
sample2.o: abstract.h sample2.c
	gcc -c sample2.c
sample_base.o: abstract.h sample_base.h sample_base.c
	gcc -c sample_base.c
control_car_test: my_socket.o control_car_test.o
	gcc -o control_car_test my_socket.o control_car_test.o
control_car_test.o: control_car_test.c
	gcc -c control_car_test.c
my_socket.o: my_socket.h my_socket.c
	gcc -c my_socket.c
vec3d.o: vec3d.h vec3d.c
	gcc -c vec3d.c
clean:
	rm -f *.o sample1 sample2 control_car_test
