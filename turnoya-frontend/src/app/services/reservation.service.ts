import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { GatewayService } from './api-gateway.service';
import { CreateReservationRequest, Reservation } from '../models/reservation.models';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private gateway = inject(GatewayService);
  
  // Endpoints del microservicio de negocios
  private readonly RESERVATION_PREFIX = '/reservations-api';

  // Obtener próximas reservas
  getUpcomingReservations(days: number = 7): Observable<Reservation[]> {
    return this.getAllReservations().pipe(
      map(reservations => {
        if (!reservations || reservations.length === 0) {
        return [];
      }
      
      const today = new Date();
      const endDate = new Date();
      endDate.setDate(today.getDate() + days);
      
      return reservations
        .filter(reservation => {
          try {
            const reservationDate = new Date(`${reservation.startDateTime}`);
            const isUpcoming = reservationDate >= today && reservationDate <= endDate;
            const isActive = reservation.status === 'BOOKED' || reservation.status === 'CONFIRMED';
            return isUpcoming && isActive;
          } catch {
            return false;
          }
        })
        .sort((a, b) => {
          const dateA = new Date(`${a.startDateTime}`);
          const dateB = new Date(`${b.startDateTime}`);
          return dateA.getTime() - dateB.getTime();
        });
      })
    );
  }

  // Obtener reservas del usuario activo
  getMyReservations(userId: string): Observable<Reservation[]> {
    const customHeaders = {
      'X-User-Id': userId
    };
    return this.gateway.getWithHeaders<Reservation[]>(`${this.RESERVATION_PREFIX}/me`, null, customHeaders);
  }

  // Obtener reservas por id del negocio
  getReservationsByBusiness(businessId: string): Observable<Reservation[]> {
    return this.gateway.getWithHeaders<Reservation[]>(`${this.RESERVATION_PREFIX}/search?businessId=${businessId}`, null);
  }

  // Obtener todas las reservas
  getAllReservations(): Observable<Reservation[]> {
    return this.gateway.get<Reservation[]>(`${this.RESERVATION_PREFIX}`);
  }

  // Cancelar reserva
  // cancelReservation(reservationId: string): Observable<{ success: boolean; message: string }> {
  //   const reservation = this.mockReservations.find(r => r.id === reservationId);
    
  //   if (reservation) {
  //     reservation.status = 'cancelled';
  //     return of({ 
  //       success: true, 
  //       message: 'Reserva cancelada exitosamente' 
  //     }).pipe(delay(500));
  //   }
    
  //   return of({ 
  //     success: false, 
  //     message: 'Reserva no encontrada' 
  //   }).pipe(delay(500));
  // }

  // Crear nueva reserva
  createReservation(userId: string, reservationData: CreateReservationRequest): Observable<Reservation> {
    const customHeaders = {
      'X-User-Id': userId
    };
    return this.gateway.postHeaders<Reservation>(`${this.RESERVATION_PREFIX}`, reservationData, customHeaders);
  }
}