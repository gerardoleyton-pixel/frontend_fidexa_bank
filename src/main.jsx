import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import './index.css'
import { NotificationProvider } from './components/Notification/NotificationContext'
import { ConfirmProvider } from './components/Confirm/ConfirmContext'
import { AuthProvider } from './components/Auth/AuthContext'

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <NotificationProvider>
        <ConfirmProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </ConfirmProvider>
      </NotificationProvider>
    </AuthProvider>
  </React.StrictMode>
)
