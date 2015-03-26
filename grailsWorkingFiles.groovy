// Produce a working files document for the current Grails project.

def ignoreDirs = [
  '/target', '/.git'
]

def stack = []

////////////////////////////////////////////////////////////////////////
// 1. Go to the root directory
////////////////////////////////////////////////////////////////////////
def root = new File('.')
println "root=${root.getCanonicalFile()}"

/** Should we show this file? */
def showFile = {
  File f ->
    !(f.name =~ /^(\..*)|#.*#|.*~|.*junk.*|.*\.log|.*\.class$/) as Boolean
}

/** Should we print this directory? */
def showDirectory = {
  File d ->
      if (d.name =~ /^\..*|target|wrapper$/) {
          return false
      }
      return true;
}

def pushDir = {
    File dir, String subPath ->
        stack << [dir: dir, path: subPath]
        def stars = "*" * stack.size()
        println "$stars ${subPath?:'Files'}"
}    

def popDir = {
    ->
            stack.pop()
}
readDirectory = {
  File dir, pathStr ->
    def files = dir.listFiles().grep{! it.isDirectory()}.grep{showFile(it)}
    def dirs = dir.listFiles().grep{it.isDirectory()}.grep{showDirectory(it)}.grep{it != dir}
    // Consolidate directories if there are no side branches.
    if (files.size() || dirs.size() > 1) {
        pushDir(dir, pathStr);
        files.each {
            file ->
            def fUrl="${file}"
            println "- [[file:$fUrl][${file.name}]]"
        }
        dirs.each {
            readDirectory(it, it.name)
        }
        popDir()
    } else if (dirs.size()) {
        // merge directory paths together
        readDirectory(dirs[0], "${pathStr}/${dirs[0].name}")
    }
}

def printHeader = {
println """#+TITLE: Working Files
#+STARTUP: showeverything
#+OPTIONS: ':nil *:t -:t ::t <:t H:3 \\n:nil ^:{} arch:headline
#+OPTIONS: author:t c:nil creator:comment d:(not "LOGBOOK") date:t
#+OPTIONS: e:t email:nil f:t inline:t num:nil p:nil pri:nil stat:t
#+OPTIONS: tags:t tasks:t tex:t timestamp:t toc:1 todo:t |:t
#+CREATOR: Emacs 24.2.1 (Org mode 8.2.6)
#+DESCRIPTION:
#+EXCLUDE_TAGS: noexport
#+KEYWORDS:
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
#+LATEX_HEADER:"""
}
printHeader()
readDirectory(root, '')
root.listFiles().grep{it.isDirectory()}.grep{showDirectory(it)}.collect{it.name}