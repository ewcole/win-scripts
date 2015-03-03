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
    !(f.name =~ /^(\..*)|#.*#|.*~|.*junk.*|.*\.log$/) as Boolean
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

readDirectory(root, '')
root.listFiles().grep{it.isDirectory()}.grep{showDirectory(it)}.collect{it.name}