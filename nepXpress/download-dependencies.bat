@echo off
echo Creating lib directory...
mkdir lib 2>nul

echo Downloading JavaMail dependencies...
powershell -Command "& {
    $urls = @{
        'javax.mail.jar' = 'https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar'
        'activation.jar' = 'https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar'
    }
    foreach ($file in $urls.Keys) {
        Write-Host "Downloading $file..."
        Invoke-WebRequest -Uri $urls[$file] -OutFile "lib\$file"
    }
}"

echo.
echo Dependencies downloaded successfully!
echo Please add these JARs to your project's classpath.
pause 