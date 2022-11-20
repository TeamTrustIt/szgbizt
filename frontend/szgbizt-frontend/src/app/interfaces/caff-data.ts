import {CaffComment} from "./caffComment";
export interface CaffData {
  id: number,
  file: string,
  name: string,
  description: string,
  price: number,
  authorName: string,
  uploadDate: Date,
  comments: CaffComment[]
}
