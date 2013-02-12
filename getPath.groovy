/** List out all of the directories in the PATH variable. */
def env = System.env
env.PATH.split(';').each {
  println it
}