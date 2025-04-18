# arch = $env:PROCESSOR_ARCHITECTURE
#  Download redis for windows [keep in mind that we need to make a change in the config file - AOF to yes]
Write-Host "Now downloading redis"
Invoke-WebRequest -Uri "https://github.com/tporadowski/redis/releases/download/v5.0.14.1/Redis-x64-5.0.14.1.msi" -OutFile "Redis-x64-5.0.14.1.msi"

#  Download mongo DB
Write-Host "Now downloading MongoDB"
Invoke-WebRequest -Uri "https://fastdl.mongodb.org/windows/mongodb-windows-x86_64-8.0.8-signed.msi" -OutFile "mongodb-windows-x86_64-8.0.8-signed.msi"

#  Downlod Go
Write-Host "Downloading Go, to use caddy"
Invoke-WebRequest -Uri "https://go.dev/dl/go1.24.2.windows-amd64.msi" -OutFile "go1.24.2.windows-amd64.msi"

#  Then download caddy with caddy-jwt from here - https://caddyserver.com/download?package=github.com%2Fggicci%2Fcaddy-jwt should have just used xcaddy but that would be manual and wont work unless Go is installed
Write-Host "Downloading the caddy-jwt build. Make sure Go is installed else this build will NOT run"
Invoke-WebRequest -Uri "https://caddyserver.com/api/download?os=windows&arch=amd64&p=github.com%2Fggicci%2Fcaddy-jwt" -OutFile "caddy.exe"

#  Get env var from secret. Even Java should be able to read this.
$secret = Read-Host -Prompt "Enter the actual symmetric secret for JWT - please dont b64 encode it. It will be done automatically"
if ([string]::IsNullOrWhiteSpace($secret)) {
    Write-Host "Error: Variable name cannot be empty"
    return
}
$b64EncodedSecret = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($secret))
[Environment]::SetEnvironmentVariable("CADDY_JWTAUTH_SIGN_KEY",$b64EncodedSecret, "User")
Write-Host "CADDY_JWTAUTH_SIGN_KEY variable set"


Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
