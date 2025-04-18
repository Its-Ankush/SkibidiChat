# SkibidiChat
<p align="center">
  <img width="100%" src="logo.png" />
</p>

## System Design Diagram

<p align="center">
  <img width="100%" src="https://github.com/user-attachments/assets/752e9f9d-7d59-4707-8895-4fbe7e737746" />
</p>


## Demo
video

## Usage instructions for windows [x64]
1. Clone the repo
2. Change directory to the repo and move to `src/main/resources/static`
3. Open up powershell in this path and execute `.\download.ps1` command
4. You may get an error stating that the current execution policy doesnt allow running remote scripts. In that case, check out [Set-ExecutionPolicy](https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.security/set-executionpolicy?view=powershell-7.5) 
5. Once everything downloads, make sure to use a good secret for your JWT secret. That will be saved as the `CADDY_JWTAUTH_SIGN_KEY` environment variable. Dont close powershell window just yet.
6. Then install Redis, MongoDB, Go, the Caddy-jwt build. Make sure to install Go else Caddy-jwt will fail. Make sure to keep `caddy.exe` in the static directory itself.
7. You can either keep the default ports or use your own ports. You have the option to change them in the server as well using the `application.properties` file in `src/main/resources`. However if you want to access the site on port other than `8080`, you will have to make that change in the `Caddyfile` in `localhost:portNumber` instead of `localhost:8080`
8. After installing redis, it will automatically run as a service. If you didnt change the path, it would usually be in `C:\Program Files\Redis`. Switch to that path
9. Open the file `redis.windows-service.conf` and if you see any line which says `appendonly no`; change that to `appendonly yes` . This will ensure messages are saved to disk as well. [You will need to open Notepad as admin to do this]. Check out [AOF](https://redis.io/docs/latest/operate/oss_and_stack/management/persistence/) to know more.
10. Dont forget to restart redis. Open Powershell as admin and run `Restart-Service -Name Redis`.  
11. Use `caddy.exe run` in powershell from step 5 to run caddy. Allow firewall perms and trust the local root certificate. This is necessary for HTTPS locally. Caddy has to be on the `static` directory itself.
12. Finally build the project by going to the project root, running `mvn clean package` . Inside the target dir, find the JAR with dependencies and open it up. 
13. Finally go to `https://localhost:8080[or any port]`