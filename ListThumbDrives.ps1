  Get-WmiObject Win32_Volume -Filter ("DriveType={0}" -f [int][System.io.Drivetype]::removable)|
    Select-Object DriveLetter | Format-Table -HideTableHeaders
# http://sushihangover.blogspot.nl/2012/02/powershell-eject-local-or-remote.html
# 
