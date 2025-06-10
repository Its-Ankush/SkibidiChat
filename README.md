<p align="center">
  <img width="46%" src="https://github.com/user-attachments/assets/5cbea151-344e-4c12-96fd-7518518ca49b" />
</p>

<p align="center">
   <a align="center" href="https://github.com/Its-Ankush/SkibidiChat/stargazers"><img src="https://img.shields.io/github/stars/Its-Ankush/SkibidiChat" alt="GitHub Stars"></a>
</p>

<h3 align="center"><b>Architecture</b></h3>
<p align="center">
  <img width="70%" src="https://github.com/user-attachments/assets/0174ceb0-9cba-4fc2-b198-22900aab186c" />
</p>


<h3 align="center"><b>Demo</b></h3>
<p align="center">
  <img width="70%" src="https://github.com/user-attachments/assets/6eb6340a-b7c8-4637-981d-2a2db26984c3" />
</p>


## Usage instructions [docker only]
Note - Docker and docker compose must be installed

1. 
```bash
git clone https://github.com/Its-Ankush/SkibidiChat
cd SkibidiChat
```
2. Rename the `.env.sample` to `.env` and select a b64 encoded secret for `CADDY_JWTAUTH_SIGN_KEY`
3. Run 
```bash
docker compose pull 
docker compose up
```
4. Visit https://localhost:8080 and accept warnings if any
