import React from 'react'
import { render, screen } from '@testing-library/react'
import { test, expect } from 'vitest'
import Header from './Header'
import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from '../Auth/AuthContext'

test('renders header', ()=>{
  render(
    <AuthProvider>
      <BrowserRouter>
        <Header />
      </BrowserRouter>
    </AuthProvider>
  )
  // header now uses a logo image and a home icon button
  expect(screen.getByAltText(/Fidexa/i)).toBeTruthy()
})
