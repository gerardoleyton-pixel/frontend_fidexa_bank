import React, { useState } from 'react'
import transactionsService from '../../services/transactions'
import { useNotification } from '../Notification/NotificationContext'
import { useNavigate } from 'react-router-dom'

export default function TransactionForm({ mode='deposit', accountId }){
  const [amount, setAmount] = useState('')
  const [destino, setDestino] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { notify } = useNotification()
  const nav = useNavigate()

  async function handleSubmit(e){
    e.preventDefault()
    if(!amount || isNaN(parseFloat(amount))){
      notify('Monto inválido','error')
      return
    }
    setSubmitting(true)
    try{
      if(mode === 'deposit'){
        await transactionsService.deposit({ accountId, amount: parseFloat(amount) })
      } else if(mode === 'withdraw'){
        await transactionsService.withdraw({ accountId, amount: parseFloat(amount) })
      } else if(mode === 'transfer'){
        if(!destino) { notify('Cuenta destino obligatoria','error'); return }
        await transactionsService.transfer({ fromAccountId: accountId, toAccountId: destino, amount: parseFloat(amount) })
      }
      notify('Operación realizada','success')
      nav(`/transacciones/cuenta/${accountId}`)
    }catch(e){
      notify(e.message || 'Error realizando operación','error')
    }finally{setSubmitting(false)}
  }

  return (
    <div>
      <h3>{ mode === 'deposit' ? 'Depósito' : mode === 'withdraw' ? 'Retiro' : 'Transferencia' }</h3>
      <form className="form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Monto</label>
          <input className="input" value={amount} onChange={e=>setAmount(e.target.value)} type="number" step="0.01" />
        </div>
        {mode === 'transfer' && (
          <div className="form-row">
            <label>Cuenta destino (ID)</label>
            <input className="input" value={destino} onChange={e=>setDestino(e.target.value)} />
          </div>
        )}
        <div style={{display:'flex',gap:8,alignItems:'center'}}>
          <button className="btn" type="submit" disabled={submitting}>{submitting? 'Procesando...': 'Enviar'}</button>
          <button type="button" className="nav-button" onClick={()=>nav(`/transacciones/cuenta/${accountId}`)}>Ver cuenta</button>
        </div>
      </form>
    </div>
  )
}
