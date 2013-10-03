rem vcvars32.bat
cl.exe ws2_32.lib sample1.c vec3d.c my_socket.c sample_base.c
cl.exe ws2_32.lib sample2.c vec3d.c my_socket.c sample_base.c
cl.exe ws2_32.lib control_car_test.c my_socket.c
