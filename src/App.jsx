import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Header from './components/Header/Header'
import AccountsPage from './pages/AccountsPage'
import NewAccountPage from './pages/NewAccountPage'
import AccountDetailPage from './pages/AccountDetailPage'
import TransactionsPage from './pages/TransactionsPage'
import TransactionFormPage from './pages/TransactionFormPage'
import LoginPage from './pages/LoginPage'
import WelcomePage from './pages/WelcomePage'
import RegisterPage from './pages/RegisterPage'
import AdminLoginPage from './pages/AdminLoginPage'
import AdminPage from './pages/AdminPage'
import ProtectedRoute from './components/Auth/ProtectedRoute'
import NotFoundPage from './pages/NotFoundPage'

export default function App(){
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<WelcomePage/>} />
        <Route path="/register" element={<RegisterPage/>} />
  <Route path="/login" element={<LoginPage/>} />
  <Route path="/cuentas" element={<ProtectedRoute><AccountsPage/></ProtectedRoute>} />
  <Route path="/cuentas/nueva" element={<ProtectedRoute><NewAccountPage/></ProtectedRoute>} />
  <Route path="/cuentas/:id" element={<ProtectedRoute><AccountDetailPage/></ProtectedRoute>} />
  <Route path="/transacciones/cuenta/:id" element={<ProtectedRoute><TransactionsPage/></ProtectedRoute>} />
  <Route path="/transacciones/cuenta/:id/:mode" element={<ProtectedRoute><TransactionFormPage/></ProtectedRoute>} />
  <Route path="/admin/login" element={<AdminLoginPage/>} />
  <Route path="/admin" element={<AdminPage/>} />
        <Route path="*" element={<NotFoundPage/>} />
      </Routes>
    </div>
  )
}
