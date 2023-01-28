import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./pages/Layout";
import Home from "./pages/Home";
import Reports from "./pages/Reports";
import Users from "./pages/Users";
import Customers from "./pages/Customers";
import Addresses from './pages/Addresses';
import NoPage from "./pages/NoPage";


export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="Reports" element={<Reports />} />
          <Route path="Users" element={<Users />} />
          <Route path="Customers" element={<Customers />} />
          <Route path="Addresses" element={<Addresses />} />
          <Route path="*" element={<NoPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

ReactDOM.render(<App />, document.getElementById("root"));

