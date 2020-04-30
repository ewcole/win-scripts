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
                 'org': 'file',
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
      def search = "::/${it.replaceAll(/(?i)[^a-z0-9 ]/, '.')}/"
      //println "path=$path"
      String filePath = "$path".replaceAll(/[\/\\]$/, '');
      println "- [[${getProtocol(dirName)}:$filePath$search][Edit]]${getEditLink('',filePath)}: $it"
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
* Config   :noexport:
#+OPTIONS: ':nil *:t -:t ::t <:t H:3 \\n:nil ^:{} arch:headline num:nil
#+OPTIONS: author:t c:nil creator:comment d:(not "LOGBOOK") date:t
#+LANGUAGE: en
#+SELECT_TAGS: export
#+OPTIONS: html-link-use-abs-url:nil html-postamble:nil
#+OPTIONS: html-preamble:nil html-scripts:t html-style:t
#+OPTIONS: html5-fancy:nil tex:t
#+html_head: <style type="text/css">
#+html_head: body {
#+html_head:     font-family: Calibri;
#+html_head: }
#+html_head: td {
#+html_head: 	border: 1px solid silver;
#+html_head: 	padding: 1px 5px;
#+html_head: }
#+html_head: 
#+html_head: th {
#+html_head: 	color: white;
#+html_head: 	background-color: #040;
#+html_head: 	padding: 3px 9px;
#+html_head:         border:  1px solid white;
#+html_head: }
#+html_head: 
#+html_head: pre {
#+html_head:    margin: 0;
#+html_head:    margin-left: 2em;
#+html_head:    padding: 0 0 0 3px;
#+html_head: }
#+html_head: </style>
#+html_head: <style type="text/css">
#+html_head: body {
#+html_head:     font-family: Calibri;
#+html_head: }
#+html_head: td {
#+html_head: 	border: 1px solid silver;
#+html_head: 	padding: 1px 5px;
#+html_head: }
#+html_head: 
#+html_head: th {
#+html_head: 	color: white;
#+html_head: 	background-color: #040;
#+html_head: 	padding: 3px 9px;
#+html_head:         border:  1px solid white;
#+html_head: }
#+html_head: 
#+html_head: pre {
#+html_head:    margin: 0;
#+html_head:    margin-left: 2em;
#+html_head:    padding: 0 0 0 3px;
#+html_head: }
#+html_head: </style>
#+html_head: <style type="text/css">
#+html_head: body {
#+html_head:     font-family: Calibri;
#+html_head: }
#+html_head: td {
#+html_head: 	border: 1px solid silver;
#+html_head: 	padding: 1px 5px;
#+html_head: }
#+html_head: 
#+html_head: th {
#+html_head: 	color: white;
#+html_head: 	background-color: #040;
#+html_head: 	padding: 3px 9px;
#+html_head:         border:  1px solid white;
#+html_head: }
#+html_head: 
#+html_head: pre {
#+html_head:    margin: 0;
#+html_head:    margin-left: 2em;
#+html_head:    padding: 0 0 0 3px;
#+html_head: }
#+html_head: ul.org-ul {
#+html_head: 	list-style: none;
#+html_head: }
#+html_head: li {
#+html_head: 	font-family: Consolas;
#+html_head: 	background-color: #d6fff9;
#+html_head: 	border: 1px solid silver;
#+html_head: 	margin: 3px;
#+html_head: }
#+html_head: </style>
""".replaceAll("\r?\n", System.getProperty('line.separator'))
}
printHeader()
printDir(allFiles, 1, "Files", "");
