import {CaffDataNoCommentDto} from "./caff-data-no-comment-dto";

export interface UserEditDto {
  id: string,
  username: string,
  email: string,
  roles: 'ROLE_USER' | 'ROLE_ADMIN',
  balance: number,
  caffData: CaffDataNoCommentDto[]
}
