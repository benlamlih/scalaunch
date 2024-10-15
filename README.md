# Scalaunch Template

This is a basic Giter8 template for creating Scala projects with support for backend, frontend, and shared modules.

## Features

- **Backend**: Supports frameworks like Cask, Akka, or Http4s.
- **Frontend**: Uses Scala.js and Laminar for building modern web applications.
- **Shared Module**: Contains shared code between the backend and frontend.
- **Cross-platform**: Supports both JVM and JavaScript platforms using sbt-scalajs-crossproject.

## How to Use

### 1. Generate a Project from this Template

You can create a new project from this template by using the `sbt new` command:

```bash
sbt new benlamlih/scalaunch.g8
```

### 2. Build and Run the Project

After generating your project, navigate into the project directory and run the following commands:

- To compile the project:

```bash
sbt compile
```

- To run the backend:

```bash
sbt backend/run
```

- To run the frontend:

```bash
sbt frontend/fastOptJS
```

### 3. Customizing the Template

You can modify the following features when generating your project:
- **Project Name**: Set the name of your project.
- **Package Name**: Set the base package for the generated code.
- **Include Backend/Frontend/Shared Modules**: Select which modules to include in your project.

### 4. Testing

To run tests in your project, use:

```bash
sbt test
```

## License

This project is licensed under the MIT License. See the LICENSE file for details.
