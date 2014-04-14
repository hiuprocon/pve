#!/usr/bin/env perl

use FindBin;

$basePath = $FindBin::RealBin;

chdir $basePath;

$ENV{CLASSPATH}=".:$basePath/lib/*:";

system("java prototype.AllClients2");
