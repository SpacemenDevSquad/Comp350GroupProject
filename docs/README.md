# Install/Build/Run Instructions
There are 2 ways to run this project:
    - Using Docker
    - Directly

# Docker Instructions

## 1. Verify Docker/Docker-Compose Installation
This can be done by running the command `docker run hello-world` through the command line. If this fails, reinstall docker: [https://www.docker.com/] (Docker Website)

## 2. Pull the Repository
In the directory you want to pull to, run the command `git clone https://github.com/SpacemenDevSquad/Comp350GroupProject.git`
Enter the project directory by running `cd Comp350GroupProject`

## 3. Run Docker
In the root of the project directory, run the command `docker compose up --build`. Patience is key; depending on the computer this may take a while

## 4. Visit Site
At this point, you should be able to visit and interact with the site, which runs at [http://localhost:5173/](http://localhost:5173/)

# Direct Install/Run Instructions

## 1. Pull the Repository
In the directory you want to pull to, run the command `git clone https://github.com/SpacemenDevSquad/Comp350GroupProject.git`
Enter the project directory by running `cd Comp350GroupProject`

## 2. Run the Backend
From the `Comp350GroupProject` directory, run `cd backend`, then `gradlew build run`

## 3. Run the Frontend
From the `Comp350GroupProject` directory, run `cd frontend`, `npm install`, then `npm run dev`

## 4. Visit Site
At this point, you should be able to visit and interact with the site, which runs at [http://localhost:5173/](http://localhost:5173/)