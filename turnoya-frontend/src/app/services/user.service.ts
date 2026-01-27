import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { GatewayService } from './api-gateway.service';
import { User } from '../models/user.models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private gateway = inject(GatewayService);
  
  // El gateway enruta /users/* al microservicio correspondiente
  private readonly USER_PREFIX = '/user-api';
  
  getProfile(): Observable<User> {
    return this.gateway.get<User>(`${this.USER_PREFIX}/${this.getUserId()}`);
  }

  getUserId(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    const decoded = this.decodeToken(token);
    return decoded?.sub || decoded?.userId || decoded?.id || null;
  }
  
  decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }
}