assert args.size() == 2
def d = Date.parse('yyyy-MM-dd', "${args[0]}-${args[1]}-01")
def wk = ['  '] * d.day;
def m = d.month
def monthDates = []
for (def d2 = d; d2.month == d.month; d2++) {
    monthDates << d2
}
// println monthDates;
def cal = monthDates.inject([wk: wk, weeks: []]) {
    dateObj, currDay ->
       //println currDay.day; 
       if (currDay.day > 0) {
           dateObj.wk << currDay.format('dd');
           //println dateObj.wk
       } else {
           dateObj.weeks << dateObj.wk;
           dateObj.wk = [currDay.format('dd')];
       }
       return dateObj;
}
cal.weeks << cal.wk;
println d.format("MMMMMM yyyy").center(21)
println "Su Mo Tu We Th Fr Sa"
cal.weeks.collect {it.join(' ');}.each {println it}