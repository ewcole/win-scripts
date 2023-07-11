def fileList = "git ls-files".execute().text
def submoduleList = "git ls-files --stage | grep 160000".execute().text
def gitBranch = ("git branch".execute().text.readLines().grep {it =~ /^\*/}.collect {
  it.replaceAll(/^\* /,'')
}?:[]).first();
def allFiles = new ConfigObject()

def readFiles;
readFiles = {
  ConfigObject cfg
}

def debug(String text) {
  // println text
}

fileList.readLines().each {
  line ->
    def path = line.split("/")
    //debug path
    def cfg = allFiles;
    if (path.size() > 1) {
      cfg = path[0..-2].inject(allFiles) {
        root, dirname ->
          //debug ">> $root, $dirname"
          root.dir."$dirname"
      }
    }
    def files = (cfg.files?:[])
    files << path[-1]
    cfg.files = files;
    //debug cfg
} 
//debug allFiles

String getProtocol(String fileName) {
  def ext = fileName.replaceAll(/.*\./,'').toLowerCase();
  // println "# ext = $ext"
  def extList = ['html':    'file+emacs',
                 'pdf':     'file+sys',
                 'dat':     'file+emacs',
                 'service': 'file+emacs',
                 'docx':    'file+sys',
                 'xlsx':    'file+sys',
  ]
  String protocol = extList.containsKey(ext)?extList[ext]:'file'
  //println "# protocol=$protocol"
  return protocol
};

def printDir(ConfigObject cfg, int level, String dirName, String path) {
  debug "# in printDir(${cfg.keySet()}, $level, $dirName, $path)"
  debug "cfg=$cfg"
  if (cfg.files.size() || cfg.dir.keySet().size() > 1) {
    println "${'*' * level} $dirName"
    cfg.files.each {
      println "- [[${getProtocol(it)}:$path$it][$it]]"
    }
    debug "# cfg.dir=${cfg.dir.keySet()}"
    cfg.dir.keySet().each {
      dir ->
        debug "# dir=$dir"
        def dd = cfg.dir."$dir"
        debug "dd=$dd"
        printDir(cfg.dir."$dir", level + 1, dir, "$path$dir/")
    }
  }
  else { 
    cfg.dir.each {
      subDirName, dirContents ->
        printDir(dirContents, level, "$dirName/$subDirName", 
                 "$path$subDirName/")
    }
  }
}

def printHeader = {
println """#+TITLE: Working Files
#+STARTUP: content
#+OPTIONS: toc:nil num:nil html-preamble:nil html-postamble:nil ^:{} 
""".replaceAll("\r?\n", System.getProperty('line.separator'))
}

def printGitState = {
"""* Git
- current branch :: ${gitBranch}
** Remotes""".readLines().each {
    println it
}
  // Fetch Remotes
  "git config --local -l".execute().text.readLines().grep {
    it =~ /^remote\..*\.url/
  }.collect {
    rm ->
    (rm =~ /^remote\.([^.]*)\.url=(.*)/).each {
      m -> println "- ${m[1]} :: ${m[2]}"
    }
  }

}
printHeader()
// printGitState()
printDir(allFiles, 1, "Files", "");
