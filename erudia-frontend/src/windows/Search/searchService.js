import { BehaviorSubject } from "rxjs";

// Estado reactivo del modal (cerrado por defecto)
const searchModalSubject = new BehaviorSubject(false);

export const searchService = {
  open: () => searchModalSubject.next(true),
  close: () => searchModalSubject.next(false),
  getStatus: () => searchModalSubject.asObservable(),
};
