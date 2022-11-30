export interface UserLoginDto {
  id: string,
  username: string,
  email: string,
  roles: 'ROLE_USER' | 'ROLE_ADMIN'
}
