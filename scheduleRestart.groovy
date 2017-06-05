def cli = new CliBuilder(usage: "Set the computer to restart at a given time")
cli.d(longOpt: "date", args: 1, required: false, "The day to restart (yyyy-mm-dd)")
cli.m(longOpt: "tomorrow", required: false, "Restart tomorrow at the given time")
cli.t(longOpt: "time", args: 1, required: true, "The time to restart (hh24:mi)")

def opt = cli.parse(args);
//def timeRe = /^[0-2]?[0-9](\:[0-6][0-9])?$/
def timeRe = /[0-2]?[0-9](\:[0-6][0-9])?/
if (!opt) {
  return
} else if (! (opt =~ timeRe)) {
  System.err.println ""
  System.err.println "Please enter time as hh24:mi (you gave me $opt.t)"
  System.err.println ""
  cli.usage()
  return;
}
// Now we have a time in an acceptable format
if (opt.m && opt.d && opt.d.size()) {
  System.err.println ""
  System.err.println "Please use only one of the options -m and -d"
  System.err.println ""
  cli.usage();
  return;
}
def restartTime = new Date().clearTime();
if (opt.m) {
  restartTime += 1;
} else {
  if (opt.d && opt.d?.size()) {
    restartTime = Date.parse("yyyy-MM-dd", opt.d)
  }
}
restartTime = Date.parse("yyyy-MM-dd hh:mm", "${restartTime.format('yyyy-MM-dd')} ${opt.t}")
def restartSeconds = Math.round((restartTime.time - new Date().time)/1000);
if (restartSeconds < 0) {
  System.err.println ""
  System.err.println "Cannot schedule restart because specified time is in the past.  $restartTime"
  System.err.println ""
} else {
  def cmd = "shutdown /r /t $restartSeconds"
  println cmd;
  println "$cmd".execute().text
}
