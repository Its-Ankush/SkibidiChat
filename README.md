# SkibidiChat
<p align="center">
  <img width="100%" src="https://github.com/user-attachments/assets/948821ce-ea32-4b9d-9037-57a8f49b3456" />
</p>

## System Design Diagram


<p align="center">
  <img width="100%" src="https://github.com/user-attachments/assets/ae2afa22-6c1f-4b8e-b8c7-fb1f8f81f9ea" />
</p>


## Demo



<p align="center">
  <img width="100%" src="https://github.com/user-attachments/assets/6eb6340a-b7c8-4637-981d-2a2db26984c3" />
</p>

## Usage instructions for windows [x64]
1. Clone the repo
2. Change directory to the repo and move to [src/main/resources/static](https://github.com/Its-Ankush/SkibidiChat/tree/main/src/main/resources/static)
3. Open up powershell in this path and execute `.\download.ps1` command
4. You may get an error stating that the current execution policy doesnt allow running remote scripts. In that case, check out [Set-ExecutionPolicy](https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.security/set-executionpolicy?view=powershell-7.5) 
5. Once everything downloads, make sure to use a good secret for your JWT secret. That will be saved as the `CADDY_JWTAUTH_SIGN_KEY` environment variable. Dont close powershell window just yet.
6. Then install Redis, MongoDB, Go, the Caddy-jwt build. Make sure to install Go else Caddy-jwt will fail. Make sure to keep `caddy.exe` in the static directory itself.
7. You can either keep the default ports or use your own ports. You have the option to change them in the server as well using the [application.properties](https://github.com/Its-Ankush/SkibidiChat/blob/main/src/main/resources/application.properties) file in `src/main/resources`. However if you want to access the site on port other than `8080`, you will have to make that change in the `Caddyfile` in `localhost:portNumber` instead of `localhost:8080`
8. After installing redis, it will automatically run as a service. If you didnt change the path, it would usually be in `C:\Program Files\Redis`. Switch to that path
9. Open the file `redis.windows-service.conf` and if you see any line which says `appendonly no`; change that to `appendonly yes` . This will ensure messages are saved to disk as well. [**You will need to open Notepad as admin to do this**]. Check out [AOF](https://redis.io/docs/latest/operate/oss_and_stack/management/persistence/) to know more.
10. Dont forget to restart redis. Open Powershell as admin and run `Restart-Service -Name Redis`.  
11. Use `caddy.exe run` in powershell from step 5 to run caddy. Allow firewall perms and trust the local root certificate. This is necessary for HTTPS locally. Caddy has to be on the [static](https://github.com/Its-Ankush/SkibidiChat/tree/main/src/main/resources/static) directory itself.
12. Finally build the project by going to the project root, running `mvn clean package` . Inside the target dir, find the JAR with dependencies and open it up. 
13. Finally go to `https://localhost:8080[or any port]`
