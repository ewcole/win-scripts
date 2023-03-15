my $level=0;
sub debug {
    my $txt = shift;
    print ((" " x ($level*4)).$txt);
}

sub calc {
    $level++;
    $line = shift;
    $line =~ s/\s+//g;
    $line =~ s/^\s+//;
    $line =~ s/\s+$//;
    $rlst = $line;
    #debug "calc($line)\n";
    if (length($line) == 0) {
        $rslt = 0;
    } elsif ($line =~ /^(\d+\.?\d*)$/) {
        debug "value=$1\n";
        $rslt = $1;
    } elsif ($line =~ /^\d+(\:\d+)+/) {
        #sexagesimal number
        my $ln = $line;
        $rslt=0;
        while ($ln=~/(\d+)\:(.*)/) {
	    debug "$ln: $rslt >> ";
	    $rslt = (60*$rslt) + $1;
	    debug "$rslt\n";
	    $ln=$2;
	}
        $rslt = (60*$rslt) + $ln;
    } elsif ($line =~ /(.*)\(([^\)]+)\)(.*)/) {
        (my $a, my $b, my $c) = ($1, $2, $3);
        debug "eval($b)\n";
        my $d = calc($b);
        $rslt = calc("$a $d $c");
    } elsif ($line =~ /^(.*?)\s*\+\s*(\S.*?)$/) {
        (my $a, my $b) = ($1, $2);
        debug "add($a, $b)\n";
        $rslt = calc($a) + calc($b);
        debug "  $a + $b = $rslt\n";
    } elsif ($line =~ /^(.*)\s*\-\s*(\S.*?)$/) {
        (my $a, my $b) = ($1, $2);
        debug "sub($a, $b)\n";
        $rslt = calc($a) - calc($b);
        debug "  $a - $b = $rslt\n";
    } elsif ($line =~ /^(.*?)\*(.*)$/) {
        (my $a, my $b) = ($1, $2);
        debug "mult($a, $b)\n";
        $rslt = calc($a) * calc($b);
        debug "  $a * $b = $rslt\n";
    } elsif ($line =~ /(.+)\/(.+)/) {
        (my $a, my $b) = ($1, $2);
        debug "div($a, $b)\n";
        $rslt = calc($a) / calc($b);
        debug "$a / $b = $rslt\n";
    #} elsif ($line =~ /^sexg(.*)\)$/i) {
    #    my $d = $1;
    #    my $r = 0;
    #    $rslt = "";
    #    while ($d > 60) {
    #        $r = $d % 60;
    #	    $d = ($d - $r)/60;
    #        $rslt = (sprintf("%02d",$r)).":$rslt";
    #	}
    #    $rslt = $d . ":$rslt";
    } else {
        die "Invalid operand: ($line)\n";
    }
    $level--;
    $rslt;
}

#while (@ARGS) {
#    $cmd .= " ". shift @ARGS;
#}

print ("$cmd = " . calc($cmd) . "\n");
while(<>) {
    chomp;
    print ("= " . calc($_) . "\n");  
}
