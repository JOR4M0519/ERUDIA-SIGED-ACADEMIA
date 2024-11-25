import CryptoJS from "crypto-js";
import config from "./config";

const authHelper = {
  encryptRole: (role) => {
    const encryptedRole = CryptoJS.AES.encrypt(role.toString(), config.encryptionKey).toString();
    return encryptedRole;
  },

  getEncryptRole: () => {
    let role = window.localStorage.getItem("role")
    return role;
  },

  getEncryptIdRole: () => {
    let role = window.localStorage.getItem("idRole")
    return role;
  },

  setIdRole: (idRole) =>{
    const encryptedRole = CryptoJS.AES.encrypt(idRole, config.encryptionKey).toString();
    window.localStorage.setItem("idRole")
    return encryptedRole;
  },

  getAuthToken: () => {
    return window.localStorage.getItem("auth_token");
  },

  decryptRole: (encryptedRole) => {
    try{
      const decryptedRole = CryptoJS.AES.decrypt(encryptedRole, config.encryptionKey);
      return decryptedRole.toString(CryptoJS.enc.Utf8);
    }catch(error){
      return null;
    }
  },
};

export default authHelper;
