import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { AuthResponse } from '../models/auth.models';

@Injectable({
  providedIn: 'root'
})
export class GatewayService {
  private http = inject(HttpClient);
  private router = inject(Router);
  
  // URL base del Gateway
  private readonly GATEWAY_URL = environment.apiGatewayUrl;
  
  // Subject para manejar estado de autenticación
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  
  // Método genérico para GET
  get<T>(endpoint: string, params?: any): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    const options = {
      headers: this.getHeaders(),
      params: this.createParams(params)
    };
    
    return this.http.get<T>(url, options).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }

  // Método genérico para GET con headers
  getWithHeaders<T>(endpoint: string, params?: any, customHeaders?: {[key:string]: string}): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    const options = {
      headers: this.getHeaders(customHeaders),
      params: this.createParams(params)
    };
    
    return this.http.get<T>(url, options).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }
  
  // Método genérico para POST
  post<T>(endpoint: string, data: any): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    
    return this.http.post<T>(url, data, { headers: this.getHeaders()} ).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }

  // Método genérico para POST con headers
  postHeaders<T>(endpoint: string, data: any, customHeaders?: {[key:string]: string}): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    
    return this.http.post<T>(url, data, { 
      headers: this.getHeaders(customHeaders) 
    }).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }
  
  // Método genérico para PUT
  put<T>(endpoint: string, body: any): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    
    return this.http.put<T>(url, body, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }
  
  // Método genérico para DELETE
  delete<T>(endpoint: string): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    
    return this.http.delete<T>(url, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }
  
  // Método para FormData (uploads)
  postFormData<T>(endpoint: string, formData: FormData): Observable<T> {
    const url = `${this.GATEWAY_URL}${endpoint}`;
    const headers = new HttpHeaders();
    
    // No establecer Content-Type, el navegador lo hará con boundary
    if (this.hasToken()) {
      headers.append('Authorization', `Bearer ${this.getToken()}`);
    }
    
    return this.http.post<T>(url, formData, { headers }).pipe(
      catchError(error => this.handleHttpError(error))
    );
  }
  
  // Headers con autenticación
  private getHeaders(customHeaders?: any): HttpHeaders {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    
    // Agregar headers personalizados
    if (customHeaders) {
      Object.keys(customHeaders).forEach(key => {
        headers = headers.set(key, customHeaders[key]);
      });
    }
    
    // Agregar token si existe
    const token = this.getToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    
    return headers;
  }
  
  // Crear parámetros de query
  private createParams(params: any): HttpParams {
    let httpParams = new HttpParams();
    
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }
    
    return httpParams;
  }
  
  // Manejo centralizado de errores
  private handleHttpError(error: HttpErrorResponse): Observable<never> {
    console.error('Gateway HTTP Error:', error);
    
    // Manejar errores específicos
    switch (error.status) {
      case 401: // Unauthorized
        this.handleUnauthorized();
        break;
        
      case 403: // Forbidden
        this.router.navigate(['/forbidden']);
        break;
        
      case 404: // Not Found
        // Puedes manejar 404s específicos si es necesario
        break;
        
      case 429: // Too Many Requests
        console.warn('Rate limit excedido');
        break;
        
      case 500: // Internal Server Error
      case 502: // Bad Gateway
      case 503: // Service Unavailable
      case 504: // Gateway Timeout
        // Podrías emitir un evento de error global aquí
        break;
    }
    
    const errorMessage = this.getErrorMessage(error);
    return throwError(() => new Error(errorMessage));
  }
  
  private getErrorMessage(error: HttpErrorResponse): string {
    // Priorizar mensaje del backend
    if (error.error?.message) {
      return error.error.message;
    }
    
    if (error.error?.error) {
      return error.error.error;
    }
    
    // Mensajes por defecto
    switch (error.status) {
      case 0: return 'No hay conexión con el servidor';
      case 400: return 'Solicitud incorrecta';
      case 401: return 'Sesión expirada';
      case 403: return 'Acceso denegado';
      case 404: return 'Recurso no encontrado';
      case 409: return 'Conflicto con el recurso';
      case 422: return 'Datos inválidos';
      case 429: return 'Demasiadas solicitudes';
      case 500: return 'Error interno del servidor';
      case 503: return 'Servicio no disponible';
      default: return `Error ${error.status}: ${error.statusText}`;
    }
  }
  
  // Manejo de autenticación
  private handleUnauthorized(): void {
    // Limpiar tokens
    this.clearTokens();
    
    // Actualizar estado
    this.isAuthenticatedSubject.next(false);
    
    // Redirigir a login si no está en página pública
    const publicRoutes = ['/login', '/register', '/forgot-password'];
    const currentRoute = this.router.url;
    
    if (!publicRoutes.some(route => currentRoute.includes(route))) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: currentRoute }
      });
    }
  }
  
  // Helpers para tokens
  private getToken(): string | null {
    return localStorage.getItem('access_token');
  }
  
  private hasToken(): boolean {
    return !!this.getToken();
  }
  
  setTokens(accessToken: string, refreshToken?: string): void {
    localStorage.setItem('access_token', accessToken);
    if (refreshToken) {
      localStorage.setItem('refresh_token', refreshToken);
    }
    this.isAuthenticatedSubject.next(true);
  }
  
  clearTokens(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.isAuthenticatedSubject.next(false);
  }
  
  // Método para refresh token
  refreshAccessToken(): Observable<AuthResponse> {
    const refreshToken = localStorage.getItem('refresh_token');
    
    if (!refreshToken) {
      this.handleUnauthorized();
      return throwError(() => new Error('No refresh token available'));
    }
    
    return this.post<AuthResponse>('/auth/refresh', { refreshToken }).pipe(
      tap(response => {
        if (response.accessToken) {
          this.setTokens(response.accessToken, response.refreshToken);
        }
      })
    );
  }
}