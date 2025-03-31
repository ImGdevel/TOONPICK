declare module 'js-cookie' {
    interface CookiesStatic {
        get(name: string): string | undefined;
        set(name: string, value: string, options?: any): void;
        remove(name: string, options?: any): void;
    }

    const Cookies: CookiesStatic;
    export default Cookies;
} 