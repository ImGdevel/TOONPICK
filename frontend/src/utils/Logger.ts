
class Logger {
    private static isDevMode: boolean = process.env.NODE_ENV === 'development';

    /**
     * Logs an informational message.
     * @param message - The message to log.
     * @param data - Optional additional data to log.
     */
    static info(message: string, data?: Record<string, any>) {
        if (this.isDevMode) {
        console.info(`[INFO]: ${message}`, data || '');
        }
    }

    /**
     * Logs a warning message.
     * @param message - The message to log.
     * @param data - Optional additional data to log.
     */
    static warn(message: string, data?: Record<string, any>) {
        if (this.isDevMode) {
        console.warn(`[WARN]: ${message}`, data || '');
        }
    }

    /**
     * Logs an error message.
     * @param message - The message to log.
     * @param data - Optional additional data to log.
     */
    static error(message: string, data?: Record<string, any>) {
        console.error(`[ERROR]: ${message}`, data || '');
    }

    /**
     * Logs a debug message.
     * @param message - The message to log.
     * @param data - Optional additional data to log.
     */
    static debug(message: string, data?: Record<string, any>) {
        if (this.isDevMode) {
        console.debug(`[DEBUG]: ${message}`, data || '');
        }
    }

    /**
     * Logs a message to an external logging service (e.g., Sentry, Datadog).
     * @param level - The log level (info, warn, error, debug).
     * @param message - The message to log.
     * @param data - Optional additional data to log.
     */
    static logToService(level: 'info' | 'warn' | 'error' | 'debug', message: string, data?: Record<string, any>) {
        // Example: Integrate with an external logging service
        // ExternalLoggingService.log({ level, message, data });
        console.log(`[SERVICE][${level.toUpperCase()}]: ${message}`, data || '');
    }
}

export default Logger;
  