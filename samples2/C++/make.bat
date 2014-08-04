rem vcvars32
cl /EHsc /c Vec3d.cpp
cl /EHsc /c MySocket.cpp
cl /EHsc /c ControlCarTest.cpp
cl /FeControlCarTest ws2_32.lib MySocket.obj ControlCarTest.obj
cl /EHsc /c SampleBase.cpp
cl /EHsc /c Sample2.cpp
cl /EHsc /c Sample1.cpp
cl /FeSample2 ws2_32.lib Vec3d.obj MySocket.obj SampleBase.obj Sample2.obj
cl /FeSample1 ws2_32.lib Vec3d.obj MySocket.obj SampleBase.obj Sample1.obj
