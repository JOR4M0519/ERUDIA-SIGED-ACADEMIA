import { BehaviorSubject } from "rxjs";

// Estado reactivo del modal (cerrado por defecto)
const searchModalSubject = new BehaviorSubject(false);

export const searchService = {
  open: () => searchModalSubject.next(true),
  close: () => searchModalSubject.next(false),
  getStatus: () => searchModalSubject.asObservable(),
};


export const storeFilterOptions = (key, value) => {
  sessionStorage.setItem(key, JSON.stringify({ ...value }));
  };
  
export const clearFilterOptions = (key) => {
  sessionStorage.removeItem(key);
};

export const getstoreFilterOptions = (key) => {
  sessionStorage.getItem(key);
};