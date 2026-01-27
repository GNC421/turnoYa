export interface Reservation {
  id: string;
  businessId: string;
  businessName: string;
  userId: string;
  startDateTime: Date;
  endDateTime: Date;
  status: 'BOOKED' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';
  createdAt: Date
}

export interface CreateReservationRequest {
  businessId: string;
  date: string;
  time: string;
  notes?: string;
}

export interface UpdateReservationRequest {
  date?: string;
  time?: string;
  notes?: string;
  status?: 'BOOKED' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';
}