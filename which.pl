# which.pl -- tell us which program is being executed.
my $usage = <<USAGE;

which [--help] <commandName>

Give the directory and name of the file that is executed when you run the 
command.

    --help - display this message
    <commandName>  The name of the program

USAGE
my $editCmd = 0;
if (@ARGV[0] =~ /--help/i) {
    die $usage;
} elsif (@ARGV[0] =~ /(--edit)|(-e)/i) {
    shift;
    $editCmd = 1;
}

my $command = shift;
#print "\$command = '$command'\n";
my $path = `path`;
chomp $path;
$path =~ s/^PATH=//;
my @path = split ";", $path;
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
#foreach (@pathExt) {
#    print "pathext $_\n";
#}
my $emacs = "\\\\jccadmin003\\mis common\\emacs\\emacs-21.3\\bin\\runemacs.exe";
  
foreach my $dir (@path) {
    $dir =~ s/\\$//;
    foreach my $ext (@pathExt) {
	my $srch = "$dir\\$baseCommand$ext";
        #print "::$srch\n";
        if (-e $srch) {
            #print qq(echo off & for \%f in ("$srch") do echo \%~ff\n);
            print `echo off & for \%f in ("$srch") do echo \%~ff`;
            exit;   # Only print the first one.
	}
    }
}
