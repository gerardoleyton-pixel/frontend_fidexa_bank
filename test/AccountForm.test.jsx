import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { describe, it, vi, expect } from 'vitest'
import AccountForm from '../src/components/Account/AccountForm'
import { AuthProvider } from '../src/components/Auth/AuthContext'
import { NotificationProvider } from '../src/components/Notification/NotificationContext'

vi.mock('../src/services/accounts', () => ({
  default: { create: vi.fn(() => Promise.resolve({ id: 123 })) }
}))

describe('AccountForm', ()=>{
  it('shows error when required fields missing', async ()=>{
    render(
      <MemoryRouter>
        <NotificationProvider>
          <AuthProvider>
            <AccountForm />
          </AuthProvider>
        </NotificationProvider>
      </MemoryRouter>
    )
  const createButtons = screen.getAllByRole('button', { name: /Crear Cuenta/i })
  fireEvent.click(createButtons[0])
    expect(await screen.findByText(/Titular y número son obligatorios/i)).toBeTruthy()
  })

  it('submits successfully when fields present', async ()=>{
    render(
      <MemoryRouter>
        <NotificationProvider>
          <AuthProvider>
            <AccountForm />
          </AuthProvider>
        </NotificationProvider>
      </MemoryRouter>
    )
    // inputs have no for/id relationship; select by class
    const inputs = document.querySelectorAll('.input')
    // order: Titular, Número de cuenta, Saldo inicial
    fireEvent.change(inputs[0], { target: { value: 'Prueba' } })
    fireEvent.change(inputs[1], { target: { value: '12345' } })
  const createButtons = screen.getAllByRole('button', { name: /Crear Cuenta/i })
  fireEvent.click(createButtons[0])
    // the NotificationProvider shows a success message on success
    expect(await screen.findByText(/Cuenta creada/i)).toBeTruthy()
  })
})
