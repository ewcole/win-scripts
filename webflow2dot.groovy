/* Convert a Spring webflow XML file to DOT */
// Get the input and output files.
def cli = new CliBuilder(usage: "Convert a Spring webflow XML file to DOT");
cli.h(longOpt: 'help', required: false, 'show usage information' )
cli.i(longOpt: 'input', required: true, args: 1, 'Input file' )
cli.o(longOpt: 'output', required: true, args: 1, 'Output file' )

def opt = cli.parse(args);
if (!opt) {
  return
} 
if (opt.h) {
  cli.usage()
  return
}
def input = new File(opt.i)
def output = new File(opt.o)

if (!input.exists()) {
  System.err.println("Cannot find $input")
  return;
}

/** How to portray the states */
def boxAttrs = [
  'action-state': [shape: "record",
                   color: "blue",
                   labelRule: {state ->
      String stateId = "${state.@id}";
      String evals = state.evaluate*.collect {it.@expression as String}.flatten().
                     collect {"<tr><td>eval $it</td></tr>"}.join('');
      "<<table><tr><td><b>${stateId}</b></td></tr>${evals}</table>>"
    }],
  'decision-state': [shape: "diamond",
                     labelRule: {
      state ->
        String testText = state.if*.@test as String
        String stateLabel = "${state.@id}".size()?
          "${state.@id}":'"decision-state"';
        // if (testText?.size()) {
        //   return "\"${stateLabel}\\n$testText}\""
        // } else 
      return "\"${stateLabel}\""
    }],
  'end-state': [shape: "box"],
  'global-transitions': [shape: "box"],
  'on-start': [shape: "box"],
  'subflow-state': [shape: "box"],
  'view-state': [shape: "note"],
  'var':   [shape: "octagon",
            labelRule: {state -> "${state.@name}"}],
  'input': [shape: "box", color: "blue"]
]

def showStateBox = {
  state, w ->
    def boxId = "${state.@id}"
    if (boxId.size()) {
      def boxName = boxId?:state.name()
      String boxLabel = boxName
      if (boxAttrs[state.name()].labelRule) {
        boxLabel = boxAttrs[state.name()].labelRule(state);
      }
      def boxRules = boxAttrs[state.name()] + [label: boxLabel]
      boxRules.remove('labelRule')
      def attrString = boxRules.collect {
        key, value ->
          "$key=$value"
      }.join(',');
      w.println "  ${boxName} [$attrString]"
      state.transition.each {
        t ->
          w.println "  ${boxId} -> ${t.@to} [label=\"${t.on}\"]"
      }
    }
}

def elemRules = boxAttrs.keySet().inject([:]) {
  ruleMap, stateName ->
    ruleMap[stateName] = showStateBox;
    return ruleMap;
}

def extendElem = {
  String name, Closure c ->
    def x = elemRules[name]
    if (x) {
      elemRules[name] = {
        state, w ->
          x(state,w);
          c(state,w);
      }
    } else {
      elemRules[name] = c;
    }
}

extendElem('decision-state') {
  state, w ->
    def id = state.@id as String
    state.if.each {
        f ->
          def testCode = (f.@test as String).replaceAll(/ and/, '\\\\nand');
          def thenId = f.@then as String
          def elseId = f.@else as String
          if (testCode?.size()) {
              
          }
          if (thenId?.size()) {
            w.println "  $id -> $thenId [label=\"${testCode}\"]"
          }
          if (elseId?.size()) {
            w.println "  $id -> $elseId [label=\"not(${testCode})\"]"
          }
      }
}

// Process the input file
output.withWriter {
  o ->
    o.println "digraph webflow {"
    o.println "  rkdir=LR;"
    def flow = new XmlSlurper().parse(input)
    flow.children().each {
      elem ->
        def name = elem.name()
        def rule = elemRules[name] 
        if (rule) {
          //def x = elemRules[name]
          // println ">> $name -> ${x.getClass()}"
          rule(elem, o)
        } else {
          println "> no rule for ${name}"
        }
    }
    o.println "}"
}
