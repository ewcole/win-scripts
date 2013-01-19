/** Compare two files or a file with its previous versions using the
    emacs ediff command.  This groovy script takes the name from the
    command line.

    usage: Compare files using the emacs ediff command.
     -h,--help       Show this screen
     -r,--revision   Compare with previous revisions

*/

/*  Copyright (c) 2013 Edward W. Cole

    Permission is hereby granted, free of charge, to any person
    obtaining a copy of this software and associated documentation
    files (the "Software"), to deal in the Software without
    restriction, including without limitation the rights to use, copy,
    modify, merge, publish, distribute, sublicense, and/or sell copies
    of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
    HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.
*/

import groovy.util.*

def cli = new CliBuilder(usage: "Compare files using the emacs ediff command.")
cli.h(longOpt: "help", "Show this screen")
cli.r(longOpt: "revision", "Compare with previous revisions")
def opt = cli.parse(args);
String emacsCmd = "cmd /c start runemacs "

if (opt?.h) {
  cli.usage()
} else if (opt?.r) {
  println "Revisions"
  opt.arguments().each {
    def f = new File(it)
    def r = '\\'
    def fileName = f.toString().replace(r, '/');
    def elScript = "(ediff-revision \"$fileName\")".replace('"', '\\"')
    println "$emacsCmd --eval \"$elScript\"".execute().text
  }
} else {
  def fl = opt.arguments().collect {
    def f = new File(it)
    def r = '\\'
    def fileName = f.toString().replace(r, '/')
  }
  println fl;
  if (fl.size() == 2) {
    def elScript = "(ediff \"${fl[0]}\" \"${fl[1]}\")".replace('"', '\\"')
    println "$emacsCmd --eval \"$elScript\"".execute().text
  } else {
    cli.usage()
  }
}