/** List the drive letters that are available for subst or net use. */

def cmds = [ 
  [cmd: 'subst', re: ~/^([A-Z]:)/],
  [cmd: 'net use', re: ~/^[^ ]+ +([A-Z]:) +/],
]
def usedDrives = cmds.inject(['C:', 'D:',]) {
  arr, val ->
    String txt = val.cmd.execute().text
    //println txt
    txt.eachLine {
      line ->
        (line =~ val.re).each {
          m ->
            if (m.size() > 1) {
              arr << (m[1] as String)
            }
        }
    }
    arr
}.flatten().sort()
//println usedDrives
def allDrives = ('A'..'Z' as List).collect{"$it:"}
//println allDrives
(allDrives - usedDrives).sort().each {
  println it
}
