
# Convert a .csv file into an XML file.  Assume that the first line
# contains column headers.
#
#    Copyright (c) 2013 Edward W. Cole
#
#   Permission is hereby granted, free of charge, to any person
#   obtaining a copy of this software and associated documentation
#   files (the "Software"), to deal in the Software without
#   restriction, including without limitation the rights to use, copy,
#   modify, merge, publish, distribute, sublicense, and/or sell copies
#   of the Software, and to permit persons to whom the Software is
#   furnished to do so, subject to the following conditions:
#
#   The above copyright notice and this permission notice shall be
#   included in all copies or substantial portions of the Software.
#
#   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
#   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
#   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
#   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
#   HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
#   WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
#   DEALINGS IN THE SOFTWARE.

# Take the headers from the first line.
my @headers = map {s/^"(.*)"$/$1/;$_;} (split ",", <>);
my $file_name = $ARGV;
my $i = 0;
my @data;

# Create attribute names from the data in the first row.  If there is no
# data in a column, make up a name.
my %attr_names;
my @attrs = map {
    chomp;
    my $attr = $_;
    $attr = lc($attr);
    #$attr =~ s/^"(.*)"$/$1/;
    $attr =~ s/\#$/_num/;
    $attr =~ s/\?$/_flag/;
    $attr =~ s/\?/_q_/;
    $attr =~ s/\&/_and_/;
    $attr =~ s/ *$//;
    $attr =~ s/^ *//;
    $attr =~ s/ +|\//_/g; 
    ($attr  =~ /^$/) 
        and $attr="field" . (++ $i);
    $attr_names{$attr}++;
    if ($attr_names{$attr} > 1) {
        $attr = "${attr}_$attr_names{$attr}";
    }
    $attr;
} @headers;


# loop through each line of the file; store the results in @data.
while(<>) {
    chomp;
    # Columns where there's a comma inside of quotes are a problem; hide the comma
    #     using the ascii code
    if (/".*"/) {
        #print "$_\n";
        s/&/&amp;/g;
        my @z = split /("[^"]*")/, $_; #"
        $_ = join " ", map {
            if (/^"(.*)"$/) {
                $_ = $1;
                s/,/&#44;/g;
            }
            #qq([$_]);
            $_;
        } @z;
        #print "\n";
        #s/".*?"/"quotes"/g;
    }
    my @row_data = split / *, */;
    my %rowdata;
    # go through the columns....
    for (my $i = 0; $i < @row_data; $i++) {
        # turn escaped commas back into commas.
        $row_data[$i] =~ s/\&\#44\;/,/g;
        if ($row_data[$i] !~ /^\s*$/) {
            # $data[$.]{$attrs[$i]} = $row_data[$i];
            $rowdata{$attrs[$i]} = $row_data[$i];
        }
    }
    push @data, \%rowdata;
}

sub xmlEscape {
    my $text = shift();
    $text =~ s/&/&amp;/g;
    $text =~ s/</&lt;/g;
    $text =~ s/>/&gt;/g;
    $text =~ s/"/&quot;/g;
    return $text;
}

# print the results out.
print qq(<?xml version="1.0"?>\n);
print qq(<csv_file file_name="$file_name">\n);
print "  <attrs>\n";
for (my $i = 0; $i < @headers; $i++) {
    print qq(    <attr name="$attrs[$i]" title="$headers[$i]"/>\n);
}
print "  </attrs>\n";
print "  <data>\n";
foreach $row (@data) {
    print "    <row";
    my %row = %{$row};
    my $comma = "";
    for (my $i = 0; $i < @attrs; $i++) {
        my $attr = $attrs[$i];
        my $val = xmlEscape($row{$attr});
        if ($val =~ /\S/)  {
            print qq($comma $attr="$val");
            $comma = "\n        ";
        }
    } 
    print "/>\n";
}
print "  </data>\n";

# use Data::Dumper;
# print Dumper(\@data);

print "</csv_file>\n";
