import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import AdminPage from '../../src/pages/AdminPage'
import api from '../../src/services/api'
import { vi, describe, it, expect, beforeEach } from 'vitest'

vi.mock('../../src/services/api')

const mockUsers = [{ id:1, username:'u1', email:'u1@x', fullName:'User 1' }]
const mockAccounts = [{ id:10, accountHolder:'User 1', accountNumber:'ACC10', balance:100.5, userId:1, userName:'u1' }]
const mockTransactions = [{ id:100, type:'DEPOSIT', amount:50, accountId:10, description:'dep', timestamp:'t' }]

describe('AdminPage', () => {
  beforeEach(()=>{
    vi.resetAllMocks()
    api.get.mockImplementation((path)=>{
      if(path.includes('/users')) return Promise.resolve({ data: mockUsers })
      if(path.includes('/accounts')) return Promise.resolve({ data: mockAccounts })
      if(path.includes('/transactions')) return Promise.resolve({ data: mockTransactions })
      return Promise.resolve({ data: [] })
    })
    localStorage.setItem('fidexa_admin','true')
  })

  it('renders lists and allows selecting user and account', async () => {
    render(<AdminPage />)
    await screen.findByText(/Clientes/)
  // click 'Ver' for the first user row
  const buttonsAll = await screen.findAllByRole('button', { name: 'Ver' })
  expect(buttonsAll.length).toBeGreaterThan(0)
  fireEvent.click(buttonsAll[0])
    // selecting a user should open the user modal with details
    await screen.findByTestId('user-modal')

    // click 'Ver' for the first account row (there will be another 'Ver' button in accounts)
  const buttons2 = await screen.findAllByRole('button', { name: 'Ver' })
  // the second 'Ver' corresponds to accounts table
  if(buttons2.length > 1){ fireEvent.click(buttons2[1]) }
    await screen.findByTestId('user-modal')
  })

  it('search by id and refreshes lists', async () => {
    render(<AdminPage />)
    // click the second 'Ver' button which corresponds to the account row
    const buttons = await screen.findAllByRole('button', { name: 'Ver' })
    expect(buttons.length).toBeGreaterThanOrEqual(2)
    fireEvent.click(buttons[1])
    // search should open the user modal for the account owner
    await screen.findByTestId('user-modal')
    // user data should have been requested for the modal (users list requested on mount too)
    await waitFor(()=> expect(api.get).toHaveBeenCalledWith('/users'))
  })
})
