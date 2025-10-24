import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import TransactionList from '../components/Transaction/TransactionList'
import accountsService from '../services/accounts'
import { useNotification } from '../components/Notification/NotificationContext'

function HeaderAccount({ cuenta, onBack }){
  if(!cuenta) return null
  return (
    <div style={{display:'flex',justifyContent:'space-between',alignItems:'center',marginBottom:12}}>
      <div>
        <div style={{fontWeight:700}}>{cuenta.accountHolder} — {cuenta.accountNumber}</div>
        <div>Saldo: ${Number(cuenta.balance).toFixed(2)}</div>
      </div>
      <div>
        <button className="nav-button" onClick={onBack}>Volver</button>
      </div>
    </div>
  )
}

export default function TransactionsPage(){
  const { id } = useParams()
  const [cuenta, setCuenta] = useState(null)
  const { notify } = useNotification()
  const nav = useNavigate()

  useEffect(()=>{
    let mounted = true
    async function load(){
      try{
        const c = await accountsService.getById(id)
        if(mounted) setCuenta(c)
      }catch(e){ notify(e.message || 'Error cargando cuenta','error') }
    }
    load()
    return ()=> mounted = false
  },[id])

  return (
    <main className="container">
      <HeaderAccount cuenta={cuenta} onBack={()=>nav(`/cuentas/${id}`)}/>
      <TransactionList accountId={id} />
    </main>
  )
}
