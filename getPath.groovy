/** List out all of the directories in the PATH variable. */
def env = System.env
assert env
def path= env.PATH?:env.Path
assert path
path.split(';').each {
  println it
}