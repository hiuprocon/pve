bcc32 -P -c -oVec3d.obj Vec3d.cpp
bcc32 -P -c -oMySocket.obj MySocket.cpp
bcc32 -P -c -oControlCarTest.obj ControlCarTest.cpp
bcc32 -eControlCarTest.exe MySocket.obj ControlCarTest.obj
bcc32 -P -c -oSampleBase.obj SampleBase.cpp
bcc32 -P -c -oSample2.obj Sample2.cpp
bcc32 -P -c -oSample1.obj Sample1.cpp
bcc32 -eSample2.exe Vec3d.obj MySocket.obj SampleBase.obj Sample2.obj
bcc32 -eSample1.exe Vec3d.obj MySocket.obj SampleBase.obj Sample1.obj
