// def submoduleList = "git ls-files --stage | grep 160000".execute().text
def allFiles = new ConfigObject()

def crlf = System.getProperty('line.separator');
String fs = System.getProperty('file.separator')
def readFiles;
readFiles = {
  ConfigObject cfg
}

def debug(String text) {
  // println "#D> $text"
}

System.in.readLines().each {
  line ->
  (line =~ /^(.*?):(.*)/).each {
    lineMatch ->
    def pathName = lineMatch[1];
    def text = lineMatch[2]
    def path = pathName.split(/(\\|\/)/)
    // debug "pathName = $pathName"
    // debug "text = $text"
    // debug "path = $path"
    //debug path
    def cfg = path.inject(allFiles) {
      root, dirname ->
      // debug ">> $root, $dirname"
      root.dir."$dirname"
    }
    def textLines = (cfg.textLines?:[])
    textLines << text
    cfg.textLines = textLines;
    //debug cfg
  }
}
//debug allFiles

String getProtocol(String fileName) {
  def ext = fileName.replaceAll(/.*\./,'').toLowerCase();
  // println "# ext = $ext"
  def extList = ['html': 'file+emacs',
                 'pdf':  'file+sys',
                 'org': 'file+emacs',
                 //'properties': 'file+emacs',
                 'service': 'file+emacs',]
  String protocol = extList.containsKey(ext)?extList[ext]:'file'
  //println "# protocol=$protocol"
  return protocol
};

String getEditLink(String path, String fileName) {
  String protocol = getProtocol(fileName);
  String link = "";
  (protocol =~ /(.*)\+(.*)/).each {
    m ->
    if (m[2] == 'emacs') {
      link += " ([[file+sys:$path$fileName][start]])"
    }
  }
  return link;
}
def printDir(ConfigObject cfg, int level, String dirName, String path) {
  debug "# in printDir(${cfg.keySet()}, $level, $dirName, $path)"
  // debug "cfg=$cfg"
  debug "cfg.dir.keySet() == ${cfg.dir.keySet()}"
  debug "cfg.textLines == ${cfg.textLines}"
  // This if statement consolidates empty directories
  if (cfg.textLines.size() || cfg.dir.keySet().size() > 1) {
    println "${'*' * level} $dirName"
    cfg.textLines.each {
      def textRe = it.replaceAll(/(?i)[^a-z0-9 ]/, '.')
      //println "path=$path"
      String filePath = "$path".replaceAll(/[\/\\]$/, '');
      println "- [[${getProtocol(dirName)}:$filePath::/$textRe/][Edit]]${getEditLink('',filePath)}: $it"
    }
    debug "# cfg.dir=${cfg.dir.keySet()}"
    cfg.dir.keySet().each {
      dir ->
      debug "# dir=$dir"
      def dd = cfg.dir."$dir"
      debug "dd=$dd"
      String fs = System.getProperty('file.separator')
      printDir(cfg.dir."$dir", level + 1, dir, "$path$dir$fs")
    }
  } else { 
      String fs = System.getProperty('file.separator')
      cfg.dir.each {
        subDirName, dirContents ->
            printDir(dirContents, level, "$dirName$fs$subDirName", 
                     "$path$subDirName$fs")
    }
  }
}

// #+STARTUP: showeverything

def printHeader = {
println """#+TITLE: Working Files
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
#+HTML_HEAD:
#+HTML_HEAD_EXTRA:
#+HTML_LINK_HOME:
#+HTML_LINK_UP:
#+HTML_MATHJAX:
#+INFOJS_OPT:
#+LATEX_HEADER:""".replaceAll("\r?\n", System.getProperty('line.separator'))
}
printHeader()
printDir(allFiles, 1, "Files", "");
