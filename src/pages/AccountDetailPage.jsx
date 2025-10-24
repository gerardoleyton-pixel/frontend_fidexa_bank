import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import accountsService from '../services/accounts'
import TransactionList from '../components/Transaction/TransactionList'
import { useNotification } from '../components/Notification/NotificationContext'

export default function AccountDetailPage(){
  const { id } = useParams()
  const [cuenta, setCuenta] = useState(null)
  const [loading, setLoading] = useState(true)
  const { notify } = useNotification()

  useEffect(()=>{
    let mounted = true
    async function load(){
      try{
        const c = await accountsService.getById(id)
        if(mounted) setCuenta(c)
      }catch(e){
        notify(e.message || 'Error al obtener cuenta','error')
      }finally{ if(mounted) setLoading(false) }
    }
    load()
    return ()=> mounted = false
  },[id])

  if(loading) return <div className="loading">Cargando cuenta...</div>
  if(!cuenta) return <div className="form">Cuenta no encontrada</div>

  return (
    <main className="container">
      <h2>Detalle de cuenta #{cuenta.id}</h2>
      <div className="form-row">Titular: {cuenta.accountHolder}</div>
      <div className="form-row">Número: {cuenta.accountNumber}</div>
      <div className="form-row">Saldo: ${Number(cuenta.balance).toFixed(2)}</div>

      <TransactionList accountId={cuenta.id} />
    </main>
  )
}
