#! /usr/bin/perl
# findexec.pl -- tell us which program is being executed.
require 5.004;

# override CORE::glob in current package
use File::DosGlob 'glob';

my $usage = <<USAGE;

findexec [--help] <commandName>

List all executables on the path that match the pattern, in the 
order that they would be found in a search.

    --help    - display this message
    --verbose - Show all searches
    <commandName>  The name of the program

USAGE
my $verbose = 0;
if (@ARGV == 0) {
    die $usage
} elsif (@ARGV[0] =~ /--help/i or @ARGV[0] =~ /-h/i) {
    die $usage;
} elsif (@ARGV[0] =~ /(--verbose)|(-v)/i) {
    shift;
    $verbose = 1;
}

my $command = shift;
#print "\$command = '$command'\n";
my $path = `path`;
chomp $path;
$path =~ s/^PATH=//;
my @path = split ";", $path;
#foreach (@path) {
#    print "$_\n";
#}
my $pathExt = `set pathext`;
chomp $pathExt;
$pathExt =~ s/^PATHEXT=//;
my @pathExt = split ";", uc $pathExt;
$baseCommand = $command;
if ($command =~ m/(.*)(\.[^\.]*)$/) {
    #print "blob's extension = $1.;\n";
    @pathExt = ((uc $2));
    $baseCommand = $1;
}

my $emacs = "\\\\jccadmin003\\mis common\\emacs\\emacs-21.3\\bin\\runemacs.exe";

foreach my $dir (@path) {
    $dir =~ s/\\$//;
    foreach my $ext (@pathExt) {
	my $srch = "$dir\\$baseCommand$ext";
        
        $verbose && print "#$srch\n";
        my $cmd = qq(\@echo off & for \%f in ("$srch") do (if exist %f echo \%~ff));
        #foreach $file (glob "\"$srch\"") {
        #    print "$file\n\n";
        #}
        $verbose && print "#$cmd\n";
        print `$cmd`;
    }
}
