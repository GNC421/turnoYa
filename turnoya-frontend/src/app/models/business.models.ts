export interface Business {
  id: string;
  name: string;
  description?: string;
  category: string;
  country: string;
  state: string;
  city: string;
  street: string;
  number: string;
  zipCode: string;
  phone: string;
  email: string;
  ownerId: string;
  status: string;
  registrationDate: string;
  updatedAt: string;
  website: string;
}

export interface CreateBusinessRequest {
  name: string;
  description?: string;
  type: string;
  address: string;
  phone: string;
  email: string;
}

export interface UpdateBusinessRequest {
  name?: string;
  description?: string;
  type?: string;
  address?: string;
  phone?: string;
  email?: string;
  isActive?: boolean;
}

export interface BusinessStats {
  // Reservas
  todayReservations: number;
  todayVsYesterday: number;
  pendingConfirmations: number;
  upcomingReservations: number;
  nextReservationTime: string;
  monthlyReservations: number;
  
  // Clientes
  totalCustomers: number;
  repeatCustomers: number;
  repeatRate: number;
  newCustomersThisMonth: number;
  
  // Finanzas
  todayEstimatedRevenue: number;
  averageTicket: number;
  monthlyRevenue: number;
  
  // Métricas
  confirmationRate: number;
  cancellationRate: number;
  occupancyRate: number;
  
  // Horarios
  peakHour: string;
  peakHourReservations: number;
}