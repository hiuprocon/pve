#!/usr/bin/env perl

use FindBin;

$basePath = $FindBin::RealBin;

chdir $basePath;

$ENV{CLASSPATH}=".:$basePath/lib/*:";

#$ENV{LC_ALL}="en";
#system("/usr/x11/bin/xterm -e bash");
system("/bin/bash");

