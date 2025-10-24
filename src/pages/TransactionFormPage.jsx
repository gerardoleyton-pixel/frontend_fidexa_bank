import React from 'react'
import { useParams } from 'react-router-dom'
import TransactionForm from '../components/Transaction/TransactionForm'

export default function TransactionFormPage(){
  const { id, mode } = useParams()
  // mode expected: deposito | retiro | transferencia
  const map = { deposito: 'deposit', retiro: 'withdraw', transferencia: 'transfer' }
  const m = map[mode] || 'deposit'
  return (
    <main className="container">
      <TransactionForm mode={m} accountId={id} />
    </main>
  )
}
