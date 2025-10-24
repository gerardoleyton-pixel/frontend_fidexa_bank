import React from 'react'
import { render, screen, waitFor } from '@testing-library/react'
import { vi, test, expect } from 'vitest'
import AccountList from '../src/components/Account/AccountList'
import accountsService from '../src/services/accounts'
import { BrowserRouter } from 'react-router-dom'
import { NotificationProvider } from '../src/components/Notification/NotificationContext'
import { AuthProvider } from '../src/components/Auth/AuthContext'

vi.mock('../src/services/accounts')

const fakeAccounts = [{ id:1, accountHolder:'Juan', accountNumber:'123', balance:100.5 }]

test('renders accounts list', async ()=>{
  accountsService.getAll.mockResolvedValue(fakeAccounts)
  // also ensure getByUser exists for the new behavior
  accountsService.getByUser = accountsService.getByUser || vi.fn()
  accountsService.getByUser.mockResolvedValue(fakeAccounts)
  // simulate logged-in user so component fetches accounts by user
  localStorage.setItem('fidexa_user', JSON.stringify({ id: 1, email: 'test@example.com' }))
  render(
    <AuthProvider>
      <NotificationProvider>
        <BrowserRouter>
          <AccountList />
        </BrowserRouter>
      </NotificationProvider>
    </AuthProvider>
  )

  expect(screen.getByText(/Cargando cuentas/i)).toBeTruthy()
  await waitFor(()=>{
    expect(screen.getByText('Juan')).toBeTruthy()
    expect(screen.getByText(/123/)).toBeTruthy()
  })
})
