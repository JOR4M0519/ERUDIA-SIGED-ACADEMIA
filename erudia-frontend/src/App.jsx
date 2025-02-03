import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "./login/LoginPage";
import { Dashboard }  from "./Dashboard/Dashboard" 
import { Layout }  from "./components/Layout" 

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={  
          <Layout>
            <Dashboard />
          </Layout>
          } />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
