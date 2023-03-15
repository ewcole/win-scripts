my $url1="http://www.bohol.ph/diksyunaryo.php?lang=Cebuano&phonetic=T&sw=";
my $html="<html><head><title>Cebuano Lookup</title></head><body>\n";

sub addWord {
    (my $word = shift)=~ s/^ +//;
    $word =~ s/ +$//;
    $html .= qq(<a href="$url1$word" target="$word">$word</a><br/>\n);
}

foreach (<>) {
    my @words = split (/\s+/, $_) ;
    foreach $word (@words) {
        addWord($word);
    }
}

$html .= "</body></html>\n";
open TEMP, "> ceb_junk.html";
print TEMP $html;
close TEMP;
`start firefox ceb_junk.html`;
