import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private apiService: UserService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(6),
        this.passwordStrengthValidator
      ]],
      confirmPassword: ['', [Validators.required]],
      name: ['', [Validators.required, Validators.minLength(2)]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9+\-\s()]{8,15}$/)]]
    }, { validators: this.passwordMatchValidator });
  }

  // Validador de fortaleza de contraseña
  private passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumber = /[0-9]/.test(value);

    const errors: ValidationErrors = {};
    
    if (!hasUpperCase) errors['uppercase'] = true;
    if (!hasLowerCase) errors['lowercase'] = true;
    if (!hasNumber) errors['number'] = true;

    return Object.keys(errors).length > 0 ? errors : null;
  }

  // Validador para confirmar contraseña
  private passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  // Helpers para mostrar errores en el template
  showError(controlName: string): boolean {
    const control = this.registerForm.get(controlName);
    return !!(control?.invalid && control?.touched);
  }

  getErrorMessage(controlName: string): string {
    const control = this.registerForm.get(controlName);
    
    if (!control?.errors) return '';
    
    if (control.errors['required']) return 'Este campo es requerido';
    if (control.errors['email']) return 'Email inválido';
    if (control.errors['minlength']) {
      const requiredLength = control.errors['minlength'].requiredLength;
      return `Mínimo ${requiredLength} caracteres`;
    }
    if (control.errors['pattern']) {
      if (controlName === 'phone') return 'Teléfono inválido';
    }
    if (control.errors['uppercase']) return 'Debe contener al menos una mayúscula';
    if (control.errors['lowercase']) return 'Debe contener al menos una minúscula';
    if (control.errors['number']) return 'Debe contener al menos un número';
    
    return 'Campo inválido';
  }

  getPasswordStrengthMessage(): string {
    const errors = this.registerForm.get('password')?.errors;
    if (!errors) return '';
    
    const messages = [];
    if (errors['uppercase']) messages.push('mayúscula');
    if (errors['lowercase']) messages.push('minúscula');
    if (errors['number']) messages.push('número');
    
    return messages.length > 0 
      ? `Falta: ${messages.join(', ')}`
      : '';
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      // Marcar todos los campos como tocados para mostrar errores
      Object.keys(this.registerForm.controls).forEach(key => {
        const control = this.registerForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    // Preparar datos para enviar (sin confirmPassword)
    const formData = { ...this.registerForm.value };
    delete formData.confirmPassword;

    console.log('Registrando usuario:', formData);

    // Simular registro exitoso
    setTimeout(() => {
      this.isLoading = false;
      this.successMessage = '¡Registro exitoso! Redirigiendo al login...';
      
      // Redirigir después de 2 segundos
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    }, 1500);
  }

  // Para mostrar/ocultar contraseña
  showPassword = false;
  showConfirmPassword = false;

  togglePasswordVisibility(field: 'password' | 'confirmPassword') {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }
}