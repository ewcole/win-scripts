def fileList = "git ls-files".execute().text
def submoduleList = "git ls-files --stage | grep 160000".execute().text
def gitBranch = "git branch".execute().text.readLines().grep {it =~ /^\*/}.collect {
  it.replaceAll(/^\* /,'')
}.first();
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
  def extList = ['html': 'file+emacs',
                 'pdf':  'file+sys',
                 'service': 'file+emacs']
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
* Config    :noexport:
#+STARTUP: content
#+OPTIONS: ':nil *:t -:t ::t <:t H:3 \\n:nil ^:{} arch:headline
#+OPTIONS: author:t c:nil creator:comment d:(not "LOGBOOK") date:t
#+LANGUAGE: en
#+SELECT_TAGS: export
#+OPTIONS: html-link-use-abs-url:nil html-postamble:nil
#+OPTIONS: html-preamble:nil html-scripts:t html-style:t
#+OPTIONS: html5-fancy:nil tex:t
#+CREATOR: <a href=\"http://www.gnu.org/software/emacs/\">Emacs</a> 24.2.1 (<a href=\"http://orgmode.org\">Org</a> mode 8.2.6)
#+HTML_CONTAINER: div
#+HTML_DOCTYPE: xhtml-strict

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
