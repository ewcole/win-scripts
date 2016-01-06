import groovy.xml.*;
/** List the ports in use and tell us what process is using them.
 */
def outputFormat = "TEXT";
def cli = new CliBuilder();
cli.h( longOpt: 'help', required: false, 'show usage information' );
cli.x( longOpt: 'xml', required: false, 'Output in XML');
def opt = cli.parse(args);
if (opt?.x) { 
  outputFormat = "XML";
}

def cmd = [ 
  netstat: [
    cmd: "netstat -a -n -o",
  ],
  tasklist: [
    cmd: "tasklist /v /fo LIST",
  ],
];

cmd.each {
  key, value ->
    String command = cmd[key].cmd;
    cmd[key].output = command.execute().text;
}

port_list = [];
// Parse netstat
String ns_out = cmd.netstat.output;
def netstat_match = "^.." + "(.......)" +
  "(.......................)" +
  "(.......................)" +
  "(................)" +
  "([0-9]+)"
  ;
ns_out.split(/\n/).each {
  line ->
    def mre = (line =~ netstat_match);
    mre.each {
        
      m ->
        def port = [:];
        String protocol       =m[1];
        String localAddress   =m[2];
        String foreignAddress =m[3];
        String state          =m[4];
        String pid            =m[5];
        port.protocol = protocol.replaceAll(" +\$", "");
        port.localAddress = localAddress.replaceAll(" +\$", "");
        port.foreignAddress = foreignAddress.replaceAll(" +\$", "");
        port.state = state.replaceAll(" +\$", "");
        port.pid = pid.replaceAll(" +\$", "");
        (localAddress =~ /(.*):([0-9]+)/).each { 
          m2 ->
            port.port = m2[2];
            port.localAddress = m2[1];
        }
        port_list.add(port);
    }
}

processes = [:];
proc = [:];
cmd.tasklist.output.split(/\n/).each {
  line ->
    //println(">>>${line}");
    if ((line =~ /^ *$/) && (proc.PID =~ /[0-9]+/)) {
      // save the data
      processes[proc.PID] = proc;
      //println("Process=${proc}");
      proc = [:];
    } else {
      (line =~ /^(.*): *(.*)/).each {
        m ->
          //println "m=${m}";
          String value = m[2];
          String k = m[1].replaceAll(/ +/, "").replaceAll(/#/, "Num");
          proc[k] = value;
      }
    }
}

if (outputFormat == "XML") { 
  def s = new StringWriter();
  def xml = new MarkupBuilder(s);
  xml.ports() { 
    port_list.sort{it.port.toInteger()}.each { 
      p ->
        port(p) { 
          if (processes[p.pid]) { 
            process(processes[p.pid]) { 
            }
          }
        };
    }
  }
  println(s);
} else { 
  // sort it by port number
  port_list.sort{it.port.toInteger()}.each { 
    p ->
      r = processes[p.pid]
      println("${p.port}\t${p.protocol}\t${p.pid}\t${r?.ImageName}\t" +
              "${r?.SessionName}\t" +
              "${(r?.WindowTitle=='N/A'?'':r?.WindowTitle)}");
  }
}