import { Component, OnInit, HostListener, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

interface NavigationItem {
  id: string;
  label: string;
  icon?: string;
  route: string;
  badge?: number;
}

interface User {
  id?: string;
  name?: string;
  email: string;
  businessName?: string;
  avatarInitials?: string;
}

interface Notification {
  id: string;
  message: string;
  icon: string;
  time: string;
  read: boolean;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatBadgeModule,
    MatTooltipModule,
    MatDividerModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './header.html',
  styleUrls: ['./header.scss']
})
export class HeaderComponent implements OnInit {
  @Input() user: User | null = null;
  @Input() isLoading: boolean = false;
  
  isMobile = false;
  notificationCount = 3;
  
  navigationItems: NavigationItem[] = [
    { id: 'dashboard', label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
    { id: 'calendar', label: 'Agenda', icon: 'calendar_month', route: '/calendar', badge: 2 },
    { id: 'clients', label: 'Clientes', icon: 'people', route: '/clients' },
    { id: 'services', label: 'Servicios', icon: 'design_services', route: '/services' },
    { id: 'reports', label: 'Reportes', icon: 'bar_chart', route: '/reports' }
  ];
  
  notifications: Notification[] = [
    { id: '1', message: 'Nueva reserva confirmada', icon: 'event_available', time: 'Hace 5 min', read: false },
    { id: '2', message: 'Recordatorio: Cita mañana a las 10:00', icon: 'notifications_active', time: 'Hace 2 horas', read: true },
    { id: '3', message: 'Cliente canceló su turno', icon: 'cancel', time: 'Ayer', read: false }
  ];

  ngOnInit() {
    this.checkScreenSize();
  }

  @HostListener('window:resize', [])
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth <= 768;
  }

  getAvatarText(): string {
    if (!this.user) return '?';
    
    if (this.user.avatarInitials) {
      return this.user.avatarInitials;
    }
    
    if (this.user.name) {
      return this.user.name.charAt(0).toUpperCase();
    }
    
    return this.user.email.charAt(0).toUpperCase();
  }

  onLogoClicked() {
    console.log('Logo clicked');
    // this.router.navigate(['/dashboard']);
  }

  onNavItemClicked(id: string) {
    console.log('Navigation item clicked:', id);
  }

  onProfileAction(action: string) {
    console.log('Profile action:', action);
    
    switch (action) {
      case 'logout':
        // this.authService.logout();
        alert('Cerrando sesión...');
        break;
      case 'account':
        // this.router.navigate(['/account']);
        break;
      case 'settings':
        // this.router.navigate(['/settings']);
        break;
      // ... otras acciones
    }
  }

  markAllAsRead() {
    this.notifications.forEach(n => n.read = true);
    this.notificationCount = 0;
  }

  viewAllNotifications() {
    console.log('View all notifications');
  }

  navigateToRegister() {
    console.log('Navigate to register');
    // this.router.navigate(['/register']);
  }

  navigateToLogin() {
    console.log('Navigate to login');
    // this.router.navigate(['/login']);
  }
}