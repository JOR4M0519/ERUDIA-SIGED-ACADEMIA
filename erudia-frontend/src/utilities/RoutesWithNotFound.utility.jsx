import { Route, Routes } from 'react-router-dom';

function RoutesWithNotFound({ children }) {
  return (
    <Routes>
      {children}
      <Route path="*" element={<div>Not Found</div>} />
    </Routes>
  );
}
export default RoutesWithNotFound;