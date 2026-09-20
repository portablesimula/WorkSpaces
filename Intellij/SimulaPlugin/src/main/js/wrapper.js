import { spawn } from 'child_process';
import path from 'path';
import fs from 'fs';

/**
 * Spawns the Java LSP server process and returns the process instance.
 * @param {string} pluginDir - The absolute path to your plugin's installation directory.
 * @param {string[]} extraJvmArgs - Optional additional JVM flags (e.g., ['-Xmx1g']).
 * @returns {ChildProcess} The running Java subprocess.
 */
export function launchLspServer(pluginDir, extraJvmArgs = []) {
    // 1. Locate the JAR file inside your plugin package
    const jarPath = path.resolve(pluginDir, 'server', 'my-lsp-server.jar');

    if (!fs.existsSync(jarPath)) {
        throw new Error(`LSP Server JAR not found at: ${jarPath}`);
    }

    // 2. Formulate the execution command
    // Assumes 'java' is available on the system PATH.
    // For absolute robustness, you can pass the IDE's internal boot JDK path if available.
    const command = 'java';

    const args = [
        ...extraJvmArgs,
        '-jar',
        jarPath
        // Add any application-specific arguments your LSP server needs here, e.g.:
        // '--stdio'
    ];

    // 3. Spawn the process with standard I/O pipes configured for LSP
    const serverProcess = spawn(command, args, {
        stdio: ['pipe', 'pipe', 'pipe'], // [stdin, stdout, stderr]
        env: { ...process.env }
    });

    // 4. Basic error handling and logging wrapper
    serverProcess.stderr.on('data', (chunk) => {
        // Redirect server internal logs/errors to the plugin's debug console
        console.error(`[LSP Server Error/Log]: ${chunk.toString()}`);
    });

    serverProcess.on('error', (err) => {
        console.error(`Failed to start LSP Java process: ${err.message}`);
    });

    serverProcess.on('exit', (code, signal) => {
        console.log(`LSP Server process exited with code ${code} and signal ${signal}`);
    });

    return serverProcess;
}
