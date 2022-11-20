import {CaffDataNoCommentDto} from "./caff-data-no-comment-dto";

export interface UserEditDto {
  id: number,
  name: string,
  email: string,
  role: string,
  caffs: CaffDataNoCommentDto[]
}
