# Redis Commands in Docker

This document outlines the commands used to interact with a Redis container using Docker and the Redis CLI, along with their purposes.

## Commands and Explanations

1. **Open a Bash Shell in the Redis Container**

   ```bash
   docker exec -it redis bash
   ```

   - **Purpose**: This command opens an interactive Bash shell inside a running Docker container named `redis`.
   - **Explanation**:
     - `docker exec`: Executes a command inside a running Docker container.
     - `-it`: Combines `-i` (interactive, keeps STDIN open) and `-t` (allocates a pseudo-TTY), allowing you to interact with the container's shell.
     - `redis`: The name of the Docker container running the Redis server.
     - `bash`: Starts a Bash shell inside the container, providing a command-line interface to run further commands.

2. **Run the Redis CLI**

   ```bash
   redis-cli
   ```

   - **Purpose**: Launches the Redis Command Line Interface (CLI), a tool used to interact with the Redis server.
   - **Explanation**:
     - `redis-cli` is a client tool that connects to the Redis server running in the container (by default on `localhost:6379` unless configured otherwise).
     - Once executed, it provides an interactive prompt where you can issue Redis commands to manage data, check server status, or configure settings.

3. **Ping the Redis Server**

   ```bash
   ping
   ```

   - **Purpose**: Tests the connection to the Redis server to verify it is responsive.
   - **Explanation**:
     - The `ping` command sends a simple request to the Redis server.
     - If the server is running and accessible, it responds with `PONG`, confirming the connection is active.
     - Useful for troubleshooting connectivity issues or verifying that the Redis server is operational.

4. **Flush the Current Database**

   ```bash
   flushdb
   ```

   - **Purpose**: Deletes all keys in the currently selected Redis database, effectively creating a clean database.
   - **Explanation**:
     - `flushdb` removes all data (keys and their values) from the current database (Redis supports multiple databases, indexed from 0 by default).
     - This does not affect other databases in the Redis instance unless explicitly targeted.
     - Use with caution, as this operation is destructive and cannot be undone.

## Usage Example

To interact with a Redis server running in a Docker container, follow these steps:

1. Open a terminal and run:
   ```bash
   docker exec -it redis bash
   ```
   This starts a Bash session inside the `redis` container.

2. Inside the container, start the Redis CLI:
   ```bash
   redis-cli
   ```

3. Test the connection to the Redis server:
   ```bash
   ping
   ```
   Expected output: `PONG`

4. Clear the current database to start fresh:
   ```bash
   flushdb
   ```
   Expected output: `OK`

## Notes
- Ensure the `redis` container is running before executing `docker exec`. Use `docker ps` to verify.
- If the Redis server is configured with a password or runs on a non-default host/port, use `redis-cli -h <host> -p <port> -a <password>` to connect.
- The `flushdb` command is irreversible. Back up important data before using it.
- To exit the Redis CLI, type `exit`. To exit the container's Bash shell, type `exit` again.
