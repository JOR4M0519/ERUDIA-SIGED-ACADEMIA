import axios from "axios";
import authHelper from "./auth_helper";
import config from "./config";

export const getAuthToken = () => {
  return window.localStorage.getItem("auth_token");
};

export const setAuthHeader = (token, role="",idRole="") => {
  window.localStorage.setItem("auth_token", token);
  window.localStorage.setItem("role", authHelper.encryptRole(role));
  window.localStorage.setItem("idRole", authHelper.encryptRole(idRole));
};

axios.defaults.baseURL = config.apiBaseUrl;
axios.defaults.headers.post["Content-Type"] = "application/json";

export const request = (method, url, data) => {


  let headers = {};
  if (getAuthToken() !== null && getAuthToken() !== "null") {
    headers = { Authorization: `Bearer ${getAuthToken()}` };
  }

  if (data && data.token != null) {
    headers.token = data.token;
  }
  try{
    return axios({
      method: method,
      url: url,
      headers: headers,
      data: data,
      withCredentials: false,
    });
  }catch(error){
  }
  
};
