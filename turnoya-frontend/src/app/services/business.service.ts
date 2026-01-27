import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { GatewayService } from './api-gateway.service';
import {
  Business,
  CreateBusinessRequest,
  UpdateBusinessRequest
} from '../models/business.models';

@Injectable({
  providedIn: 'root'
})
export class BusinessService {
  private gateway = inject(GatewayService);
  
  // Endpoints del microservicio de negocios
  private readonly BUSINESS_PREFIX = '/business-api';

  getBusinessById(id: string): Observable<Business> {
    return this.gateway.get<Business>(`${this.BUSINESS_PREFIX}/${id}`);
  }

  getBusinessesByOwner(ownerId: string): Observable<Business[]> {
    return this.gateway.get<Business[]>(`${this.BUSINESS_PREFIX}/owner/${ownerId}`);
  }

  createBusiness(businessData: CreateBusinessRequest): Observable<Business> {
    return this.gateway.post<Business>(
      `${this.BUSINESS_PREFIX}`,
      businessData
    );
  }

  updateBusiness(id: string, businessData: UpdateBusinessRequest): Observable<Business> {
    return this.gateway.put<Business>(
      `${this.BUSINESS_PREFIX}/${id}`,
      businessData
    );
  }

  deleteBusiness(id: string): Observable<void> {
    return this.gateway.delete<void>(`${this.BUSINESS_PREFIX}/${id}`);
  }

  searchBusinesses(query: string): Observable<Business[]> {
    return this.gateway.get<Business[]>(
      `${this.BUSINESS_PREFIX}/search?q=${encodeURIComponent(query)}`
    );
  }
}