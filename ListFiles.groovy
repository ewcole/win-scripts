def cli = new CliBuilder(usage: "List interesting files in directory")
//cli.r(longOpt: 'exclude_regex'

def start = new File('.')
def zix = start.getCanonicalFile();
def trimStr = zix.path

println trimStr
