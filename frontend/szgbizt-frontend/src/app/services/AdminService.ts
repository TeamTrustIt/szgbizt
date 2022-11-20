import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class AdminService {

  private baseUrl = environment.apiBaseUrl + "/admin";

  constructor(private http: HttpClient) { }


}
