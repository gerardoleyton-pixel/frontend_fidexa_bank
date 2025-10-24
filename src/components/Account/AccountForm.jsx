import React, { useState } from 'react'
import accountsService from '../../services/accounts'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../Auth/AuthContext'
import { useNotification } from '../Notification/NotificationContext'

export default function AccountForm(){
  const [titular, setTitular] = useState('')
  const [numero, setNumero] = useState('')
  const [saldoInicial, setSaldoInicial] = useState('0.00')
  const [submitting, setSubmitting] = useState(false)
  const { user } = useAuth()
  const { notify } = useNotification()
  const nav = useNavigate()

  async function handleSubmit(e){
    e.preventDefault()
    if(!titular || !numero){
      notify('Titular y número son obligatorios','error')
      return
    }
    const payload = {
      accountHolder: titular,
      accountNumber: numero,
      initialBalance: parseFloat(saldoInicial) || 0,
      userId: user ? user.id : undefined
    }
    setSubmitting(true)
    try{
      await accountsService.create(payload)
      notify('Cuenta creada','success')
      nav('/cuentas')
    }catch(e){
      notify(e.message || 'Error creando cuenta','error')
    }finally{setSubmitting(false)}
  }

  return (
    <div>
      <h2>Registrar nueva cuenta</h2>
      <form className="form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Titular</label>
          <input className="input" value={titular} onChange={e=>setTitular(e.target.value)} />
        </div>
        <div className="form-row">
          <label>Número de cuenta</label>
          <input className="input" value={numero} onChange={e=>setNumero(e.target.value)} />
        </div>
        <div className="form-row">
          <label>Saldo inicial</label>
          <input className="input" type="number" step="0.01" value={saldoInicial} onChange={e=>setSaldoInicial(e.target.value)} />
        </div>
        <div style={{display:'flex',gap:8}}>
          <button className="btn" type="submit" disabled={submitting}>{submitting? 'Creando...':'Crear Cuenta'}</button>
          <button className="btn" type="button" onClick={()=> nav(-1)}>Volver</button>
        </div>
      </form>
    </div>
  )
}
