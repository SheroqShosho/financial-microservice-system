// Utility för att extrahera och checka JWT roles

export function decodeJWT(token: string) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Failed to decode JWT:', error);
        return null;
    }
}

export function getRoles(token: string): string[] {
    const payload = decodeJWT(token);
    if (!payload) return [];

    const roles = payload.roles || payload.role || [];
    return Array.isArray(roles) ? roles : [roles];
}

export function isUserAdmin(token: string): boolean {
    const roles = getRoles(token);
    return roles.some((role: string) =>
        role.toLowerCase() === 'admin' ||
        role.toLowerCase() === 'administrator'
    );
}

