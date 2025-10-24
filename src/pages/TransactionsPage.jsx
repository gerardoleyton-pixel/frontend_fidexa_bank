import React from 'react'
import { useParams } from 'react-router-dom'
import TransactionList from '../components/Transaction/TransactionList'

export default function TransactionsPage(){
  const { id } = useParams()
  return (
    <main className="container">
      <TransactionList accountId={id} />
    </main>
  )
}
