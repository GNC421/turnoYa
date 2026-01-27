export interface User {
  id: string;
  email: string;
  name: string;
  phone: string;
  avatar?: string;
  hasBusinesses: boolean;
  businessCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface UserStats {
  email: string;
  name: string;
  phone: string;
  avatar?: string;
  booksFailed: number;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  phone: string;
}

export interface UpdateProfileRequest {
  name?: string;
  phone?: string;
  avatar?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}