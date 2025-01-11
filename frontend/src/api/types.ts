export interface Response<T = any> {
    success: boolean;
    data?: T;
    message?: string;
}

export interface PagedResponse<T> extends Response<T> {
    page?: number; 
    size?: number; 
    totalElements?: number; 
    totalPages?: number;
    last?: boolean;
    total?: number;
}