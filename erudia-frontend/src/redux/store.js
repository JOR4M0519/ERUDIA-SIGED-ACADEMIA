import {configureStore} from "@reduxjs/toolkit"
import userSliceReducer from './states/user';
  
export default configureStore({
    reducer: {
      user: userSliceReducer
    }
});



// export class AppStore {
//   user;
// }


// export default configureStore<AppStore>({
//   reducer: {
//     user: userSliceReducer
//   }
// });

