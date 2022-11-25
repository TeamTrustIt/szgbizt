import {CaffComment} from "./caffComment";
export interface CaffData {
  id: string,
  filename: string,
  description: string,
  price: number,
  username: string,
  urlImage: string,
  uploadDate: Date

  comments: CaffComment[]
}
