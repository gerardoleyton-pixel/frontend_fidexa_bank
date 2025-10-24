import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { describe, it, vi, expect } from 'vitest'
import LoginPage from '../src/pages/LoginPage'
import { AuthProvider } from '../src/components/Auth/AuthContext'
import { NotificationProvider } from '../src/components/Notification/NotificationContext'

describe('LoginPage', ()=>{
  it('shows validation error when fields empty', async ()=>{
    render(
      <MemoryRouter>
        <NotificationProvider>
          <AuthProvider>
            <LoginPage />
          </AuthProvider>
        </NotificationProvider>
      </MemoryRouter>
    )
  fireEvent.click(screen.getByRole('button', { name: /Ingresar/i }))
    expect(await screen.findByText(/Email y contraseña son obligatorios/i)).toBeTruthy()
  })
})
