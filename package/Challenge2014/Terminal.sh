#!/usr/bin/env perl

use FindBin;

$basePath = $FindBin::RealBin;

chdir $basePath;

$ENV{CLASSPATH}=".:$basePath/lib/*:";

system("xterm -e /bin/sh");
