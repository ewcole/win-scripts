///////////////////////////////////////////////////////////////////////////////
// which.groovy -- tell us which program is being executed when
// you type a command at the CMD.EXE command line.
///////////////////////////////////////////////////////////////////////////////

def cli = new CliBuilder(usage: """which [--help] <commandName>

Give the directory and name of the file that is executed
when you run the command.

    <commandName>  The name of the program

""")
cli.h(longOpt: "help", "display this message");
cli.a(longOpt: "all", "print all matches");
def opt = cli.parse(args);

if (!opt || !(opt.arguments()?.size())) {
  cli.usage();
  return;
}
// Get the name of the command we will search for.
String command = opt.arguments().first();
// Now, get the PATH from windows (it might be easier to
//    take the path from the environment variable, but this
//    is how I did it first).

// Windows always checks the current directory first.
def sysPath = System.env[System.env.keySet().grep{it ==~ /(?i)PATH/}.first()];
def path = ".;${sysPath}".split(";");
// path now holds the list of directories in the search
//     path, in order.

// The Windows PATHEXT variable is a list of file extensions
//    in the order that they will be used.
def pathExt = System.env[System.env.keySet().grep{
    it ==~ /(?i)PATHext/
  }.first()].split(";").collect{it.toUpperCase()};
// Check to see if we have an extension specified in the
//     input; if so, we ignore all other file types.
(command =~ /(.*)(\.[^.]+)$/).each {
  m ->
  pathExt = ["${m[2]}".toUpperCase()];
  command = "${m[1]}"
}
// Now, to do the real work.  We go to each directory in
//    turn and see if there is a matching program.
def exe = path.inject([]) {
  exeList, dirName ->
    if (! exeList.size()) {
      def dir = new File(dirName);
      // Go through the extensions on the PATHEXT list
      //    and see if there is a match.
      pathExt.each {
        ext ->
          def srch = new File(dir, "$command$ext");
          if (srch.exists()) {
            exeList << srch.getCanonicalFile();
          }
      }
    }
    return exeList;
}
if (exe.size()) {
  if (opt.a) {
    // System.err.println('print all');
    exe.each {println it}
  } else {
    println exe.first();
  }
}
