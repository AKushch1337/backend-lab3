# backend-lab3

DB app

# My variant

Number in group list = 10. Variant 10 % 3 = 1 -> Currencies 


# Build the project
## Clone the project
```bash
git clone https://github.com/AKushch1337/backend-lab3
```
## Move into the directory of the project
```bash
cd /path/to/project
```

## Build the project using Gradle Wrapper
```bash
./gradlew build
```
```bash
./gradlew run
```

# Build the project using Docker

```bash
docker build . -t lab3:1.0 
```

```bash
docker run -p 8080:8080 --rm lab3:1.0
```

# Build the project using Docker Compose

```bash
docker compose build
```

```bash
docker compose up
```

## Check the web page on your local host

Go to http://127.0.0.1:8080/healthcheck
