# BACKUP_FILE.AWK -- Create a copy of a file or files with the date as part of the name.
#
# INPUT:  standard input will be a directory listing with all of the files we want to 
#         backup.

function backupFileName(name, date, time) {
    split(date, dateArr, "/")
    if (dateArr[3] < 100) { 
        if (dateArr[3] > 80) dateArr[3] += 1900;
        else dateArr[3] += 2000;
    }
    #print("   date: " dateArr[0] ": " dateArr[1] "/" dateArr[2] "/" dateArr[3])
    dateStr = dateArr[3] "-" dateArr[1] "-" dateArr[2]
    #print "    " dateStr
    fnArrLen = split(name, fnArr, "\.")
    period = ""
    for (i=1; i < length(fnArr); i++) {
        newName = period fnArr[i]
    }
    newName = newName "-" dateStr "." fnArr[length(fnArr)]
    #print newName
    return newName
}

function backup_file(currentDir, name, date, time) {
    newName = backupFileName(name, date, time)
    newPathName = currentDir "\\" newName
    fileExists = "N"
    cmdstr = "if exist " newPathName " echo Y"
    while ((cmdstr | getline str) >0) {
        if (str == "Y") fileExists = "Y"
    } 
    close cmdstr
    if (fileExists == "Y") {
        print "The file " newName " already exists.  "
        print "    It will not be replaced."
    } else {
        print " "
        print name "*******************************"
        print " "
        cmdstr = "copy \"" currentDir "\\" name "\" \"" newPathName "\""
        #print "cmdstr = " cmdstr
        print "Creating " newPathName
        while ((cmdstr | getline str) > 0) {
            print str
            #print "*"
        }
        close cmdstr
    }
}

BEGIN {
    currentDir = "."
}

/ Directory of / {
    currentDir = substr($0, 15)
    if (currentDir ~ /\\$/) {
        currentDir = substr(currentDir, 1, length(currentDir)-1)
    }
    #print "currentDir = " currentDir "."
}


/^[0-9]/ {
    if ($3 != "<DIR>") {
        name = substr($0, 40)
        backup_file(currentDir, name, $1, $2)
    }    
}