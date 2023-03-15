$baseURL = "http://www.bohol.ph/diksyunaryo.php";
$baseURL .= "?lang=Cebuano&phonetic=T&sw=";
$baseURL =~ s/\&/^&/;
foreach (@ARGV) {
    print "$baseURL$_";
}
