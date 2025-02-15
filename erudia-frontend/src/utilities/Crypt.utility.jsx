import CryptoJS from "crypto-js";
import { persistStorage } from "./webStorage.utility";
import { UserInfo } from "../models";
import config from "./config.utility";

/**
 * Encripta la Data.
 */
export const encryptData = (data) => {
    return CryptoJS.AES.encrypt(data, config.encryptionKey).toString();
};

/**
 * Desencripta la Data.
 */
export const decryptData = (encryptedData) => {
    return CryptoJS.AES.decrypt(encryptedData, config.encryptionKey).toString(CryptoJS.enc.Utf8);
};


/**
 * Codifica la Data.
 */
export const encodeData = (data) => {
    return CryptoJS.SHA256(data).toString();
};


/**
 * Obtiene los roles desencriptados desde sessionStorage.
 */
export const decodeRoles = (roles) => {
    try {
            const rolesDecoded = roles.map(role => decryptData(role)); 
            return rolesDecoded
    } catch (error) {
        console.error("Error al desencriptar los roles:", error);
        return [];
    }
};


/**
* Guarda los roles encriptados en sessionStorage. Desuso
*/
export const storeEncryptedRoles = (roles) => {
    const encryptedRoles = roles.map(role => encryptData(role));
    persistStorage(UserInfo.rol, JSON.stringify(encryptedRoles));
};
