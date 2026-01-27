import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { HeaderComponent } from '../shared/header/header';
import { BusinessService } from '../services/business.service';
import { ReservationService } from '../services/reservation.service';
import { Router } from '@angular/router';
import { BusinessStats } from '../models/business.models';
import { Business } from '../models/business.models';
import { User } from '../models/user.models';
import { Reservation } from '../models/reservation.models';

interface DashboardStats {
  todayReservations: number;
  pendingReservations: number;
  totalClients: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatButtonToggleModule,
    MatMenuModule,
    MatDividerModule,
    HeaderComponent,
    MatProgressSpinner
],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  activeTab: 'business' | 'user' = 'business';
  activeBusiness: Business | null = null;
  user: User | null = null;
  
  businesses: Business[] = [];
  upcomingReservations: Reservation[] = [];
  userReservations: Reservation[] = [];
  lastReservation: Reservation | null = null;
  
  businessStats: BusinessStats = {
    todayReservations: 0,
    todayVsYesterday: 0,
    pendingConfirmations: 0,
    upcomingReservations: 0,
    nextReservationTime: '',
    monthlyReservations: 0,
    totalCustomers: 0,
    repeatCustomers: 0,
    repeatRate: 0,
    newCustomersThisMonth: 0,
    todayEstimatedRevenue: 0,
    averageTicket: 0,
    monthlyRevenue: 0,
    confirmationRate: 0,
    cancellationRate: 0,
    occupancyRate: 0,
    peakHour: '',
    peakHourReservations: 0
  };

  constructor(
    private businessService: BusinessService,
    private reservationService: ReservationService,
    private router: Router
  ) {
    this.user = JSON.parse(localStorage.getItem('user') || '');
  }

  ngOnInit() {
    // Cargar los negocios del usuario activo
    this.businessService.getBusinessesByOwner(this.user?.id || '').subscribe({
      next: (response) => {
        this.businesses = response;
        // Seleccionar el negocio activo por defecto
        this.activeBusiness = this.businesses[0] || null;
        
        // Si hay negocio activo, cargar sus estadísticas
        if (this.activeBusiness) {
          this.calculateBusinessStats();
        }
      },
      error: (error) => {
        console.error(error);
      }
    });
    
    // Cargar las reservas del usuario activo
    this.reservationService.getMyReservations(this.user?.id || '').subscribe({
      next: (response) => {
        this.userReservations = response;
        // Encontrar la última reserva
        this.findLastReservation();
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  switchBusiness(business: Business) {
    this.activeBusiness = business;
    this.calculateBusinessStats();
    console.log('Negocio cambiado:', business.name);
  }

  createNewBusiness() {
    console.log('Crear nuevo negocio');
    // Navegar a creación de negocio
  }

  confirmAllPending() {
    console.log('confirm all');
    //Confirmar todas las reservas pendientes
  }

  viewBusinessCalendar() {
    console.log('Ver agenda del negocio');
    // Navegar a agenda
  }

  viewClients() {
    console.log('Ver clientes');
    // Navegar a clientes
  }

  createService() {
    console.log('Crear servicio');
    // Navegar a creación de servicio
  }

  viewBusinessSettings() {
    console.log('Ver configuración del negocio');
    // Navegar a configuración
  }

  viewReservationDetails(reservation: Reservation) {
    console.log('Ver detalles de reserva:', reservation);
    // Mostrar detalles
  }

  confirmReservation(reservation: Reservation) {
    console.log('Confirmar reserva:', reservation);
    // Lógica de confirmación
  }

  viewUserReservations() {
    console.log('Ver todas mis reservas');
    // Navegar a reservas de usuario
  }

  cancelUserReservation(reservation: Reservation) {
    console.log('Cancelar reserva:', reservation);
    // Lógica de cancelación
  }

  viewUserReservationDetails(reservation: Reservation) {
    console.log('Ver detalles de reserva de usuario:', reservation);
    // Mostrar detalles
  }

  searchBusinesses() {
    console.log('Buscar negocios');
    // Navegar a búsqueda
  }

  bookNewAppointment() {
    console.log('Reservar nuevo turno');
    // Navegar a reserva
  }

  viewFavoriteBusinesses() {
    console.log('Ver negocios favoritos');
    // Navegar a favoritos
  }

  viewUserProfile() {
    console.log('Ver perfil de usuario');
    // Navegar a perfil
  }

  getStatusText(status: string): string {
    switch(status) {
      case 'confirmed': return 'Confirmado';
      case 'booked': return 'Pendiente';
      case 'cancelled': return 'Cancelado';
      case 'completed': return 'Completado';
      default: return status;
    }
  }

  ///// AÑADIDO TODO PARA HEADER
  // Métodos para las acciones del header
onLogoClick() {
  console.log('🔙 Logo clickeado - Volviendo al dashboard principal');
  
  // 1. Resetear al tab principal (Negocio)
  this.activeTab = 'business';
  
  // 2. Scroll suave al inicio de la página
  window.scrollTo({ top: 0, behavior: 'smooth' });
  
  // 3. Feedback visual (opcional - se puede implementar con animaciones)
  this.showLogoClickFeedback();
}

onProfileAction(event: any) {

  const action = typeof event === 'string' ? event : event?.detail || event?.type || 'unknown';
  console.log(`👤 Acción de perfil recibida: ${action}`);
  
  switch(action) {
    case 'logout':
      this.handleLogout();
      break;
    
    case 'account':
      this.handleAccount();
      break;
    
    case 'dashboard':
      this.handleDashboard();
      break;
    
    case 'business':
      this.handleBusiness();
      break;
    
    case 'calendar':
      this.handleCalendar();
      break;
    
    case 'settings':
      this.handleSettings();
      break;
    
    case 'help':
      this.handleHelp();
      break;
    
    case 'password':
      this.handlePasswordChange();
      break;
    
    case 'notifications':
      this.handleNotifications();
      break;
    
    default:
      console.warn(`⚠️ Acción no reconocida: ${action}`);
  }
}

// Implementaciones específicas de cada acción
private handleLogout() {
  console.log('🚪 Cerrando sesión...');
  
  // 1. Mostrar confirmación
  const confirmLogout = confirm('¿Estás seguro de que quieres cerrar sesión?');
  
  if (confirmLogout) {
    // 2. Lógica de logout (simulada)
    console.log('✅ Sesión cerrada correctamente');
    
    // 3. Feedback al usuario
    this.showToast('Sesión cerrada correctamente');
    
    // 4. En una app real: redirigir al login
    // this.router.navigate(['/login']);
  }
}

private handleAccount() {
  console.log('👤 Navegando a Mi Cuenta');
  
  // 1. Cambiar al tab de usuario si estamos en negocio
  if (this.activeTab === 'business') {
    this.activeTab = 'user';
  }
  
  // 2. Scroll a la sección de perfil
  setTimeout(() => {
    const profileSection = document.querySelector('.user-actions-section');
    if (profileSection) {
      profileSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }, 100);
  
  // 3. Feedback
  this.showToast('Accediendo a tu cuenta...');
}

private handleDashboard() {
  console.log('📊 Navegando al Dashboard');
  
  // 1. Asegurar que estamos en el tab de negocio
  this.activeTab = 'business';
  
  // 2. Scroll al inicio
  window.scrollTo({ top: 0, behavior: 'smooth' });
  
  // 3. Feedback
  this.showToast('Volviendo al panel principal');
}

private handleBusiness() {
  console.log('🏪 Navegando a Mi Negocio');
  
  // 1. Cambiar al tab de negocio si estamos en usuario
  if (this.activeTab === 'user') {
    this.activeTab = 'business';
  }
  
  // 2. Scroll a la sección de negocio
  setTimeout(() => {
    const businessSection = document.querySelector('.business-selector-section');
    if (businessSection) {
      businessSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }, 100);
  
  // 3. Feedback
  this.showToast('Gestionando tu negocio...');
}

private handleCalendar() {
  console.log('📅 Navegando al Calendario');
  
  // 1. Dependiendo del tab activo, navegar al calendario correspondiente
  if (this.activeTab === 'business') {
    // Navegar al calendario del negocio
    this.viewBusinessCalendar();
  } else {
    // Navegar al calendario personal del usuario
    this.viewUserReservations();
  }
  
  // 2. Feedback
  this.showToast('Abriendo calendario...');
}

private handleSettings() {
  console.log('⚙️ Navegando a Configuración');
  
  // 1. Dependiendo del tab activo, mostrar configuración correspondiente
  if (this.activeTab === 'business') {
    this.viewBusinessSettings();
  } else {
    // Navegar a configuración de usuario
    console.log('Abrir configuración de usuario');
    // this.router.navigate(['/user/settings']);
  }
  
  // 2. Feedback
  this.showToast('Abrir configuración...');
}

private handleHelp() {
  console.log('❓ Navegando a Ayuda y Soporte');
  
  // 1. Mostrar modal de ayuda o navegar a página de ayuda
  alert('🔧 Sección de ayuda y soporte\n\nPágina en desarrollo...');
  
  // 2. Feedback
  this.showToast('Cargando ayuda...');
}

private handlePasswordChange() {
  console.log('🔐 Cambiando contraseña');
  
  // 1. Mostrar modal/formulario para cambiar contraseña
  const newPassword = prompt('🔐 Cambiar contraseña\n\nIntroduce tu nueva contraseña:');
  
  if (newPassword && newPassword.length >= 6) {
    console.log('✅ Contraseña cambiada (simulado)');
    this.showToast('Contraseña actualizada correctamente');
  } else if (newPassword) {
    alert('La contraseña debe tener al menos 6 caracteres');
  }
}

private handleNotifications() {
  console.log('🔔 Navegando a Notificaciones');
  
  // 1. Mostrar notificaciones (simuladas)
  alert('📢 Notificaciones\n\n• Nueva reserva confirmada\n• Recordatorio: Cita mañana\n• Sistema actualizado');
  
  // 2. Feedback
  this.showToast('Mostrando notificaciones...');
}

// Métodos auxiliares para feedback
private showToast(message: string) {
  // Simulación de toast/feedback visual
  console.log(`💬 Toast: ${message}`);
  
  // En una app real usarías Angular Material Snackbar o similar
  // this.snackBar.open(message, 'Cerrar', { duration: 3000 });
}

private showLogoClickFeedback() {
  // Efecto visual para el clic del logo (opcional)
  const logoFeedback = document.querySelector('.dashboard-title');
  if (logoFeedback) {
    logoFeedback.classList.add('logo-click-feedback');
    setTimeout(() => {
      logoFeedback.classList.remove('logo-click-feedback');
    }, 500);
  }
}

// Añade estos métodos a la clase DashboardComponent

private calculateBusinessStats() {
  if (!this.activeBusiness) return;

  // Primero cargar las reservas del negocio
  this.reservationService.getReservationsByBusiness(this.activeBusiness.id).subscribe({
    next: (businessReservations) => {
      this.upcomingReservations = this.filterUpcomingReservations(businessReservations);
      this.businessStats = this.calculateStatsFromReservations(businessReservations);
    },
    error: (error) => {
      console.error('Error cargando reservas del negocio:', error);
    }
  });
}

private filterUpcomingReservations(reservations: Reservation[]): Reservation[] {
  const today = new Date();
  const nextWeek = new Date();
  nextWeek.setDate(today.getDate() + 7);
  
  return reservations.filter(reservation => {
    const reservationDate = new Date(reservation.startDateTime);
    return reservationDate >= today && 
           reservationDate <= nextWeek &&
           (reservation.status === 'BOOKED' || reservation.status === 'CONFIRMED');
  }).sort((a, b) => {
    return new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime();
  });
}

private calculateStatsFromReservations(reservations: Reservation[]): BusinessStats {
  const today = new Date();
  const todayStr = today.toISOString().split('T')[0];
  
  // Filtros
  console.log("RESERVA:" + JSON.stringify(reservations[0]));
  const todayReservations = reservations.filter(r => 
    new Date(r.startDateTime).getDate() == today.getDate() && r.status !== 'CANCELLED'
  );
  
  const pendingReservations = reservations.filter(r => r.status === 'BOOKED');
  
  const confirmedReservations = reservations.filter(r => r.status === 'CONFIRMED');
  
  const cancelledReservations = reservations.filter(r => r.status === 'CANCELLED');
  
  // Cálculos básicos
  const totalReservations = reservations.length;
  const totalCustomers = this.getUniqueCustomers(reservations);
  const repeatCustomers = this.getRepeatCustomers(reservations);
  const repeatRate = totalCustomers > 0 ? Math.round((repeatCustomers / totalCustomers) * 100) : 0;
  
  // Horas de reservas
  const hourCounts = this.calculateHourDistribution(reservations);
  const peakHour = this.findPeakHour(hourCounts);
  
  return {
    todayReservations: todayReservations.length,
    todayVsYesterday: this.calculateDailyTrend(reservations),
    pendingConfirmations: pendingReservations.length,
    upcomingReservations: this.filterUpcomingReservations(reservations).length,
    nextReservationTime: this.getNextReservationTime(reservations),
    monthlyReservations: this.getMonthlyReservations(reservations),
    totalCustomers: totalCustomers,
    repeatCustomers: repeatCustomers,
    repeatRate: repeatRate,
    newCustomersThisMonth: this.getNewCustomersThisMonth(reservations),
    todayEstimatedRevenue: this.calculateEstimatedRevenue(todayReservations),
    averageTicket: this.calculateAverageTicket(reservations),
    monthlyRevenue: this.calculateMonthlyRevenue(reservations),
    confirmationRate: totalReservations > 0 ? 
      Math.round((confirmedReservations.length / totalReservations) * 100) : 0,
    cancellationRate: totalReservations > 0 ? 
      Math.round((cancelledReservations.length / totalReservations) * 100) : 0,
    occupancyRate: this.calculateOccupancyRate(reservations),
    peakHour: peakHour,
    peakHourReservations: hourCounts[peakHour] || 0
  };
}

// Métodos helper
private getUniqueCustomers(reservations: Reservation[]): number {
  const uniqueUserIds = new Set(reservations.map(r => r.userId));
  return uniqueUserIds.size;
}

private getRepeatCustomers(reservations: Reservation[]): number {
  const customerCounts: { [userId: string]: number } = {};
  reservations.forEach(r => {
    customerCounts[r.userId] = (customerCounts[r.userId] || 0) + 1;
  });
  return Object.values(customerCounts).filter(count => count > 1).length;
}

private calculateHourDistribution(reservations: Reservation[]): { [hour: string]: number } {
  const hourCounts: { [hour: string]: number } = {};
  
  reservations.forEach(r => {
    const date = new Date(r.startDateTime);
    const hour = date.getHours().toString().padStart(2, '0') + ':00';
    hourCounts[hour] = (hourCounts[hour] || 0) + 1;
  });
  
  return hourCounts;
}

private findPeakHour(hourCounts: { [hour: string]: number }): string {
  if (Object.keys(hourCounts).length === 0) return '--:--';
  
  return Object.entries(hourCounts).reduce((a, b) => 
    a[1] > b[1] ? a : b
  )[0];
}

private getNextReservationTime(reservations: Reservation[]): string {
  const upcoming = reservations
    .filter(r => {
      const date = new Date(r.startDateTime);
      return date > new Date() && r.status === 'CONFIRMED';
    })
    .sort((a, b) => new Date(a.startDateTime).getTime() - new Date(b.startDateTime).getTime());
  
  if (upcoming.length === 0) return '';
  
  const date = new Date(upcoming[0].startDateTime);
  return date.toLocaleTimeString('es-ES', { 
    hour: '2-digit', 
    minute: '2-digit' 
  });
}

// Métodos de cálculo simplificados (puedes implementarlos según tu lógica de negocio)
private calculateDailyTrend(reservations: Reservation[]): number {
  // Implementa según tus datos históricos
  return 0;
}

private getMonthlyReservations(reservations: Reservation[]): number {
  const currentMonth = new Date().getMonth();
  return reservations.filter(r => 
    new Date(r.startDateTime).getMonth() === currentMonth
  ).length;
}

private getNewCustomersThisMonth(reservations: Reservation[]): number {
  // Implementa según tus datos
  return 0;
}

private calculateEstimatedRevenue(reservations: Reservation[]): number {
  // Asume un precio promedio de 30€ por reserva
  return reservations.length * 30;
}

private calculateAverageTicket(reservations: Reservation[]): number {
  return reservations.length > 0 ? 30 : 0;
}

private calculateMonthlyRevenue(reservations: Reservation[]): number {
  const monthlyReservations = this.getMonthlyReservations(reservations);
  return monthlyReservations * 30;
}

private calculateOccupancyRate(reservations: Reservation[]): number {
  // Ejemplo: capacidad de 8 horas al día * 30 días = 240 horas posibles
  const possibleHours = 240;
  const occupiedHours = reservations.length * 1; // Asume 1 hora por reserva
  return Math.round((occupiedHours / possibleHours) * 100);
}

private findLastReservation() {
  if (this.userReservations.length === 0) {
    this.lastReservation = null;
    return;
  }
  
  // Ordenar por fecha descendente y tomar la más reciente
  this.lastReservation = [...this.userReservations]
    .sort((a, b) => new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime())[0];
}

// Añade estos métodos al final de la clase

getCategoryLabel(category: string): string {
  const categories: { [key: string]: string } = {
    'HAIR_SALON': 'Peluquería',
    'CLINIC': 'Clínica',
    'GYM': 'Gimnasio',
    'SPA': 'Spa',
    'AUTO_REPAIR': 'Taller mecánico',
    'RESTAURANT': 'Restaurante',
    'BAR': 'Bar',
    'STORE': 'Tienda',
    'SERVICE': 'Servicio'
  };
  return categories[category] || category;
}

getStatusLabel(status: string): string {
  const statuses: { [key: string]: string } = {
    'ACTIVE': 'Activo',
    'INACTIVE': 'Inactivo',
    'PENDING': 'Pendiente',
    'SUSPENDED': 'Suspendido'
  };
  return statuses[status] || status;
}

extractTime(reservation: Reservation): string {
  try {
    const date = new Date(reservation.startDateTime);
    return date.toLocaleTimeString('es-ES', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
  } catch {
    return '--:--';
  }
}

extractDuration(reservation: Reservation): string {
  const differenceInMs = new Date(reservation.endDateTime).getTime() - new Date(reservation.startDateTime).getTime();
  const totalMinutes = Math.floor(differenceInMs / (1000 * 60));

  // Si es menos de 90 minutos, mostrar solo minutos
  if (totalMinutes < 90) {
    return `${totalMinutes} mins`;
  }
  
  // Si es 90 minutos o más, calcular horas y minutos
  const hours = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  
  // Formatear según si quedan minutos restantes
  if (minutes === 0) {
    return `${hours}h`;
  } else {
    return `${hours}h ${minutes} mins`;
  }
}

extractDate(isoString: string): string {
  try {
    const date = new Date(isoString);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  } catch {
    return '--/--/----';
  }
}
}

