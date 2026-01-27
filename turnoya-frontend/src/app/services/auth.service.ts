import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { GatewayService } from './api-gateway.service';
import { RegisterRequest } from '../models/user.models';
import {
  LoginRequest,
  AuthResponse,
  RefreshTokenRequest
} from '../models/auth.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private gateway = inject(GatewayService);
  
  // Endpoints del microservicio de autenticación
  private readonly AUTH_PREFIX = '/auth-api';
  
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.gateway.post<AuthResponse>(
      `${this.AUTH_PREFIX}/login`,
      credentials
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.gateway.post<AuthResponse>(
      `${this.AUTH_PREFIX}/register`,
      userData
    );
  }

  refreshToken(refreshToken: string): Observable<AuthResponse> {
    const request: RefreshTokenRequest = { refreshToken };
    return this.gateway.post<AuthResponse>(
      `${this.AUTH_PREFIX}/refresh`,
      request
    );
  }

  logout(): Observable<void> {
    return this.gateway.post<void>(`${this.AUTH_PREFIX}/logout`, {});
  }

  validateToken(): Observable<{ valid: boolean }> {
    return this.gateway.get<{ valid: boolean }>(`${this.AUTH_PREFIX}/validate`);
  }
}