import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

export interface DecodedToken {
  sub: string;
  exp: number;
  iat: number;
  // Otros campos que tengas en tu JWT
  email?: string;
}

@Injectable({
  providedIn: 'root'
})
export class JwtService {
  
  // Método para decodificar el token y obtener el sub
  getSubjectFromToken(token: string): string | null {
    try {
      const decoded: DecodedToken = jwtDecode(token);
      return decoded.sub;
    } catch (error) {
      console.error('Error decodificando token JWT:', error);
      return null;
    }
  }

  // Método completo para obtener información del usuario
  getTokenInfo(token: string): DecodedToken | null {
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (error) {
      console.error('Error decodificando token:', error);
      return null;
    }
  }

  // Verificar si el token está expirado
  isTokenExpired(token: string): boolean {
    try {
      const decoded = jwtDecode<DecodedToken>(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp < currentTime;
    } catch (error) {
      return true;
    }
  }
}