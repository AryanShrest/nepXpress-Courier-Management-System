$libPath = "lib"
if (-not (Test-Path $libPath)) {
    New-Item -ItemType Directory -Path $libPath
}

$urls = @{
    "javax.mail.jar" = "https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar"
    "activation.jar" = "https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar"
}

foreach ($file in $urls.Keys) {
    $output = Join-Path $libPath $file
    Write-Host "Downloading $file..."
    Invoke-WebRequest -Uri $urls[$file] -OutFile $output
    Write-Host "Downloaded to $output"
}

Write-Host "All JAR files have been downloaded successfully!" 