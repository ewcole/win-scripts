#! /usr/bin/perl
###########################################################
# which.pl -- tell us which program is being executed when 
# you type a command at the CMD.EXE command line.
###########################################################
# First, show a help message if requested.
my $usage = <<USAGE;

which [--help] <commandName>

Give the directory and name of the file that is executed 
when you run the command.

    --help - display this message
    <commandName>  The name of the program

USAGE

if (@ARGV[0] =~ /--help/i) {
    die $usage;
} 

# Get the name of the command we will search for.
my $command = shift;
# Now, get the PATH from windows (it might be easier to
#    take the path from the environment variable, but this
#    is how I did it first).
my $path = `path`;
chomp $path;
$path =~ s/^PATH=//;
# Windows always looks at the current directory first.
$path = ".;" . $path;
my @path = split ";", $path;
# @path now holds the list of directories in the search 
#     path, in order.
my $pathExt = `set pathext`;
# The Windows PATHEXT variable is a list of file extensions
#    in the order that they will be used.
chomp $pathExt;
$pathExt =~ s/^PATHEXT=//;
my @pathExt = split ";", uc $pathExt;
# Check to see if we have an extension specified in the 
#     input; if so, we ignore all other file types.
$baseCommand = $command;
if ($command =~ m/(.*)(\.[^\.]*)$/) {
    #print "blob's extension = $1.;\n";
    @pathExt = ((uc $2));
    $baseCommand = $1;
}
  
# Now, to do the real work.  We go to each directory in 
#    turn and see if there is a matching program.
foreach my $dir (@path) {
    $dir =~ s/\\$//;
    # Go through the extensions on the PATHEXT list 
    #    and see if there is a match.
    foreach my $ext (@pathExt) {
	my $srch = "$dir\\$baseCommand$ext";
        if (-e $srch) {
            # The file is found, use the Windows shell to 
            #     print it neatly.
            print `echo off & for \%f in ("$srch") do echo \%~ff`;
            exit;   # Only print the first one.
	}
    }
}
